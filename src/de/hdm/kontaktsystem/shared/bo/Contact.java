package de.hdm.kontaktsystem.shared.bo;


/**
 * Die Klasse Contact stellt die Kontakte dar, die von einem User erstellt werden bzw. auf welche ein User zugreifen kann, 
sowie den User selbst.
 * @author Katalin
 *
 */


public class Contact extends BusinessObject {

		
	/**
	 * Name des Kontakts
	 */
	private PropertyValue propertyValue = null;

	
	/**
	 * Kontruktoren
	 * 
	 */
	public Contact() {
	
	}
	
	/**
	 * Konstruktor muss mindestens das Setzen einer Eigenschaftsausprägung erzwingen.
	 * Der Kontakt <code>Contact</code> darf nie leer angelegt werden. 
	 * 
	 */
	
	public Contact(PropertyValue pV, User owner) {
		super.setOwner(owner);
		this.propertyValue = pV;
	}
	
	public Contact(PropertyValue pV) {
		this.propertyValue = pV;
	}

	
	/**
	 * Kontact Name auslesen
	 */
	
	public PropertyValue getpropertyValue() {
		return this.propertyValue;
	}

	/**
	 * Kontakt Name setzen
	 */

	public void setpropertyValue(PropertyValue pV) {
		this.propertyValue = pV;
	}


	
	@Override
	public String toString() {
		if(propertyValue == null){
			return "Contact [owner=" + getOwner() + ", id=" + id + ", Eigenschaft = leer Ausprägung = leer ]";
		}
		
		return "Contact [owner=" + getOwner() + ", id=" + id + ", Eigenschaft = "
				+ propertyValue.getProp() + "Ausprägung =" + propertyValue.getValue() + "]";
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
