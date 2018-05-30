package de.hdm.kontaktsystem.shared;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.kontaktsystem.client.gui.ContactSystem;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

@RemoteServiceRelativePath("editor")
public interface ContactSystemAdministration extends RemoteService {
	
	public User getUserByID(int id);
	
	public User getUserBygMail(String gMail);
	
	public Vector<User> getAllUsers();
	
	
	
	public Vector<Contact> getAllContacts(); 
	
	//Aus Klassendiagramm: was genau macht diese Methode?
	public Contact getContactOf(User u);
	
	public Contact getContactById(int id);
	
	//Erstellen des eigenen Kontakts eines Users
	public Contact createContactForUser(User u);
	
	
	public Vector<ContactList> getAllContactLists();
	
	public ContactList getContactListById(int id);
	
	public Vector<ContactList> getContactListByName(String name);
	
	
	//Aus Klassendiagramm: Kann das so stimmen?
	public Participation sharePropertyValueOfContact(Contact c, Participation part);
	
	public PropertyValue getPropertyValueForContactByName(String name, Contact c);

	
	public ContactList addContactToList(Contact c, ContactList cl);
	
	public PropertyValue getNameOfContact(Contact c);

	/**
	 * Create
	 */
	//Zu klären: was wird jeweils bei create übergeben?
	public User createUser(User u, Contact c);
	
	public Contact createContact(Contact c);
	
	public ContactList createContactList(ContactList cl);
	
	public PropertyValue createPropertyValue(PropertyValue pv);
	
	//Rückgabe von Participation Objekt anhand GUI Eingabe?
	public Participation createParticipation(Participation part);

	/**
	 * Update
	 */
	//Zu klären: was wird jeweils als Parameter übergeben?
	public User editUser(User u);
	
	public Contact editContact(Contact c);
	
	public ContactList editContactList(ContactList cl);
	
	public PropertyValue editPropertyValue(PropertyValue pv);
	
	
	/**
	 * Delete
	 */
	public User deleteUser(User u);
	
	public Contact deleteContact(Contact c);
	
	public ContactList deleteContactList(ContactList cl);
	
	public PropertyValue deletePropertyValue(PropertyValue pv);
	
	public Participation deleteParticipation(Participation p);
	
	public ContactList removeContactFromList(Contact contact, ContactList contactList);
	

	public Participation shareContactWith(Participation part);
	
	public Participation shareContactListWith(Participation part);
	
	public Vector<PropertyValue> getPropertyValuesForContact(Contact c);
	
	public Vector<Participation> getAllParticipationsByOwner(User u);

	
	public Vector<Participation> getAllParticipationsByParticipant(User participant);


}
