package de.hdm.kontaktsystem.server;

import java.util.Comparator;
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
 * Klasse, die neben {@link ReportGeneratorImpl} saemtliche Applikationslogik
 * (engl. Business Logic) aggregiert. Diese sorgt fuer einen geordneten Ablauf
 * und Konsistenz der Daten sowie Ablaeufe in der Applikation.
 * </p>
 * 
 * Jede Methode dieser Klasse bildet die Applikationslogik ab und kann als
 * <em>Transaction Script</em> bezeichnet werden. Diese ueberfuehren das System
 * von einem konsistenten Zustand in einen anderen ueber.
 * 
 * <li>{@link RemoteServiceServlet}: Jede Server-seitig instantiierbare und
 * Client-seitig ueber GWT RPC nutzbare Klasse muss die Klasse
 * <code>RemoteServiceServlet</code> implementieren. Sie legt die funktionale
 * Basis fuer die Anbindung von <code>ContactSystemImpl</code> an die Runtime
 * des GWT RPC-Mechanismus.</li>
 *
 */

public class ContactSystemAdministrationImpl extends RemoteServiceServlet implements ContactSystemAdministration {

	/**
	 * Default SerialVersionUID
	 */

	private static final long serialVersionUID = 1L;
	
	/**
	 * Referenzen auf die zugehoerigen DatenbankMapper
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
		init();
	}

	/**
	 * Methode zur Initialisierung der Datenbank Mapper
	 * Wird beim login eines Nutzers aufgerufen um die Datenbank  anbindung zu erneuern
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

	
	public double getCurrentUser() {
		return  Double.parseDouble(userService.getCurrentUser().getUserId()); // currentUser;
	}
	

	/*
	 * ***************************************************************************
	 * ABSCHNITT, User
	 * ***************************************************************************
	 */
	
	/**
	 * Diese Methode erhaelt durch den GoogleUserService den eingeloggten User.
	 * Wenn die Google-ID bereits in der Datenbank vorhanden ist wird das dazugehoerige User-Object zurueck gegeben.
	 * Wenn die Google-ID nicht bekannt ist, wird ein neuer User und ein Dazugehoeriger Kontakt mit den Google User Daten erstellt
	 * 
	 * @param requestUri um die Login und Logout links an an den Client anzupassen.
	 * @return User-Object das zu dem Google account gehoert mit dem sich der Nuter eingeloggt hat
	 * @author Oliver Gorges
	 */
	public User login(String requestUri) {
		init();
		UserService userService = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User guser = userService.getCurrentUser();
		User user = new User();
		Contact own = new Contact();
		PropertyValue name = new PropertyValue();
		PropertyValue email = new PropertyValue();

		if (guser != null) {

			double id = Double.parseDouble(guser.getUserId());
			user = this.getUserByID(id);
			
			// Ueberpruefung ob der nutzer bereits existiert oder ob es ein neuer Nutzer ist
			if (user == null) {
				String gmail = guser.getEmail();
				// Ersetzt die Lange googlemail adresse gegen die gekuerzte gmail variante
				if(gmail.contains("googlemail")) gmail.replace("googlemail", "gmail");
				user = new User();
				user.setGoogleID(id);
				user.setGMail(gmail);
				own.setBo_Id(1); // Wird in der Datenbank durch die DB-ID ersetzt
				own.setOwner(user);
				own.setName(name);
				// erstellen der Kontakt Eigenschaften mit den Daten des GoogleUsers
				name.setProperty(this.getPropertyByID(1));
				email.setProperty(this.getPropertyByID(6));
				name.setValue(guser.getNickname()); 
				email.setValue(user.getGMail());
				own.addPropertyValue(name);
				own.addPropertyValue(email);
				
				user = this.createUser(user, own);
				
			} 
			// Loggt den Nutzer ein und erstellt einen Link um den Nutzer wieder auszuloggen
			user.setLoggedIn(true); 
			user.setLogoutUrl(userService.createLogoutURL(requestUri));

		} else {
			// Loggt den Nutzer aus und erstellt einen Link um den Nutzer wieder einzuloggen
			user.setLoggedIn(false);
			user.setLoginUrl(userService.createLoginURL(requestUri));

		}
		return user;

	}

	/**
	 *  Gibt das User-Obejct von dem aktuell eingeloggten User zurueck
	 *  @return User-Objekt
	 */
	@Override
	public User getAccountOwner() {
		return this.getUserByID(this.getCurrentUser());
	}

	/**
	 * Erstellt einen neuen User mit dem dazugehoerigen eigenen Kontakt.
	 * Diese Methode wird aufgerufen, wenn beim login ein User nicht gefunden wurde
	 * @param User-Objekt
	 * @param Contact-Object
	 * @return User-Objekt
	 */
	// ## IO ##
	public User createUser(User u, Contact contact) {

		User user = uMapper.insert(u);
		contact.setOwner(user);

		user.setUserContact(this.createContact(contact));
		return this.editUser(user);

	}
	/**
	 * Benutzt fuer Login & BO Owner
	 */
	// ## IO ##
	@Override
	public User getUserByID(double id) {
		User user = uMapper.findById(id);
		if(user != null){
			user.setUserContact(this.getOwnContact(user));
		}
		return user;

	}
	
