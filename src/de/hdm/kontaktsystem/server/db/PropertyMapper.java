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

	  /*
	   * Abruf aller geteilter (participation) und eigener (ownership) Eigenschaften 
	   * der Kontakte eines Users im KontaktSystem
	   * 
	   */
	  
	  // TODO: Logik prüfen --> entweder id oder user übergeben?
	  
	  public Vector <Property> getAllPropertiesByUser(int user_id){
		  
		 // User und Business Objekte erzeugen
		  
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
		      ResultSet rs = stmt.executeQuery("SELECT BusinessObject.ID, BusinessObject.CreationDate, "
		      		+ "BusinessObject.ModificationDate, BusinessObject.Status, "
		      		+ "Property.ID, Property.description" + 
		      		"FROM BusinessObject" + 
		      		"INNER JOIN Property ON BusinessObject.ID = Property.ID" + 
		      		"WHERE BusinessObject.OwnerID =" + user.getGoogleID());
		      
		      while (rs.next()) {
		    	  // TODO: Klären ob Business Objekt noch extra generiert werden muss
		          Property property = new Property();
		          //Property 
		          property.setId(rs.getInt("id"));
		          property.setDescription(rs.getString("description"));
		          // Superklasse Business Object Attribute befüllen
		          property.setCreationDate(rs.getTimestamp("creationDate"));
		          property.setModifyDate(rs.getTimestamp("modificationDate"));
		          property.setShared_status(rs.getBoolean("status"));
		          
		          System.out.println(property.toString());

		          // Hinzufügen des neuen Objekts zum Ergebnisvektor
		          propertyResult.addElement(property);
		          
		      	}
		  } catch (SQLException e) {
			  e.printStackTrace();
		  } 
		  
		  return propertyResult;
	  }
	  
	public Vector <Property> getAllSharedPropertiesByUser(int user_id){
	  
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
		      ResultSet rs = stmt.executeQuery("SELECT BusinessObject.ID, BusinessObject.CreationDate, "
		      		+ "BusinessObject.ModificationDate, BusinessObject.Status, "
		      		+ "Property.ID, Property.description" + 
		      		"FROM BusinessObject" + 
		      		"INNER JOIN Property ON BusinessObject.ID =" + user.getGoogleID() +
		      		"INNER JOIN Property ON BusinessObject.ID = Property.ID" +
		      		"WHERE BusinessObject.Status =" + 1);
		      		// TODO: Klären Business Object Status = TinyInt in der DB
		      		// Bedeutet --> True = 1, False = 0
		      
		      while (rs.next()) {
		    	  
		    	  BusinessObject businessObject = new BusinessObject();		    	  
		          Property property = new Property();
		          
		          property.setId(rs.getInt("id"));
		          property.setDescription(rs.getString("description"));
		          
		          // Superklasse Business Object Attribute befüllen
		          property.setCreationDate(rs.getTimestamp("creationDate"));
		          property.setModifyDate(rs.getTimestamp("modificationDate"));
		          property.setShared_status(rs.getBoolean("status"));
		          
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
	
	  
	  public Vector<Property> getAllOwnedProperties(int user_id){
		  
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
		      ResultSet rs = stmt.executeQuery("SELECT BusinessObject.ID, BusinessObject.CreationDate, "
		      		+ "BusinessObject.ModificationDate, BusinessObject.Status, "
		      		+ "Property.ID, Property.description" + 
		      		"FROM BusinessObject" + 
		      		"INNER JOIN Property ON BusinessObject.ID =" + user.getGoogleID() +
		      		"INNER JOIN Property ON BusinessObject.ID = Property.ID" +
		      		"WHERE BusinessObject.Status =" + 0);
		      		// TODO: Klären Business Object Status = TinyInt in der DB
		      		// Bedeutet --> True = 1, False = 0
		      
		      while (rs.next()) {
		    	  
		    	  BusinessObject businessObject = new BusinessObject();		    	  
		          Property property = new Property();
		          
		          property.setId(rs.getInt("id"));
		          property.setDescription(rs.getString("description"));
		          
		          // Superklasse Business Object Attribute befüllen
		          property.setCreationDate(rs.getTimestamp("creationDate"));
		          property.setModifyDate(rs.getTimestamp("modificationDate"));
		          property.setShared_status(rs.getBoolean("status"));
		          
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
	  
	  public Property getPropertyByID(int id) {
		  
		  Property property = new Property();	  
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
			  // Leeres SQL Statement anlegen	
			  stmt = con.createStatement();
			  // Statement ausfüllen und als Query an die DB schicken
		      ResultSet rs = stmt.executeQuery("SELECT id FROM property "
		    		  + "WHERE id = " + id + " ORDER BY id");
		      
		      if (rs.next()) {
		          property.setId(rs.getInt("id"));
		          property.setDescription(rs.getString("description"));
		          property.setCreationDate(rs.getTimestamp("creationDate"));
		          property.setModifyDate(rs.getTimestamp("modificationDate"));
		          
		  }
		  } catch (SQLException e) {
			  e.printStackTrace();
		  } 
		  
		  return property;
	  }
	  
	  
	  public Vector <Property> getPropertyByDescription(String description) {
		  
		  Vector <Property> propertyResult = new Vector();	  
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
			  // Leeres SQL Statement anlegen	
			  stmt = con.createStatement();
			  // Statement ausfüllen und als Query an die DB schicken
		      ResultSet rs = stmt.executeQuery("SELECT id, description FROM property "
		    		  + "WHERE description = " + description + " ORDER BY id");
		      
		      while (rs.next()) {
		    	  
		    	  Property property = new Property();		    	  
		    	  BusinessObject businessObject = new BusinessObject();		    	  
		         		          
		          property.setId(rs.getInt("id"));
		          property.setDescription(rs.getString("description"));
		          
		          // Superklasse Business Object Attribute befüllen
		          property.setCreationDate(rs.getTimestamp("creationDate"));
		          property.setModifyDate(rs.getTimestamp("modificationDate"));
		          property.setShared_status(rs.getBoolean("status"));
		          
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
	  
		  
	  public Property updateProperty(Property property){
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();
		      stmt.executeUpdate("UPDATE property " + "SET description" + property.getDescription()
		          + "\" " + "SET modificationDate=\"" + property.getModifyDate()
		          + "\" " + "SET status=\"" + property.getShared_Status()		    
		          + "\" "+ "WHERE id=" + property.getId());
		      
		    } 
		  	  catch (SQLException e) {
		      e.printStackTrace();
		    } 
		    return property;
		  }
		  
	  
	  
	  public void deleteProperty(Property property) {
		  
		  PropertyValue propertyValue = null;
		  Vector <PropertyValue> propertyValueResult = new Vector();
		  
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
			  
			  stmt = con.createStatement();
			  
			  propertyValue = new PropertyValue();			  
			  propertyValueResult = property.getPropertyValues();
			  
			  for (PropertyValue pV : propertyValueResult){
				  PropertyValueMapper.propertyValueMapper().deletePropertyValue(pV.getId());
			  } 
			  
		      stmt.executeUpdate("DELETE FROM property " + "WHERE id=" + property.getId());
		    }
		    catch (SQLException e2) {
		      e2.printStackTrace();
		    }
		  
	  }
	  
	  
	  public void deletePropertyByID(int id) {
		  
		  Property property = null;
		  PropertyValue propertyValue = null;
		  Vector <PropertyValue> propertyValueResult = new Vector();
		  
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
			  
			  property = new Property();
			  // Leeres SQL Statement anlegen	
			  stmt = con.createStatement();
			  // Statement ausfüllen und als Query an die DB schicken
		      ResultSet rs = stmt.executeQuery("SELECT id FROM property "
		    		  + "WHERE id = " + id + " ORDER BY id");
		      
		      if (rs.next()) {
		          property.setId(rs.getInt("id"));
		          property.setDescription(rs.getString("description"));
		          property.setCreationDate(rs.getTimestamp("creationDate"));
		          property.setModifyDate(rs.getTimestamp("modificationDate"));
		          
		  }
		  } catch (SQLException e) {
			  e.printStackTrace();
		  } 		  
		  
		  try {
			  
			  stmt = con.createStatement();
			  
			  propertyValue = new PropertyValue();			  
			  propertyValueResult = property.getPropertyValues();
			  
			  for (PropertyValue pV : propertyValueResult){
				  PropertyValueMapper.propertyValueMapper().deletePropertyValue(pV.getId());
			  } 
			  
		      stmt.executeUpdate("DELETE FROM property " + "WHERE id=" + property.getId());
		    }
		    catch (SQLException e2) {
		      e2.printStackTrace();
		    }
		  
	  }
	  
	  // TODO: Klären ob diese Methode so sinnvoll ist
	  
	  public void deletePropertyOfContact(Contact contact) {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();
		      // TODO: PropertyValueMapper.propertyValueMapper().deletePropertyValueByProperty(property);
		      stmt.executeUpdate("DELETE FROM property " + "WHERE owner=" + contact.getId());

		    }
		    catch (SQLException e2) {
		      e2.printStackTrace();
		    }
		  
	  }
	  
	  public void deleteAllPropertiesFromUser(int user_id) {
		  
		 Vector <Property> propertyResult = new Vector(); 
		 propertyResult = PropertyMapper.propertyMapper().getAllPropertiesByUser(user_id);
		
		 for (Property pV : propertyResult){				 
			  PropertyValueMapper.propertyValueMapper().deletePropertyValue(pV.getId());
			  PropertyMapper.propertyMapper().deleteProperty(pV);
		  }		 
	  }
	  
	  
	  
	  public void insert(Property property) {
		  
		  Vector <PropertyValue> propertyValues = new Vector();
		  
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  //TODO: Logik überprüfen
		  BusinessObjectMapper.businessObjectMapper().insert(property);
		  	  
		  try {
			  
		  propertyValues = property.getPropertyValues();
		
		  stmt = con.createStatement();		  
		  ResultSet rs = stmt.executeQuery("SELECT id"
		          + "FROM businessObject"
				  + "WHERE businessObject.bo_id = property.id");
		  
		  if (rs.next()) {
			 
			  property.setId(rs.getInt("id"));
		      property.setDescription(rs.getString("description"));
		      property.setCreationDate(rs.getDate("creationDate"));
		      property.setShared_Status(rs.getBoolean("status"));
		      property.setPropertyValues(propertyValues);
		      
			  stmt = con.createStatement();

		        // die Einfügeoperation erfolgt
		        stmt.executeUpdate("INSERT INTO property (id, description, status, creationDate) "
		            + "VALUES (" + property.getId() + ",'" + property.getDescription() + "','"
		            + property.getShared_Status() + "," + property.getCreationDate() + "')");
		  	}
		  } catch(SQLException e) {
			  e.printStackTrace();
		  }
		  
	  }
		  
	  //TODO: Entscheiden ob innere Klasse oder nicht?
		  public Vector<PropertyValue> getPropertyValueOf(Contact contact) {
			    
			    return ContactMapper.contactMapper().findByOwner(contact);
			  }

		public PropertyValue findByProperty(Property prop) {
			// TODO Auto-generated method stub
			return null;
		}

		public PropertyValue findByStatus(Property prop) {
			// TODO Auto-generated method stub
			return null;
		}

		public PropertyValue getProperty(Property prop) {
			// TODO Auto-generated method stub
			return null;
		}
		
	  
}

