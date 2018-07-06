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

/**
 * @author OliverGorges
 * @author JaninaMattes
 * @author Kim-Ly Le
 * @author Sandra Prestel
 */

public interface ContactSystemAdministrationAsync {
	
	/**
	 * Aufruf von ContactSystem / ReportGenerator	
	 * @param requestUri
	 * @param callback
	 */
	public void login(String requestUri, AsyncCallback<User> callback);
	
	/**
	 * Aufruf in Report, gebraucht: googleID, Name (ueber UserContact.getName().getValue())
	 * @param id
	 * @param callback
	 */
	public void getUserByID(double id, AsyncCallback<User> callback);
	
	/**
	 * ContactForm: Aufruf des Users mit Gmail/OwnContact/GoogleID
	 * @param gMail
	 * @param callback
	 */
	public void getUserBygMail(String gMail, AsyncCallback<User> callback);
	
	/**
	 * Aufruf in Report, gebraucht: googleID, Name (über UserContact.getName().getValue())  
	 * Aufruf in Kontaktliste, -> Callback aller im System existenten User zur Befuellung der Listbox mit Usern 
	 * an die Kontaktliste geteilt werden kann 
	 * @param callback
	 */
	public void getAllUsers(AsyncCallback<Vector<User>> callback);
	
	/**
	 * Aufruf in Report
	 * @param callback
	 */
	public void findAllKnownUsers(AsyncCallback<Vector<User>> callback);
	
	/**
	 * Aufruf in Report, gebraucht: User-Objekt mit GoogleID, gMail, Name (über UserContact.getName().getValue()).
	 * @param callback
	 */
	public void getAccountOwner(AsyncCallback<User> callback);
	
	/**
	 * Aufruf in Report, gebraucht: Kontakt-Objekte (boID, owner: Name und ID, Status, PropertyValues mit Namen sowie 
	 * Bezeichnung der zugehoerigen Property).
	 * Aufruf fuer Kontaktliste: Aufruf fuer Befuellung der Listbox mit allen Kontakten des Users zum Hinzufuegen zur Kontaktliste.
	 * @param callback
	 */
	public void getAllContactsFromUser(AsyncCallback<Vector<Contact>> callback);

	/**
	 * Aufrauf von ContactSystem -> TreeView.
	 * @param callback
	 */
	public void getMyContactsPrev(AsyncCallback<Vector<Contact>> callback);
	
	public void getContactsFromList(ContactList cl, AsyncCallback< Vector<Contact>> callback);
	
	/**
	 * Aufruf in Report, gebraucht: Kontakt-Objekte (boID, owner: Name und ID, Status, PropertyValues mit Namen sowie 
	 * Bezeichung und boID der zugehoerigen Property).
	 * @param pv
	 * @param callback
	 */
	public void getContactByPropertyValue(PropertyValue pv, AsyncCallback<Contact> callback);
	
	/**
	 * ContactForm: Abruf eines Kontakt Objektes zur initialien Befüllung des Referenzattributs in der 
	 * Klasse - alle Attribute benoetigt.
	 * @param id
	 * @param callback
	 */
	public void getContactById(int id, AsyncCallback<Contact> callback);
	
	/**
	 * Aufruf in Kontaktliste: Callback mit allen Kontaktlisten die der User angelegt oder die ihm geteilt wurden.
	 * @param callback
	 */
	public void getAllContactListsFromUser(AsyncCallback<Vector<ContactList>> callback);
	
	/**
	 * Aufrauf von ContactSystem -> TreeView.
	 * @param callback
	 */
	public void getMyContactListsPrev(AsyncCallback<Vector<ContactList>> callback);

	public void getContactListById(int id, AsyncCallback<ContactList> callback);
	
	/**
	 * Aufruf in Report, gebraucht: referencedObject (Kontakt mit boID, owner: Name und ID, Status, PropertyValues 
	 * mit Namen sowie Bezeichnung der zugehoerigen Property).
	 * @param owner
	 * @param callback
	 */
	public void getAllParticipationsByOwner(User owner, AsyncCallback<Vector<Participation>> callback);
	
	/**
	 * Aufruf in Report, gebraucht: referencedObject (Kontakt mit boID, owner: Name und ID, Status, PropertyValues 
	 * mit Namen sowie Bezeichnung der zugehoerigen Property).
	 * Aufruf in Kontaktliste: Status Label das anzeigt ob Teilhaberschaft oder Besitz der Kontaktliste.
	 * @param gMail
	 * @param callback
	 */
	public void getAllParticipationsByParticipant(User gMail, AsyncCallback<Vector<Participation>> callback);
	
	/**
	 * Mehrfach Aufruf in Report, gebraucht 
	 * (1): Teilhaber-Objekte (Namen der Teilhaber)), bzw. gebraucht 
	 * (2): participantID, referencedObject (Kontakt mit boID, owner: Name und ID, Status, PropertyValues 
	 * mit Namen sowie Bezeichnung der zugehoerigen Property) bzw. gebraucht 
	 * (3): participantID Aufruf in Kontaktliste: Callback mit allen Usern denen die Kontaktliste geteilt wurde
	 * Aufruf in ContactForm 
	 * (1) Methode fuer die Rueckgabe NUR eines einzigen Participation Objekts waere ausreichend, da anhand der Uebergabe 
	 * des User(Participant)+Contacts(BusinessObjects) dies in DB abrufbar
     * Methode getParticipation(BusinessObject, User, AsyncCallback<Participation>)
	 * (2) Methode um ListBox mit allen Participants (User) eines BOs zu befuellen 
	 * @param bo
	 * @param callback
	 */
	public void getAllParticipationsByBusinessObject(BusinessObject bo, AsyncCallback<Vector<Participation>> callback); 

