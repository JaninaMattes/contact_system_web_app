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
	
	public User createUser(User u, Contact contact) {
		return uMapper.insert(u, contact);
		
	}
	
	public User getUserByID(int id) {
		return uMapper.findById(id);
		
	}
	
	// Nur für Report!
	public Vector<User> getAllUsers(){
		return uMapper.findAll();
		
	}
	

	public void saveUser(User user) { //?? TODO: Klären
		uMapper.update(user);
	}
	
	
	public User deleteUser(User user) {
		return uMapper.delete(user);
	}
	
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Beginn: Methoden 
	* ***************************************************************************
	*/
	

	// Nur für Report!
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

	
	public void shareBusinessObjectWith(BusinessObject reference, User participant) {
		Participation part = new Participation();
		part.setParticipant(participant);
		part.setReference(reference);
		
		partMapper.insertParticipation(part);
		
	}
		
	public Contact createContactForUser(User u) {
		return cMapper.insertContact(u.getUserContact());
		
	}
	
	public ContactList addContactToList(Contact contact, ContactList contactList) {
		return clMapper.addContactToContactlist(contactList, contact);
		 
	}
	public ContactList removeContactFromList(Contact contact, ContactList contactList) {
		return clMapper.removeContactFromContactList(contactList, contact);

	}
	
	
	public Contact createContact(Contact contact) {
		return cMapper.insertContact(contact);
		
	}
	
	public ContactList createContactList(ContactList contactList) {
		return clMapper.insertContactList(contactList);
			
	}	
	
	public PropertyValue createPropertyValue(PropertyValue propertyValue) {
		return propValMapper.insert(propertyValue);
	}
	
	
	public Vector <ContactList> getContactListByName(String name) {
		return clMapper.findContactListByName(name);
		
	}	

	public User editUser(User user) {
		return uMapper.update(user);		
	}
	
	public Contact editContact(Contact contact) {
		return cMapper.updateContact(contact);
		
	}	
	
	public PropertyValue editPropertyValue(PropertyValue propertyValue) {
		return propValMapper.update(propertyValue);
	}
	
	
	

	public Contact deleteContact(Contact contact) {
		return cMapper.deleteContact(contact);
	}
	
	public ContactList deleteContactList(ContactList contactList) {
		return clMapper.deleteContactList(contactList);
	}
	

	// Benötigt?
	@Override
	public ContactSystem getContactSystem() {
		return null;
	}


	// Kein Mapper? Rückgabewert?
	@Override
	public ContactSystem setContactSystem(ContactSystem cs) {
		return null;
	}


	@Override
	public User getUserBygMail(String gMail) {
		return uMapper.findByEmail(gMail);
	}


	public Contact getContactOf(User u) {
		return cMapper.findOwnContact(u);
	}


	@Override
	public Contact getContactById(int id) {
		return cMapper.findContactById(id);
	}



	public Participation shareContactWith(Participation part) {
		return partMapper.insertParticipation(part);
		
	}

	// Nur für Report!
	@Override
	public Vector<ContactList> getAllContactLists() {
		return clMapper.findAllContactLists();
	}


	@Override
	public ContactList getContactListById(int id) {
		return clMapper.findContactListById(id);
	}


	

	@Override
	public Participation shareContactListWith(Participation part) {
		return partMapper.insertParticipation(part);
		
	}


	@Override
	public Participation sharePropertyValueOfContact(Contact c, Participation part) {
		return partMapper.insertParticipation(part);
	}


	@Override
	public PropertyValue getPropertyValueForContactByName(String name, Contact c) {
		return propValMapper.findName(c);
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
	public PropertyValue getNameOfContact(Contact c) {
		return propValMapper.findName(c);
	}


	@Override
	public Participation createParticipation(Participation part) {
		return partMapper.insertParticipation(part);
	}


	@Override
	public ContactList editContactList(ContactList cl) {
		return clMapper.updateContactList(cl);
	}


	@Override
	public Participation deleteParticipation(Participation p) {
		return partMapper.deleteParticipation(p);
		
	}

	@Override
	public PropertyValue deletePropertyValue(PropertyValue pv) {
		return propValMapper.delete(pv);
	}

	@Override
	public Vector<PropertyValue> getPropertyValuesForContact(Contact c) {
		return propValMapper.findBy(c);
	}

	@Override
	public User saveUser(User u, Contact c) {
		return uMapper.insert(u, c);
	}


	
	
	
}