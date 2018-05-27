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
			
		BusinessObjectMapper.businessObjectMapper().insert(cl);
		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con
					.prepareStatement("INSERT INTO ContactList (ID, contactList_name) VALUES (?, ?)");
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
	 * Fügte dem ContactList-Objekt einen Vektor mit allen enthaltenen Contact-Objekten hinzu
	 * 
	 * @param ContactList-Objekt
	 */

	public Vector<Contact> findContactFromList(ContactList cl) {
		Connection con = DBConnection.connection();
		try {
			Vector<Contact> c = new Vector<Contact>();

			PreparedStatement stmt = con.prepareStatement(
					"SELECT Contact_ID from Contact_ContactList where ContactList_ID = ?"
					 );
			stmt.setInt(1, cl.getBoId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				c.add(ContactMapper.contactMapper().findContactById(rs.getInt("Contact_ID")));				
			}
			return c;
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
	 *  
	 *  @param User-Objekt
	 *  @return Vector<ContactList>
	 */

	public Vector<ContactList> findAllSharedByMe (User user) {

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
	
	

	
	/**
	 * Alle für den Benutzer in der Applikation geteilte Kontaktelisten <code>ContactList</code> -Objekte
	 * künnen über den Aufruf dieser Methode aus der DB zur�ck gegeben werden.
	 * 
	 * @param User-Objekt
	 * @return Vector ContactList-Objekte
	 */

	public Vector<ContactList> findAllSharedByOthersToMe(User user) {

		Vector<Participation> participationVector = new Vector<Participation>();		
		participationVector = ParticipationMapper.participationMapper().findParticipationsByParticipant(user);
		Vector<ContactList> clResultVector = new Vector <ContactList>(); 		
		
		for (Participation part : participationVector) {
			 //System.out.println("part id:" + part.getReferenceID());	
			 
			 BusinessObject bo = BusinessObjectMapper.businessObjectMapper().findBusinessObjectByID(part.getReferenceID());
			 //System.out.println(bo.getClass());
			 ContactList cl = new ContactList();			 
			 //System.out.println("bo gefunden: " + bo.getBo_Id());

			 
			 	if(bo instanceof ContactList) {			 		
			 		cl = (ContactList) bo;
			 		//System.out.println("contactList name " + bo);
			 		clResultVector.addElement(cl);
			 	}
			}
		
		if(clResultVector.isEmpty()) System.out.println("# no contactList found");	
		
		return clResultVector;		
	}
	

	/**
	 * Updatet einen eintrag in der ContactList Tabelle
	 * verwendet zur Namensänderung
	 * 
	 * @param ContactList-Objekt
	 */

	public void updateContactList(ContactList cl) {
		BusinessObjectMapper.businessObjectMapper().update(cl);
		Connection con = DBConnection.connection();

		try {
			PreparedStatement stmt = con.prepareStatement("UPDATE ContactList SET contactList_name = ? WHERE ID = ?");
			stmt.setString(1, cl.getName());
			stmt.setInt(2, cl.getBoId());
			stmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	   
	/**
	 * Methode zur Löschung aller von einem User erstellten Kontaktlisten <code>ContactList</code> -Objekte,
	 * welche im System mit anderen Nutzern geteilt wurden. 
	 * @param User
	 */
	
	public void deleteAllSharedByMe(User user) {
		
		Vector <ContactList> clResult = new Vector <ContactList>();
		clResult = this.findAllSharedByMe(user);
		
		for(ContactList cl : clResult) {
			ParticipationMapper.participationMapper().deleteParticipationForBusinessObject(cl);
			this.deleteContactListById(cl.getBoId());
			//System.out.println("# shared contactList deleted: " + cl.getBo_Id() );

		}
	}
	
	/**
	 * Eine Methode zur Löschung aller Verbindungen in der Participation Tabelle der DB.
	 * Dies führt dazu, dass die für einen Nutzer geteilten Objekte nicht mehr aufgerufen werden können.
	 * Die Teilhaberschaft ist damit beendet. 
	 * 
	 * @param User-Objekt
	 */
	
	public void deleteAllSharedByOthersToMe(User user) {
		
		Vector <ContactList> clResult = new Vector <ContactList>();
		clResult = this.findAllSharedByOthersToMe(user);		
		//System.out.println(clResult);

		for(ContactList cl : clResult) {
			ParticipationMapper.participationMapper().deleteParticipationForParticipant(user);
			//System.out.println("# participation for contact deleted: " + cl.getBo_Id() );

		}
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
		BusinessObjectMapper.businessObjectMapper().deleteBusinessObjectByID(id);
		return i;
	}

	
	/**
	 * Es löscht alle Kontakte, welche zu einer User ID gehören.
	 * 
	 * @param User Id
	 */

	public void deleteContactListByUserId(Double userId) {
		
		for(ContactList cl : this.findContactListByUserId(userId)){
			this.deleteContactListById(cl.getBoId());
		}
		
		Vector<ContactList> clList = new Vector <ContactList>();
		clList = this.findContactListByUserId(userId);
		for(ContactList cl : clList) {
			BusinessObjectMapper.businessObjectMapper().deleteBusinessObjectByID(cl.getBoId());
		}
	}

	
	/**
	 * Alle Kontaktlisten löschen.
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

	public void addContactToContactlist(ContactList cl, Contact c) {

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
	}

	/**
	 * Einen Kontakt aus der Kontaktliste entfernen.
	 * 
	 * @param ContactList-Objekt
	 * @param Contact-Objekt
	 */

	public void removeContactFromContactList(ContactList cl, Contact c) {
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

	}

}
