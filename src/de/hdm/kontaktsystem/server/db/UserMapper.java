package de.hdm.kontaktsystem.server.db;

import java.util.Vector;

import de.hdm.kontaktsystem.shared.bo.User;
/**
 * 
 * @author Oliver Gorges
 *
 */

public class UserMapper {
	
	/**
	 * Create an UserMapper object 
	 */
	private static final UserMapper um = new UserMapper();
	
	
	private UserMapper(){
		
	}
	
	/**
	 * Returns an Instance of the UserMapper 
	 * @return Instance of the UserMapper
	 */
	public static UserMapper getInstance(){
		return um;
	}
	
	/**
	 * Returns all <code>User</code> objects from the database table 
	 * @return Vector<User>
	 */
	public Vector<User> getAllUsers(){
		// TODO SQL Statement
		return null;
	}
	
	/**
	 * Return a <code>User</code> object with the param id when it exist
	 * @param id
	 * @return User
	 */
	public User getUserById(int id){
		// TODO SQL Statement
		return null;
	}
	
	/**
	 * Select the User which is connect to the Google Account or generate a new <code>User</code> when the token is unknown
	 * This method is used for Login
	 * @param token
	 * @return User that is linked to the Google Account
	 */
	public User getUserByToken(String token){
		// TODO SQL Statement
		return null;
	}
	
	/**
	 * Delete all <code>User</code> objects from the database table 
	 */
	public void deleteAllUsers(){
		// TODO SQL Statement
	}
	
	/**
	 * Delete the <code>User</code> object with the param id
	 * @param id
	 */
	public void deleteUserById(int id){
		// TODO SQL Statement
	}
	
	/**
	 * 
	 * @param user
	 */
	public void updateUser(User user){
		// TODO SQL Statement
	}
	
	/**
	 * Generate new row in the UserTable with the data from the <code>User</code> object
	 * @param user
	 */
	public void insertUser(User user){
		// TODO SQL Statement
	}
	
	/**
	 * Generate a new UserTabel in the Database
	 */
	public void initUserTabel(){
		// TODO SQL Statement
	}
	
	/**
	 * Delete the hole UserTabel
	 */
	public void deleteUserTabel(){
		// TODO SQL Statement
	}
	
	
}
