package test;

import java.sql.Connection;
import java.sql.SQLException;

import de.hdm.kontaktsystem.server.db.DBConnection;

public class DBInitTableStatements {
		
		public void initDummyDataUser() {
			
			Connection con = DBConnection.connection();
			
			try {			
				con.createStatement().executeUpdate(
				"INSERT INTO User (ID, g_token) VALUES (1, 'maxmuster@gmail.com')\"); "
				+ "INSERT INTO User (ID, g_token) VALUES (1, 'maxmuster@gmail.com')\"); "
				+ "INSERT INTO User (ID, g_token) VALUES (1, 'maxmuster@gmail.com')"
				+ ");");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}
