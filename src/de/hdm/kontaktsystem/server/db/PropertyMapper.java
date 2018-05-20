package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Logger;

import de.hdm.kontaktsystem.shared.bo.BusinessObject;
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
					  "SELECT BusinessObject.bo_ID, BusinessObject.user_ID, BusinessObject.creationDate, "
					+ "BusinessObject.modificationDate, BusinessObject.status, "
					+ "Property.ID, Property.description "
					+ "FROM BusinessObject "  
					+ "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID "
					+ "WHERE BusinessObject.bo_ID = Property.ID "
					+ "ORDER BY BusinessObject.user_ID "
					);		
  			
  			System.out.println("Aufruf SQL Statement");
  			
  			while(rs.next()){  				
  				// Vector um Eigenschaftsausprägungen zu speichern
  				Vector<PropertyValue> propertyValues = new Vector <PropertyValue>();
  				
  				Property property = new Property();
  				//property.setBo_Id(rs.getInt("bo_ID"));
  				property.setDescription(rs.getString("description"));
  				//property.setCreationDate(rs.getTimestamp("creationDate"));
  				//property.setModifyDate(rs.getTimestamp("modificationDate"));
  				//property.setShared_status(rs.getBoolean("status"));
  				//property.setOwner(UserMapper.userMapper().findUserById(rs.getDouble("user_ID")));
  				
  				System.out.println("Property id: " + rs.getInt("bo_ID"));
  				
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
     
    

      public Vector <Property> findByUser(User user){

         
          Vector <Property> propertyResult = new Vector<Property>();
         
          Connection con = DBConnection.connection();
                 
          try {
              // Leeres SQL Statement anlegen  
        	  PreparedStatement stmt = con.prepareStatement(
            		  "SELECT BusinessObject.bo_ID, BusinessObject.user_ID,"
                              + "BusinessObject.creationDate, BusinessObject.modificationDate, BusinessObject.status, "
                              + "Property.ID, Property.description " 
                              + "FROM BusinessObject " 
                              + "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID " 
                              + "WHERE BusinessObject.bo_ID = Property.ID "
                              + "AND BusinessObject.user_ID = ? "
                              // + "ORDER BY Property.Description"
                              );
        	  
        	  stmt.setDouble(1, user.getGoogleID());
        	  
        	  // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery();
                           
              while (rs.next()) {
            	  Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
                  Property property = new Property();
                  //Property-Objekt befüllen
                  //property.setBo_Id(rs.getInt("bo_ID"));
                  //property.setDescription(rs.getString("description"));
                  // Superklasse Business Object Attribute befüllen
                  //property.setCreationDate(rs.getTimestamp("creationDate"));
                  // property.setModifyDate(rs.getTimestamp("modificationDate"));
                  //property.setShared_status(rs.getBoolean("status"));

                  //property.setOwner(UserMapper.userMapper().findUserById(rs.getDouble("user_ID")));                  

                  System.out.println("Property ID: " + rs.getInt("bo_ID"));
                  System.out.println("User ID: " + rs.getDouble("user_ID"));
                  
                  // Aufrufen aller zu einer Eigenschaft (Property) gehörigen Eigenschaftsausprägungen 
                  propertyValues = PropertyValueMapper.propertyValueMapper().findBy(property);
                  // Setzen des Eigenschaftsausprägungs Vector
                  property.setPropertyValues(propertyValues); 
                                    
                  // Hinzufügen des neuen Objekts zum Ergebnisvektor
                  propertyResult.addElement(property);
                 
                }
              return propertyResult;
              
          } catch (SQLException e) {
              e.printStackTrace();
          }
         
          return null;
      }

      // @author Oli Ich habe es im Merge Conflikt mal drin gelassen, wenn es raus soll wieder löschen
    public Vector<Property> findShared(double user_id, Property property){
    	// Alle Properties welche dem User geteilt wurden
    	return null;
    }

         
      /**
       * Suchen eines Eigenschaft Objekts innerhalb der DB anhand derer Primärschlüssel ID.
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
            		  "SELECT BusinessObject.bo_ID, BusinessObject.user_ID, "
                      + "BusinessObject.creationDate, BusinessObject.modificationDate, BusinessObject.status,"
                      + "Property.ID, Property.description "
                      + "FROM BusinessObject "
                      + "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID "
                      + "WHERE BusinessObject.bo_ID = ? " 
                      );
              
              stmt.setInt(1, property_id);
              // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery();          		 
                           
              if (rs.next()) {
                  Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
                  
                 // property.setBo_Id(rs.getInt("ID"));
                 // property.setDescription(rs.getString("description"));
                  //property.setCreationDate(rs.getTimestamp("creationDate"));
                  // property.setModifyDate(rs.getTimestamp("modificationDate"));
                  //property.setShared_status(rs.getBoolean("status"));
                 // property.setOwner(UserMapper.userMapper().findUserById(rs.getDouble("user_ID")));
                  
                  System.out.println("propertyid : " + (rs.getInt("ID")));
  				  System.out.println("description : " + (rs.getString("description")));
  				  System.out.println("userid : " + (rs.getDouble("user_ID")));
                  
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
       * Suchen eines oder mehrerer Eigenschaft Objekte innerhalb der DB anhand derer Beschreibung.
       * Da dies nicht unbedingt eindeutig ist, wird nicht immer genau ein Eigenschafts Objekt zurückgegeben.
       *
       * @param description ist ein Attribut in der Property Tabelle
       * @return Property Objekt, das dem übergebenen Schlüssel entspricht,
       * dies wird null, wenn kein Datenbank Tupel vorhanden ist.
       */
     
     
      public Vector <Property> findByDescription(String description) {
         
          Vector <Property> propertyResult = new Vector <Property>();    
          // Verbindung zur DB herstellen
          Connection con = DBConnection.connection();
                   
          try {
              
              // Statement ausfüllen und als Query an die DB schicken
             PreparedStatement stmt = con.prepareStatement(
            		  "SELECT BusinessObject.bo_ID, BusinessObject.user_ID, BusinessObject.creationDate, "
                    + "BusinessObject.modificationDate, BusinessObject.status, "
                    + "Property.ID, Property.description "
                    + "FROM BusinessObject "
                    + "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID "
                    + "WHERE Property.description = ?" 
                   // + "ORDER BY Property.ID "
                    );
             
             stmt.setString(1, description);
             // Statement ausfüllen und als Query an die DB schicken
             ResultSet rs = stmt.executeQuery();
             
             System.out.println("Statement ausgeführt");
                          
              while (rs.next()) {
            	  
                  Property property = new Property(); 
                  Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
                                 
                  // property.setBo_Id(rs.getInt("ID"));
                  property.setDescription(rs.getString("description"));
                 
                  // Superklasse Business Object Attribute befüllen
                  //  property.setCreationDate(rs.getTimestamp("creationDate"));
                  // property.setModifyDate(rs.getTimestamp("modificationDate"));
                  // property.setShared_status(rs.getBoolean("status"));
                  //property.setOwner(UserMapper.userMapper().findUserById(rs.getDouble("user_ID")));
                  
                  System.out.println("Property ID: " + rs.getInt("ID"));
                  System.out.println("Description: " + rs.getString("description"));
                  System.out.println("User ID: " + rs.getDouble("user_ID"));
                  
                  // Aufrufen aller zu einer Eigenschaft (Property) gehörigen Eigenschaftsausprägungen 
                  propertyValues = PropertyValueMapper.propertyValueMapper().findBy(property);
                  // Setzen der Eigenschaftsausprägungen
                  property.setPropertyValues(propertyValues);
                 
                  propertyResult.add(property);
                }
              
              return propertyResult;
             
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
         
          Vector <Property> propertyResult = new Vector <Property>() ;                 
          Connection con = DBConnection.connection();
                   
           try {
          	        	 
               // Leeres SQL Statement anlegen  
          	 PreparedStatement stmt = con.prepareStatement( 
          			 			  "SELECT BusinessObject.bo_ID, BusinessObject.user_ID, "
          	                    + "BusinessObject.creationDate, BusinessObject.modificationDate, BusinessObject.status, "
          	                    + "Property.ID, Property.description "
          	                    + "FROM BusinessObject "
          	                    + "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID "
          	                    + "WHERE BusinessObject.user_ID = ? " 
          	                    + "AND BusinessObject.status = ? "
          	                    // + "ORDER BY Property.description"
          	                    );
          	  
          	  stmt.setDouble(1, user.getGoogleID());
          	  stmt.setBoolean(2, shared_status);
          	  
          	  // Statement ausfüllen und als Query an die DB schicken
          	  ResultSet rs = stmt.executeQuery();           		
                      
                System.out.println("Aufruf ResultSet");
                
           while (rs.next()) {
                
          	  Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
                Property property = new Property();
               
                // property.setBo_Id(rs.getInt("ID"));
                property.setDescription(rs.getString("description"));             
                // Superklasse Business Object Attribute befüllen
                //property.setCreationDate(rs.getTimestamp("creationDate"));
                // property.setModifyDate(rs.getTimestamp("modificationDate"));
                // property.setShared_status(rs.getBoolean("status"));
                // property.setOwner(UserMapper.userMapper().findUserById(rs.getInt("user_ID")));
                
                System.out.println("Property ID: " + rs.getInt("ID"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("Status: " + rs.getBoolean("status"));
                             
                // Aufrufen aller zu einer Eigenschaft (Property) gehörigen Eigenschaftsausprägungen 
                propertyValues = PropertyValueMapper.propertyValueMapper().findBy(property);
                // Setzen des Eigenschaftsausprägungs Vector
                property.setPropertyValues(propertyValues);
   
                // Hinzufügen des neuen Objekts zum Ergebnisvektor
                propertyResult.addElement(property);
               
              }
           
           return propertyResult;
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
           
        return null;
       
      }
     
      /**
       * Abruf aller <code>Property</code>-Objekte welche einem bestimmten 
       * User nur als Teilhaber <em>participant</em> zugeordnet werden könnten. 
       * @param participant ist ein User-Objekt
       * @return Vector <Property>
       */
      
      public Vector<Property> findByParticipation(User participant){
      	
      	// Alle Properties welche dem User geteilt wurden
      	// Der Nutzer ist hier nur Participant und nicht Owner
      	Vector <Property> propertyResult = new Vector <Property>();
      	System.out.println("Abruf Participation");
      	// Abruf aller Teilhaberschaften, welche mit geg. User geteilt wurden
      	Vector <Participation> participationResult = new Vector <Participation>();    	
      	participationResult = ParticipationMapper.participationMapper().findParticipationsByParticipantID(participant.getGoogleID());

      	if(participationResult != null) {
      	for (Participation p : participationResult) {
      		System.out.println("ID: " + p.getReferenceID());
      		Property property = new Property();
      		property = this.findByID(p.getReferenceID());
      		if(property != null) propertyResult.add(property);
      	}
      	return propertyResult;
      }
      	return null;
      }
      
      /**
       * Abruf aller <code>Property</code>-Objekte welche einem bestimmten 
       * User als Eigentümer <em>owner</em> zugeordnet werden könnten. 
       * @param owner ist ein User-Objekt
       * @return Vector <Property>
       */
      
      public Vector<Property> findByOwnership(User owner){
      	
      	// Alle Properties welche vom User mit andere geteilt wurden
      	Vector <Property> propertyResult = new Vector <Property>();
      	System.out.println("Abruf Participation");
      	// Abruf aller Teilhaberschaften, zu Property Objekten eines Users
      	Vector <Participation> participationResult = new Vector <Participation>();    	
      	participationResult = ParticipationMapper.participationMapper().findParticipationsByOwnerID(owner.getGoogleID());
      	System.out.println("Befüllen Participation-Objekt");
      	
      	if(participationResult != null) {
      		for (Participation p : participationResult) {
      	    
          		Property property = new Property();
          		property = this.findByID(p.getReferenceID());
          		// System.out.println("ID: " + property.getBo_Id());
          		if(property != null) propertyResult.add(property);
      		
      			}      	
      	return propertyResult;
      	
      	}
      	
      	return null;
      }
          
     
      /**
       * Löschen der Daten eines <code>Property</code>-Objekts aus der Datenbank.     *
       * @param property, welches das aus der DB zu löschende "Objekt" ist
       */
     
      public void delete(Property property) {
                
          Connection con = DBConnection.connection();
                  
          try {
             
              PreparedStatement stmt = con.prepareStatement("DELETE FROM Property WHERE Property.ID= ?");
              // Aufruf und Übergabe aller PropertyValues
              Vector <PropertyValue> propertyValueResult = new Vector<PropertyValue>();     
              propertyValueResult = property.getPropertyValues();
              
              if(propertyValueResult != null) {
            	  for (PropertyValue pV : propertyValueResult){

            		  System.out.println("Gelöscht - propertyValueId: " + pV.getBo_Id());
            		  PropertyValueMapper.propertyValueMapper().delete(pV);                 
            	  	}
            	  }                            

              // System.out.println("Gelöscht - propertyid: " + property.getBo_Id());
              
              // stmt.setInt(1, property.getBo_Id());
              stmt.executeUpdate();
              
            }
            catch (SQLException e2) {
              e2.printStackTrace();
            }          
          	// BusinessObjectMapper.businessObjectMapper().deleteBusinessObject(property);         
      }
           
      
      /**
       * Löschen der Daten eines <code>Property</code>-Objekts aus der Datenbank.     
       * @param id ist der Primärschlüssel, des aus der DB zu löschenden "Objektes"
       */
     
      public void deleteByID(int property_id) {
         
          Property property = new Property();
          Vector <PropertyValue> propertyValueResult = new Vector<PropertyValue>();
         
          Connection con = DBConnection.connection();
                      
          try {           
              
              // Abruf eines Property Objektes aus der DB
              property = this.findByID(property_id);
              
              // Abruf aller PropertyValues, welche zu einem Property Objekt gehören können                     
              propertyValueResult = property.getPropertyValues();
              
              if(propertyValueResult != null) {
            	  for (PropertyValue pV : propertyValueResult){
                  PropertyValueMapper.propertyValueMapper().delete(pV);
                  
                  System.out.println("Gelöscht PV: " + pV);
              	}
              }
              
              PreparedStatement stmt = con.prepareStatement("DELETE FROM Property WHERE Property.ID = ?");
              // Löschoperation für Property wird aufgerufen              
             // stmt.setInt(1, property.getBo_Id());
              stmt.executeUpdate();
                            
             // System.out.println("Property: " + property.getBo_Id() + " gelöscht");              

              // Löschoperation für BO Tabelle wird aufgerufen
             // BusinessObjectMapper.businessObjectMapper().deleteBusinessObject(property);
              
            }
            catch (SQLException e2) {
              e2.printStackTrace();
            }         
      }
     
    
      /**
       * Löschen sämtlicher Eigenschaften <code>Property</code> Objekte eines Nutzers.
       *  
       * @param user_id als Primärschlüssel des <code>User</code> Objekts,
       * zu dem die Properties gehören.
       */
     

      public void deleteByUser(User user) {

         
         Vector <Property> propertyResult = new Vector <Property>();
         propertyResult = PropertyMapper.propertyMapper().findByUser(user);
         System.out.println("Properties gefunden");
         if(propertyResult != null) {
        	 for (Property p : propertyResult){  
        		 
        		 // System.out.println("Gelöscht ID: " + p.getBo_Id());
        		 PropertyValueMapper.propertyValueMapper().deleteBy(p);
        		 PropertyMapper.propertyMapper().delete(p);
          }  
         }    
      }
      
      /**
       * Löschen sämtlicher Eigenschaften <code>Property</code> Objekte eines Nutzers.
       * @param user entspricht dem Nutzer dessen Eigenschaften gelöscht werden sollen.
       */
           
      public void deleteByUserID(int user_id) {
    	  // Aufruf des User-Objektes aus der DB
    	  User user = new User();
    	  user = UserMapper.userMapper().findUserById(user_id);
    	  System.out.println("userID: " + user.getGoogleID());
    	  this.deleteByUser(user);
      }
     
      
      /**
       * Einfügen eines <code>Property</code>-Objekts in die Datenbank. Dabei wird
       * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
       * berichtigt.
       *
       * @param property ist das zu speichernde Objekt
       * @return das bereits übergebene Objekt, jedoch mit gegebenfals korrigierter
       * <code>id</code>.
       */    
     
      public void insert(Property property) {
    	       	  
          Connection con = DBConnection.connection();
                    
          // Eintrag in BusinessObjekt Tabelle
          // BusinessObjectMapper.businessObjectMapper().insert(property);
             
          try {        	  
        	    System.out.println("Aufruf Prepared Statement");
              	// Die Einfügeoperation erfolgt	
              	PreparedStatement stmt = con.prepareStatement("INSERT INTO Property (ID, description) VALUES (?, ?)");
    			stmt.setInt(1, property.getId());
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
       * Wiederholtes Schreiben eines Objekts in die Datenbank.
       *
       * @param property das Eigenschaft (Property) Objekt, das in die DB geschrieben werden soll
       * @return das als Parameter übergebene Objekt
       */    
     
         
      public void updateProperty(Property property){
          Connection con = DBConnection.connection();
          
          // BusinessObjectMapper.businessObjectMapper().update(property);
                   
          try{
        	  
  			PreparedStatement stmt = con.prepareStatement("UPDATE Property SET description = ? WHERE ID = ?");
  			stmt.setString(1, property.getDescription());
  			// stmt.setInt(2, property.getBo_Id());
  			stmt.execute();
            
  			System.out.println("Aufruf SQL Statement");
  			
  			// Die Update-peration für PropertyValue
			Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
    	  	propertyValues = property.getPropertyValues();
    	  
            // Update von PropertyValue erfolgt
            for (PropertyValue pV : propertyValues){                 
            	PropertyValueMapper.propertyValueMapper().insert(pV);
            	System.out.println("Insert: " + pV);
            }  
            
            }
              catch (SQLException e) {
              e.printStackTrace();
            }         
      	}      
      
      
}
         
      
     
