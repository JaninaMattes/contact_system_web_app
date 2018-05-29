package de.hdm.kontaktsystem.shared.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Die Klasse <code>BusinessObject</code> ist die Superklasse aller in diesem 
 * Projekt wichtigen Geschäftsobjekt-Klassen.
 * </p>
 * <p>
 * Jedes Geschäftsobjekt, und damit jede Instanz der Klasse <code>BusinessObject</code> 
 * hat eine eindeutige Nummer, die in der Datenbank als Primärschlüssel verwendet wird.
 * Außerdem ist jedes <code>BusinessObject</code> {@link Serializable}. Dadurch kann es
 * automatisch in eine textuelle Form überführt und z.B. zwischen Client und Server 
 * transportiert werden. Bei GWT RPC ist diese textuelle Notation in JSON 
 * (siehe http://www.json.org/) kodiert.
 * Weiterhin enthält jedes Geschäftsobjekt Attribute, die auf das Erstellungs- und 
 * Modifikationsdatum der jeweiligen Instanz verweisen sowie eine user_ID, die der 
 * eindeutigen ID eines User-Objekts entspricht.
 * 
 * (vgl. Vorlesung Software Projekt, BankProjekt 2.0, Klasse BusinessObject)
 * </p>
 * 
 */


public class BusinessObject implements Serializable{
	
	/**
	 * Seriennummer, vorgegeben durch das Interface {@link Serializable}
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Eindeutige Identifikationsnummer einer Instanz der Subklasse dieser Klasse.
	 */
	private int bo_id = 0;
	
	/**
	 * Datum des Erstellens und der letzten Änderung einer Instanz dieser Klasse.
	 */
	private Date creationDate = null;
	private Date modifyDate = null;
	
	/**
	 * Ersteller eines BO-Objektes
	 */
	private User owner = null;
    
	/**
	 * Shared Status einer Instanz der Subklasse dieser Klasse 
	 */
	private boolean shared_status = false; 
	
	public BusinessObject(){
		
	}
		
	/**
	* Zurückgeben der ID.
	*/
	public int getBoId() {
		return this.bo_id;
	}


	/**
	 * Setzen der ID
	 */
	public void setBo_Id(int id) {
		this.bo_id = id;
	}		
	
	/**
	* Zurückgeben der UserID.
	*/
	public User getOwner() {
		return this.owner;
	}

	/**
	 * Setzen der ID
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	
	/**
	 * Zurückgeben des Erstelldatums
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}
	
	/**
	 * Setzen des Erstelldatums
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	
	/**
	 * Zurückgeben des letzten Änderungszeitpunkts
	 */
	public Date getModifyDate() {
		return this.modifyDate;
	}

	/**
	 * Setzen des letzten Änderungszeitpunkts
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
		
	/*
	 * Status abrufen ob ein BO geteilt wurde 
	 */

	public boolean isShared_status() {
		return shared_status;
	}

	/*
	 * Setzen des Status
	 */

	public void setShared_status(boolean shared_status) {
		this.shared_status = shared_status;
	}


	/**
	 * Erzeugen einer Darstellung der jeweiligen Instanz als String (Text).
	 * Dies überschreibt die Methode toString() der Klasse Object.
	 */
	
	@Override
	public String toString() {
		/*
		 * Zurückgeben des Klassennamens + der ID der Instanz
	     */
		return this.getClass().getName() + " #" + this.owner.getGoogleID();
	}
	  
	
	/**
	 * Der Hash-Code liefert zu jedem Objekt eine eindeutige Integerzahl, mit der das 
	 * Objekt identifiziert werden kann. Der Hash-Wert entspricht hier einfachheitshalber 
	 * der ID des Objekts.
	 * Dies überschreibt die Methode hashCode() der Klasse Object.
	 */
	@Override
	public int hashCode(){
		return this.getBoId();
	}
	
	/**
	 * Feststellen, ob ein <code>BusinessObject</code>-Objekt inhaltlich einem anderen 
	 * <code>BusinessObject</code>-Objekt (hier als Parameter übergeben) gleicht. 
	 * Inhaltliche Gleichheit besteht dann, wenn der Hashcode beider Objekte übereinstimmt.
	 * Dies überschreibt die Methode equals(Object object) der Klasse Object.
	 */
	@Override
	public boolean equals(Object object) {
	    /*
	     * Sicherstellen, dass das übergebene Objekt nicht NULL ist und vom Typ BusinessObject ist
	     */
		if (object != null && object instanceof BusinessObject) {
			BusinessObject businessObject = (BusinessObject) object;
			try {
				if (businessObject.hashCode() == this.hashCode()){
					return true;
				}	
			}
			catch (IllegalArgumentException e) {
	        /*
	         * Wenn ein Fehler auftritt, wird sicherheitshalber false zurückgegeben
	         */
	        return false;
	      }
	    }
	    /*
	     * Wenn bislang keine Gleichheit bestimmt werden konnte, wird
	     * schließlich false zurückgeben.
	     */
	    return false;
	  }
	 
	  	
}