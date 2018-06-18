package de.hdm.kontaktsystem.server;

import java.util.Iterator;
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
	private double currentUser = 510d;
	
	public void setCurrentUser(double uid){ // Test um den User zu wechseln
		currentUser = uid;
	}
	
	public double getCurrentUser() {
		// Test
		return currentUser;// Double.parseDouble(userService.getCurrentUser().getUserId());
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, User
	 * ***************************************************************************
	 */
	// Login
	// ## IO ##
	public User login(String requestUri) {

		UserService userService = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User guser = userService.getCurrentUser();
		User user = new User();
		Contact own = new Contact();
		PropertyValue name = new PropertyValue();
		PropertyValue email = new PropertyValue();

		if (guser != null) {

			double id = Double.parseDouble(guser.getUserId());
			user = UserMapper.userMapper().findById(id);
			

			if (user == null) {
				
				System.out.println("Create new User: " + user);
				user = new User();
				user.setGoogleID(id);
				user.setGMail(guser.getEmail());
//				user.setNickname(guser.getNickname()); // Not used
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
			user.setLoggedIn(true); // norm True
			user.setLogoutUrl(userService.createLogoutURL(requestUri));

		} else {

			user.setLoggedIn(false);
			user.setLoginUrl(userService.createLoginURL(requestUri));

		}

		return user;

	}

	@Override
	public User getAccountOwner() {
		return getUserByID(this.getCurrentUser());
	}

	/**
	 * Interner Aufruf bei Login
	 * @param u
	 * @param contact
	 * @return
	 */
	// ## IO ##
	public User createUser(User u, Contact contact) {

		User user = uMapper.insert(u);
		contact.setOwner(user);

		user.setUserContact(this.createContact(contact));
		return this.editUser(user);

	}
	/**
	 * Benutzt für Login & BO Owner
	 */
	// ## IO ##
	@Override
	public User getUserByID(double id) {
		User user = uMapper.findById(id);
		user.setUserContact(this.getOwnContact(user));
		return user;

	}
	
	/**
	 * Benutzt zum Teilen
	 */
	// ## IO ##
	@Override
	public User getUserBygMail(String email) {
		User user = uMapper.findByEmail(email);
		user.setUserContact(this.getOwnContact(user));
		return user;

	}
	
	/**
	 * Nur für Report!
	 */
	// ## IO ##
	@Override
	public Vector<User> getAllUsers() {
		Vector<User> userVector = uMapper.findAll();
		for (User user : userVector) {
			user.setUserContact(this.getOwnContact(user));
		}
		return userVector;

	}

	/**
	 * Intern verwendet um die eigene KontaktID nach zu tragen
	 * @param user
	 * @return
	 */
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


	// TODO Nocheinmal Absprechen
	@Override
	public Contact createContact(Contact contact) {
		if(contact.getOwner() == null) contact.setOwner(this.getUserByID(this.getCurrentUser()));
		boMapper.insert(contact);
		Contact c = cMapper.insertContact(contact);
		Vector<PropertyValue> pvv = new Vector<PropertyValue>();
		for(PropertyValue pv : contact.getPropertyValues()){
			pv.setContact(c);
			pv.setOwner(c.getOwner());
			pvv.add(this.createPropertyValue(pv));
		}
		c.setPropertyValues(pvv);
		return c;
	
	}
	
	// #### IO ####
	@Override
	public Contact editContact(Contact contact) {
		boMapper.update(contact);
		Vector<PropertyValue> newPV = contact.getPropertyValues();
		Vector<PropertyValue> oldPV = this.getPropertyValuesForContact(contact); // Liste uim Iterieren
		Vector<PropertyValue> oldPV2 = oldPV; // Liste aus der Elemente gelöscht werden
		// Löscht alle PropertyValues die es in dem neuen Eigenschafts Vector nicht mehr gibt
		for (PropertyValue pV : oldPV) {
			if(!newPV.contains(pV)) {
				this.deletePropertyValue(pV);
			}
		}
		// Fügt dem Kontakt neue PropertyValues hinzu
		// Updatet die bestehenden PropertyValues
		for (PropertyValue pV : newPV) {
			if(!oldPV.contains(pV)) {
				this.createPropertyValue(pV);
			}else{
				this.editPropertyValue(pV);
			}
			
		}
		return this.getContactById(contact.getBoId());

	}
	
	/**
	 * Nur Intern Verwendet
	 * Anzeige des User Namen
	 * @param u
	 * @return
	 */
	public Contact getOwnContact(User u) {
		Contact contact = cMapper.findOwnContact(u);
		if(contact != null){
			contact.setName(this.getNameOfContact(contact));
		}
		return contact;
	}
	

	/**
	 * Verwendet in Report
	 */
	@Override
	public Vector<Contact> getAllContactsFromUser()  {
		User user = this.getUserByID(this.getCurrentUser());
		Vector<Contact> cv = cMapper.findAllContactsByUser(user);
		for (Contact contact : cv) {
			contact.setOwner(this.getUserByID(contact.getOwner().getGoogleID()));
			contact.setName(this.getNameOfContact(contact));
			contact.setPropertyValues(this.getPropertyValuesForContact(contact));
		}
		return cv;
	}
	
	
	/**
	 * Vorschau für TreeView
	 */
	// TODO alles geteilten Kontakte anzeigen
	@Override
	public Vector<Contact> getMyContactsPrev()  {
		User user = this.getUserByID(this.getCurrentUser());
		Vector<Contact> cv = cMapper.findAllContactsByUser(user);
		
		for (Contact contact : cv) {
			contact.setName(this.getNameOfContact(contact));
		}
		//cv.addAll(this.getAllCSharedByOthersToMePrev());
		return cv;
	}

	

	/**
	 * Verwendet im TreeView und ContactListForm
	 */
	// ### IO ###
	@Override
	public Vector<Contact> getContactsFromList(ContactList cl) {
		Vector<Integer> iv = cMapper.findContactFromList(cl);
		Vector<Contact> cv = new Vector<Contact>();
		if (iv != null) {
			for (int i : iv) {
				Contact c = cMapper.findContactById(i);
				c.setName(this.getNameOfContact(c));
				cv.add(c);
			}
		}
		return cv;

	}

	/**
	 * Verwendet in ContactForm und Intern
	 */
	
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
	public Vector<BusinessObject> search(String value) {

		Vector<BusinessObject> cv = new Vector<BusinessObject>();
		// Findet alle Kontakte
		Vector<PropertyValue> pvv = propValMapper.findByValue(value);
		for (PropertyValue pv : pvv) {
			Contact c = cMapper.findBy(pv);
			contact.setName(this.getNameOfContact(contact));
			if (!cv.contains(c))
				cv.add(c);

		}
		// Findet alle KontaktListen
		//cv.addAll(clMapper.findContactListByName(value));
		
		return cv;
	}

	
	@Override
	public Contact getContactByPropertyValue(PropertyValue pv) {
		Contact contact = cMapper.findBy(pv);
		contact.setOwner(this.getUserByID(contact.getOwner().getGoogleID()));
		contact.setName(this.getNameOfContact(contact));
		contact.setPropertyValues(this.getPropertyValuesForContact(contact));
		return contact;
	}

	
	// TODO Überprüfen
	@Override
	public Contact deleteContact(Contact contact) {
		Contact c = null;
		if(contact.getOwner().getGoogleID() == this.getCurrentUser()){
			for (PropertyValue pv : this.getPropertyValuesForContact(contact)) {
				this.deletePropertyValue(pv);
			}
			c = cMapper.deleteContact(contact);
			if (c != null)
				boMapper.deleteBusinessObjectByID(c.getBoId());
		}else{
			Participation part = new Participation();
			User user = this.getUserByID(this.getCurrentUser());
			part.setParticipant(user);
			part.setReference(contact);
			part = this.deleteParticipation(part);
			if(part != null){
				c = contact;
				for(PropertyValue pv : c.getPropertyValues()){
					part = new Participation();
					part.setParticipant(user);
					part.setReference(pv);
					this.deleteParticipation(part);
				}
			}
		}
		return c;
	}

	/**
	 * Mapper-Methode um alle Kontakte eines bestimmten Users mittels der User-ID zu
	 * loeschen
	 * 
	 */
	
	public void deleteAllContactsByUser() {

		Vector<Contact> result = new Vector<Contact>();
		// Aufrufen aller Kontakte eines bestimmten Users
		result = this.getAllContactsFromUser(); // TODO Überprüfen
		for (Contact c : result) {
			this.deleteContact(c);
		}
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, ContactList
	 * ***************************************************************************
	 */

	// TODO zusammenlegen mit Edit ? 
	@Override
	public ContactList createContactList(ContactList contactList) {
		if(contactList.getOwner() == null) contactList.setOwner(this.getUserByID(this.getCurrentUser()));
		boMapper.insert(contactList);
		return clMapper.insertContactList(contactList);

	}

	@Override
	public ContactList editContactList(ContactList cl) {
		boMapper.update(cl);
		return clMapper.updateContactList(cl);
	}



	// Nur für Report!
	// TODO Überprüfen ob benötigt
	@Override
	public Vector<ContactList> getAllContactListsFromUser() {

		Vector<ContactList> contactListVector = clMapper.findContactListByUserId(this.getCurrentUser());

		for (ContactList cl : contactListVector) {
			cl.setOwner(this.getUserByID(cl.getOwner().getGoogleID()));
			cl.setContacts(this.getContactsFromList(cl));
		}
		return contactListVector;
	}
	
	// ## IO ##
	public Vector<ContactList> getMyContactListsPrev() {

		Vector<ContactList> contactListVector = clMapper.findContactListByUserId(this.getCurrentUser());
		// Alle geteilten Listen
		//contactListVector.addAll(this.getAllCLSharedByOthersToMePrev());
		return contactListVector;
	}

	// ### IO ###
	@Override
	public ContactList getContactListById(int id) {
		ContactList contactList = clMapper.findContactListById(id);
		if (contactList != null) {
			contactList.setOwner(getUserByID(contactList.getOwner().getGoogleID()));
			contactList.setContacts(getContactsFromList(contactList));
		}
		return contactList;
	}
	
	// TODO Überprüfen
	@Override
	public ContactList deleteContactList(ContactList contactList) {
		ContactList cl = null;
		if(contactList.getOwner().getGoogleID() == this.getCurrentUser()){
			cl = clMapper.deleteContactList(contactList);
			if (cl != null)
				boMapper.deleteBusinessObjectByID(cl.getBoId());
		}else{
			Participation p = new Participation();
			p.setParticipant(this.getUserByID(this.getCurrentUser()));
			p.setReference(contactList);
			p = this.deleteParticipation(p);
			if(p != null) cl = contactList;
		}
		return cl;
	}

	/**
	 * Es löscht alle KontaktListen, welche zu einer User ID gehören.
	 * 
	 * @param UserId
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
	/**
	 * Intern Verwendet in EDIT / Create Contact
	 * @param propertyValue
	 * @return
	 */
	public PropertyValue createPropertyValue(PropertyValue propertyValue) {
		// Da Property immer fest zu einem Contact-Objekt gehört hat es auch den selben
		// Besitzer

		propertyValue.setOwner(propertyValue.getContact().getOwner());
		boMapper.insert(propertyValue);
		PropertyValue pv = propValMapper.insert(propertyValue);
		// Eigenschaft wird automatisch geteilt, wenn der Ersteller nicht der Kontaktbesitzer ist
		if(pv.getOwner().getGoogleID() != this.getCurrentUser()){
			Participation p = new Participation();
			p.setParticipant(this.getAccountOwner());
			p.setReference(pv);
			this.createParticipation(p);
		}
		return pv;
	}
	
	/**
	 * Intern Verwendet in EDIT / Create Contact
	 * @param propertyValue
	 * @return
	 */
	public PropertyValue editPropertyValue(PropertyValue propertyValue) {
		boMapper.update(propertyValue);
		return propValMapper.update(propertyValue);
	}

/**
 * Keine Verwendung
 * @param propertyValue
 * @return
 */
	// TODO Entfernen ?? 
	public PropertyValue deletePropertyValue(PropertyValue propertyValue) {
		PropertyValue pv = propValMapper.delete(propertyValue);
		if (pv != null)
			boMapper.deleteBusinessObjectByID(pv.getBoId());
		return pv;
	}
	
	
	// TODO Überprüfen
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
	// TODO Aufräumen 
	public PropertyValue getNameOfContact(Contact contact) {
		PropertyValue name = new PropertyValue();
		Vector<PropertyValue> result = new Vector<PropertyValue>();
		result = this.getPropertyValuesForContact(contact);

		for (PropertyValue val : result) {
			if (val.getProperty().getId() == 1) {
				name = val;
			}
		}
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
	// TODO Verwendung Überprüfen
	public Vector<PropertyValue> searchPropertyValues(String suchtext) {
		Vector<PropertyValue> pvv = propValMapper.findByValue(suchtext);
		return pvv;
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Property
	 * ***************************************************************************
	 */
	@Override
	public Vector<Property> getAllProperties() {
		return propMapper.findAll();
	}
	
	@Override
	public Property getPropertyByID(int id) {
		return propMapper.findBy(id);
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Participation
	 * ***************************************************************************
	 */
	// TODO Überprüfen 
	@Override
	public Participation createParticipation(Participation part) {
		Participation participation = partMapper.insertParticipation(part);
		boMapper.setStatusTrue(participation.getReferenceID());
		if(part.getReferencedObject() instanceof Contact){
			for(PropertyValue pv : ((Contact) part.getReferencedObject()).getPropertyValues()){
				Participation partPV = new Participation();
				partPV.setParticipant(part.getParticipant());
				partPV.setReference(pv);
				this.createParticipation(partPV);
			}
		}
		return participation;

	}
	// TODO Überprüfen
	@Override
	public Participation editParticpation(Participation part){
		if(part.getReferencedObject() instanceof Contact){
			Contact c = (Contact) part.getReferencedObject();
			// Suchen von allen geteilten Eigenschaften 
			Vector<Participation> allShared = this.getAllParticipationsByOwner(this.getUserByID(this.getCurrentUser()));
			// Referenz Contact-Object um festzustellen was nicht geteilt wird
			Contact refC = this.getContactById(c.getBoId());
			// Kontrolle ob neue PropertyValues geteilt wurden
			for(PropertyValue pv : c.getPropertyValues()){
				Participation addPart = new Participation();
				addPart.setParticipant(part.getParticipant());
				addPart.setReference(pv);
				if(!allShared.contains(addPart)){
					this.createParticipation(addPart);
				}
			}
			// Löscht alle beziehungen zu PropertyValues die nicht mehr geteilt werden sollen.
			for(PropertyValue pv : refC.getPropertyValues()){
				if(!c.getPropertyValues().contains(pv)){
					Participation delPart = new Participation();
					delPart.setParticipant(part.getParticipant());
					delPart.setReference(pv);
					this.deleteParticipation(delPart);
				}
			}
			
			return part;
		}else if(part.getReferencedObject() instanceof ContactList){
			ContactList cl = (ContactList) part.getReferencedObject();
			// Kontaktlisten werden aktuell nur Vollständig geteilt
			return part;
		}else if(part.getReferencedObject() instanceof PropertyValue){
			// PropertyValues werden aktuell nicht geupdatet
			return part;
		}
		
		
		return null;
	}

	// Report
	// TODO Entfernen ?
	public Vector<Participation> getAllParticipations() {
		Vector<Participation> partV = partMapper.findAllParticipations();
		for (Participation part : partV) {
			part.setParticipant(this.getUserByID(part.getParticipantID()));
			part.setReference(this.getBusinessObjectByID(part.getReferenceID()));
		}
		return partV;
	}

	@Override
	public Vector<Participation> getAllParticipationsByOwner(User u) {
		Vector<Participation> partV = partMapper.findParticipationsByOwner(u);
		for (Participation part : partV) {
			part.setParticipant(this.getUserByID(part.getParticipantID()));
			part.setReference(this.getBusinessObjectByID(part.getReferenceID()));
		}
		return partV;
	}
	
	/**
	 * Intern Verwendet in FindAllSharedByOthersToMe
	 */
	@Override
	public Vector<Participation> getAllParticipationsByParticipant(User participant) {
		Vector<Participation> partV = partMapper.findParticipationsByParticipant(participant);
		for (Participation part : partV) {
			part.setParticipant(this.getUserByID(part.getParticipantID()));
			part.setReference(this.getBusinessObjectByID(part.getReferenceID()));
		}
		return partV;
	}

	public Vector<Participation> getAllParticipationsByBusinessObject(BusinessObject bo) {
		Vector<Participation> partV = partMapper.findParticipationsByBusinessObject(bo);
		for (Participation part : partV) {
			part.setParticipant(this.getUserByID(part.getParticipantID()));
			part.setReference(this.getBusinessObjectByID(part.getReferenceID()));
		}
		return partV;
	}

	/**
	 * Keine Verwendung
	 * @param p
	 * @return
	 */
	// TODO Entfernen??
	public Participation deleteParticipation(Participation p) {
		Participation part = partMapper.deleteParticipation(p);
		// Prüfen, ob es zu dem geteilten Objekt noch eine Teilhaberschaft gibt,
		// wenn nicht, Status (geteilt) des Objekt auf false setzen
		Vector<Participation> participations = this.getAllParticipationsByBusinessObject(part.getReferencedObject());
		if (participations.isEmpty()) {
			BusinessObjectMapper.businessObjectMapper().setStatusFalse(part.getReferenceID());
		}
		if(p.getReferencedObject() instanceof Contact){
			for(PropertyValue pv : ((Contact) p.getReferencedObject()).getPropertyValues()){
				Participation partPV = new Participation();
				partPV.setParticipant(p.getParticipant());
				partPV.setReference(pv);
				this.deleteParticipation(part);
			}
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
	// TODO Überarbeiten für Tree ???
	public BusinessObject getBusinessObjectByID(int id) {

		BusinessObject bo = null;

		if (bo == null)
			bo = this.getContactById(id);

		if (bo == null)
			bo = this.getContactListById(id);

		if (bo == null)
			bo = this.getPropertyValueById(id);

		return bo;
	}
	
	// TODO Alle Überabrieten und Aussortieren
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Find All Shared
	 * ***************************************************************************
	 */

	public Vector<BusinessObject> getAllSharedByOthersToMe() {
		Vector<BusinessObject> bov = new Vector<BusinessObject>();
		for (Contact c : this.getAllCSharedByOthersToMePrev()) {
			
			bov.add(c);
		}
		for (ContactList cl : this.getAllCLSharedByOthersToMePrev()) {
			
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

	public Vector<PropertyValue> getAllPVSharedByOthersToMe() {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln,
		// die von diesem geteilt wurden
		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = this.getAllParticipationsByParticipant(this.getUserByID(this.getCurrentUser()));
		// Vector für die Speicherung aller BusinessObjekte erzeugen
		Vector<PropertyValue> propertyResultVector = new Vector<PropertyValue>();

		for (Participation part : participationVector) {
			PropertyValue propVal = new PropertyValue();
			BusinessObject bo = this.getBusinessObjectByID(part.getReferenceID());

			if (bo instanceof PropertyValue) {
				propVal = (PropertyValue) bo;
				propertyResultVector.addElement(propVal);
			}
		}
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

		public Vector<Contact> getAllCSharedByMe (User user) {

			// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln, die von diesem geteilt wurden
			Vector<Participation> participationVector = new Vector<Participation>();		
			participationVector = this.getAllParticipationsByOwner(user);		
			Vector<Contact> contactResultVector = new Vector <Contact>(); 		
					
			for (Participation part : participationVector) {			 
				 BusinessObject bo = this.getBusinessObjectByID(part.getReferenceID());
				 Contact contact = new Contact();
				 
				 	if(bo instanceof Contact) {			 		
				 		contact = (Contact) bo;
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

		public Vector<ContactList> getAllCLSharedByMe (User user) {

			Vector<Participation> participationVector = new Vector<Participation>();		
			participationVector = this.getAllParticipationsByOwner(user);
					// Vector für die Speicherung aller BusinessObjekte erzeugen
					Vector<ContactList> contactListVector = new Vector <ContactList>(); 	
					
					for (Participation part : participationVector) { 		
						
						 BusinessObject bo = this.getBusinessObjectByID(part.getReferenceID());
						 ContactList contactList = new ContactList();	 
						 if(bo instanceof ContactList) {			 		
							 contactList = (ContactList) bo;
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
			propertyValueResult = this.getAllPVSharedByMe();
			
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

	public Vector<Contact> getAllCSharedByOthersToMe() {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln,
		// die von diesem geteilt wurden
		User user = this.getUserByID(this.getCurrentUser());
		Vector<Participation> participationVector = this.getAllParticipationsByParticipant(user);
		Vector<Contact> contactResultVector = new Vector<Contact>();

		for (Participation part : participationVector) {
			BusinessObject bo = this.getBusinessObjectByID(part.getReferenceID());
			Contact contact = new Contact();

			if (bo instanceof Contact) {
				contact = (Contact) bo;
				
				
				contactResultVector.addElement(this.filterContactData(contact));
			}
		}
		if (contactResultVector.isEmpty())
			System.out.println("# no contacts found");
		return contactResultVector;
	}
	
	/**
	 * Vorschau frü TreeView
	 * @return
	 */
	public Vector<Contact> getAllCSharedByOthersToMePrev() {

		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = this.getAllParticipationsByParticipant(this.getUserByID(this.getCurrentUser()));
		Vector<Contact> cResultVector = new Vector<Contact>();

		for (Participation part : participationVector) {
			Contact c = cMapper.findContactById(part.getReferenceID());
			if (c != null) {
				cResultVector.addElement(c);
			}
		}

		if (cResultVector.isEmpty())
			System.out.println("# no contactList found");

		return cResultVector;
	}
	
	private Contact filterContactData(Contact contact){
		// Filter damit nur die geteilten PropertyValues ausgegeben werden; Namen werden nicht gefiltert
		Iterator<PropertyValue> iterator = contact.getPropertyValues().iterator(); // Liste zum Iterieren
		Vector<PropertyValue> pvv = new Vector<PropertyValue>(); // Liste aus der Elemente gelöscht werden
		Vector<PropertyValue> allPVswm = this.getAllPVSharedByOthersToMe();
		
		while(iterator.hasNext()){
			PropertyValue pv = iterator.next();
			if(pv.getProperty().getId() == 1){
				pvv.add(pv);
			}else if(allPVswm.contains(pv)){
				pvv.add(pv);
			}else{
				System.out.println(pv +" not Shared");
				
			}
		}
		
		contact.setPropertyValues(pvv);
		return contact;
	}
	
	@Override
	public Vector<PropertyValue> getAllPVFromContactSharedWithUser(Contact c, User u){
		Vector<Participation> allSharedWithUser = this.getAllParticipationsByParticipant(u);
		Vector<PropertyValue> pvFromContact = c.getPropertyValues();
		Vector<PropertyValue> output = new Vector<PropertyValue>();
		
		for(Participation p : allSharedWithUser){
			if(pvFromContact.contains(p.getReferencedObject())){
				output.add((PropertyValue) p.getReferencedObject());
			}
			
		}
		return output;
	}
	
	/**
	 * Alle für den Benutzer in der Applikation geteilte Kontaktelisten
	 * <code>ContactList</code> -Objekte künnen über den Aufruf dieser Methode aus
	 * der DB zur�ck gegeben werden.
	 * 
	 * @param User-Objekt
	 * @return Vector ContactList-Objekte
	 */

	public Vector<ContactList> getAllCLSharedByOthersToMe() {

		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = this.getAllParticipationsByParticipant(this.getUserByID(this.getCurrentUser()));
		Vector<ContactList> clResultVector = new Vector<ContactList>();

		for (Participation part : participationVector) {

			BusinessObject bo = this.getBusinessObjectByID(part.getReferenceID());
			ContactList cl = new ContactList();

			if (bo instanceof ContactList) {
				cl = (ContactList) bo;
				
				clResultVector.addElement(cl);
			}
		}

		if (clResultVector.isEmpty())
			System.out.println("# no contactList found");

		return clResultVector;
	}
	/**
	 * Vorschau frü TreeView
	 * @return
	 */
	public Vector<ContactList> getAllCLSharedByOthersToMePrev() {

		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = this.getAllParticipationsByParticipant(this.getUserByID(this.getCurrentUser()));
		Vector<ContactList> clResultVector = new Vector<ContactList>();

		for (Participation part : participationVector) {
			ContactList cl = clMapper.findContactListById(part.getReferenceID());
			if (cl != null) {
				clResultVector.addElement(cl);
			}
		}

		if (clResultVector.isEmpty())
			System.out.println("# no contactList found");

		return clResultVector;
	}

	public Vector<BusinessObject> getAllSharedByMe() {
		Vector<BusinessObject> vbo = new Vector<BusinessObject>();
		for (Contact c : this.getAllCSharedByMe()) {
			vbo.add(c);
		}
		for (ContactList cl : this.getAllCLSharedByMe()) {
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

	public Vector<PropertyValue> getAllPVSharedByMe() {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln,
		// die von diesem geteilt wurden
		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = this.getAllParticipationsByOwner(this.getUserByID(this.getCurrentUser()));
		Vector<PropertyValue> propertyResultVector = new Vector<PropertyValue>();

		for (Participation part : participationVector) {

			BusinessObject bo = this.getBusinessObjectByID(part.getReferenceID());
			PropertyValue propVal = new PropertyValue();
			if (bo instanceof PropertyValue) {
				propVal = (PropertyValue) bo;
				propertyResultVector.addElement(propVal);
			}
		}
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

	public Vector<Contact> getAllCSharedByMe() {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln,
		// die von diesem geteilt wurden
		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = this.getAllParticipationsByOwner(this.getUserByID(this.getCurrentUser()));
		Vector<Contact> contactResultVector = new Vector<Contact>();

		for (Participation part : participationVector) {
			BusinessObject bo = this.getBusinessObjectByID(part.getReferenceID());
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

	public Vector<ContactList> getAllCLSharedByMe() {

		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = this.getAllParticipationsByOwner(this.getUserByID(this.getCurrentUser()));
		// Vector für die Speicherung aller BusinessObjekte erzeugen
		Vector<ContactList> contactListVector = new Vector<ContactList>();

		for (Participation part : participationVector) {
			BusinessObject bo = this.getBusinessObjectByID(part.getReferenceID());
			ContactList contactList = new ContactList();
			if (bo instanceof ContactList) {
				contactList = (ContactList) bo;
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
		propertyValueResult = this.getAllPVSharedByMe();

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
		contactResult = this.getAllCSharedByMe();

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
		clResult = this.getAllCLSharedByMe();

		for (ContactList cl : clResult) {
			ParticipationMapper.participationMapper().deleteParticipationForBusinessObject(cl);
			this.deleteContactList(cl);
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
		pvVector = this.getAllPVSharedByOthersToMe();
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
		contactResult = this.getAllCSharedByOthersToMe();

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
		clResult = this.getAllCLSharedByOthersToMe();

		for (ContactList cl : clResult) {
			ParticipationMapper.participationMapper().deleteParticipationForParticipant(user);
		}
	}
	
	

}