	/**
	 * Benutzt zum Teilen
	 */
	// ## IO ##
	@Override
	public User getUserBygMail(String email) {
		// Ueberpruefung ob die Vollstaendige Email engegebnen wurde
		if(!email.contains("@")) email = email + "@gmail.com";
		
		User user = uMapper.findByEmail(email);
		if(user != null){
			user.setUserContact(this.getOwnContact(user));
		}
		return user;

	}
	
	/**
	 * Nur fuer Report!
	 * Wenn nicht woanders verwendet kann das raus, im Report wird es nicht mehr benutzt.
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

	/**
	 * Gibt die IDs aller User zurueck, die fuer den eingeloggten User sichtbar sind.
	 * Verwendet im Report-Generator.
	 * @return alle bekannten User
	 */
	public Vector<User> findAllKnownUsers(){
		User user = this.getUserByID(this.getCurrentUser());
		Vector<Participation> sharedParticipations = partMapper.findParticipationsByOwner(user);
		Vector<Participation> reveivedParticipations = partMapper.findParticipationsByParticipant(user);
		sharedParticipations.addAll(reveivedParticipations);
		Vector<User> users = new Vector<User>();
		for(Participation part : sharedParticipations) {
			User oneUser = this.getUserByID(part.getParticipant().getGoogleID());
			boolean containsUser = false;
			for(User userElement : users) {
				if(userElement.getGoogleID() == oneUser.getGoogleID()) {
					containsUser = true;
				}
			}
			if(!containsUser) {				
				users.add(oneUser);
			}
		}
		for (User singleUser : users) {
			singleUser.setUserContact(this.getOwnContact(singleUser));
		}
		return users;
	}
	
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Contact
	 * ***************************************************************************
	 */


	/**
	 * Erstellt einen Kontakt, dieser wird in der Datenbank gespeichert.
	 */
	@Override
	public Contact createContact(Contact contact) {
		if(contact.getOwner() == null) contact.setOwner(this.getUserByID(this.getCurrentUser()));
		boMapper.insert(contact);
		Contact c = cMapper.insertContact(contact);
		Vector<PropertyValue> pvv = new Vector<PropertyValue>();
		for(PropertyValue pv : contact.getPropertyValues()){
			if(pv.getProperty().getId() == 0){
				pv.setProperty(this.createProperty(pv.getProperty()));
			}
			pv.setContact(c);
			pv.setOwner(c.getOwner());
			pvv.add(this.createPropertyValue(pv));
		}
		c.setName(this.getNameOfContact(c));
		c.setPropertyValues(pvv);
		return c;
	
	}
	
