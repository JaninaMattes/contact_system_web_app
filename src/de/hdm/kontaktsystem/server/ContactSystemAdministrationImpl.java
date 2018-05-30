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
 * Implementierungsklasse des Interface <code>ContactSystemAdministration</code>. Diese
 * Klasse ist <em>die</em> Klasse, die neben {@link ReportGeneratorImpl}
 * sämtliche Applikationslogik (engl. Business Logic) aggregiert. Diese sorgt
 * für einen geordneten Ablauf und Konsistenz der Daten sowie Abläufe in der
 * Applikation. </p>
 * 
 * Jede Methode dieser Klasse bildet die Applikationslogik ab und
 * kann als <em>Transaction Script</em> bezeichnet werden. Diese überführen das
 * System von einem konsistenten Zustand in einen anderen über.
 * 
 * <li> {@link RemoteServiceServlet}: Jede Server-seitig instantiierbare und
 * Client-seitig über GWT RPC nutzbare Klasse muss die Klasse
 * <code>RemoteServiceServlet</code> implementieren. Sie legt die funktionale
 * Basis für die Anbindung von <code>ContactSystemImpl</code> an die Runtime
 * des GWT RPC-Mechanismus.</li>
 *
 */

public class ContactSystemAdministrationImpl extends RemoteServiceServlet implements ContactSystemAdministration{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
			
	private Contact contact = null; //?? TODO: überprüfen
	
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
	 *  Methode zur Initialisierung
	 */
	
