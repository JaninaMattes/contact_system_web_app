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
	
	private int googleID = 0;
	
	private String gMail = null;
	
	private boolean shared_status = false; 
	
	private Contact userContact = null;
	
	
	public User() {

		// user_Contact = new Contact();
		// TODO: Create a Contructor in Contact with an User 

	}
	
	/**
	 * Return the Google-Account ID
	 * @return googleID
	 */
	public int getGoogleID(){
		return googleID;
	}
	
	/**
	 * Set the unique UserID from the Google-Account
	 */
	public void setGoogleID(int id){
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
	
	/*
	 * Abfragen des Status
	 */
	
	public boolean isShared_status() {
		return shared_status;
	}

	/*
	 * SEtzen des Status
	 */
	
	public void setShared_status(boolean shared_status) {
		this.shared_status = shared_status;
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

}