package de.hdm.kontaktsystem.server;

import java.util.Vector;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.kontaktsystem.shared.ContactSystemAdministration;
import de.hdm.kontaktsystem.shared.bo.*;
import de.hdm.kontaktsystem.server.db.*;

/**
 * <p>
 * Implementierungsklasse des Interface
 * <code>ContactSystemAdministration</code>. Diese Klasse ist <em>die</em>
 * Klasse, die neben {@link ReportGeneratorImpl} sämtliche Applikationslogik
 * (engl. Business Logic) aggregiert. Diese sorgt für einen geordneten Ablauf
 * und Konsistenz der Daten sowie Abläufe in der Applikation.
 * </p>
 * 
 * Jede Methode dieser Klasse bildet die Applikationslogik ab und kann als
 * <em>Transaction Script</em> bezeichnet werden. Diese überführen das System
 * von einem konsistenten Zustand in einen anderen über.
 * 
 * <li>{@link RemoteServiceServlet}: Jede Server-seitig instantiierbare und
 * Client-seitig über GWT RPC nutzbare Klasse muss die Klasse
 * <code>RemoteServiceServlet</code> implementieren. Sie legt die funktionale
 * Basis für die Anbindung von <code>ContactSystemImpl</code> an die Runtime
 * des GWT RPC-Mechanismus.</li>
 *
 */

public class ContactSystemAdministrationImpl extends RemoteServiceServlet implements ContactSystemAdministration {

	/**
	 * Default SerialVersionUID
	 */

	private static final long serialVersionUID = 1L;

	private Contact contact = null; // ?? TODO: überprüfen -> Analog zu BankProjekt

	/*
	 * Referenzen auf die zugehörigen DatenbankMapper
	 */

	private BusinessObjectMapper boMapper = null;
	private ContactListMapper clMapper = null;
	private ContactMapper cMapper = null;
	private ParticipationMapper partMapper = null;
	private PropertyMapper propMapper = null;
	private PropertyValueMapper propValMapper = null;
	private UserMapper uMapper = null;
	private UserService userService = null;

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Initialisierung
	 * ***************************************************************************
	 */

	public ContactSystemAdministrationImpl() throws IllegalArgumentException {

	}

	/**
	 * Methode zur Initialisierung
	 */

