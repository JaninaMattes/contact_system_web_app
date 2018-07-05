  package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;

/**
 * Die Mapper-Klasse <code>PropertyMapper</code> bildet <code>Property</code>-Objekte
 * auf eine relationale Datenbank ab. Dazu werden Methoden zum Erzeugen, Suchen, Aendern und
 * Loeschen von Objekten zur Verfuegung gestellt. Es koennen sowohl Objekte in Datenbank-Strukturen,
 * als auch Datenbank-Strukturen in Objekte Ueberfuehrt werden.
 *
 * @author Janina Mattes
 *
 */
 
public class PropertyMapper {
   
   
    /**
     * Die Klasse <code>PropertyMapper</code> ist ein Singleton (Singleton Design Pattern),
     * d.h. sie wird nur einmal instantiiert. Die statische Variable <code>INSTANCE</code> speichert
     * die einzige Instanz der Klasse. Durch den Bezeichner <code>static</code> ist diese Variable
     * nur einmal fuer alle Instanzen der Klasse vorhanden.
     *
     */
   
      private static PropertyMapper propertyMapper = null;
     
      /**
        * Der Konstruktor ist <code>private</code>, um einen Zugriff von ausserhalb der Klasse zu verhindern.
        */
 
      private PropertyMapper() {
         
      }
     
      /**
         * Hier findet die Anwendung des <code> Singleton Pattern </code> statt
         * Diese Methode gibt das einzige Objekt dieser Klasse zurueck.
         * 
         * @return Instanz des PropertyMapper
         */        
 
      public static PropertyMapper propertyMapper() {
        if (propertyMapper == null) {
          propertyMapper = new PropertyMapper();
        } 
        return propertyMapper;
      }
      
      

      /**
       * Einfuegen eines <code>Property</code>-Objekts in die Datenbank. Dabei wird
       * auch der Primaerschluessel des uebergebenen Objekts geprueft und gegebenfals
       * berichtigt.
       *
       * @param property ist das zu speichernde Objekt
       * @return das bereits uebergebene Objekt, jedoch mit gegebenfals korrigierter
       * 
       * 
       * <code>id</code>.
       */    
     
