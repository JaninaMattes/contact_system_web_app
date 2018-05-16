package test;

import java.sql.Connection;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import de.hdm.kontaktsystem.server.db.BusinessObjectMapper;
import de.hdm.kontaktsystem.server.db.ContactMapper;
import de.hdm.kontaktsystem.server.db.DBConnection;
import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class DBTest {
	
	
	public static void main(String args[]){
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
		PropertyValue c = PropertyValueMapper.propertyValueMapper().findByKey(10);
		c.setShared_status(true);
		BusinessObjectMapper.businessObjectMapper().update(c);
		
	}
}
