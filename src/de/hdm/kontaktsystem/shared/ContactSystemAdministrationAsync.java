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
		
	public void login(String requestUri, AsyncCallback<User> callback); // Aufruf von ContactSystem / ReportGenerator 
		
	public void getUserByID(double id, AsyncCallback<User> callback); //Aufruf in Report, gebraucht: googleID, Name (über UserContact.getName().getValue())
	
	public void getUserBygMail(String gMail, AsyncCallback<User> callback); //ContactForm: Aufruf des Users mit Gmail/OwnContact/GoogleID
	
	
	public void getAllUsers(AsyncCallback<Vector<User>> callback); //Aufruf in Report, gebraucht: googleID, Name (über UserContact.getName().getValue())  
																   // Aufruf in Kontaktliste, -> Callback aller im System existenten User zur Befüllung der Listbox mit Usern an die Kontaktliste geteilt werden kann 

	
	public void getAccountOwner(AsyncCallback<User> callback);  //Aufruf in Report, gebraucht: User-Objekt mit GoogleID, gMail, Name (über UserContact.getName().getValue())
	

	public void getAllContactsFromUser(AsyncCallback<Vector<Contact>> callback); //Aufruf in Report, gebraucht: Kontakt-Objekte (boID, owner: Name und ID, Status, PropertyValues mit Namen sowie Bezeichnung der zugehörigen Property)  
																				 // Aufruf für Kontaktliste: Aufruf für Befüllung der Listbox mit allen Kontakten des Users zum Hinzufügen zur Kontaktliste

	
	public void getMyContactsPrev(AsyncCallback<Vector<Contact>> callback); // Aufrauf von ContactSystem -> TreeView
	
	public void getContactsFromList(ContactList cl, AsyncCallback< Vector<Contact>> callback);
	
	public void getContactByPropertyValue(PropertyValue pv, AsyncCallback<Contact> callback); //Aufruf in Report, gebraucht: Kontakt-Objekte (boID, owner: Name und ID, Status, PropertyValues mit Namen sowie Bezeichung und boID der zugehörigen Property)
	
	
	public void getContactById(int id, AsyncCallback<Contact> callback); //ContactForm: Abruf eines Kontakt Objektes zur initialien Befüllung des Referenzattributs in der Klasse - alle Attribute benötigt 
	
	
	public void getAllContactListsFromUser(AsyncCallback<Vector<ContactList>> callback); // Aufruf in Kontaktliste: Callback mit allen Kontaktlisten die der User angelegt oder die ihm geteilt wurden
	
	public void getMyContactListsPrev(AsyncCallback<Vector<ContactList>> callback); // Aufrauf von ContactSystem -> TreeView

	
	public void getContactListById(int id, AsyncCallback<ContactList> callback); // Nicht von ContactListForm verwendet ???? 
	
	
	public void getAllParticipationsByOwner(User owner, AsyncCallback<Vector<Participation>> callback); //Aufruf in Report, gebraucht: referencedObject (Kontakt mit boID, owner: Name und ID, Status, PropertyValues mit Namen sowie Bezeichnung der zugehörigen Property)
	
	public void getAllParticipationsByParticipant(User gMail, AsyncCallback<Vector<Participation>> callback); //Aufruf in Report, gebraucht: referencedObject (Kontakt mit boID, owner: Name und ID, Status, PropertyValues mit Namen sowie Bezeichnung der zugehörigen Property)
																											  // Aufruf in Kontaktliste: Status Label das anzeigt ob Teilhaberschaft oder Besitz der Kontaktliste

	public void getAllParticipationsByBusinessObject(BusinessObject bo, AsyncCallback<Vector<Participation>> callback); //mehrfach Aufruf in Report, gebraucht (1): Teilhaber-Objekte (Namen der Teilhaber)), 
																														//bzw. gebraucht (2): participantID, referencedObject (Kontakt mit boID, owner: Name und ID, Status, PropertyValues mit Namen sowie Bezeichnung der zugehörigen Property)
																														//bzw. gebraucht (3): participantID
																														// Aufruf in Kontaktliste: Callback mit allen Usern denen die Kontaktliste geteilt wurde
																														
																														// Aufruf in ContactForm 
																														// (1) Methode für die Rückgabe NUR eines einzigen Participation Objekts wäre ausreichend, da anhand der Übergabe des User(Participant)+Contacts(BusinessObjects) dies in DB abrufbar
																														//     Methode getParticipation(BusinessObject, User, AsyncCallback<Participation>)
																														// (2) Methode um ListBox mit allen Participants (User) eines BOs zu befüllen 

		
	public void searchPropertyValues(String suchtext, AsyncCallback<Vector<PropertyValue>> callback); //Aufruf in Report, gebraucht: boID, boID der zugehörigen Property

	public void getAllPVFromContactSharedWithUser(Contact c, User u, AsyncCallback<Vector<PropertyValue>> callback);
	
	public void getAllProperties(AsyncCallback<Vector<Property>> callback); //Aufruf in Report, gebraucht: Bezeichnung, boID
	
	public void getPropertyByID(int id, AsyncCallback<Property> callback); //Aufruf in Report, gebraucht: Bezeichnung, boID
	
	public void getAllSharedByMe(AsyncCallback<Vector<BusinessObject>> callback); // TreeView , Nur Hülle
	
	public void getAllSharedByOthersToMe(AsyncCallback<Vector<BusinessObject>> callback); // TreeView , Nur Hülle
	
	public void getAllCSharedByOthersToMe(AsyncCallback<Vector<Contact>> callback); //Aufruf in Report, gebraucht: Kontakt-Objekte (boID, owner: Name und ID, Status, PropertyValues mit Namen sowie Bezeichnung der zugehörigen Property)
	
	
	/**
	 * Methode für die Suchfunktion
	 * @param value
	 * @param callback
	 */
	
	public void searchContacts(String value, AsyncCallback<Vector<Contact>> callback);

	/**
	 * Create
	 */
	
	
	public void createContact(Contact c, AsyncCallback<Contact> callback); //ContactForm: Erstellen eines neuen Kontakt Eintrages in DB
	
	/*
	 * Speichern neu angelegter Kontaktlisten
	 * -> Kontaktliste
	 */
	
	public void createContactList(ContactList cl, AsyncCallback<ContactList> callback);
	
	
	public void createParticipation(Participation part, AsyncCallback<Participation> callback); //ContactForm: Erstellen eines neuen Participation Eintrags in der DB
	
	
	public void addContactToList(Contact c, ContactList cl, AsyncCallback<ContactList> callback); // Aufruf in Kontaktliset: Hinzufügen eines ausgewählten Kontakt von User (geteilt oder erstellt) zu ausgewählter Kontaktliste

	/**
	 * Update
	 */
	
	
	public void editContact(Contact c, AsyncCallback<Contact> callback);//ContactForm: Übergabe des kompletten Kontakt-Objektes für Update in DB

	public void editContactList(ContactList cl, AsyncCallback<ContactList> callback); // Aufruf in Kontaktliste: Editieren bereits existenter Kontaktlisten
	
	public void editParticpation(Participation part, AsyncCallback<Participation> callback);
	
	
	/**
	 * Delete
	 */
	public void deleteUser(User u, AsyncCallback<User> callback); // UserForm: Löschen Des Accounts
	
	public void deleteContact(Contact c, AsyncCallback<Contact> callback); //ContctForm: Löschen eines Kontaktes
	
	public void deleteContactList(ContactList cl, AsyncCallback<ContactList> callback);  // Aufruf in Kontaktliste: Löschen von Kontaktlisten
		
	public void removeContactFromList(Contact contact, ContactList contactList, AsyncCallback<ContactList> callback); // Aufruf in Kontaktliste: Entfernen von Kontakten aus Kontaktliste

	
}
