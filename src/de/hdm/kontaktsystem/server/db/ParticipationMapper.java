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
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;


/**
 * Die Mapper-Klasse <code>ParticipationMapper</code> bildet <code>Participation</code>-Objekte 
 * auf eine relationale Datenbank ab. Dazu werden Methoden zum Erzeugen, Suchen, Ändern und 
 * Löschen von Objekten zur Verfügung gestellt. Es können sowohl Objekte in Datenbank-Strukturen, 
 * als auch Datenbank-Strukturen in Objekte überführt werden.
 * 
 * @author Sandra Prestel
 *
 */



public class ParticipationMapper {
	
	/**
	 * Die Klasse <code>ParticipationMapper</code> ist ein Singleton, d.h. sie wird nur einmal instantiiert.
	 * Die statische Variable <code>INSTANCE</code> speichert die einzige Instanz der Klasse. Durch den 
	 * Bezeichner <code>static</code> ist diese Variable nur einmal für alle Instanzen der Klasse vorhanden.
	 */	
	
	private static ParticipationMapper participationMapper = null;
	
	/**
	 * Der Konstruktor ist <code>privat</code>, um einen Zugriff von außerhalb der Klasse zu verhindern.
	 */
	private ParticipationMapper() {
	}
	
	 /**
	 * Hier findet die Anwendung des <code> Singleton Pattern </code> statt
	 * Diese Methode gibt das einzige Objekt dieser Klasse zurück.
	 * @return Instanz des PropertyMapper 
	 */			

