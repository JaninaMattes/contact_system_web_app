package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Vector;

import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

/**
 * Die Mapper-Klasse <code>PropertyMapper</code> bildet <code>Property</code>-Objekte
 * auf eine relationale Datenbank ab. Dazu werden Methoden zum Erzeugen, Suchen, Ändern und
 * Löschen von Objekten zur Verfügung gestellt. Es können sowohl Objekte in Datenbank-Strukturen,
 * als auch Datenbank-Strukturen in Objekte überführt werden.
 *
 * @author Janina Mattes
 *
 */
 
public class PropertyMapper {
   
   
    /**
     * Die Klasse <code>PropertyMapper</code> ist ein Singleton (Singleton Design Pattern),
     * d.h. sie wird nur einmal instantiiert.Die statische Variable <code>INSTANCE</code> speichert
     * die einzige Instanz der Klasse. Durch den Bezeichner <code>static</code> ist diese Variable
     * nur einmal für alle Instanzen der Klasse vorhanden.
     *
     */
   
      private static PropertyMapper propertyMapper = null;
     
      /**
        * Der Konstruktor ist <code>private</code>, um einen Zugriff von außerhalb der Klasse zu verhindern.
        */
 
      private PropertyMapper() {
         
      }
     
      /**
         * Hier findet die Anwendung des <code> Singleton Pattern </code> statt
         * Diese Methode gibt das einzige Objekt dieser Klasse zurück.
         * @return Instanz des PropertyMapper
         */        
 
      public static PropertyMapper propertyMapper() {
        if (propertyMapper == null) {
          propertyMapper = new PropertyMapper();
        }
 
        return propertyMapper;
      }
      
      
      /**
       * Die Methode <em>findAll</em> ermöglicht das Abrufen aller Property Objekte
       * in der Datenbank. Diese werden über einen Vector mit Property-Objekten, und
       * nach deren zugehörigen User (über deren User ID) gefiltert zurück gegeben.
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
  			
  			// Löschoperation für Property wird aufgerufen              
  			ResultSet rs = stmt.executeQuery( 
					  "SELECT Property.ID, Property.description, "
					+ "PropertyValue.ID, PropertyValue.value "
					+ "FROM Property "  
					+ "INNER JOIN Property ON PropertyValue.property_ID = Property.ID "
					+ "WHERE PropertyValue.property_ID = Property.ID "
				    + "ORDER BY Property.ID "
					);		
  			
  			while(rs.next()){  			
  				
  				// Vector um Eigenschaftsausprägungen zu speichern
  				Vector<PropertyValue> propertyValues = new Vector <PropertyValue>();
  				
  				Property property = new Property();
  				property.setDescription(rs.getString("description"));
  				property.setId(rs.getInt("ID"));
  				  				
  				// Aufrufen aller zu einer Eigenschaft (Property) gehörigen Eigenschaftsausprägungen 
  				propertyValues = PropertyValueMapper.propertyValueMapper().findBy(property);
  				
  				// Setzen des Eigenschaftsausprägungs Vector
  				property.setPropertyValues(propertyValues);   				

                // Hinzufügen des neuen Objekts zum Ergebnisvektor
                propertyResult.addElement(property);
  				
  				}  			
  			// Rückgabe der gefundenen Property Werte
  			return propertyResult;
  			
  		}catch(SQLException e){
  			e.printStackTrace();
  		}
  		
  		return null;
    	  
      }
      
      /**
       * Die Methode <code>findAll</code> mit einem User Objekt als übergebenes 
       * Parameter ermöglicht das Abrufen aller Property Objekte
       * in der Datenbank. Diese werden über einen Vector mit Property-Objekten, und
       * nach dem zugehörigen User zurück gegeben.
       * 
       * @param user Objekt
       * @return Vector mit Property-Objekten
       */
      
      public Vector <Property> findAll(User user){    	  
    	//TODO: Abklären 
  		return null;
    	  
      }
      
      /**
       * 
       * @param property_id
       * @return
       */
      
