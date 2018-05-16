package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
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
      
      
      
      public Vector <Property> getAllProperties(){
    	  
    	  	// Vector zur Speicherung aller Properties
			Vector<Property> propertyResult = new Vector<Property>();
			Vector<PropertyValue> propertyValues = new Vector <PropertyValue>();
			
		  Connection con = DBConnection.connection();
    	  Statement stmt; 
    	  
  		try{			
  			
  			stmt = con.createStatement();
  			ResultSet rs = stmt.executeQuery(
  					"SELECT BusinessObject.bo_ID, BusinessObject.CreationDate, BusinessObject.ModificationDate,"
  					+ "BusinessObject.Status, Property.ID, Property.Description, PropertyValue.ID, PropertyValue."
  					+ "FROM BusinessObject "  
  					+ "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID "
  					+ "INNER JOIN PropertyValue ON BusinessObject.bo_ID = PropertyValue.ID"
  					+ "WHERE BusinessObject.bo_ID = Property.ID ");
  			
  			while(rs.next()){
  				
  				Property property = new Property();
  				property.setBo_Id(rs.getInt("bo_id"));
  				property.setDescription(rs.getString("Description")); 				
  				property.setPropertyValues(propertyValues);
  				
  				
  			}  			
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
     
    
      public Vector <Property> getAllPropertiesByUser(int user_id){
         
          Vector <Property> propertyResult = new Vector<Property>();
         
          Connection con = DBConnection.connection();
          Statement stmt = null;
         
          try {
              // Leeres SQL Statement anlegen  
              stmt = con.createStatement();
              // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery("SELECT BusinessObject.bo_ID, BusinessObject.CreationDate, "
                    + "BusinessObject.ModificationDate, BusinessObject.Status, "
                    + "Property.ID, Property.Description" +
                    "FROM BusinessObject" +
                    "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID" +
                    "WHERE BusinessObject.user_ID =" + user_id);
             
              while (rs.next()) {
                  // TODO: Klären ob Business Objekt noch extra generiert werden muss
                  Property property = new Property();
                  //Property
                  property.setBo_Id(rs.getInt("bo_ID"));
                  property.setDescription(rs.getString("Description"));
                  // Superklasse Business Object Attribute befüllen
                  property.setCreationDate(rs.getTimestamp("CreationDate"));
                  property.setModifyDate(rs.getTimestamp("ModificationDate"));
                  property.setShared_status(rs.getBoolean("Status"));
                 
                  System.out.println(property.toString());
 
                  // Hinzufügen des neuen Objekts zum Ergebnisvektor
                  propertyResult.addElement(property);
                 
                }
          } catch (SQLException e) {
              e.printStackTrace();
          }
         
          return propertyResult;
      }
      
      
      public Vector <Property> getAllPropertiesByUser(User user){
    	 return this.getAllOwnedProperties(user.getGoogleID());
      }
      
      
           
      /**
       * Abruf aller geteilter (participation) Eigenschaften
       * der Kontakte eines Users im KontaktSystem.
       * Diese Methode soll es ermöglichen alle Eigenschaften, welche einerm User
       * zugewiesen werden können und mit ihm geteilt wurden abzurufen.
       * Da es sich hierbei um geteilte Eigenschaften <code>Property</code> handelt
       * müssen hier keine zugehörigen Eigenschaftsausprägungen PropertyValues abgerufen werden. 
       *
       * @param id Primärschlüsselattribut des Users
       * @return einen Vector mit Property Objekten, welche dem übergebenen User Primärschlüssel,
       * zugeordnet werden können. Dies entspricht null bei einem nicht vorhandenem DB-Tupel.
       *
       */
     
    public Vector <Property> getAllSharedPropertiesByUser(int user_id){
                 
          Vector <Property> propertyResult = new Vector<Property>();
         
          Connection con = DBConnection.connection();
          Statement stmt = null;
         
          try {
              // Leeres SQL Statement anlegen  
              stmt = con.createStatement();
              // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery("SELECT BusinessObject.bo_ID, BusinessObject.CreationDate, "
                    + "BusinessObject.ModificationDate, BusinessObject.Status, "
                    + "Property.ID, Property.Description" +
                    "FROM BusinessObject" +
                    "INNER JOIN Property ON BusinessObject.User_ID =" + user_id +
                    "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID" +
                    "WHERE BusinessObject.Status =" + 1);
             
             
              while (rs.next()) {
                           
                  Property property = new Property();
                 
                  property.setBo_Id(rs.getInt("ID"));
                  property.setDescription(rs.getString("Description"));
                 
                  // Superklasse Business Object Attribute befüllen
                  property.setCreationDate(rs.getTimestamp("CreationDate"));
                  property.setModifyDate(rs.getTimestamp("ModificationDate"));
                  property.setShared_status(rs.getBoolean("Status"));
                  
                  // Hinzufügen des neuen Objekts zum Ergebnisvektor
                  propertyResult.addElement(property);
                 
                }
          } catch (SQLException e) {
              e.printStackTrace();
          }
         
          return propertyResult;     
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
       
     
    public Vector <Property> findPropertyByStatus(boolean shared_status) {
       
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
                    + "PropertyValue.ID, PropertyValue.value"
                    + "FROM BusinessObject"
                    + "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID"
                    + "INNER JOIN USER ON BusinessObject.Participation_ID = User.ID"
                    + "WHERE BusinessObject.Status =" + shared_status );
                         
         while (rs.next()) {
             
              BusinessObject businessObject = new BusinessObject();              
              Property property = new Property();
             
              property.setBo_Id(rs.getInt("ID"));
              property.setDescription(rs.getString("Description"));
             
              // Superklasse Business Object Attribute befüllen
              property.setCreationDate(rs.getTimestamp("CreationDate"));
              property.setModifyDate(rs.getTimestamp("ModificationDate"));
              property.setShared_status(rs.getBoolean("Status"));
             
              System.out.println(property.toString());
              System.out.println(businessObject.toString());
 
              // Hinzufügen des neuen Objekts zum Ergebnisvektor
              propertyResult.addElement(property);
             
            }
      } catch (SQLException e) {
          e.printStackTrace();
      }
     
      return propertyResult;
     
    }
   
 
     /**
       * Abruf aller NICHT geteilter (ownership) Eigenschaften
       * welche den Kontakten eines Users im KontaktSystem zugeordnet sind.
       * Diese Methode soll es so ermöglichen alle Eigenschaften, welche einerm User
       * zugewiesen werden können und von diesem als Urheber (owner) erstellt wurden, abzurufen.
       *
       * @param id Primärschlüsselattribut des Users
       * @return einen Vector mit Property Objekten, welche dem übergebenen User Primärschlüssel,
       * zugeordnet werden können. Dies entspricht null bei einem nicht vorhandenem DB-Tupel.
       *
       */  
     
      public Vector <Property> getAllOwnedProperties(int user_id){
         
          // User Objekte erzeugen
          User user = null;
         
          if (user_id != 0) {
              user = new User();
              user = UserMapper.userMapper().getUserById(user_id);
          }          
         
          Vector <Property> propertyResult = new Vector<Property>();
         
          Connection con = DBConnection.connection();
          Statement stmt = null;
         
          try {
              // Leeres SQL Statement anlegen  
              stmt = con.createStatement();
              // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery("SELECT BusinessObject.bo_ID, BusinessObject.CreationDate, "
                    + "BusinessObject.ModificationDate, BusinessObject.Status, "
                    + "Property.ID, Property.Description" +
                    "FROM BusinessObject" +
                    "INNER JOIN Property ON BusinessObject.Owner_ID =" + user.getGoogleID() +
                    "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID" +
                    "WHERE BusinessObject.Status =" + 0);
             
              while (rs.next()) {
                 
                  //TODO: Klären --> Muss hier auch ein BO erstellt werden?
                  //BusinessObject businessObject = new BusinessObject();  
                 
                  Property property = new Property();
                 
                  property.setBo_Id(rs.getInt("ID"));
                  property.setDescription(rs.getString("Description"));
                 
                  // Superklasse Business Object Attribute befüllen
                  property.setCreationDate(rs.getTimestamp("CreationDate"));
                  property.setModifyDate(rs.getTimestamp("ModificationDate"));
                  property.setShared_status(rs.getBoolean("Status"));
                 
                  System.out.println(property.toString());
 
                  // Hinzufügen des neuen Objekts zum Ergebnisvektor
                  propertyResult.addElement(property);
                 
                }
             
          } catch (SQLException e) {
              e.printStackTrace();
          }
         
          return propertyResult;         
         
      }
      
     
      /**
       * Abruf aller PropertyValue Objekte, welche zu einem Property Objekt zugeordnet 
       * werden können. 
       *
       * @param Property Objekt, dessen PropertyValue Einträge ausgelesen werden sollen
       * @return einen Vector mit PropertyValue Objekten, welche dem übergebenen Property Objekt,
       * zugeordnet werden können. Dies entspricht null bei einem nicht vorhandenem DB-Tupel.
       *
       */  
     
      public Vector <PropertyValue> findPropertyValuesByProperty(Property property){
         
          Vector <PropertyValue> propertyValueResult = new Vector<PropertyValue>();
         
          Connection con = DBConnection.connection();
          Statement stmt = null;
         
          try {
              // Leeres SQL Statement anlegen  
              stmt = con.createStatement();
              // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery("SELECT BusinessObject.bo_ID, BusinessObject.CreationDate, "
                    + "BusinessObject.ModificationDate, BusinessObject.Status, "
                    + "Property.ID, Property.Description,"
                    + "PropertyValue.ID, PropertyValue.Value,"
                    + "FROM BusinessObject"
                    + "INNER JOIN Property ON BusinessObject.bo_ID = Property.ID"
                    + "INNER JOIN Property ON BusinessObject.bo_ID = PropertyValue.ID"
                    + "WHERE BusinessObject.user_ID =" + property.getUserId());
             
                    // TODO: Klären wie wird hier die ID - Google Token (g_token) aus der DB in die
                    // Business Object Tabelle übergeben? Diese sollte gleich sein
             
              while (rs.next()) {
                                  
                  PropertyValue propertyValue = new PropertyValue();
                 
                  propertyValue.setBo_Id(rs.getInt("ID"));
                  propertyValue.setValue(rs.getString("Value"));
                 
                  // Superklasse Business Object Attribute befüllen
                  property.setCreationDate(rs.getTimestamp("CreationDate"));
                  property.setModifyDate(rs.getTimestamp("ModificationDate"));
                  property.setShared_status(rs.getBoolean("Status"));
                 
                  System.out.println(property.toString());
 
                  // Hinzufügen des neuen Objekts zum Ergebnisvektor
                  propertyValueResult.addElement(propertyValue);                 
                }
             
          } catch (SQLException e) {
              e.printStackTrace();
          }
         
          return propertyValueResult;    
      }
      
      /**
       * Auslesen eines Vectors mit PropertyValues anhand des Primärschlüssels 
       * eines Property Objektes in der DB
       * 
       */
      
      public Vector <PropertyValue> findPropertyValuesByPropertyID(int property_id){
    	  
    	  Property property = new Property();
    	  property = this.findPropertyByID(property_id);
    	  
    	  return this.findPropertyValuesByProperty(property);
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
     
     
      public Property findPropertyByID(int id) {
         
          Property property = new Property();  
          Vector <PropertyValue> propertyValue = new Vector <PropertyValue>();
         
          Connection con = DBConnection.connection();
          Statement stmt = null;
         
          try {
              // Leeres SQL Statement anlegen  
              stmt = con.createStatement();
              // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery("SELECT id, description FROM property "
                      + "WHERE id = " + id);
             
              if (rs.next()) {
                  property.setBo_Id(rs.getInt("id"));
                  property.setDescription(rs.getString("description"));
                  property.setCreationDate(rs.getTimestamp("creationDate"));
                  property.setModifyDate(rs.getTimestamp("modificationDate"));
                  
                  //Abrufen der Eigenschaftsausprägungen
                  propertyValue = this.findPropertyValuesByProperty(property);
                  property.setPropertyValues(propertyValue);
          }
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return property;
      }
     
      /**
       * Suchen eines Eigenschaft Objekts innerhalb der DB anhand derer Primärschlüssel ID.
       * Da diese eindeutig ist, wird genau ein Eigenschafts Objekt zur�ckgegeben.
       *
       * @param user ist das User Objekt des Nutzers.
       * @return Property Objekt, das dem übergebenen Schlüssel entspricht,
       * dies wird null, wenn kein Datenbank Tupel vorhanden ist.
       */
      
     
      public Property findPropertyByUser(User user) {
          //TODO: Überprüfen ob die Google ID hier der richtige Parameter ist
        return this.findPropertyByID(user.getGoogleID());        
      }
     
      
      /**
       * Suchen eines oder mehrerer Eigenschaft Objekte innerhalb der DB anhand derer Beschreibung.
       * Da dies nicht unbedingt eindeutig ist, wird nicht immer genau ein Eigenschafts Objekt zurückgegeben.
       *
       * @param description ist ein Attribut in der Property Tabelle
       * @return Property Objekt, das dem übergebenen Schlüssel entspricht,
       * dies wird null, wenn kein Datenbank Tupel vorhanden ist.
       */
     
     
      public Vector <Property> findPropertyByDescription(String description) {
         
          Vector <Property> propertyResult = new Vector <Property>();    
          Vector <PropertyValue> propertyValueResult = new Vector <PropertyValue>();
         
          Connection con = DBConnection.connection();
          Statement stmt = null;
         
          try {
              // Leeres SQL Statement anlegen  
              stmt = con.createStatement();
              // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery("SELECT ID, Description FROM Property "
                      + "WHERE Description = " + description + " ORDER BY ID");
             
              while (rs.next()) {
                 
                  Property property = new Property();                        
                                 
                  property.setBo_Id(rs.getInt("ID"));
                  property.setDescription(rs.getString("Description"));
                 
                  // Superklasse Business Object Attribute befüllen
                  property.setCreationDate(rs.getTimestamp("CreationDate"));
                  property.setModifyDate(rs.getTimestamp("ModificationDate"));
                  property.setShared_status(rs.getBoolean("Status"));
                 
                  // Auslesen der Eigenschaftsausprägungen einer Eigenschaft
                  propertyValueResult = this.findPropertyValuesByProperty(property);
                  property.setPropertyValues(propertyValueResult);
                 
                  System.out.println(property.toString());
 
                  // Hinzufügen des neuen Objekts zum Ergebnisvektor
                  propertyResult.addElement(property);
                 
                }
             
          } catch (SQLException e) {
              e.printStackTrace();
          }
         
          return propertyResult;
         
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
              stmt = con.createStatement();
              stmt.executeUpdate(
            	 "UPDATE property " + "SET description" + property.getDescription()       
                  + "\" "+ "WHERE id=" + property.getBo_Id());
             
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
       * Löschen der Daten eines <code>Property</code>-Objekts aus der Datenbank.     *
       * @param property, welches das aus der DB zu löschende "Objekt" ist
       */
     
      public void deleteProperty(Property property) {
       
          Vector <PropertyValue> propertyValueResult = new Vector<PropertyValue>();
         
          Connection con = DBConnection.connection();
          Statement stmt = null;
         
          try {
             
              stmt = con.createStatement();
                     
              propertyValueResult = property.getPropertyValues();
             
              for (PropertyValue pV : propertyValueResult){
                  PropertyValueMapper.propertyValueMapper().delete(pV);
              }              
              stmt.executeUpdate("DELETE FROM property " + "WHERE id=" + property.getBo_Id());
            }
            catch (SQLException e2) {
              e2.printStackTrace();
            }          
          	BusinessObjectMapper.businessObjectMapper().deleteBusinessObject(property);         
      }
     
      /**
       * Löschen der Daten eines <code>Property</code>-Objekts aus der Datenbank.     
       * @param id ist der Primärschlüssel, des aus der DB zu löschenden "Objektes"
       */
     
      public void deletePropertyByPropertyID(int property_id) {
         
          Property property = null;
          PropertyValue propertyValue = null;
          Vector <PropertyValue> propertyValueResult = new Vector<PropertyValue>();
         
          Connection con = DBConnection.connection();
          Statement stmt = null;
         
          try {
             
              property = new Property();
              // Leeres SQL Statement anlegen  
              stmt = con.createStatement();
              // Statement ausfüllen und als Query an die DB schicken
              ResultSet rs = stmt.executeQuery("SELECT id FROM property "
                      + "WHERE id = " + property_id + " ORDER BY id");
             
              if (rs.next()) {
                  property.setBo_Id(rs.getInt("ID"));
                  property.setDescription(rs.getString("Description"));
                  property.setCreationDate(rs.getTimestamp("CreationDate"));
                  property.setModifyDate(rs.getTimestamp("mMdificationDate"));
                  property.setShared_status(rs.getBoolean("Status"));
                 
          }
          } catch (SQLException e) {
              e.printStackTrace();
          }          
         
          try {
             
              stmt = con.createStatement();
             
              // Abruf aller PropertyValues, welche zu einem Property Objekt gehören können
              propertyValue = new PropertyValue();           
              propertyValueResult = property.getPropertyValues();
             
              if(propertyValueResult != null) {
              for (PropertyValue pV : propertyValueResult){
                  PropertyValueMapper.propertyValueMapper().delete(pV);
              }
              }          
              stmt.executeUpdate("DELETE FROM property " + "WHERE id=" + property.getBo_Id());
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
     
      public void deleteAllPropertiesByUser(int user_id) {
         
         Vector <Property> propertyResult = new Vector <Property>();
         propertyResult = PropertyMapper.propertyMapper().getAllPropertiesByUser(user_id);
       
         for (Property pV : propertyResult){                 
              PropertyValueMapper.propertyValueMapper().deletePropertyValue(pV.getBo_Id());
              PropertyMapper.propertyMapper().deleteProperty(pV);
          }      
      }
      
      /**
       * Löschen sämtlicher Eigenschaften <code>Property</code> Objekte eines Nutzers.
       * @param user entspricht dem Nutzer dessen Eigenschaften gelöscht werden sollen.
       */
           
      public void deleteAllPropertiesByUserID(User user) {
    	  this.deleteAllPropertiesByUser(user.getGoogleID());
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
                
          } catch(SQLException e) {
              e.printStackTrace();
          }      
      }


		
	}
       
       
     
}