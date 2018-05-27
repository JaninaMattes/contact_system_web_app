package test;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Vector;

import de.hdm.kontaktsystem.server.db.BusinessObjectMapper;
import de.hdm.kontaktsystem.server.db.ContactListMapper;
import de.hdm.kontaktsystem.server.db.ContactMapper;
import de.hdm.kontaktsystem.server.db.DBConnection;
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


public class PropertyValueTest {
	
	private final static BusinessObjectMapper boMapper = BusinessObjectMapper.businessObjectMapper();	
	private final static ContactListMapper clMapper = ContactListMapper.contactListMapper();
	private final static ContactMapper cMapper = ContactMapper.contactMapper();
	private final static ParticipationMapper partMapper = ParticipationMapper.participationMapper();
	private final static PropertyMapper pMapper = PropertyMapper.propertyMapper(); 
	private final static PropertyValueMapper pvMapper = PropertyValueMapper.propertyValueMapper();
	private final static UserMapper uMapper = UserMapper.userMapper();
	
	// Gültige IDs zu Testen
	private final static double vUID = 666;
	private final static int vCID = 19;
	private final static int vCLID = 3;
	private final static int vPID = 1; // = Sternzeichen
	private final static int vPVID = 29; // = Janina
	
	static double uID;
	

	
	public static void main(String args[]) {
		
		System.out.println("\n ############ Test PropertyValue insert ################ \n");
		
		Property prop = new Property();
		Contact contact = new Contact();
		User u = new User();
		u.setGoogleID(666);
		contact.setBo_Id(28);
		prop.setId(2);
		PropertyValue pvInsert = new PropertyValue("Bussenstraße 23");
		pvInsert.setProperty(prop);
		pvInsert.setContact(contact);
		pvInsert.setOwner(u);
		pvMapper.insert(pvInsert);
		
	
//		System.out.println("\n ############ Test PopertyValue Update ################ \n");
//		
//		PropertyValue pvUpdate = pvMapper.findByKey(vPVID);
//		pvUpdate.setShared_status(true);
//		pvUpdate.setValue("KimlysTest");
//		pvMapper.update(pvUpdate);
		
		
//		System.out.println("\n ############ Test PropertyValue FindByName ################ \n");
//		
//		
//		Contact contact = new Contact();
//		contact.setBo_Id(28);
//		PropertyValue pvfindByName = pvMapper.findByKey(vPVID);
//		pvMapper.findName(contact);
		
	
		// Insert Methode zum Befüllen der DB mit Testwerten
		

			
			// Test insert method from BusinessObjectMapper 
			System.out.println("\n ############ Test BO ################ \n");
			System.out.println(BusinessObjectMapper.businessObjectMapper().findAllBusinessObjectIDs());
			System.out.println(BusinessObjectMapper.businessObjectMapper().findBusinessObjectIDsByUserID(vUID));
			

			 //Test insert method from UserMapper 
			System.out.println("\n ############ Test User ################ \n");
			
			
			//System.out.println("Find All: " +uMapper.findAll());
			//System.out.println("Find by EMail: " +uMapper.findByEmail("Oli@example.com"));
			PropertyValue pv = pvMapper.findByKey(vPVID);			
			System.out.println(pv);
			Contact contact1 = cMapper.findContactById(vCID);
			contact1.addPropertyValue(pv);
			User u1 = uMapper.findById(vUID);
			u1.setUserContact(cMapper.findContactById(22));
			
			uMapper.update(u1);
			System.out.println(u1 = uMapper.findById(u1.getGoogleID()));

			
			
			
			
			/**
			 * Test für den ContactList Mapper
			 */
			System.out.println("\n ############ Test ContactList ################ \n");
			
			
			
			System.out.println("Find All: "+clMapper.findAllContactLists());
			
			
			Vector<ContactList> cll = new Vector<ContactList>();
			
			cll = clMapper.findContactListByName("Meine Liste");
			System.out.println("Find By Name: " +cll);
			
			cll = clMapper.findContactListByUser(u);
			System.out.println("Find By User: " +cll);
			

			ContactList cl = clMapper.findContactListById(vCLID);
			
			cl.setName("Die Liste");
			cl.setOwner(u);
			System.out.println("Update ContactList: "+cl);
			clMapper.updateContactList(cl);
			
			// Testet Contact - ContactList BezTabelle
			System.out.println("Add Contact to Contact List");
			clMapper.addContactToContactlist(cl, cMapper.findContactById(vCID));
			
			System.out.println("Find CL by ID: " + clMapper.findContactListById(vCLID));
			
			System.out.println("Remove Contact from Contact List");
			clMapper.removeContactFromContactList(cl, cMapper.findContactById(vCID));
			
			
			
			
			/**
			 * Test Contact Mapper
			 */
			System.out.println("\n ############ Test Contact ################ \n");
			
			Contact c;
			
			//System.out.println(cMapper.findBy(pvMapper.findByKey(vPVID)));
			System.out.println("Find by User: " +cMapper.findAllContactsByUser(vUID));
			System.out.println("Find by Status False: " +cMapper.findContactByStatus(vUID, false));

			System.out.println("Find All: " +cMapper.findAllContacts());
			
			System.out.println(c = cMapper.findContactById(vCID));
			
			
			
			
			
			System.out.println("\n ############ Test Property ################ \n");
			
			System.out.println("Find All: " +pMapper.findAll());
			System.out.println("Find by ID: " +pMapper.findBy(vPVID));
			System.out.println("Find by Desc: " +pMapper.findBy("Name"));

			//System.out.println("Find PV: " +pMapper.findBy(pvMapper.findByKey(vPVID)));

//		int propName = 1;
//		double partUser = 777;
//		double sharingUser = 666;
////		
////		System.out.println("\n ############ Test Poperty ################ \n");
////		
////		Property p = new Property();
////		p.setId(propName);
////		p.setDescription("Test"+ propName);
////		//pMapper.insert(p);
////		
////		
////		System.out.println("\n ############ Test User ################ \n");
////		
//		User u = new User();
//		u.setGMail("mail@gmail.com");
////		// Generate test User with random ID
//		u.setGoogleID(sharingUser);//rng.nextDouble()*100000000000000000d);
////		uID = u.getGoogleID();
//		Contact c = new Contact();
//		c.setOwner(u);
//		c.setBo_Id(28);
////		uMapper.insert(u, c);
////		u = uMapper.findById(sharingUser);
////		
////		
//		System.out.println("\n ############ Test Contact ################ \n");
//		
//		
//		//cMapper.insertContact(c);
//		
//		
//		System.out.println("\n ############ Test PopertyValue ################ \n");
//		
//		PropertyValue pv = new PropertyValue();
//		pv.setContact(c);
//		pv.setOwner(c.getOwner());
//		pv.setProp(pMapper.findBy(propName));
//		pv.setValue("TestUser");
//		pv.setBo_Id(32);
//		//pvMapper.insert(pv);
//		
//		System.out.println("Add Contact to User");
//		u.setUserContact(c);
//		//uMapper.update(u);
//		
//		
//		System.out.println("\n ############ Test ContactList ################ \n");
//	
//		ContactList cl = new ContactList();
//		cl.setName("Meine Liste");
//		cl.setOwner(u);
//		cl.setBo_Id(33);
//		//clMapper.insertContactList(cl);
//		
//		System.out.println("Add Contact to ContactList");
//		//clMapper.addContactToContactlist(cl, c);
//		
//		
//		System.out.println("\n ############ Test Participation ################ \n");
//		
//		Participation p1 = new Participation();
//		Participation p2 = new Participation();
//		Participation p3 = new Participation();
//		
//		p1.setParticipant(uMapper.findById(partUser));
//		p2.setParticipant(uMapper.findById(partUser));
//		p3.setParticipant(uMapper.findById(partUser));
//		
//		p1.setReference(c);
//		p2.setReference(cl);
//		p3.setReference(pv);
//		
//		partMapper.insertParticipation(p1);
//		partMapper.insertParticipation(p2);
//		partMapper.insertParticipation(p3);
	

		// Delete Methode zum Leeren der Datenbank
	
	
//	public static void deleteDB() {
		
//		Connection con = DBConnection.connection();
//		  
//		  try {
//		      PreparedStatement stmt1 = con.prepareStatement
//		      ("DELETE FROM PropertyValue ");
//		      stmt1.execute();
//		      
//		      PreparedStatement stmt2 = con.prepareStatement
//		      ("DELETE FROM Contact ");
//		      stmt2.execute();
//		      
// 
//		      PreparedStatement stmt3 = con.prepareStatement
//		      ("DELETE FROM User_BusinessObject ");
//		      stmt3.execute();
//		      
//		  
//		      PreparedStatement stmt4 = con.prepareStatement
//		      ("DELETE FROM ContactList");
//		      stmt4.execute();
//
//		 
//		      PreparedStatement stmt5 = con.prepareStatement
//		      ("DELETE FROM BusinessObject ");
//		      stmt5.execute();
//		     
//		      PreparedStatement stmt6 = con.prepareStatement
//		      ("DELETE FROM User ");
//		      stmt6.execute();
//		      
//		    }
//		    catch (SQLException e) {
//		      e.printStackTrace();
//		    }	
		
		
	} 
	
}


	

	


