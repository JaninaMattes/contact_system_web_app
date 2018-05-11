package de.hdm.kontaktsystem.shared.bo;

import java.util.Vector;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;

/**
 * <p>
 * Die Klasse <code>Property</code> stellt die Eigenschaften eines Kontaktes dar
 * diese können einem Nutzer, dessen Kontakten oder Kontaktlisten zugeordnet werden.
 * </p>
 * <p>
 * Ein Nutzer (User Klasse) ist dabei entweder der Besitzer (owner) der Eigenschaft oder 
 * ein Teilhaber dieser (participant).
 * </p>
 * 
 * @author Janina Mattes
 *
 */

public class Property extends BusinessObject{
	

	private static final long serialVersionUID = 1L;
	
	/*
	 * Beschreibung der Eigenschaft
	 */
	
	private String description = null;
	
	/*
	 * Beschreibung des Status, dieser ist entweder geteilt 
	 * (shared) oder nicht geteilt (unshared)
	 * Per default gilt ein neu erstelltes Property Objekt
	 * nicht geteilt, daher wird dieser zuerst als false angegeben
	 * 
	 */
	
	private boolean shared_Status = false;	
	
	/*
	 * Jeder Eigenschaftinstanz ist einer oder mehrerer Eigenschaftsausprägungen
	 * zugeordnet. Diese werden durch eine Vector Liste repräsentiert
	 * 
	 * */
	
	private Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
	
	/*
	 * Bei der Erzeugung einer neuen Eigenschaftinstanz muss ebenso eine neue
	 * Eigenschaftsausprägung dieser erzeugt und zugeordnet werden
	 */
		
	public Property () {
		
	}
	
	public Property(String description) {
		this.description = description;
		
	}
	
	public Property(String value, String description) {
		this.description = description;				
		// erstellen einer zugehörigen Eigenschaftsausprägung zu einem Eigenschaft Objekt
		PropertyValue propertyValue = new PropertyValue(value);
		propertyValues.addElement(propertyValue);
		
	}	

	/*
	 * Auslesen der Beschreibung eines Eigenschafts Objektes
	 */

	public String getDescription() {
		return description;
	}

	/**
	   * Setzen der Beschreibung eines Eigenschaft Objekts.
	   * @param description der neuen Eigenschaft
	   */
	
	public void setDescription(String description) {
		this.description = description;
	}		
		
	/*
	 * Abrufen des Teilhaber Status 
	 * dieser gibt Informationen darüber ob 
	 * eine Eigenschaft geteilt oder nicht geteilt wurde
	 * @return shared_Status ist boolean
	 */
	
	public boolean getShared_Status() {
		return shared_Status;
	}
	
	/*
	 * Wurde eine Property Instanz mit einem anderen Nutzer
	 * im Kontaktsystem geteilt wird der isShared Status auf true gesetzt.
	 * Dieser kann später über die Oberfläche im Client dem Nutzer dargestellt
	 * werden, um so zusätzliche Informationen zu bieten. 
	 * 
	 */

	public void setShared_Status(boolean shared_Status) {
		this.shared_Status = shared_Status;
	}
	
	/*
	 * Auslesen des PropertyValue Vectors
	 *  
	 */
	
	public Vector<PropertyValue> getPropertyValues() {
		return propertyValues;
	}

	/*
	 * Setzen des PropertyValue Vectors für eine neue Liste
	 * an PropertyValues, welche einer Property zugeordnet werden
	 */
	
	public void setPropertyValues(Vector<PropertyValue> propertyValues) {
		this.propertyValues = propertyValues;
	}

	/**
	   * Erzeugen einer textuellen Darstellung der jeweiligen Eigenschaft.
	   */
	
	@Override
	public String toString() {
		return "Property [description=" + description + ", propertyValues=" + propertyValues + "]";
	}
	
	/*
	 * <p>
	 * Feststellen der <em>inhaltlichen</em> Gleichheit zweier PropertyValue-Objekte.
	 * Die Gleichheit wird in diesem Beispiel auf eine gleiche Identität
	 * beschränkt.
	 * </p>
	 *  
	 */
	
	 public boolean equals(Object o) {
		    /*
		     * Abfragen, ob ein Objekt ungl. NULL ist und ob ein Objekt gecastet werden
		     * kann
		     */
		    if (o != null && o instanceof Property) {
		      Property prop = (Property) o;
		      try {
		        return super.equals(prop);
		      }
		      catch (IllegalArgumentException e) {
		        return false;
		      }
		    }
		    return false;
		}
	

}
