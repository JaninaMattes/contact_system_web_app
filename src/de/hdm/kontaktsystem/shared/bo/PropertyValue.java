package de.hdm.kontaktsystem.shared.bo;

import java.util.Vector;
import de.hdm.kontaktsystem.shared.bo.Property;

/**
 * Die Klasse <code>PropertyValue</code>... //TODO:  
 */


public class PropertyValue extends BusinessObject {

	/*
	 * Die default SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;
			
	/*
	 * Wert der Eigenschaftsausprägung
	 */
	private String value = null;
	
	/*
	 * Referenz auf zugehöriges Eigenschafts Objekt
	 */
	private Property prop = null;
	
	/*
	 * Referenz auf zugehöriges Kontakt Objekt
	 */
	private Contact contact = null;	

	/*
	 * Default Konstruktor 
	 */
	public PropertyValue() {
		
	}
	
	/*
	 * Konstruktor 
	 */
	
	public PropertyValue(String value, Property prop) {		
		this.value = value;		
		this.prop = prop;
		
	}
	
	public PropertyValue(String value) {		
		this.value = value;		
		
	}

	/*
	 * Auslesen des Ausprägungswertes
	 */
	
	public String getValue() {
		return value;
	}

	/*
	 * Setzen des Ausprägungswertes.
	   */
	
	public void setValue(String value) {
		this.value = value;
	}
	
	/*
	 * Abruf des zugehörigen Eigenschaft Objektes
	 */
	
	public Property getProperty() {
		return prop;
	}

	/*
	 * Setzen des zugehörigen Eigenschaft Objektes
	 */
	
	public void setProperty(Property prop) {
		this.prop = prop;
	}
	
	/*
	 * Abruf des zugehörigen Kontakt Objektes
	 */
	public Contact getContact() {
		return contact;
	}

	/*
	 * Setzen des zugehörigen Kontakt Objektes
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
	
	 public boolean equals(Object o) {
		    /*
		     * Abfragen, ob ein Objekt ungl. NULL ist und ob ein Objekt gecastet werden
		     * kann
		     */
		    if (o != null && o instanceof PropertyValue) {
		      PropertyValue propVal = (PropertyValue) o;
		      try {
		        return super.equals(propVal);
		      }
		      catch (IllegalArgumentException e) {
		        return false;
		      }
		    }
		    return false;
		  }


	 /*
	  * Die toString Methode 
	  * 
	  */
	 
	  public String toString() {
		  if(prop == null){
			  return "PropertyValue: " + "\n" 
						+ "Eigenschaft = leer \n" 
						+ "Ausprägung = " + value+ " \n";
		  }
		return "PropertyValue: " + "\n" 
				+ "Eigenschaft = " + prop.getDescription() + " \n" 
				+ "Ausprägung = " + value+ " \n";
		}



}
