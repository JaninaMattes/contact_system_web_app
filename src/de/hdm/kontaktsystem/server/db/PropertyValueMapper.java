package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
	   * 
	   */
	  
	  public void insert(PropertyValue pv) { 
		  
		  BusinessObjectMapper.businessObjectMapper().insert(pv);
		  
		  Connection con = DBConnection.connection();


		  try {
		        // Einfügeoperation in propertyvalue erfolgt
			  PreparedStatement stmt = con.prepareStatement
		        ("INSERT INTO PropertyValue (id, property_id, value) VALUES (?, ?, ?)");
		        	stmt.setInt(1, pv.getBo_Id());
		        	stmt.setInt(2, pv.getProp().getBo_Id());
		        	stmt.setString(3, pv.getValue());
		        	stmt.execute();

		  
		  } catch(SQLException e) {
			  e.printStackTrace();
		  }
		  
}


	  /*
	   *  Aktualisierung der Daten fuer PropertyValue Tabelle in DB
	   */
	  
	  public PropertyValue update(PropertyValue pv){
		  
		  BusinessObjectMapper.businessObjectMapper().update(pv);
		  
		  Connection con = DBConnection.connection();
		  
		  try {
			  // Einfügeoperation in propertyvalue erfolgt
		      PreparedStatement stmt = con.prepareStatement
		      ("UPDATE PropertyValue SET value=? WHERE id= ?");
		      stmt.setString(1, pv.getValue());
		      stmt.setInt(2, pv.getBo_Id());
		      stmt.execute();
		      
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
	  
	  public void delete(PropertyValue pv) {
		  
		  deleteBy(pv.getBo_Id());
		  
	  }
	  
	  /*
	   * Loeschen der Eigenschaftsauspraegung in DB anhand der PropertyValue ID
	   */
	  
	  public void deleteBy(int id) {
		  
		  Connection con = DBConnection.connection();
		  
		  try {
			  // Einfügeoperation in propertyvalue erfolgt
		      PreparedStatement stmt = con.prepareStatement
		      ("DELETE FROM PropertyValue WHERE id= ?");
		      stmt.setInt(1, id);
		      stmt.execute();
		      
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		  
	  }
	  
	  /*
	   * Anhand des zugehörigen Kontakts wird eine Auspraegung gelöscht
	   */
	  
	  public void deleteBy(Contact c) {
		   
		  Connection con = DBConnection.connection();

		  
		  try {
			  // Einfügeoperation in propertyvalue erfolgt
		      PreparedStatement stmt = con.prepareStatement
		      ("DELETE FROM PropertyValue INNER JOIN Contact ON Contact.ID = ?");
		      stmt.setInt(1, 31);
		      stmt.execute();

		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		  
	  }
	  
	  
	  /*
	   * Anhand der zugehörigen Eigenschaft wird eine Auspraegung gelöscht  
	   */
	  
	  public void deleteBy(Property prop) {
		  
		  Connection con = DBConnection.connection();
		  
		  try {
			  
			  Vector <PropertyValue> propResult = new Vector<PropertyValue>();     
			  propResult = prop.getPropertyValues();
              
              for (PropertyValue pV : propResult) {
              	  
			  // Einfügeoperation in propertyvalue erfolgt
		      PreparedStatement stmt = con.prepareStatement
		      ("DELETE FROM PropertyValue "
		      		+ "WHERE PropertyValue.ID = ? AND PropertyValue.property_ID = ?");
		      stmt.setInt(1, pV.getBo_Id());
		      stmt.setInt(2, prop.getBo_Id());
		      stmt.execute();
              }

		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		  
	  }	  
	 
	  /*
	   * Funktion zum Löschen aller Auspraegungen die von User selbst geteilt wurden
	   */
	  
	  /***********************************************************************
	   * IDENTIFIKATION EIGENE user_id??
	   ***********************************************************************/

	  public void deleteAllSharedBy(int id) {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();
		      stmt.executeUpdate
		      ("DELETE FROM propertyvalue INNER JOIN businessobject" 
		      + " WHERE businessobject.user_id=" + id
		      + " AND businessobject.status= TRUE"
		      );

		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		  
	  }	  
	  
	  /*
	   * Funktion zum Löschen aller Auspraegungen die für den User geteilt wurden
	   */

	  /***********************************************************************
	   * IDENTIFIKATION EIGENE user_id??
	   ***********************************************************************/
	  
	  public void deleteAllSharedByOther(int id) {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();
		      stmt.executeUpdate
		      
		      /*
		       * Id des Users suchen, der diese Auspraegungen geteilt hat
		       */
		      
		      ("DELETE FROM propertyvalue INNER JOIN businessobject" 
		      + " WHERE businessobject.user_id=" + id
		      + " AND businessobject.status= TRUE"
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
						  	+ "AND businessobject.status=" + pv.getShared_status()
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
	  
		public Vector<PropertyValue> findByValue(String value) {
				  
			 	  Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();	  
				  Connection con = DBConnection.connection();
				  Statement stmt = null;
				  
				  
				  
				  try {
					  // Leeres SQL Statement anlegen	
					  stmt = con.createStatement();
					  // Statement ausfüllen und als Query an die DB schicken
				      ResultSet rs = stmt.executeQuery(
				      "SELECT id, value FROM propertyvalue "
				    		+ "WHERE value = " + value  
				    		+ " ORDER BY id"
				      );
				      
				      if (rs.next()) {
				    	  PropertyValue propValue = new PropertyValue();
				    	  propValue.setBo_Id(rs.getInt("id"));
				    	  propValue.setValue(rs.getString("value"));
				    	  propValueResult.addElement(propValue);
				          
				  }
				  } catch (SQLException e) {
					  e.printStackTrace();
				  } 
				  
				  return propValueResult;
				  
			  }
		
		/*
		 * Alle fuer den Benutzer in der Applikation zugaenglichen 
		 * Auspraegungen werden anhand ihrer zugehoerigen Eigenschaften gesucht und 
		 * mit dieser (!!!!!!!!!) zurueckgegeben
		 */
	  
		public Vector<PropertyValue> findBy(Property prop) {
			  			  
			  Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();
			  
			  Connection con = DBConnection.connection();
			  Statement stmt = null;
			  
			  try {
				  // Leeres SQL Statement anlegen	
				  stmt = con.createStatement();
				  // Statement ausfüllen und als Query an die DB schicken
			      ResultSet rs = stmt.executeQuery
			    	("SELECT propertyvalue.id, propertyvalue.value"
			  			  + " FROM propertyvalue INNER JOIN property" 
			  			  + " WHERE property.id=" + prop.getBo_Id()
			  			  + " ORDER BY propertyvalue.id"
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
		 * Alle fuer den Benutzer in der Applikation zugaenglichen Auspraegungen (selbst erstellt 
		 * oder Teilhaberschaft freigegeben) werden anhand 
		 * ihres Status gesucht und die Ergebnisse zurueckgegeben
		 */
	  
		public Vector <PropertyValue> findBy(Boolean shared_status){
			  
			  Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();
			  
			  Connection con = DBConnection.connection();
			  Statement stmt = null;
			  
			  try {
				  // Leeres SQL Statement anlegen	
				  stmt = con.createStatement();
				  // Statement ausfüllen und als Query an die DB schicken
			      ResultSet rs = stmt.executeQuery
			    		("SELECT propertyvalue.id, propertyvalue.value"
			    			+ " FROM propertyvalue INNER JOIN businessobject" 
					  		+ " WHERE businessobject.status= TRUE" 
					  	);
			      
			      while (rs.next()) {
			          PropertyValue propValue = new PropertyValue();		          
			          propValue.setBo_Id(rs.getInt("id"));
			          propValue.setShared_status(rs.getBoolean("status"));
			          
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
		
		public Vector<PropertyValue> findBy(Contact c){
			  
			  Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();
			  
			  Connection con = DBConnection.connection();
			  Statement stmt = null;
			  
			  try {
				  // Leeres SQL Statement anlegen	
				  stmt = con.createStatement();
				  
				  // Statement ausfüllen und als Query an die DB schicken
			      ResultSet rs = stmt.executeQuery
			      ("SELECT propertyvalue.id, propertyvalue.value"
			    	+ " FROM propertyvalue INNER JOIN contact" 
			    	+ " WHERE contact.id=" + c.getBo_Id()
			    	+ " \""
			      );
			    	
			      while (rs.next()) {
			          PropertyValue propValue = new PropertyValue();		          
			          propValue.setBo_Id(rs.getInt("id"));
			          propValue.setValue(rs.getString("value"));		
			          
			          // Hinzufuegen des neuen Objekts zum Ergebnisvektor
			          propValueResult.addElement(propValue);
			          
			      	}
			      
			  } catch (SQLException e) {
				  e.printStackTrace();
			  } 
			  
			  return propValueResult;
	}

		
		
		public void deleteByProperty(int property_id) {
			// TODO Auto-generated method stub
			
		}

		public Vector<PropertyValue> findByProperty(Property property) {
			// TODO Auto-generated method stub
			return null;
		}

		public void deleteByContactId(int id) {
			// TODO Auto-generated method stub
			
		}
		
		
}
