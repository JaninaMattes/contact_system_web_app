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
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
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
				cl.setOwner(UserMapper.userMapper().findById(rs.getDouble("user_ID")));
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
				cl.setOwner(UserMapper.userMapper().findById(rs.getDouble("user_ID")));
				cl.setShared_status(rs.getBoolean("status"));
				return cl;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

		
	}

	public Vector<ContactList> findContactListByUser(User user) {
		return findContactListByUser(user.getGoogleID());
	}
	/**
	 * Alle Kontaktlisten eines Users finden.
	 * 
	 * @param user
	 * @return
	 */

	public Vector<ContactList> findContactListByUser(double userID) {
		Vector<ContactList> cll = new Vector<ContactList>();
		Connection con = DBConnection.connection();
		try {
			PreparedStatement stmt = con.prepareStatement(
					"SELECT * FROM ContactList LEFT JOIN BusinessObject ON ContactList.ID = BusinessObject.bo_ID  WHERE user_ID = ?");
			stmt.setDouble(1, userID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ContactList cl = new ContactList();
				cl.setBo_Id(rs.getInt("ID"));
				cl.setName(rs.getString("contactList_name"));
				cl.setOwner(UserMapper.userMapper().findById(rs.getDouble("user_ID")));
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
				cl.setOwner(UserMapper.userMapper().findById(rs.getDouble("user_ID")));
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
	 *  Alle fuer den Benutzer in der Applikation zugaenglichen Kontaktlisten <code>ContactList</code> - Objekte
	 * (diese sind selbst erstellt und anderen zur Teilhaberschaft freigegeben) werden anhand ihres Status gesucht
	 *  und die Ergebnisse zurueckgegeben
	 */

	public Vector<ContactList> findAllSharedByMe (User user) {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln, die von diesem geteilt wurden
				Vector<Participation> participationVector = new Vector<Participation>();		
				participationVector = ParticipationMapper.participationMapper().findParticipationsByOwner(user);
				// Vector für die Speicherung aller BusinessObjekte erzeugen
				Vector<ContactList> contactListVector = new Vector <ContactList>(); 		
				//System.out.println(participationVector);
				
				for (Participation part : participationVector) { 		
					
					//System.out.println(part);
					 BusinessObject bo = BusinessObjectMapper.businessObjectMapper().findBusinessObjectByID(part.getReferenceID());
					 //System.out.println(bo);
					 ContactList contactList = new ContactList();	 
					 //System.out.println(propVal); 	
					 if(bo instanceof ContactList) {			 		
						 contactList = (ContactList) bo;
					 		//System.out.println("Ausprägung " + propVal.getProp());
						 contactListVector.addElement(contactList);		     
					 }
				}
				return contactListVector;
				
			}
	
	public Vector<ContactList> findAllSharedByOthersToMe (User user) {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln, die von diesem geteilt wurden
		Vector<Participation> participationVector = new Vector<Participation>();		
		participationVector = ParticipationMapper.participationMapper().findParticipationsByParticipant(user);
//		for (Participation part : participationVector) {
//			System.out.println(part);
//		}

		// Vector für die Speicherung aller BusinessObjekte erzeugen
		Vector<ContactList> contactListResultVector = new Vector <ContactList>(); 
		
		for (Participation part : participationVector) {
			ContactList cl = new ContactList();
			 BusinessObject bo = BusinessObjectMapper.businessObjectMapper().findBusinessObjectByID(part.getReferenceID());	
			 //System.out.println(part);		     
			 //System.out.println(propVal);
			 //System.out.println("pov-id: " + propVal.getBo_Id());		     
			 if(bo instanceof ContactList) {			 		
				 cl = (ContactList) bo;
			 		//System.out.println("Ausprägung " + propVal.getProp());
				 contactListResultVector.addElement(cl);		     
			 }
		}
		return contactListResultVector;
		
	}

	
	/**
	 * Die Methode erm�glicht das l�schen aller f�r einen User geteilten <code>ContactListen</code>, 
	 * sowie deren enthaltene Kontakte <code>Contact</code> und deren PropertyValue-Objekte
	 */
	
	public void deleteAllSharedForMe() {
		
	}
	
	/**
	 * Funktion zum l�schen aller geteilten ContactListe - Objekte
	 * @param user 
	 * 
	 */

	public void deleteAllSharedByMe() {

				

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
		
		System.out.println("Create ContactList: " + cl);
		
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

	public void deleteContactList(ContactList cl) {
		deleteContactListById(cl.getBo_Id());
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
		BusinessObjectMapper.businessObjectMapper().deleteBusinessObjectByID(id);
	}

	/**
	 * Es l�scht alle Kontakte, welche zu einer userID geh�ren.
	 * 
	 * @param userId
	 */

	public void deleteContactListByUserId(Double userId) {
		
		for(ContactList cl : findContactListByUser(userId)){
			this.deleteContactListById(cl.getBo_Id());
		}
	}

	/**
	 * Alle Kontaktlisten l�schen.
	 */
	public void deleteAllContactList() {
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE cl.*, bo.* FROM ContactList cl "
					+ "INNER JOIN BusinessObject bo "
					+ "WHERE cl.ID = bo.bo_ID");

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
