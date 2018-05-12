package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.User;


//TODO: Test all Methods

/**
 * Die Mapper-Klasse <code>ParticipationMapper</code> bildet <code>Participation</code>-Objekte 
 * auf eine relationale Datenbank ab. Dazu werden Methoden zum Erzeugen, Suchen, Ändern und 
 * Löschen von Objekten zur Verfügung gestellt. Es können sowohl Objekte in Datenbank-Strukturen, 
 * als auch Datenbank-Strukturen in Objekte überführt werden.
 * 
 * @author Sandra
 *
 */

//TODO: Implement Methods, create descriptions for methods


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
	 * Get all Participations in the Database
	 * @return all Participations as Participation-Objects in a Vector
	 */
	public Vector<Participation> getAllParticipations(){
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
				Participation p = new Participation();
				p.setParticipantID(rs.getInt("User_ID"));
				p.setReferenceID(rs.getInt("BusinessObject_ID"));
				participations.add(p);
			}
			return participations;
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
//	Fällt weg, da nicht mehr Subklasse von BusinessObject, damit keine eigene ID
//	/**
//	 * Get Participation by the unique ID
//	 * @return Participation-Object
//	 */
//	public Participation getParticipationById(int id) {
//		//TODO Implement Method
//		return null;
//	}

	
	/**
	 * Get all Participations which refer to Business-Objects, that a given User owns
	 * @return Participation-Objects in a Vector
	 */
	public Vector<Participation> getParticipationsByOwner(User owner) {
		Connection con = DBConnection.connection();
		
		try {
			// Create Vector for all Participation-Objects
			Vector<Participation> participations = new Vector<Participation>();
			
			// Get all Participations from database and store in a ResultSet-Object
			PreparedStatement stmt = con.prepareStatement("SELECT User_BusinessObject.*, BusinessObject.*"
					+ "FROM  User_BusinessObject"
					+ "INNER JOIN BusinessObject"
					+ "ON User_BusinessObject.BusinessObject_ID = BusinessObject.bo_ID"
					+ "WHERE BusinessObject.user_ID = ?");
			stmt.setInt(1, owner.getGoogleID());
			ResultSet rs = stmt.executeQuery();
			
			//Transfer all Participations from database to Participation-Objects
			while(rs.next()) {
				Participation p = new Participation();
				p.setParticipantID(rs.getInt("User_ID"));
				p.setReferenceID(rs.getInt("BusinessObject_ID"));
				participations.add(p);
			}
			return participations;
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Get all Participations that a given User participates in
	 * @return Participation-Objects in a Vector
	 */
	public Vector<Participation> getParticipationsByParticipant(User participant){
		Connection con = DBConnection.connection();
		
		try {
			// Create Vector for all Participation-Objects
			Vector<Participation> participations = new Vector<Participation>();
			
			// Get all Participations from database and store in a ResultSet-Object
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM User_BusinessObject WHERE User_ID = ?");
			stmt.setInt(1, participant.getGoogleID());
			ResultSet rs = stmt.executeQuery();
			
			//Transfer all Participations from database to Participation-Objects
			while(rs.next()) {
				Participation p = new Participation();
				p.setParticipantID(rs.getInt("User_ID"));
				p.setReferenceID(rs.getInt("BusinessObject_ID"));
				participations.add(p);
			}
			return participations;
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Get all Participations that refer to a given Business-Object
	 * @return Participation-Objects in a Vector
	 */
	public Vector<Participation> getParticipationsByContact(BusinessObject businessObject){
		Connection con = DBConnection.connection();
		
		try {
			// Create Vector for all Participation-Objects
			Vector<Participation> participations = new Vector<Participation>();
			
			// Get all Participations from database and store in a ResultSet-Object
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM User_BusinessObject WHERE BusinessObject_ID = ?");
			stmt.setInt(1, businessObject.getBo_Id());
			ResultSet rs = stmt.executeQuery();
			
			//Transfer all Participations from database to Participation-Objects
			while(rs.next()) {
				Participation p = new Participation();
				p.setParticipantID(rs.getInt("User_ID"));
				p.setReferenceID(rs.getInt("BusinessObject_ID"));
				participations.add(p);
			}
			return participations;
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}


	
	/**
	 * Update a specific Participation in the database
	 * @param participation
	 */
		/* 
		 * nothing to update, because the IDs don't change.
		 * Since the IDs are the Primary Key other values will just generate a new Participation
		 */
	
	
	/**
	 * Insert a new Participation into the database
	 * @param participation
	 */
	public void insertParticipation(Participation participation) {
		Connection con = DBConnection.connection();
		
		try {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO User_BusinessObject (BusinessObject_ID, User_ID) VALUES (?, ?)");
			stmt.setInt(1, participation.getReferenceID());
			stmt.setInt(2, participation.getParticipantID());
			stmt.execute();
			
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Delete all Participations in the database
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
	 * Delete all Participations, which refer to Business-Objects, that a given User owns
	 * @param owner
	 */
	public void deleteParticipationForOwner(User owner) {
		Vector<Participation> participations = getParticipationsByOwner(owner);
		
		Connection con = DBConnection.connection();
		for(Participation p : participations) {
			try {
				PreparedStatement stmt = con.prepareStatement("DELETE FROM User_BusinessObject WHERE BusinessObject_ID = ?");
				stmt.setInt(1, p.getReferenceID());
				stmt.execute();
				
			} catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Delete all Participations, that a given User participates in
	 * @param participant
	 */
	public void deleteParticipationForParticipant(User participant) {
		Connection con = DBConnection.connection();
		
		try {
			PreparedStatement stmt = con.prepareStatement("DELETE FROM User_BusinessObject WHERE User_ID = ?");
			stmt.setInt(1, participant.getGoogleID());
			stmt.execute();
			
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Delete all Participations, that refer to a given BusinessObject
	 * @param businessObject
	 */
	public void deleteParticipationForBusinessObject(BusinessObject businessObject) {
		Connection con = DBConnection.connection();
		
		try {
			PreparedStatement stmt = con.prepareStatement("DELETE FROM User_BusinessObject WHERE BusinessObject_ID = ?");
			stmt.setInt(1, businessObject.getBo_Id());
			stmt.execute();
			
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Generate a new Participation-Table in the database
	 */
	public void initParticipationTable() {
		Connection con = DBConnection.connection();
		Statement stmt;
		
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("CREATE TABLE User_BusinessObject(\r\n" + 
					"BusinessObject_ID INT (10) NOT NULL,\r\n" + 
					"User_ID INT (10) NOT NULL,\r\n" + 
					"PRIMARY KEY(BusinessObject_ID, User_ID),\r\n" + 
					"FOREIGN KEY(BusinessObject_ID) REFERENCES BusinessObject(bo_ID),\r\n" + 
					"FOREIGN KEY(User_ID) REFERENCES User(ID)\r\n" + 
					")");
		}catch(SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * Delete the Participation-Table in the database
	 */
	public void deleteParticipationTable() {
		Connection con = DBConnection.connection();
		Statement stmt;
		
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("DROP TABLE User_BusinessObject");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}