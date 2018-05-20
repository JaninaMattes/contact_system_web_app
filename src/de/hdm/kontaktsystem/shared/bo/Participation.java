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
	private static final long serialVersionUID = 1L;

	
	/**
	 * Verweis auf die ID des Users, der an der Teilhaberschaft teilnimmt
	 */
	private User participant = null;
	
	/**
	 * Verweis auf die ID des geteilten BusinessObjects
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
	public Participation(User participant, BusinessObject reference) {
		this.participant = participant;
		this.reference = reference;
		
	}
	
	/**
	* Zurückgeben der TeilhaberID
	*/
	public double getParticipantID() {
		return this.participant.getGoogleID();
		
	}

	/**
	 * Zurückgeben des Teilhabers
	 */
	public User getParticipant() {
		return this.participant;
	}
	
	/**
	 * Setzen des Teilhabers
	 */
	public void setParticipant(User participant) {
		this.participant = participant;
	}

	
	/**
	* Zurückgeben der ID des geteilten BusinessObjects
	*/
	public int getReferenceID() {
		return this.reference.getBo_Id();
	}

	/**
	 * Zurückgeben des BusinessObjects
	 */
	public BusinessObject getReferencedObject() {
		return this.reference;
	}
	
	/**
	 * Setzen des geteilten BusinessObjects
	 */
	public void setReference(BusinessObject reference) {
		this.reference = reference;
	}

	
	//Anpassen
	@Override
	public String toString() {
		return "Participation [participantID=" + this.getParticipantID() + ", referenceID=" + this.getReferenceID()
				+ "]";
	}
	
	//Anpassen
	@Override
	public int hashCode() {
		double dec = 1000000000000d;
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (getParticipantID() / dec);
		result = prime * result + getReferenceID();
		return result;
	}

	//Anpassen
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Participation other = (Participation) obj;
		if (this.getParticipantID() != other.getParticipantID())
			return false;
		if (this.getReferenceID() != other.getReferenceID())
			return false;
		return true;
	}
	
	
	

}