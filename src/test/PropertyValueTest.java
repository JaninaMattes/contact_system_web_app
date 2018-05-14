package test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.hdm.kontaktsystem.server.db.DBConnection;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class PropertyValueTest {

	public static void main(String args[]) {
			
		/*
		User kimly = new User();
		kimly.setGoogleID(1);
		kimly.setGMail("kl055@hdm-stuttgart.de");
		UserMapper.userMapper().insertUser(kimly);
		
		Contact ingo = new Contact();
		PropertyValue pv = new PropertyValue("Bussenstrasse");
		pv.setValue("Bussenstrasse");
		pv.setContact(ingo);
		PropertyValueMapper.propertyValueMapper().insert(pv);
		
		System.out.println("Eigenschaft Straße: " +
				PropertyValueMapper.propertyValueMapper().findByValue("Bussenstrasse") +
				"User :" + UserMapper.userMapper().getUserById(1)
				);
				*/
		
	
				
				Connection con = DBConnection.connection();
				Statement stmt = null;
				
				try {
					stmt = con.createStatement();
					stmt.executeUpdate(
						"INSERT INTO User (id, g_token)"
							  + " VALUES(1, 'kl055@hdm-stuttgart.de')"
						);

					ResultSet rs2 = con.createStatement().executeQuery(
							"Select * from User"
							);
					while(rs2.next()){
						System.out.println("ID: "+rs2.getInt("ID")+" E-Mail: "+rs2.getString("g_token"));
					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
				
			}
		
		
	}
	


