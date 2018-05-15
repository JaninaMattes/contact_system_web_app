package de.hdm.kontaktsystem.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.kontaktsystem.server.db.BusinessObjectMapper;
import de.hdm.kontaktsystem.server.db.ContactListMapper;
import de.hdm.kontaktsystem.server.db.ContactMapper;
import de.hdm.kontaktsystem.server.db.ParticipationMapper;
import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.ContactSystemAdministration;
import de.hdm.kontaktsystem.shared.bo.Contact;



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
	public User createUser() {
		return user;
		
	}
	
	public Vector<User> getUserByName(String name){
		return null;
		
	}
	
	public User getUserByID(int id) {
		return user;
		
	}
	
	public Vector <User> getAllUsers(){
		return null;
		
	}
	
	public void saveUser(User user) {
		
	}
	
	public void deleteUser(int id) {
		
	}
	
	
	public Vector<Contact> getAllContacts(){
		return null;
		
	}
	
	public Contact getContactFromUser(User user) {
		return contact;
		
	}
	
	public Contact getContactByID(int it) {
		return contact;
		
	}
	
	public Contact createContactForUser(User user) {
		return contact;
		
	}
	
	public void addContactToList(Contact contact, ContactList conList) {
		
	}
	

	
	
	public void shareContactWith(Contact contact, User user) {
		
	}
	
	public void shareContactListWith(ContactList contactList, User user) {
		
	}
	
	public void sharePropertyValueOfContactWith(Contact contact, User user) {
		
	}
	
	public void sharePropertyOfContactWith(Contact contact, User user) {
		
	}
	
	public PropertyValue getPropertyValueOfContact(Contact contact) {
		
	}
	
	public Property getPropertyOfContact(Contact contact) {
		return null;
		
	}
	
	public Contact createContact() {
		return contact;
		
	}
	
	public ContactList createContactList() {
		return null;
			
	}
	
	public Property createProperty() {
		return null;
		
	}
	
	public PropertyValue createPropertyValue() {
		return null;
	}
	
	
	public ContactList getContactListByName(String name) {
		return null;
		
	}
	
	public User editUser() {
		return user;
		
	}
	
	public Contact editContact() {
		return contact;
		
	}
	
	public Property editProperty() {
		return null;
		
	}
	
	public PropertyValue editPropertyValue() {
		return null;
	}
	
	
	

	public void deleteContact(Contact contact) {
		
	}
	
	public void deleteContactList(ContactList contList) {
		
	}
	
	public void deleteProperty(Property prop) {
		
	}
	
	public void deletePropertyValue(PropertyValue <T> propVal) {
		
	}
	
	*
	*
	*/
	
	
}