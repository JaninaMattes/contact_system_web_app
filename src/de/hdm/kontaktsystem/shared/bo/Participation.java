package de.hdm.kontaktsystem.shared.bo;

import java.io.Serializable;

/**
 * <p>
 * Die Klasse <code>Participation</code> stellt die Teilhaberschafts-Beziehung 
 * zwischen einem User und einem Geschäftsobjekt (Contact, ContactList, PropertyValue) dar.
 * </p>
 * <p>
 * Jede Teilhaberschaft, und damit jede Instanz der Klasse <code>Participation</code> bezieht 
 * sich auf einen User, der Teilhaber (participant) ist und das geteilte BusinessObject (reference). 
 * Die Verweise auf die Objekte finden durch die eindeutige ID der Objekte statt.
 * </p>
 * 
 * @author Sandra
 *
 */


public class Participation implements Serializable {
	
	/**
	 * Seriennummer, vorgegeben durch das Interface {@link Serializable}
	 */
	
	//TODO: Logik klären Soll hier auf UserID verwiesen werden?
	private User owner = null;
	private static final long serialVersionUID = 1L;
	
	// Owner = Owner des BusinessObjects, das geteilt wird
	
	/**
	 * Verweis auf die ID des Users, der an der Teilhaberschaft teilnimmt
	 */
	private int participantID = 0;
	
	/**
	 * Verweis auf die ID des geteilten BusinessObjects
	 */
	private int referenceID = 0;

	
	/**
	 * Leerer Konstruktor
	 */
	public Participation() {
		
	}
	
	/**
	 * Konstruktor, der alle Attribute mit Werten belegt
	 */
	public Participation(int participantID, int referenceID) {
		this.participantID = participantID;
		this.referenceID = referenceID;
	}
	

	
	/**
	* Zurückgeben der TeilhaberID
	*/
	public int getParticipantID() {
		return participantID;
	}

	/**
	 * Setzen der TeilhaberID
	 */
	public void setParticipantID(int participantID) {
		this.participantID = participantID;
	}

	
	/**
	* Zurückgeben der ID des geteilten BusinessObjects
	*/
	public int getReferenceID() {
		return referenceID;
	}

	/**
	 * Setzen der ID des geteilten BusinessObjects
	 */
	public void setReferenceID(int referenceID) {
		this.referenceID = referenceID;
	}

	
	
	@Override
	public String toString() {
		return "Participation [owner=" + owner + ", participantID=" + participantID + ", referenceID=" + referenceID
				+ "]";
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + participantID;
		result = prime * result + referenceID;
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
		Participation other = (Participation) obj;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (participantID != other.participantID)
			return false;
		if (referenceID != other.referenceID)
			return false;
		return true;
	}
	
	
	

}