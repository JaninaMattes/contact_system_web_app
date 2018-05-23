package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
			PreparedStatement stmt = con.prepareStatement
			("INSERT INTO PropertyValue (id, property_id, value, contact_ID) VALUES (?, ?, ?, ?)"
					);
			stmt.setInt(1, pv.getBo_Id());
			stmt.setInt(2, pv.getProp().getId());
			stmt.setString(3, pv.getValue());
			stmt.setInt(4, pv.getContact().getBo_Id());
			stmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Aktualisierung der Daten fuer PropertyValue Tabelle in DB
	 */

	public PropertyValue update(PropertyValue pv) {

		BusinessObjectMapper.businessObjectMapper().update(pv);

		Connection con = DBConnection.connection();

		try {
			// Einfügeoperation in propertyvalue erfolgt
			PreparedStatement stmt = con.prepareStatement
					("UPDATE PropertyValue SET value= ? WHERE ID= ?"
					);
			stmt.setString(1, pv.getValue());
			stmt.setInt(2, pv.getBo_Id());
			stmt.execute();

		} catch (SQLException e) {
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
			("DELETE FROM PropertyValue WHERE id= ?"
			);
			stmt.setInt(1, id);
			stmt.execute();

		} catch (SQLException e) {
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
	 * Aufruf über Contact, der PropertyValue löscht
	 */

	public void deleteByContact(int id) {

		Connection con = DBConnection.connection();

		try {
			// Einfügeoperation in propertyvalue erfolgt
			PreparedStatement stmt = con.prepareStatement
					("DELETE FROM PropertyValue " 
					+ "WHERE PropertyValue.contact_ID = ? "
					);
			stmt.setInt(1, id);
			stmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Anhand der zugehörigen Eigenschaft wird eine Auspraegung gelöscht
	 */

	public void deleteBy(Property prop) {

		deleteByProp(prop.getId());

	}

	/*
	 * Anhand der übergebenen ID einer Eigenschaft wird die Zugehörigkeit zur
	 * Ausprägung gesucht und alle Ausprägungen mitsamt ihrer Eigenschaften gelöscht
	 */

	public void deleteByProp(int property_id) {

		Connection con = DBConnection.connection();

		try {
			// Einfügeoperation in propertyvalue erfolgt
			PreparedStatement stmt = con.prepareStatement
					("DELETE FROM PropertyValue " 
					+ "WHERE PropertyValue.property_ID = ?"
					);
			stmt.setInt(1, property_id);
			stmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Funktion zum Löschen aller Auspraegungen die für den User geteilt wurden
	 */

	public void deleteAllSharedForMe(User u) {

		

	}
	
	/*
	 * Funktion zum Löschen aller Auspraegungen die vom User geteilt wurden
	 */

	public void deleteAllSharedByMe(User u) {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln, die von diesem geteilt wurden
				Vector<Participation> participationVector = new Vector<Participation>();		
				//participationVector = ParticipationMapper.participationMapper().deleteParticipationForParticipant(u);
				// Vector für die Speicherung aller BusinessObjekte erzeugen
				Vector<PropertyValue> propertyResultVector = new Vector <PropertyValue>(); 
				
				
				for (Participation part : participationVector) {
					
					 PropertyValue propVal = new PropertyValue();
					 propVal = this.findByKey(part.getReferenceID());			 
					 System.out.println("pov-id: " + propVal.getBo_Id());		     
					 	if(propVal != null) {
					 		propertyResultVector.addElement(propVal);
				     }
				}

	}
	

	/*
	 * Funktion zum Löschen aller Auspraegungen die vom User erstellt wurden
	 */

	public void deleteAllOwnership(User u) {

		// TODO: @ Sandra --> ParticipationMapper fehlt?

	}

	/*************************************************************************************
	 * Methode zum Leeren der PropertyValue Tabelle
	 * WICHTIG: In App Logik nicht anwendbar, da Namensausprägung für Contact ggf. leer
	 *************************************************************************************/
	
	public void deleteAll() {

		Connection con = DBConnection.connection();
		
		try {
			PreparedStatement stmt = con.prepareStatement
					("DELETE FROM PropertyValue INNER JOIN BusinessObject "
					+ "ON PropertyValue.ID = BusinessObject.bo_ID" 
					);
					stmt.execute();
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
		

	

	/*
	 * Anhand der uebergegebenen ID wird das zugehoerige PropertyValue eindeutig
	 * identifiziert und zurueckgegeben
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
							);			
			stmt.setInt(1, propvalue_id);
			stmt.execute();
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				// Erzeugen eines Property-Objektes und befüllen
				Property p = new Property();
				p.setId(rs.getInt("ID"));
				p.setDescription(rs.getString("description"));
				// Befülen eines PropertyValue-Objektes und setzen der Property-Objektes
				pv.setBo_Id(rs.getInt("ID"));
				pv.setValue(rs.getString("value"));
				pv.setProp(p);
				//System.out.println("Pv-id: " + pv.getBo_Id());
			}
			return pv;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * Alle fuer Benutzer zugaenglichen PropertyValues (Participant und Ownership)
	 * werden gesucht, in einen Vector gespeichert und zurueckgegeben
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
					+ "ORDER BY property_ID"
						);
					stmt.execute();
					ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
				PropertyValue propValue = new PropertyValue();
				Property prop = new Property();
				propValue.setBo_Id(rs.getInt("id"));
				propValue.setValue(rs.getString("value"));
				prop.setDescription(rs.getString("description"));
				propValue.setProp(prop);
				// Hinzufügen des neuen Objekts zum Ergebnisvektor
				propValueResult.addElement(propValue);
				
			}
		
		return propValueResult;
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *  Alle fuer den Benutzer in der Applikation zugaenglichen Auspraegungen <code>PropertyValue</code> - Objekte 
	 * (diese sind selbst erstellt und anderen zur Teilhaberschaft freigegeben) werden anhand ihres Status gesucht
	 *  und die Ergebnisse zurueckgegeben
	 */

	public Vector<PropertyValue> findAllSharedByMe (User user) {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln, die von diesem geteilt wurden
		Vector<Participation> participationVector = new Vector<Participation>();		
		participationVector = ParticipationMapper.participationMapper().findParticipationsByOwner(user);
		// Vector für die Speicherung aller BusinessObjekte erzeugen
		Vector<PropertyValue> propertyResultVector = new Vector <PropertyValue>(); 		
		
		for (Participation part : participationVector) {
			
			 PropertyValue propVal = new PropertyValue();
//			 BusinessObject bo = new BusinessObject();
//			 bo = BusinessObjectMapper.businessObjectMapper().findBusinessObjectByID(part.getReferenceID());
			 propVal = this.findByKey(part.getReferenceID());	
			 System.out.println(propVal);
			 //System.out.println("pov-id: " + propVal.getBo_Id());		     
			 	if(propVal != null) {
			 		propertyResultVector.addElement(propVal);
		     }
		}
		return propertyResultVector;
		
	}
	
	/**
	 * Alle fuer den Benutzer in der Applikation geteilte Ausprägungen <code>PropertyValue</code> Objekte
	 * können über den Aufruf dieser Methode aus der DB zurück gegeben werden.
	 * 
	 * @param user-Objekt
	 * @return Vector PropertyValue-Objekte
	 */

	public Vector<PropertyValue> findAllSharedByOthersToMe (User user) {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln, die von diesem geteilt wurden
		Vector<Participation> participationVector = new Vector<Participation>();		
		participationVector = ParticipationMapper.participationMapper().findParticipationsByParticipant(user);
//		for (Participation part : participationVector) {
//			
//			System.out.println(part);
//			
//		}
		// Vector für die Speicherung aller BusinessObjekte erzeugen
		Vector<PropertyValue> propertyResultVector = new Vector <PropertyValue>(); 
		
		
		for (Participation part : participationVector) {
			 PropertyValue propVal = new PropertyValue();
			 Property prop = new Property();
			 part.setReference(propVal);	
			 propVal = this.findByKey(part.getReferenceID());	
			 propVal.setProp(prop);
			 System.out.println(propVal);
			 //System.out.println("pov-id: " + propVal.getBo_Id());		     
			 if(propVal != null) {
				  propertyResultVector.addElement(propVal);
			     }
		}
		return propertyResultVector;
		
	}

	/*
	 * UserMapper, ContactMapper, alle erzeugten PVs ausgeben
	 */

	 public Vector <PropertyValue> findAllCreated(User u) {
	
		 return null; 
		 
	 }

	/*
	 * Aufruf der Auspraegungen anhand ihrer zugeordneten Kontakte
	 * ContactMapper, 
	 */

	public Vector<PropertyValue> findBy(Contact c) {

		c = ContactMapper.contactMapper().findContactById(c.getBo_Id());
		
		Vector<PropertyValue> propValueResult = new Vector<PropertyValue>();

		Connection con = DBConnection.connection();

		try {

			// Statement ausfüllen und als Query an die DB schicken
			PreparedStatement stmt = con.prepareStatement
					("SELECT pv.* "
					+ "FROM PropertyValue pv "
					+ "WHERE pv.contact_ID =" + c.getBo_Id() 
					);
					stmt.execute();
					ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				PropertyValue propValue = new PropertyValue();
				
				propValue.setProp(PropertyMapper.propertyMapper().findBy(rs.getInt("property_ID")));
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

	/**
	 * Aufruf der Auspraegungen anhand ihrer zugeordneten Eigenschaft
	 * 
	 */

	public Vector<PropertyValue> findBy(Property p) {
		//System.out.println("PV-FindBy Methode");

		Vector<PropertyValue> propValueResult = new Vector<PropertyValue>();

		Connection con = DBConnection.connection();

		try {

			// Statement ausfüllen und als Query an die DB schicken

			PreparedStatement stmt = con.prepareStatement
					("SELECT PropertyValue.ID, PropertyValue.value, PropertyValue.property_ID, "
					+ "Property.description "
					+ "FROM PropertyValue INNER JOIN Property "
					+ "ON PropertyValue.property_ID = ? "
					);
					stmt.setInt(1, p.getId());
					stmt.execute();
					ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				PropertyValue propValue = new PropertyValue();
				Property prop = new Property();				
				prop.setId(rs.getInt("property_ID"));	
				prop.setDescription(rs.getString("description"));
				//PropertyMapper.propertyMapper().findBy(prop.getId());
				propValue.setBo_Id(rs.getInt("ID"));
				propValue.setValue(rs.getString("value"));
				//System.out.println("PV-id: " + propValue.getValue());
				propValue.setProp(prop);				
				// Hinzufuegen des neuen Objekts zum Ergebnisvektor
				propValueResult.addElement(propValue);
				
			}
			return propValueResult;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void UpdatePropertyValueByContact(Contact contact) {
		// TODO Auto-generated method stub

	}

}