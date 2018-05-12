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
		  Statement stmt1 = null;
		  Statement stmt2 = null;
		  
		  
		  /*
		   * INSERT INTO MULTIPLE TABLES??? 
		   * creationDate & modificationDate attributes in propertyvalue table?
		   */
		  
		  try {
		  stmt1 = con.createStatement();		  
		  ResultSet rs1 = stmt1.executeQuery("SELECT MAX(id) AS maxid "
		          + "FROM businessobject");
		  if (rs1.next()) {
			 
			  propertyValue.setId(rs1.getInt("maxid") + 1);

		        stmt1 = con.createStatement();

		        // Einfügeoperation erfolgt
		        stmt1.executeUpdate
		        ("INSERT INTO propertyvalue (id, value) "
		            + "VALUES (" + propertyValue.getId() + ",'" 
		        		+ propertyValue.getValue() + "','" + "')");
		  	}
		  
		  stmt2 = con.createStatement();		  
		  ResultSet rs2 = stmt2.executeQuery("SELECT MAX(id) AS maxid "
		          + "FROM businessobject");
		  if (rs2.next()) {
			 
			  propertyValue.setId(rs2.getInt("maxid") + 1);

			  stmt2 = con.createStatement();

		        // Einfügeoperation erfolgt
			  stmt2.executeUpdate
		        ("INSERT INTO businessobject (id, creationdate) "
		            + "VALUES (" + propertyValue.getId() + ",'" 
		             + propertyValue.getCreationDate() + "','"
		             + "')");
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

		      stmt.executeUpdate("UPDATE propertyvalue " + "SET value=\"" 
		    	  + propertyValue.getValue()
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
	   * Anhand des zugehörigen Kontakts wird eine Auspraegung gelöscht
	   */
	  
	  public void deleteByContact(Contact c, PropertyValue pv) {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();
		      stmt.executeUpdate
		      ("DELETE value "
		      + "FROM propertyvalue INNER JOIN Contact" 
		      + "WHERE PropertyValue.id=" + pv.getPropertyValueID()
		      + "AND" + "Contact.id=" + c.getId());

		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		  
	  }
	  
	  /*
	   * Anhand der zugehörigen Eigenschaft wird eine Auspraegung gelöscht  
	   */
	  
	  public void deleteByProperty(Property prop, PropertyValue pv) {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();
		      stmt.executeUpdate
		      ("DELETE value "
		      + "FROM propertyvalue INNER JOIN property" 
		      + "WHERE propertyvalue.id=" + pv.getPropertyValueID()
		      + "AND" + "property.id=" + prop.getId());

		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		  
	  }	  

	  
	  /*
	   * Anhand der als Parameter �bergegebenen ID wird das zugeh�rige PropertyValue eindeutig identifiziert und zur�ckgegeben
	   */
	  
	  public PropertyValue findByKey(int id) {
				  
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
	  
		public PropertyValue findByValue(String value, PropertyValue pv) {
				  
				  PropertyValue propertyValue = new PropertyValue();	  
				  Connection con = DBConnection.connection();
				  Statement stmt = null;
				  
				  try {
					  // Leeres SQL Statement anlegen	
					  stmt = con.createStatement();
					  // Statement ausfüllen und als Query an die DB schicken
				      ResultSet rs = stmt.executeQuery("SELECT id, value FROM propertyvalue "
				    		  + "WHERE value = " + value + 
				    		  "AND id =" + pv.getPropertyValueID() + "ORDER BY id");
				      
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
		 * Alle f�r den Benutzer in der Applikation zug�nglichen Auspraegungen werden anhand ihrer zugeh�rigen Eigenschaften 
	  	 * gesucht und zur�ckgegeben
		 */
	  
		public PropertyValue findPropertyValueByProp(Property prop) {
			  			  
			  return prop.getPropertyID(prop);
			  
		  }
		
		/*
		 * Alle für den Benutzer in der Applikation zugaenglichen Auspraegungen (selbst erstellt 
		 * oder Teilhaberschaft freigegeben) werden anhand 
		 * ihres Status gesucht und Ergebnisse zurückgegeben
		 */
	  
		public Vector<PropertyValue> findByStatus (PropertyValue pv,Boolean shared_status){
			  
			  Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();
			  Vector <Business>
			  
			  Connection con = DBConnection.connection();
			  Statement stmt = null;
			  
			  try {
				  // Leeres SQL Statement anlegen	
				  stmt = con.createStatement();
				  // Statement ausfüllen und als Query an die DB schicken
			      ResultSet rs = stmt.executeQuery
			    		  ("SELECT propertyvalue.id, businessobject.status"
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
