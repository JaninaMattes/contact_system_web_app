package test;

import java.util.Random;
import java.util.Vector;

import de.hdm.kontaktsystem.server.db.BusinessObjectMapper;
import de.hdm.kontaktsystem.server.db.ContactListMapper;
import de.hdm.kontaktsystem.server.db.ContactMapper;
import de.hdm.kontaktsystem.server.db.ParticipationMapper;
import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class DBTest {
	
	
	
	public static void main(String args[]){
		
		BusinessObjectMapper boMapper = BusinessObjectMapper.businessObjectMapper();	
		ContactListMapper clMapper = ContactListMapper.contactListMapper();
		ContactMapper cMapper = ContactMapper.contactMapper();
		ParticipationMapper partMapper = ParticipationMapper.participationMapper();
		PropertyMapper pMapper = PropertyMapper.propertyMapper(); 
		PropertyValueMapper pvMapper = PropertyValueMapper.propertyValueMapper();
		UserMapper uMapper = UserMapper.userMapper();
		
		// Gültige IDs zu Testen
		double vUID = 1.1423815165163371e20;
		int vCID = 106;
		int vCLID = 101;
		int vPID = 5; // = Sternzeichen
		int vPVID = 111; // = Janina
		
		
		
		
		//System.out.println(propMapper.findByStatus(0, false));
		
		
		// Test insert method from BusinessObjectMapper 
		System.out.println("\n ############ Test BO ################ \n");
		System.out.println(BusinessObjectMapper.businessObjectMapper().findAllBusinessObjectIDs());
		System.out.println(BusinessObjectMapper.businessObjectMapper().findBusinessObjectIDsByUserID(vUID));
		

		System.out.println(BusinessObjectMapper.businessObjectMapper().findBusinessObjectByID(vCLID));

		
		
		// Test insert method from UserMapper 
		System.out.println("\n ############ Test User ################ \n");
		/*
		User u = new User();
		Random rng = new Random();
		u.setGMail("mail@gmail.com");
		// Generate test User with random ID
		u.setGoogleID(rng.nextDouble()*100000000000000000d);
		System.out.println("Create User "+u.getGoogleID());

		
		uMapper.insert(u);
		System.out.println(uMapper.findAll());
		System.out.println(uMapper.findByEmail("oli@gmail.de"));
		
		u.setContact(cMapper.findContactById(32));
		uMapper.update(u);
		System.out.println(u = uMapper.findById(u.getGoogleID()));

		//uMapper.insertUser(u);
		*/
		System.out.println(uMapper.findAll());
		System.out.println(uMapper.findByEmail("Oli@example.com"));
		User u = uMapper.findById(vUID);
		u.setContact(cMapper.findContactById(vCID));
		uMapper.update(u);
		System.out.println(u = uMapper.findById(u.getGoogleID()));

		
		//uMapper.deleteUserById(502);
		
		/**
		 * Test für den ContactList Mapper
		 */
		System.out.println("\n ############ Test ContactList ################ \n");
		
		/*
		 // Erzeugt eine neue Liste
		ContactList cl = new ContactList();
		cl.setName("Meine Liste");
		cl.setOwner(u);
		System.out.println(cl);
		clMapper.insertContactList(cl);
		*/
		
		System.out.println("Find All: "+clMapper.findAllContactLists());
		
		
		Vector<ContactList> cll = new Vector<ContactList>();
		
		cll = clMapper.findContactListByName("Meine Liste");
		System.out.println("Find By Name: " +cll);
		
		cll = clMapper.findContactListByUser(u);
		System.out.println("Find By User: " +cll);
		

		ContactList cl = clMapper.findContactListById(vCLID);
		
		cl.setName("Die Liste");
		System.out.println("Update ContactList: "+cl);
		clMapper.updateContactList(cl);
		
		// Testet Contact - ContactList BezTabelle
		System.out.println("Add Contact to Contact List");
		//clMapper.addContactToContactlist(cl, cMapper.findContactById(vCID));
		
		System.out.println(clMapper.findContactListById(vCLID));
		
		System.out.println("Remove Contact from Contact List");
		//clMapper.removeContactFromContactList(cl, cMapper.findContactById(vCID));
		
		
		/**
		 * Test Contact Mapper
		 */
		System.out.println("\n ############ Test Contact ################ \n");
		
		/*
		Contact c = new Contact();
		c.setOwner(u);
<<<<<<< HEAD
		c.setpropertyValue(propValMapper.findByKey(230));
		
		cMapper.insertContact(c);
		
		System.out.println(cMapper.findBy(propValMapper.findByKey(230)));
		System.out.println(cMapper.findAllContactsByUser(615));
		System.out.println(cMapper.findContactByStatus(615, false));
=======
		c.setName(propValMapper.findByKey(111));
		//cMapper.insertContact(c);
		*/
		Contact c;
		/*
		System.out.println(cMapper.findByName(pvMapper.findByKey(vPVID)));
		System.out.println(cMapper.findAllContactsByUser(vUID));
		System.out.println(cMapper.findContactByStatus(vUID, false));
>>>>>>> refs/heads/OliverGorges
		System.out.println(cMapper.findAllContacts());
		*/
		System.out.println(c = cMapper.findContactById(vCID));
		
		//cMapper.updateContact(c);
		
		System.out.println("\n ############ Test Property ################ \n");
		
		//System.out.println(pMapper.findAll());
		System.out.println(pMapper.findBy(vPVID));
		//System.out.println(pMapper.findBy("Name"));
		System.out.println(pMapper.findBy(pvMapper.findByKey(vPVID)));
		
		System.out.println("\n ############ Test PropertyValue ################ \n");
		
		PropertyValue pv;
		System.out.println(pv = pvMapper.findByKey(vPVID));
		System.out.println(pvMapper.findAll(pv, u));
		System.out.println(pvMapper.findBy(c));
		System.out.println(pvMapper.findBy(u));
		System.out.println(pvMapper.findBy(pMapper.findBy(vPID)));
		System.out.println(pvMapper.findAllShared(u, pv));
	}
}
