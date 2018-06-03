package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.User;

public class ContactListMapper {

	/**
	 * Singleton Pattern
	 */

	private static ContactListMapper contactListMapper = null;

	protected ContactListMapper() {

	}

	public static ContactListMapper contactListMapper() {
		if (contactListMapper == null) {
			contactListMapper = new ContactListMapper();
		}

		return contactListMapper;
	}

	/**
	 * Eine Kontaktliste erstellen.
	 * 
	 * @param ContactList-Objekt
	 * @return ContactList-Objekt
	 */

	public ContactList insertContactList(ContactList cl) {
		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con
					.prepareStatement
					("INSERT INTO ContactList (ID, contactList_name) VALUES (?, ?)");
			stmt.setInt(1, cl.getBoId());
			stmt.setString(2, cl.getName());
			if(stmt.executeUpdate() > 0) return cl;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	/**
	 * Alle Kontaktlisten finden
	 * 
	 * @return ContactList-Objekt
	 */

	public Vector<ContactList> findAllContactLists() {

		Connection con = DBConnection.connection();
		try {
			Vector<ContactList> contactList = new Vector<ContactList>();

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM ContactList LEFT JOIN BusinessObject ON ContactList.ID = BusinessObject.bo_ID");
			while (rs.next()) {
				ContactList cl = new ContactList();
				User owner = new User();
				owner.setGoogleID(rs.getDouble("user_ID"));
				cl.setOwner(owner);
				cl.setBo_Id(rs.getInt("ID"));
				cl.setName(rs.getString("contactList_name"));
				cl.setShared_status(rs.getBoolean("status"));
				contactList.add(cl);

			}
			return contactList;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	

	/**
	 * Eine Kontaktliste über ihre ID finden
	 * 
	 * @param ContactList ID
	 * @return ContactList
	 */

	public ContactList findContactListById(int id) {
		
		Connection con = DBConnection.connection();

		try {

			PreparedStatement stmt = con.prepareStatement(
					"SELECT * FROM ContactList "
					+ "LEFT JOIN BusinessObject ON ContactList.ID = BusinessObject.bo_ID  "
					+ "WHERE id = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				ContactList cl = new ContactList();
				User owner = new User();
				owner.setGoogleID(rs.getDouble("user_ID"));
				cl.setOwner(owner);
				cl.setBo_Id(rs.getInt("ID"));
				cl.setName(rs.getString("contactList_name"));
				cl.setShared_status(rs.getBoolean("status"));
				return cl;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

		
	}

	/**
	 * Gibt einen Vektor mit allen allen ContactList Objekten zurück, welche zu einem User gehören
	 * @param User-Objekt
	 * @return Vector<ContactList>
	 */
	
	public Vector<ContactList> findContactListByUser(User user) {
		return findContactListByUserId(user.getGoogleID());
	}
	/**
	 * Gibt einen Vektor mit allen allen ContactList Objekten zurück, welche zu einem User gehören
	 * 
	 * @param User ID
	 * @return Vector<ContactList>
	 */

	public Vector<ContactList> findContactListByUserId(double userID) {
		Vector<ContactList> cll = new Vector<ContactList>();
		Connection con = DBConnection.connection();
		try {
			PreparedStatement stmt = con.prepareStatement(
					"SELECT * FROM ContactList LEFT JOIN BusinessObject ON ContactList.ID = BusinessObject.bo_ID  WHERE user_ID = ?");
			stmt.setDouble(1, userID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ContactList cl = new ContactList();
				User owner = new User();
				owner.setGoogleID(rs.getDouble("user_ID"));
				cl.setOwner(owner);
				cl.setBo_Id(rs.getInt("ID"));
				cl.setName(rs.getString("contactList_name"));
				cl.setShared_status(rs.getBoolean("status"));
				cll.add(cl);
			}
			return cll;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cll;
	}
	
	/**
	 * Kontaktlisten anhand des Names finden.
	 * 
	 * @param String (Listen Name)
	 * @return Vector<ContactList>
	 */

	public Vector<ContactList> findContactListByName(String name) {
		Vector<ContactList> cll = new Vector<ContactList>();
		Connection con = DBConnection.connection();
		try {
			PreparedStatement stmt = con.prepareStatement(
					"SELECT * FROM ContactList LEFT JOIN BusinessObject ON ContactList.ID = BusinessObject.bo_ID WHERE contactList_name = ?");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ContactList cl = new ContactList();
				User owner = new User();
				owner.setGoogleID(rs.getDouble("user_ID"));
				cl.setOwner(owner);
				cl.setBo_Id(rs.getInt("ID"));
				cl.setName(rs.getString("contactList_name"));
				cl.setShared_status(rs.getBoolean("status"));
				cll.add(cl);
			}
			return cll;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cll;
	}
	
	

	/**
	 * Updatet einen eintrag in der ContactList Tabelle
	 * verwendet zur Namensänderung
	 * 
	 * @param ContactList-Objekt
	 */

	public ContactList updateContactList(ContactList cl) {
		
		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con.prepareStatement("UPDATE ContactList SET contactList_name = ? WHERE ID = ?");
			stmt.setString(1, cl.getName());
			stmt.setInt(2, cl.getBoId());
			stmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
	/**
	 * Löscht einen eintrag aus der ContactList Tabelle
	 * @param ContactList-Objekt
	 * @return ContactList-Objekt
	 */
	
	public ContactList deleteContactList(ContactList cl) {
		if(deleteContactListById(cl.getBoId()) > 0) return cl;
		else return null;
	}
	
	
	/**
	 * Eine Kontaktliste löschen, mit der übergebenden ID.
	 * 
	 * @param ContactList ID
	 * @return Anzahl der gelöschten Zeilen
	 */

	public int deleteContactListById(int id) {
		Connection con = DBConnection.connection();
		int i = 0; // Anzahl der gelöschten Zeilen
		try {
			PreparedStatement stmt = con.prepareStatement("DELETE FROM ContactList Where ID = ?");
			stmt.setInt(1, id);
			i = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	

	
	/**
	 * Alle Kontaktlisten löschen.
	 * @note Nicht genutzt
	 * 
	 */
	
	public void deleteAllContactLists() {
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE cl.*, bo.* FROM ContactList cl "
					+ "INNER JOIN BusinessObject bo "
					+ "WHERE cl.ID = bo.bo_ID");

		}catch(SQLException e){
			e.printStackTrace();
		}
		// ?????
		Vector<ContactList> clList = new Vector <ContactList>();
		clList = this.findAllContactLists();
		for(ContactList cl : clList) {
			BusinessObjectMapper.businessObjectMapper().deleteBusinessObjectByID(cl.getBoId());
		}
	}

	/**
	 * Einen Kontakt zur Kontaktliste hinzufügen.
	 * 
	 * @param ContactList-Objekt
	 * @param Contact-Objekt
	 */

	public ContactList addContactToContactlist(ContactList cl, Contact c) {

		Connection con = DBConnection.connection();
		try {
			PreparedStatement stmt = con
					.prepareStatement("INSERT INTO Contact_ContactList (Contact_ID, ContactList_ID) VALUES (?, ?)");
			stmt.setInt(1, c.getBoId());
			stmt.setInt(2, cl.getBoId());
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	/**
	 * Einen Kontakt aus der Kontaktliste entfernen.
	 * 
	 * @param ContactList-Objekt
	 * @param Contact-Objekt
	 */

	public ContactList removeContactFromContactList(ContactList cl, Contact c) {
		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con
					.prepareStatement("DELETE FROM Contact_ContactList WHERE Contact_ID = ? AND ContactList_ID = ?");
			stmt.setInt(1, c.getBoId());
			stmt.setInt(2, cl.getBoId());
			stmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;

	}

}
