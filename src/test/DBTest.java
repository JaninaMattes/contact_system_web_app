package test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.hdm.kontaktsystem.server.db.DBConnection;

public class DBTest {
	
	
	public static void main(String args[]){
		
		Connection con = DBConnection.connection();
		try {
			ResultSet rs = con.createStatement().executeQuery("Select * from User1");
			while(rs.next()){
				System.out.println("Name: "+rs.getString("name")+" E-Mail: "+rs.getString("email"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
