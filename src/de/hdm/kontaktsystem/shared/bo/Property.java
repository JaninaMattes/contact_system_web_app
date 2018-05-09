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
	 */
	
	private String status = null;
	
	/*
	 * Anwendung eines ENUM als Variable um ein Set an 
	 * vordefinierten Konstanten geteilt (shared) oder 
	 * nicht geteilt (unshared) bietet
	 */
	
	private Status state = null;
	
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
	
	public Property(String description, Status state) {
		this.description = description;
		this.state = state;	
		
		
	}
	
	public Property(String value, String description, Status state) {
		this.description = description;
		this.state = state;
		
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
		
	 /**
		 * Hier findet die Anwendung eines <code> ENUM </code> statt
		 * Dies ermöglicht ein Set an vordefinierten Konstanten auszugeben
		 * in diesem Fall wäre dies für den Status geteilt oder nicht geteilt
		 * @return Status der Property
		 */	
	
	public String getStatus() {
		
		switch (state) {
        case SHARED: this.status = "shared";
        	break;
        case NOTSHARED: this.status = "not shared";
        	break;
        default:
            break;
        }
		return status;
	}


	public Status getState() {
		return state;
	}

	public void setState(Status state) {
		this.state = state;
	}

	public Vector<PropertyValue> getPropertyValues() {
		return propertyValues;
	}

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
