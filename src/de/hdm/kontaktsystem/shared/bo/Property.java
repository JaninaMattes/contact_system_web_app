package de.hdm.kontaktsystem.shared.bo;

import java.util.Vector;



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
	 * Beschreibung der Eigenschaft
	 */
	
	private String description = null;
	
	/*
	 * Jeder Eigenschaftinstanz ist einer oder mehrerer Eigenschaftsausprägungen
	 * zugeordnet. Diese werden durch eine Vector Liste repräsentiert
	 * 
	 * */
	
	
	/*
	 * Ausprägung der Eigenschaft
	 */
	
	private String value = null;
	
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
		this.value =value;
		this.description = description;		
		
	}	
	
	

	
	/*
	* ***************************************************************************
	* ABSCHNITT 2: Getter und Setter
	* ***************************************************************************
	*/
	
	
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
	 * Auslesen der Ausprägung eines Eigenschaftsobjektes
	 */
	
	 public String getValue() {
		return value;
	}
	 
	 /*
	  * Setzen der Ausprägung einer Eigenschaft
	  */

	public void setValue(String value) {
		this.value = value;
	}
		
	
	/*
	 * Auslesen des PropertyValue Vectors
	 *  
	 */

		
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
			return super.getBo_Id();
		}
	 

		/**
		   * Erzeugen einer textuellen Darstellung der jeweiligen Eigenschaft.
		   * Dies überschreibt die Methode toString() der Klasse Object
		   * 
		   */
		
		@Override
		public String toString() {
			return "Property [description=" + description + ", propertyValues=" + value + "]";
		}
		
	

}
