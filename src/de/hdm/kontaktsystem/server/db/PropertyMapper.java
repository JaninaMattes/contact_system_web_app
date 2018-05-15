package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

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
      
      
      
      public Vector <Property> findAll(){
    	  
    	  	// Vector zur Speicherung aller Properties
			Vector<Property> propertyResult = new Vector<Property>();
			
		  Connection con = DBConnection.connection();
    	  Statement stmt; 
    	  
  		try{			
  			// Leeres SQL Statement anlegen
  			stmt = con.createStatement();
  			// Statement ausfüllen und als Query an DB schicken
  			ResultSet rs = stmt.executeQuery(
  					"SELECT BusinessObject.bo_ID, BusinessObject.CreationDate, "
  					+ "BusinessObject.ModificationDate, BusinessObject.Status"
  					+ "Property.ID, Property.Description, "
  					+ "PropertyValue.ID, PropertyValue.Value"
  					+ "FROM BusinessObject,"  
  					+ "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID "
  					+ "INNER JOIN PropertyValue ON BusinessObject.bo_ID = PropertyValue.ID"
  					+ "WHERE BusinessObject.bo_ID = Property.ID "
  					+ "ORDER BY Property.Description");
  			
  			while(rs.next()){  				
  				// Vector um Eigenschaftsausprägungen zu speichern
  				Vector<PropertyValue> propertyValues = new Vector <PropertyValue>();
  				
  				Property property = new Property();
  				property.setBo_Id(rs.getInt("bo_id"));
  				property.setDescription(rs.getString("Description"));
  				property.setCreationDate(rs.getTimestamp("CreationDate"));
  				property.setModifyDate(rs.getTimestamp("ModificationDate"));
  				property.setShared_status(rs.getBoolean("Status"));
  				
  				// Aufrufen aller zu einer Eigenschaft (Property) gehörigen Eigenschaftsausprägungen 
  				propertyValues = PropertyValueMapper.propertyValueMapper().findByProperty(property);
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
     
    
      public Vector <Property> findByUserID(int user_id){
         
          Vector <Property> propertyResult = new Vector<Property>();
         
          Connection con = DBConnection.connection();
          Statement stmt = null;
         
          try {
              // Leeres SQL Statement anlegen  
              stmt = con.createStatement();
              // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery("SELECT BusinessObject.bo_ID, BusinessObject.CreationDate, "
                    + "BusinessObject.ModificationDate, BusinessObject.Status, "
                    + "Property.ID, Property.Description" 
                    + "PropertyValue.ID, PropertyValue.Value "
                    + "FROM BusinessObject" 
                    + "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID" 
                    + "INNER JOIN PropertyValue ON BusinessObject.bo_ID = PropertyValue.ID" 
                    + "WHERE BusinessObject.user_ID =" + user_id
                    + "ORDER BY Property.Description");
             
              while (rs.next()) {
            	  Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
                  Property property = new Property();
                  //Property
                  property.setBo_Id(rs.getInt("bo_ID"));
                  property.setDescription(rs.getString("Description"));
                  // Superklasse Business Object Attribute befüllen
                  property.setCreationDate(rs.getTimestamp("CreationDate"));
                  property.setModifyDate(rs.getTimestamp("ModificationDate"));
                  property.setShared_status(rs.getBoolean("Status"));
                  
                  // Aufrufen aller zu einer Eigenschaft (Property) gehörigen Eigenschaftsausprägungen 
                  propertyValues = PropertyValueMapper.propertyValueMapper().findByProperty(property);
                  // Setzen des Eigenschaftsausprägungs Vector
                  property.setPropertyValues(propertyValues); 
                  
                  // Hinzufügen des neuen Objekts zum Ergebnisvektor
                  propertyResult.addElement(property);
                 
                }
          } catch (SQLException e) {
              e.printStackTrace();
          }
         
          return propertyResult;
      }
      
      /**
       * Abruf aller geteilter (participation) und eigener (ownership) Eigenschaften
       * der Kontakte eines Users im KontaktSystem.
       * @param user als Users Objekt
       * @return einen Vector mit Property Objekten, welche dem übergebenen User Objekt
       * zugeordnet werden können. Dies entspricht null bei einem nicht vorhandenem DB-Tupel.
       *
       */
      
      
      public Vector <Property> findByUser(User user){
    	 return this.findByUserID(user.getGoogleID());
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
       
     
    public Vector <Property> findByStatus(int user_id, boolean shared_status) {
       
        Vector <Property> propertyResult = null;
                 
        Connection con = DBConnection.connection();
        Statement stmt = null;
         
         try {
              // Leeres SQL Statement anlegen  
              stmt = con.createStatement();
              // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery("SELECT BusinessObject.bo_ID, BusinessObject.CreationDate, "
                    + "BusinessObject.ModificationDate, BusinessObject.Status, "
                    + "Property.ID, Property.Description"
                    + "PropertyValue.ID, PropertyValue.Value"
                    + "FROM BusinessObject"
                    + "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID"
                    + "INNER JOIN PropertyValue ON BusinessObject.bo_ID = PropertyValue.ID"
                    + "WHERE BusinessObject.Status =" + shared_status 
                    + "AND BusinessObject.user_ID =" + user_id
                    + "ORDER BY Property.Description");
                         
         while (rs.next()) {
              
        	  Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
              Property property = new Property();
             
              property.setBo_Id(rs.getInt("ID"));
              property.setDescription(rs.getString("Description"));             
              // Superklasse Business Object Attribute befüllen
              property.setCreationDate(rs.getTimestamp("CreationDate"));
              property.setModifyDate(rs.getTimestamp("ModificationDate"));
              property.setShared_status(rs.getBoolean("Status"));
                           
              // Aufrufen aller zu einer Eigenschaft (Property) gehörigen Eigenschaftsausprägungen 
              propertyValues = PropertyValueMapper.propertyValueMapper().findByProperty(property);
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
     * Je nach übergebenem gesuchten Status findet der
     * Abruf aller geteilter (participation) oder nicht geteilter (owner) Eigenschaften
     * der Kontakte <em>aller</em> User im KontaktSystem statt.
     * Diese Methode soll es ermöglichen alle Eigenschaften, welche einerm User
     * zugewiesen werden können und mit ihm geteilt wurden abzurufen.
     *
     * @param shared_status des gesuchten Objectes
     * @return einen Vector mit Property Objekten, welche dem übergebenen boolean Parameter (shared_status)
     * zugeordnet werden können. Dies entspricht null bei einem nicht vorhandenem DB-Tupel.
     *
     */
       
     
    public Vector <Property> findByStatus(boolean shared_status) {
       
        Vector <Property> propertyResult = null;
                 
        Connection con = DBConnection.connection();
        Statement stmt = null;
         
         try {
              // Leeres SQL Statement anlegen  
              stmt = con.createStatement();
              // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery("SELECT BusinessObject.bo_ID, BusinessObject.CreationDate, "
                    + "BusinessObject.ModificationDate, BusinessObject.Status, "
                    + "Property.ID, Property.Description"
                    + "PropertyValue.ID, PropertyValue.Value"
                    + "FROM BusinessObject"
                    + "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID"
                    + "INNER JOIN PropertyValue ON BusinessObject.bo_ID = PropertyValue.ID"
                    + "WHERE BusinessObject.Status =" + shared_status 
                    + "ORDER BY Property.Description");
                         
         while (rs.next()) {
              
        	  Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
              Property property = new Property();
             
              property.setBo_Id(rs.getInt("ID"));
              property.setDescription(rs.getString("Description"));             
              // Superklasse Business Object Attribute befüllen
              property.setCreationDate(rs.getTimestamp("CreationDate"));
              property.setModifyDate(rs.getTimestamp("ModificationDate"));
              property.setShared_status(rs.getBoolean("Status"));
                           
              // Aufrufen aller zu einer Eigenschaft (Property) gehörigen Eigenschaftsausprägungen 
              propertyValues = PropertyValueMapper.propertyValueMapper().findByProperty(property);
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
          Statement stmt = null;
         
          try {
              // Leeres SQL Statement anlegen  
              stmt = con.createStatement();
              // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery("SELECT BusinessObject.bo_ID, BusinessObject.CreationDate, "
                    + "BusinessObject.ModificationDate, BusinessObject.Status, "
                    + "Property.ID, Property.Description"
                    + "PropertyValue.ID, PropertyValue.Value"
                    + "FROM BusinessObject"
                    + "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID"
                    + "INNER JOIN PropertyValue ON BusinessObject.bo_ID = PropertyValue.ID"
                    + "WHERE BusinessObject.bo_ID =" + property_id 
                    );
             
              if (rs.next()) {

                  Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
                  
                  property.setBo_Id(rs.getInt("id"));
                  property.setDescription(rs.getString("description"));
                  property.setCreationDate(rs.getTimestamp("creationDate"));
                  property.setModifyDate(rs.getTimestamp("modificationDate"));
                  property.setShared_status(rs.getBoolean("Status"));
                  
                  // Aufrufen aller zu einer Eigenschaft (Property) gehörigen Eigenschaftsausprägungen 
                  propertyValues = PropertyValueMapper.propertyValueMapper().findByProperty(property);
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
         
          Connection con = DBConnection.connection();
          Statement stmt = null;
         
          try {
              // Leeres SQL Statement anlegen  
              stmt = con.createStatement();
              // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery("SELECT BusinessObject.bo_ID, BusinessObject.CreationDate, "
                    + "BusinessObject.ModificationDate, BusinessObject.Status, "
                    + "Property.ID, Property.Description"
                    + "PropertyValue.ID, PropertyValue.Value"
                    + "FROM BusinessObject"
                    + "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID"
                    + "INNER JOIN PropertyValue ON BusinessObject.bo_ID = PropertyValue.ID"
                    + "WHERE Property.Description =" + description 
                    + "ORDER BY Property.Description");
             
              while (rs.next()) {
                 
                  Property property = new Property(); 
                  Vector <PropertyValue> propertyValues = new Vector <PropertyValue>();
                                 
                  property.setBo_Id(rs.getInt("ID"));
                  property.setDescription(rs.getString("Description"));                 
                  // Superklasse Business Object Attribute befüllen
                  property.setCreationDate(rs.getTimestamp("CreationDate"));
                  property.setModifyDate(rs.getTimestamp("ModificationDate"));
                  property.setShared_status(rs.getBoolean("Status"));
                 
                  // Aufrufen aller zu einer Eigenschaft (Property) gehörigen Eigenschaftsausprägungen 
                  propertyValues = PropertyValueMapper.propertyValueMapper().findByProperty(property);
                  // Setzen des Eigenschaftsausprägungs Vector
                  property.setPropertyValues(propertyValues);
                 
                }
              
              return propertyResult;
             
          } catch (SQLException e) {
              e.printStackTrace();
          }
         
          return null;
         
      }
   
      /**
       * Wiederholtes Schreiben eines Objekts in die Datenbank.
       *
       * @param property das Eigenschaft (Property) Objekt, das in die DB geschrieben werden soll
       * @return das als Parameter übergebene Objekt
       */    
     
         
      public Property updateProperty(Property property){
          Connection con = DBConnection.connection();
          Statement stmt = null;
         
          try {
        	  
        	  // Die zugehörige BO Tabelle wird ebenso mit Einträgen befüllt
              BusinessObjectMapper.businessObjectMapper().update(property);
        	  
        	  Vector <PropertyValue> propertyValueResult = new Vector <PropertyValue>();
              stmt = con.createStatement();
              stmt.executeUpdate(
            	 "UPDATE property " + "SET description" + property.getDescription()       
                  + "\" "+ "WHERE id=" + property.getBo_Id());            
              
             propertyValueResult = property.getPropertyValues();
             // Die zugehörige PropertyValue Tabelle wird mit Einträgen befüllt
              for(PropertyValue pv: propertyValueResult) {
              PropertyValueMapper.propertyValueMapper().update(pv);
              }
            }
              catch (SQLException e) {
              e.printStackTrace();
            }
         
          /**
           *  Um eine Analogie zu insert Methode zu wahren wird das
           *  <code>Property</code> Objekt zurück gegeben
           * 
           */  
            return property;
          }
         
     
      /**
       * Löschen der Daten eines <code>Property</code>-Objekts aus der Datenbank.     
       * @param property, welches das aus der DB zu löschende "Objekt" ist
       */
     
      public void delete(Property property) {
       
          Vector <PropertyValue> propertyValueResult = new Vector<PropertyValue>();
         
          Connection con = DBConnection.connection();
          Statement stmt = null;
         
          try {
             
              stmt = con.createStatement();
                     
              propertyValueResult = property.getPropertyValues();
             
              // zuerst müssen alle PropertyValue Einträge von Property aus der DB gelöscht werden
              for (PropertyValue pV : propertyValueResult){
                  PropertyValueMapper.propertyValueMapper().delete(pV);
              }              
              stmt.executeUpdate("DELETE FROM property " + "WHERE id=" + property.getBo_Id());
              // Löschen des Eintrags in der BO Tabelle
              BusinessObjectMapper.businessObjectMapper().deleteBusinessObject(property);      
            }
            catch (SQLException e2) {
              e2.printStackTrace();
            }          
          	   
      }
     
      /**
       * Löschen der Daten eines <code>Property</code>-Objekts aus der Datenbank.     
       * @param id ist der Primärschlüssel, des aus der DB zu löschenden "Objektes"
       */
     
      public void deleteById(int property_id) {
         
         Property property = new Property();
         property = this.findByID(property_id);
         
         Connection con = DBConnection.connection();
         Statement stmt = null;
         
          try {
             
              stmt = con.createStatement();
             
              // Abruf aller PropertyValues, welche zu einem Property Objekt gehören können
              Vector <PropertyValue>propertyValue = new Vector <PropertyValue>();           
              propertyValue = property.getPropertyValues();
              // Löschen aller PropertyValues
              if(propertyValue != null) {
            	  for (PropertyValue pV : propertyValue){
            		  PropertyValueMapper.propertyValueMapper().delete(pV);
              		}
              }          
              stmt.executeUpdate("DELETE FROM property " + "WHERE id=" + property.getBo_Id());
              // Löschen des Eintrags in der BO Tabelle
              BusinessObjectMapper.businessObjectMapper().deleteBusinessObject(property);      
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
     
      public void deleteByUserId(int user_id) {
         
         Vector <Property> propertyResult = new Vector <Property>();
         propertyResult = PropertyMapper.propertyMapper().findByUserID(user_id);
       
         for (Property p : propertyResult){                 
              PropertyValueMapper.propertyValueMapper().deletePropertyValue(p.getBo_Id());
              PropertyMapper.propertyMapper().delete(p);
              //Löschen des Eintrags in der BO Tabelle
              BusinessObjectMapper.businessObjectMapper().deleteBusinessObject(p);  
          }   
          
      }
      
      /**
       * Löschen sämtlicher Eigenschaften <code>Property</code> Objekte eines Nutzers.
       * @param user entspricht dem Nutzer dessen Eigenschaften gelöscht werden sollen.
       */
           
      public void deleteByUser(User user) {
    	  this.deleteByUserId(user.getGoogleID());
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
          Statement stmt = null;
          
          BusinessObjectMapper.businessObjectMapper().insert(property);
             
          try {
                stmt = con.createStatement();
 
                // die Einfügeoperation erfolgt
                stmt.executeUpdate("INSERT INTO property (id, description) "
                    + "VALUES (" + property.getBo_Id() + ",'" + property.getDescription() + "')");
                
                // die zugehörigen Eigenschaftsausprägungen werden ebenfalls in die DB eingefügt
                Vector <PropertyValue> propertyValueResult = new Vector <PropertyValue>();
                propertyValueResult = property.getPropertyValues();
                
                for (PropertyValue pv : propertyValueResult){                 
                     PropertyValueMapper.propertyValueMapper().insert(pv);
                 }      
                                
          } 
          
          catch(SQLException e) {
              e.printStackTrace();
          }      
      }
       
       
     
}