	public static ParticipationMapper participationMapper() {
		if (participationMapper == null) {
			participationMapper = new ParticipationMapper();
		}
		return participationMapper;
	}
  
	
	/**
	 * Zurückgeben aller Teilhaberschaften in der Datenbank
	 * @return alle Teilhaberschaften als Participation-Objekte in einem Vector
	 * @note only used in Report Generator
	 */
	public Vector<Participation> findAllParticipations(){
		Connection con = DBConnection.connection();
		Statement stmt;
		try {
			// Create Vector for all Participation-Objects
			Vector<Participation> participations = new Vector<Participation>();
			
			stmt = con.createStatement();
			// Get all Participations from database and store in a ResultSet-Object
			ResultSet rs = stmt.executeQuery("SELECT * FROM User_BusinessObject");
			//Transfer all Participations from database to Participation-Objects
			while(rs.next()) {
				Participation participation = new Participation();
				User participant = new User();
				PropertyValue bo = new PropertyValue();  // Träger für BO ID, da BO nicht instanziiert werden kann.
				participant.setGoogleID(rs.getDouble("User_ID"));
				participation.setParticipant(participant);	
				bo.setBo_Id(rs.getInt("BusinessObject_ID"));
				participation.setReference(bo);
		
				participations.add(participation);
			}
			return participations;
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isFullShared(BusinessObject bo, User u){
		
		Connection con = DBConnection.connection();
		
		try {
			// Create Vector for all Participation-Objects
			Vector<Participation> participations = new Vector<Participation>();			
			// Get all Participations from database and store in a ResultSet-Object
			PreparedStatement stmt = con.prepareStatement("Select * From `User_BusinessObject` " + 
			"WHERE `User_ID` = ? AND `BusinessObject_ID` = ? " );
			stmt.setDouble(1, u.getGoogleID());
			stmt.setInt(2, bo.getBoId());
			ResultSet rs = stmt.executeQuery();
			 if(rs.next()){
				 return rs.getBoolean("Share_All");
			 }
			
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}

	
	/**
	 * Zurückgeben aller Teilhaberschaften zu Objekten eines gegebenen Users.
	 * Dieser stellt den Ersteller <em>Owner</em> des BusinessObjektes dar.
	 * 
	 * @return Teilhaberschaften als Participation-Objekte in einem Vector
	 */
	
	public Vector<Participation> findParticipationsByOwner(User user) {
		Connection con = DBConnection.connection();
		
		try {
			
			Vector<Participation> participations = new Vector<Participation>();
			
			// Get all Participations from database and store in a ResultSet-Object
			PreparedStatement stmt = con.prepareStatement(
					  "SELECT User_BusinessObject.*, BusinessObject.* "
					+ "FROM  User_BusinessObject "
					+ "INNER JOIN BusinessObject "
					+ "ON User_BusinessObject.BusinessObject_ID = BusinessObject.bo_ID "
					+ "WHERE BusinessObject.User_ID = ?");
			
			stmt.setDouble(1, user.getGoogleID());
			ResultSet rs = stmt.executeQuery();
			
			//Transfer all Participations from database to Participation-Objects
			while(rs.next()) {
				Participation participation = new Participation();
				BusinessObject reference = new BusinessObject();
				
				User participant = new User();
				PropertyValue bo = new PropertyValue();  // Träger für BO ID, da BO nicht instanziiert werden kann.
				participant.setGoogleID(rs.getDouble("User_ID"));
				participation.setParticipant(participant);	
				bo.setBo_Id(rs.getInt("BusinessObject_ID"));
				participation.setReference(bo);
				participation.setShareAll(rs.getBoolean("Share_All"));
				participations.add(participation);
				
			}
			return participations;
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Zurückgeben aller Teilhaberschaften, die mit einem gegebenen User geteilt werden
	 * @return Teilhaberschaften als Participation-Objekte in einem Vector
	 */
	public Vector<Participation> findParticipationsByParticipant(User user){
		Connection con = DBConnection.connection();
		
		try {
			// Create Vector for all Participation-Objects
			Vector<Participation> participations = new Vector<Participation>();
			
			// Get all Participations from database and store in a ResultSet-Object
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM User_BusinessObject WHERE User_ID = ?");
			stmt.setDouble(1, user.getGoogleID());
			ResultSet rs = stmt.executeQuery();
			
			//Transfer all Participations from database to Participation-Objects
			while(rs.next()) {

				Participation participation = new Participation();
				User participant = new User();
				PropertyValue bo = new PropertyValue();  // Träger für BO ID, da BO nicht instanziiert werden kann.
				participant.setGoogleID(rs.getDouble("User_ID"));
				participation.setParticipant(participant);	
				bo.setBo_Id(rs.getInt("BusinessObject_ID"));
				participation.setReference(bo);
				participation.setShareAll(rs.getBoolean("Share_All"));
				participations.add(participation);

			}
			return participations;
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Zurückgeben aller Teilhaberschaften, die sich auf ein gegebenes BusinessObject beziehen
	 * @return Teilhaberschaften als Participation-Objekte in einem Vector
	 */
	public Vector<Participation> findParticipationsByBusinessObject(BusinessObject businessObject){
		
		Connection con = DBConnection.connection();
		
		try {
			// Create Vector for all Participation-Objects
			Vector<Participation> participations = new Vector<Participation>();			
			// Get all Participations from database and store in a ResultSet-Object
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM User_BusinessObject WHERE BusinessObject_ID = ?");
			stmt.setInt(1, businessObject.getBoId());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Participation participation = new Participation();
				User participant = new User();
				PropertyValue bo = new PropertyValue();  // Träger für BO ID, da BO nicht instanziiert werden kann.
				participant.setGoogleID(rs.getDouble("User_ID"));
				participation.setParticipant(participant);	
				bo.setBo_Id(rs.getInt("BusinessObject_ID"));
				participation.setReference(bo);
				participation.setShareAll(rs.getBoolean("Share_All"));
				participations.add(participation);
			}
			
			return participations;
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * Zurückgeben aller Teilhaberschaften von Vollständig geteilten Kontakten
	 * @return Teilhaberschaften als Participation-Objekte in einem Vector
	 */
	public Vector<Participation> findSharedContacts(User u){
		
		Connection con = DBConnection.connection();
		
		try {
			// Create Vector for all Participation-Objects
			Vector<Participation> participations = new Vector<Participation>();			
			// Get all Participations from database and store in a ResultSet-Object
			PreparedStatement stmt = con.prepareStatement("Select * From `User_BusinessObject` part " +
			"INNER JOIN `Contact` c ON c.ID = part.`BusinessObject_ID` " +
			"INNER JOIN `BusinessObject` bo ON bo.`bo_ID` = part.`BusinessObject_ID` " +
			"WHERE part.`User_ID` = ?");
			stmt.setDouble(1, u.getGoogleID());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Participation participation = new Participation();
				Contact c = new Contact();
				
				participation.setParticipant(u);	
				c.setBo_Id(rs.getInt("BusinessObject_ID"));
				c.setCreationDate(rs.getDate("creationDate"));
				c.setModifyDate(rs.getDate("modificationDate"));	
				c.setShared_status(rs.getBoolean("status"));
				participation.setShareAll(rs.getBoolean("Share_All"));
				participation.setReference(c);
				
				participations.add(participation);
			}
			
			return participations;
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Zurückgeben aller Teilhaberschaften von Teilweise geteilten Kontakten
	 * @return Teilhaberschaften als Participation-Objekte in einem Vector
	 */
	public Vector<PropertyValue> findPVforSharedContact(Contact c, User u){
		
		Connection con = DBConnection.connection();
		try {
			// Create Vector for all Participation-Objects
			Vector<Participation> participations = new Vector<Participation>();			
			// Get all Participations from database and store in a ResultSet-Object
			PreparedStatement stmt = con.prepareStatement("Select * From `User_BusinessObject` part " +
			"INNER JOIN `PropertyValue` pv ON pv.ID = part.`BusinessObject_ID` " +
			"INNER JOIN Property p ON p.`ID` = pv.`property_ID` " +
			"INNER JOIN `BusinessObject` bo ON bo.`bo_ID` = pv.`contact_ID` " +
			"WHERE part.`User_ID` = ? AND pv.`contact_ID` = ? " +
			"ORDER BY `contact_ID`");
			stmt.setDouble(1, u.getGoogleID());
			stmt.setInt(2, c.getBoId());
			ResultSet rs = stmt.executeQuery();
			
			Vector<PropertyValue> pvv = new Vector<PropertyValue>();
			
			while(rs.next()){
				PropertyValue pv = new PropertyValue(); // geteilte Eigenschft
				Property p = new Property();
				p.setDescription(rs.getString("Description"));
				p.setId(rs.getInt("property_ID"));
				pv.setProperty(p);
				pv.setBo_Id(rs.getInt("BusinessObject_ID"));
				pv.setCreationDate(rs.getDate("creationDate"));
				pv.setModifyDate(rs.getDate("modificationDate"));
				pv.setShared_status(rs.getBoolean("status"));
				pv.setOwner(c.getOwner());
				pv.setValue(rs.getString("value"));
				pvv.add(pv);
			}
			
			return pvv;
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * Zurückgeben aller Teilhaberschaften, von geteilten KontaktListen
	 * @return Teilhaberschaften als Participation-Objekte in einem Vector
	 */
	public Vector<Participation> findAllSharedContactLists(User u){
		
		Connection con = DBConnection.connection();
		
		try {
			// Create Vector for all Participation-Objects
			Vector<Participation> participations = new Vector<Participation>();			
			// Get all Participations from database and store in a ResultSet-Object
			PreparedStatement stmt = con.prepareStatement("Select * From `User_BusinessObject` part " +
			"INNER JOIN `ContactList` cl ON cl.ID = part.`BusinessObject_ID` " +
			"INNER JOIN `BusinessObject` bo ON bo.`bo_ID` = part.`BusinessObject_ID` " +
			"WHERE part.`User_ID` = ?");
			stmt.setDouble(1, u.getGoogleID());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Participation participation = new Participation();
				ContactList cl = new ContactList();
				
				participation.setParticipant(u);	
				
				cl.setBo_Id(rs.getInt("BusinessObject_ID"));
				cl.setCreationDate(rs.getDate("creationDate"));
				cl.setModifyDate(rs.getDate("modificationDate"));
				cl.setName(rs.getString("contactList_name"));
				cl.setShared_status(rs.getBoolean("status"));
				participation.setShareAll(rs.getBoolean("Share_All"));
				participation.setReference(cl);
				
				participations.add(participation);
			}
			
			return participations;
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	
	
	
	
	
	/**
	 * Updatet den ShareAll status der Teilhaberschaft
	 * @param participation
	 */
	public Participation updateParticipation(Participation participation) {
		Connection con = DBConnection.connection();
		
		try {
			PreparedStatement stmt = con.prepareStatement("UPDATE User_BusinessObject SET Share_All = ? WHERE BusinessObject_ID = ? AND User_ID = ?");
			stmt.setBoolean(1, participation.getShareAll());
			stmt.setInt(2, participation.getReferenceID());
			stmt.setDouble(3, participation.getParticipantID());
			stmt.execute();
			return participation;
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * Einfügen einer neuen Teilhaberschaft in die Datenbank
	 * @param participation
	 */
	public Participation insertParticipation(Participation participation) {
		Connection con = DBConnection.connection();
		
		try {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO User_BusinessObject (BusinessObject_ID, User_ID, Share_All) VALUES (?, ?, ?)");
			stmt.setInt(1, participation.getReferenceID());
			stmt.setDouble(2, participation.getParticipantID());
			stmt.setBoolean(3, participation.getShareAll());
			stmt.execute();
			return participation;
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}
		
	/**
	 * Löschen aller Teilhaberschaften
	 *  @note in GUI Nicht Verwendet
	 */
	public void deleteAllParticipations() {
		Connection con = DBConnection.connection();
		Statement stmt;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM User_BusinessObject");
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
//	Fällt weg, da nicht mehr Subklasse von BusinessObject, damit keine eigene ID
//	/**
//	 * Delete a Participation with a given ID
//	 * @param id
//	 */
//	public void deleteParticipationForId(int id) {
//		//TODO Implement Method
//	}
	
	/**
	 * Löschen aller Teilhaberschaften zu Objekten eines gegebenen Users
	 * @param owner
	 * @note in GUI Nicht Verwendet
	 */
	public void deleteParticipationForOwner(User user) {
		Vector<Participation> participations = this.findParticipationsByOwner(user);
		
		Connection con = DBConnection.connection();
		for(Participation p : participations) {
			try {
				PreparedStatement stmt = con.prepareStatement("DELETE FROM User_BusinessObject WHERE BusinessObject_ID = ?");
				stmt.setInt(1, p.getReferenceID());
				stmt.execute();
				
			} catch(SQLException e){
				e.printStackTrace();
			}
			BusinessObjectMapper.businessObjectMapper().setStatusFalse(p.getReferenceID());
		}
		
	}
	
	/**
	 * Löschen aller Teilhaberschaften, die mit einem gegebenen User geteilt wurden.
	 * 
	 * @param participant
	 *  @note in GUI Nicht Verwendet
	 */
	public void deleteParticipationForParticipant(User user) {
		Vector<Participation> participations = findParticipationsByParticipant(user);		
		Connection con = DBConnection.connection();
		for(Participation p : participations) {
			try {
				PreparedStatement stmt = con.prepareStatement("DELETE FROM User_BusinessObject WHERE User_ID = ? AND BusinessObject_ID = ?");
				stmt.setDouble(1, p.getParticipantID());
				stmt.setInt(2, p.getReferenceID());
				stmt.execute();
				
			} catch(SQLException e){
				e.printStackTrace();
			}
			
			
		}
	}
	
	public Participation deleteParticipation(Participation part) {
		Connection con = DBConnection.connection();
		
		try {
			
			PreparedStatement stmt = con.prepareStatement("DELETE FROM User_BusinessObject WHERE User_ID = ? AND BusinessObject_ID = ?");
			stmt.setDouble(1, part.getParticipantID());
			stmt.setInt(2, part.getReferenceID());
			stmt.execute();
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		return part;
		
	}
	
	
	/**
	 * Löschen aller Teilhaberschaften, die sich auf ein gegebenes BusinessObject beziehen
	 * @param businessObject
	 *  @note in GUI Nicht Verwendet
	 */
	public void deleteParticipationForBusinessObject(BusinessObject businessObject) {
		Connection con = DBConnection.connection();
		
		try {
			PreparedStatement stmt = con.prepareStatement("DELETE FROM User_BusinessObject WHERE BusinessObject_ID = ?");
			stmt.setInt(1, businessObject.getBoId());
			stmt.execute();
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		BusinessObjectMapper.businessObjectMapper().setStatusFalse(businessObject.getBoId());
	}
	
	
	

	
	
	
}