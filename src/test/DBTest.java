package test;

import java.sql.Connection;


import java.sql.ResultSet;
import java.sql.SQLException;
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
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class DBTest {
	
	
 
	
	
		
	
	
	
	public static void main(String args[]){
		
		BusinessObjectMapper boMapper = BusinessObjectMapper.businessObjectMapper();	
		ContactListMapper clMapper = ContactListMapper.contactListMapper();
		ContactMapper cMapper = ContactMapper.contactMapper();
		ParticipationMapper partMapper = ParticipationMapper.participationMapper();
		PropertyMapper propMapper = PropertyMapper.propertyMapper(); 
		PropertyValueMapper propValMapper = PropertyValueMapper.propertyValueMapper();
		UserMapper uMapper = UserMapper.userMapper();
		
		
		// Test insert method from BusinessObjectMapper 
		System.out.println("############ Test BO ################");
		System.out.println(BusinessObjectMapper.businessObjectMapper().findAllBusinessObjectIDs());
		System.out.println(BusinessObjectMapper.businessObjectMapper().findBusinessObjectIDsByUserID(325));
		
		// BusinessObjectMapper.businessObjectMapper().insert(new Property());
		
		// Generate test User with random ID
		
		
		User u = new User();
		Random rng = new Random();
		u.setGMail("mail@gmail.com");
		u.setGoogleID(rng.nextInt(1000)+1);
		
		
		UserMapper.userMapper().insertUser(u);
		
		
		/**
		 * Test für den ContactList Mapper
		 */
		
		ContactList cl = new ContactList();
		cl.setName("Meine Liste");
		cl.setOwner(uMapper.findUserById(615));
		
		//clMapper.insertContactList(cl);
		
		System.out.println(clMapper.findAllContactLists());
		
		
		Vector<ContactList> cll = new Vector<ContactList>();
		
		cll = clMapper.findContactListByName("Meine Liste");
		System.out.println(cll);
		
		cll = clMapper.findContactListByUser(uMapper.findUserById(615));
		System.out.println(cll);
		

		cl = clMapper.findContactListById(cl.getBo_Id());
		
		cl.setName("Marcos Liste");
		
		clMapper.updateContactList(cl);
		
		clMapper.deleteContactListById(241);
		
		
		/**
		 * Test Contact Mapper
		 */
		
		Contact c = new Contact();
		c.setOwner(uMapper.findUserById(615));
		c.setName(propValMapper.findByKey(230));
		
		cMapper.insertContact(c);
		
		System.out.println(cMapper.findByName(propValMapper.findByKey(230)));
		System.out.println(cMapper.findAllContactsByUser(615));
		System.out.println(cMapper.findContactByStatus(615, false));
		System.out.println(cMapper.findAllContacts());
		System.out.println(cMapper.findContactById(32));
		
		//cMapper.updateContact(c);
		
		cl = clMapper.findContactListById(16);
		System.out.println(c);
		clMapper.addContactToContactlist(cl, c);
		c = cMapper.findContactById(32);
		clMapper.removeContactFromContactList(cl, c);
		
		clMapper.deleteContactListById(cl.getBo_Id());
		cMapper.deleteContactByID(c.getBo_Id());
		
	}
}
