package de.hdm.kontaktsystem.server.db;

import java.util.Vector;


import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.User;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

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
	private static ParticipationMapper INSTANCE = new ParticipationMapper();
	
	private static ParticipationMapper participationMapper = null;
	
	/**
	 * Der Konstruktor ist <code>privat</code>, um einen Zugriff von außerhalb der Klasse zu verhindern.
	 */
	private ParticipationMapper() {
	}
	
	
	/**
	 * Diese Methode gibt das einzige Objekt dieser Klasse zurück.
	 * @return Instanz des ParticipationMapper
	 */
	public static ParticipationMapper getInstance(){
		return INSTANCE;
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
  
	
	public Vector<Participation> getAllParticipations(){
		//TODO Implement Method
		return null;
	}
	
	
	public Participation getParticipationById(int id) {
		//TODO Implement Method
		return null;
	}
	
	
	public Vector<Participation> getParticipationsByOwner(User owner) {
		//TODO Implement Method
		return null;
	}
	
	
	public Vector<Participation> getParticipationsByParticipant(User participant){
		//TODO Implement Method
		return null;
	}
	
	
	public Vector<Participation> getParticipationsByContact(Contact contact){
		//TODO Implement Method
		return null;
	}

	
	public void updateParticipation(Participation participation) {
		//TODO Implement Method
	}
	
	
	public void insertParticipation(Participation participation) {
		//TODO Implement Method
	}
	
	
	public void deleteParticipationForId(int id) {
		//TODO Implement Method
	}
	
	
	public void deleteParticipationForOwner(User owner) {
		//TODO Implement Method
	}
	
	
	public void deleteParticipationForParticipant(User participant) {
		//TODO Implement Method
	}
	
	
	public void deleteAllParticipations() {
		//TODO Implement Method
	}
	
	
	public void initParticipationTable() {
		//TODO Implement Method
	}
	
	
	public void deleteParticipationTable() {
		//TODO Implement Method
	}
	
}
