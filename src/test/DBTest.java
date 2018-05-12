package test;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import de.hdm.kontaktsystem.server.db.BusinessObjectMapper;
import de.hdm.kontaktsystem.server.db.DBConnection;
import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.User;

public class DBTest {
	
	
	public static void main(String args[]){
		
		Connection con = DBConnection.connection();
		try {
			ResultSet rs = con.createStatement().executeQuery("Select * from User");
			while(rs.next()){
				System.out.println("Name: "+rs.getInt("ID")+" E-Mail: "+rs.getString("g_mail"));
			}
			con.createStatement().executeUpdate("Update BusinessObject Set user_ID = 0 Where bo_ID = 2");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Test insert method from BusinessObjectMapper 
		
		//BusinessObjectMapper.businessObjectMapper().insert(new Property());
		
		// Generate test User with random ID
		User u = new User();
		Random rng = new Random();
		u.setGMail("mail@gmail.com");
		u.setGoogleID(rng.nextInt(1000)+1);
		//UserMapper.userMapper().insertUser(u);
		
		
		PropertyMapper.propertyMapper().getAllPropertiesByUser(u.getGoogleID());
	}
}
