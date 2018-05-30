package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.sql.ResultSet;

import de.hdm.kontaktsystem.shared.bo.BusinessObject;

//import com.mysql.jdbc.Connection;

import de.hdm.kontaktsystem.shared.bo.Contact;
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
		
		BusinessObjectMapper.businessObjectMapper().insert(contact);
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
					  "SELECT c.* , bo.* " 
					+ "FROM  Contact AS c " 
					+ "INNER JOIN BusinessObject AS bo ON bo.bo_ID = c.ID "
					+ "WHERE bo.user_ID = ?"
					);
			
			stmt.setDouble(1, user_id);
			ResultSet rs = stmt.executeQuery();							  

			while (rs.next()) {
				Contact contact = new Contact();
				contact.setBo_Id(rs.getInt("c.ID"));
				contact.setShared_status(rs.getBoolean("bo.status"));
				contact.setCreationDate(rs.getTimestamp("bo.creationDate"));
				contact.setModifyDate(rs.getTimestamp("bo.modificationDate"));
				contact.setOwner(UserMapper.userMapper().findById(rs.getDouble("bo.user_ID")));
				contact.addPropertyValue(PropertyValueMapper.propertyValueMapper().findName(contact));				
				result.addElement(contact);
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 *  Alle für den Benutzer in der Applikation zugaenglichen Kontakte <code>Contact</code> - Objekte
	 * (diese sind selbst erstellt und anderen zur Teilhaberschaft freigegeben) werden anhand ihres Status gesucht
	 *  und als ein Ergebnissvektor aus Contact-objekten zurueckgegeben. 
	 *  
	 *  @param User-Objekt
	 *  @return Vector mit allen geteilten Contact-Objekten
	 */

	public Vector<Contact> findAllSharedByMe (User user) {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln, die von diesem geteilt wurden
		Vector<Participation> participationVector = new Vector<Participation>();		
		participationVector = ParticipationMapper.participationMapper().findParticipationsByOwner(user);		
		Vector<Contact> contactResultVector = new Vector <Contact>(); 		
				
		for (Participation part : participationVector) {
			 System.out.println("part id:" + part.getReferenceID());			 
			 BusinessObject bo = BusinessObjectMapper.businessObjectMapper().findBusinessObjectByID(part.getReferenceID());
			 Contact contact = new Contact();
			 
			 	if(bo instanceof Contact) {			 		
			 		contact = (Contact) bo;
			 		System.out.println("contact name " + contact.getName());
			 		contactResultVector.addElement(contact);	     
			 }		
		}	 	
		if(contactResultVector.isEmpty()) System.out.println("# no contacts found");			
		
		return contactResultVector;
		
	}
	
	
	/**
	 * Alle für den Benutzer in der Applikation geteilte Kontakte <code>Contact</code> Objekte
	 * können über den Aufruf dieser Methode aus der DB zurück gegeben werden.
	 * 
	 * @param User-Objekt
	 * @return Vector Contact-Objekte
	 */

	public Vector<Contact> findAllSharedByOthersToMe (User user) {

		// Alle Participation-Objekte eines Users abrufen, welche für Objekte kapseln, die von diesem geteilt wurden
		Vector<Participation> participationVector = new Vector<Participation>();		
		participationVector = ParticipationMapper.participationMapper().findParticipationsByParticipant(user);
		Vector<Contact> contactResultVector = new Vector <Contact>(); 
		
		for (Participation part : participationVector) {
			 System.out.println("part id:" + part.getReferenceID());			 
			 BusinessObject bo = BusinessObjectMapper.businessObjectMapper().findBusinessObjectByID(part.getReferenceID());
			 System.out.println(bo.getClass());
			 Contact contact = new Contact();			 
			 System.out.println("bo gefunden: " + bo.getBoId());
			 
			 	if(bo instanceof Contact) {				
			 		contact = (Contact) bo;
			 		System.out.println("contact name " + bo);
			 		contactResultVector.addElement(contact);
			 	}
		}		
		if(contactResultVector.isEmpty()) System.out.println("# no contacts found");		
		return contactResultVector;		
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
				contact.setOwner(UserMapper.userMapper().findById(rs.getDouble("user_ID")));
				contact.setBo_Id(rs.getInt("ID"));
				contact.setShared_status(rs.getBoolean("status"));
				contact.setCreationDate(rs.getTimestamp("creationDate"));
				contact.setModifyDate(rs.getTimestamp("modificationDate"));
				contact.setName(PropertyValueMapper.propertyValueMapper().findName(contact));
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
		//System.out.println("#Contact -findcontactByID");
		Connection con = DBConnection.connection();
			
		try {			
			PreparedStatement stmt = con.prepareStatement(
					  "SELECT * " 
					+ "FROM  Contact c "
					+ "INNER JOIN BusinessObject bo ON bo.bo_ID = c.ID "
					+ "WHERE c.ID = ? "); 
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {						
				Contact contact = new Contact();
				contact.setOwner(UserMapper.userMapper().findById(rs.getDouble("user_ID")));
				contact.setBo_Id(rs.getInt("bo_ID"));
				contact.setShared_status(rs.getBoolean("status"));
				contact.setCreationDate(rs.getTimestamp("creationDate"));
				contact.setModifyDate(rs.getTimestamp("modificationDate"));
				contact.setPropertyValues(PropertyValueMapper.propertyValueMapper().findBy(contact));
				contact.setName(PropertyValueMapper.propertyValueMapper().findName(contact));
				//System.out.println("contact id: " + contact.getBo_Id());

				return contact;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	/**
	 * Den eigenen Kontakt eines User, welcher bei dessen Erzeugung in 
	 * der DB erstellt wird aus der DB aufrufen. 
	 * 
	 *  @param User - Objekt
	 *  @return Contact - Objekt
	 */
	
	public Contact findOwnContact(User u) {
		Contact contact = new Contact();
		return contact = this.findContactById(u.getUserContact().getBoId());
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
				contact.setBo_Id(rs.getInt("bo.bo_ID"));
				contact.setShared_status(rs.getBoolean("bo.status"));
				contact.setCreationDate(rs.getTimestamp("bo.creationDate"));
				contact.setModifyDate(rs.getTimestamp("bo.modificationDate"));
				contact.setOwner(UserMapper.userMapper().findById(rs.getDouble("user_ID")));		
				contact.setName(PropertyValueMapper.propertyValueMapper().findName(contact));

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
				contact.setOwner(UserMapper.userMapper().findById(rs.getDouble("user_ID")));
				contact.setBo_Id(rs.getInt("id"));
				contact.setShared_status(rs.getBoolean("status"));
				contact.setCreationDate(rs.getTimestamp("creationDate"));
				contact.setModifyDate(rs.getTimestamp("modificationDate"));
				contact.addPropertyValue(PropertyValueMapper.propertyValueMapper().findName(contact));
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
			
			Vector <PropertyValue> propResult = new Vector <PropertyValue>();
			propResult = PropertyValueMapper.propertyValueMapper().findBy(contact);
			for(PropertyValue pV : propResult) {
				PropertyValueMapper.propertyValueMapper().update(pV);
			}			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contact;
	}


	/**
	 * Methode zur Löschung aller von einem User erstellten Kontakte <code>Contact</code> Objekte,
	 * welche im System mit anderen Nutzern geteilt wurden. 
	 * @param User-Objekt
	 */
	
	public void deleteAllSharedByMe(User user) {
		
		Vector <Contact> contactResult = new Vector <Contact>();
		contactResult = this.findAllSharedByMe(user);
		
		for(Contact contact : contactResult) {
			ParticipationMapper.participationMapper().deleteParticipationForBusinessObject(contact);
			this.deleteContact(contact);
			System.out.println("# shared contact deleted: " + contact.getBoId() );
		}
	}
	
	
	/**
	 * Eine Methode zur Loeschung aller Verbindungen in der Participation Tabelle der DB.
	 * Dies fuehrt dazu, dass urspruenglich fuer einen Nutzer geteilten Objekte
	 * von diesem nicht mehr aufgerufen werden koennen.
	 * 
	 * @param User-Objekt
	 */
	
	public void deleteAllSharedByOthersToMe(User user) {
		
		Vector <Contact> contactResult = new Vector <Contact>();
		contactResult = this.findAllSharedByOthersToMe(user);		

		for(Contact contact : contactResult) {
			ParticipationMapper.participationMapper().deleteParticipationForParticipant(user);
			System.out.println("# participation for contact deleted: " + contact.getBoId() );
		}
	}
	
	
	/**
	 * Mapper-Methode um alle Kontakte zu loeschen
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
		System.out.println("Delete Contact");
		int i = 0; // Anzahl der gelöschten Reihen
		PropertyValueMapper.propertyValueMapper().deleteByContact(id);
		Connection con = DBConnection.connection();
		try {
			PreparedStatement stmt = con.prepareStatement("DELETE FROM Contact WHERE ID = ?" );
			stmt.setInt(1, id);
			i = stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		BusinessObjectMapper.businessObjectMapper().deleteBusinessObjectByID(id);
		return i;
	}
	

	/**
	 * Mapper-Methode um alle Kontakte eines bestimmten Users mittels der User-ID zu
	 * loeschen
	 * 
	 * @param User ID 
	 */

	public void deleteAllContactsByUser(double user_id) {

		Vector<Contact> result = new Vector<Contact>();		
		//Aufrufen aller Kontakte eines bestimmten Users
		result = ContactMapper.contactMapper().findAllContactsByUser(user_id);
		for (Contact c : result) {
			deleteContactByID(c.getBoId());
			BusinessObjectMapper.businessObjectMapper().deleteBusinessObjectByID(c.getBoId());
			System.out.println("contact deleted: " + c.getBoId());
		}
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
	

	public Contact addOwnContact(User owner) {
		//System.out.println("#Contact -faddOwncontact");
		int contact_ID = owner.getUserContact().getBoId();
		Connection con = DBConnection.connection();
		try {
			PreparedStatement stmt = con.prepareStatement(
					  "SELECT c.* , bo.* " 
					+ "FROM  Contact c "
					+ "INNER JOIN BusinessObject bo ON bo.bo_ID = c.ID " 
					+ "WHERE c.ID = ?");

			stmt.setInt(1, contact_ID);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {	
				Contact contact = new Contact();				
				contact.setBo_Id(rs.getInt("bo.bo_ID"));
				contact.setShared_status(rs.getBoolean("bo.status"));
				contact.setCreationDate(rs.getTimestamp("bo.creationDate"));
				contact.setModifyDate(rs.getTimestamp("bo.modificationDate"));
				contact.setName(PropertyValueMapper.propertyValueMapper().findName(contact));
				owner.setUserContact(contact);
				contact.setOwner(owner);
				return contact;				
			}			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return null;
	}


}