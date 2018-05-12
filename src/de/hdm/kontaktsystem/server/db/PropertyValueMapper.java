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
	   * Einf�gen einer neu angelegten Eigenschaftsauspr�gung in die DB
	   */
	  
	  public void insert(PropertyValue propertyValue) {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		  stmt = con.createStatement();		  
		  ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid "
		          + "FROM businessobject");
		  if (rs.next()) {
			 
			  propertyValue.setId(rs.getInt("maxid") + 1);

		        stmt = con.createStatement();

		        // Einfügeoperation erfolgt
		        stmt.executeUpdate
		        ("INSERT INTO propertyvalue (id, value, creationDate) "
		            + "VALUES (" + propertyValue.getId() + ",'" + propertyValue.getValue() + "','"
		            + propertyValue.getCreationDate() + "')"
		          
		        		);
		        
		  	}
		  } catch(SQLException e) {
			  e.printStackTrace();
		  }
		  
	  }
	  
	  /*
	   *  Aktualisierung der Daten f�r PropertyValue Tabelle in DB
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
	   * L�schen der Eigenschaftsauspraegung in DB anhand des PropertyValue Objekts
	   * Aufruf der deletePropertyValue(int id)
	   */
	  
	  public void deletePropertyValue(PropertyValue propertyValue) {
		  
		  deletePropertyValue(propertyValue.getPropertyValueID());
		  
	  }
	  
	  /*
	   * L�schen der Eigenschaftsauspraegung in DB anhand der PropertyValue ID
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
	   * L�schen der Eigenschaftsauspraegung in DB anhand des Kontaktnamens
	   */
	  
	  public void deletePropertyValueOf(Contact c) {

		  Connection con = DBConnection.connection();

		    try {
		      Statement stmt = con.createStatement();

		      stmt.executeUpdate("DELETE FROM propertyvalue " + "WHERE contact=" + c.getId());
		      
		    
		    }
		    catch (SQLException e2) {
		      e2.printStackTrace();
		    }
		  
	  }
	  
	  /*
	   * Anhand der als Parameter �bergegebenen ID wird das zugeh�rige PropertyValue eindeutig identifiziert und zur�ckgegeben
	   */
	  
	  public PropertyValue findPropertyValueByKey(int id) {
				  
		  		  PropertyValue propertyValue = new PropertyValue();	  
				  Connection con = DBConnection.connection();
				  Statement stmt = null;
				  
				  try {
					  // Leeres SQL Statement anlegen	
					  stmt = con.createStatement();
					  // Statement ausfüllen und als Query an die DB schicken
				      ResultSet rs = stmt.executeQuery("SELECT id, value FROM propertyvalue "
				    		  + "WHERE id = " + id + " ORDER BY id");
				      
				      if (rs.next()) {
				    	  propertyValue.setId(rs.getInt("id"));
				    	  propertyValue.setValue(rs.getString("value"));

				          
				  }
				  } catch (SQLException e) {
					  e.printStackTrace();
				  } 
				  
				  return propertyValue;
		}
	  
	  /*
	   * Alle f�r den Benutzer zug�nglichen PropertyValues werden gesucht, in einen Vector gespeichert und zur�ckgegeben
	   */

	  public Vector<PropertyValue> findByContact(Contact c){
				  
				  Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();
				  
				  Connection con = DBConnection.connection();
				  Statement stmt = null;
				  
				  try {
					  // Leeres SQL Statement anlegen	
					  stmt = con.createStatement();
					  // Statement ausfüllen und als Query an die DB schicken
				      ResultSet rs = stmt.executeQuery("SELECT id, value FROM propertyvalue "
				          + " ORDER BY id");
				      
				      /***********************************************************************
				       * INNER Join in Statements!!!
				       ***********************************************************************/
				      
				      while (rs.next()) {
				          PropertyValue propValue = new PropertyValue();
				          propValue.setId(rs.getInt("id"));
				          propValue.setValue(rs.getString("value"));
				         
				          		          
		
				          // Hinzufügen des neuen Objekts zum Ergebnisvektor
				          propValueResult.addElement(propValue);
				          
				      	}
				  } catch (SQLException e) {
					  e.printStackTrace();
				  } 
				  
				  return propValueResult;
		}
	  
	  /*
	   * Alle für Benutzer zug�nglichen PropertyValues werden gesucht, in einen Vector gespeichert und zur�ckgegeben
	   */

	  public Vector<PropertyValue> findAll(){
				  
				  Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();
				  
				  Connection con = DBConnection.connection();
				  Statement stmt = null;
				  
				  try {
					  // Leeres SQL Statement anlegen	
					  stmt = con.createStatement();
					  // Statement ausfüllen und als Query an die DB schicken
				      ResultSet rs = stmt.executeQuery("SELECT id, value FROM propertyvalue "
				          + " ORDER BY id");
				      
				      while (rs.next()) {
				          PropertyValue propValue = new PropertyValue();
				          propValue.setId(rs.getInt("id"));
				          propValue.setValue(rs.getString("value"));
				          // Hinzufügen des neuen Objekts zum Ergebnisvektor
				          propValueResult.addElement(propValue);
				          
				      	}
				  } catch (SQLException e) {
					  e.printStackTrace();
				  } 
				  
				  return propValueResult;
		}
	  
	  
	  	/*
	  	 * Alle f�r den Benutzer in der Applikation zug�nglichen Auspraegungen werden anhand ihrer Auspraegungswerte 
	  	 * gesucht und zur�ckgegeben
	  	 */
	  
		public PropertyValue findPropertyValueByValue(String value) {
				  
				  PropertyValue propertyValue = new PropertyValue();	  
				  Connection con = DBConnection.connection();
				  Statement stmt = null;
				  
				  try {
					  // Leeres SQL Statement anlegen	
					  stmt = con.createStatement();
					  // Statement ausfüllen und als Query an die DB schicken
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
		 * Alle f�r den Benutzer in der Applikation zug�nglichen Auspraegungen werden anhand ihrer zugeh�rigen Eigenschaften 
	  	 * gesucht und zur�ckgegeben
		 */
	  
		public PropertyValue findPropertyValueByProp(Property prop) {
			  			  
			  return PropertyMapper.propertyMapper().getProperty(prop);
			  
		  }
		
		/*
		 * Alle für den Benutzer in der Applikation zugaenglichen Auspraegungen (selbst erstellt 
		 * oder Teilhaberschaft freigegeben) werden anhand 
		 * ihres Status gesucht und Ergebnisse zurückgegeben
		 */
	  
		public Vector<PropertyValue> findByStatus(Boolean shared_status){
			  
			  Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();
			  
			  Connection con = DBConnection.connection();
			  Statement stmt = null;
			  
			  try {
				  // Leeres SQL Statement anlegen	
				  stmt = con.createStatement();
				  // Statement ausfüllen und als Query an die DB schicken
			      ResultSet rs = stmt.executeQuery("SELECT id, status FROM propertyvalue "
			          + " ORDER BY id");
			      
			      while (rs.next()) {
			          PropertyValue propValue = new PropertyValue();
			          propValue.setId(rs.getInt("id"));
			          propValue.setShared_Status(rs.getBoolean("status"));
			          // Hinzufügen des neuen Objekts zum Ergebnisvektor
			          propValueResult.addElement(propValue);
			          
			      	}
			  } catch (SQLException e) {
				  e.printStackTrace();
			  } 
			  
			  return propValueResult;
	}
		
		
		public PropertyValue findByContact(PropertyValue propertyValue) {
		    /*
		     * Wir bedienen uns hier einfach des CustomerMapper. Diesem geben wir
		     * einfach den in dem Account-Objekt enthaltenen Fremdschlüssel für den
		     * Kontoinhaber. Der CustomerMapper lässt uns dann diese ID in ein Objekt
		     * auf.
		     */
		    return ContactMapper.contactMapper().getId();
		  }
		
		
}
