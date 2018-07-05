package de.hdm.kontaktsystem.shared;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.User;
import de.hdm.kontaktsystem.shared.report.AllContactsForParticipantReport;
import de.hdm.kontaktsystem.shared.report.AllContactsForPropertyReport;
import de.hdm.kontaktsystem.shared.report.AllContactsOfUserReport;

/**
 * Synchrone Schnittstelle fuer eine RPC-fuehige Klasse zur Erstellung von Reports.
 * 
 * @author Sandra Prestel
 */
@RemoteServiceRelativePath("reportgenerator")
public interface ReportGenerator extends RemoteService {
	
	   /**
	   * Die Methode <code>init</code> ermoeglicht die Initialisierung des Objekts. 
	   * Sie ist zusaetzlich zum <em>No Argument Konstruktor</em> der implementierenden
	   * Klasse, <code>ContactSystemImpl</code> notwendig fuer GWT RPC. 
	   * Diese Methode wird direkt nach der Instantiierung aufgerufen.
	   * 
	   * @throws IllegalArgumentException
	   */
	  public void init() throws IllegalArgumentException;
	  

	  /**
	   * Mehtode zum einloggen des Nutzers in den Reportgenerator 
	   */
	  public User login(String requestUri);
	  
	  /**
	   * Erstellen eines AllContactsOfUserReport. Dieser Report stellt alle Kontakte dar,
	   * auf die ein Nutzer Zugriff hat, durch Besitz oder Teilhaberschaft.
	   * 
	   * @return Das fertige Reportobjekt
	   * @throws IllegalArgumentException
	   * @see AllContactsOfUserReport
	   */
	  public AllContactsOfUserReport createAllContactsReport() throws IllegalArgumentException;
	  
	  /**
	   * Erstellen eines AllContactsForParticipantReport. Dieser Report stellt alle Kontakte dar,
	   * auf die ein Nutzer Zugriff hat, und die mit einem definierten Nutzer geteilt wurden.
	   * 
	   * @param participantId GoogleID des Teilhabers, für den die mit ihm geteilten Kontakte angezeigt werden sollen
	   * @return Das fertige Reportobjekt
	   * @throws IllegalArgumentException
	   * @see AllContactsForParticipantReport
	   */
	  public AllContactsForParticipantReport createAllContactsForParticipantReport(double participantId) 
			  throws IllegalArgumentException;
	  
	  /**
	   * Erstellen eines AllContactsForPropertyReport. Dieser Report stellt alle Kontakte dar,
	   * auf die ein Nutzer Zugriff hat, und die eine definierte Eigenschaft und zugehoerige 
	   * Eigenschaftsauspraegung aufweisen.
	   * 
	   * @param propertyId ID der gesuchten Eigenschaft
	   * @param propertyvalue Die gesuchte Eigenschaftsauspraegung
	   * @return AllContactsForPropertyReport
	   * @throws IllegalArgumentException
	   */
	  public AllContactsForPropertyReport createAllContactsForPropertyReport(int propertyId, String propertyvalue) 
			  throws IllegalArgumentException;
	  
	  /**
	   * Abrufen aller aktuell vorhandenen Eigenschaften
	   * 
	   * @return Vector mit allen Properties
	   * @throws IllegalArgumentException
	   */
	  public Vector<Property> getAllProperties() throws IllegalArgumentException;
	  
	  
	  /**
	   * Abrufen aller aktuell vorhandenen User
	   * 
	   * @return Vector mit allen Usern
	   * @throws IllegalArgumentException
	   */
	  public Vector<User> getAllUsers() throws IllegalArgumentException;
	  
	  
}
