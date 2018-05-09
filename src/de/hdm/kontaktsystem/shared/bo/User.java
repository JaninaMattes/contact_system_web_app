package de.hdm.kontaktsystem.shared.bo;

/**<p>
 * An User is a special <code>Contact</code> an inherits all Attributes from <code>Contact</code>. 
 * </p><p>
 * It extends the <code>Contact</code> class with an googleToken, to link the Userprofile with the Google Account.
 * This class is used to login the user and can be set as owner or praticipant for Contact oder ContactList.
 * </p>
 * @author Oliver
 *
 */

// TODO: Mit Datenbank Modell abgleichen!

public class User {
	
	private static final long serialVersionUID = 1L;
	
	private String g_Mail = null;
	private Contact user_Contact = null;
	
	public User() {
		user_Contact = new Contact();
		// TODO Create an Contructor in Contact withput an User 
	}
	
	/**
	 * Return the Google Account Token
	 * @return googleToken
	 */
	public String getGoogleToken(){
		return googleToken;
	}
	
	/**
	 * Set the Token to link the Google Account to an User
	 */
	public void setGoogleToken(String token){
		this.googleToken = token;
	}

}
