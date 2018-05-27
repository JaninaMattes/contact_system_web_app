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
	
	public void sharePropertyValueOfContact(AsyncCallback<Void> callback);

	//Wo wird das hier verwendet?
	public void getContactSystem(AsyncCallback<ContactSystem> callback);
	
	public void setContactSystem(ContactSystem cs, AsyncCallback<Void> callback);
	
	
	public void getUserByID(int id, AsyncCallback<User> callback);
	
	public void getUserByName(String name, AsyncCallback<User> callback);
	
	public void getAllUsers(AsyncCallback<Vector<User>> callback);
	
	//Aus Klassendiagramm: was genau macht diese Methode? save = update?
	public void saveUser(User u, AsyncCallback<Void> callback);
	
	
	public void getAllContacts(AsyncCallback<Vector<Contact>> callback);
	
	//Aus Klassendiagramm: was genau macht diese Methode?
	public void getContactOf(AsyncCallback<Contact> callback);
	
	public void getContactById(int id, AsyncCallback<Contact> callback);
	
	//Erstellen des eigenen Kontakts eines Users
	public void createContactForUser(User u, AsyncCallback<Contact> callback);
	
	//Aufruf von createParticipation
	public void shareContact(User participant, AsyncCallback<Void> callback);
	
	
	public void getAllContactLists(AsyncCallback<Vector<ContactList>> callback);
	
	public void getContactListById(int id, AsyncCallback<ContactList> callback);
	
	public void getContactListByName(String name, AsyncCallback<Vector<ContactList>> callback);
	
	//Aufruf von createParticipation
	public void shareContactList(User participant, AsyncCallback<Void> callback);
	
	
	//Aus Klassendiagramm: Kann das so stimmen?
	public void sharePropertyValueOfContact(Contact c, AsyncCallback<PropertyValue> callback);
	
	public void getPropertyValuesForContact(Contact c, AsyncCallback<PropertyValue> callback);
	
	public void getPropertyValueForContactByName(String name, Contact c, AsyncCallback<PropertyValue> callback);
	
	
	public void getAllParticipationsByOwner(User owner, AsyncCallback<Vector<Participation>> callback);
	
	public void getAllParticipationsByParticipant(User participant, AsyncCallback<Vector<Participation>> callback);
	
	
	public void addContactToList(Contact c, ContactList cl, AsyncCallback<Void> callback);
	
	public void getNameOfContact(AsyncCallback<String> callback);

	/**
	 * Create
	 */
	//Zu klären: was wird jeweils bei create übergeben?
	public void createUser(AsyncCallback<User> callback);
	
	public void createContact(AsyncCallback<Contact> callback);
	
	public void createContactList(AsyncCallback<ContactList> callback);
	
	public void createProperty(AsyncCallback<Property> callback); //?
	
	public void createPropertyValue(AsyncCallback<PropertyValue> callback);
	
	public void createParticipation(BusinessObject reference, User participant, AsyncCallback<Participation> callback);

	/**
	 * Update
	 */
	//Zu klären: was wird jeweils als Parameter übergeben?
	public void editUser(AsyncCallback<User> callback);
	
	public void editContact(AsyncCallback<Contact> callback);
	
	public void editContactList(AsyncCallback<ContactList> callback);
	
	public void editProperty(AsyncCallback<Property> callback); //?
	
	public void editPropertyValue(AsyncCallback<PropertyValue> callback);
	
	
	/**
	 * Delete
	 */
	public void deleteUser(User u, AsyncCallback<Void> callback);
	
	public void deleteContact(Contact c, AsyncCallback<Void> callback);
	
	public void deleteContactList(ContactList cl, AsyncCallback<Void> callback);
	
	public void deleteProperty(Property p, AsyncCallback<Void> callback); //?
	
	public void deletePropertyValue(PropertyValue pv, AsyncCallback<Void> callback);
	
	public void deleteParticipation(Participation p, AsyncCallback<Void> callback);



}
