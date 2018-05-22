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
import de.hdm.kontaktsystem.shared.bo.User;

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
			("INSERT INTO PropertyValue (id, property_id, value) VALUES (?, ?, ?)"
					);
			stmt.setInt(1, pv.getBo_Id());
			stmt.setInt(2, pv.getProp().getId());
			stmt.setString(3, pv.getValue());
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
	 */

	public void deleteByContact(int id) {

		Connection con = DBConnection.connection();

		try {
			// Einfügeoperation in propertyvalue erfolgt
			PreparedStatement stmt = con.prepareStatement
					("DELETE FROM PropertyValue " + "INNER JOIN Contact "
					+ "ON PropertyValue.contact_ID = Contact = ? "
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
					("DELETE FROM PropertyValue " + "WHERE PropertyValue.property_ID = ?"
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

	public void deleteAllShared(User u) {

		// TODO: @ Sandra --> ParticipationMapper fehlt?

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
		
		
		public Vector <PropertyValue> findBy(User u) {
			
			Vector<Contact> contacts = new Vector<Contact>();
			
			contacts = ContactMapper.contactMapper().findAllContactsByUser(u.getGoogleID());
		
			
			for (Contact c : contacts) {
				
				Vector<PropertyValue> pV = new Vector<PropertyValue>();
				
				pV = findBy(c);	
				
				
				for (PropertyValue PropValue : pV) {
					
				c.setName(PropValue);
				
				System.out.println(PropValue);
				
				}
			}

			return null;
			
	
		}
		
	

	/*
	 * Löschen aller für den User geteilten und und vom User erstellten
	 * Eigenschaftsausprägungen
	 */

	public void deleteAll(User u) {

		ParticipationMapper.participationMapper().deleteParticipationForParticipantID(u.getGoogleID());

	}

	/*
	 * Anhand der uebergegebenen ID wird das zugehoerige PropertyValue eindeutig
	 * identifiziert und zurueckgegeben
	 */

	public PropertyValue findByKey(int id) {

		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con
					.prepareStatement("SELECT PropertyValue.ID, PropertyValue.value, Property.description "
							+ "FROM PropertyValue " + "INNER JOIN Property " + "WHERE PropertyValue.ID = ?");
			stmt.setInt(1, id);
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
	 * werden gesucht, in einen Vector gespeichert und zurueckgegeben
	 */

	public Vector<PropertyValue> findAll() {

		Vector<PropertyValue> propValueResult = new Vector<PropertyValue>();

		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con.prepareStatement
					("SELECT PropertyValue.ID, PropertyValue.value " 
					+ "FROM PropertyValue "
						);
					stmt.execute();
							
					ResultSet rs = stmt.executeQuery();

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
	 * Alle fuer den Benutzer in der Applikation zugaenglichen Auspraegungen (selbst
	 * erstellt oder Teilhaberschaft freigegeben) werden anhand ihres Status gesucht
	 * und die Ergebnisse zurueckgegeben
	 */

	public Vector<PropertyValue> findAllShared(User u, PropertyValue pV) {


		Vector<Participation> partVector1 = new Vector<Participation>();
		
		partVector1 = ParticipationMapper.participationMapper().findParticipationsByOwnerID(u.getGoogleID());
		
		for (Participation part1 : partVector1) {
			
			part1.setReference(pV);
			Vector<Participation> partVector2 = new Vector<Participation>();
			partVector2 = ParticipationMapper.participationMapper().findParticipationsByBusinessObject(part1.getReferencedObject());	
				
			for (Participation part2 : partVector1) {
				
				System.out.println(part2);
				
			}
			
			}
		
		
		
		return null;
		
	}

	/*
	 * UserMapper, ContactMapper, alle erzeugten PVs ausgeben
	 */

	// public Vector <PropertyValue> findAllCreated(User u) {
	//
	// Vector <PropertyValue> propValueResult = new Vector<PropertyValue>();
	//
	// Connection con = DBConnection.connection();
	// Statement stmt = null;
	//
	// try {
	// // Leeres SQL Statement anlegen
	// stmt = con.createStatement();
	// // Statement ausfüllen und als Query an die DB schicken
	// ResultSet rs = stmt.executeQuery
	// ("SELECT propertyvalue.id, propertyvalue.value"
	// + " FROM propertyvalue INNER JOIN businessobject"
	// + " WHERE businessobject.status= TRUE"
	// );
	//
	// while (rs.next()) {
	// PropertyValue propValue = new PropertyValue();
	// propValue.setBo_Id(rs.getInt("id"));
	// propValue.setShared_status(rs.getBoolean("status"));
	// propValueResult.addElement(propValue);
	// return propValueResult;
	//
	// }
	//
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	//
	// return null;
	// }

	/*
	 * Aufruf der Auspraegungen anhand ihrer zugeordneten Kontakte
	 */

	public Vector<PropertyValue> findBy(Contact c) {

		Vector<PropertyValue> propValueResult = new Vector<PropertyValue>();

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
					);

			while (rs.next()) {
				PropertyValue propValue = new PropertyValue();
				Property prop = new Property();
				propValue.setProp(prop);
				prop.setId(rs.getInt("property_ID"));
				PropertyMapper.propertyMapper().findBy(prop.getId());
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

	public Vector<PropertyValue> findBy(Property p) {

		Vector<PropertyValue> propValueResult = new Vector<PropertyValue>();

		Connection con = DBConnection.connection();
		Statement stmt = null;

		try {
			// Leeres SQL Statement anlegen
			stmt = con.createStatement();

			// Statement ausfüllen und als Query an die DB schicken
			ResultSet rs = stmt.executeQuery
					("SELECT PropertyValue.ID, PropertyValue.value, PropertyValue.property_ID"
					+ " FROM PropertyValue INNER JOIN Property" 
					+ " ON Property.ID = " + p.getId()
					);

			if (rs.next()) {
				PropertyValue propValue = new PropertyValue();
				Property prop = new Property();				
				prop.setId(rs.getInt("property_ID"));
				propValue.setProp(prop);
				
				System.out.println("PV-id: " + propValue.getValue());
				//PropertyMapper.propertyMapper().findBy(prop.getId());
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

	public void UpdatePropertyValueByContact(Contact contact) {
		// TODO Auto-generated method stub

	}

}