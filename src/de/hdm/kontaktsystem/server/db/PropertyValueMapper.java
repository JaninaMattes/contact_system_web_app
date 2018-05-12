package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.google.appengine.api.users.User;

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
	   * Einfuegen einer neu angelegten Eigenschaftsauspraegung in die DB
	   */
	  
	  public void insert(PropertyValue pv) {
		  Connection con = DBConnection.connection();
		  Statement stmt1 = null;
		  Statement stmt2 = null;
		  
		  
		  /**********************************************************************
		   * INSERT INTO MULTIPLE TABLES??? 
		   * Brauche ich hier 2 Statements die separat ueber executeQuery() abgerufen werden?
		   ***********************************************************************/
		  
		  try {
		  stmt1 = con.createStatement();		  
		  ResultSet rs1 = stmt1.executeQuery(
				  "SELECT MAX(id) AS maxid "
		          + "FROM businessobject"
				  );
		  
		  if (rs1.next()) {
			 
			  pv.setBo_Id(rs1.getInt("maxid") + 1);

		        stmt1 = con.createStatement();

		        // Einfügeoperation erfolgt
		        stmt1.executeUpdate
		        ("INSERT INTO propertyvalue (id, value) "
		            + "VALUES (" + pv.getBo_Id() + ",'" 
		        		+ pv.getValue() 
		            + "')");
		  	}
		  
		  stmt2 = con.createStatement();		  
		  ResultSet rs2 = stmt2.executeQuery(
				  "SELECT MAX(id) AS maxid "
		          + "FROM businessobject"
				  );
		  
		  if (rs2.next()) {
			 
			  pv.setBo_Id(rs2.getInt("maxid") + 1);

			  stmt2 = con.createStatement();

		        // Einfügeoperation erfolgt
			  stmt2.executeUpdate
		        ("INSERT INTO businessobject (bo_id, creationdate) "
		            + "VALUES (" + pv.getBo_Id() + ",'" 
		             + pv.getCreationDate() 
		             + "')");
		  	}
		  
		  } catch(SQLException e) {
			  e.printStackTrace();
		  }
		  
	  }
	  
	  /*
	   *  Aktualisierung der Daten fuer PropertyValue Tabelle in DB
	   */
	  
	  public PropertyValue updatePropertyValue(PropertyValue pv){
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();

		      stmt.executeUpdate("UPDATE propertyvalue " + "SET value=\"" 
		    	  + pv.getValue()
		          + "\"" 
		          + "WHERE id=" + pv.getBo_Id()
		          + "ORDER BY id"
		          );

		    } 
		  	  catch (SQLException e) {
		      e.printStackTrace();
		    } 
		    return pv;
		  }
	  
	  /*
	   * Loeschen der Eigenschaftsauspraegung in DB anhand des PropertyValue Objekts,
	   * Weitergabe an deletePropertyValue(int id)
	   */
	  
	  public void deletePropertyValue(PropertyValue pv) {
		  
		  deleteById(pv.getBo_Id());
		  
	  }
	  
	  /*
	   * Loeschen der Eigenschaftsauspraegung in DB anhand der PropertyValue ID
	   */
	  
	  public void deleteById(int id) {
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
	  
	  public void deleteByContact(PropertyValue pv, Contact c) {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();
		      stmt.executeUpdate
		      ("DELETE FROM propertyvalue INNER JOIN Contact" 
		      + "WHERE PropertyValue.id=" + pv.getBo_Id()
		      + "AND" + "Contact.id=" + c.getBo_Id());

		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		  
	  }
	  
	  /*
	   * Anhand der zugehörigen Eigenschaft wird eine Auspraegung gelöscht  
	   */
	  
	  public void deleteByProperty(PropertyValue pv, Property prop) {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();
		      stmt.executeUpdate
		      ("DELETE FROM propertyvalue INNER JOIN property" 
		      + "WHERE propertyvalue.id=" + pv.getBo_Id()
		      + "AND" + "property.id=" + prop.getBo_Id()
		      );

		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		  
	  }	  

	  
	  /*
	   * Anhand der uebergegebenen ID wird das 
	   * zugehoerige PropertyValue eindeutig identifiziert und zurueckgegeben
	   */
	  
	  public PropertyValue findByKey(int id) {
				  
		  		  PropertyValue propertyValue = new PropertyValue();	  
				  Connection con = DBConnection.connection();
				  Statement stmt = null;
				  
				  try {
					  // Leeres SQL Statement anlegen	
					  stmt = con.createStatement();
					  // Statement ausfüllen und als Query an die DB schicken
				      ResultSet rs = stmt.executeQuery(
				      "SELECT id, value FROM propertyvalue "
				    		+ "WHERE id = " + id 
				    		+ " ORDER BY id");
				      
				      if (rs.next()) {
				    	  propertyValue.setBo_Id(rs.getInt("id"));
				    	  propertyValue.setValue(rs.getString("value"));
				      }
				      
				  } catch (SQLException e) {
					  e.printStackTrace();
					  
				  	} 
				  
				  return propertyValue;
		}
	  
	  /*********************************************************************
	   * 2 SELECT Statements jeweils ueber INNER JOIN? 
	   * Fall a) Participation (status=true, id, user_id)
	   * Fall b) Ownership (user_id, id)
	   * 
	   * Ausplittung in 2 Methoden?
	   * 1) findAllByOwnershipt
	   * 2) findAllByParticipation
	   *********************************************************************/
	  
	  /*
	   * Alle fuer Benutzer zugaenglichen PropertyValues (Participant und Ownership)
	   *  werden gesucht, in einen Vector gespeichert und zurueckgegeben
	   */

	  public Vector<PropertyValue> findAll(PropertyValue pv, User u){
				  
				  Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();
				  
				  Connection con = DBConnection.connection();
				  Statement stmt = null;
				  
				  try {
					  // Leeres SQL Statement anlegen	
					  stmt = con.createStatement();
					  // Statement ausfüllen und als Query an die DB schicken
				      ResultSet rs = stmt.executeQuery(
				      "SELECT propertyvalue.id, propertyvalue.value "
						  	+ "FROM propertyvalue INNER JOIN businessobject" 
						  	+ "WHERE propertyvalue.id=" 
						  	+ "AND businessobject.user_id=" + u.getUserId() 
						  	+ "AND businessobject.status=" + pv.getShared_Status()
						  	+ "ORDER BY propertyvalue.id"
					  );
				      
				      while (rs.next()) {
				          PropertyValue propValue = new PropertyValue();
				          propValue.setBo_Id(rs.getInt("id"));
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
	  	 * Alle fuer den Benutzer in der Applikation zugaenglichen Auspraegungen 
	  	 * werden anhand ihrer Auspraegungswerte gesucht und zurueckgegeben
	  	 */
	  
		public PropertyValue findByValue(String value, PropertyValue pv) {
				  
				  PropertyValue propertyValue = new PropertyValue();	  
				  Connection con = DBConnection.connection();
				  Statement stmt = null;
				  
				  try {
					  // Leeres SQL Statement anlegen	
					  stmt = con.createStatement();
					  // Statement ausfüllen und als Query an die DB schicken
				      ResultSet rs = stmt.executeQuery(
				      "SELECT id, value FROM propertyvalue "
				    		+ "WHERE value = " + value 
				    		+ "AND id =" + pv.getBo_Id() 
				    		+ "ORDER BY id"
				      );
				      
				      if (rs.next()) {
				    	  propertyValue.setBo_Id(rs.getInt("id"));
				    	  propertyValue.setValue(rs.getString("value"));
				          
				  }
				  } catch (SQLException e) {
					  e.printStackTrace();
				  } 
				  
				  return propertyValue;
				  
			  }
		
		/*
		 * Alle fuer den Benutzer in der Applikation zugaenglichen 
		 * Auspraegungen werden anhand ihrer zugehoerigen Eigenschaften gesucht und zurueckgegeben
		 */
	  
		public Vector<PropertyValue> findByProp(PropertyValue pv,Property prop) {
			  			  
			  Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();
			  
			  Connection con = DBConnection.connection();
			  Statement stmt = null;
			  
			  try {
				  // Leeres SQL Statement anlegen	
				  stmt = con.createStatement();
				  // Statement ausfüllen und als Query an die DB schicken
			      ResultSet rs = stmt.executeQuery
			    	("SELECT propertyvalue.id, propertyvalue.value "
			  			  + "FROM propertyvalue INNER JOIN property" 
			  			  + "WHERE propertyvalue.id=" + pv.getBo_Id()
			  			  + "AND" + "property.id=" + prop.getBo_Id()
			  			  + "ORDER BY propertyvalue.id"
			  		);
			      
			      while (rs.next()) {
			          PropertyValue propValue = new PropertyValue();		          
			          propValue.setBo_Id(rs.getInt("id"));
			          propValue.setShared_Status(rs.getBoolean("status"));
			          
			          // Hinzufügen des neuen Objekts zum Ergebnisvektor
			          propValueResult.addElement(propValue);
			          
			      	}
			      
			  } catch (SQLException e) {
				  e.printStackTrace();
			  } 
			  
			  return propValueResult;
			  
		  }
		
		/*
		 * Alle fuer den Benutzer in der Applikation zugaenglichen Auspraegungen (selbst erstellt 
		 * oder Teilhaberschaft freigegeben) werden anhand 
		 * ihres Status gesucht und die Ergebnisse zurueckgegeben
		 */
	  
		public Vector<PropertyValue> findByStatus (PropertyValue pv, Boolean shared_status){
			  
			  Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();
			  
			  Connection con = DBConnection.connection();
			  Statement stmt = null;
			  
			  try {
				  // Leeres SQL Statement anlegen	
				  stmt = con.createStatement();
				  // Statement ausfüllen und als Query an die DB schicken
			      ResultSet rs = stmt.executeQuery
			    		("SELECT propertyvalue.id, propertyvalue.value "
			    			+ "FROM propertyvalue INNER JOIN businessobject" 
					  		+ "WHERE propertyvalue.id=" + pv.getBo_Id()
					  		+ "AND" + "businessobject.status=" + pv.getShared_Status()
					  	);
			      
			      while (rs.next()) {
			          PropertyValue propValue = new PropertyValue();		          
			          propValue.setBo_Id(rs.getInt("id"));
			          propValue.setShared_Status(rs.getBoolean("status"));
			          
			          // Hinzufuegen des neuen Objekts zum Ergebnisvektor
			          propValueResult.addElement(propValue);
			          
			      	}
			      
			  } catch (SQLException e) {
				  e.printStackTrace();
			  } 
			  
			  return propValueResult;
	}
		
		/*
		 * Aufruf der Auspraegungen anhand ihrer zugeordneten Kontakte
		 */
		
		public Vector<PropertyValue> findByContact (PropertyValue pv, Contact c){
			  
			  Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();
			  
			  Connection con = DBConnection.connection();
			  Statement stmt = null;
			  
			  try {
				  // Leeres SQL Statement anlegen	
				  stmt = con.createStatement();
				  
				  // Statement ausfüllen und als Query an die DB schicken
			      ResultSet rs = stmt.executeQuery
			      ("SELECT propertyvalue.id, propertyvalue.value"
			    	+ "FROM propertyvalue INNER JOIN contact" 
			    	+ "WHERE propertyvalue.id=" + pv.getBo_Id()
			    	+ "AND" + "contact.id=" + c.getBo_Id()
			      );
			    	
			      while (rs.next()) {
			          PropertyValue propValue = new PropertyValue();		          
			          propValue.setBo_Id(rs.getInt("id"));
			          propValue.setContact(c);
			          
			          // Hinzufuegen des neuen Objekts zum Ergebnisvektor
			          propValueResult.addElement(propValue);
			          
			      	}
			      
			  } catch (SQLException e) {
				  e.printStackTrace();
			  } 
			  
			  return propValueResult;
	}
		
		
}
