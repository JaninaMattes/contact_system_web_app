package de.hdm.kontaktsystem.shared.bo;

import java.io.Serializable;

/**
 * <p>
 * Die Klasse <code>Participation</code> stellt die Teilhaberschafts-Beziehung 
 * zwischen einem User und einem Geschäftsobjekt (Contact, ContactList, PropertyValue) dar.
 * </p>
 * <p>
 * Jede Teilhaberschaft, und damit jede Instanz der Klasse <code>Participation</code> bezieht 
 * sich auf einen User, der Besitzer (owner) der Teilhaberschaft ist, einen User, der Teilhaber 
 * (participant) ist und das geteilte BusinessObject (reference).
 * </p>
 * 
 * @author Sandra
 *
 */


public class Participation extends BusinessObject {
	
	/**
	 * Serial Number
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Verweis auf den User, der die Teilhaberschaft besitzt
	 */
	//TODO: Logik klären Soll hier auf UserID verwiesen werden?
	// Warum?
	private User owner = null;
	
	/**
	 * Verweis auf den User, der an der Teilhaberschaft teilnimmt
	 */
	private User participant = null;
	
	/**
	 * Verweis auf das geteilte BusinessObject
	 */
	private BusinessObject reference = null;

	
	/**
	 * Leerer Konstruktor
	 */
	public Participation() {
		
	}
	
	/**
	 * Konstruktor, der alle Attribute mit Werten belegt
	 */
	public Participation(User owner, User participant, BusinessObject reference) {
		this.owner = owner;
		this.participant = participant;
		this.reference = reference;
	}
	
	
	/**
	* Zurückgeben des Eigentümers.
	*/
	public User getOwner() {
		return owner;
	}

	/**
	 * Setzen des Eigentümers
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	
	/**
	* Zurückgeben des Teilhabers
	*/
	public User getParticipant() {
		return participant;
	}

	/**
	 * Setzen des Teilhabers
	 */
	public void setParticipant(User participant) {
		this.participant = participant;
	}

	
	/**
	* Zurückgeben des geteilten BusinessObjects
	*/
	public BusinessObject getReference() {
		return reference;
	}

	/**
	 * Setzen des geteilten BusinessObjects
	 */
	public void setReference(BusinessObject reference) {
		this.reference = reference;
	}
	
	
	

}
