package de.hdm.kontaktsystem.shared.bo;

import java.util.Vector;

/**
 * Die Klasse Contact stellt die Kontakte dar, die von einem User erstellt werden bzw. auf welche ein User zugreifen kann, 
 * sowie den User selbst.
 * @author Katalin Wagner
 */


public class Contact extends BusinessObject {

		
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Eigenschaftsauspraegungen des Kontakts
	 */
	
	private Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
	
	/**
	 * Name eines Kontaktes
	 */
	private PropertyValue name = null;	
	

	/**
	 * Kontruktoren
	 */
	public Contact() {
	
	}
	
	/**
	 * Konstruktor muss mindestens das Setzen einer Eigenschaftsauspraegung erzwingen.
	 * Die erzwungene Eigenschaftsauspraegung enthaelt mindestens den Namen eines Kontaktes.
	 * Der Kontakt <code>Contact</code> darf nie leer angelegt werden. 
	 * 
	 */
	

	public Contact(PropertyValue name) {
		this.name = name;
	}
	
	
	public Contact(PropertyValue name, User owner) {
		super.setOwner(owner);
		this.name = name;
	}

	
	/**
	 * Kontact Name auslesen
	 */
	
	public Vector <PropertyValue> getPropertyValues() {
		return this.propertyValues;
	}

	
	/**
	 * Abruf einer PropertyValue, diese stellt den Namen des
	 * Kontaktes dar.
	 * @return PropertyValue - Objekt
	 */
	
	public PropertyValue getName() {
		return name;
	}

	/**
	 * Setzen eines PropertyValue Objektes
	 */
	
	public void setName(PropertyValue name) {
		this.name = name;
	}

	/**
	 * Einzelne PropertyValue -Objekte dem Vector hinzufuegen
	 * @param pv
	 */
	
	public void addPropertyValue(PropertyValue pv) {
		this.propertyValues.add(pv);
	}

	/**
	 * Vector an PropertyValue-Objekten setzen
	 * @param pv
	 */
	
	public void setPropertyValues(Vector <PropertyValue> pv) {
		this.propertyValues = pv;
	}
	
	
	 /**
	  * Die toString Methode gibt die Attribute der <code>Contact</code> Klasse textuell aus.
	  * Sie gibt die das User Objekt als String zurueck.
	  */
	@Override
	public String toString() {

		return "Contact [owner=" + getOwner() + ", id=" + super.getBoId() + ", Eigenschaft = "
				+ propertyValues + "]";
	}

	
	 /**
	 * Der Hash-Code liefert zu jedem Objekt eine eindeutige Integerzahl, mit der das 
	 * Objekt identifiziert werden kann. Der Hash-Wert entspricht hier einfachheitshalber der ID des Objekts.
	 * Dies ueberschreibt die Methode hashCode() der Klasse Object.
	 */
 
	@Override
	public int hashCode(){
		return super.getBoId();
	}
	
	
	/**
	 * Prueft, ob ein Objekt einem User Objekt gleicht.
	 * Gleichheit bedeutet hier, dass alle Attribute der Objekte uebereinstimmen.
	 */
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (getBoId() != ((Contact) obj).getBoId())
			return false;
		
		return true;
	}
	
	
	
}
