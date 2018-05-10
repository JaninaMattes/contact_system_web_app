package test;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.hdm.kontaktsystem.server.db.BusinessObjectMapper;
import de.hdm.kontaktsystem.server.db.DBConnection;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Property;

public class DBTest {
	
	
	public static void main(String args[]){
		
		Connection con = DBConnection.connection();
		try {
			ResultSet rs = con.createStatement().executeQuery("Select * from Test");
			while(rs.next()){
				System.out.println("Name: "+rs.getString("Name")+" E-Mail: "+rs.getString("Email"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Test insert method from BusinessObjectMapper 
		
		BusinessObjectMapper.businessObjectMapper().insert(new Property());
		
		
	}
}
