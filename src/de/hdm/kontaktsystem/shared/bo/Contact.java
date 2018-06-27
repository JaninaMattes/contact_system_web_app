package de.hdm.kontaktsystem.shared.bo;

import java.util.Vector;

/**
 * Die Klasse Contact stellt die Kontakte dar, die von einem User erstellt werden bzw. auf welche ein User zugreifen kann, 
sowie den User selbst.
 * @author Katalin Wagner
 *
 */


public class Contact extends BusinessObject {

		
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Eigenschaftsausprägungen des Kontakts
	 */
	
	private Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
	
	/*
	 * Name eines Kontaktes
	 */
	private PropertyValue name = null;	
	

	/**
	 * Kontruktoren
	 * 
	 */
	public Contact() {
	
	}
	
	/**
	 * Konstruktor muss mindestens das Setzen einer Eigenschaftsausprägung erzwingen.
	 * Die erzwungene Eigenschaftsausprägung enthält mindestens den Namen eines Kontaktes.
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

	/*
	 * Setzen eines PropertyValue Objektes
	 */
	
	public void setName(PropertyValue name) {
		this.name = name;
	}

	/**
	 * Einzelne PropertyValue -Objekte dem Vector hinzufügen
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
	
	
	
	@Override
	public String toString() {

//		if(propertyValues.isEmpty()){
//			return "Contact [owner=" + getOwner() + ", id=" + getBo_Id() + ", Eigenschaft = leer Ausprägung = leer ]";
//		}
		
		return "Contact [owner=" + getOwner() + ", id=" + super.getBoId() + ", Eigenschaft = "
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
		return super.getBoId();
	}
	
	
	/**
	 * equals Methode
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
