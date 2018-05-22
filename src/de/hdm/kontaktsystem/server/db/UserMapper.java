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
	public Vector <User> findAll(){
		
		Connection con = DBConnection.connection();
		try{
			// Create Vector to save all Users from the Database
			Vector<User> userList = new Vector<User>();
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM User");
			while(rs.next()){
				User u = new User();
				u.setGoogleID(rs.getDouble("ID"));
				u.setGMail(rs.getString("g_mail"));
				u.setContact(ContactMapper.contactMapper().findContactById(rs.getInt("own_Contact")));
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
	public User findById(double id){
		
		
		Connection con = DBConnection.connection();
		
		try{
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM User WHERE id = ?");
			stmt.setDouble(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				User u = new User();
				Contact c = new Contact();
				c = ContactMapper.contactMapper().findContactById(rs.getInt("own_Contact"));
				
				u.setGoogleID(rs.getDouble("ID"));
				u.setGMail(rs.getString("g_mail"));
				u.setContact(c);
				return u;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Select the User which is connect to the Google Account or generate a new <code>User</code> when the email is unknown
	 * This method is used for Login
	 * @param email
	 * @return User that is linked to the Google Account
	 */
	public User findByEmail(String email){
		
		Connection con = DBConnection.connection();
		try{
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM User WHERE g_mail = ?");
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){				
				
				User u = new User();
				u.setGoogleID(rs.getDouble("ID"));
				u.setGMail(rs.getString("g_mail"));

				u.setContact(ContactMapper.contactMapper().findContactById(rs.getInt("own_Contact")));

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
	public void deleteAll(){
		

		ParticipationMapper.participationMapper().deleteAllParticipations();
		
		
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
	public void delete(User user){
		
		ParticipationMapper.participationMapper().deleteParticipationForOwner(user);
		BusinessObjectMapper.businessObjectMapper().deleteBusinessObjectByUserId(user);
		
		Connection con = DBConnection.connection();
		try{
			PreparedStatement stmt = con.prepareStatement("DELETE FROM User WHERE ID = ?");
			stmt.setDouble(1, user.getGoogleID());
			stmt.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Update the User account
	 * @param user
	 */
	public void update(User user){
		/*
		 * There is nothing to update at this time, because the Google-Data (Email, ID) don't change.
		 * Possible usage: Update Profile Image, use Nickname, give the User possibility to customise GUI (Color, ...)
		 */
		System.out.println(user);
		if(user.getContact() != null){
			Connection con = DBConnection.connection();
			try{
				PreparedStatement stmt = con.prepareStatement("UPDATE User SET own_Contact = ? WHERE ID = ?");
				stmt.setInt(1, user.getContact().getBo_Id());
				stmt.setDouble(2, user.getGoogleID());
				stmt.execute();
				
				
			}catch(SQLException e){
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * Generate new row in the UserTable with the data from the <code>User</code> object
	 * @param user
	 */
	
	public void insert(User user){
		Connection con = DBConnection.connection();
		try{
			PreparedStatement stmt = con.prepareStatement("INSERT INTO User (ID, g_mail) VALUES (?, ?)");
			stmt.setDouble(1, user.getGoogleID());
			stmt.setString(2, user.getGMail());
			stmt.execute();
			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
