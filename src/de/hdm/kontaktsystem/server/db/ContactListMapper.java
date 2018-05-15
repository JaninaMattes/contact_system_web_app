package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
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
				cl.setOwner(UserMapper.userMapper().getUserById(rs.getInt("owner")));
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
				c.add(ContactMapper.contactMapper().findContactById(rs.getInt("ID")));

			}
			cl.setContacts(c);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public ContactList findContactListById(int id) {
		// getId Mapper
		return null;
	}

	public Vector<ContactList> findContactListByUser(User user) {
		// getUser Mapper
		return null;
	}

	public Vector<ContactList> findContactListByName(String name) {
		// getName Mapper
		return null;
	}

	public void updateContactList(ContactList cl) {
		// updateCl Mapper
	}

	public void insertContactList(ContactList cl) {
		// insertCl Mapper
	}

	public void deleteContactListById(int id) {
		// deleteId Mapper
	}

	public void deleteContactListByUser(User user) {
		// deleteUser Mapper
	}

	public void deleteAllContactList() {
		// deleteAll Mapper
	}

	public void initContactListTable() {
		// initTable Mapper
	}

	public void deleteContactListTable() {
		// deleteTable Mapper
	}

	public ContactListMapper findInstance() {
		// findInstance Mapper
		return null;
	}

}
