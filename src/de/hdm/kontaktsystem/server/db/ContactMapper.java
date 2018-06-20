package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.sql.ResultSet;

import de.hdm.kontaktsystem.server.ContactSystemAdministrationImpl;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;

//import com.mysql.jdbc.Connection;

import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
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

	/**
	 * Gibt nach dem Singelton Pattern eine Instanz des ContactMppers zurück
	 * @return ContactMapper-Objekt
	 */
	public static ContactMapper contactMapper() {
		if (contactMapper == null) {
			contactMapper = new ContactMapper();
		}
		return contactMapper;
	}
	

	
	/**
	 * Mapper-Methode um einen Kontakt zu erstellen und
	 * das Contact-Objekt in der DB einzufuegen. 
	 * 
	 * @param contact
	 * @return Contact
	 */

	public Contact insertContact(Contact contact) {
		
		
		Connection con = DBConnection.connection();

		try {		
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Contact (ID) VALUES ( ? )");				  
			stmt.setInt(1, contact.getBoId());
			if(stmt.executeUpdate() > 0) return contact;	

		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	
	/**
	 * Mapper-Methode zur Rückgabe aller Kontakte eines bestimmten Users.
	 * 
	 * @param User Id
	 * @return Vector mit alle Contact-Objekte  eines Users
	 */
		
	public Vector<Contact> findAllContactsByUser(double user_id) {

		Vector<Contact> result = new Vector<Contact>();
		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con.prepareStatement(
					  "SELECT * " 
					+ "FROM  Contact AS c " 
					+ "INNER JOIN BusinessObject AS bo ON bo.bo_ID = c.ID "
					+ "WHERE bo.user_ID = ?"
					);
			
			stmt.setDouble(1, user_id);
			ResultSet rs = stmt.executeQuery();							  

			while (rs.next()) {
				Contact contact = new Contact();
				User u = new User();
				u.setGoogleID(rs.getDouble("user_ID"));
				contact.setOwner(u);
				contact.setBo_Id(rs.getInt("ID"));
				contact.setShared_status(rs.getBoolean("status"));
				contact.setCreationDate(rs.getTimestamp("creationDate"));
				contact.setModifyDate(rs.getTimestamp("modificationDate"));
				result.addElement(contact);
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	

	/**
	 * Mapper-Methode um alle vorhandenen Kontakte zu suchen.
	 * Den Kontakt - Objekten wird ein PropertyValue-Objekt zugewiesen.
	 * Einem Kontakt wird dabei zusätzlich das zugehörige PropertyValue - Objekt
	 * mit der Ausprägung der Eigenschaft "Name" gesetzt. 
	 * 
	 * @return Vector <Contact>
	 */
	
	public Vector <Contact> findAllContacts() {
		
		Vector<Contact> result = new Vector<Contact>();
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT c.* , bo.* " 
				  + "FROM  Contact c "
				  + "INNER JOIN BusinessObject bo ON bo.bo_ID = c.ID "
				  );

			while (rs.next()) {
				Contact contact = new Contact();
				User u = new User();
				u.setGoogleID(rs.getDouble("user_ID"));
				contact.setOwner(u);
				contact.setBo_Id(rs.getInt("ID"));
				contact.setShared_status(rs.getBoolean("status"));
				contact.setCreationDate(rs.getTimestamp("creationDate"));
				contact.setModifyDate(rs.getTimestamp("modificationDate"));
				result.addElement(contact);
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * Mapper-Methode um einen Kontakt über die ID zu suchen.
	 * 
	 * @param Contact ID
	 * @return Contact-Objekt
	 */
	
	public Contact findContactById(int id) {	
		Connection con = DBConnection.connection();
			
		try {			
			PreparedStatement stmt = con.prepareStatement(
					  "SELECT * " 
					+ "FROM  Contact c "
					+ "INNER JOIN BusinessObject bo ON bo.bo_ID = c.ID "
//					+ "INNER JOIN User u ON u.ID = bo.user_ID "
//					+ "INNER JOIN PropertyValue pv ON pv.contact_ID = c.ID "
//					+ "INNER JOIN Property p ON p.ID = pv.property_ID "
					+ "WHERE c.ID = ? "); 
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {						
				Contact contact = new Contact();
				User u = new User();
				u.setGoogleID(rs.getDouble("user_ID"));
				contact.setOwner(u);
				contact.setBo_Id(rs.getInt("bo_ID"));
				contact.setShared_status(rs.getBoolean("status"));
				contact.setCreationDate(rs.getTimestamp("creationDate"));
				contact.setModifyDate(rs.getTimestamp("modificationDate"));
				

				return contact;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	
	
	/**
	 * Fügte dem ContactList-Objekt einen Vektor mit allen enthaltenen Contact-Objekten hinzu
	 * 
	 * @param ContactList-Objekt
	 */

	public Vector<Integer> findContactFromList(ContactList cl) {
		Connection con = DBConnection.connection();
		try {
			Vector<Integer> c = new Vector<Integer>();

			PreparedStatement stmt = con.prepareStatement(
					"SELECT Contact_ID from Contact_ContactList where ContactList_ID = ?"
					 );
			stmt.setInt(1, cl.getBoId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				c.add(rs.getInt("Contact_ID"));				
			}
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	/**
	 * Mapper-Methode um einen Kontakt nach einer Eigenschaft zu finden
	 * 
	 * @param PropertyValue
	 * @return Contact
	 */
	
	public Contact findBy(PropertyValue pV) {
		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con.prepareStatement(
					  "SELECT c.*, pv.*, bo.* " 
					+ "FROM  Contact c " 
					+ "INNER JOIN PropertyValue pv ON pv.contact_ID = c.ID "
					+ "INNER JOIN BusinessObject bo ON bo.bo_ID = c.ID "
					+ "WHERE pv.ID = ?");
			
			stmt.setInt(1, pV.getBoId());
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				Contact contact = new Contact();
				User u = new User();
				u.setGoogleID(rs.getDouble("user_ID"));
				contact.setOwner(u);
				contact.setBo_Id(rs.getInt("bo.bo_ID"));
				contact.setShared_status(rs.getBoolean("bo.status"));
				contact.setCreationDate(rs.getTimestamp("bo.creationDate"));
				contact.setModifyDate(rs.getTimestamp("bo.modificationDate"));
				
				return contact;
			}					
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Methode um alle geteileten oder nicht geteilten Kontakte zu finden.
	 * 
	 * @param user_id
	 * @param shared_status
	 * @return Contact-Objekt
	 */

	public Vector <Contact> findContactByStatus(double user_id, boolean shared_status) {
		Vector <Contact> contactResult = new Vector <Contact>();
		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con.prepareStatement( 
					  "SELECT * "
					+ "FROM  Contact c " 
					+ "INNER JOIN BusinessObject bo ON bo.bo_ID = c.ID "
					+ "WHERE bo.user_ID = ?"
					+ "AND bo.status = ?"
					);
			stmt.setDouble(1, user_id);
			stmt.setBoolean(2, shared_status);
			ResultSet rs = stmt.executeQuery();					 

			while (rs.next()) {
				Contact contact = new Contact();
				User u = new User();
				u.setGoogleID(rs.getDouble("user_ID"));
				contact.setOwner(u);
				contact.setBo_Id(rs.getInt("id"));
				contact.setShared_status(rs.getBoolean("status"));
				contact.setCreationDate(rs.getTimestamp("creationDate"));
				contact.setModifyDate(rs.getTimestamp("modificationDate"));
				contactResult.addElement(contact);
			}
			return contactResult;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	public Vector<Contact> findAllContactsByUser(User user) {
		
		return findAllContactsByUser(user.getGoogleID());
	}
	

	/**
	 * Mapper-Methode um einen Kontakt zu bearbeiten
	 * 
	 * @param Contact-Objekt
	 * @return Contact-Objekt
	 */
	
	public Contact updateContact(Contact contact) {
		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con.prepareStatement("UPDATE Contact SET ID = ? WHERE ID= ?");			
			stmt.setInt(1, contact.getBoId());
			stmt.setInt(2, contact.getBoId());
			stmt.execute();
					
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contact;
	}


	
	
	
	/**
	 * Mapper-Methode um alle Kontakte zu loeschen
	 * 
	 * @note  Nicht verwendet
	 */
	
	public void deleteAllContacts() {

		Vector<Contact> result = new Vector<Contact>();
		result = ContactMapper.contactMapper().findAllContacts();

		for (Contact c : result) {
			this.deleteContactByID(c.getBoId());
		}
	}
	

	/**
	 * Mapper-Methode um einen Kontakt zu loeschen
	 * 
	 * @param Contact-Objekt
	 * @return gelöschtes Contact-Objekt
	 */
	
	public Contact deleteContact(Contact contact) {
		if(deleteContactByID(contact.getBoId()) > 0) return contact;
		else return null;
	}

	/**
	 * Mapper-Methode um einen Kontakt mit Hilfe der ID zu loeschen
	 * 
	 * @param Contact ID
	 * @return Anzahl gelöschter Elemente
	 */
	
	public int deleteContactByID(int id) {
		int i = 0; // Anzahl der gelöschten Reihen
		Connection con = DBConnection.connection();
		try {
			PreparedStatement stmt = con.prepareStatement("DELETE FROM Contact WHERE ID = ?" );
			stmt.setInt(1, id);
			i = stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return i;
	}
	

	

	/**

	 * Den eigenen Kontakt eines Users, welcher bei dessen Erzeugung in 
	 * der DB erstellt wird abgerufen. 
	 * 
	 * Wirn nur Verwendet um dem User-Objekt seine Contact-Objekt zu zuweisen.
	 * Wird benötigt um Schleife zwischen SetOwner in <code> Contact </code> und SetContact in <code> User </code> aufzulösen
	 * 
	 * @param User-Objekt
	 * @param Contact ID
	 */
	

	public Contact findOwnContact(User owner) {
		Connection con = DBConnection.connection();
		try {
			PreparedStatement stmt = con.prepareStatement(
					  "SELECT c.* , bo.* " 
					+ "FROM  Contact c "
					+ "INNER JOIN BusinessObject bo ON bo.bo_ID = c.ID " 
					+ "WHERE c.ID = ?");

			stmt.setInt(1, owner.getUserContact().getBoId());
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {	
				Contact contact = new Contact();				
				contact.setBo_Id(rs.getInt("bo.bo_ID"));
				contact.setShared_status(rs.getBoolean("bo.status"));
				contact.setCreationDate(rs.getTimestamp("bo.creationDate"));
				contact.setModifyDate(rs.getTimestamp("bo.modificationDate"));
				contact.setOwner(owner);
				return contact;				
			}			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return null;
	}


}