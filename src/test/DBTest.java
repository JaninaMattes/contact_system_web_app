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
		/*
		Connection con = DBConnection.connection();
		
		try {
			ResultSet rs1 = con.createStatement().executeQuery(
					"Select * from User"
					);

			ResultSet rs2 = con.createStatement().executeQuery(
					"Select * from User"
					);
			while(rs2.next()){
				System.out.println("Name: "+rs2.getInt("ID")+" E-Mail: "+rs2.getString("g_token"));
			}
			con.createStatement().executeUpdate(
					"Update BusinessObject Set user_ID = 0 Where bo_ID = 2"
					);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		// Test insert method from BusinessObjectMapper 
		
		//BusinessObjectMapper.businessObjectMapper().insert(new Property());
		
		// Generate test User with random ID
		
		/*
		User u = new User();
		Random rng = new Random();
		u.setGMail("mail@gmail.com");
		u.setGoogleID(rng.nextInt(1000)+1);
		
		*/
		//UserMapper.userMapper().insertUser(u);
		ContactList cl = new ContactList();
		cl.setName("Meine Liste");
		cl.setOwner(uMapper.getUserById(615));
		
		//clMapper.insertContactList(cl);
		
		System.out.println(clMapper.findAllContactLists());
		
		
		Vector<ContactList> cll = new Vector<ContactList>();
		
		cll = clMapper.findContactListByName("Meine Liste");
		System.out.println(cll);
		
		cll = clMapper.findContactListByUser(uMapper.getUserById(615));
		System.out.println(cll);
		

		cl = clMapper.findContactListById(cl.getBo_Id());
		
		cl.setName("Marcos Liste");
		
		clMapper.updateContactList(cl);
		
		clMapper.deleteContactListById(241);
		
		Contact c = new Contact();
		System.out.println(c);
		c.setOwner(uMapper.getUserById(615));
		c.setBo_Id(31);
		clMapper.addContactToContactlist(cl, c);
		c.setBo_Id(32);
		clMapper.removeContactFromContactList(cl, c);
		
	}
}
