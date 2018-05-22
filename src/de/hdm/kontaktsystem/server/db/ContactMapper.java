package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.sql.ResultSet;

//import com.mysql.jdbc.Connection;

import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

/**
 * Die Mapper-Klasse <code>ContactMapper</code> bildet <code>Contact</code>-Objekte 
 * auf eine relationale Datenbank ab. Dazu werden Methoden zum Erzeugen, Suchen, Ändern und 
 * Löschen von Objekten zur Verfügung gestellt. Es können sowohl Objekte in Datenbank-Strukturen, 
 * als auch Datenbank-Strukturen in Objekte überführt werden.
 * 
 * @author Katalin
 *
 */
public class ContactMapper {
	/**
	 * Die Klasse <code>ContactMapper</code> ist ein Singleton, 
	 * d.h. sie wird nur einmal instantiiert.
	 * Die statische Variable <code>INSTANCE</code> speichert die einzige Instanz der Klasse. 
	 * Durch den Bezeichner <code>static</code> ist diese Variable 
	 * nur einmal für alle Instanzen der Klasse vorhanden.
	 */

	private static ContactMapper contactMapper = null;

	protected ContactMapper() {

	}

	public static ContactMapper contactMapper() {
		if (contactMapper == null) {
			contactMapper = new ContactMapper();
		}

		return contactMapper;
	}

