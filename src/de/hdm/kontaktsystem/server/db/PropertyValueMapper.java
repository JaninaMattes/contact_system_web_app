package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;

public class PropertyValueMapper {
	
	  /**
	   * Singleton Pattern
	   */
	
	  private static PropertyValueMapper propertyValueMapper = null;

	  protected PropertyValueMapper() {
		  
	  }

	  public static PropertyValueMapper propertyValueMapper() {
			    if (propertyValueMapper == null) {
			      propertyValueMapper = new PropertyValueMapper();
			    }
		
			    return propertyValueMapper;
	  }
	  
	  /*
	   * Einfügen einer neu angelegten Eigenschaftsausprägung in die DB
	   */
	  
	  public void insert(PropertyValue propertyValue) {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		  stmt = con.createStatement();		  
		  ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid "
		          + "FROM propertyvalue ");
		  if (rs.next()) {
			 
			  //TODO: Ersetzung durch Generated Keys
			  propertyValue.setId(rs.getInt("maxid") + 1);

		        stmt = con.createStatement();

		        // die EinfÃ¼geoperation erfolgt
		        stmt.executeUpdate("INSERT INTO propertyvalue (id, value, creationDate) "
		            + "VALUES (" + propertyValue.getId() + ",'" + propertyValue.getValue() + "','"
		            + propertyValue.getCreationDate() + "')");
		  	}
		  } catch(SQLException e) {
			  e.printStackTrace();
		  }
		  
	  }
	  
	  /*
	   *  Aktualisierung der Daten für PropertyValue Tabelle in DB
	   */
	  
	  public PropertyValue updatePropertyValue(PropertyValue propertyValue){
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();

		      stmt.executeUpdate("UPDATE property " + "SET description=\"" + propertyValue.getValue()
		          + "\" " + "SET modificationDate=\"" + propertyValue.getModifyDate()
		          + "\" "+ "WHERE id=" + propertyValue.getId());

		    } 
		  	  catch (SQLException e) {
		      e.printStackTrace();
		    } 
		    return propertyValue;
		  }
	  
	  /*
	   * Löschen der Eigenschaftsauspraegung in DB anhand des PropertyValue Objekts
	   * Aufruf der deletePropertyValue(int id)
	   */
	  
	  public void deletePropertyValue(PropertyValue propertyValue) {
		  
		  deletePropertyValue(propertyValue.getPropertyValueID());
		  
	  }
	  
	  /*
	   * Löschen der Eigenschaftsauspraegung in DB anhand der PropertyValue ID
	   */
	  
	  public void deletePropertyValue(int id) {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();
		      stmt.executeUpdate("DELETE FROM propertyvalue " + "WHERE id=" + id);

		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		  
	  }
	  
	  /*
	   * Anhand der als Parameter übergegebenen ID wird das zugehörige PropertyValue eindeutig identifiziert und zurückgegeben
	   */
	  
	  public PropertyValue findPropertyValueByKey(int id) {
				  
		  		  PropertyValue propertyValue = new PropertyValue();	  
				  Connection con = DBConnection.connection();
				  Statement stmt = null;
				  
				  try {
					  // Leeres SQL Statement anlegen	
					  stmt = con.createStatement();
					  // Statement ausfÃ¼llen und als Query an die DB schicken
				      ResultSet rs = stmt.executeQuery("SELECT id FROM propertyvalue "
				    		  + "WHERE id = " + id + " ORDER BY id");
				      
				      if (rs.next()) {
				    	  propertyValue.setId(rs.getInt("id"));
				    	  propertyValue.setValue(rs.getString("value"));
				    	  propertyValue.setCreationDate(rs.getTimestamp("creationDate"));
				    	  propertyValue.setModifyDate(rs.getTimestamp("modificationDate"));
				          
				  }
				  } catch (SQLException e) {
					  e.printStackTrace();
				  } 
				  
				  return propertyValue;
		}
	  
	  
	  /*
	   * Alle für den Benutzer zugänglichen PropertyValues werden gesucht, in einen Vector gespeichert und zurückgegeben
	   */

	  public Vector<PropertyValue> findAllPropertyValues(){
				  
				  Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();
				  
				  Connection con = DBConnection.connection();
				  Statement stmt = null;
				  
				  try {
					  // Leeres SQL Statement anlegen	
					  stmt = con.createStatement();
					  // Statement ausfÃ¼llen und als Query an die DB schicken
				      ResultSet rs = stmt.executeQuery("SELECT* FROM propertyvalue "
				          + " ORDER BY id");
				      
				      while (rs.next()) {
				          PropertyValue propValue = new PropertyValue();
				          propValue.setId(rs.getInt("id"));
				          propValue.setValue(rs.getString("value"));
				          propValue.setCreationDate(rs.getTimestamp("creationDate"));
				          propValue.setModifyDate(rs.getTimestamp("modificationDate"));
				          		          
		
				          // HinzufÃ¼gen des neuen Objekts zum Ergebnisvektor
				          propValueResult.addElement(propValue);
				          
				      	}
				  } catch (SQLException e) {
					  e.printStackTrace();
				  } 
				  
				  return propValueResult;
		}
	  
	  
	  	/*
	  	 * Alle für den Benutzer in der Applikation zugänglichen Auspraegungen werden anhand ihrer Auspraegungswerte 
	  	 * gesucht und zurückgegeben
	  	 */
	  
		public PropertyValue findPropertyValueByValue(String value) {
				  
				  PropertyValue propertyValue = new PropertyValue();	  
				  Connection con = DBConnection.connection();
				  Statement stmt = null;
				  
				  try {
					  // Leeres SQL Statement anlegen	
					  stmt = con.createStatement();
					  // Statement ausfÃ¼llen und als Query an die DB schicken
				      ResultSet rs = stmt.executeQuery("SELECT id, value FROM propertyvalue "
				    		  + "WHERE value = " + value + " ORDER BY id");
				      
				      if (rs.next()) {
				    	  propertyValue.setId(rs.getInt("id"));
				    	  propertyValue.setValue(rs.getString("value"));
				    	  propertyValue.setCreationDate(rs.getTimestamp("creationDate"));
				    	  propertyValue.setModifyDate(rs.getTimestamp("modificationDate"));
				          
				  }
				  } catch (SQLException e) {
					  e.printStackTrace();
				  } 
				  
				  return propertyValue;
				  
			  }
		
		/*
		 * Alle für den Benutzer in der Applikation zugänglichen Auspraegungen werden anhand ihrer zugehörigen Eigenschaften 
	  	 * gesucht und zurückgegeben
		 */
	  
		public PropertyValue findPropertyValueByProp(Property prop) {
			  			  
			  return PropertyMapper.propertyMapper().findByProperty(prop);
			  
		  }
		
		/*
		 * Alle für den Benutzer in der Applikation zugänglichen Auspraegungen werden anhand ihres Status 
	  	 * gesucht und zurückgegeben
		 */
	  
		public PropertyValue findPropertyValueByStatus(Property prop) {
			  			  
			  return PropertyMapper.propertyMapper().findByStatus(prop);
			  
		  }
		
		
		
}
