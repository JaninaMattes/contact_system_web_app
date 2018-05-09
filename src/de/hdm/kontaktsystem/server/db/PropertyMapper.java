package de.hdm.kontaktsystem.server.db;


import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;

/**
 * Die Mapper-Klasse <code>PropertyMapper</code> bildet <code>Property</code>-Objekte 
 * auf eine relationale Datenbank ab. Dazu werden Methoden zum Erzeugen, Suchen, Ändern und 
 * Löschen von Objekten zur Verfügung gestellt. Es können sowohl Objekte in Datenbank-Strukturen, 
 * als auch Datenbank-Strukturen in Objekte überführt werden.
 * 
 * @author Janina Mattes
 *
 */

// TODO: Logik der Methoden absprechen und überprüfen

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

	  
	  public Vector<Property> getAllProperties(){
		  
		  Vector <Property> propResult = new Vector<Property>();
		  
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
			  // Leeres SQL Statement anlegen	
			  stmt = con.createStatement();
			  // Statement ausfüllen und als Query an die DB schicken
		      ResultSet rs = stmt.executeQuery("SELECT* FROM property "
		          + " ORDER BY id");
		      
		      while (rs.next()) {
		          Property property = new Property();
		          property.setId(rs.getInt("id"));
		          property.setDescription(rs.getString("description"));
		          property.setCreationDate(rs.getTimestamp("creationDate"));
		          property.setModifyDate(rs.getTimestamp("modificationDate"));
		          		          

		          // Hinzufügen des neuen Objekts zum Ergebnisvektor
		          propResult.addElement(property);
		          
		      	}
		  } catch (SQLException e) {
			  e.printStackTrace();
		  } 
		  
		  return propResult;
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
	  
	  
	  public Property getPropertyByDescription(String description) {
		  
		  Property property = new Property();	  
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
			  // Leeres SQL Statement anlegen	
			  stmt = con.createStatement();
			  // Statement ausfüllen und als Query an die DB schicken
		      ResultSet rs = stmt.executeQuery("SELECT id, description FROM property "
		    		  + "WHERE description = " + description + " ORDER BY id");
		      
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
	  
	  
	  public Property updateProperty(Property property){
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();

		      stmt.executeUpdate("UPDATE property " + "SET description=\"" + property.getDescription()
		          + "\" " + "SET modificationDate=\"" + property.getModifyDate()
		          + "\" "+ "WHERE id=" + property.getId());

		    } 
		  	  catch (SQLException e) {
		      e.printStackTrace();
		    } 
		    return property;
		  }
		  
	  
	  
	  public void deleteProperty(Property property) {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();
		      // TODO: PropertyValueMapper.propertyValueMapper().deletePropertyValueByProperty(property);
		      stmt.executeUpdate("DELETE FROM property " + "WHERE id=" + property.getId());

		    }
		    catch (SQLException e2) {
		      e2.printStackTrace();
		    }
		  
	  }
	  
	  public void deletePropertyByID(int id) {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();
		   // TODO: PropertyValueMapper.propertyValueMapper().deletePropertyValueByPropertyID(id);
		      stmt.executeUpdate("DELETE FROM property " + "WHERE id=" + id);

		    }
		    catch (SQLException e2) {
		      e2.printStackTrace();
		    }
		  
	  }
	  
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
	  
	  public void deleteAllProperties() {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;		  

		  try {
		      stmt = con.createStatement();
		      // TODO: PropertyValueMapper.propertyValueMapper().deletePropertyValueByProperty(property);
		      // stmt.executeUpdate("DELETE FROM property " + "WHERE owner=" + contact.getId());

		    }
		    catch (SQLException e2) {
		      e2.printStackTrace();
		    }
		  
	  }
	  
	  public void insert(Property property) {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		  stmt = con.createStatement();		  
		  ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid "
		          + "FROM property ");
		  if (rs.next()) {
			 
			  //TODO: Ersetzung durch Generated Keys
			  property.setId(rs.getInt("maxid") + 1);

		        stmt = con.createStatement();

		        // die Einfügeoperation erfolgt
		        stmt.executeUpdate("INSERT INTO property (id, description, creationDate) "
		            + "VALUES (" + property.getId() + ",'" + property.getDescription() + "','"
		            + property.getCreationDate() + "')");
		  	}
		  } catch(SQLException e) {
			  e.printStackTrace();
		  }
		  
	  }
		  
	  //TODO: Entscheiden ob innere Klasse oder nicht?
		  public Vector<PropertyValue> getPropertyValueOf(Contact contact) {
			    
			    return ContactMapper.contactMapper().findByOwner(contact);
			  }
	  
}
