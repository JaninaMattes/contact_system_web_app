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
	 * Eine Kontaktliste über ihre ID finden
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
			stmt.setDouble(1, user.getGoogleID());
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
	 * Alle Konaktlisten eines Users finden anhand seiner User-ID
	 * 
	 * @param user_id
	 * @return ContactList Vector
	 */

	public Vector <ContactList> findContactListByUserID(double user_id){
		User u = new User();
		u = UserMapper.userMapper().findById(user_id);
		return this.findContactListByUser(u);
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
	 *  Alle für den Benutzer in der Applikation zugaenglichen Kontakte <code>Contact</code> - Objekte
	 * (diese sind selbst erstellt und anderen zur Teilhaberschaft freigegeben) werden anhand ihres Status gesucht
	 *  und als ein Ergebnissvektor aus Contact-objekten zurueckgegeben. 
	 */

	public Vector<ContactList> findAllSharedByMe (User user) {

		Vector<Participation> participationVector = new Vector<Participation>();		
		participationVector = ParticipationMapper.participationMapper().findParticipationsByOwner(user);
		
		Vector<ContactList> clResultVector = new Vector <ContactList>(); 		
				
		for (Participation part : participationVector) {
			 System.out.println("part id:" + part.getReferenceID());
			 
			 BusinessObject bo = BusinessObjectMapper.businessObjectMapper().findBusinessObjectByID(part.getReferenceID());
			 ContactList cl = new ContactList();
			    System.out.println(bo.getClass());
			 	
			    if(bo instanceof ContactList) {			 		
			 		cl = (ContactList) bo;
			 		System.out.println("contactList id: " + cl.getBo_Id());
			 		clResultVector.addElement(cl);		     
			 }		
		}	 	
		if(clResultVector.isEmpty()) System.out.println("# no contacts found");			
			
		return clResultVector;
		
	}
	
	
	/**
	 * Alle für den Benutzer in der Applikation geteilte Kontaktelisten <code>ContactList</code> -Objekte
	 * künnen über den Aufruf dieser Methode aus der DB zur�ck gegeben werden.
	 * 
	 * @param user-Objekt
	 * @return Vector ContactList-Objekte
	 */

	public Vector<ContactList> findAllSharedByOthersToMe (User user) {

		Vector<Participation> participationVector = new Vector<Participation>();		
		participationVector = ParticipationMapper.participationMapper().findParticipationsByParticipant(user);
		Vector<ContactList> clResultVector = new Vector <ContactList>(); 		
		
		for (Participation part : participationVector) {
			 System.out.println("part id:" + part.getReferenceID());	
			 
			 BusinessObject bo = BusinessObjectMapper.businessObjectMapper().findBusinessObjectByID(part.getReferenceID());
			 System.out.println(bo.getClass());
			 ContactList cl = new ContactList();			 
			 System.out.println("bo gefunden: " + bo.getBo_Id());
			 
			 	if(bo instanceof ContactList) {			 		
			 		cl = (ContactList) bo;
			 		System.out.println("contactList name " + bo);
			 		clResultVector.addElement(cl);
			 	}
			}
		
		if(clResultVector.isEmpty()) System.out.println("# no contacts found");	
		
		return clResultVector;		
	}
	
	
	   
		/**
		 * Methode zur Löschung aller von einem User erstellten Kontaktlisten <code>ContactList</code> -Objekte,
		 * welche im System mit anderen Nutzern geteilt wurden. 
		 * @param user
		 */
		
		public void deleteAllSharedByMe(User user) {
			
			Vector <ContactList> clResult = new Vector <ContactList>();
			clResult = this.findAllSharedByMe(user);
			
			for(ContactList cl : clResult) {
				ParticipationMapper.participationMapper().deleteParticipationForBusinessObject(cl);
				this.deleteContactListById(cl.getBo_Id());
				System.out.println("# shared contact deleted: " + cl.getBo_Id() );
			}
		}
		
		/**
		 * Eine Methode zur Löschung aller Verbindungen in der Participation Tabelle der DB.
		 * Dies führt dazu, dass die für einen Nutzer geteilten Objekte nicht mehr aufgerufen werden können.
		 * Die Teilhaberschaft ist damit beendet. 
		 * 
		 */
		
		public void deleteAllSharedByOthersToMe(User user) {
			
			Vector <ContactList> clResult = new Vector <ContactList>();
			clResult = this.findAllSharedByOthersToMe(user);		

			for(ContactList cl : clResult) {
				ParticipationMapper.participationMapper().deleteParticipationForParticipant(user);
				System.out.println("# participation for contact deleted: " + cl.getBo_Id() );
			}
		}	
	

	/**
	 * Name der Liste verändern.
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
			System.out.println("Create ContactList: " + cl);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Eine Kontaktliste löschen, mit der übergebenden ID.
	 * 
	 * @param id
	 */

	public void deleteContactListById(int id) {
		Connection con = DBConnection.connection();
		try {
			PreparedStatement stmt = con.prepareStatement("DELETE FROM ContactList Where ID = ?");
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		BusinessObjectMapper.businessObjectMapper().deleteBusinessObjectByID(id);
	}
	
	
	/**
	 *  Eine Kontaktliste löschen anhand deren übergebenen ContactList-Objekts
	 *  
	 *  @param cl
	 */
	
	public void delete(ContactList cl) {
		this.deleteContactListById(cl.getBo_Id());
	}

	
	/**
	 * Es löscht alle Kontakte, welche zu einer userID gehören.
	 * 
	 * @param userId
	 */

	public void deleteContactListByUserId(Double userId) {
		Connection con = DBConnection.connection();
		try {
			PreparedStatement stmt = con.prepareStatement(
					"DELETE cl.*, bo.* "
				  + "FROM ContactList cl "
				  + "INNER JOIN BusinessObject bo ON cl.ID = bo.bo_ID Where bo.user_ID = ?" 
				  );
			stmt.setDouble(1, userId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Vector<ContactList> clList = new Vector <ContactList>();
		clList = this.findContactListByUserID(userId);
		for(ContactList cl : clList) {
			BusinessObjectMapper.businessObjectMapper().deleteBusinessObjectByID(cl.getBo_Id());
		}
	}

	/**
	 * Alle Kontaktlisten löschen.
	 */
	
	public void deleteAllContactLists() {
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE * FROM ContactList");

		}catch(SQLException e){
			e.printStackTrace();
		}
		
		Vector<ContactList> clList = new Vector <ContactList>();
		clList = this.findAllContactLists();
		for(ContactList cl : clList) {
			BusinessObjectMapper.businessObjectMapper().deleteBusinessObjectByID(cl.getBo_Id());
		}
	}

	/**
	 * Einen Kontakt zur Kontaktliste hinzufögen.
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
