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
		    return null;
		  }
	  
	  /*
	   * Loeschen der Eigenschaftsauspraegung in DB anhand des PropertyValue Objekts,
	   * Weitergabe an deletePropertyValue(int id)
	   */
	  
	  public void delete(PropertyValue pv) {
		  
		  deleteByPropValue(pv.getBo_Id());
		  
	  }
	  
	  /*
	   * Loeschen der Eigenschaftsauspraegung in DB anhand der PropertyValue ID
	   */
	  
	  public void deleteByPropValue(int id) {
		  
		  // BusinessObjectMapper.businessObjectMapper().update(id);
		  
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
			      ("DELETE PropertyValue FROM PropertyValue "
			      		+ "INNER JOIN Contact_PropertyValue "
			    		+ "ON PropertyValue.ID = Contact_PropertyValue.propertyValue_ID " 
			      		+ "WHERE Contact_PropertyValue.Contact_ID = ? ");
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
		      ("DELETE FROM PropertyValue "
		      		+ "WHERE PropertyValue.property_ID = ?");
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

	  
	  public void deleteAllShared() {
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
		      ("DELETE FROM PropertyValue INNER JOIN BusinessObject" 
		      + " WHERE BusinessObject.user_ID=" + u.getUserId()
		      + " AND BusinessObject.ID = PropertyValue.ID"
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
				  	
		  		  
				  Connection con = DBConnection.connection();
			
				  
				  try {
					  PreparedStatement stmt = con.prepareStatement
				      ("SELECT PropertyValue.ID, PropertyValue.value, Property.description "
				      		+ "FROM PropertyValue "
				      		+ "INNER JOIN Property "
				    		+ "WHERE PropertyValue.ID = ?" 
				    		);
					  stmt.setInt(1,  id);
					  stmt.execute();
					  
					  ResultSet rs = stmt.executeQuery();
				      
				      if (rs.next()) {
				    	  PropertyValue pv = new PropertyValue();
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
						  	+ "AND businessobject.status=" + pv.isShared_status()
						  	+ "ORDER BY propertyvalue.id"
					  );
				      
				      while (rs.next()) {
				          PropertyValue propValue = new PropertyValue();
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
		 */
	  
		public Vector <PropertyValue> findAllShared() {
			  
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
		
		public Vector <PropertyValue> findAllCreated(){
			  
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
		
		public Vector<PropertyValue> findBy(Contact c){
			  
			  Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();
			  
			  Connection con = DBConnection.connection();
			  Statement stmt = null;
			  
			  try {
				  // Leeres SQL Statement anlegen	
				  stmt = con.createStatement();
				  
				  // Statement ausfüllen und als Query an die DB schicken
			      ResultSet rs = stmt.executeQuery
			      ("SELECT PropertyValue.ID, PropertyValue.value"
			    	+ " FROM PropertyValue INNER JOIN Contact" 
			    	+ " WHERE Contact.ID=" + c.getBo_Id()
			    	+ " \""
			      );
			    	
			      while (rs.next()) {
			          PropertyValue propValue = new PropertyValue();		          
			          propValue.setBo_Id(rs.getInt("ID"));
			          propValue.setValue(rs.getString("value"));		
			          
			          // Hinzufuegen des neuen Objekts zum Ergebnisvektor
			          propValueResult.addElement(propValue);
			          			          
			      	}
			      
			      return propValueResult;
			      
			  } catch (SQLException e) {
				  e.printStackTrace();
			  } 
			  
			  return null;
	}

		

		/*
		 * Aufruf der Auspraegungen anhand ihrer zugeordneten Eigenschaft
		 */

		
		public Vector<PropertyValue> findBy(Property p){
			  
			  Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();
			  
			  Connection con = DBConnection.connection();
			  Statement stmt = null;
			  
			  try {
				  // Leeres SQL Statement anlegen	
				  stmt = con.createStatement();
				  
				  // Statement ausfüllen und als Query an die DB schicken
			      ResultSet rs = stmt.executeQuery
			      ("SELECT PropertyValue.ID, PropertyValue.value"
			    	+ " FROM PropertyValue INNER JOIN Property" 
			    	+ " WHERE Property.ID=" + p.getBo_Id()
			    	+ " \""
			      );
			    	
			      while (rs.next()) {
			          PropertyValue propValue = new PropertyValue();		          
			          propValue.setBo_Id(rs.getInt("ID"));
			          propValue.setValue(rs.getString("value"));		
			          
			          // Hinzufuegen des neuen Objekts zum Ergebnisvektor
			          propValueResult.addElement(propValue);
			          
			      	}
			      
			      return propValueResult;
			      
			  } catch (SQLException e) {
				  e.printStackTrace();
			  } 
			  
			  return null;
	}
		
		
		
		
		// TODO: 
		/**
		 * @author Janina, um anhand eines Users zugehörige PropertyValues suchen zu können
		 * zugeordnet werden abrufen. Würde diese mal als Vorschlag hier lassen.
		 * Dies würde mir später ermöglichen auch nach allen, einem User zugehörigen Property-Objekten
		 * zu filtern. 
		 * 
		 * --> Falls nicht anders möglich wäre es super wenn diese Methode noch umgesetzt würde. Danke :)!
		 *  
		 * @param user
		 * @return Vector <PropertyValue>
		 * 
		 */
		
		
		public Vector <PropertyValue> findBy(User user) {
			// TODO Auto-generated method stub
			
			return null;
		}
		
		//TODO:
		
		/**
		 * @author Janina, um Anhand eines PropertyValue-Objektes zugehörige Property-Objekte suchen zu können.
		 * Dies erlaubt mir auch hier über eine PropertyValue nach dem Property-Objekt zu suchen, ich könnte dies
		 * aber auch bei mir über einen Inner Join und den FK von PropertyValue zu Property lösen. Hätte nur gedacht
		 * dass es hier von der Zuständigkeit wahrscheinlich eher zu PropValMapper gehört.
		 * 
		 * --> Daher nur optional
		 * 
		 * @param propVal
		 * @return
		 */
		
		public Property findBy(PropertyValue propVal) {
			// TODO Auto-generated method stub
			
			return null;
		}


		
		
		
}