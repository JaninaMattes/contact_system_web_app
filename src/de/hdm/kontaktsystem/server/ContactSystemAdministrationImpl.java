package de.hdm.kontaktsystem.server;

import java.util.Vector;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


import de.hdm.kontaktsystem.shared.ContactSystemAdministration;
import de.hdm.kontaktsystem.shared.bo.*;
import de.hdm.kontaktsystem.client.gui.ContactSystem;
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
	
	public void createUser(User user, Contact contact) {
		uMapper.insert(user, contact);
		
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
	
	public void saveUser(User user) { //?? TODO: Klären
		uMapper.update(user);
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
	
	public void createContactForUser(Contact contact) { //??? TOOD: Klären 
		cMapper.insertContact(contact);		
	}
	
	
	public void addContactToList(Contact contact, ContactList contactList) {
		clMapper.addContactToContactlist(contactList, contact);
	}
	
	public void removeContactToList(Contact contact, ContactList contactList) {
		clMapper.removeContactFromContactList(contactList, contact);
	}
	
	public void shareBusinessObjectWith(BusinessObject reference, User participant) {
		Participation part = new Participation();
		part.setParticipant(participant);
		part.setReference(reference);
		
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
	
	public void editUser(User user) { // TODO: Klären, doppelte Methode
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


	@Override
	public ContactSystem getContactSystem() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setContactSystem(ContactSystem cs) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public User getUserByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Contact getContactOf() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Contact getContactById(int id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Contact createContactForUser(User u) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void shareContact(User participant) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Vector<ContactList> getAllContactLists() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ContactList getContactListById(int id) {
		// TODO Auto-generated method stub
		return null;
	}


	

	@Override
	public void shareContactList(User participant) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void sharePropertyValueOfContact() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public PropertyValue sharePropertyValueOfContact(Contact c) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public PropertyValue getPropertyValuesForContact(Contact c) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public PropertyValue getPropertyValueForContactByName(String name, Contact c) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Vector<Participation> getAllParticipationsByOwner(User owner) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Vector<Participation> getAllParticipationsByParticipant(User participant) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getNameOfContact() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public User createUser() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Contact createContact() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ContactList createContactList() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Property createProperty() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public PropertyValue createPropertyValue() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Participation createParticipation(BusinessObject reference, User participant) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public User editUser() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Contact editContact() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ContactList editContactList() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Property editProperty() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public PropertyValue editPropertyValue() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void deleteProperty(Property p) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deletePropertyValue(PropertyValue pv) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteParticipation(Participation p) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}