package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import de.hdm.kontaktsystem.shared.bo.User;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;


public class PropertyValueMapper {

	/**
	 * Singleton Pattern
	 */

	private static PropertyValueMapper propertyValueMapper = null;

	protected PropertyValueMapper() {

	}

	/**
	 * Gibt nach dem Singelton Pattern eine Instanz des PropertyValueMppers zurueck
	 * 
	 * @return PropertyValueMapper-Objekt
	 */
	public static PropertyValueMapper propertyValueMapper() {
		if (propertyValueMapper == null) {
			propertyValueMapper = new PropertyValueMapper();
		}

		return propertyValueMapper;
	}

	/**
	 * Einfuegen einer neu angelegten Eigenschaftsauspraegung in die DB
	 * @param PropertyValue - Objekt
	 * 
	 * @param PropertyValue-Objekt
	 * @return PropertyValue-Objekt
	 */
	
	public PropertyValue insert(PropertyValue pv) {

		Connection con = DBConnection.connection();


		try {
			PreparedStatement stmt = con.prepareStatement(
			"INSERT INTO PropertyValue (ID, property_ID, value, contact_ID) VALUES (?, ?, ?, ?)");
			stmt.setInt(1, pv.getBoId());
			stmt.setInt(2, pv.getProperty().getId());
			stmt.setString(3, pv.getValue());
			stmt.setInt(4, pv.getContact().getBoId());	
			if(stmt.executeUpdate() > 0) return pv;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * Anhand der uebergegebenen ID wird das zugehoerige PropertyValue eindeutig
	 * identifiziert und zurueckgegeben
	 * 
	 * @param PropertyValue ID
	 * @return PropertyValue-Objekt
	 */

	public PropertyValue findByKey(int propvalue_id) {

		Connection con = DBConnection.connection();
		PropertyValue pv = new PropertyValue();
		
		try {
			PreparedStatement stmt = con.prepareStatement(
							  "SELECT PropertyValue.ID, PropertyValue.value, "
							+ "Property.description, Property.ID "
							+ "FROM PropertyValue " 
							+ "INNER JOIN Property ON PropertyValue.property_ID = Property.ID " 
							+ "WHERE PropertyValue.ID = ? "
							+ "ORDER BY Property.ID");	// ORDER BY um es nach den ProbValue zu sortieren.		
			stmt.setInt(1, propvalue_id);
			stmt.execute();
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				// Erzeugen eines Property-Objektes und befuellen
				Property p = new Property();
				p.setId(rs.getInt("ID"));
				p.setDescription(rs.getString("description"));
				// Befuellen eines PropertyValue-Objektes und setzen der Property-Objektes
				pv.setBo_Id(rs.getInt("ID"));
				pv.setValue(rs.getString("value"));
				pv.setProperty(p);
			}
			return pv;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	/**
	 * Alle fuer Benutzer zugaenglichen PropertyValues (Participant und Ownership)
	 * werden gesucht, in einen Vector gespeichert und zurueckgegeben
	 * 
	 * @return Vector<PropertyValue>
	 */

	public Vector<PropertyValue> findAll() {

		Vector<PropertyValue> propValueResult = new Vector<PropertyValue>();

		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con.prepareStatement
					("SELECT PropertyValue.ID, PropertyValue.value, "
					+ "Property.description, Property.ID " 
					+ "FROM PropertyValue INNER JOIN Property "
					+ "ON PropertyValue.property_ID = Property.ID "
					+ "WHERE PropertyValue.property_ID = Property.ID " 
					+ "ORDER BY Property.ID");  // ORDER BY um nach dem PropVal zu sortieren. 
					stmt.execute();
					ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
				PropertyValue propValue = new PropertyValue();
				Property prop = new Property();
				propValue.setBo_Id(rs.getInt("id"));
				propValue.setValue(rs.getString("value"));
				prop.setDescription(rs.getString("description"));
				propValue.setProperty(prop);
				// Hinzufuegen des neuen Objekts zum Ergebnisvektor
				propValueResult.addElement(propValue);
				
			}
		
		return propValueResult;
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	

	/**
	 * Alle Eigenschaftsausprägungen finden die User angelegt hat
	 * 
	 * @param User Objekt
	 */

	 public Vector <PropertyValue> findAllCreated(User u) {	

			Vector<PropertyValue> propValueResult = new Vector<PropertyValue>();

			Connection con = DBConnection.connection();

			try {
				PreparedStatement stmt = con.prepareStatement
						("SELECT PropertyValue.ID, PropertyValue.value, Property.description, Property.ID " 
						+ "FROM PropertyValue "
						+ "INNER JOIN Property ON PropertyValue.property_ID = Property.ID "
						+ "INNER JOIN BusinessObject ON BusinessObject.bo_ID = PropertyValue.ID "
						+ "WHERE BusinessObject.user_ID = ? " 
						+ "ORDER BY Property.ID");
				
						stmt.setDouble(1, u.getGoogleID());
						stmt.execute();
						ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
					PropertyValue propValue = new PropertyValue();
					Property prop = new Property();
					propValue.setBo_Id(rs.getInt("id"));
					propValue.setValue(rs.getString("value"));
					prop.setDescription(rs.getString("description"));
					propValue.setProperty(prop);
					// Hinzufuegen des neuen Objekts zum Ergebnisvektor
					propValueResult.addElement(propValue);
					
				}
			
			return propValueResult;
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null; 
		 
	 }

	 /**
	  * Gibt alle PropertValues zurueck die zu einem Contact-Objekt gehoeren
	  * @param Contact-Objekt
	  * @return Vector<PropertyValue>
	  */
	 
	 public Vector<PropertyValue> findBy(Contact c) {
		 return findByContactID(c.getBoId());
	 }
	 
	 
	/**
	 * Aufruf der Auspraegungen anhand ihrer zugeordneten Kontakte
	 * ContactMapper,
	 * 
	 * @param Contact ID
	 * @return Vector<PropertyValue> 
	 */

	public Vector<PropertyValue> findByContactID(int contactID) {
		Vector<PropertyValue> propValueResult = new Vector<PropertyValue>();

		Connection con = DBConnection.connection();

		try {
			// Statement ausfuellen und als Query an die DB schicken
			PreparedStatement stmt = con.prepareStatement
					( "SELECT pv.*, c.* "
					+ "FROM PropertyValue pv "
					+ "INNER JOIN Contact c ON pv.contact_ID = c.ID "
					+ "WHERE pv.contact_ID = ? "
					+ "ORDER BY pv.property_ID" );    // ORDER BY property_ID
					stmt.setInt(1, contactID);
					ResultSet rs = stmt.executeQuery();
					
			while (rs.next()) {
				PropertyValue propValue = new PropertyValue();				
				Property p = new Property();
				p.setId(rs.getInt("pv.property_ID"));
				propValue.setProperty(p);
				propValue.setBo_Id(rs.getInt("pv.ID"));
				propValue.setValue(rs.getString("pv.value"));

				// Hinzufuegen des neuen Objekts zum Ergebnisvektor
				propValueResult.addElement(propValue);

			}
			return propValueResult;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	
	/**
	  * Gibt alle PropertValues zurueck die von einer Property sind
	  * 
	  * @param Property-Objekt
	  * @return Vector<PropertyValue>
	  */
	
	public Vector<PropertyValue> findBy(Property p) {
		return findByPropertyID(p.getId());
		
	}
	
	
	/**
	 * Aufruf der Auspraegungen anhand ihrer zugeordneten Kontakte
	 * ContactMapper,
	 * 
	 * @param Contact ID
	 * @return Vector<PropertyValue> 
	 */

	public Vector<PropertyValue> findByValue(String value) {
		
		
		Vector<PropertyValue> propValueResult = new Vector<PropertyValue>();

		Connection con = DBConnection.connection();

		try {
			// Statement ausfuellen und als Query an die DB schicken
			PreparedStatement stmt = con.prepareStatement
					( "SELECT * "
					+ "FROM PropertyValue pv "
					+ "INNER JOIN Contact c ON pv.contact_ID = c.ID "
					+ "INNER JOIN BusinessObject bo ON bo.bo_ID = pv.ID "
					+ "WHERE pv.value LIKE  ? "
					+ "ORDER BY pv.property_ID" );  // ORDER BY um nach der PropVal zu sortieren.
					stmt.setString(1, "%" + value + "%");
					ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				PropertyValue propValue = new PropertyValue();				
				Property p = new Property();
				User u = new User();
				u.setGoogleID(rs.getDouble("bo.user_ID"));
				p.setId(rs.getInt("pv.property_ID"));
				propValue.setProperty(p);
				propValue.setBo_Id(rs.getInt("pv.ID"));
				propValue.setValue(rs.getString("pv.value"));
				propValue.setOwner(u);
				// Hinzufuegen des neuen Objekts zum Ergebnisvektor
				propValueResult.addElement(propValue);

			}
			return propValueResult;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Aufruf der Auspraegungen anhand ihrer zugeordneten Eigenschaft
	 * 
	 * @param Property ID
	  * @return Vector<PropertyValue>
	 */

	public Vector<PropertyValue> findByPropertyID(int propertyID) {
		Vector<PropertyValue> propValueResult = new Vector<PropertyValue>();
		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con.prepareStatement
					( "SELECT PropertyValue.ID, PropertyValue.value, PropertyValue.property_ID, "
					+ "Property.ID, Property.description "
					+ "FROM PropertyValue "
					+ "INNER JOIN Property ON PropertyValue.property_ID = Property.ID "
					+ "WHERE PropertyValue.property_ID = ? "
					+ "ORDER BY property_ID");	// ORDER BY um nach der PropVal zu sortieren.
					stmt.setInt(1, propertyID);
					stmt.execute();
					ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				PropertyValue propValue = new PropertyValue();
				Property prop = new Property();				
				prop.setId(rs.getInt("property_ID"));	
				prop.setDescription(rs.getString("description"));
				propValue.setBo_Id(rs.getInt("ID"));
				propValue.setValue(rs.getString("value"));
				propValue.setProperty(prop);
				propValueResult.addElement(propValue);
			}
			return propValueResult;
			

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	/*
	 * Aktualisierung der Daten fuer PropertyValue Tabelle in DB
	 * 
	 * @parm PropertyValue-Objekt
	 * @return PropertyValue-Objekt
	 */

	public PropertyValue update(PropertyValue pv) {

		
		Connection con = DBConnection.connection();

		try {
			// Einfügeoperation in propertyvalue erfolgt
			PreparedStatement stmt = con.prepareStatement
					("UPDATE PropertyValue SET value= ? WHERE ID= ?"
					);
			stmt.setString(1, pv.getValue());
			stmt.setInt(2, pv.getBoId());
			stmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Loeschen der Eigenschaftsauspraegung in DB anhand des PropertyValue Objekts,
	 * Weitergabe an deletePropertyValue(int id)
	 * 
	 * @parm PropertyValue-Objekt
	 * @return PropertyValue-Objekt
	 */

	public PropertyValue delete(PropertyValue pv) {

		if(deleteByPropValue(pv.getBoId()) > 0) return pv;
		else return null;

	}

	/**
	 * Loeschen der Eigenschaftsauspraegung in DB anhand der PropertyValue ID
	 * 
	 * @parm PropertyValue ID
	 * @return Anzahl geänderter Einträge
	 */

	public int deleteByPropValue(int id) {

		Connection con = DBConnection.connection();
		int i = 0;

		try {
			// Einfuegeoperation in propertyvalue erfolgt
			PreparedStatement stmt = con.prepareStatement
			("DELETE FROM PropertyValue WHERE id= ?"
			);
			stmt.setInt(1, id);
			i = stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return i;
	}

	

	/**
	 * Anhand der zugehoerigen Eigenschaft wird eine Auspraegung geloescht
	 * 
	 * @parm Property-Objekt
	 */

	public void deleteBy(Property prop) {

		deleteByProp(prop.getId());

	}

	/**
	 * Anhand der Uebergebenen ID einer Eigenschaft wird die Zugehoerigkeit zur
	 * Ausprägung gesucht und alle Auspraegungen mitsamt ihrer Eigenschaften geloescht
	 * 
	 * @parm Property ID
	 */

	public void deleteByProp(int property_id) {
		
		for(PropertyValue pv : findByPropertyID(property_id)){
			delete(pv);
		}

	}

	
	


	/*************************************************************************************
	 * Methode zum Leeren der PropertyValue Tabelle
	 * WICHTIG: In App Logik nicht anwendbar, da Namensauspraegung fuer Contact ggf. leer
	 *************************************************************************************/
	
	public void deleteAll() {
		

		Connection con = DBConnection.connection();
		
		try {
			PreparedStatement stmt = con.prepareStatement(
					  "DELETE FROM PropertyValue "
					+ "INNER JOIN BusinessObject "
					+ "ON PropertyValue.ID = BusinessObject.bo_ID" 
					);
					stmt.execute();
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	


}