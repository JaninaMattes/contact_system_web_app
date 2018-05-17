package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
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
	 * Alle Kontaktlisten finden
	 * 
	 * @return contaktList
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
				cl.setBo_Id(rs.getInt("ID"));
				cl.setName(rs.getString("contactList_name"));
				cl.setOwner(UserMapper.userMapper().findUserById(rs.getInt("user_ID")));
				cl.setShared_status(rs.getBoolean("status"));
				findContactFromList(cl);
				contactList.add(cl);

			}
			return contactList;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Einen Kontakt aus aus einer Liste finden.
	 * 
	 * @return
	 */

	public void findContactFromList(ContactList cl) {
		Connection con = DBConnection.connection();
		try {
			Vector<Contact> c = new Vector<Contact>();

			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT Contact_ID from Contact_ContactList where ContactList_ID = " + cl.getBo_Id());
			while (rs.next()) {
				c.add(ContactMapper.contactMapper().findContactById(rs.getInt("Contact_ID")));

			}
			cl.setContacts(c);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Eine Kontaktliste �ber ihre ID finden
	 * 
	 * @param id
	 * @return ContactList
	 */

	public ContactList findContactListById(int id) {
		
		Connection con = DBConnection.connection();

		try {

			PreparedStatement stmt = con.prepareStatement(
					"SELECT * FROM ContactList LEFT JOIN BusinessObject ON ContactList.ID = BusinessObject.bo_ID  WHERE id = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				ContactList cl = new ContactList();
				cl.setBo_Id(rs.getInt("ID"));
				cl.setName(rs.getString("contactList_name"));
				cl.setOwner(UserMapper.userMapper().findUserById(rs.getInt("user_ID")));
				cl.setShared_status(rs.getBoolean("status"));
				return cl;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

		
	}

	
	/**
	 * Alle Kontaktlisten eines Users finden.
	 * 
	 * @param user
	 * @return
	 */

	public Vector<ContactList> findContactListByUser(User user) {
		Vector<ContactList> cll = new Vector<ContactList>();
		Connection con = DBConnection.connection();
		try {
			PreparedStatement stmt = con.prepareStatement(
					"SELECT * FROM ContactList LEFT JOIN BusinessObject ON ContactList.ID = BusinessObject.bo_ID  WHERE user_ID = ?");
			stmt.setInt(1, user.getGoogleID());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ContactList cl = new ContactList();
				cl.setBo_Id(rs.getInt("ID"));
				cl.setName(rs.getString("contactList_name"));
				cl.setOwner(UserMapper.userMapper().findUserById(rs.getInt("user_ID")));
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
	 * Kontaktlisten durch Namensssuche finden.
	 * 
	 * @param name
	 * @return
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
				cl.setBo_Id(rs.getInt("ID"));
				cl.setName(rs.getString("contactList_name"));
				cl.setOwner(UserMapper.userMapper().findUserById(rs.getInt("user_ID")));
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
	 * Name der Liste ver�ndern.
	 * 
	 * @param cl
	 */

	public void updateContactList(ContactList cl) {
		BusinessObjectMapper.businessObjectMapper().update(cl);
		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con.prepareStatement("UPDATE ContactList SET contactList_name = ? WHERE ID = ?");
			stmt.setString(1, cl.getName());
			stmt.setInt(2, cl.getBo_Id());

			stmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Eine Kontaktliste erstellen.
	 * 
	 * @param cl
	 */

	public void insertContactList(ContactList cl) {
		BusinessObjectMapper.businessObjectMapper().insert(cl);
		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con
					.prepareStatement("INSERT INTO ContactList (ID, contactList_name) VALUES (?, ?)");
			stmt.setInt(1, cl.getBo_Id());
			stmt.setString(2, cl.getName());
			stmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Eine Kontaktliste l�schen, mit der �bergebenden ID.
	 * 
	 * @param id
	 */

	public void deleteContactListById(int id) {
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM ContactList Where ID = " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Es l�scht alle Kontakte, welche zu einer userID geh�ren.
	 * 
	 * @param userId
	 */

	public void deleteContactListByUserId(int userId) {
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("DELETE cl.*, bo.* FROM ContactList cl "
					+ "INNER JOIN BusinessObject bo ON cl.ID = bo.bo_ID Where bo.user_ID =" + userId);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Alle Kontaktlisten l�schen.
	 */
	public void deleteAllContactList() {
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE * FROM ContactList");

		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Einen Kontakt zur Kontaktliste hinzuf�gen.
	 * 
	 * @param cl
	 * @param c
	 */

	public void addContactToContactlist(ContactList cl, Contact c) {

		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con
					.prepareStatement("INSERT INTO Contact_ContactList (Contact_ID, ContactList_ID) VALUES (?, ?)");
			stmt.setInt(1, c.getBo_Id());
			stmt.setInt(2, cl.getBo_Id());
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Einen Kontakt aus der Kontaktliste entfernen.
	 * 
	 * @param cl
	 * @param c
	 */

	public void removeContactFromContactList(ContactList cl, Contact c) {
		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con
					.prepareStatement("DELETE FROM Contact_ContactList WHERE Contact_ID = ? AND ContactList_ID = ?");
			stmt.setInt(1, c.getBo_Id());
			stmt.setInt(2, cl.getBo_Id());
			stmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
