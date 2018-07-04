package de.hdm.kontaktsystem.shared.bo;

import java.io.Serializable;

/**<p>
 * Die Klasse <code>User</code> bestitzt folgende Elemente, User_ID, welche der Google ID entspricht, 
 * die dazugehoerige Google Adresse und einen <code>Contact</code> Objekt. Die Google-Mail Adresse dient dazu, um den 
 * <code>User</code> einzeln zu identifiezieren und von anderen <code>User</code> abzugrenzen, um die Teilhaber-Funktion zu nutzen. 
 * Das Kontaktobjekt beinhaltet den Namen des <code>User</code>, welcher im System angezeigt wird. 
 * </p><p>
 * Sie erweitert die <code>Contact</code> Klasse mit einem googleToken, um das Userprofile mit dem Google Account zu verbinden.
 * Diese Klasse dient als Login für den User und kann als Besitzer, oder Teilhaber eines Kontaktes oder Kontaktliste 
 * gesetzt werden.
 * </p>
 * @author Oliver
 *
 */

public class User implements Serializable{
	
	/**
	 * Die default SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Gibt an ob der Login erfolgreich war.
	 */
	private boolean loggedIn = false;
	
	/**
	 * Verweist auf die Login URL
	 */
	private String loginUrl;
	
	/**
	 * Verweist auf die logout URL
	 */
	private String logoutUrl;
	
	/**
	 * Verweist auf die GoogleID
	 */
	private Double googleID;
	
	/**
	 * Referenz auf die Gmail Adresse
	 */
	private String gMail = null; 
	
	/**
	 * Referenz auf ein Kontakt Objekt
	 */
	private Contact userContact = null;
	
	/**
	 * Leerer Konstruktor
	 */
	public User() {

	}
	
	/**
	 * Gibt die Google-Account ID zurueck
	 * @return googleID
	 */
	public double getGoogleID(){
		return googleID;
	}
	
	/**
	 * Setzt die eindeutige UserId vom Google Account
	 */
	public void setGoogleID(double id){
		this.googleID = id;
	}
	
	/**
	 * Gibt die Google Mail Adresse vom User zurueck
	 * @return gMail address
	 */
	public String getGMail(){
		return gMail;
	}
	
	/**
	 * Setzt die Google Mail Adresse eines User
	 */
	public void setGMail(String mail){
		this.gMail = mail;
	}
	
	/**
	 * Gibt den <code>Contact</code>, welcher mit dem User verknüpft ist, zurueck
	 * @return contact
	 */
	public Contact getUserContact(){
		return userContact;
	}
	
	/**
	 * Setzt den <code>Contact</code>, welcher mit dem User verknüpft ist
	 */
	public void setUserContact(Contact contact){
		this.userContact = contact;
	}
	
	/**
	 * Gibt die Login URL zurueck
	 * @return
	 */
	public String getLoginUrl() {
		return loginUrl;
	}
	
	/**
	 * Setzt die Login URL
	 * @param loginUrl
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	/**
	 * Gibt die Login URL zurueck
	 * @return
	 */
	public String getLogoutUrl() {
		return logoutUrl;
	}

	/**
	 * Setzt die Logout URL
	 * @param logoutUrl
	 */
	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}
	
	/**
	 * Setzt den LoggedIn
	 * @param loggedIn
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	/**
	 * Gibt den isLoggedIn zurueck
	 * @return
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * Der Hash-Code liefert zu jedem Objekt eine eindeutige Integerzahl, mit der das 
	 * Objekt identifiziert werden kann. Der Hash-Wert entspricht hier einfachheitshalber 
	 * der ID des Objekts. Dies ueberschreibt die Methode hashCode() der Klasse Object.
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		double dec = 1000000000000d;
		
		result = prime * result + ((gMail == null) ? 0 : gMail.hashCode());
		result = prime * result + (int) (googleID / dec);
		result = prime * result + ((userContact == null) ? 0 : userContact.hashCode());
		return result;
	}

	/**
	 * Prueft, ob ein Objekt einem User Objekt gleicht.
	 * Gleichheit bedeutet hier, dass alle Attribute der Objekte uebereinstimmen.
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (this != obj)
			return false;
		User other = (User) obj;
		if (googleID != other.googleID)
			return false;
		
		return true;
	}
	
	 /**
	  * Die toString Methode gibt die Attribute der <code>User</code> Klasse textuell aus.
	  * Sie gibt die das User Objekt als String zurueck.
	  */
	public String toString(){
		return "User " + googleID + ": "+  gMail;
	}

	
	
}