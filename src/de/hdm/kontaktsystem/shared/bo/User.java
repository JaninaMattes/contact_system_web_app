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
	
	private boolean loggedIn = false;
	
	private String loginUrl;
	
	private String logoutUrl;
	
	private Double googleID; // -> Double
	
	private String gMail = null; 
	
	private Contact userContact = null;
	
	
	public User() {

		// user_Contact = new Contact();
		// TODO: Create a Contructor in Contact with an User 

	}
	
	/**
	 * Return the Google-Account ID
	 * @return googleID
	 */
	public double getGoogleID(){
		return googleID;
	}
	
	/**
	 * Set the unique UserID from the Google-Account
	 */
	public void setGoogleID(double id){
		this.googleID = id;
	}
	
	/**
	 * Return the Google-Mail address from the User
	 * @return gMail address
	 */
	public String getGMail(){
		return gMail;
	}
	
	/**
	 * Set the Google-Mail address to an User
	 */
	public void setGMail(String mail){
		this.gMail = mail;
	}
	
	/**
	 * Returns the <code>Contact</code> that is linked to the User
	 * @return contact
	 */
	public Contact getContact(){
		return userContact;
	}
	
	/**
	 * Set the <code>Contact</code> that is linked to the User
	 */
	public void setContact(Contact contact){
		this.userContact = contact;
	}
	
	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		double dec = 1000000000000d;
		
		result = prime * result + ((gMail == null) ? 0 : gMail.hashCode());
		result = prime * result + (int) (googleID / dec);
		result = prime * result + ((userContact == null) ? 0 : userContact.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (gMail == null) {
			if (other.gMail != null)
				return false;
		} else if (!gMail.equals(other.gMail))
			return false;
		if (googleID != other.googleID)
			return false;
		if (userContact == null) {
			if (other.userContact != null)
				return false;
		} else if (!userContact.equals(other.userContact))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return "User " + googleID +": " + gMail +" -> "+userContact;
	}

	
	
}