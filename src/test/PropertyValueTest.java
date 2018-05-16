package test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.hdm.kontaktsystem.server.db.DBConnection;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class PropertyValueTest {

	public static void main(String args[]) {
			
		Contact c = new Contact();
		PropertyValueMapper.propertyValueMapper().deleteBy(c);
				
		
		
		 
		/*
		System.out.println("Eigenschaft Straße: " + 
				PropertyValueMapper.propertyValueMapper().findByKey(10)
				);
		*/
		
		/*
				
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

				*/

				
	}
			
		
}

	

	


