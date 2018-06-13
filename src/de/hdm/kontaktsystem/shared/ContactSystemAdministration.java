package de.hdm.kontaktsystem.shared;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
	
	
	
	public User login(String requestUri);
	
	public User getUserByID(double id);
	
	public User getUserBygMail(String gMail);
	
	public Vector<User> getAllUsers();
	
	public User getAccountOwner(); //TODO
	

	public Vector<Contact> getAllContactsFromUser(); 
	
	public Vector<Contact> getMyContactsPrev();

	public Vector<Contact> getAllContacts(); 

	
	public Contact getContactByPropertyValue(PropertyValue pv);
	
	public Vector<Contact> getContactsByStatus(Boolean status);
	
	public Contact getContactById(int id);
	
	public Vector<ContactList> getAllContactListsFromUser();
	
	public Vector<ContactList> getMyContactListsPrev();
	
	public Vector<ContactList> getAllContactLists();
	
	public ContactList getContactListById(int id);
	
	public Vector<ContactList> getContactListByName(String name);
	
	public PropertyValue getNameOfContact(Contact c);

	public ContactList addContactToList(Contact c, ContactList cl);
	
	public Vector<Contact> searchContacts(String value);
	
	public Vector<PropertyValue> getPropertyValuesForContact(Contact c); 
	
	public Vector<PropertyValue> searchPropertyValues(String suchtext);
	
	public Vector<Property> getAllProperties();
	
	public Property getPropertyByID(int id);
	
	public Vector<BusinessObject> getAllSharedByMe();
	
	public Vector<BusinessObject> getAllSharedByOthersToMe();

	/**
	 * Create
	 */
	public User createUser(User u, Contact c);
	
	public Contact createContact(Contact c);
	
	public ContactList createContactList(ContactList cl);
	
	public PropertyValue createPropertyValue(PropertyValue pv);
	
	public Participation createParticipation(Participation part);

	/**
	 * Update
	 */
	public User editUser(User u);
	
	public Contact editContact(Contact c);
	
	public ContactList editContactList(ContactList cl);
	
	public PropertyValue editPropertyValue(PropertyValue pv);
	
	public Participation editParticpation(Participation part);
		
	/**
	 * Delete
	 */
	public User deleteUser(User u);
	
	public Contact deleteContact(Contact c);
	
	public ContactList deleteContactList(ContactList cl);
	
	public PropertyValue deletePropertyValue(PropertyValue pv);
	
	public Participation deleteParticipation(Participation p);
	
	public ContactList removeContactFromList(Contact contact, ContactList contactList);
	
	/*
	 * Get all
	 */
	
	public Vector<Participation> getAllParticipationsByOwner(User u);
	
	public Vector<Participation> getAllParticipationsByParticipant(User participant);
	
	public Vector<Participation> getAllParticipationsByBusinessObject(BusinessObject bo);
	
	public Vector<Contact> findAllCSharedByOthersToMe();
	


}