      public Property insert(Property property) {
    	       	  
          Connection con = DBConnection.connection();
            
          try {        	  
              	// Die Einfuegeoperation erfolgt	
              	PreparedStatement stmt = con.prepareStatement("INSERT INTO Property (description) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
    			stmt.setString(1, property.getDescription());
    			stmt.executeUpdate();
    			ResultSet rs = stmt.getGeneratedKeys();
    			// Setzt die generrierte ID in die neue Eigenschaft
				if (rs.next()) {
					property.setId(rs.getInt(1));
					return property;
                }                  
          	} catch(SQLException e) {
              e.printStackTrace();
          }  
          return null;
      }
      
      
      /**
       * Die Methode <em>findAll</em> ermoeglicht das Abrufen aller Property Objekte
       * in der Datenbank. Diese werden ueber einen Vector mit Property-Objekten, und
       * nach deren zugehoerigen User (ueber deren User ID) gefiltert zurueck gegeben.
       * 
       * @return Vector mit Property
       */
      
      public Vector <Property> findAll(){
    	  
    	  // Vector zur Speicherung aller Properties
		  Vector<Property> propertyResult = new Vector<Property>();			
		  Connection con = DBConnection.connection();    	   
    	  
  		try{			
  			// Leeres SQL Statement anlegen
  			Statement stmt = con.createStatement();
  			
  			// Alle Property Objekte aus DB abrufen          
  			ResultSet rs = stmt.executeQuery( 
					  "SELECT Property.ID, Property.description "
					+ "FROM Property "  
					+ "ORDER BY Property.ID ");		//ORDER BY um nach PropID zu sortieren.
  			
  			while(rs.next()){  			
  				
  				Property property = new Property();  				
  				
  				property.setId(rs.getInt("ID"));
  				property.setDescription(rs.getString("description"));
  				
  				 	
  				 // Hinzufuegen des neuen Objekts zum Ergebnisvektor
                propertyResult.addElement(property);
  				
  				}  					
  			return propertyResult;
  			
  		}catch(SQLException e){
  			e.printStackTrace();
  		}  		
  		return null;    	  
      }
   
      
      
     /**
       * Suchen einer Eigenschaft <code>Property</code> - Objekts innerhalb der DB anhand derer Primaerschluessel ID.
       * Da diese eindeutig ist, wird genau ein Eigenschafts Objekt zur�ckgegeben.
       *
       * @param prop ist ein Property-Objekt 
       * @return Property Objekt, das dem uebergebenen Schluessel entspricht,
       * dies wird null, wenn kein Datenbank Tupel vorhanden ist.
       * 
       */             
          
     public Property findBy(Property prop) {
    	 return findBy(prop.getId());
     }
     
     
     
     /**
      * Suchen einer Eigenschaft <code>Property</code> - Objekts innerhalb der DB anhand derer Primaerschluessel ID.
      * Da diese eindeutig ist, wird genau ein Eigenschafts Objekt zurueckgegeben.
      *
      * @param id ist das Primaerschluesselattribut
      * @return Property Objekt, das dem uebergebenen Schluessel entspricht,
      * dies wird null, wenn kein Datenbank Tupel vorhanden ist.
      * 
      */      
     
      public Property findBy(int property_id) {       
    	  Property property = new Property();
          Connection con = DBConnection.connection();          
         
          try {
              // Leeres SQL Statement anlegen  
              PreparedStatement stmt = con.prepareStatement( 
            		  "SELECT Property.ID, Property.description "
            		+ "FROM Property "
            		+ "WHERE Property.ID = ? "
            		+ "ORDER BY Property.ID");  // ORDER BY um die PropID zu sortieren.
              
              stmt.setInt(1, property_id);
              // Statement ausfuellen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery();     
                                        
              if (rs.next()) {
                  Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
                  
                  property.setId(rs.getInt("ID"));
                  property.setDescription(rs.getString("description"));
                  //propertyValues = PropertyValueMapper.propertyValueMapper().findBy(property);                  
                  property.setPropertyValues(propertyValues);            
                 
              }              
              return property;
              
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return null;
      }
         
      
      
      /**
       * Suchen eines <code>Property</code> -Objektes innerhalb der DB anhand dessen Beschreibung.
       * Da dies in diesem Fall eindeutig ist, wird genau ein Eigenschafts - Objekt zurueckgegeben.
       *
       * @param description ist ein Attribut in der Property Tabelle
       * @return Property Objekt, das dem uebergebenen Schluessel entspricht,
       * dies wird null, wenn kein Datenbank Tupel vorhanden ist.
       */
           
      public Property findBy(String description) {

          Property property = new Property(); 
          // Verbindung zur DB herstellen
          Connection con = DBConnection.connection();  
          try {              
             // Statement ausfuellen und als Query an die DB schicken
             PreparedStatement stmt = con.prepareStatement(
            		 			"SELECT* FROM Property "
                              + "WHERE Property.description = ? "
                              + "ORDER BY Property.ID" );  // ORDER BY um nach PropID zu sortieren.
             
             stmt.setString(1, description);
             ResultSet rs = stmt.executeQuery();           
              
             if (rs.next()) {
            	 
                  Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();                                 
                  property.setId(rs.getInt("ID"));
                  property.setDescription(rs.getString("description"));
                  
                  
                  //propertyValues = PropertyValueMapper.propertyValueMapper().findBy(property);
                  property.setPropertyValues(propertyValues);   
                  return property;
                }             
                 
          } catch (SQLException e) {
              e.printStackTrace();
          }         
          return null;         
      }
   
      
      
      /**
       * Ersetzt die Beschreibung eines <code>Property</code> -Objektes gegen eine neue Beschreibung.
       *
       * @param property ist das zu veraendernde Objekt
       * @return das bereits uebergebene Objekt, jedoch mit gegebenfals korrigierter
       * Es wird null zurueckgegeben, wenn kein Datenbank eintrag veraendert wurde.
       */
      public Property update(Property property) {
       	  
          Connection con = DBConnection.connection();
            
          try {        	  
              	// Die Einfuegeoperation erfolgt	
              	PreparedStatement stmt = con.prepareStatement("UPDATE Property SET description = ? WHERE ID = ?");
    			stmt.setString(1, property.getDescription());
    			stmt.setInt(2, property.getId());
    			if(stmt.executeUpdate() > 0){
    				return property;
    			}
    			
    			// Die Einfuegeoperation fuer PropertyValue
    			Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
        	  	propertyValues = property.getPropertyValues();
        	  
                // Eintrag in PropertyValue erfolgt
                for (PropertyValue pV : propertyValues){                 
                		PropertyValueMapper.propertyValueMapper().insert(pV);

                }                  
          	} catch(SQLException e) {
              e.printStackTrace();
          } 
          return null;
      }
     
      
      /**
       * Loescht ein <code>Property</code> -Objekteaus der Datenbank.
       *
       * @param property ist das zu veraendernde Objekt
       * @return das bereits uebergebene Objekt um es aus der Anzeige zu entfernen
       * Es wird null zurueckgegeben, wenn kein Datenbank eintrag veraendert wurde.
       */ 
      public Property delete(Property property) {
       	  
          Connection con = DBConnection.connection();
            
          try {        	  
              	// Die Einfuegeoperation erfolgt	
              	PreparedStatement stmt = con.prepareStatement("DELETE FROM Property WHERE ID = ?");
    			stmt.setInt(1, property.getId());
    			if(stmt.executeUpdate() > 0){
    				return property;
    			}
    			                 
          	} catch(SQLException e) {
              e.printStackTrace();
          } 
          return null;
      }
      
     
}
         
      
     
