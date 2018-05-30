package test;

import java.util.Random;

import java.util.Vector;

import de.hdm.kontaktsystem.server.ContactSystemAdministrationImpl;
import de.hdm.kontaktsystem.server.db.BusinessObjectMapper;
import de.hdm.kontaktsystem.server.db.ContactListMapper;
import de.hdm.kontaktsystem.server.db.ContactMapper;
import de.hdm.kontaktsystem.server.db.ParticipationMapper;
import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class DBTest {
	
	private final static ContactSystemAdministrationImpl csa = new ContactSystemAdministrationImpl();
	
	// Gültige IDs zu Testen
	private final static double vUID = 666;
	private final static int vCID = 22;
	private final static int vCLID = 21;
	private final static int vPID = 5; // = Sternzeichen
	private final static int vPVID = 61; // = Janina
	
	
	public static void main(String[] args){
		csa.init();
		System.out.println("Suche nach Taxi: " + csa.searchContacts("Taxi"));
		System.out.println("User: " + csa.getUserByID(170));
		System.out.println("Alle KontaktListe: " + csa.getAllContactLists());
		
		csa.getUserBygMail("user@gmail.com");
		
		csa.getAllUsers();
		
		csa.getAllContactsFromUser(); 
		
		csa.getAllContacts(); 
		
		//csa.getContactByPropertyValue(PropertyValue pv);
		
		csa.getContactsFromUser();
		
		csa.getContactsByStatus(true);
		
		csa.getContactById(106);
		
		csa.getAllContactListsFromUser();
		
		csa.getAllContactLists();
		
		csa.getContactListById(58);
		
		csa.getContactListByName("Olis Liste");
		
		//csa.getNameOfContact(Contact c);

		//csa.addContactToList(Contact c, ContactList cl);


		/**
		 * Create
		 *
		//Zu klären: was wird jeweils bei create übergeben?
		createUser(User u, Contact c);
		
		createContact(Contact c);
		
		createContactList(ContactList cl);
		
		createPropertyValue(PropertyValue pv);
		
		//Rückgabe von Participation Objekt anhand GUI Eingabe?
		createParticipation(Participation part);

		/**
		 * Update
		 *
		//Zu klären: was wird jeweils als Parameter übergeben?
		csa.editUser(User u);
		
		csa.editContact(Contact c);
		
		csa.editContactList(ContactList cl);
		
		csa.editPropertyValue(PropertyValue pv);
		
		
		/**
		 * Delete
		 *
		deleteUser(User u);
		
		deleteContact(Contact c);
		
		deleteContactList(ContactList cl);
		
		deletePropertyValue(PropertyValue pv);
		
		deleteParticipation(Participation p);
		
		removeContactFromList(Contact contact, ContactList contactList);
		
		getAllParticipationsByOwner(User u);

		
		getAllParticipationsByParticipant(User participant);
		*/

		
	}
}
