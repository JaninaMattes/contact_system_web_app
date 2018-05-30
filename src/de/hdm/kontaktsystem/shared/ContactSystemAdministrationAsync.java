package de.hdm.kontaktsystem.shared;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.kontaktsystem.client.gui.ContactSystem;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public interface ContactSystemAdministrationAsync {
		
	
	public void getUserByID(double id, AsyncCallback<User> callback);
	
	public void getUserBygMail(String gMail, AsyncCallback<User> callback);
	
	public void getAllUsers(AsyncCallback<Vector<User>> callback);
	
	public void getAllContactsFromUser(AsyncCallback<Vector<Contact>> callback);
	
	public void getAllContacts(AsyncCallback<Vector<Contact>> callback);
	
	
	public void getContactById(int id, AsyncCallback<Contact> callback);
	
	public void getAllContactListsFromUser(AsyncCallback<Vector<ContactList>> callback);
	
	public void getAllContactLists(AsyncCallback<Vector<ContactList>> callback);
	
	public void getContactListById(int id, AsyncCallback<ContactList> callback);
	
	public void getContactListByName(String name, AsyncCallback<Vector<ContactList>> callback);
	
	
	public void getPropertyValueForContactByName(String name, Contact c, AsyncCallback<PropertyValue> callback);
	
	
	public void getAllParticipationsByOwner(User owner, AsyncCallback<Vector<Participation>> callback);
	
	public void getAllParticipationsByParticipant(User gMail, AsyncCallback<Vector<Participation>> callback);
	
	
	
	public void getNameOfContact(Contact c, AsyncCallback<PropertyValue> callback);

	/**
	 * Create
	 */
	//Zu klären: was wird jeweils bei create übergeben?
	public void createUser(User u, Contact c, AsyncCallback<User> callback);
	
	public void createContact(Contact c, AsyncCallback<Contact> callback);
	
	public void createContactList(ContactList cl, AsyncCallback<ContactList> callback);
	
	public void createPropertyValue(PropertyValue pv, AsyncCallback<PropertyValue> callback);
	
	public void createParticipation(Participation part, AsyncCallback<Participation> callback);
	
	public void addContactToList(Contact c, ContactList cl, AsyncCallback<ContactList> callback);

	/**
	 * Update
	 */
	//Zu klären: was wird jeweils als Parameter übergeben?
	public void editUser(User u, AsyncCallback<User> callback);
	
	public void editContact(Contact c, AsyncCallback<Contact> callback);
	
	public void editContactList(ContactList cl, AsyncCallback<ContactList> callback);
	
	public void editPropertyValue(PropertyValue pv, AsyncCallback<PropertyValue> callback);
	
	
	/**
	 * Delete
	 */
	public void deleteUser(User u, AsyncCallback<User> callback);
	
	public void deleteContact(Contact c, AsyncCallback<Contact> callback);
	
	public void deleteContactList(ContactList cl, AsyncCallback<ContactList> callback);
	
	public void deletePropertyValue(PropertyValue pv, AsyncCallback<PropertyValue> callback);
	
	public void deleteParticipation(Participation p, AsyncCallback<Participation> callback);
	
	public void removeContactFromList(Contact contact, ContactList contactList, AsyncCallback<ContactList> callback);



}
