package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

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
	 * Diese Methode gibt das einzige Objekt dieser Klasse zurück.
	 * @return Instanz des PropertyMapper 
	 */			

  public static UserMapper userMapper() {
    if (userMapper == null) {
      userMapper = new UserMapper();
    }

    return userMapper;
  }

	
	
	/**
	 * Returns all <code>User</code> objects from the database table 
	 * @return Vector<User>
	 */
	public Vector <User> getAllUsers(){
		
		Connection con = DBConnection.connection();
		try{
			// Create Vector to save all Users from the Database
			Vector<User> userList = new Vector<User>();
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM User");
			while(rs.next()){
				User u = new User();
				u.setGoogleID(rs.getInt("ID"));
				u.setGMail(rs.getString("g_mail"));
				// u.setContact(ContactMapper.contactMapper().getContactByID(rs.getInt("contactID")));
				userList.add(u);
			}
			return userList;
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Return a <code>User</code> object with the param id when it exist
	 * @param id
	 * @return User
	 */
	public User getUserById(int id){
		
		User u = new User();
		Connection con = DBConnection.connection();
		
		try{
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM User WHERE id = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				u.setGoogleID(rs.getInt("ID"));
				u.setGMail(rs.getString("g_token"));
				// u.setContact(ContactMapper.contactMapper().getContactByID(rs.getInt("contactID")));
				
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return u;
	}
	
	/**
	 * Select the User which is connect to the Google Account or generate a new <code>User</code> when the email is unknown
	 * This method is used for Login
	 * @param email
	 * @return User that is linked to the Google Account
	 */
	public User getUserByEmail(String email){
		
		Connection con = DBConnection.connection();
		try{
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM User WHERE g_mail = ?");
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				User u = new User();
				u.setGoogleID(rs.getInt("ID"));
				u.setGMail(rs.getString("g_mail"));
				// u.setContact(ContactMapper.contactMapper().getContactByID(rs.getInt("contactID")));
				return u;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Delete all <code>User</code> objects from the database table 
	 */
	public void deleteAllUsers(){
		
		Connection con = DBConnection.connection();
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM User");
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Delete the <code>User</code> object with the param id
	 * @param id
	 */
	public void deleteUserById(int id){
		Connection con = DBConnection.connection();
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM User Where user_ID = "+ id);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Update the User account
	 * @param user
	 */
	public void updateUser(User user){
		/*
		 * There is nothing to update at this time, because the Google-Data (Email, ID) don't change.
		 * Possible usage: Update Profile Image, use Nickname, give the User possibility to customise GUI (Color, ...)
		 */
		
	}
	
	/**
	 * Generate new row in the UserTable with the data from the <code>User</code> object
	 * @param user
	 */
	public void insertUser(User user){
		Connection con = DBConnection.connection();
		try{
			PreparedStatement stmt = con.prepareStatement("INSERT INTO User (ID, g_mail) VALUES (?, ?)");
			stmt.setInt(1, user.getGoogleID());
			stmt.setString(2, user.getGMail());
			stmt.execute();
			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Generate a new UserTable in the Database
	 */
	public void initUserTable(){
		Connection con = DBConnection.connection();
		Statement stmt;
		try {
			stmt = con.createStatement();
			// Create new User table
			stmt.executeUpdate("CREATE TABLE User (ID INT(10) NOT NULL, g_mail VARCHAR(255) NOT NULL, PRIMARY KEY(ID));");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Delete the hole UserTable
	 */
	public void deleteUserTable(){
		Connection con = DBConnection.connection();
		Statement stmt;
		
		/*
		 * Delete the BusinessObject table before the User table because the UserID is used as ForeignKey
		 */
		//BusinessObjectMapper.businessObjectMapper().deleteBusinessObjectTable();
		
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("DROP TABLE User");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	
}
