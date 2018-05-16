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

	/**
	 * Eine Kontaktliste �ber ihre ID finden
	 * 
	 * @param id
	 * @return ContactList
	 */

	public ContactList findContactListById(int id) {
		ContactList cl = new ContactList();
		Connection con = DBConnection.connection();

		try {
			
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM ContactList WHERE id = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				cl.setBo_Id(rs.getInt("ID"));
				cl.setName(rs.getString("contactList_name"));
				cl.setOwner(UserMapper.userMapper().getUserById(rs.getInt("owner")));
				cl.setShared_status(rs.getBoolean("status"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cl;
	}
	
	//NOCHMAL PR�FEN!
	public Vector<ContactList> findContactListByUser(User user) {

		Connection con = DBConnection.connection();
		try{
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM ContactList WHERE User = ?");
			stmt.setString(User, user);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				ContactList cl = new ContactList();
				cl.setBo_Id(rs.getInt("ID"));
				cl.setName(rs.getString("contactList_name"));
				cl.setOwner(UserMapper.userMapper().getUserById(rs.getInt("owner")));
				cl.setShared_status(rs.getBoolean("status"));
				return cl;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}

	//NOCHMAL PR�FEN!
	public Vector<ContactList> findContactListByName(String name) {
		Connection con = DBConnection.connection();
		try{
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM ContactList WHERE name = ?");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				ContactList cl = new ContactList();
				//cl.setBo_Id(rs.getInt("ID"));
				cl.setName(rs.getString("contactList_name"));
				cl.setOwner(UserMapper.userMapper().getUserById(rs.getInt("owner")));
				//cl.setShared_status(rs.getBoolean("status"));
				return cl;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}
	public void updateContactList(ContactList cl) {
		// updateCl Mapper
	}

	public void insertContactList(ContactList cl) {
		Connection con = DBConnection.connection();
		try{
			PreparedStatement stmt = con.prepareStatement("INSERT INTO ContactList (ID, ContactList_ID) VALUES (?, ?)");
			stmt.setInt(1, cl.getBo_Id());
			stmt.setString(2, cl.getName());
			stmt.setString(3, cl.getOwner()); //Zum Nachpr�fen
			stmt.execute();
			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	public void deleteContactListById(int id) {
		Connection con = DBConnection.connection();
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM ContactList Where ConactList_ID = "+ id);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	
	
	
	public void deleteContactListByUser(User user) {
		Connection con = DBConnection.connection();
		try{
			Statement stmt = con.createStatement();
			//SQL BEFEHL NOCHMAL PR�FEN
			stmt.executeUpdate("DELETE FROM ContactList Where ContactList_User = "+ user);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	
	
	
	public void deleteAllContactList() {
		Connection con = DBConnection.connection();
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE * FROM ContactList");
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	public void initContactListTable() {
		Connection con = DBConnection.connection();
		Statement stmt;
		try {
			stmt = con.createStatement();
			//SQL BEFEHL NOCHMAL NACHPR�FEN
			stmt.executeUpdate("CREATE TABLE ContactList (ID INT(10) NOT NULL, Name VARCHAR(255) NOT NULL, PRIMARY KEY(ID));");
		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void deleteContactListTable() {
		Connection con = DBConnection.connection();
		Statement stmt;

		try {
			stmt = con.createStatement();
			stmt.executeUpdate("DROP TABLE ContactList");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	//METHODE KL�REN
	public ContactListMapper findInstance() {
		// findInstance Mapper
		return null;
	}

}
