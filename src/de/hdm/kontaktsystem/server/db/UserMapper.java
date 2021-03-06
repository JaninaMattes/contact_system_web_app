package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.User;
/**
 * 
 * @author Oliver Gorges
 *
 */

public class UserMapper {
	
	private static UserMapper userMapper = null;
	
	
	private UserMapper(){
		
	}
	
	/**
	 * Hier findet die Anwendung des <code> Singleton Pattern </code> statt
	 * Diese Methode gibt das einzige Objekt dieser Klasse zurueck.
	 * @return Instanz des PropertyMapper 
	 */			

	public static UserMapper userMapper() {
	  if (userMapper == null) {
	    	userMapper = new UserMapper();
    	}

	   return userMapper;
	}
	
	 /**
	 * Legt einen neuen User in der Datenbank an. Analgo dazu wird
	 * auch der eigene Kontakt eines Users in der DB angelegt. 
	 * 
	 * @param User-Objekt
	 * @param Contact-Objekt (OwnContact)
	 * @return User-Objekt
	 */
	
	public User insert(User user){
		
		Connection con = DBConnection.connection();
		try{
			PreparedStatement stmt = con.prepareStatement("INSERT INTO User (ID, g_mail) VALUES (?, ?)");
			stmt.setDouble(1, user.getGoogleID());
			stmt.setString(2, user.getGMail());
			if(stmt.executeUpdate() > 0) {
				
				return user;
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gibt alle <code>User</code> Ojekte aus der Tabelle zurueck
	 * @return Vector<User>
	 */
	public Vector<User> findAll(){
		
		Connection con = DBConnection.connection();
		try{
			Vector<User> userList = new Vector<User>();			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM User");
			while(rs.next()){
				Contact c = new Contact();
				User u = new User();				
				u.setGoogleID(rs.getDouble("ID"));
				u.setGMail(rs.getString("g_mail"));
				c.setBo_Id(rs.getInt("own_Contact"));
				u.setUserContact(c);
				userList.add(u);
			}
			return userList;
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Gibt das <code>User</code> mit der gesuchten ID zurueck
	 * @param User ID
	 * @return User
	 */
	public User findById(double id){	
		//System.out.println("#User -findByID");
		Connection con = DBConnection.connection();
		
		try{
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM User WHERE id = ?");
			stmt.setDouble(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				Contact c = new Contact();
				User u = new User();
				c.setBo_Id(rs.getInt("own_Contact"));
				u.setGoogleID(rs.getDouble("ID"));
				u.setGMail(rs.getString("g_mail"));		
				u.setUserContact(c);
				return u;	
			}
		}catch(SQLException e){
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * Gibt das <code>User</code> mit der gesuchten Email-Adresse zurueck
	 * @param Email-String
	 * @return User-Objekt
	 */
	
	public User findByEmail(String email){
		
		Connection con = DBConnection.connection();
		try{
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM User WHERE g_mail = ?");
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){				
				Contact c = new Contact();
				User u = new User();
				u.setGoogleID(rs.getDouble("ID"));
				u.setGMail(rs.getString("g_mail"));
				c.setBo_Id(rs.getInt("own_Contact"));
				u.setUserContact(c);
				return u;	

			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Aktuallisiert die Daten eines Users in der UserTabelle
	 * @param User
	 * @return User
	 */
	public User update(User user){
		
		if(user.getUserContact() != null){
			Connection con = DBConnection.connection();
			try{
				PreparedStatement stmt = con.prepareStatement("UPDATE User SET own_Contact = ? WHERE ID = ?");
				stmt.setInt(1, user.getUserContact().getBoId());
				stmt.setDouble(2, user.getGoogleID());
				if(stmt.executeUpdate() > 0) return user;
				
				
			}catch(SQLException e){
				e.printStackTrace();
			}		
		}
		return null;
	}
	
	
	/**
	 * Loescht alle eintraege in der User Tabelle 
	 * 
	 * @note Nicht Verwendet
	 */
	public void deleteAll(){
		
		ContactMapper.contactMapper().deleteAllContacts();
		ContactListMapper.contactListMapper().deleteAllContactLists();
		
		Connection con = DBConnection.connection();
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM User");
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Loescht eine <code>User</code> Objekt aus der Datenbank
	 * @param user
	 * @return geloeschtes User-Objekt
	 */
	
	public User delete(User user){
		if(deleteByID(user.getGoogleID()) > 0) return user;
		else return null;
	}
	
	
	/**
	 * Loescht ein <code>User</code> mit der uebergeben ID aus der Datenbank
	 * @param id
	 * @return anzahl geloeschter User
	 */
	public int deleteByID(double id){
		

		int i = 0;
		
		Connection con = DBConnection.connection();
		try{
			PreparedStatement stmt = con.prepareStatement("DELETE FROM User WHERE ID = ?");
			stmt.setDouble(1, id);
			i = stmt.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return i;
	}
	
	
	
	
}
	
	

