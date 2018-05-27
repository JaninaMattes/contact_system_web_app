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
	
	//Wo wird das hier verwendet?
	public ContactSystem getContactSystem();
	
	public void setContactSystem(ContactSystem cs);
	
	
	public User getUserByID(int id);
	
	public User getUserByName(String name);
	
	public Vector<User> getAllUsers();
	
	//Aus Klassendiagramm: was genau macht diese Methode? save = update?
	public void saveUser(User u);
	
	
	public Vector<Contact> getAllContacts();
	
	//Aus Klassendiagramm: was genau macht diese Methode?
	public Contact getContactOf();
	
	public Contact getContactById(int id);
	
	//Erstellen des eigenen Kontakts eines Users
	public Contact createContactForUser(User u);
	
	//Aufruf von createParticipation
	public void shareContact(User participant);
	
	
	public Vector<ContactList> getAllContactLists();
	
	public ContactList getContactListById(int id);
	
	public Vector<ContactList> getContactListByName(String name);
	
	//Aufruf von createParticipation
	public void shareContactList(User participant);
	
	public void sharePropertyValueOfContact();
	
	
	//Aus Klassendiagramm: Kann das so stimmen?
	public PropertyValue sharePropertyValueOfContact(Contact c);
	
	public PropertyValue getPropertyValuesForContact(Contact c);
	
	public PropertyValue getPropertyValueForContactByName(String name, Contact c);
	
	
	public Vector<Participation> getAllParticipationsByOwner(User owner);
	
	public Vector<Participation> getAllParticipationsByParticipant(User participant);
	
	
	public void addContactToList(Contact c, ContactList cl);
	
	public String getNameOfContact();

	/**
	 * Create
	 */
	//Zu klären: was wird jeweils bei create übergeben?
	public User createUser();
	
	public Contact createContact();
	
	public ContactList createContactList();
	
	public Property createProperty(); //?
	
	public PropertyValue createPropertyValue();
	
	public Participation createParticipation(BusinessObject reference, User participant);

	/**
	 * Update
	 */
	//Zu klären: was wird jeweils als Parameter übergeben?
	public User editUser();
	
	public Contact editContact();
	
	public ContactList editContactList();
	
	public Property editProperty(); //?
	
	public PropertyValue editPropertyValue();
	
	
	/**
	 * Delete
	 */
	public void deleteUser(User u);
	
	public void deleteContact(Contact c);
	
	public void deleteContactList(ContactList cl);
	
	public void deleteProperty(Property p); //?
	
	public void deletePropertyValue(PropertyValue pv);
	
	public void deleteParticipation(Participation p);


}
