package de.hdm.kontaktsystem.server;

import java.util.Vector;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


import de.hdm.kontaktsystem.shared.ContactSystemAdministration;
import de.hdm.kontaktsystem.shared.bo.*;
import de.hdm.kontaktsystem.server.db.*;



public class ContactSystemAdministrationImpl extends RemoteServiceServlet implements ContactSystemAdministration{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
			
	private Contact contact = null;
	
	//Referenzen auf die zugehörigen DatenbankMapper

	
	private BusinessObjectMapper boMapper = null;	
	private ContactListMapper clMapper = null;
	private ContactMapper cMapper = null;
	private ParticipationMapper partMapper = null;
	private PropertyMapper propMapper = null;
	private PropertyValueMapper propValMapper = null;
	private UserMapper uMapper = null;
	
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Beginn: Initialisierung
	* ***************************************************************************
	*/
	
	public ContactSystemAdministrationImpl() throws IllegalArgumentException {
		
	}
	
	
	//TODO: Ausformulieren der Mapper Klassen
	
	public void init() throws IllegalArgumentException{
		
		this.boMapper = BusinessObjectMapper.businessObjectMapper();
		this.clMapper = ContactListMapper.contactListMapper();
		this.cMapper = ContactMapper.contactMapper();
		this.partMapper = ParticipationMapper.participationMapper();
		this.propMapper = PropertyMapper.propertyMapper(); 
		this.propValMapper = PropertyValueMapper.propertyValueMapper();
		this.uMapper = UserMapper.userMapper();
		
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
	
	public void createUser(User user) {
		uMapper.insert(user);
		
	}
	
	public User getUserByEmail(String email){
		return uMapper.findByEmail(email);
		
	}
	
	public User getUserByID(int id) {
		return uMapper.findById(id);
		
	}
	
	public Vector<User> getAllUsers(){
		return uMapper.findAll();
		
	}
	
	public void saveUser(User user) {
		//?????
	}
	
	public void deleteUser(int id) {
		uMapper.deleteByID(id);;
	}
	
	public void deleteUser(User user) {
		uMapper.delete(user);;
	}
	
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Beginn: Methoden 
	* ***************************************************************************
	*/
	public Vector<Contact> getAllContacts(){
		return cMapper.findAllContacts();
		
	}
	
	public Vector<Contact> getContactFromUser(User user) {
		return cMapper.findAllContactsByUser(user);
		
	}
	
	public Contact getContactByID(int id) {
		return cMapper.findContactById(id);
		
	}
	
	public void createContactForUser(Contact contact) {
		cMapper.insertContact(contact);
		
	}
	
	public void addContactToList(Contact contact, ContactList contactList) {
		clMapper.addContactToContactlist(contactList, contact);
	}
	public void removeContactToList(Contact contact, ContactList contactList) {
		clMapper.removeContactFromContactList(contactList, contact);
	}
	
	public void shareBusinessObjectWith(Participation part) {
		partMapper.insertParticipation(part);
	}
	
	
	public Vector<PropertyValue> getPropertyValuesOfContact(Contact contact) {
		return propValMapper.findBy(contact);
	}
	
	
	public void createContact(Contact contact) {
		cMapper.insertContact(contact);
		
	}
	
	public void createContactList(ContactList contactList) {
		clMapper.insertContactList(contactList);
			
	}
	
	
	public void createPropertyValue(PropertyValue propertyValue) {
		propValMapper.insert(propertyValue);
	}
	
	
	public Vector <ContactList> getContactListByName(String name) {
		return clMapper.findContactListByName(name);
		
	}
	
	public void editUser(User user) {
		uMapper.update(user);
		
	}
	
	public void editContact(Contact contact) {
		cMapper.updateContact(contact);
		
	}
	
	
	public void editPropertyValue(PropertyValue propertyValue) {
		propValMapper.update(propertyValue);
	}
	
	
	

	public void deleteContact(Contact contact) {
		cMapper.deleteContact(contact);
	}
	
	public void deleteContactList(ContactList contactList) {
		clMapper.deleteContactList(contactList);
	}
	
	
	public void deletePropertyValue(int id) {
		propValMapper.deleteByPropValue(id);
	}
	
	
	
}