package de.hdm.kontaktsystem.shared.bo;

import java.io.Serializable;

/**
 * <p>
 * Die Klasse <code>Participation</code> stellt die Teilhaberschafts-Beziehung 
 * zwischen einem User und einem Geschaeftsobjekt (Contact, ContactList, PropertyValue) dar.
 * </p>
 * <p>
 * Jede Teilhaberschaft, und damit jede Instanz der Klasse <code>Participation</code> bezieht 
 * sich auf einen User, der Teilhaber (participant) ist und das geteilte BusinessObject (reference). 
 * Die Verweise auf die Objekte finden durch die eindeutige ID der Objekte statt. Ausserdem wird 
 * abgebildet, ob ein Objekt, beispielsweise ein Kontakt, vollstaendig geteilt wurde.
 * </p>
 * 
 * @author Sandra Prestel
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
	 * Gibt an, ob Kontakte oder Listen vollstaendig oder nur teilweise geteilt wurden
	 */
	private boolean shareAll = false;
	
	/**
	 * Leerer Konstruktor
	 */
	public Participation() {
		
	}
	
	/**
	 * Konstruktor, der alle Attribute mit Werten belegt
	 * @param participant Teilhaber am BusinessObject
	 * @param reference Geteiltes BusinessObject
	 */
	public Participation(User participant, BusinessObject reference) {
		this.participant = participant;
		this.reference = reference;
		
	}
	
	/**
	 * Zurueckgeben der TeilhaberID
	 * @return ID des Teilhabers am BusinessObject
	 */
	public double getParticipantID() {
		return this.participant.getGoogleID();
		
	}

	/**
	 * Zurueckgeben des Teilhabers
	 * @return Teilhaber am BusinessObject
	 */
	public User getParticipant() {
		return this.participant;
	}
	
	/**
	 * Zurueckgeben des ShareAll werts
	 * @return Angabe, ob das gesamte Objekt geteilt wurde
	 */
	public boolean getShareAll() {
		return shareAll;	
	}
	
	/**
	 * Setzen des Teilhabers
	 * @param participant Teilhaber am BusinessObject
	 */
	public void setParticipant(User participant) {
		this.participant = participant;
	}

	
	/**
	* Zurueckgeben der ID des geteilten BusinessObjects
	 * @return Die ID des geteilten BusinessObjects
	*/
	public int getReferenceID() {
		return this.reference.getBoId();
	}

	/**
	 * Zurueckgeben des BusinessObjects
	 * @return Das geteilte BusinessObject
	 */
	public BusinessObject getReferencedObject() {
		return this.reference;
	}
	
	/**
	 * Setzen des geteilten BusinessObjects
	 * @param reference Geteiltes BusinessObjekt
	 */
	public void setReference(BusinessObject reference) {
		this.reference = reference;
	}
	
	/**
	 * Setzen des ShareALL werts
	 * @param all Angabe, ob das gesamte Objekt geteilt wurde
	 */
	public void setShareAll(boolean all) {
		 shareAll = all;
	}

	/**
	 * Gibt eine Repraesentation des Participation-Objekts als String zurueck.
	 * Dieser beinhaltet alle Attribute und deren Wertauspraegungen des Objekts.
	 */
	@Override
	public String toString() {
		return "Participant = " + this.getParticipant() + "\n"		
				+ "Reference Object = " + this.getReferencedObject() + "\n"
				+ "ShareAll = " + this.getShareAll() + "\n"
		;
	}
	
	/**
	 * Gibt eine Ganzzahl zurueck, die das Participation-Objekt eindeutig identifiziert.
	 * Diese setzt sich aus allen Attributen und deren Wertauspraegungen des Objekts zusammen.
	 */
	@Override
	public int hashCode() {
		double dec = 1000000000000d;
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (getParticipantID() / dec);
		result = prime * result + getReferenceID();
		int allShared = this.getShareAll() ? 1 : 0;
		result = prime * result + allShared;
		return result;
	}

	/**
	 * Prueft, ob ein Objekt einem Participation-Objekt gleicht.
	 * Gleichheit bedeutet hier, dass alle Attribute der Objekte uebereinstimmen.
	 */
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
		if(this.getShareAll() != other.getShareAll()) {
			return false;
		}
		return true;
	}
	
	
	

}