	/**
	 * Mapper-Methode um einen Kontakt zu loeschen
	 * 
	 * @param contact
	 */
	public void deleteContact(Contact contact) {
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("DELET FROM CONTACT WHERE id = " + contact.getBo_Id());
			
			//loeschen der Eigenschaftsausprägungen eines Kontaktes
			PropertyValueMapper.propertyValueMapper().deleteBy(contact); 
			
			BusinessObjectMapper.businessObjectMapper().deleteBusinessObject(contact);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Mapper-Methode um einen Kontakt mit Hilfe der ID zu loeschen
	 * 
	 * @param id
	 */
	public void deleteContactByID(int id) {
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM CONTACT WHERE ID = " + id);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Mapper-Methode um alle Kontakte eines bestimmten Users mittels der User-ID zu
	 * loeschen
	 */

	public void deleteAllContactsByUser(double user_id) {

		Vector<Contact> result = new Vector<Contact>();
		
		//Aufrufen aller Kontakte eines bestimmten Users
		result = ContactMapper.contactMapper().findAllContactsByUser(user_id);

		//Kontakte loeschen
		for (Contact c : result) {
			deleteContactByID(c.getBo_Id());

		}
	}

	/**
	 * Mapper-Methode zur Rückgabe aller Kontakte eines bestimmten Users
	 * 
	 * @param user_id
	 * @return
	 */

		
		//Vektor zur Speicherung der Contact-Objekte

	public Vector<Contact> findAllContactsByUser(double user_id) {

		Vector<Contact> result = new Vector<Contact>();

		Connection con = DBConnection.connection();

		try {
			//SQL-Statement erzeugen
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * " 
							+ "FROM  Contact AS c " 
							+ "INNER JOIN Contact_PropertyValue bez ON c.ID = bez.Contact_ID "
							+ "INNER JOIN PropertyValue AS pv ON pv.ID = bez.PropertyValue_ID "
							+ "INNER JOIN Property AS p ON p.ID = pv.Property_ID "
							+ "INNER JOIN BusinessObject AS bo ON bo.bo_ID = c.ID "
							//+ "WHERE description = 'Name' AND "
							+ "WHERE bo.user_ID =" + user_id);

			while (rs.next()) {
				Contact contact = new Contact();
				contact.setBo_Id(rs.getInt("ID"));
				contact.setShared_status(rs.getBoolean("status"));
				//contact.setName(PropertyValueMapper.propertyValueMapper().findByKey(rs.getInt("propertyValue_ID")));
				contact.setCreationDate(rs.getTimestamp("creationDate"));
				contact.setModifyDate(rs.getTimestamp("modificationDate"));
				contact.setOwner(UserMapper.userMapper().findById(rs.getDouble("user_ID")));

				System.out.println(contact.toString());
				
				//Hinzufügen zum Ergebnisvektor
				result.addElement(contact);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Rückgabe des Ergebnisvektors
		return result;
	}

	/**
	 * Mapper-Methode um alle Kontakte zu loeschen
	 */
	public void deleteAllContacts() {

		Vector<Contact> result = new Vector<Contact>();
		
		//Aufruf aller Kontakte
		result = ContactMapper.contactMapper().findAllContacts();

		//Loeschen der Kontakte
		for (Contact c : result) {
			deleteContactByID(c.getBo_Id());
		}
	}

	/**
	 * Mapper-Methode um einen Kontakt zu erstellen
	 * TODO: Owner ID überdenken!
	 */

	public void insertContact(Contact contact) {

		
		BusinessObjectMapper.businessObjectMapper().insert(contact);
		PropertyValueMapper.propertyValueMapper().insert(contact.getpropertyValue());

		Connection con = DBConnection.connection();

		try {		
			//SQL-Statement zur Erstellung eines Kontaktes erzeugen
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO Contact (ID) VALUES (" + contact.getBo_Id() + ")");


		} catch (SQLException e) {
			e.printStackTrace();
		}
		

	}

	/**
	 * Mapper-Methode um alle vorhandenen Kontakte zu suchen
	 * 
	 * @return
	 */
	public Vector<Contact> findAllContacts() {
		
		//Vektor zur Speicherung der Contact-Objekte
		Vector<Contact> result = new Vector<Contact>();

		Connection con = DBConnection.connection();

		try {
			//SQL Statement erzeugen
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT *" + "FROM  Contact c " + "INNER JOIN Contact_PropertyValue bez ON c.ID = bez.Contact_ID "
							+ "INNER JOIN PropertyValue pv ON pv.ID = bez.PropertyValue_ID "
							+ "INNER JOIN Property p ON p.ID = pv.Property_ID " + "WHERE description = 'Name'");

			while (rs.next()) {
				Contact contact = new Contact();
				contact.setOwner(UserMapper.userMapper().findById(rs.getDouble("user_ID")));
				contact.setBo_Id(rs.getInt("id"));
				contact.setShared_status(rs.getBoolean("status"));
				contact.setCreationDate(rs.getTimestamp("creationDate"));
				contact.setModifyDate(rs.getTimestamp("modificationDate"));
				contact.setpropertyValue(PropertyValueMapper.propertyValueMapper().findByKey(rs.getInt("propertyValue_ID")));

				//Hinzufügen zum Ergebnisvektor
				result.addElement(contact);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		//Rückgabe des Ergebnisvektors
		return result;
	}

	/**
	 * Mapper-Methode um einen Kontakt über die ID zu suchen.
	 * 
	 * @param id
	 * @return
	 */
	public Contact findContactById(int id) {
		
		Contact contact = new Contact();
		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con.prepareStatement(
					"SELECT Contact.ID "
				  + "FROM Contact "
				  + "WHERE Contact.ID = ?"
					);
			
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {

				
			}
			return contact;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Mapper-Methode um einen Kontakt durch den Namen zu finden
	 * 
	 * @param name
	 * @return
	 */
	public Contact findBy(PropertyValue pV) {
		
		Contact contact = new Contact();
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
							"SELECT PropertyValue.value "
							+ "FROM PropertyValue AS pv INNER JOIN Contact AS c "
							+ "ON pv.contact_ID = c.ID "
							+ "WHERE pv.ID = " + pV.getBo_Id() 
							);
							//Innerjoin um auf den Namen zugreifen zu können

			if (rs.next()) {

				PropertyValue propValue = new PropertyValue();
				propValue = PropertyValueMapper.propertyValueMapper().findByKey(rs.getInt("propertyValue_ID"));
				Property prop = new Property();
				prop = PropertyMapper.propertyMapper().findBy(propValue);
				propValue.setProp(prop);
				contact.setOwner(UserMapper.userMapper().findById(rs.getDouble("user_ID")));
				contact.setBo_Id(rs.getInt("id"));			
				contact.setpropertyValue(propValue);
				

			}
			
			return contact;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Mapper-Methode um einen Kontakt zu bearbeiten
	 * 
	 * @param contact
	 * @return
	 */
	public Contact updateContact(Contact contact) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("UPDATE contact SET status = " + contact.isShared_status() + "SET modificationDate = "
					+ contact.getModifyDate() + "WHERE id = " + contact.getBo_Id());
			
			//TODO: UpdatePropertyValueBYContact Methode?
			PropertyValueMapper.propertyValueMapper().UpdatePropertyValueByContact(contact);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contact;
	}

	/**
	 * 
	 * @param user_id
	 * @param shared_status
	 * @return
	 */

	public Contact findContactByStatus(double user_id, boolean shared_status) {
		Contact contact = new Contact();
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * " + "FROM  Contact c " + "INNER JOIN Contact_PropertyValue bez ON c.ID = bez.Contact_ID "
							+ "INNER JOIN PropertyValue pv ON pv.ID = bez.PropertyValue_ID "
							+ "INNER JOIN Property p ON p.ID = pv.Property_ID "
							+ "INNER JOIN BusinessObject bo ON bo.bo_ID = c.ID "
							+ "WHERE description = 'Name' AND bo.status = " + shared_status);

			if (rs.next()) {
				contact.setOwner(UserMapper.userMapper().findById(rs.getDouble("user_ID")));
				contact.setBo_Id(rs.getInt("id"));
				contact.setShared_status(rs.getBoolean("status"));
				contact.setCreationDate(rs.getTimestamp("creationDate"));
				contact.setModifyDate(rs.getTimestamp("modificationDate"));
				contact.setpropertyValue(PropertyValueMapper.propertyValueMapper().findByKey(rs.getInt("propertyValue_ID")));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return contact;
	}

}