package de.hdm.kontaktsystem.shared;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public interface ContactSystemAdministrationAsync {
		
	public void login(String requestUri, AsyncCallback<User> callback);
		
	public void getUserByID(double id, AsyncCallback<User> callback); //Aufruf in Report, gebraucht: googleID, Name (über UserContact.getName().getValue())
	
	public void getUserBygMail(String gMail, AsyncCallback<User> callback);
	
	
	public void getAllUsers(AsyncCallback<Vector<User>> callback); //Aufruf in Report, gebraucht: googleID, Name (über UserContact.getName().getValue())  
																   // Aufruf in Kontaktliste, -> Callback aller im System existenten User zur Befüllung der Listbox mit Usern an die Kontaktliste geteilt werden kann 

	
	public void getAccountOwner(AsyncCallback<User> callback); //TODO //Aufruf in Report, gebraucht: User-Objekt mit GoogleID, gMail, Name (über UserContact.getName().getValue())
	

	public void getAllContactsFromUser(AsyncCallback<Vector<Contact>> callback); //Aufruf in Report, gebraucht: Kontakt-Objekte (boID, owner: Name und ID, Status, PropertyValues mit Namen sowie Bezeichnung der zugehörigen Property)  
																				 // Aufruf für Kontaktliste: Aufruf für Befüllung der Listbox mit allen Kontakten des Users zum Hinzufügen zur Kontaktliste

	
	public void getMyContactsPrev(AsyncCallback<Vector<Contact>> callback);
	
	public void getAllContacts(AsyncCallback<Vector<Contact>> callback);
	
	public void getContactByPropertyValue(PropertyValue pv, AsyncCallback<Contact> callback); //Aufruf in Report, gebraucht: Kontakt-Objekte (boID, owner: Name und ID, Status, PropertyValues mit Namen sowie Bezeichung und boID der zugehörigen Property)
	
	public void getContactsByStatus(Boolean status, AsyncCallback<Vector<Contact>> callback);
	

	public void getContactsFromList(ContactList cl, AsyncCallback<Vector<Contact>> callback);
	
	public void getContactById(int id, AsyncCallback<Contact> callback);
	
	
	public void getAllContactListsFromUser(AsyncCallback<Vector<ContactList>> callback); // Aufruf in Kontaktliste: Callback mit allen Kontaktlisten die der User angelegt oder die ihm geteilt wurden
	
	public void getMyContactListsPrev(AsyncCallback<Vector<ContactList>> callback);
	
	public void getAllContactLists(AsyncCallback<Vector<ContactList>> callback);
	
	public void getContactListById(int id, AsyncCallback<ContactList> callback);
	
	public void getContactListByName(String name, AsyncCallback<Vector<ContactList>> callback);
	
	public void getAllParticipationsByOwner(User owner, AsyncCallback<Vector<Participation>> callback); //Aufruf in Report, gebraucht: referencedObject (Kontakt mit boID, owner: Name und ID, Status, PropertyValues mit Namen sowie Bezeichnung der zugehörigen Property)
	
	public void getAllParticipationsByParticipant(User gMail, AsyncCallback<Vector<Participation>> callback); //Aufruf in Report, gebraucht: referencedObject (Kontakt mit boID, owner: Name und ID, Status, PropertyValues mit Namen sowie Bezeichnung der zugehörigen Property)
	

	public void getAllParticipationsByBusinessObject(BusinessObject bo, AsyncCallback<Vector<Participation>> callback); //mehrfach Aufruf in Report, gebraucht (1): Teilhaber-Objekte (Namen der Teilhaber)), 
																														//bzw. gebraucht (2): participantID, referencedObject (Kontakt mit boID, owner: Name und ID, Status, PropertyValues mit Namen sowie Bezeichnung der zugehörigen Property)
																														//bzw. gebraucht (3): participantID
																														// Aufruf in Kontaktliste: Callback mit allen Usern denen die Kontaktliste geteilt wurde

	public void getNameOfContact(Contact c, AsyncCallback<PropertyValue> callback);
	
	public void getPropertyValuesForContact(Contact c, AsyncCallback<Vector<PropertyValue>> callback);
	
	public void searchPropertyValues(String suchtext, AsyncCallback<Vector<PropertyValue>> callback); //Aufruf in Report, gebraucht: boID, boID der zugehörigen Property

	public void getAllProperties(AsyncCallback<Vector<Property>> callback); //Aufruf in Report, gebraucht: Bezeichnung, boID
	
	public void getPropertyByID(int id, AsyncCallback<Property> callback); //Aufruf in Report, gebraucht: Bezeichnung, boID
	
	public void getAllSharedByMe(AsyncCallback<Vector<BusinessObject>> callback);
	
	public void getAllSharedByOthersToMe(AsyncCallback<Vector<BusinessObject>> callback);
	
	public void findAllCSharedByOthersToMe(AsyncCallback<Vector<Contact>> callback); //Aufruf in Report, gebraucht: Kontakt-Objekte (boID, owner: Name und ID, Status, PropertyValues mit Namen sowie Bezeichnung der zugehörigen Property)
	
	
	/**
	 * Methode für die Suchfunktion
	 * @param value
	 * @param callback
	 */
	
	public void searchContacts(String value, AsyncCallback<Vector<Contact>> callback);

	/**
	 * Create
	 */
	
	public void createUser(User u, Contact c, AsyncCallback<User> callback);
	
	public void createContact(Contact c, AsyncCallback<Contact> callback);
	
	/*
	 * Speichern neu angelegter Kontaktlisten
	 * -> Kontaktliste
	 */
	
	public void createContactList(ContactList cl, AsyncCallback<ContactList> callback);
	
	public void createPropertyValue(PropertyValue pv, AsyncCallback<PropertyValue> callback);
	
	public void createParticipation(Participation part, AsyncCallback<Participation> callback);
	
	
	public void addContactToList(Contact c, ContactList cl, AsyncCallback<ContactList> callback); // Aufruf in Kontaktliset: Hinzufügen eines ausgewählten Kontakt von User (geteilt oder erstellt) zu ausgewählter Kontaktliste

	/**
	 * Update
	 */
	
	public void editUser(User u, AsyncCallback<User> callback);
	
	public void editContact(Contact c, AsyncCallback<Contact> callback);

	
	public void editContactList(ContactList cl, AsyncCallback<ContactList> callback); // Aufruf in Kontaktliste: Editieren bereits existenter Kontaktlisten
	
	public void editPropertyValue(PropertyValue pv, AsyncCallback<PropertyValue> callback);	
	public void editParticpation(Participation part, AsyncCallback<Participation> callback);
	
	
	/**
	 * Delete
	 */
	public void deleteUser(User u, AsyncCallback<User> callback);
	
	public void deleteContact(Contact c, AsyncCallback<Contact> callback);
	
	public void deleteContactList(ContactList cl, AsyncCallback<ContactList> callback);  // Aufruf in Kontaktliste: Löschen von Kontaktlisten
	
	public void deletePropertyValue(PropertyValue pv, AsyncCallback<PropertyValue> callback);
	
	public void deleteParticipation(Participation p, AsyncCallback<Participation> callback);
	
	
	public void removeContactFromList(Contact contact, ContactList contactList, AsyncCallback<ContactList> callback); // Aufruf in Kontaktliste: Entfernen von Kontakten aus Kontaktliste

	

	

	

	




}
