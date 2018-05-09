package de.hdm.kontaktsystem.shared.bo;


/**
 * Die Klasse Contact stellt die Kontakte dar, die von einem User erstellt werden bzw. auf welche ein User zugreifen kann, 
sowie den User selbst.
 * @author Katalin
 *
 */


public class Contact extends BusinessObject {

	/**
	 *  Bezug zu dem User der den Kontakt besitzt
	 */
	private User owner = null;
	
	/**
	 * 
	 */
	private String status = null;
	
	/**
	 * Name des Kontakts
	 */
	private PropertyValue name = null;
	
	/**
	*Datum wird von BusinessObject geerbt
	*/
	
	/**
	 * Kontruktoren
	 */

	public Contact(PropertyValue name, String status, User owner) {
		this.name = name;
		this.status = status;
		this.owner = owner;
	}
	
	public Contact(PropertyValue name, String status) {
		this.name = name;
		this.status = status;
	}
	
	
	/**
	 * Getter und Setter
	 */
	
	/**
	 * Auslesen des Eigent�mers
	 */
	
	public User getOwner() {
		return owner;
	}

	/**
	 * Eigent�mer setzen
	 */

	public void setOwner(User owner) {
		this.owner = owner;
	}
	/**
	 * Status auslesen
	 */
	
	public String getStatus() {
		return status;
	}

	/**
	 * Status setzen
	 */

	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * Namen auslesen
	 */
	
	public PropertyValue getName() {
		return name;
	}

	/**
	 * Namen setzen
	 */

	public void setName(PropertyValue name) {
		this.name = name;
	}
	
}
