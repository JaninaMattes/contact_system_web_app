package de.hdm.kontaktsystem.shared.bo;

import de.hdm.kontaktsystem.shared.bo.Property;

public class PropertyValue extends BusinessObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			
	private int propertyValueID;
	private String value = null;
	private Property prop = null;
	
	/*
	 * Beschreibung des Status, dieser ist entweder geteilt 
	 * (shared) oder nicht geteilt (unshared)
	 * Per default gilt ein neu erstelltes Property Objekt
	 * nicht geteilt, daher wird dieser zuerst als false angegeben
	 * 
	 */
	
	private boolean shared_Status = false;	
	
	
	// TODO: Überprüfen ob die Verbindung von Property/PropertyValue so sinnvoll ist
	
	public PropertyValue() {
		
	}
	
	public PropertyValue(String value) {
		
		this.value = value;		
		prop = new Property(value);

	}
	
	public int getPropertyValueID() {
		return propertyValueID;
	}

	public void setPropertyValueID(int propertyValueID) {
		this.propertyValueID = propertyValueID;
	}

	/*
	 * Auslesen des Ausprägungswertes
	 */
	
	public String getValue() {
		return value;
	}

	/**
	   * Setzen des Ausprägungswertes.
	   * @param value der neuen Eigenschaft
	   */
	
	public void setValue(String value) {
		this.value = value;
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
	
	

}
