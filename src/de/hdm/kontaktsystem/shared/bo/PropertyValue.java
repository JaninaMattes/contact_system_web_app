package de.hdm.kontaktsystem.shared.bo;

import de.hdm.kontaktsystem.shared.bo.Property;

/**
 * Die Klasse <code>PropertyValue</code> stellt die Ausprägung
 * zu einer Eigenschaft <code>Property</code> dar.

 * Jede Teilhaberschaft, und damit jede Instanz der Klasse <code>Participation</code> bezieht 
 * sich auf einen User, der Teilhaber (participant) ist und das geteilte BusinessObject (reference). 
 * Die Verweise auf die Objekte finden durch die eindeutige ID der Objekte statt.
 * 
 * @author Kim-Ly
 */


public class PropertyValue extends BusinessObject {

	/**
	 * Die default SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;
			
	/**
	 * Wert der Eigenschaftsausprägung
	 */
	private String value = null;
	
	/**
	 * Referenz auf zugehöriges Eigenschafts Objekt
	 */
	private Property prop = null;
	
	/**
	 * Referenz auf zugehöriges Kontakt Objekt
	 */
	private Contact contact = null;	

	/**
	 * Default Konstruktor 
	 */
	public PropertyValue() {
		
	}
	

	/**
	 * Konstruktor der Instanziierung mit Ausprägung und Eigenschaft ermöglicht
	 */
	
	public PropertyValue(String value, Property prop) {		

		this.value = value;		
		this.prop = prop;
		
	}
	
	/**
	 * Konstruktor der Instanziierung mit Ausprägung ermöglicht
	 * @param value
	 */

	public PropertyValue(String value) {
		this.value = value;			
		
	}

	/**
	 * Auslesen des Ausprägungswertes
	 */
	
	public String getValue() {
		return value;
	}

	/**
	 * Setzen des Ausprägungswertes.
	 */
	
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Abruf des zugehörigen Eigenschaft Objekts
	 */
	
	public Property getProperty() {
		return prop;
	}

	/**
	 * Setzen des zugehörigen Eigenschaft Objekts
	 */
	
	public void setProperty(Property prop) {
		this.prop = prop;
	}
	
	/**
	 * Abruf des zugehörigen Kontakt Objekts
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * Setzen des zugehörigen Kontakt Objekts
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}


	/*
	 * <p>
	 * Feststellen der <em>inhaltlichen</em> Gleichheit zweier PropertyValue-Objekte.
	 * Die Gleichheit wird in diesem Beispiel auf eine gleiche Identität
	 * beschränkt.
	 * </p>
	 * 
	 * 
	 */
	@Override
	 public boolean equals(Object o) {
		    /*
		     * Abfragen, ob ein Objekt ungl. NULL ist und ob ein Objekt gecastet werden
		     * kann
		     */
		
		    if (o == null) return false;
		    if (!(o instanceof PropertyValue))  return false;
		    if (this.getBoId() != ((PropertyValue) o).getBoId())  return false;
		    return true;
	}


	 /**
	  * Die toString Methode, die Attribute der <code>PropertyValue</code> Klasse textuell in Konsole ausgibt 
	  * 
	  */
	 
	  public String toString() {
		  if(prop == null){
			  return "PropertyValue: " + "\n" 
						+ "Eigenschaft = leer \n" 
						+ "Ausprägung = " + value+ " \n";
		  }
		  	 return "PropertyValue: " +prop.getId()+ "\n" 
				+ "Eigenschaft = " + prop.getDescription() + " \n" 
				+ "Ausprägung = " + value + " \n";
		  	 	//+ "Contact = " + this.getName() + "\n";
		}



}