      public Property findBy(PropertyValue pV) {     
          
    	  Property property = new Property();
          Connection con = DBConnection.connection();          
         
          try {
              // Leeres SQL Statement anlegen  
              PreparedStatement stmt = con.prepareStatement( 
            		  "SELECT Property.ID, Property.description, "
          			+ "PropertyValue.ID, PropertyValue.value "
          			+ "FROM Property "  
          			+ "INNER JOIN Property ON PropertyValue.property_ID = Property.ID "
                    + "WHERE Property.ID = ? " 
                    );
              
              stmt.setInt(1, pV.getProp().getId());
              // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery();          		 
                           
              if (rs.next()) {
                  Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
                 
                  property.setId(rs.getInt("ID"));
                  property.setDescription(rs.getString("description"));
                  
                  // Aufrufen aller zu einer Eigenschaft (Property) gehörigen Eigenschaftsausprägungen 
                  propertyValues = PropertyValueMapper.propertyValueMapper().findBy(property);
                  // Setzen des Eigenschaftsausprägungs Vector
                  property.setPropertyValues(propertyValues);
                  
              }              
              return property;
              
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return null;
      }
         
      
 
      /**
       * Abruf aller geteilter (participation) und eigener (ownership) Eigenschaften
       * der Kontakte eines Users im KontaktSystem.
       * Diese Methode soll es damit ermöglichen alle Eigenschaften, welche einerm User
       * zugewiesen werden können, sei es dass sie diesem geteilt wurden oder er
       * selbst diese erstellt hat, abzurufen.
       *
       * @param id Primärschlüsselattribut des Users
       * @return einen Vector mit Property Objekten, welche dem übergebenen User Primärschlüssel,
       * zugeordnet werden können. Dies entspricht null bei einem nicht vorhandenem DB-Tupel.
       *
       */
         

      public Vector <Property> findBy(User user){
         
          Vector <Property> propertyResult = new Vector<Property>();
          Vector <PropertyValue> propertyValueResult = new Vector <PropertyValue>();

         try { 
        	
         /**
          *  Zur Einhaltung des SoC Aufruf der PropertyValueMapper Methode.
          *  Aufruf aller PropertyValue - Objekte welche einem User im System zugeordnet werden können.
          *  Durchlauf mit einer <code>for-each</code> Schleife, um Property-Objekte über den Aufruf
          *  des zweiten PropertyValue Mapper <em>findBy(PropertyValue-Instanz)</em> zurück zu geben.
          */
         
         // Rückgabe von PropertyValue Werten 
         propertyValueResult = PropertyValueMapper.propertyValueMapper().findBy(user); 
                 
         for(PropertyValue pV: propertyValueResult) {
        	 Property property = new Property();
        	 
        	 property = this.findBy(pV);        	 
        	 if(property != null) propertyResult.add(property);
         } 
         
         // HashSet erlaubt per Default nur einzigartige Einträge
    	 HashSet<Property> uniqueEntries = new HashSet<Property>(propertyResult);   
         // Umwandeln des HashSet in einen Vector 
    	 Vector<Property> uniqueVector = new Vector<Property>(uniqueEntries);    	 
    	 
         return propertyResult = uniqueVector;
         
        } catch(NullPointerException e) {
        	e.printStackTrace();
        }         
          return null;
      }

            
      /**
       * Suchen einer Eigenschaft <code>Property</code> - Objekts innerhalb der DB anhand derer Primärschlüssel ID.
       * Da diese eindeutig ist, wird genau ein Eigenschafts Objekt zur�ckgegeben.
       *
       * @param id ist das Primärschlüsselattribut
       * @return Property Objekt, das dem übergebenen Schlüssel entspricht,
       * dies wird null, wenn kein Datenbank Tupel vorhanden ist.
       * 
       */     
     
      public Property findByID(int property_id) {       
                    
    	  Property property = new Property();
          Connection con = DBConnection.connection();          
         
          try {
              // Leeres SQL Statement anlegen  
              PreparedStatement stmt = con.prepareStatement( 
            		  "SELECT Property.ID, Property.description, "
          			+ "PropertyValue.ID, PropertyValue.value "
          			+ "FROM Property "  
          			+ "INNER JOIN Property ON PropertyValue.property_ID = Property.ID "
                    + "WHERE Property.ID = ? " 
                    );
              
              stmt.setInt(1, property_id);
              // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery();          		 
                           
              if (rs.next()) {
                  Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
                 
                  property.setId(rs.getInt("ID"));
                  property.setDescription(rs.getString("description"));
                  
                  // Aufrufen aller zu einer Eigenschaft (Property) gehörigen Eigenschaftsausprägungen 
                  propertyValues = PropertyValueMapper.propertyValueMapper().findBy(property);
                  // Setzen des Eigenschaftsausprägungs Vector
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
       * Da dies in diesem Fall eindeutig ist, wird genau ein Eigenschafts - Objekt zurückgegeben.
       *
       * @param description ist ein Attribut in der Property Tabelle
       * @return Property Objekt, das dem übergebenen Schlüssel entspricht,
       * dies wird null, wenn kein Datenbank Tupel vorhanden ist.
       */
      
     
      public Property findBy(String description) {

          Property property = new Property(); 
          // Verbindung zur DB herstellen
          Connection con = DBConnection.connection();
                   
          try {              
             // Statement ausfüllen und als Query an die DB schicken
             PreparedStatement stmt = con.prepareStatement(
            		 			"SELECT Property.ID, Property.description, "
            		 		  + "PropertyValue.ID, PropertyValue.value "
                   			  + "FROM Property "  
                   			  + "INNER JOIN Property ON PropertyValue.property_ID = Property.ID "
                              + "WHERE Property.ID = ? " 
                   			  );
             
             stmt.setString(1, description);
             // Statement ausfüllen und als Query an die DB schicken
             ResultSet rs = stmt.executeQuery();
                          
              if (rs.next()) {
            	  
                  Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
                                 
                  property.setId(rs.getInt("ID"));
                  property.setDescription(rs.getString("description"));
                  
                  // Aufrufen aller zu einer Eigenschaft (Property) gehörigen Eigenschaftsausprägungen 
                  propertyValues = PropertyValueMapper.propertyValueMapper().findBy(property);
                  // Setzen der Eigenschaftsausprägungen
                  property.setPropertyValues(propertyValues);
                 
                }              
              return property;
             
          } catch (SQLException e) {
              e.printStackTrace();
          }
         
          return null;
         
      }
   
      
      /**
       * Je nach übergebenem gesuchten Status findet der
       * Abruf aller geteilter (participation) oder nicht geteilten (owner) Eigenschaften
       * der Kontakte eines Users im KontaktSystem statt.
       * Diese Methode soll es ermöglichen alle Eigenschaften, welche einerm User
       * zugewiesen werden können und mit ihm geteilt wurden abzurufen.
       *
       * @param shared_status des gesuchten Objectes
       * @return einen Vector mit Property Objekten, welche dem übergebenen boolean Parameter (shared_status)
       * zugeordnet werden können. Dies entspricht null bei einem nicht vorhandenem DB-Tupel.
       *
       */
         
       
      public Vector <Property> findByStatus(User user, boolean shared_status) {         
        //TODO: Abklären           
        return null;
       
      }
     
      /**
       * Abruf aller <code>Property</code>-Objekte welche einem bestimmten 
       * User nur als Teilhaber <em>participant</em> zugeordnet werden könnten. 
       * @param participant ist ein User-Objekt
       * @return Vector <Property>
       */
      
      public Vector<Property> findByParticipation(User participant){
      	// TODO: Abklären
      	return null;
      }
      
      /**
       * Abruf aller <code>Property</code>-Objekte welche einem bestimmten 
       * User als Eigentümer <em>owner</em> zugeordnet werden könnten. 
       * @param owner ist ein User-Objekt
       * @return Vector <Property>
       */
      
      public Vector<Property> findByOwnership(User owner){
      	//TODO: Abklären
      	return null;
      }
          
      /**
       * <code>Delete Methode</code>
       * Es war eine bewusste Entscheidung die CRUD Methoden für Property-Objekte nicht in den
       * Mappern abzubilden, da Property-Objekte als statisch festgelegte Objekte festgelegt sind.
       * Diese können zwar mit anderen Nutzern <em>User</em>-Objekten im System geteilt werden,
       * jedoch sind diese nicht veränderbar. Nur <em>PropertyValue</em>-Objekte sollen in diesem Zusammen-
       * hang veränderbar sein. Property und <em>PropertyValue</em>-Objekte werden nur gemeinsam geteilt.
       * Ein PropertyValue-Objekt kann dabei auch <em>null</em> sein.
       * 
       */
      
      
      
      /**
       * Einfügen eines <code>Property</code>-Objekts in die Datenbank. Dabei wird
       * auch der Primärschlüssel des übergebenen Objekts geprüft und gegebenfals
       * berichtigt.
       *
       * @param property ist das zu speichernde Objekt
       * @return das bereits übergebene Objekt, jedoch mit gegebenfals korrigierter
       * <code>id</code>.
       */    
     
      public void insert(Property property) {
    	       	  
          Connection con = DBConnection.connection();
            
          try {        	  
              	// Die Einfügeoperation erfolgt	
              	PreparedStatement stmt = con.prepareStatement("INSERT INTO Property (ID, description) VALUES (?, ?)");
    			// stmt.setInt(1, property.getBo_Id());
    			stmt.setString(2, property.getDescription());
    			stmt.execute();
    			 
    			// Die Einfügeoperation für PropertyValue
    			Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
        	  	propertyValues = property.getPropertyValues();
        	  
                // Eintrag in PropertyValue erfolgt
                for (PropertyValue pV : propertyValues){                 
                		PropertyValueMapper.propertyValueMapper().insert(pV);
                }  
                
          	} catch(SQLException e) {
              e.printStackTrace();
          }  
          
         
      }
     
      
      /**
       * <code>Update Methode</code>
       * Analog zur Delete Methode wurde eine bewusste Entscheidung getroffen die CRUD Methoden 
       * bei den Mappern für <em>Property</em>-Objekte nicht komplett durchzusetzen. Dabei wird
       * berücksichtigt, dass die <em>Property</em>-Objekte als statisch in der DB festgelegte
       * Objekte existieren sollen. Eine Update Methode würde diesem Grundprinzip daher nicht 
       * entsprechen. 
       * 
       */
     
}
         
      
     
