package test;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.hdm.kontaktsystem.server.db.DBConnection;

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
		
		
	}
}
