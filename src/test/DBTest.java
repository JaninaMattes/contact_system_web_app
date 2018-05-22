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
		
		System.out.println("############ Test Property ################");
		
		//System.out.println(propMapper.findByStatus(0, false));
		
		
		// Test insert method from BusinessObjectMapper 
		System.out.println("############ Test BO ################");
		System.out.println(BusinessObjectMapper.businessObjectMapper().findAllBusinessObjectIDs());
		System.out.println(BusinessObjectMapper.businessObjectMapper().findBusinessObjectIDsByUserID(325));
		
		
		System.out.println(BusinessObjectMapper.businessObjectMapper().findBy(2));
		
		
		// Test insert method from UserMapper 
		System.out.println("############ Test User ################");
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
		
		//uMapper.deleteUserById(502);
		
		/**
		 * Test für den ContactList Mapper
		 */
		System.out.println("############ Test ContactList ################");
		System.out.println(u);
		ContactList cl = new ContactList();
		cl.setName("Meine Liste");
		cl.setOwner(u);
		System.out.println(cl);
		clMapper.insertContactList(cl);
		
		System.out.println("Find All: "+clMapper.findAllContactLists());
		
		
		Vector<ContactList> cll = new Vector<ContactList>();
		
		cll = clMapper.findContactListByName("Meine Liste");
		System.out.println("Find By Name: " +cll);
		
		cll = clMapper.findContactListByUser(u);
		System.out.println("Find By User: " +cll);
		

		cl = clMapper.findContactListById(cl.getBo_Id());
		
		cl.setName("Marcos Liste");
		System.out.println("Update Contact: "+cl);
		clMapper.updateContactList(cl);
		
		//clMapper.deleteContactListById();
		
		
		/**
		 * Test Contact Mapper
		 */
		System.out.println("############ Test Contact ################");
		Contact c = new Contact();
		c.setOwner(u);
		c.setpropertyValue(propValMapper.findByKey(230));
		
		cMapper.insertContact(c);
		
		System.out.println(cMapper.findBy(propValMapper.findByKey(230)));
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
