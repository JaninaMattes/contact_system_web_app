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
	
	private int id;
		
	/**
	 * Name des Kontakts
	 */
	private PropertyValue pV = null;

	
	/**
	*Datum wird von BusinessObject geerbt
	*/
	
	/**
	 * Kontruktoren
	 */
	public Contact() {
		
	}

	
	public Contact(PropertyValue pV, String status, User owner) {

	}
	

	public Contact(PropertyValue pV, User owner) {
		super.setOwner(owner);
		this.pV = pV;
	}
	
	public Contact(PropertyValue pV) {
		this.pV = pV;
	}
	
	
	/**
	 * Getter und Setter
	 */
	
	
	/**
	 * Namen auslesen
	 */
	
	public PropertyValue getpV() {
		return pV;
	}

	/**
	 * Namen setzen
	 */

	public void setpV(PropertyValue pV) {
		this.pV = pV;
	}


	
	@Override
	public String toString() {
		return "Contact [owner=" + getOwner() + ", id=" + id + ", Eigenschaft = "
				+ pV.getProp() + "Ausprägung =" + pV.getValue() + "]";
	}

	
	 /**
	 * Der Hash-Code liefert zu jedem Objekt eine eindeutige Integerzahl, mit der das 
	 * Objekt identifiziert werden kann. Der Hash-Wert entspricht hier einfachheitshalber 
	 * der ID des Objekts.
	 * Dies überschreibt die Methode hashCode() der Klasse Object.
	 * 
	 */
 
	@Override
	public int hashCode(){
		return super.getBo_Id();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (id != other.id)
			return false;
		if (getOwner() == null) {
			if (other.getOwner() != null)
				return false;
		} else if (!getOwner().equals(other.getOwner()))
			return false;
		return true;
	}
	
	
	
}