	public void init() throws IllegalArgumentException{
		
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
	
	public double getCurrentUser(){
		return Double.parseDouble(userService.getCurrentUser().getUserId());
	}
	
	/*
	* ***************************************************************************
	* ABSCHNITT, User 
	* ***************************************************************************
	*/
	
	public User createUser(User u, Contact contact) {
		User user = uMapper.insert(u);
		contact.setOwner(user);
		user.setUserContact(this.createContact(contact));
		return this.editUser(user);
		
	}
	
	public User getUserByID(double id) {
		User user = uMapper.findById(id);
		user.setUserContact(this.getOwnContact(user));
		return user;
		
	}
	
	public User getUserBygMail(String email) {
		User user = uMapper.findByEmail(email);
		user.setUserContact(this.getOwnContact(user));
		return user;
		
	}
	
	// Nur für Report!
	public Vector<User> getAllUsers(){
		Vector<User> userVector = uMapper.findAll();
		for(User user : userVector){
			user.setUserContact(this.getOwnContact(user));
		}
		return userVector;
		
	}
		
	public User editUser(User user) {
		return uMapper.update(user);		
	}
	
	public User deleteUser(User user) {
		this.deleteAllContactsByUser(user.getGoogleID());
		this.deleteContactListByUserId(user.getGoogleID());
		return uMapper.delete(user);
	}
	
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Contact
	* ***************************************************************************
	*/
	
	
	// Nur für Report!
	public Vector<Contact> getAllContacts(){
		Vector<Contact> cv = cMapper.findAllContacts();
		for(Contact contact : cv){
			contact.setOwner(this.getUserByID(contact.getOwner().getGoogleID()));
			contact.setName(this.getNameOfContact(contact));
			contact.setPropertyValues(this.getPropertyValueForContact(contact));
		}
		return cv;
		
	}
	
	// Nur für Report!
	public Vector<Contact> getAllContactsFromUser(){
		return cMapper.findAllContactsByUser(this.getCurrentUser());
		
	}
	
	// Nur Intern Verwendet
	public Contact getOwnContact(User u) {
		return cMapper.findOwnContact(u);
	}
	
	public Contact editContact(Contact contact) {
		Contact con = cMapper.updateContact(contact);
		Vector <PropertyValue> propResult = new Vector <PropertyValue>();
		propResult = this.getPropertyValueForContact(con);
		for(PropertyValue pV : propResult) {
			this.editPropertyValue(pV);
		}	
		return con;
		
	}	
	
	public Contact getContactByPropertyValue(PropertyValue pv){
		Contact contact = cMapper.findBy(pv);
		contact.setOwner(this.getUserByID(contact.getOwner().getGoogleID()));		
		contact.setName(this.getNameOfContact(contact));
		contact.setPropertyValues(this.getPropertyValueForContact(contact));
		return contact;
	}
	
	public Vector<Contact> getContactsFromUser(User user) {
		Vector<Contact> cv = cMapper.findAllContactsByUser(user);
		for(Contact contact : cv){
			contact.setOwner(user);
			contact.setPropertyValues(this.getPropertyValueForContact(contact));
			contact.setName(this.getNameOfContact(contact));
		}
		return cv;
		
	}
	
	public Vector<Contact> getContactsByStatus(Boolean status) {
		Vector<Contact> cv = cMapper.findContactByStatus(this.getCurrentUser(), status);
		for(Contact contact : cv){
			contact.setOwner(this.getUserByID(this.getCurrentUser()));
			contact.setPropertyValues(this.getPropertyValueForContact(contact));
			contact.setName(this.getNameOfContact(contact));
		}
		return cv;
		
	}
	
	public Vector<Contact> getContactsFromList(ContactList cl) {
		Vector<Integer> iv = cMapper.findContactFromList(cl);
		Vector<Contact> cv = new Vector<Contact>();
		if(iv != null){
			for(int i : iv){
				cv.add(getContactById(i));
			}
		}
		return cv;
		
	}
	
	@Override
	public Contact getContactById(int id) {

		contact.setPropertyValues(this.getPropertyValueForContact(contact));
		contact.setName(this.getNameOfContact(contact));
		contact.setOwner(this.getUserByID(contact.getOwner().getGoogleID()));
		return this.getContactById(id);
	}
	
	
	
	public Contact createContact(Contact contact) {
		boMapper.insert(contact);
		return cMapper.insertContact(contact);
		
	}
	

	public Contact deleteContact(Contact contact) {	
		for(PropertyValue pv : this.getPropertyValueForContact(contact)){
			this.deletePropertyValue(pv);
		}
		Contact c = cMapper.deleteContact(contact);
		if(c != null) boMapper.deleteBusinessObjectByID(c.getBoId());
		return c;
	}
	

	/**
	 * Mapper-Methode um alle Kontakte eines bestimmten Users mittels der User-ID zu
	 * loeschen
	 * 
	 * @param User ID 
	 */

	public void deleteAllContactsByUser(double user_id) {

		Vector<Contact> result = new Vector<Contact>();		
		//Aufrufen aller Kontakte eines bestimmten Users
		result = ContactMapper.contactMapper().findAllContactsByUser(user_id);
		for (Contact c : result) {
			deleteContact(c);
		}
	}
	
	/*
	* ***************************************************************************
	* ABSCHNITT, ContactList
	* ***************************************************************************
	*/
	
	public ContactList createContactList(ContactList contactList) {

		boMapper.insert(contactList);
		return clMapper.insertContactList(contactList);
			
	}	

	@Override
	public ContactList editContactList(ContactList cl) {
		boMapper.update(cl);
		return clMapper.updateContactList(cl);
	}
	
	public Vector <ContactList> getContactListByName(String name) {
		
		Vector<ContactList> contactListVector = clMapper.findContactListByName(name);
		
		for(ContactList cl : contactListVector){
			cl.setOwner(this.getUserByID(cl.getOwner().getGoogleID()));
			cl.setContacts(this.getContactsFromList(cl));
		}
		return contactListVector;
		
	}
	
	// Nur für Report!
	@Override
	public Vector<ContactList> getAllContactLists() {
		Vector<ContactList> contactListVector = clMapper.findAllContactLists();
		
		for(ContactList cl : contactListVector){
			cl.setOwner(this.getUserByID(cl.getOwner().getGoogleID()));
			cl.setContacts(this.getContactsFromList(cl));
		}
		return contactListVector;
	}
	
	// Nur für Report!
		@Override
		public Vector<ContactList> getAllContactListsFromUser() {
			
			Vector<ContactList> contactListVector = clMapper.findContactListByUserId(this.getCurrentUser());
			
			for(ContactList cl : contactListVector){
				cl.setOwner(this.getUserByID(cl.getOwner().getGoogleID()));
				cl.setContacts(this.getContactsFromList(cl));
			}
			return contactListVector;
		}


	@Override
	public ContactList getContactListById(int id) {
		ContactList contactList = clMapper.findContactListById(id);
		
		contactList.setOwner(getUserByID(contactList.getOwner().getGoogleID()));
		contactList.setContacts(getContactsFromList(contactList));
		
		return contactList;
	}
	
	
	public ContactList deleteContactList(ContactList contactList) {
		ContactList cl = clMapper.deleteContactList(contactList);
		if(cl != null) boMapper.deleteBusinessObjectByID(cl.getBoId());
		return cl;
	}
	

	/**
	 * Es löscht alle Kontakte, welche zu einer User ID gehören.
	 * 
	 * @param User Id
	 */

	public void deleteContactListByUserId(Double userId) {
		
		for(ContactList cl : this.getAllContactListsFromUser()){
			this.deleteContactList(cl);
		}
	}
	
	public ContactList addContactToList(Contact contact, ContactList contactList) {
		return clMapper.addContactToContactlist(contactList, contact);
		 
	}
	public ContactList removeContactFromList(Contact contact, ContactList contactList) {
		return clMapper.removeContactFromContactList(contactList, contact);

	}
	
	
	/*
	* ***************************************************************************
	* ABSCHNITT, PropertyValue
	* ***************************************************************************
	*/
	public PropertyValue createPropertyValue(PropertyValue propertyValue) {
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
		if(pv != null) boMapper.deleteBusinessObjectByID(pv.getBoId());
		return pv;
	}
	
	
	
	public Vector<PropertyValue> getPropertyValueForContact(Contact c) {
		Vector<PropertyValue> pvv = propValMapper.findBy(c);
		
		for(PropertyValue pv : pvv){
			pv.setProperty(propMapper.findBy(pv.getProperty().getId()));
		}
		return pvv;
	}
	
	public PropertyValue getPropertyValueById(int id) {
		return propValMapper.findByKey(id);
	}
	
	public Vector<PropertyValue> getPropertyValueByValue(String value) {
		Vector<PropertyValue> pvv = propValMapper.findByValue(value);
		
		for(PropertyValue pv : pvv){
			pv.setContact(this.getContactByPropertyValue(pv));
			pv.setProperty(PropertyMapper.propertyMapper().findBy(pv.getProperty().getId()));
		}
		return pvv;
	}


	/**
	 * Anhand der uebergegebenen ID wird das zugehoerige PropertyValue - Objekt,
	 * welches der Eigenschaft "Name" zugewiesen werden kann, eindeutig
	 * identifiziert und zurueckgegeben
	 * 
	 *  @param Contact-Objekt
	 *  @return PropertyValue - Objekt
	 */

	public PropertyValue getNameOfContact(Contact contact) {
		////System.out.println("#PV -findName");
		PropertyValue name = new PropertyValue();	
		Vector <PropertyValue> result = new Vector <PropertyValue>();
		result = this.getPropertyValueForContact(contact);
		
		for(PropertyValue val : result) {

			//System.out.println("propertyVal id: " + val.getBo_Id());
			//System.out.println("propertyVal description: " + val.getProp().getDescription());
			
			if(val.getProperty().getId() == 1) {
				name = val; 
				//System.out.println("Contact name:" + val.getProperty().getPropertyValues());
			}
		}	
		////System.out.println("Name: " + name);
		return name;
		
	}
	

	/*
	* ***************************************************************************
	* ABSCHNITT, Participation
	* ***************************************************************************
	*/
	

	@Override
	public Participation createParticipation(Participation part) {
		return partMapper.insertParticipation(part);
	}

	@Override
	public Vector<Participation> getAllParticipationsByOwner(User u) {
		return partMapper.findParticipationsByOwner(u);
	}

	@Override
	public Vector<Participation> getAllParticipationsByParticipant(User participant) {
		return partMapper.findParticipationsByParticipant(participant);
	}

	@Override
	public Participation deleteParticipation(Participation p) {
		return partMapper.deleteParticipation(p);
		
	}
	

	/*
	* ***************************************************************************
	* ABSCHNITT, BusinessObject
	* ***************************************************************************
	*/

	/**
	   * Gibt ein BusinessObject vom Typ Contact, ContctList oder PropertyValue zurück
	   * 
	   * @param BusinessObject ID
	   * @return Contact, ContactList, PropertyValue
	   */
	  
	  public BusinessObject findBusinessObjectByID(int id) {
		  	  
		BusinessObject bo = null;
		
		
		if(bo == null) bo = this.getContactById(id);
		 
		
		if(bo == null) bo = this.getContactListById(id);
		
		
		if(bo == null) bo = this.getPropertyValueById(id);
		  	 
		return bo;
	  }


	
	
	
}