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
		System.out.println("My Contacts: " + csa.getAllContactsFromUser());
		
		/**
		csa.init();
		csa.searchContacts("Test");
		csa.getUserByID(170);
		csa.getAllContactLists();
		
		csa.getUserBygMail("user@gmail.com");
		
		csa.getAllUsers();
		
		csa.getAllContactsFromUser(); 
		
		csa.getAllContacts(); 
		
		csa.getContactByPropertyValue(csa.getPropertyValueById(90));
		
		csa.getContactsFromUser();
		
		csa.getContactsByStatus(true);
		
		csa.getContactById(69);
		
		csa.getAllContactListsFromUser();
		
		csa.getAllContactLists();
		
		csa.getContactListById(58);
		
		csa.getContactListByName("Olis Liste");
		
		csa.getNameOfContact(csa.getContactById(69));

		//csa.addContactToList(csa.getContactById(10), csa.getContactListById(105));


		/**
		 * Create

		//Zu klären: was wird jeweils bei create übergeben?
		User u = new User();
		u.setGoogleID(170);
		u.setGMail("oli@test.de");
		
		Contact c = new Contact();
		
		//System.out.println(csa.createUser(u, c));
		
		//csa.createContact(c);
		
		ContactList cl = new ContactList();
		cl.setName("DB Test");
		cl.setOwner(csa.getUserByID(170));
		
		//csa.createContactList(cl);
		PropertyValue pv = new PropertyValue();
		pv.setValue("DBTest");
		//pv.setContact(csa.getContactById(107));
		pv.setProperty(PropertyMapper.propertyMapper().findBy(1));
		//csa.createPropertyValue(pv);
		
		Participation part = new Participation();
		part.setParticipant(csa.getUserByID(170));
		//part.setReference(csa.findBusinessObjectByID(95));
		
		// csa.createParticipation(part);

		/**
		 * Update
		 */
		//Zu klären: was wird jeweils als Parameter übergeben?
		//csa.editUser(User u);
		
		//csa.editContact(Contact c);
		
		//ContactList cl2 = csa.getContactListById(108);
		//cl2.setName("DBTest 2");
		//csa.editContactList(cl2);
		
		//csa.editPropertyValue(PropertyValue pv);
		
		
		/**
		 * Delete
		 */
		
		//csa.deleteUser(csa.getUserByID(521));
		/*
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
