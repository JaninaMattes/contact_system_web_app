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
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.Property;


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
	  
	  public void insert(Property pv) { 
		  
		  BusinessObjectMapper.businessObjectMapper().insert(pv);
		  
		  Connection con = DBConnection.connection();


		  try {
		        // Einfügeoperation in propertyvalue erfolgt
			    PreparedStatement stmt = con.prepareStatement
		        ("INSERT INTO Property (id, property_id, value) VALUES (?, ?, ?)");
		        	stmt.setInt(1, pv.getBo_Id());
		        	stmt.setInt(2, pv.getProp().getBo_Id());
		        	stmt.setString(3, pv.getValue());
		        	stmt.execute();

		  
		  } catch(SQLException e) {
			  e.printStackTrace();
		  }
		  
}


	  /*
	   *  Aktualisierung der Daten fuer Property Tabelle in DB
	   */
	  
	  public Property update(Property pv){
		  
		  BusinessObjectMapper.businessObjectMapper().update(pv);
		  
		  Connection con = DBConnection.connection();
		  
		  try {
			  // Einfügeoperation in propertyvalue erfolgt
		      PreparedStatement stmt = con.prepareStatement
		      ("UPDATE Property SET value=? WHERE id= ?");
		      stmt.setString(1, pv.getValue());
		      stmt.setInt(2, pv.getBo_Id());
		      stmt.execute();
		      
		    } 
		  	  catch (SQLException e) {
		      e.printStackTrace();
		    } 
		    return null;
		  }
	  
	  /*
	   * Loeschen der Eigenschaftsauspraegung in DB anhand des Property Objekts,
	   * Weitergabe an deleteProperty(int id)
	   */
	  
	  public void delete(Property pv) {
		  
		  deleteByPropValue(pv.getBo_Id());
		  
	  }
	  
	  /*
	   * Loeschen der Eigenschaftsauspraegung in DB anhand der Property ID
	   */
	  
	  public void deleteByPropValue(int id) {
		  
		  // BusinessObjectMapper.businessObjectMapper().update(id);
		  
		  Connection con = DBConnection.connection();
		  
		  try {
			  // Einfügeoperation in propertyvalue erfolgt
		      PreparedStatement stmt = con.prepareStatement
		      ("DELETE FROM Property WHERE id= ?");
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
		   		  
		 deleteByContact(c.getBo_Id());

	  }
	
		/*
		 * Löschen der Ausprägung anhand der zugehörigen Kontakt Id
		 */

		public void deleteByContact(int id) {

		Connection con = DBConnection.connection();
			  
			  try {
				  // Einfügeoperation in propertyvalue erfolgt
			      PreparedStatement stmt = con.prepareStatement
			      ("DELETE Property FROM Property "
			      		+ "INNER JOIN Contact_Property "
			    		+ "ON Property.ID = Contact_Property.propertyValue_ID " 
			      		+ "WHERE Contact_Property.Contact_ID = ? ");
			      stmt.setInt(1, id);
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
		  
		  deleteByProp(prop.getBo_Id());
		  
	  }	  
	  
	  /*
	   * Anhand der übergebenen ID einer Eigenschaft wird die Zugehörigkeit zur Ausprägung gesucht 
	   * und alle Ausprägungen mitsamt ihrer Eigenschaften gelöscht
	   */
	  
	  public void deleteByProp(int property_id) {
 
		  Connection con = DBConnection.connection();
		  
		  try {
			  // Einfügeoperation in propertyvalue erfolgt
		      PreparedStatement stmt = con.prepareStatement
		      ("DELETE FROM Property "
		      		+ "WHERE Property.property_ID = ?");
		      stmt.setInt(1, property_id);
		      stmt.execute();
              

		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
			
		}
	  
	 
	  /*
	   * Funktion zum Löschen aller Auspraegungen die von User selbst erstellt wurden
	   */
	  

	  public void deleteByUser(User u) {
		  
		  Connection con = DBConnection.connection();
		  
		  try {
			  PreparedStatement stmt = con.prepareStatement
		      ("DELETE FROM propertyvalue INNER JOIN businessobject" 
		      + " WHERE businessobject.user_id=" 
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

	  
	  public void deleteAllShared(Participation p) {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();
		      stmt.executeUpdate

		      
		      ("DELETE FROM propertyvalue INNER JOIN businessobject" 
		      + " WHERE businessobject.user_id=" 
		      + " AND businessobject.status= TRUE"
		      );

		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		  
	  }	  
	  
	  /*
	   * Funktion zum Löschen aller Auspraegungen die für den User geteilt wurden anhand des mitgegebenen Kontakts
	   */

	  
	  public void deleteSharedFrom(Contact c) {
		  Connection con = DBConnection.connection();
		  Statement stmt = null;
		  
		  try {
		      stmt = con.createStatement();
		      stmt.executeUpdate

		      
		      ("DELETE FROM propertyvalue INNER JOIN businessobject" 
		      + " WHERE businessobject.user_id=" 
		      + " AND businessobject.status= TRUE"
		      );

		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		  
	  }	  
	  
	  /*
	   * Löschen aller für den User geteilten und erstellten Eigenschaftsausprägungen
	   */
	  
	  	public void deleteAll(User u) {
		  
		Connection con = DBConnection.connection();
		  
		  try {
			  PreparedStatement stmt = con.prepareStatement
		      ("DELETE FROM Property INNER JOIN BusinessObject" 
		      + " WHERE BusinessObject.user_ID=" + u.getUserId()
		      + " AND BusinessObject.ID = Property.ID"
		      );

		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		  
	  }
	  
	  /*
	   * Anhand der uebergegebenen ID wird das 
	   * zugehoerige Property eindeutig identifiziert und zurueckgegeben
	   */
	  
	  public Property findByKey(int id) {
				  	
		  		  
				  Connection con = DBConnection.connection();
			
				  
				  try {
					  PreparedStatement stmt = con.prepareStatement
				      ("SELECT Property.ID, Property.value, Property.description "
				      		+ "FROM Property "
				      		+ "INNER JOIN Property "
				    		+ "WHERE Property.ID = ?" 
				    		);
					  stmt.setInt(1,  id);
					  stmt.execute();
					  
					  ResultSet rs = stmt.executeQuery();
				      
				      if (rs.next()) {
				    	  Property pv = new Property();
				    	  pv.setBo_Id(rs.getInt("ID"));
				    	  pv.setValue(rs.getString("value"));
				    	  pv.getProp().setDescription(rs.getString("description"));
				    	  return pv;
				      }
				      
				  	  } catch (SQLException e) {
				  		  e.printStackTrace();
					  
				  	  } 
				  
				  return null;
		}
	  
	  
	  /*
	   * Alle fuer Benutzer zugaenglichen Propertys (Participant und Ownership)
	   *  werden gesucht, in einen Vector gespeichert und zurueckgegeben
	   */

	  public Vector<Property> findAll(Property pv, User u){
				  
				  Vector <Property> propValueResult = new Vector<Property>();
				  
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
				          Property propValue = new Property();
				          propValue.setBo_Id(rs.getInt("id"));
				          propValue.setValue(rs.getString("value"));
				          // Hinzufügen des neuen Objekts zum Ergebnisvektor
				          propValueResult.addElement(propValue);
				          return propValueResult;
				          
				      	}
				  } catch (SQLException e) {
					  e.printStackTrace();
				  } 
				  
				  return null;
		}
	  
	  
		
		/*
		 * Alle fuer den Benutzer in der Applikation zugaenglichen Auspraegungen (selbst erstellt 
		 * oder Teilhaberschaft freigegeben) werden anhand 
		 * ihres Status gesucht und die Ergebnisse zurueckgegeben
		 * 
		 * UserMapper, ContactMapper, alle geteilten PVs ausgeben
		 */
	  
		public Vector <Property> findAllShared() {
			  
			  Vector <Property> propValueResult = new Vector<Property>();
			  
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
			          Property propValue = new Property();		          
			          propValue.setBo_Id(rs.getInt("id"));
			          propValue.setShared_status(rs.getBoolean("status"));
			          
			          // Hinzufuegen des neuen Objekts zum Ergebnisvektor
			          propValueResult.addElement(propValue);
			          return propValueResult;
			          
			      	}
			      
			  } catch (SQLException e) {
				  e.printStackTrace();
			  } 
			  
			  return null;
	}
		
		/*
		 * UserMapper, ContactMapper, alle erzeugten PVs ausgeben
		 */
		
		public Vector <Property> findAllCreated(){
			  
			  Vector <Property> propValueResult = new Vector<Property>();
			  
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
			          Property propValue = new Property();		          
			          propValue.setBo_Id(rs.getInt("id"));
			          propValue.setShared_status(rs.getBoolean("status"));
			          propValueResult.addElement(propValue);
			          return propValueResult;
			          
			      	}
			      
			  } catch (SQLException e) {
				  e.printStackTrace();
			  } 
			  
			  return null;
	}
		
		/*
		 * Aufruf der Auspraegungen anhand ihrer zugeordneten Kontakte
		 */
		
		public Vector<Property> findBy(Contact c){
			  
			  Vector <Property> propValueResult = new Vector<Property>();
			  
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
			          Property propValue = new Property();		          
			          propValue.setBo_Id(rs.getInt("id"));
			          propValue.setValue(rs.getString("value"));		
			          
			          // Hinzufuegen des neuen Objekts zum Ergebnisvektor
			          propValueResult.addElement(propValue);
			          return propValueResult;
			          
			      	}
			      
			  } catch (SQLException e) {
				  e.printStackTrace();
			  } 
			  
			  return null;
	}

		

		
		
}