	public void getAllPVFromContactSharedWithUser(Contact c, User u, AsyncCallback<Vector<PropertyValue>> callback);
	
	/**
	 * Aufruf in Report, gebraucht: boID, boID der zugehoerigen Property.
	 * @param suchtext
	 * @param callback
	 */
	public void searchPropertyValues(String suchtext, AsyncCallback<Vector<PropertyValue>> callback);

	/**
	 * Aufruf in Report, gebraucht: Bezeichnung, boID.
	 * @param callback
	 */
	public void getAllProperties(AsyncCallback<Vector<Property>> callback);
	
	/**
	 * Aufruf in Report, gebraucht: Bezeichnung, boID.
	 * @param id
	 * @param callback
	 */
	public void getPropertyByID(int id, AsyncCallback<Property> callback);
	
	/**
	 * TreeView , Nur Huelle.
	 * @param callback
	 */
	public void getAllSharedByMe(AsyncCallback<Vector<BusinessObject>> callback);
	
	/**
	 * TreeView , Nur Huelle.
	 * @param callback
	 */
	public void getAllSharedByOthersToMe(AsyncCallback<Vector<BusinessObject>> callback);
	
	/**
	 *  Aufruf in Report, gebraucht: Kontakt-Objekte (boID, owner: Name und ID, Status, PropertyValues mit 
	 *  Namen sowie Bezeichnung der zugehoerigen Property).
	 * @param callback
	 */
	public void getAllCSharedByOthersToMe(AsyncCallback<Vector<Contact>> callback);
	
	/**
	 * Methode fuer die Suchfunktion.
	 * @param value
	 * @param callback
	 */
	public void search(String value, AsyncCallback<Vector<BusinessObject>> callback);

	/**
	 * Create
	 * ContactForm: Erstellen eines neuen Kontakt Eintrages in DB.
	 */
	public void createContact(Contact c, AsyncCallback<Contact> callback);
	
	/**
	 * Speichern neu angelegter Kontaktlisten -> Kontaktliste.
	 */
	public void createContactList(ContactList cl, AsyncCallback<ContactList> callback);
	
	/**
	 * ContactForm: Erstellen eines neuen Participation Eintrags in der DB.
	 * @param part
	 * @param callback
	 */
	public void createParticipation(Participation part, AsyncCallback<Participation> callback);
	
	/**
	 * Aufruf in Kontaktliset: Hinzufuegen eines ausgewaehlten Kontakt von User (geteilt oder erstellt) 
	 * zu ausgewaehlter Kontaktliste.
	 * @param c
	 * @param cl
	 * @param callback
	 */
	public void addContactToList(Contact c, ContactList cl, AsyncCallback<ContactList> callback);

	public void createProperty(Property p, AsyncCallback<Property> callback);
	
	/**
	 * Update
	 * ContactForm: Uebergabe des kompletten Kontakt-Objektes fuer Update in DB.
	 */
	public void editContact(Contact c, AsyncCallback<Contact> callback);
	
	/**
	 * Aufruf in Kontaktliste: Editieren bereits existenter Kontaktlisten.
	 * @param cl
	 * @param callback
	 */
	public void editContactList(ContactList cl, AsyncCallback<ContactList> callback);
	
	/**
	 * Veraendert den ShareStatus oder die mit einem Kontakt geteilten Eigenschaftsauspraegungen.
	 * @param part
	 * @param callback
	 */
	public void editParticpation(Participation part, AsyncCallback<Participation> callback);
	
	/**
	 * Veraendert die Beschreibung einer Eigenschaft.
	 * @param p
	 * @param callback
	 */
	public void editProperty(Property p, AsyncCallback<Property> callback);
	
	
	/**
	 * Delete
	 * UserForm: Loeschen Des Accounts.
	 */
	public void deleteUser(User u, AsyncCallback<User> callback);
	
	/**
	 * ContctForm: Loeschen eines Kontaktes.
	 * @param c
	 * @param callback
	 */
	public void deleteContact(Contact c, AsyncCallback<Contact> callback);
	
	/**
	 * Aufruf in Kontaktliste: Loeschen von Kontaktlisten.
	 * @param cl
	 * @param callback
	 */
	public void deleteContactList(ContactList cl, AsyncCallback<ContactList> callback);
	
	/**
	 * Aufruf in Kontaktliste: Entfernen von Kontakten aus Kontaktliste
	 * @param contact
	 * @param contactList
	 * @param callback
	 */
	public void removeContactFromList(Contact contact, ContactList contactList, AsyncCallback<ContactList> callback);

	/**
	 * Loescht eine Teilhaberschaft.
	 * @param part
	 * @param callback
	 */
	public void deleteParticipation(Participation part, AsyncCallback<Participation> callback);
	
	/**
	 * Loescht eine Eigenschaft und deren Auspraegungen.
	 * @param p
	 * @param callback
	 */
	public void deleteProperty(Property p, AsyncCallback<Property> callback);
	

	
}
