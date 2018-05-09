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
	private static UserMapper INSTANCE = new UserMapper();
	
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
	 * Returns an Instance of the UserMapper 
	 * @return Instance of the UserMapper
	 */
	public static UserMapper getInstance(){
		return INSTANCE;
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
=======
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
		   * Singleton Pattern
		   */
		
		private static UserMapper userMapper = null;
		
		
		private UserMapper(){
			
		}
		
		public static UserMapper userMapper() {
		    if (userMapper == null) {
		      userMapper = new UserMapper();
		    }

		    return userMapper;
		  }
		
		
		/**
		 * Returns an Instance of the UserMapper 
		 * @return Instance of the UserMapper
		 */
		public static UserMapper getInstance(){
			return userMapper;
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
