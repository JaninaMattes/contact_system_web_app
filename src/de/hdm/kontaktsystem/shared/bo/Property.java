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
	
	/*
	 * Die default Serial Version UID vergibt jeder serialisierbaren Java Klasse
	 * eine einzigartige ID. Diese wird bei der <em> desirialisation </em> verwendet
	 * um sicherzustellen, dass Sender und Empfänger die Klassen für das empfangene
	 * Objekt geladen haben. 
	 */

	private static final long serialVersionUID = 1L;
	
	/*
	 *  ID der Eigenschaft
	 */
	
	private int ID = 0;
	
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
	* ***************************************************************************
	* ABSCHNITT 1: Konstruktoren
	* ***************************************************************************
	*/
	
	/*
	 * Default Konstruktor
	 */
		
	public Property() {
		
	}
	
	/*
	 * Bei der Erzeugung einer neuen Eigenschaftsinstanz muss nicht zwingend
	 * eine Eigenschaftsausprägung (PropertyValue Instanz) erzeugt werden
	 */
	
	public Property(String description) {
		this.description = description;
		
	}
	
	/*
	 * Bei der Erzeugung einer neuen Eigenschaftinstanz muss ebenso eine neue
	 * Eigenschaftsausprägung dieser erzeugt und zugeordnet werden
	 */
	
	public Property(String value, String description) {
		this.description = description;		
		
		// erstellen einer zugehörigen Eigenschaftsausprägung zu einem Eigenschaft Objekt
		PropertyValue propertyValue = new PropertyValue(value);
		propertyValues.addElement(propertyValue);
		
	}	

	
	/*
	* ***************************************************************************
	* ABSCHNITT 2: Getter und Setter
	* ***************************************************************************
	*/
	
	
	/*
	 * Abruf der ID 
	 */
	
	public int getID() {
		return ID;
	}
	
	/*
	 * Setzen der ID
	 */
	
	public void setID(int iD) {
		ID = iD;
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
	 * 
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
	 * an PropertyValues, welche einer Property zugeordnet werden.
	 * Möglichkeit auch nur eine PropertyValue Instanz dem Vector
	 * beizufügen, wenn nur ein einziges Objekt zugeordnet werden soll.
	 *  
	 */
	
	public void setPropertyValues(Vector<PropertyValue> propertyValues) {
		this.propertyValues = propertyValues;
	}
	
	/*
	 * Möglichkeit nur eine einzelnee Eigenschaftsausprägung (PropertyValue) Instanz
	 * für eine Eigenschaft zu setzen
	 */
	
	public void addPropertyValue(PropertyValue value) {
		this.propertyValues.addElement(value);
	}
	
	/*
	* ***************************************************************************
	* ABSCHNITT 3: toString und equals Methode
	* ***************************************************************************
	*/
	

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
	 
	 
	 /**
		 * Der Hash-Code liefert zu jedem Objekt eine eindeutige Integerzahl, mit der das 
		 * Objekt identifiziert werden kann. Der Hash-Wert entspricht hier einfachheitshalber 
		 * der ID des Objekts.
		 * Dies überschreibt die Methode hashCode() der Klasse Object.
		 * 
		 */
	 
		@Override
		public int hashCode(){
			return this.getID();
		}
	 

		/**
		   * Erzeugen einer textuellen Darstellung der jeweiligen Eigenschaft.
		   * Dies überschreibt die Methode toString() der Klasse Object
		   * 
		   */
		
		@Override
		public String toString() {
			return "Property [description=" + description + ", propertyValues=" + propertyValues + "]";
		}
		
	

}
