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
	
	public User getAccountOwner(); 
	
	

	public Vector<Contact> getAllContactsFromUser(); 
	
	public Vector<Contact> getMyContactsPrev();
	
	public Vector<Contact> getContactsFromList(ContactList cl);

	
	public Contact getContactByPropertyValue(PropertyValue pv);
	
	
	public Contact getContactById(int id);
	
	public Vector<ContactList> getAllContactListsFromUser();
	
	public Vector<ContactList> getMyContactListsPrev();
	
	public ContactList getContactListById(int id);
	
	public ContactList addContactToList(Contact c, ContactList cl);
	
	public Vector<BusinessObject> search(String value);
	
	public Vector<PropertyValue> searchPropertyValues(String suchtext);
	
	public Vector<PropertyValue> getAllPVFromContactSharedWithUser(Contact c, User u);
	
	public Vector<Property> getAllProperties();
	
	public Property getPropertyByID(int id);
	
	public Vector<BusinessObject> getAllSharedByMe();
	
	public Vector<BusinessObject> getAllSharedByOthersToMe();

	/**
	 * Create
	 */
	
	public Contact createContact(Contact c);
	
	public ContactList createContactList(ContactList cl);
	
	public Participation createParticipation(Participation part);

	/**
	 * Update
	 */
	
	public Contact editContact(Contact c);
	
	public ContactList editContactList(ContactList cl);
	
	public Participation editParticpation(Participation part);
		
	/**
	 * Delete
	 */
	public User deleteUser(User u);
	
	public Contact deleteContact(Contact c);
	
	public ContactList deleteContactList(ContactList cl);
	
	public ContactList removeContactFromList(Contact contact, ContactList contactList);
	
	/*
	 * Get all
	 */
	
	public Vector<Participation> getAllParticipationsByOwner(User u);
	
	public Vector<Participation> getAllParticipationsByParticipant(User participant);
	
	public Vector<Participation> getAllParticipationsByBusinessObject(BusinessObject bo);
	
	public Vector<Contact> getAllCSharedByOthersToMe();
	


}
