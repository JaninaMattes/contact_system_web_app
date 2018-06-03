package de.hdm.kontaktsystem.shared;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.report.AllContactsForParticipantReport;
import de.hdm.kontaktsystem.shared.report.AllContactsForPropertyReport;
import de.hdm.kontaktsystem.shared.report.AllContactsOfUserReport;

/**
 * Synchrone Schnittstelle für eine RPC-fähige Klasse zur Erstellung von
 * Reports.
 * 
 * @author Sandra
 */
@RemoteServiceRelativePath("reportgenerator")
public interface ReportGenerator extends RemoteService {
	
	   /**
	   * Die Methode <code>init</code> ermöglicht die Initialisierung des Objekts. 
	   * Sie ist zusätzlich zum <em>No Argument Konstruktor</em> der implementierenden
	   * Klasse, <code>ContactSystemImpl</code> notwendig für GWT RPC. 
	   * Diese Methode wird direkt nach der Instantiierung aufgerufen.
	   * 
	   * @throws IllegalArgumentException
	   */
	  public void init() throws IllegalArgumentException;
	  
	  /**
	   * Erstellen eines AllContactsOfUserReport. Dieser Report stellt alle Kontakte
	   * eines Users dar.
	   * 
	   * @return Das fertige Reportobjekt
	   * @throws IllegalArgumentException
	   * @see AllContactsOfUserReport
	   */
	  public AllContactsOfUserReport createAllContactsReport() throws IllegalArgumentException;
	  
	  /**
	   * Erstellen eines AllContactsForParticipantReport. Dieser Report stellt alle Kontakte
	   * des Users dar, die mit einem bestimmten User geteilt wurden.
	   * 
	   * @param participant Name des Teilhabers, für den die mit ihm geteilten Kontakte angezeigt werden sollen
	   * @return Das fertige Reportobjekt
	   * @throws IllegalArgumentException
	   * @see AllContactsForParticipantReport
	   */
	  public AllContactsForParticipantReport createAllContactsForParticipantReport(String participant) 
			  throws IllegalArgumentException;
	  
	  /**
	   * Erstellen eines AllContactsForPropertyReport. Dieser Report stellt alle Kontakte 
	   * des Users dar, die eine bestimmte Eigenschaftsausprägung aufweisen.
	   * 
	   * @param property Die gesuchte Eigenschaft
	   * @param propertyvalue Die gesuchte Eigenschaftsausprägung
	   * @return AllContactsForPropertyReport
	   * @throws IllegalArgumentException
	   */
	  public AllContactsForPropertyReport createAllContactsForPropertyReport(String property, String propertyvalue) 
			  throws IllegalArgumentException;
	  
	  /**
	   * Abrufen aller aktuell vorhandenen Eigenschaftsbezeichnungen
	   * 
	   * @return Vector mit allen Properties
	   * @throws IllegalArgumentException
	   */
	  public Vector<Property> getAllProperties() throws IllegalArgumentException;
	  
	  
	  
	  
	  

}
