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
		
//		public static void deletePvTable() {
			
			pvMapper.deleteAll();
			
		}
		
	
		
		
		public static void deleteAllSharedByOthersToMe() {
			
			User u = new User();
			u.setGoogleID(666);
			pvMapper.deleteAllSharedByOthersToMe(u);
			
		}
		
	
		
		public static void deleteAllSharedByMe() {
			
			User u = new User();
			u.setGoogleID(666);
			pvMapper.deleteAllSharedByMe(u);
			
		}
		
	
	
		public static void deleteByProp()  {
			
			Property prop = new Property();
			prop.setId(1);
			pvMapper.deleteBy(prop);
			
		}

		
		public static void deleteByContact() {
			
			Contact contact = new Contact();
			contact.setBo_Id(28);
			pvMapper.deleteBy(contact);
			
		}
		
		public static void deleteByPv() {
			
			PropertyValue pv = new PropertyValue();
			pv.setBo_Id(36);
			pvMapper.delete(pv);
			
		}
		
	
	
		public static void findAllSharedByOthersToMe() {
			
			
			User u = new User();
			Property prop = new Property();
			u.setGoogleID(777);
			System.out.println(pvMapper.findAllSharedByOthersToMe(u));
			
		}
		
	
	
		
		public static void findAllSharedByMepV()  {
			
		
			User u = new User();
			Property prop = new Property();
			u.setGoogleID(777);
			pvMapper.findAllSharedByMe(u);
			
		}
		
	
	
		public static void findAllpV() {
			System.out.println(pvMapper.findAll());
			
		}
		
	
	
		public static void findByProp() {
			
			
		Property prop = new Property();
		prop.setId(1);
		System.out.println(pvMapper.findBy(prop));
			
		}
		
	
	
		
		public static void findByContactpV() {
		
		Contact contact = new Contact();
		contact.setBo_Id(28);
		System.out.println(pvMapper.findBy(contact));
		
	}
		
		
		public static void findByKeypV() {
		
		System.out.println(pvMapper.findByKey(38));
		
	}
	
		public static void findByPropertyIDpV() {
			
			
			Property prop = new Property();
			prop.setId(1);
			
			Vector<PropertyValue> hilfsVector = new Vector<PropertyValue>();
			hilfsVector = pvMapper.findByPropertyID(prop.getId());
			//System.out.println(hilfsVector);
			
			for (PropertyValue pv : hilfsVector) {
				//System.out.println(pv);
			}
			
		}
	
	
		
	
		public static void findNamepV() {
			
			
			Contact contact = new Contact();
			contact.setBo_Id(28);
			PropertyValue pv = new PropertyValue();
			pv.setBo_Id(38);
			PropertyValue pvName = new PropertyValue();
			pvName.setBo_Id(29);
			Property prop = pMapper.findBy(1);
			User u = new User();
			u.setGoogleID(777);
			pv.setContact(contact);
			pv.setProp(prop);
			contact.setName(pvName);
			contact.setOwner(u);
			pvMapper.findName(contact);
			
		}
		
		
	
		
		public static void findByValuepV() {
		
		System.out.println("\n ############ Test PropertyValue findByValue ################ \n");
		
		Contact contact = cMapper.findContactById(28);
		PropertyValue pV = new PropertyValue();
		Vector<PropertyValue> pVVector = new Vector<PropertyValue>();
		pV.setContact(contact);
		pV.setName("KimlysTest");
		contact.setName(pV);
		pVVector.addElement(pV);
		contact.setPropertyValues(pVVector);
		contact.setPropertyValue(pV);
		Vector<PropertyValue> hilfsVector = new Vector<PropertyValue>();
		hilfsVector = pvMapper.findByValue("KimlysTest");
		
		for (PropertyValue pv : hilfsVector) {
				
		System.out.println(pv);
		
		}
		}
				
		
		public static void insertPv() {
		
		System.out.println("\n ############ Test PropertyValue insert ################ \n");
		
		Property prop = new Property();
		Contact contact = new Contact();
		User u = new User();
		u.setGoogleID(666);
		contact.setBo_Id(28);
		prop.setId(2);
		PropertyValue pvInsert = new PropertyValue("Bussenstraße 23", "TestUser");
		pvInsert.setProp(prop);
		pvInsert.setContact(contact);
		pvInsert.setOwner(u);
		pvMapper.insert(pvInsert);
		
	}
		
		public static void updatePv() {
		
	
		System.out.println("\n ############ Test PopertyValue Update ################ \n");
		
		PropertyValue pvUpdate = pvMapper.findByKey(vPVID);
		pvUpdate.setShared_status(true);
		pvUpdate.setValue("KimlysTest");
		pvMapper.update(pvUpdate);
		
		}
		
		public static void findByNamePv() {
		
		
		System.out.println("\n ############ Test PropertyValue FindByName ################ \n");
	
		Contact contact = new Contact();
		contact.setBo_Id(28);
		PropertyValue pvfindByName = pvMapper.findByKey(vPVID);
		pvMapper.findName(contact);
			
		}
		
		
		public static void createAllForTesting() {
	
		// Insert Methode zum Befüllen der DB mit Testwerten
		
		int propName = 1;
		double partUser = 777;
		double sharingUser = 666;
		
		System.out.println("\n ############ Test Poperty ################ \n");
		
		Property p = new Property();
		p.setId(propName);
		p.setDescription("Test"+ propName);
		//pMapper.insert(p);
		
		
		System.out.println("\n ############ Test User ################ \n");
		
		User u = new User();
		u.setGMail("mail@gmail.com");
		// Generate test User with random ID
		u.setGoogleID(sharingUser);//rng.nextDouble()*100000000000000000d);
		uID = u.getGoogleID();
		Contact c = new Contact();
		c.setOwner(u);
		c.setBo_Id(28);
		uMapper.insert(u, c);
		u = uMapper.findById(sharingUser);
		
		
		System.out.println("\n ############ Test Contact ################ \n");
		
		
		//cMapper.insertContact(c);
		
		
		System.out.println("\n ############ Test PopertyValue ################ \n");
		
		PropertyValue pv = new PropertyValue();
		pv.setContact(c);
		pv.setOwner(c.getOwner());
		pv.setProp(pMapper.findBy(propName));
		pv.setValue("TestUser");
		pv.setBo_Id(32);
		//pvMapper.insert(pv);
		
		System.out.println("Add Contact to User");
		u.setUserContact(c);
		//uMapper.update(u);
		
		
		System.out.println("\n ############ Test ContactList ################ \n");
	
		ContactList cl = new ContactList();
		cl.setName("Meine Liste");
		cl.setOwner(u);
		cl.setBo_Id(33);
		//clMapper.insertContactList(cl);
		
		System.out.println("Add Contact to ContactList");
		//clMapper.addContactToContactlist(cl, c);
		
		
		System.out.println("\n ############ Test Participation ################ \n");
		
		Participation p1 = new Participation();
		Participation p2 = new Participation();
		Participation p3 = new Participation();
		
		p1.setParticipant(uMapper.findById(partUser));
		p2.setParticipant(uMapper.findById(partUser));
		p3.setParticipant(uMapper.findById(partUser));
		
		p1.setReference(c);
		p2.setReference(cl);
		p3.setReference(pv);
		
		partMapper.insertParticipation(p1);
		partMapper.insertParticipation(p2);
		partMapper.insertParticipation(p3);
	
		}

		
	
	
	public static void deleteDB() {
		
		// Delete Methode zum Leeren der Datenbank
		
		Connection con = DBConnection.connection();
		  
		  try {
		      PreparedStatement stmt1 = con.prepareStatement
		      ("DELETE FROM PropertyValue ");
		      stmt1.execute();
		      
		      PreparedStatement stmt2 = con.prepareStatement
		      ("DELETE FROM Contact ");
		      stmt2.execute();
		      
 
		      PreparedStatement stmt3 = con.prepareStatement
		      ("DELETE FROM User_BusinessObject ");
		      stmt3.execute();
		      
		  
		      PreparedStatement stmt4 = con.prepareStatement
		      ("DELETE FROM ContactList");
		      stmt4.execute();

		 
		      PreparedStatement stmt5 = con.prepareStatement
		      ("DELETE FROM BusinessObject ");
		      stmt5.execute();
		     
		      PreparedStatement stmt6 = con.prepareStatement
		      ("DELETE FROM User ");
		      stmt6.execute();
		      
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }	
		
		
	} 
	
}


	

	


