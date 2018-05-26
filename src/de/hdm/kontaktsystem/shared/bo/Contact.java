package de.hdm.kontaktsystem.shared.bo;

import java.util.Vector;

/**
 * Die Klasse Contact stellt die Kontakte dar, die von einem User erstellt werden bzw. auf welche ein User zugreifen kann, 
sowie den User selbst.
 * @author Katalin
 *
 */


public class Contact extends BusinessObject {

		
	/**
	 * Eigenschaftsausprägungen des Kontakts
	 */
	private Vector <PropertyValue> propertyValues = null;
	
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
	

	public Contact(PropertyValue name) {
		this.propertyValues.add(name);
	}
	
	
	public Contact(PropertyValue name, User owner) {
		super.setOwner(owner);
		this.propertyValues.addElement(name);
	}

	
	/**
	 * Kontact Name auslesen
	 */
	
	public Vector <PropertyValue> getpropertyValues() {
		return this.propertyValues;
	}

	/**
	 * Kontakt Name setzen
	 */

	public void setpropertyValues(Vector <PropertyValue> pV) {
		this.propertyValues = pV;
	}
	
	public PropertyValue getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(PropertyValue propertyValue) {
		this.propertyValue = propertyValue;
	}

	/**
	 * Einzelne PropertyValue -Objekte hinzufügen
	 * @param pv
	 */
	
//	public void addPropertyValue(PropertyValue pv) {
//		this.propertyValues.addElement(pv);
//	}

	
	@Override
	public String toString() {

//		if(propertyValues.isEmpty()){
//			return "Contact [owner=" + getOwner() + ", id=" + getBo_Id() + ", Eigenschaft = leer Ausprägung = leer ]";
//		}
		
		return "Contact [owner=" + getOwner() + ", id=" + getBo_Id() + ", Eigenschaft = "
				+ propertyValues + "]";
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
		if (getBo_Id() != other.getBo_Id())
			return false;
		if (getOwner() == null) {
			if (other.getOwner() != null)
				return false;
		} else if (!getOwner().equals(other.getOwner()))
			return false;
		return true;
	}
	
	
	
}