	/**
	 * Aendert einen Kontakt. 
	 * Da die Contact-Tabelle nur eine ID besitzt wird in dieser nichts geupdatet.
	 * Dennoch wird das modified Date von BO veraendert und alle Eigenschaftsauspraegungen des Kontakts werden aktuallisiert.
	 * Wenn eine Eigenschaftsauspraegung auf dem Clien neu Angelegt wurde wird diese ebenso auf der Datenbank angelegt, das 
	 * selebe gilt ebenso beim entfernen.
	 * Wird eine neue Eigenschaft erstellt wird diese ebenso in dieser klasse neu in der Datenbank angegelegt. 
	 */
	// #### IO ####
	@Override
	public Contact editContact(Contact contact) {
		boMapper.update(contact);
		Vector<PropertyValue> newPV = contact.getPropertyValues();
		Vector<PropertyValue> oldPV = this.getPropertyValuesForContact(contact); // Liste um Iterieren
		
		// Fuegt dem Kontakt neue PropertyValues hinzu
		// Updatet die bestehenden PropertyValues
		for (PropertyValue pV : newPV) {
			if(pV.getValue().isEmpty() && pV.getProperty().getId() != 1) { // Namensfeld kann nicht geloescht werden
				this.deletePropertyValue(pV);
			}else
			if(!oldPV.contains(pV)) {
				// Legt eine neue Eigenschaft in der Datanbank an, falls diese in dem Formular neu angelegt wurde.
				if(pV.getProperty().getId() == 0){
					pV.setProperty(this.createProperty(pV.getProperty()));
				}
				this.createPropertyValue(pV);
			}else{
				if(!pV.getValue().isEmpty()) {// Wenn das Namensfeld leer ist wird es nicht geupdatet
					this.editPropertyValue(pV);
				}
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
	 * Vorschau fuer TreeView
	 */

	@Override
	public Vector<Contact> getMyContactsPrev()  {
		User user = this.getUserByID(this.getCurrentUser());
		Vector<Contact> cv = cMapper.findAllContactsByUser(user);
		
		for (Contact contact : cv) {
			contact.setName(this.getNameOfContact(contact));
		}
		cv.addAll(this.getAllCSharedByOthersToMePrev());
//		cv.sort(new BOCompare());
		return cv;
	}

	

	/**
	 * Verwendet im TreeView und ContactListForm
	 */
	// ### IO ###
	@Override
	public Vector<Contact> getContactsFromList(ContactList cl) {
		Vector<Integer> idV = cMapper.findContactFromList(cl);
		Vector<Contact> cV = new Vector<Contact>();
		
		if (idV != null) { // Ueberprueft ob die Kontaktliste Kontakte beinhaltet
			if(cl.getOwner().getGoogleID() == this.getCurrentUser()){
				for (int i : idV) {
					Contact c = cMapper.findContactById(i);
					c.setName(this.getNameOfContact(c));
					cV.add(c);
				}
			}else{ // Filtert Kontakte die nicht geteilt wurden.
				Vector<Contact> partV = this.getAllCSharedByOthersToMePrev();
					for (int i : idV) {
						Contact c = cMapper.findContactById(i);
						if(c != null && partV.contains(c)){ // Filter ob der Kontakt noch mit dem Nutzer getielt wurde.
							c.setName(this.getNameOfContact(c));
							cV.add(c);
						}
					}
			
			}
		}
		return cV;

	}

	/**
	 * Verwendet in ContactForm und Intern
	 */
	
	@Override
	public Contact getContactById(int id) {
		Contact contact = cMapper.findContactById(id);
		if (contact != null) {
			if(contact.getOwner().getGoogleID() == this.getCurrentUser()){ // Mein Kontakt
				contact.setPropertyValues(this.getPropertyValuesForContact(contact));
				contact.setName(this.getNameOfContact(contact));
				contact.setOwner(this.getUserByID(contact.getOwner().getGoogleID()));
			}else{ // Geteilter Kontakt
				User myUser = this.getUserByID(this.getCurrentUser());
				if(partMapper.isFullShared(contact, myUser)){
					contact.setPropertyValues(this.getPropertyValuesForContact(contact));
					contact.setName(this.getNameOfContact(contact));
					contact.setOwner(this.getUserByID(contact.getOwner().getGoogleID()));
				}else{
					contact.setName(this.getNameOfContact(contact));
					contact.setOwner(this.getUserByID(contact.getOwner().getGoogleID()));
					contact.setPropertyValues(this.getAllPVFromContactSharedWithUser(contact, myUser));
				}
			}
		}
		return contact;

	}

	/**
	 * Gibt alle Contact-Objekte zurueck die eine PropertyValue Besitzen, welche der
	 * Sucheingabe entspricht.
	 * Noch nicht nach eigenen Gefiltert 
	 * @param Sucheingabe
	 * @return Vector<Contact>
	 */
	@Override
	public Vector<BusinessObject> search(String value) {

		Vector<BusinessObject> cv = new Vector<BusinessObject>();
		// Findet alle Kontakte
		Vector<PropertyValue> pvv = propValMapper.findByValue(value);
		Vector<Contact> myContacts = this.getMyContactsPrev();
		Vector<ContactList> myLists = this.getMyContactListsPrev();
		
		for (PropertyValue pv : pvv) {
			Contact c = cMapper.findBy(pv);
			c.setName(this.getNameOfContact(c));
			// Filtert doppelete Kontakte und Kontakte die einem nicht gehoeren, bzw die mit einem nicht geteiltwurden aus.
			if (!cv.contains(c) && myContacts.contains(c))
				cv.add(c);

		}
		// Findet alle KontaktListen die der Nutzer anzeigen darf
		for(ContactList cl : clMapper.findContactListByName(value)){
			if (myLists.contains(cl))
				cv.add(cl);
		}
//		cv.sort(new BOCompare());
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

	/**
	 * Loescht einen Kontakt aus der Datenbank.
	 * 
	 */
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
		result = this.getAllContactsFromUser(); // TODO Ueberpruefen
		for (Contact c : result) {
			this.deleteContact(c);
		}
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, ContactList
	 * ***************************************************************************
	 */

	/**
	 * Erstellt eine Kontaktliste, diese wird in der Datenbank gespeichert. 
	 */
	@Override
	public ContactList createContactList(ContactList contactList) {
		if(contactList.getOwner() == null) contactList.setOwner(this.getUserByID(this.getCurrentUser()));
		boMapper.insert(contactList);
		ContactList cl =  clMapper.insertContactList(contactList);
		if(!contactList.getContacts().isEmpty()){
			// Uebertragen der Kontakte aus der Liste in einen neuen Vektor, wegen einer ConcurrentModificationException.
			Vector<Contact> cil = new Vector<Contact>(); 
			cil.addAll(contactList.getContacts());
			for(Contact c : cil){
				this.addContactToList(c, cl);
			}
		}
		return cl;

	}

	/**
	 * Aendert den Namen einer Kontaktliste.
	 */
	@Override
	public ContactList editContactList(ContactList cl) {
		boMapper.update(cl);
		// Absicherung das der Name nicht leer gelassen wird
		if(!cl.getName().isEmpty()){
			return clMapper.updateContactList(cl);
		}
		return cl;
	}



	/**
	 * Gibt alle Kontaktlisten zurueck, welche zu einem User gehoeren.
	 */
	@Override
	public Vector<ContactList> getAllContactListsFromUser() {

		Vector<ContactList> contactListVector = clMapper.findContactListByUserId(this.getCurrentUser());

		for (ContactList cl : contactListVector) {
			cl.setOwner(this.getUserByID(cl.getOwner().getGoogleID()));
			cl.setContacts(this.getContactsFromList(cl));
		}
		return contactListVector;
	}
	
	/**
	 * Vorschau Ansicht der Listenobjekten im Treeview
	 */
	public Vector<ContactList> getMyContactListsPrev() {

		Vector<ContactList> contactListVector = clMapper.findContactListByUserId(this.getCurrentUser());
		// Alle geteilten Listen
		contactListVector.addAll(this.getAllCLSharedByOthersToMePrev());
//		contactListVector.sort(new BOCompare());
		return contactListVector;
	}

	/**
	 * Gibt alle Kontaktlisten nach der GoogleID aus.
	 */
	@Override
	public ContactList getContactListById(int id) {
		ContactList contactList = clMapper.findContactListById(id);
		
		if (contactList != null) {
			contactList.setOwner(getUserByID(contactList.getOwner().getGoogleID()));
			contactList.setContacts(getContactsFromList(contactList));
		}
		return contactList;
	}
	
	/**
	 * Loescht eine Kontaktliste eines Users.
	 */
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
	 * Es loescht alle KontaktListen, welche zu einer User ID gehoeren.
	 * Verwendet beim Loeschen eines Accounts.
	 */

	public void deleteContactListByUserId() {

		for (ContactList cl : this.getAllContactListsFromUser()) {
			this.deleteContactList(cl);
		}
	}

	/**
	 * Fuegt einer Liste einen neuen Kontakt hinzu.
	 * Wenn Die Liste Geteilt wurde, wird dieser ebenso geteilt.
	 * Wenn Der Kontakt nicht von dem Besitzer hinzugefuegt wurde, wird der Kontakt zudem mit dem Besitzer geteilt.
	 * 
	 * @param Contact, ContactList
	 * @return ContactList
	 */
	@Override
	public ContactList addContactToList(Contact contact, ContactList contactList) {
		
		// Teilt den neuen Kontakt mit allen Teilhabern der Liste
		Vector<Participation> partv = this.getAllParticipationsByBusinessObject(contactList);
		if(partv != null){
			for(Participation part : partv){
				Participation createPart = new Participation();
				createPart.setParticipant(part.getParticipant());
				createPart.setReference(contact);
				createPart.setShareAll(true);
				this.createParticipation(createPart);
			}
		}
		if(this.getCurrentUser() != contactList.getOwner().getGoogleID()){ 
				Participation createPart = new Participation();
				createPart.setParticipant(contactList.getOwner());
				createPart.setReference(contact);
				createPart.setShareAll(true);
				this.createParticipation(createPart);
		}
		
		return clMapper.addContactToContactlist(contactList, contact);

	}

	/**
	 * Entfernt einen Kontakt aus einer KontaktListe.
	 * Wenn Die Liste Geteilt wurde, wird die Teilhaberschft zu der Liste und der dazugehoerigen Kontakte geloescht.
	 * 
	 * @param Contact, ContactList
	 * @return ContactList
	 */
	@Override
	public ContactList removeContactFromList(Contact contact, ContactList contactList) {
		
		Vector<Participation> partv = this.getAllParticipationsByBusinessObject(contactList);
		if(partv != null){
			for(Participation part : partv){
				Participation delPart = new Participation();
				delPart.setParticipant(part.getParticipant());
				delPart.setReference(contact);
				delPart.setShareAll(true);
				this.deleteParticipation(delPart);
			}
		}
		
		return clMapper.removeContactFromContactList(contactList, contact);

	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, PropertyValue
	 * ***************************************************************************
	 */
	/**
	 * Erstellt eine neue Eigenschaftsausraegung fuer einen Kontakt. 
	 * Intern Verwendet in EDIT / Create Contact
	 * @param PropertyValue
	 * @return PropertyValue
	 */
	public PropertyValue createPropertyValue(PropertyValue propertyValue) {
		// Da Property immer fest zu einem Contact-Objekt gehoert hat es auch den selben Besitzer.

		propertyValue.setOwner(propertyValue.getContact().getOwner());
		boMapper.insert(propertyValue);
		PropertyValue pv = propValMapper.insert(propertyValue);
		// Eigenschaft wird automatisch geteilt, wenn der Ersteller nicht der Kontaktbesitzer ist.
		if(pv.getOwner().getGoogleID() != this.getCurrentUser()){
			Participation p = new Participation();
			p.setParticipant(this.getAccountOwner());
			p.setReference(pv);
			this.createParticipation(p);
		}
		return pv;
	}
	
	/**
	 * Veraendert den Value (angezeigten Text) der Eigenschaftsauspraegung
	 * Intern Verwendet in EDIT / Create Contact
	 * @param PropertyValue
	 * @return PropertyValue
	 */
	public PropertyValue editPropertyValue(PropertyValue propertyValue) {
		boMapper.update(propertyValue);
		return propValMapper.update(propertyValue);
	}

	/**
	 * Loescht eine Eigenschaftsauspraegung von einem Kontakt.
	 * @param PropertyValue
	 * @return PropertyValue
	 */
	public PropertyValue deletePropertyValue(PropertyValue propertyValue) {
		PropertyValue pv = propValMapper.delete(propertyValue);
		if (pv != null)
			boMapper.deleteBusinessObjectByID(pv.getBoId());
		return pv;
	}
	
	
	/**
	 * Gibt einen Vector mit allen Eigenschaftsauspraegungen zurueck, die zu einem Kontakt gehoeren
	 * @param Contact
	 * @return Vector<PropertyValue>
	 */
	public Vector<PropertyValue> getPropertyValuesForContact(Contact c) {

		Vector<PropertyValue> pvv = propValMapper.findBy(c);

		for (PropertyValue pv : pvv) {
			pv.setProperty(this.getPropertyByID(pv.getProperty().getId()));
		}
		return pvv;

	}

	/**
	 * Sucht eine eigenschaftsauspraegung mithilfe der <code>BusinessObject</code>-ID
	 * @param PropertyValue-ID
	 * @return PropertyValue
	 */
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
	 * Anhand des uebergebenen Strings werden alle PropertyValue - Objekte
	 * identifiziert, deren Wert dem Suchtext entspricht und zurueckgegeben.
	 *
	 * @param suchtext Gesuchter Wert
	 * @return Vector mit PropertyValue - Objekten
	 */
	@Override
	public Vector<PropertyValue> searchPropertyValues(String suchtext) {
		Vector<PropertyValue> pvv = propValMapper.findByValue(suchtext);
		return pvv;
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Property
	 * ***************************************************************************
	 */
	
	/**
	 * Erstellt eine neue Eigenschaft 
	 * 
	 * @param Property
	 * @return Property
	 */
	@Override
	public Property createProperty(Property p){
		return propMapper.insert(p);
	}
	
	/**
	 * Gibt alle Eigenschaften aus der Datenbank zurueck 
	 * 
	 * @return Vector<Property>
	 */
	@Override
	public Vector<Property> getAllProperties() {
		return propMapper.findAll();
	}
	
	/**
	 * Gibt die Eigenschaft mit der uebergebenen ID zurueck.
	 * 
	 * @param Property-ID
	 * @return Property
	 */
	@Override
	public Property getPropertyByID(int id) {
		return propMapper.findBy(id);
	}
	
	/**
	 * Aendert die Beschreibung einer Eigenschaft.
	 * Die Eigenschaft "Name" darf nicht veraendert werden.
	 * 
	 * @param Property
	 * @return Property
	 */
	@Override
	public Property editProperty(Property p){
		if(p.getId() != 1){ 
			return propMapper.update(p);
		}
		return null;
	}
	
	/**
	 * Loescht alle Eigenschaftsauspraegungen, die auf die Eigenschaft verweisen und loescht diese Auspraegung.
	 * Die Eigenschaft "Namen" kann nicht geloescht werden.
	 * @return Propery
	 * @param Property
	 */
	@Override
	public Property deleteProperty(Property p){
		if(p.getId() != 1){
			for(PropertyValue pv : propValMapper.findBy(p)){
				this.deletePropertyValue(pv);
			}
			return propMapper.delete(p);
		}
		return null;
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Participation
	 * ***************************************************************************
	 */

	/**
	 * Erstellt eine Teilhaberschaft verschiedener BOs.
	 */
	@Override
	public Participation createParticipation(Participation part) {
		Participation participation = partMapper.insertParticipation(part);
		boMapper.setStatusTrue(part.getReferenceID());
		
		if(part.getReferencedObject() instanceof Contact){
			// Teilt nur Eigenscaftsauspraegungen die sich aktuell in dem Contact-Objekt befinden und wenn der Kontakt nicht 
			// vollstaendig geteilt wurde
			if( !part.getShareAll() && ((Contact) part.getReferencedObject()).getPropertyValues() != null){
				// Teilt alle Eienscaftsauspraegungen die sich in der Lite Befinden mit dem Nutzer
				for(PropertyValue pv : ((Contact) part.getReferencedObject()).getPropertyValues()){
					Participation partPV = new Participation();
					partPV.setParticipant(part.getParticipant());
					partPV.setReference(pv);
					this.createParticipation(partPV);
				}
			}
		}else if(part.getReferencedObject() instanceof ContactList){
			if(((ContactList) part.getReferencedObject()).getContacts() != null){
				// Teilt alle kontakte die sich in der Lite Befinden mit dem Nutzer
				for(Contact c : ((ContactList) part.getReferencedObject()).getContacts()){
					Participation partC = new Participation();
					partC.setParticipant(part.getParticipant());
					partC.setReference(c);
					partC.setShareAll(true);
					this.createParticipation(partC);
				}
			}
		}
		return participation;

	}

	/**
	 * Wenn Vollstaendig geteilt, alle PVS aus der Part tabelle loeschen
	 * Ansonsten abgleich ob neue teilhaberschaft oder ob welche entfernet wurden.
	 */
	@Override
	public Participation editParticpation(Participation part){
		partMapper.updateParticipation(part); 
		if(part.getReferencedObject() instanceof Contact){
			Contact c = (Contact) part.getReferencedObject();
			// Wenn Kontakt vollstaendig geteilt, dann werden alle PropertyValue Teilhaberschaften enfernt
			if(part.getShareAll()){
				for(PropertyValue pv : c.getPropertyValues()){
					Participation removePart = new Participation();
					removePart.setParticipant(part.getParticipant());
					removePart.setReference(pv);
					this.deleteParticipation(removePart);
				}
			
			}else{ 
				// Wenn nicht alles geteilt wird muss ueberprueft werden was bereits geteilt ist, was neu geteilt werdne muss 
				// und was entfernt werden soll.
				Vector<Participation> allShared = this.getAllParticipationsByParticipant(part.getParticipant());
				// Referenz Contact-Object um festzustellen was nicht geteilt wird
				Vector<PropertyValue> refPV = this.getPropertyValuesForContact(c);
				// Suchen von allen geteilten Eigenschaften 
				
				// Kontrolle ob neue PropertyValues geteilt wurden
				for(PropertyValue pv : c.getPropertyValues()){
					Participation addPart = new Participation();
					addPart.setParticipant(part.getParticipant());
					addPart.setReference(pv);
					if(!allShared.contains(addPart)){
						this.createParticipation(addPart);
					}
				}
				// Loescht alle beziehungen zu PropertyValues die nicht mehr geteilt werden sollen.
				for(PropertyValue pv : refPV){
					if(!c.getPropertyValues().contains(pv)){
						Participation removePart = new Participation();
						removePart.setParticipant(part.getParticipant());
						removePart.setReference(pv);
						this.deleteParticipation(removePart);
					}
				}
			}
			return part;
		}else if(part.getReferencedObject() instanceof ContactList){
			ContactList cl = (ContactList) part.getReferencedObject();
			// Kontaktlisten werden aktuell nur Vollstaendig geteilt
			return part;
		}else if(part.getReferencedObject() instanceof PropertyValue){
			// PropertyValues werden aktuell nicht geupdatet
			return part;
		}
		
		
		return null;
	}


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
	 * Verwendet in FindAllSharedByOthersToMe und Report
	 */
	@Override
	public Vector<Participation> getAllParticipationsByParticipant(User participant) {
		Vector<Participation> partV = partMapper.findParticipationsByParticipant(participant);
		for (Participation part : partV) {
			part.setParticipant(participant);
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
	 * Loescht eine erstellte Teilhaberschaft, je nach BO.
	 * @param p
	 * @return
	 */
	@Override
	public Participation deleteParticipation(Participation p) {
		Participation part = partMapper.deleteParticipation(p);
		// Pruefen, ob es zu dem geteilten Objekt noch eine Teilhaberschaft gibt,
		// wenn nicht, Status (geteilt) des Objekt auf false setzen
		Vector<Participation> participations = this.getAllParticipationsByBusinessObject(part.getReferencedObject());
		if (participations.isEmpty()) {
			BusinessObjectMapper.businessObjectMapper().setStatusFalse(part.getReferenceID());
		}
		if(p.getReferencedObject() instanceof Contact){
			for(PropertyValue pv : this.getPropertyValuesForContact((Contact) p.getReferencedObject())){
				Participation partPV = new Participation();
				partPV.setParticipant(p.getParticipant());
				partPV.setReference(pv);
				this.deleteParticipation(partPV);
			}
		}else if(p.getReferencedObject() instanceof ContactList){
			for(Contact c : this.getContactsFromList((ContactList)p.getReferencedObject())){
				Participation partC = new Participation();
				partC.setParticipant(part.getParticipant());
				partC.setReference(c);
				this.deleteParticipation(partC);
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
	 * zurueck
	 * 
	 * @param BusinessObject
	 *            ID
	 * @return Contact, ContactList, PropertyValue
	 */
	
	
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
//		bov.sort(new BOCompare());
		return bov;

	}

	/**
	 * Alle fuer den Benutzer in der Applikation geteilte Auspraegungen
	 * <code>PropertyValue</code> Objekte koennen ueber den Aufruf dieser Methode
	 * aus der DB zurueck gegeben werden.
	 * 
	 * @param User-Objekt
	 * @return Vector PropertyValue-Objekte
	 */

	public Vector<PropertyValue> getAllPVSharedByOthersToMe() {

		// Alle Participation-Objekte eines Users abrufen, welche fuer Objekte kapseln,
		// die von diesem geteilt wurden
		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = this.getAllParticipationsByParticipant(this.getUserByID(this.getCurrentUser()));
		// Vector fuer die Speicherung aller BusinessObjekte erzeugen
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
		 *  Alle fuer den Benutzer in der Applikation zugaenglichen Kontakte <code>Contact</code> - Objekte
		 * (diese sind selbst erstellt und anderen zur Teilhaberschaft freigegeben) werden anhand ihres Status gesucht
		 *  und als ein Ergebnissvektor aus Contact-objekten zurueckgegeben. 
		 *  
		 *  @param User-Objekt
		 *  @return Vector mit allen geteilten Contact-Objekten
		 */

		public Vector<Contact> getAllCSharedByMe (User user) {

			// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln, die von diesem geteilt wurden
			Vector<Participation> participationVector = this.getAllParticipationsByOwner(user);		
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
		 *  Alle fuer den Benutzer in der Applikation zugaenglichen Kontakte <code>Contact</code> - Objekte
		 * (diese sind selbst erstellt und anderen zur Teilhaberschaft freigegeben) werden anhand ihres Status gesucht
		 *  und als ein Ergebnissvektor aus Contact-objekten zurueckgegeben. 
		 *  
		 *  @param User-Objekt
		 *  @return Vector<ContactList>
		 */

		public Vector<ContactList> getAllCLSharedByMe (User user) {

			Vector<Participation> participationVector = new Vector<Participation>();		
			participationVector = this.getAllParticipationsByOwner(user);
					// Vector fuer die Speicherung aller BusinessObjekte erzeugen
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
		 * Methode zur Loeschung aller von einem User erstellten Auspraegungen, Ownership und Participation!
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
	 * Alle fuer den Benutzer in der Applikation geteilte Kontakte
	 * <code>Contact</code> Objekte koennen ueber den Aufruf dieser Methode aus der
	 * DB zurueck gegeben werden.
	 * 
	 * @param User-Objekt
	 * @return Vector Contact-Objekte
	 */

	public Vector<Contact> getAllCSharedByOthersToMe() {

		// Alle Participation-Objekte eines Users abrufen, welche fuer Objekte kapseln,
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
	 * Vorschau fruer TreeView
	 * @return
	 */
	public Vector<Contact> getAllCSharedByOthersToMePrev() {

		Vector<Participation> participationVector = partMapper.findSharedContacts(this.getUserByID(this.getCurrentUser()));
		Vector<Contact> cResultVector = new Vector<Contact>();

		for (Participation part : participationVector) {
			Contact c = (Contact) part.getReferencedObject();	
			if(c != null && !cResultVector.contains(c)){
				c.setOwner(this.getUserByID(c.getOwner().getGoogleID()));
				c.setName(this.getNameOfContact(c));
				cResultVector.addElement(c);
			}
			
		}

		if (cResultVector.isEmpty())
			System.out.println("# no contact found");
//		cResultVector.sort(new BOCompare());
		return cResultVector;
	}
	
	private Contact filterContactData(Contact contact){
		// Filter damit nur die geteilten PropertyValues ausgegeben werden; Namen werden nicht gefiltert
		Iterator<PropertyValue> iterator = contact.getPropertyValues().iterator(); // Liste zum Iterieren
		Vector<PropertyValue> pvv = new Vector<PropertyValue>(); // Liste aus der Elemente geloescht werden
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
		
		return partMapper.findPVforSharedContact(c, u);
	}
	
	/**
	 * Alle fuer den Benutzer in der Applikation geteilte Kontaktelisten
	 * <code>ContactList</code> -Objekte koennen ueber den Aufruf dieser Methode aus
	 * der DB zurueck gegeben werden.
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
	 * Vorschau fuer TreeView
	 * @return
	 */
	public Vector<ContactList> getAllCLSharedByOthersToMePrev() {

		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = partMapper.findAllSharedContactLists(this.getUserByID(this.getCurrentUser()));
		Vector<ContactList> clResultVector = new Vector<ContactList>();

		for (Participation part : participationVector) {
			ContactList cl = (ContactList) part.getReferencedObject(); 
			if (cl != null) {
				cl.setOwner(this.getUserByID(cl.getOwner().getGoogleID()));
				clResultVector.addElement(cl);
			}
		}

		if (clResultVector.isEmpty())
			System.out.println("# no contactList found");

		return clResultVector;
	}

	public Vector<BusinessObject> getAllSharedByMe() {
		Vector<BusinessObject> vbo = new Vector<BusinessObject>();
		for (Contact c : this.getAllCSharedByMePrev()) {
			vbo.add(c);
		}
		for (ContactList cl : this.getAllCLSharedByMePrev()) {
			vbo.add(cl);
		}
//		vbo.sort(new BOCompare());
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

		// Alle Participation-Objekte eines Users abrufen, welche fuer Objekte kapseln,
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
	 * Alle fuer den Benutzer in der Applikation zugaenglichen Kontakte
	 * <code>Contact</code> - Objekte (diese sind selbst erstellt und anderen zur
	 * Teilhaberschaft freigegeben) werden anhand ihres Status gesucht und als ein
	 * Ergebnissvektor aus Contact-objekten zurueckgegeben.
	 * 
	 * @param User-Objekt
	 * @return Vector mit allen geteilten Contact-Objekten
	 */

	public Vector<Contact> getAllCSharedByMePrev() {

		// Alle Participation-Objekte eines Users abrufen, welche fuer Objekte kapseln,
		// die von diesem geteilt wurden
		 Vector<Participation> participationVector = this.getAllParticipationsByOwner(this.getUserByID(this.getCurrentUser()));
		Vector<Contact> contactResultVector = new Vector<Contact>();

		for (Participation part : participationVector) {
			Contact c = cMapper.findContactById(part.getReferenceID());
			if (c != null && !contactResultVector.contains(c)) {
				c.setName(this.getNameOfContact(c));
				contactResultVector.addElement(c);
			}
		}
		if (contactResultVector.isEmpty())
			System.out.println("# no contacts found");
//		contactResultVector.sort(new BOCompare());
		return contactResultVector;

	}

	/**
	 * Alle fuer den Benutzer in der Applikation zugaenglichen Kontakte
	 * <code>Contact</code> - Objekte (diese sind selbst erstellt und anderen zur
	 * Teilhaberschaft freigegeben) werden anhand ihres Status gesucht und als ein
	 * Ergebnissvektor aus Contact-objekten zurueckgegeben.
	 * 
	 * @param User-Objekt
	 * @return Vector<ContactList>
	 */

	public Vector<ContactList> getAllCLSharedByMePrev() {

		Vector<Participation> participationVector = new Vector<Participation>();
		participationVector = this.getAllParticipationsByOwner(this.getUserByID(this.getCurrentUser()));
		// Vector fuer die Speicherung aller BusinessObjekte erzeugen
		Vector<ContactList> contactListVector = new Vector<ContactList>();

		for (Participation part : participationVector) {
			ContactList cl = clMapper.findContactListById(part.getReferenceID());
			if (cl != null) {
				contactListVector.addElement(cl);
			}
		}
//		contactListVector.sort(new BOCompare());
		return contactListVector;

	}

	/**
	 * Methode zur Loeschung aller von einem User erstellten Auspraegungen,
	 * Ownership und Participation!
	 * 
	 * @param User-Objekt
	 */

	public void deleteAllPVSharedByMe() {

		Vector<PropertyValue> propertyValueResult = new Vector<PropertyValue>();
		propertyValueResult = this.getAllPVSharedByMe();

		for (PropertyValue pV : propertyValueResult) {
			// loeschen aller Eintraege in der Teilhaberschaft Tabelle Participation
			ParticipationMapper.participationMapper().deleteParticipationForBusinessObject(pV);
			this.deletePropertyValue(pV);
		}
	}

	/**
	 * Methode zur Loeschung aller von einem User erstellten Kontakte
	 * <code>Contact</code> Objekte, welche im System mit anderen Nutzern geteilt
	 * wurden.
	 * 
	 * @param User-Objekt
	 */

	public void deleteAllCSharedByMe(User user) {

		Vector<Contact> contactResult = new Vector<Contact>();
		contactResult = this.getAllCSharedByMePrev();

		for (Contact contact : contactResult) {
			ParticipationMapper.participationMapper().deleteParticipationForBusinessObject(contact);
			this.deleteContact(contact);
			System.out.println("# shared contact deleted: " + contact.getBoId());
		}

	}

	/**
	 * Methode zur Loeschung aller von einem User erstellten Kontaktlisten
	 * <code>ContactList</code> -Objekte, welche im System mit anderen Nutzern
	 * geteilt wurden.
	 * 
	 * @param User
	 */

	public void deleteAllCLSharedByMe(User user) {

		Vector<ContactList> clResult = new Vector<ContactList>();
		clResult = this.getAllCLSharedByMePrev();

		for (ContactList cl : clResult) {
			ParticipationMapper.participationMapper().deleteParticipationForBusinessObject(cl);
			this.deleteContactList(cl);
		}
	}

	/**
	 * Funktion zum Loeschen aller Auspraegungen die vom User geteilt wurden
	 * 
	 * @param User-Objekt
	 */

	public void deleteAllPVSharedByOthersToMe(User u) {

		// Alle Participation-Objekte eines Users abrufen, welche fuer Objekte kapseln,
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
	 * Eine Methode zur Loeschung aller Verbindungen in der Participation Tabelle
	 * der DB. Dies fuehrt dazu, dass die fuer einen Nutzer geteilten Objekte nicht
	 * mehr aufgerufen werden koennen. Die Teilhaberschaft ist damit beendet.
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
	
	/**
	 * Methode zum vergleichen von BusinesObjects.
	 * Wird verwendet um die ausgabe an den TreeView zu sortieren.
	 * @author Oliver Gorges
	 *
	 */
	public class BOCompare implements Comparator<BusinessObject>{

			@Override
			public int compare(BusinessObject o1, BusinessObject o2) {

				String s1 = "", s2 = "";
				// Wandelt ein ContactList-Objekt in einen String um
				if(o1 instanceof ContactList){
					s1 = ((ContactList) o1).getName();
				}else if(o2 instanceof ContactList){
					s2 = ((ContactList) o2).getName();
				}
				// Wandelt ein Contact-Objekt in einen String um
				if(o1 instanceof Contact){
					s1 = ((Contact) o1).getName().getValue();
				}else if(o2 instanceof Contact){
					s2 = ((Contact) o2).getName().getValue();
				}
				// Wandelt ein PropertyValue-Objekt in einen String um
				if(o1 instanceof PropertyValue){
					s1 = ((PropertyValue) o1).getValue();
				}else if(o2 instanceof PropertyValue){
					s2 = ((PropertyValue) o2).getValue();
				}
				
				return s1.compareTo(s2);
			}
	}
	

}