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
	 * Gibt nach dem Singelton Pattern eine Instanz des PropertyValueMppers zurück
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

		BusinessObjectMapper.businessObjectMapper().insert(pv);
		Connection con = DBConnection.connection();

		/*
		Property prop = new Property();
		Contact contact = new Contact();
		pv.setContact(contact);
		pv.setProp(prop);
		*/

		try {
			PreparedStatement stmt = con.prepareStatement(
			"INSERT INTO PropertyValue (ID, property_ID, value, contact_ID) VALUES (?, ?, ?, ?)");
			stmt.setInt(1, pv.getBo_Id());
			stmt.setInt(2, pv.getProp().getId());
			stmt.setString(3, pv.getValue());
			stmt.setInt(4, pv.getContact().getBo_Id());			

			if(stmt.executeUpdate() > 1) return pv;
			
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
	 *  
	 *  @param User-Objekt
	 *  @return Vector<PropertyValue>
	 */

	public Vector<PropertyValue> findAllSharedByMe (User user) {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln, die von diesem geteilt wurden
		Vector<Participation> participationVector = new Vector<Participation>();		
		participationVector = ParticipationMapper.participationMapper().findParticipationsByOwner(user);
		// Vector für die Speicherung aller BusinessObjekte erzeugen
		Vector<PropertyValue> propertyResultVector = new Vector <PropertyValue>(); 		
		//System.out.println(participationVector);
		
		for (Participation part : participationVector) { 		
			
			//System.out.println(part);
			 BusinessObject bo = BusinessObjectMapper.businessObjectMapper().findBusinessObjectByID(part.getReferenceID());
			 //System.out.println(bo);
			 PropertyValue propVal = new PropertyValue();	 
			 //System.out.println(propVal); 	
			 if(bo instanceof PropertyValue) {			 		
				 propVal = (PropertyValue) bo;
			 		//System.out.println("Ausprägung " + propVal.getProp());
			 		propertyResultVector.addElement(propVal);		     
			 }
		}
		return propertyResultVector;		
	}
	
	/**
	 * Alle fuer den Benutzer in der Applikation geteilte Ausprägungen <code>PropertyValue</code> Objekte
	 * können über den Aufruf dieser Methode aus der DB zurück gegeben werden.
	 * 
	 * @param User-Objekt
	 * @return Vector PropertyValue-Objekte
	 */

	public Vector<PropertyValue> findAllSharedByOthersToMe (User user) {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln, die von diesem geteilt wurden
		Vector<Participation> participationVector = new Vector<Participation>();		
		participationVector = ParticipationMapper.participationMapper().findParticipationsByParticipant(user);
		// Vector für die Speicherung aller BusinessObjekte erzeugen
		Vector<PropertyValue> propertyResultVector = new Vector <PropertyValue>(); 
		
		for (Participation part : participationVector) {
			 PropertyValue propVal = new PropertyValue();
			 BusinessObject bo = BusinessObjectMapper.businessObjectMapper().findBusinessObjectByID(part.getReferenceID());	
			 //System.out.println("pov-id: " + propVal.getBo_Id());		     
			 if(bo instanceof PropertyValue) {			 		
				 propVal = (PropertyValue) bo;
			 		//System.out.println("Ausprägung " + propVal.getProp());
			 		propertyResultVector.addElement(propVal);		     
			 }
		}
		return propertyResultVector;
		
	}

	/** TODO: 
	 * UserMapper, ContactMapper, alle erzeugten PVs ausgeben
	 */

	 public Vector <PropertyValue> findAllCreated(User u) {	
		 return null; 
		 
	 }

	 /**
	  * Gibt alle PropertValues zurück die zu einem Contact-Objekt gehören
	  * @param Contact-Objekt
	  * @return Vector<PropertyValue>
	  */
	 
	 public Vector<PropertyValue> findBy(Contact c) {
		 return findByContactID(c.getBo_Id());
	 }
	 
	 
	/**
	 * Aufruf der Auspraegungen anhand ihrer zugeordneten Kontakte
	 * ContactMapper,
	 * 
	 * @param Contact ID
	 * @return Vector<PropertyValue> 
	 */

	public Vector<PropertyValue> findByContactID(int contactID) {
		//System.out.println("#PV -findByContactID");
		Vector<PropertyValue> propValueResult = new Vector<PropertyValue>();

		Connection con = DBConnection.connection();

		try {
			// Statement ausfüllen und als Query an die DB schicken
			PreparedStatement stmt = con.prepareStatement
					( "SELECT pv.*, c.* "
					+ "FROM PropertyValue pv "
					+ "INNER JOIN Contact c ON pv.contact_ID = c.ID "
					+ "WHERE pv.contact_ID = ? " 
					);
					stmt.setInt(1, contactID);
					ResultSet rs = stmt.executeQuery();
					
			while (rs.next()) {
				PropertyValue propValue = new PropertyValue();				
				propValue.setProp(PropertyMapper.propertyMapper().findBy(rs.getInt("pv.property_ID")));
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
	  * Gibt alle PropertValues zurück die von einer Property sind
	  * 
	  * @param Property-Objekt
	  * @return Vector<PropertyValue>
	  */
	
	public Vector<PropertyValue> findBy(Property p) {
		System.out.println("#PV -findBy(Property)");
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
		System.out.println("#PV -findByContactID");
		Vector<PropertyValue> propValueResult = new Vector<PropertyValue>();

		Connection con = DBConnection.connection();

		try {
			// Statement ausfüllen und als Query an die DB schicken
			PreparedStatement stmt = con.prepareStatement
					( "SELECT pv.*, p.*, bo.* "
					+ "FROM PropertyValue pv "
					+ "INNER JOIN BusinessObject bo ON pv.ID = bo.ID "
					+ "INNER JOIN Property p ON pv.property_ID = p.ID "
					+ "WHERE pv.value = ? " 
					);
					stmt.setString(1, value);
					ResultSet rs = stmt.executeQuery();
					
			while (rs.next()) {
				PropertyValue propValue = new PropertyValue();				
				propValue.setProp(PropertyMapper.propertyMapper().findBy(rs.getInt("pv.property_ID")));
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
	 * Aufruf der Auspraegungen anhand ihrer zugeordneten Eigenschaft
	 * 
	 * @param Property ID
	  * @return Vector<PropertyValue>
	 */

	public Vector<PropertyValue> findByPropertyID(int propertyID) {
		System.out.println("#PV-FindBy(id)");
		Vector<PropertyValue> propValueResult = new Vector<PropertyValue>();
		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con.prepareStatement
					( "SELECT PropertyValue.ID, PropertyValue.value, PropertyValue.property_ID, "
					+ "Property.ID, Property.description "
					+ "FROM PropertyValue "
					+ "INNER JOIN Property ON PropertyValue.property_ID = Property.ID "
					+ "WHERE PropertyValue.property_ID = ? "
					);
					stmt.setInt(1, propertyID);
					stmt.execute();
					ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				PropertyValue propValue = new PropertyValue();
				Property prop = new Property();				
				prop.setId(rs.getInt("property_ID"));	
				prop.setDescription(rs.getString("description"));
				propValue.setBo_Id(rs.getInt("ID"));
				propValue.setValue(rs.getString("value"));
				//System.out.println("PV-id: " + propValue.getValue());
				propValue.setProp(prop);
				
				propValueResult.addElement(propValue);

			}
			return propValueResult;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Anhand der uebergegebenen ID wird das zugehoerige PropertyValue - Objekt,
	 * welches der Eigenschaft "Name" zugewiesen werden kann, eindeutig
	 * identifiziert und zurueckgegeben
	 * 
	 *  @param Contact-Objekt
	 *  @return PropertyValue - Objekt
	 */

	public PropertyValue findName(Contact contact) {
		//System.out.println("#PV -findName");
		PropertyValue name = new PropertyValue();		
		Vector <PropertyValue> result = new Vector <PropertyValue>();
		result = PropertyValueMapper.propertyValueMapper().findBy(contact);
		
		for(PropertyValue val : result) {
//			System.out.println("propertyVal id: " + val.getBo_Id());
//			System.out.println("propertyVal description: " + val.getProp().getDescription());
			
			if(val.getProp().getId() == 1) {
				name = val; 
				System.out.println("Contact name:" + val.getProp().getPropertyValues());
			}
		}	
		//System.out.println("Name: " + name);
		return name;
	}
	

	/*
	 * Aktualisierung der Daten fuer PropertyValue Tabelle in DB
	 * 
	 * @parm PropertyValue-Objekt
	 * @return PropertyValue-Objekt
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
	
	
	/**
	 * Loeschen der Eigenschaftsauspraegung in DB anhand des PropertyValue Objekts,
	 * Weitergabe an deletePropertyValue(int id)
	 * 
	 * @parm PropertyValue-Objekt
	 * @return PropertyValue-Objekt
	 */

	public PropertyValue delete(PropertyValue pv) {

		if(deleteByPropValue(pv.getBo_Id()) > 0) return pv;
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
			// Einfügeoperation in propertyvalue erfolgt
			PreparedStatement stmt = con.prepareStatement
			("DELETE FROM PropertyValue WHERE id= ?"
			);
			stmt.setInt(1, id);
			i = stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		BusinessObjectMapper.businessObjectMapper().deleteBusinessObjectByID(id);
		return i;
	}

	/**
	 * Anhand des zugehörigen Kontakts wird eine Auspraegung gelöscht
	 * 
	 * @parm Contact-Objekt
	 */

	public void deleteBy(Contact c) {
		deleteByContact(c.getBo_Id());	

	}

	/**
	 * Löschen der Ausprägung anhand der zugehörigen Kontakt Id
	 * Aufruf über Contact, der PropertyValue löscht
	 * 
	 * @parm Contact ID
	 */

	public void deleteByContact(int id) {
		
		for(PropertyValue pv : findByContactID(id)){
			delete(pv);
		}
		
	}

	/**
	 * Anhand der zugehörigen Eigenschaft wird eine Auspraegung gelöscht
	 * 
	 * @parm Property-Objekt
	 */

	public void deleteBy(Property prop) {

		deleteByProp(prop.getId());

	}

	/**
	 * Anhand der übergebenen ID einer Eigenschaft wird die Zugehörigkeit zur
	 * Ausprägung gesucht und alle Ausprägungen mitsamt ihrer Eigenschaften gelöscht
	 * 
	 * @parm Property ID
	 */

	public void deleteByProp(int property_id) {
		
		for(PropertyValue pv : findByPropertyID(property_id)){
			delete(pv);
		}

	}

	/**
	 * Methode zur L�schung aller von einem User erstellten Kontakte <code>Contact</code> Objekte,
	 * welche im System mit anderen Nutzern geteilt wurden. 
	 * 
	 * @param User-Objekt
	 */
	
	public void deleteAllSharedByMe(User user) {
		
		Vector <PropertyValue> propertyValueResult = new Vector <PropertyValue>();
		propertyValueResult = this.findAllSharedByMe(user);
		
		for(PropertyValue pV : propertyValueResult) {
			// loeschen aller Eintr�ge in der Teilhaberschaft Tabelle Participation
			ParticipationMapper.participationMapper().deleteParticipationForBusinessObject(pV);
			this.delete(pV);
		}
	}
	
	/**
	 * Funktion zum Löschen aller Auspraegungen die vom User geteilt wurden
	 * 
	 * @param User-Objekt
	 */

	public void deleteAllSharedByOthersToMe(User u) {

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
	


	/*************************************************************************************
	 * Methode zum Leeren der PropertyValue Tabelle
	 * WICHTIG: In App Logik nicht anwendbar, da Namensausprägung für Contact ggf. leer
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