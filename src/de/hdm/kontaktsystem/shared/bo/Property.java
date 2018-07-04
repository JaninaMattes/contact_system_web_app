package de.hdm.kontaktsystem.shared.bo;

import java.io.Serializable;
import java.util.Vector;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;

/**
 * <p>
 * Die Klasse <code>Property</code> stellt die Eigenschaften eines Kontaktes dar
 * diese koennen einem Nutzer, dessen Kontakten oder Kontaktlisten zugeordnet werden.
 * </p>
 * <p>
 * Ein Nutzer (User Klasse) ist dabei entweder der Besitzer (owner) der Eigenschaft oder 
 * ein Teilhaber dieser (participant). Die Teilhaberschaft wird dabei ueber die Zuordnung
 * zu der Auspraegung der Eigenschaft mit <code>PropertyValue</code> hergestellt. 
 * </p>
 * 
 * @author Janina Mattes
 *
 */

public class Property implements Serializable{
	
	/**
	 * Die default Serial Version UID vergibt jeder serialisierbaren Java Klasse
	 * eine einzigartige ID. Diese wird bei der <em> desirialisation </em> verwendet
	 * um sicherzustellen, dass Sender und Empfänger die Klassen für das empfangene
	 * Objekt geladen haben. 
	 */

	private static final long serialVersionUID = 1L;
	
	/**
	 * Eindeutige Identifikationsnummer einer Instanz dieser Klasse
	 */
	
	private int id = 0;
	
	/**
	 * Beschreibung der Eigenschaft
	 */
	
	private String description = null;
	
	/**
	 * Jeder Eigenschaftinstanz ist einer oder mehrerer Eigenschaftsauspraegungen
	 * zugeordnet. Diese werden durch eine Vector Liste repraesentiert
	 */
	
	private Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();

	
	/**
	 * Default Konstruktor
	 */
		
	public Property() {
		
	}
	
	/**
	 * Bei der Erzeugung einer neuen Eigenschaftsinstanz muss nicht zwingend
	 * eine Eigenschaftsauspraegung (PropertyValue Instanz) erzeugt werden
	 */
	
	public Property(String description) {
		this.description = description;
		
	}
	
	/**
	 * Bei der Erzeugung einer neuen Eigenschaftinstanz muss ebenso eine neue
	 * Eigenschaftsausprägung dieser erzeugt und zugeordnet werden
	 * 
	 */
	
	public Property(String value, String description) {
		this.description = description;			
		// erstellen einer zugehoerigen Eigenschaftsausprägung zu einem Eigenschaft Objekt
		PropertyValue propertyValue = new PropertyValue(value);
		propertyValues.addElement(propertyValue);		
	}	


	/**
	 * Auslesen der Id zu eindeutigen Identifikation einer Instanz	
	 * @return <code>id</code> des Property-Objektes
	 */
		
	public int getId() {
		return id;
	}
	
	/**
	 * Setzen der Id zu eindeutigen Identifikation einer Instanz
	 * @param id
	 */

	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Auslesen der Beschreibung eines Eigenschafts Objektes
	 * @return description 
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
	 * Auslesen des PropertyValue Vectors
	 */
	
	public Vector<PropertyValue> getPropertyValues() {
		return propertyValues;
	}	

	/**
	 * Setzen des PropertyValue Vectors fuer eine neue Liste
	 * an PropertyValues, welche einer Property zugeordnet werden.
	 * Moeglichkeit auch nur eine PropertyValue Instanz dem Vector
	 * beizufuegen, wenn nur ein einziges Objekt zugeordnet werden soll.
	 */
	
	public void setPropertyValues(Vector<PropertyValue> propertyValues) {
		this.propertyValues = propertyValues;
	}
	
	/**
	 * Moeglichkeit bei Bedarf auch nur ein einziges PropertyValue Objekt
	 * zu setzen. 
	 */
	
	public void setPropertyValue(PropertyValue propertyVal) {
		this.propertyValues.addElement(propertyVal);
	}
	

	/**
	 * Feststellen der <em>inhaltlichen</em> Gleichheit zweier PropertyValue-Objekte.
	 * Die Gleichheit wird in diesem Beispiel auf eine gleiche Identitaet beschraenkt.
	 */
	
	 public boolean equals(Object o) {
		    
		 	/**
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
	 
	 
	 /**
	  * Der Hash-Code liefert zu jedem Objekt eine eindeutige Integerzahl, mit der das 
	  * Objekt identifiziert werden kann. Der Hash-Wert entspricht hier einfachheitshalber 
	  * der ID des Objekts. Dies ueberschreibt die Methode hashCode() der Klasse Object.
	  */
	 
		@Override
		public int hashCode(){
			return this.id;
		}
	 

		/**
		 * Erzeugen einer textuellen Darstellung der jeweiligen Eigenschaft.
		 * Dies ueberschreibt die Methode toString() der Klasse Object
		 */
		
		@Override
		public String toString() {
			if(propertyValues == null){
				return "Property [description=" + description + ", propertyValues= leer ]";
			}
			return "Property [description=" + description + ", propertyValues=" + propertyValues + "]";
		}
		
	

}
