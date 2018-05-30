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
	
	/*
	* ***************************************************************************
	* ABSCHNITT, User 
	* ***************************************************************************
	*/
	
	public User createUser(User u, Contact contact) {
		User user = uMapper.insert(u);
		contact.setOwner(user);
		user.setUserContact(ContactMapper.contactMapper().insertContact(contact));
		return UserMapper.userMapper().update(user);
		
	}
	
	public User getUserByID(double id) {
		User user = uMapper.findById(id);
		user.setUserContact(ContactMapper.contactMapper().addOwnContact(user));
		return user;
		
	}
	
	public User getUserBygMail(String email) {
		User user = uMapper.findByEmail(email);
		user.setUserContact(ContactMapper.contactMapper().addOwnContact(user));
		return user;
		
	}
	
	// Nur für Report!
	public Vector<User> getAllUsers(){
		Vector<User> userVector = uMapper.findAll();
		for(User user : userVector){
			user.setUserContact(ContactMapper.contactMapper().addOwnContact(user));
		}
		return userVector;
		
	}
		
	public User editUser(User user) {
		return uMapper.update(user);		
	}
	
	public User deleteUser(User user) {
		ContactMapper.contactMapper().deleteAllContactsByUser(user.getGoogleID());
		deleteContactListByUserId(user.getGoogleID());
		return uMapper.delete(user);
	}
	
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Contact
	* ***************************************************************************
	*/
	
	@Override
	public PropertyValue getNameOfContact(Contact c) {
		return propValMapper.findName(c);
	}

	// Nur für Report!
	public Vector<Contact> getAllContacts(){
		return cMapper.findAllContacts();
		
	}
	
	// Nur für Report!
	public Vector<Contact> getAllContactsFromUser(){
		return cMapper.findAllContactsByUser(Double.parseDouble(userService.getCurrentUser().getUserId()));
		
	}
	
	// Nur Intern Verwendet
	public Contact getContactOf(User u) {
		return cMapper.findOwnContact(u);
	}
	
	public Contact editContact(Contact contact) {
		return cMapper.updateContact(contact);
		
	}	
	
	public Vector<Contact> getContactsFromUser(User user) {
		return cMapper.findAllContactsByUser(user);
		
	}
	
	public Vector<Contact> getContactsFromList(ContactList cl) {
		return cMapper.findContactFromList(cl);
		
	}
	
	@Override
	public Contact getContactById(int id) {
		return cMapper.findContactById(id);
	}
	
	
	
	public Contact createContact(Contact contact) {
		return cMapper.insertContact(contact);
		
	}
	

	public Contact deleteContact(Contact contact) {
		return cMapper.deleteContact(contact);
	}
	
	/*
	* ***************************************************************************
	* ABSCHNITT, ContactList
	* ***************************************************************************
	*/
	
	public ContactList createContactList(ContactList contactList) {

		BusinessObjectMapper.businessObjectMapper().insert(contactList);
		return clMapper.insertContactList(contactList);
			
	}	

	@Override
	public ContactList editContactList(ContactList cl) {
		BusinessObjectMapper.businessObjectMapper().update(cl);
		return clMapper.updateContactList(cl);
	}
	
	public Vector <ContactList> getContactListByName(String name) {
		
		Vector<ContactList> contactListVector = clMapper.findContactListByName(name);
		
		for(ContactList cl : contactListVector){
			cl.setOwner(getUserByID(cl.getOwner().getGoogleID()));
			cl.setContacts(getContactsFromList(cl));
		}
		return contactListVector;
		
	}
	
	// Nur für Report!
	@Override
	public Vector<ContactList> getAllContactLists() {
		Vector<ContactList> contactListVector = clMapper.findAllContactLists();
		
		for(ContactList cl : contactListVector){
			cl.setOwner(getUserByID(cl.getOwner().getGoogleID()));
			cl.setContacts(getContactsFromList(cl));
		}
		return contactListVector;
	}
	
	// Nur für Report!
		@Override
		public Vector<ContactList> getAllContactListsFromUser() {
			
			Vector<ContactList> contactListVector = clMapper.findContactListByUserId(
													Double.parseDouble(userService.getCurrentUser().getUserId()));
			
			for(ContactList cl : contactListVector){
				cl.setOwner(getUserByID(cl.getOwner().getGoogleID()));
				cl.setContacts(getContactsFromList(cl));
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
		if(cl != null) BusinessObjectMapper.businessObjectMapper().deleteBusinessObjectByID(cl.getBoId());
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
		return propValMapper.insert(propertyValue);
	}
	
	
	public PropertyValue editPropertyValue(PropertyValue propertyValue) {
		return propValMapper.update(propertyValue);
	}
	

	@Override
	public PropertyValue deletePropertyValue(PropertyValue pv) {
		return propValMapper.delete(pv);
	}
	
	@Override
	public PropertyValue getPropertyValueForContactByName(String name, Contact c) {
		return propValMapper.findName(c);
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



	


	
	
	
}