	public void init() throws IllegalArgumentException {

		this.boMapper = BusinessObjectMapper.businessObjectMapper();
		this.clMapper = ContactListMapper.contactListMapper();
		this.cMapper = ContactMapper.contactMapper();
		this.partMapper = ParticipationMapper.participationMapper();
		this.propMapper = PropertyMapper.propertyMapper();
		this.propValMapper = PropertyValueMapper.propertyValueMapper();
		this.uMapper = UserMapper.userMapper();
		this.userService = UserServiceFactory.getUserService();
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Methoden
	 * ***************************************************************************
	 */

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public double getCurrentUser() {
		// Test
		return 170d;// Double.parseDouble(userService.getCurrentUser().getUserId());
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, User
	 * ***************************************************************************
	 */
	// Login

	public User login(String requestUri) {

		UserService userService = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User guser = userService.getCurrentUser();
		User user = new User();
		Contact own = new Contact();
		PropertyValue name = new PropertyValue();
		PropertyValue email = new PropertyValue();

		if (guser != null) {

			double id = Double.parseDouble(guser.getUserId());
			user.setGoogleID(id);
			user.setGMail(guser.getEmail());
			// user.setNickname(guser.getNickname()); // Not used

			user.setLoggedIn(true); // norm True
			user.setLogoutUrl(userService.createLogoutURL(requestUri));

			if (UserMapper.userMapper().findById(id) == null) {
				System.out.println("Create new User: " + user);

				own.setBo_Id(1); // updated in db
				own.setOwner(user);
				own.setName(name);
				user = this.createUser(user, own);

				name.setContact(user.getUserContact());
				email.setContact(user.getUserContact());
				name.setProperty(this.getPropertyByID(1));
				email.setProperty(this.getPropertyByID(6));
				name.setValue(guser.getNickname()); // force initial name
				email.setValue(user.getGMail());
				this.createPropertyValue(name);
				this.createPropertyValue(email);

			} else {
				System.out.println("Login User: " + guser.getUserId() + " -> " + id);
			}

		} else {

			user.setLoggedIn(false);
			user.setLoginUrl(userService.createLoginURL(requestUri));

		}

		return user;

	}

	@Override
	public User createUser(User u, Contact contact) {

		User user = uMapper.insert(u);
		contact.setOwner(user);

		user.setUserContact(this.createContact(contact));
		return this.editUser(user);

	}

	@Override
	public User getUserByID(double id) {
		User user = uMapper.findById(id);
		user.setUserContact(this.getOwnContact(user));
		return user;

	}

	@Override
	public User getUserBygMail(String email) {
		User user = uMapper.findByEmail(email);
		user.setUserContact(this.getOwnContact(user));
		return user;

	}

	// Nur für Report!
	public Vector<User> getAllUsers() {
		Vector<User> userVector = uMapper.findAll();
		for (User user : userVector) {
			user.setUserContact(this.getOwnContact(user));
		}
		return userVector;

	}

	public User editUser(User user) {
		return uMapper.update(user);

	}

	@Override
	public User deleteUser(User user) {
		this.deleteAllContactsByUser();
		this.deleteContactListByUserId();
		return uMapper.delete(user);
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Contact
	 * ***************************************************************************
	 */

	// Nur für Report!
	public Vector<Contact> getAllContacts() {
		Vector<Contact> cv = cMapper.findAllContacts();
		for (Contact contact : cv) {
			contact.setOwner(this.getUserByID(contact.getOwner().getGoogleID()));
			contact.setName(this.getNameOfContact(contact));
			contact.setPropertyValues(this.getPropertyValuesForContact(contact));
		}
		return cv;

	}

	// Nur für Report!
	public Vector<Contact> getAllContactsFromUser() {
		return cMapper.findAllContactsByUser(this.getCurrentUser());
	}

	// Nur Intern Verwendet
	public Contact getOwnContact(User u) {
		Contact contact = cMapper.findOwnContact(u);
		contact.setName(this.getNameOfContact(contact));
		contact.setPropertyValues(this.getPropertyValuesForContact(contact));
		return contact;
	}

	@Override
	public Contact editContact(Contact contact) {
		Contact con = cMapper.updateContact(contact);
		Vector<PropertyValue> propResult = new Vector<PropertyValue>();
		propResult = this.getPropertyValuesForContact(con);
		for (PropertyValue pV : propResult) {
			this.editPropertyValue(pV);
		}
		return con;

	}

	public Contact getContactByPropertyValue(PropertyValue pv) {
		Contact contact = cMapper.findBy(pv);
		contact.setOwner(this.getUserByID(contact.getOwner().getGoogleID()));
		contact.setName(this.getNameOfContact(contact));
		contact.setPropertyValues(this.getPropertyValuesForContact(contact));
		return contact;
	}

	@Override
	public Vector<Contact> getContactsFromUser() {
		User user = this.getUserByID(getCurrentUser());
		Vector<Contact> cv = cMapper.findAllContactsByUser(user);
		for (Contact contact : cv) {
			contact.setOwner(user);
			contact.setPropertyValues(this.getPropertyValuesForContact(contact));
			contact.setName(this.getNameOfContact(contact));
		}
		return cv;
	}

	@Override
	public Vector<Contact> getContactsByStatus(Boolean status) {
		Vector<Contact> cv = cMapper.findContactByStatus(this.getCurrentUser(), status);
		for (Contact contact : cv) {
			contact.setOwner(this.getUserByID(this.getCurrentUser()));
			contact.setPropertyValues(this.getPropertyValuesForContact(contact));
			contact.setName(this.getNameOfContact(contact));
		}
		return cv;

	}

	public Vector<Contact> getContactsFromList(ContactList cl) {
		Vector<Integer> iv = cMapper.findContactFromList(cl);

		Vector<Contact> cv = new Vector<Contact>();
		if (iv != null) {
			for (int i : iv) {
				cv.add(this.getContactById(i));
			}
		}
		return cv;

	}

	@Override
	public Contact getContactById(int id) {
		Contact contact = cMapper.findContactById(id);
		if (contact != null) {
			contact.setPropertyValues(this.getPropertyValuesForContact(contact));
			contact.setName(this.getNameOfContact(contact));
			contact.setOwner(this.getUserByID(contact.getOwner().getGoogleID()));
		}
		return contact;

	}

	/**
	 * Gibt alle Contact-Objekte zurück die eine PropertyValue Besitzen, welche der
	 * Sucheingabe entspricht.
	 * 
	 * @param Sucheingabe
	 * @return Vector<Contact>
	 */
	@Override
	public Vector<Contact> searchContacts(String value) {

		Vector<Contact> cv = new Vector<Contact>();

		Vector<PropertyValue> pvv = propValMapper.findByValue(value);
		for (PropertyValue pv : pvv) {
			// pv.setProperty(propMapper.findBy(pv.getProperty().getId()));
			Contact c = this.getContactByPropertyValue(pv);
			if (!cv.contains(c))
				cv.add(c);

		}
		return cv;
	}

	@Override
	public Contact createContact(Contact contact) {

		boMapper.insert(contact);
		return cMapper.insertContact(contact);

	}

	@Override
	public Contact deleteContact(Contact contact) {
		for (PropertyValue pv : this.getPropertyValuesForContact(contact)) {
			this.deletePropertyValue(pv);
		}
		Contact c = cMapper.deleteContact(contact);
		if (c != null)
			boMapper.deleteBusinessObjectByID(c.getBoId());
		return c;
	}

	/**
	 * Mapper-Methode um alle Kontakte eines bestimmten Users mittels der User-ID zu
	 * loeschen
	 * 
	 * @param User
	 *            ID
	 */

	public void deleteAllContactsByUser() {

		Vector<Contact> result = new Vector<Contact>();
		// Aufrufen aller Kontakte eines bestimmten Users
		result = this.getAllContactsFromUser();
		for (Contact c : result) {
			deleteContact(c);
		}
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, ContactList
	 * ***************************************************************************
	 */
	@Override
	public ContactList createContactList(ContactList contactList) {
		boMapper.insert(contactList);
		return clMapper.insertContactList(contactList);

	}

	@Override
	public ContactList editContactList(ContactList cl) {
		boMapper.update(cl);
		return clMapper.updateContactList(cl);
	}

	@Override
	public Vector<ContactList> getContactListByName(String name) {

		Vector<ContactList> contactListVector = clMapper.findContactListByName(name);

		for (ContactList cl : contactListVector) {
			cl.setOwner(this.getUserByID(cl.getOwner().getGoogleID()));
			cl.setContacts(this.getContactsFromList(cl));
		}
		return contactListVector;

	}

	// Nur für Report!
	@Override
	public Vector<ContactList> getAllContactLists() {
		Vector<ContactList> contactListVector = clMapper.findAllContactLists();

		for (ContactList cl : contactListVector) {
			cl.setOwner(this.getUserByID(cl.getOwner().getGoogleID()));
			cl.setContacts(this.getContactsFromList(cl));
		}
		return contactListVector;
	}

	// Nur für Report!
	@Override
	public Vector<ContactList> getAllContactListsFromUser() {

		Vector<ContactList> contactListVector = clMapper.findContactListByUserId(this.getCurrentUser());

		for (ContactList cl : contactListVector) {
			cl.setOwner(this.getUserByID(cl.getOwner().getGoogleID()));
			cl.setContacts(this.getContactsFromList(cl));
		}
		return contactListVector;
	}

	@Override
	public ContactList getContactListById(int id) {
		ContactList contactList = clMapper.findContactListById(id);
		if (contactList != null) {
			contactList.setOwner(getUserByID(contactList.getOwner().getGoogleID()));
			contactList.setContacts(getContactsFromList(contactList));
		}
		return contactList;
	}

	@Override
	public ContactList deleteContactList(ContactList contactList) {
		ContactList cl = clMapper.deleteContactList(contactList);
		if (cl != null)
			boMapper.deleteBusinessObjectByID(cl.getBoId());
		return cl;
	}

	/**
	 * Es löscht alle Kontakte, welche zu einer User ID gehören.
	 * 
	 * @param User
	 *            Id
	 */

	public void deleteContactListByUserId() {

		for (ContactList cl : this.getAllContactListsFromUser()) {
			this.deleteContactList(cl);
		}
	}

	@Override
	public ContactList addContactToList(Contact contact, ContactList contactList) {
		return clMapper.addContactToContactlist(contactList, contact);

	}

	@Override
	public ContactList removeContactFromList(Contact contact, ContactList contactList) {
		return clMapper.removeContactFromContactList(contactList, contact);

	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, PropertyValue
	 * ***************************************************************************
	 */
	public PropertyValue createPropertyValue(PropertyValue propertyValue) {
		// Da Property immer fest zu einem Contact-Objekt gehört hat es auch den selben
		// Besitzer
		System.out.println("Eingenschaft: " + propertyValue.getValue());
		propertyValue.setOwner(propertyValue.getContact().getOwner());
		boMapper.insert(propertyValue);
		return propValMapper.insert(propertyValue);
	}

	public PropertyValue editPropertyValue(PropertyValue propertyValue) {
		boMapper.update(propertyValue);
		return propValMapper.update(propertyValue);
	}

	@Override
	public PropertyValue deletePropertyValue(PropertyValue propertyValue) {
		PropertyValue pv = propValMapper.delete(propertyValue);
		if (pv != null)
			boMapper.deleteBusinessObjectByID(pv.getBoId());
		return pv;
	}

	public Vector<PropertyValue> getPropertyValuesForContact(Contact c) {

		Vector<PropertyValue> pvv = propValMapper.findBy(c);

		for (PropertyValue pv : pvv) {
			pv.setProperty(this.getPropertyByID(pv.getProperty().getId()));
		}
		return pvv;

	}

	public PropertyValue getPropertyValueById(int id) {
		return propValMapper.findByKey(id);
	}

	/**
	 * Anhand der uebergegebenen ID wird das zugehoerige PropertyValue - Objekt,
	 * welches der Eigenschaft "Name" zugewiesen werden kann, eindeutig
	 * identifiziert und zurueckgegeben
	 * 
	 * @param Contact-Objekt
	 * @return PropertyValue - Objekt
	 */

	public PropertyValue getNameOfContact(Contact contact) {
		//// System.out.println("#PV -findName");
		PropertyValue name = new PropertyValue();
		Vector<PropertyValue> result = new Vector<PropertyValue>();
		result = this.getPropertyValuesForContact(contact);

		for (PropertyValue val : result) {

			// System.out.println("propertyVal id: " + val.getBo_Id());
			// System.out.println("propertyVal description: " +
			// val.getProp().getDescription());

			if (val.getProperty().getId() == 1) {
				name = val;
				// System.out.println("Contact name:" + val.getProperty().getPropertyValues());
			}
		}
		//// System.out.println("Name: " + name);
		return name;

	}

	/**
	 * Anhand des übergebenen Strings werden alle PropertyValue - Objekte
	 * identifiziert, deren Wert dem Suchtext entspricht und zurückgegeben.
	 * 
	 * @param suchtext
	 *            Gesuchter Wert
	 * @return Vector mit PropertyValue - Objekten
	 */
	public Vector<PropertyValue> searchPropertyValues(String suchtext) {
		Vector<PropertyValue> pvv = propValMapper.findByValue(suchtext);
		return pvv;
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Property
	 * ***************************************************************************
	 */

	public Vector<Property> getAllProperties() {
		return propMapper.findAll();
	}

	public Property getPropertyByID(int id) {
		return propMapper.findBy(id);
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Participation
	 * ***************************************************************************
	 */

	@Override
	public Participation createParticipation(Participation part) {
		Participation participation = partMapper.insertParticipation(part);
		boMapper.setStatusTrue(participation.getReferenceID());
		return participation;

	}

	// Report
	public Vector<Participation> getAllParticipations() {
		Vector<Participation> partV = partMapper.findAllParticipations();
		for (Participation part : partV) {
			part.setParticipant(this.getUserByID(part.getParticipantID()));
			part.setReference(this.findBusinessObjectByID(part.getReferenceID()));
		}
		return partV;
	}

	@Override
	public Vector<Participation> getAllParticipationsByOwner(User u) {
		Vector<Participation> partV = partMapper.findParticipationsByOwner(u);
		for (Participation part : partV) {
			part.setParticipant(this.getUserByID(part.getParticipantID()));
			part.setReference(this.findBusinessObjectByID(part.getReferenceID()));
		}
		return partV;
	}

	@Override
	public Vector<Participation> getAllParticipationsByParticipant(User participant) {
		Vector<Participation> partV = partMapper.findParticipationsByParticipant(participant);
		for (Participation part : partV) {
			part.setParticipant(this.getUserByID(part.getParticipantID()));
			part.setReference(this.findBusinessObjectByID(part.getReferenceID()));
		}
		return partV;
	}

	public Vector<Participation> getAllParticipationsByBusinessObject(BusinessObject bo) {
		Vector<Participation> partV = partMapper.findParticipationsByBusinessObject(bo);
		for (Participation part : partV) {
			part.setParticipant(this.getUserByID(part.getParticipantID()));
			part.setReference(this.findBusinessObjectByID(part.getReferenceID()));
		}
		return partV;
	}

	@Override
	public Participation deleteParticipation(Participation p) {
		Participation part = partMapper.deleteParticipation(p);
		// Prüfen, ob es zu dem geteilten Objekt noch eine Teilhaberschaft gibt,
		// wenn nicht, Status (geteilt) des Objekt auf false setzen
		Vector<Participation> participations = this.getAllParticipationsByBusinessObject(part.getReferencedObject());
		if (participations.isEmpty()) {
			BusinessObjectMapper.businessObjectMapper().setStatusFalse(part.getReferenceID());
		}
		return part;

	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, BusinessObject
	 * ***************************************************************************
	 */

	/**
	 * Gibt ein BusinessObject vom Typ Contact, ContctList oder PropertyValue
	 * zurück
	 * 
	 * @param BusinessObject
	 *            ID
	 * @return Contact, ContactList, PropertyValue
	 */

	public BusinessObject findBusinessObjectByID(int id) {

		BusinessObject bo = null;

		if (bo == null)
			bo = this.getContactById(id);

		if (bo == null)
			bo = this.getContactListById(id);

		if (bo == null)
			bo = this.getPropertyValueById(id);

		return bo;
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Find All Shared
	 * ***************************************************************************
	 */

	public Vector<BusinessObject> getAllSharedByOthersToMe() {
		Vector<BusinessObject> bov = new Vector<BusinessObject>();
		for (Contact c : this.findAllCSharedByOthersToMe()) {
			bov.add(c);
		}
		for (ContactList cl : this.findAllCLSharedByOthersToMe()) {
			bov.add(cl);
		}
		return bov;

	}

	/**
	 * Alle fuer den Benutzer in der Applikation geteilte Ausprägungen
	 * <code>PropertyValue</code> Objekte können über den Aufruf dieser Methode
	 * aus der DB zurück gegeben werden.
	 * 
	 * @param User-Objekt
	 * @return Vector PropertyValue-Objekte
	 */

	public Vector<PropertyValue> findAllPVSharedByOthersToMe() {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln,
		// die von diesem geteilt wurden
		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = this.getAllParticipationsByParticipant(this.getUserByID(this.getCurrentUser()));
		// Vector für die Speicherung aller BusinessObjekte erzeugen
		Vector<PropertyValue> propertyResultVector = new Vector<PropertyValue>();

		for (Participation part : participationVector) {
			PropertyValue propVal = new PropertyValue();
			BusinessObject bo = this.findBusinessObjectByID(part.getReferenceID());
			//// System.out.println("pov-id: " + propVal.getBo_Id());

			if (bo instanceof PropertyValue) {
				propVal = (PropertyValue) bo;
				//// System.out.println("Ausprägung " + propVal.getProp());
				propertyResultVector.addElement(propVal);
			}
		}
			//System.out.println(propertyResultVector);
			return propertyResultVector;		
		
		}
		
		/**
		 *  Alle für den Benutzer in der Applikation zugaenglichen Kontakte <code>Contact</code> - Objekte
		 * (diese sind selbst erstellt und anderen zur Teilhaberschaft freigegeben) werden anhand ihres Status gesucht
		 *  und als ein Ergebnissvektor aus Contact-objekten zurueckgegeben. 
		 *  
		 *  @param User-Objekt
		 *  @return Vector mit allen geteilten Contact-Objekten
		 */

		public Vector<Contact> findAllCSharedByMe (User user) {

			// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln, die von diesem geteilt wurden
			Vector<Participation> participationVector = new Vector<Participation>();		
			participationVector = this.getAllParticipationsByOwner(user);		
			Vector<Contact> contactResultVector = new Vector <Contact>(); 		
					
			for (Participation part : participationVector) {
				 System.out.println("part id:" + part.getReferenceID());			 
				 BusinessObject bo = this.findBusinessObjectByID(part.getReferenceID());
				 Contact contact = new Contact();
				 
				 	if(bo instanceof Contact) {			 		
				 		contact = (Contact) bo;
				 		System.out.println("contact name " + contact.getName());
				 		contactResultVector.addElement(contact);	     
				 }		
			}	 	
			if(contactResultVector.isEmpty()) System.out.println("# no contacts found");			
			
			return contactResultVector;
			
		}
		
		/**
		 *  Alle für den Benutzer in der Applikation zugaenglichen Kontakte <code>Contact</code> - Objekte
		 * (diese sind selbst erstellt und anderen zur Teilhaberschaft freigegeben) werden anhand ihres Status gesucht
		 *  und als ein Ergebnissvektor aus Contact-objekten zurueckgegeben. 
		 *  
		 *  @param User-Objekt
		 *  @return Vector<ContactList>
		 */

		public Vector<ContactList> findAllCLSharedByMe (User user) {

			Vector<Participation> participationVector = new Vector<Participation>();		
			participationVector = this.getAllParticipationsByOwner(user);
					// Vector für die Speicherung aller BusinessObjekte erzeugen
					Vector<ContactList> contactListVector = new Vector <ContactList>(); 		
					//System.out.println(participationVector);
					
					for (Participation part : participationVector) { 		
						
						//System.out.println(part);
						 BusinessObject bo = this.findBusinessObjectByID(part.getReferenceID());
						 //System.out.println(bo);
						 ContactList contactList = new ContactList();	 
						 //System.out.println(propVal); 	
						 if(bo instanceof ContactList) {			 		
							 contactList = (ContactList) bo;
						 		//System.out.println("Ausprägung " + propVal.getProp());
							 contactListVector.addElement(contactList);		     
						 }
					}
					return contactListVector;
					
				}
		
	
		
		/**
		 * Methode zur L�schung aller von einem User erstellten Ausprägungen, Ownership und Participation!
		 * 
		 * @param User-Objekt
		 */
		
		public void deleteAllPVSharedByMe(User user) {
			
			Vector <PropertyValue> propertyValueResult = new Vector <PropertyValue>();
			propertyValueResult = this.findAllPVSharedByMe();
			
			for(PropertyValue pV : propertyValueResult) {
				// loeschen aller Eintr�ge in der Teilhaberschaft Tabelle Participation
				ParticipationMapper.participationMapper().deleteParticipationForBusinessObject(pV);
				this.deletePropertyValue(pV);
		}

	}

	/**
	 * Alle für den Benutzer in der Applikation geteilte Kontakte
	 * <code>Contact</code> Objekte können über den Aufruf dieser Methode aus der
	 * DB zurück gegeben werden.
	 * 
	 * @param User-Objekt
	 * @return Vector Contact-Objekte
	 */

	public Vector<Contact> findAllCSharedByOthersToMe() {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln,
		// die von diesem geteilt wurden
		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = this.getAllParticipationsByParticipant(this.getUserByID(this.getCurrentUser()));
		Vector<Contact> contactResultVector = new Vector<Contact>();

		for (Participation part : participationVector) {
			BusinessObject bo = this.findBusinessObjectByID(part.getReferenceID());
			Contact contact = new Contact();

			if (bo instanceof Contact) {
				contact = (Contact) bo;
				contactResultVector.addElement(contact);
			}
		}
		if (contactResultVector.isEmpty())
			System.out.println("# no contacts found");
		return contactResultVector;
	}

	/**
	 * Alle für den Benutzer in der Applikation geteilte Kontaktelisten
	 * <code>ContactList</code> -Objekte künnen über den Aufruf dieser Methode aus
	 * der DB zur�ck gegeben werden.
	 * 
	 * @param User-Objekt
	 * @return Vector ContactList-Objekte
	 */

	public Vector<ContactList> findAllCLSharedByOthersToMe() {

		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = this.getAllParticipationsByParticipant(this.getUserByID(this.getCurrentUser()));
		Vector<ContactList> clResultVector = new Vector<ContactList>();

		for (Participation part : participationVector) {
			// System.out.println("part id:" + part.getReferenceID());

			BusinessObject bo = this.findBusinessObjectByID(part.getReferenceID());
			// System.out.println(bo.getClass());
			ContactList cl = new ContactList();
			// System.out.println("bo gefunden: " + bo.getBo_Id());

			if (bo instanceof ContactList) {
				cl = (ContactList) bo;
				// System.out.println("contactList name " + bo);
				clResultVector.addElement(cl);
			}
		}

		if (clResultVector.isEmpty())
			System.out.println("# no contactList found");

		return clResultVector;
	}

	public Vector<BusinessObject> getAllSharedByMe() {
		Vector<BusinessObject> vbo = new Vector<BusinessObject>();
		for (Contact c : this.findAllCSharedByMe()) {
			vbo.add(c);
		}
		for (ContactList cl : this.findAllCLSharedByMe()) {
			vbo.add(cl);
		}
		return vbo;

	}

	/**
	 * Alle fuer den Benutzer in der Applikation zugaenglichen Auspraegungen
	 * <code>PropertyValue</code> - Objekte (diese sind selbst erstellt und anderen
	 * zur Teilhaberschaft freigegeben) werden anhand ihres Status gesucht und die
	 * Ergebnisse zurueckgegeben
	 * 
	 * @param User-Objekt
	 * @return Vector<PropertyValue>
	 */

	public Vector<PropertyValue> findAllPVSharedByMe() {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln,
		// die von diesem geteilt wurden
		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = this.getAllParticipationsByOwner(this.getUserByID(this.getCurrentUser()));
		// Vector für die Speicherung aller BusinessObjekte erzeugen
		Vector<PropertyValue> propertyResultVector = new Vector<PropertyValue>();
		// System.out.println(participationVector);

		for (Participation part : participationVector) {

			// System.out.println(part);
			BusinessObject bo = this.findBusinessObjectByID(part.getReferenceID());
			// System.out.println(bo);
			PropertyValue propVal = new PropertyValue();
			//// System.out.println(propVal);
			if (bo instanceof PropertyValue) {
				propVal = (PropertyValue) bo;
				propertyResultVector.addElement(propVal);
			}
		}
		// System.out.println(propertyResultVector);
		return propertyResultVector;
	}

	/**
	 * Alle für den Benutzer in der Applikation zugaenglichen Kontakte
	 * <code>Contact</code> - Objekte (diese sind selbst erstellt und anderen zur
	 * Teilhaberschaft freigegeben) werden anhand ihres Status gesucht und als ein
	 * Ergebnissvektor aus Contact-objekten zurueckgegeben.
	 * 
	 * @param User-Objekt
	 * @return Vector mit allen geteilten Contact-Objekten
	 */

	public Vector<Contact> findAllCSharedByMe() {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln,
		// die von diesem geteilt wurden
		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = this.getAllParticipationsByOwner(this.getUserByID(this.getCurrentUser()));
		Vector<Contact> contactResultVector = new Vector<Contact>();

		for (Participation part : participationVector) {
			BusinessObject bo = this.findBusinessObjectByID(part.getReferenceID());
			Contact contact = new Contact();

			if (bo instanceof Contact) {
				contact = (Contact) bo;
				contactResultVector.addElement(contact);
			}
		}
		if (contactResultVector.isEmpty())
			System.out.println("# no contacts found");

		return contactResultVector;

	}

	/**
	 * Alle für den Benutzer in der Applikation zugaenglichen Kontakte
	 * <code>Contact</code> - Objekte (diese sind selbst erstellt und anderen zur
	 * Teilhaberschaft freigegeben) werden anhand ihres Status gesucht und als ein
	 * Ergebnissvektor aus Contact-objekten zurueckgegeben.
	 * 
	 * @param User-Objekt
	 * @return Vector<ContactList>
	 */

	public Vector<ContactList> findAllCLSharedByMe() {

		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = this.getAllParticipationsByOwner(this.getUserByID(this.getCurrentUser()));
		// Vector für die Speicherung aller BusinessObjekte erzeugen
		Vector<ContactList> contactListVector = new Vector<ContactList>();
		// System.out.println(participationVector);

		for (Participation part : participationVector) {

			// System.out.println(part);
			BusinessObject bo = this.findBusinessObjectByID(part.getReferenceID());
			// System.out.println(bo);
			ContactList contactList = new ContactList();
			// System.out.println(propVal);
			if (bo instanceof ContactList) {
				contactList = (ContactList) bo;
				// System.out.println("Ausprägung " + propVal.getProp());
				contactListVector.addElement(contactList);
			}
		}
		return contactListVector;

	}

	/**
	 * Methode zur L�schung aller von einem User erstellten Ausprägungen,
	 * Ownership und Participation!
	 * 
	 * @param User-Objekt
	 */

	public void deleteAllPVSharedByMe() {

		Vector<PropertyValue> propertyValueResult = new Vector<PropertyValue>();
		propertyValueResult = this.findAllPVSharedByMe();

		for (PropertyValue pV : propertyValueResult) {
			// loeschen aller Eintr�ge in der Teilhaberschaft Tabelle Participation
			ParticipationMapper.participationMapper().deleteParticipationForBusinessObject(pV);
			this.deletePropertyValue(pV);
		}
	}

	/**
	 * Methode zur Löschung aller von einem User erstellten Kontakte
	 * <code>Contact</code> Objekte, welche im System mit anderen Nutzern geteilt
	 * wurden.
	 * 
	 * @param User-Objekt
	 */

	public void deleteAllCSharedByMe(User user) {

		Vector<Contact> contactResult = new Vector<Contact>();
		contactResult = this.findAllCSharedByMe();

		for (Contact contact : contactResult) {
			ParticipationMapper.participationMapper().deleteParticipationForBusinessObject(contact);
			this.deleteContact(contact);
			System.out.println("# shared contact deleted: " + contact.getBoId());
		}

	}

	/**
	 * Methode zur Löschung aller von einem User erstellten Kontaktlisten
	 * <code>ContactList</code> -Objekte, welche im System mit anderen Nutzern
	 * geteilt wurden.
	 * 
	 * @param User
	 */

	public void deleteAllCLSharedByMe(User user) {

		Vector<ContactList> clResult = new Vector<ContactList>();
		clResult = this.findAllCLSharedByMe();

		for (ContactList cl : clResult) {
			ParticipationMapper.participationMapper().deleteParticipationForBusinessObject(cl);
			this.deleteContactList(cl);
			// System.out.println("# shared contactList deleted: " + cl.getBo_Id() );

		}
	}

	/**
	 * Funktion zum Löschen aller Auspraegungen die vom User geteilt wurden
	 * 
	 * @param User-Objekt
	 */

	public void deleteAllPVSharedByOthersToMe(User u) {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln,
		// die von diesem geteilt wurden
		Vector<PropertyValue> pvVector = new Vector<PropertyValue>();
		pvVector = this.findAllPVSharedByOthersToMe();
		for (PropertyValue pv : pvVector) {
			ParticipationMapper.participationMapper().deleteParticipationForParticipant(u);
		}
	}

	/**
	 * Eine Methode zur Loeschung aller Verbindungen in der Participation Tabelle
	 * der DB. Dies fuehrt dazu, dass urspruenglich fuer einen Nutzer geteilten
	 * Objekte von diesem nicht mehr aufgerufen werden koennen.
	 * 
	 * @param User-Objekt
	 */

	public void deleteAllCSharedByOthersToMe(User user) {

		Vector<Contact> contactResult = new Vector<Contact>();
		contactResult = this.findAllCSharedByOthersToMe();

		for (Contact contact : contactResult) {
			ParticipationMapper.participationMapper().deleteParticipationForParticipant(user);
		}
	}

	/**
	 * Eine Methode zur Löschung aller Verbindungen in der Participation Tabelle
	 * der DB. Dies führt dazu, dass die für einen Nutzer geteilten Objekte nicht
	 * mehr aufgerufen werden können. Die Teilhaberschaft ist damit beendet.
	 * 
	 * @param User-Objekt
	 */

	public void deleteAllCLSharedByOthersToMe(User user) {

		Vector<ContactList> clResult = new Vector<ContactList>();
		clResult = this.findAllCLSharedByOthersToMe();
		// System.out.println(clResult);

		for (ContactList cl : clResult) {
			ParticipationMapper.participationMapper().deleteParticipationForParticipant(user);
			// System.out.println("# participation for contact deleted: " + cl.getBo_Id() );

		}
	}

}