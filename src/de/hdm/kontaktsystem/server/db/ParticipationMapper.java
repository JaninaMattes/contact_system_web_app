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


/**
 * Die Mapper-Klasse <code>ParticipationMapper</code> bildet <code>Participation</code>-Objekte 
 * auf eine relationale Datenbank ab. Dazu werden Methoden zum Erzeugen, Suchen, Ändern und 
 * Löschen von Objekten zur Verfügung gestellt. Es können sowohl Objekte in Datenbank-Strukturen, 
 * als auch Datenbank-Strukturen in Objekte überführt werden.
 * 
 * @author Sandra
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
				
				Participation p = new Participation();
				User participant = UserMapper.userMapper().findById(rs.getDouble("User_ID"));
				p.setParticipant(participant);
				BusinessObject reference = BusinessObjectMapper.businessObjectMapper()
						.findBy(rs.getInt("BusinessObject_ID"));
				p.setReference(reference);
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
	 * Zurückgeben aller Teilhaberschaften zu Objekten eines gegebenen Users
	 * @return Teilhaberschaften als Participation-Objekte in einem Vector
	 */
	public Vector<Participation> findParticipationsByOwner(User user) {
		Connection con = DBConnection.connection();
		System.out.println("Methode findParticipation..");
		
		try {
			// Create Vector for all Participation-Objects
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
				
				User participant = UserMapper.userMapper().findById(rs.getDouble("User_ID"));
				participation.setParticipant(participant);
				
				reference = BusinessObjectMapper.businessObjectMapper().findBy(rs.getInt("bo_ID"));
				participation.setReference(reference);
				
				participations.add(participation);
				
				System.out.println("Participant id: " + participant.getGoogleID());
				System.out.println("Participation object id: " + reference.getBo_Id());
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
				Participation p = new Participation();
				User participant = UserMapper.userMapper().findById(rs.getDouble("User_ID"));
				p.setParticipant(participant);
				BusinessObject reference = BusinessObjectMapper.businessObjectMapper().findBy(rs.getInt("BusinessObject_ID"));
				p.setReference(reference);
				participations.add(p);
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
			stmt.setInt(1, businessObject.getBo_Id());
			ResultSet rs = stmt.executeQuery();
			
			//Transfer all Participations from database to Participation-Objects
			while(rs.next()) {
				Participation p = new Participation();
				User participant = UserMapper.userMapper().findById(rs.getDouble("User_ID"));
				p.setParticipant(participant);
				BusinessObject reference = BusinessObjectMapper.businessObjectMapper().findBy(rs.getInt("BusinessObject_ID"));
				p.setReference(reference);
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
	 * Einfügen einer neuen Teilhaberschaft in die Datenbank
	 * @param participation
	 */
	public void insertParticipation(Participation participation) {
		Connection con = DBConnection.connection();
		
		try {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO User_BusinessObject (BusinessObject_ID, User_ID) VALUES (?, ?)");
			stmt.setInt(1, participation.getReferenceID());
			stmt.setDouble(2, participation.getParticipantID());
			stmt.execute();
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		BusinessObjectMapper.businessObjectMapper().setStatusTrue(participation.getReferenceID());
	}
	
	
	/**
	 * Löschen aller Teilhaberschaften
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
	 * Löschen aller Teilhaberschaften, die mit einem gegebenen User geteilt werden
	 * @param participant
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
			
			//Prüfen, ob es zu dem geteilten Objekt noch eine Teilhaberschaft gibt, 
			//wenn nicht, Status (geteilt) des Objekt auf false setzen
			Vector<Participation> part = findParticipationsByParticipant(p.getParticipant());
			if(part.isEmpty()) {
				BusinessObjectMapper.businessObjectMapper().setStatusFalse(p.getReferenceID());
			}
		}
	}
	
	
	/**
	 * Löschen aller Teilhaberschaften, die sich auf ein gegebenes BusinessObject beziehen
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
		BusinessObjectMapper.businessObjectMapper().setStatusFalse(businessObject.getBo_Id());
	}
	
	
	
}