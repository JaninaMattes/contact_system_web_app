package de.hdm.kontaktsystem.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import de.hdm.kontaktsystem.shared.CommonSettings;
import de.hdm.kontaktsystem.shared.ContactSystemAdministration;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.ReportGeneratorAsync;


/**
 * Die Klasse <code>ClientsideSettings</code> enthält wichtige Eigenschaften und Diensten,
 * welche für alle Clientseitigen Klassen von Relevanz sind. 
 *  
 */

public class ClientsideSettings extends CommonSettings {

  /**
   * Remote Service Proxy zur Verbindungsaufnahme mit dem Serverseitgen Dienst
   * namens <code>ContactSystemAdministration</code>.
   */

  private static ContactSystemAdministrationAsync contactSystemAdministration = null;

  /**
   * Remote Service Proxy zur Verbindungsaufnahme mit dem Server-seitgen Dienst
   * namens <code>ReportGenerator</code>.
   */
  private static ReportGeneratorAsync reportGenerator = null;

  /**
   * Name des Clientseitigen Loggers.
   */
  private static final String LOGGER_NAME = "ContactSystem Web Client";
  
  /**
   * Instanz des Clientseitigen Loggers.
   */
  private static final Logger log = Logger.getLogger(LOGGER_NAME);

  
  public static Logger getLogger() {
    return log;
  }

  /**
   * <p>
   * Anlegen und Auslesen der applikationsweit eindeutigen ContactSystemAdministration Objekts. Diese
   * Methode erstellt die ContactSystemAdministration, sofern sie noch nicht existiert. Bei
   * wiederholtem Aufruf dieser Methode wird stets das bereits zuvor angelegte
   * Objekt zurückgegeben.
   * </p>
   * 
   * <p>
   * Hinweis: Der Aufruf dieser Methode erfolgt im Client beispielsweise durch
   * <code>ContactSystemAdministrationAsync contactSystemAdministration = ClientSideSettings.getContactAdministration()</code>.
   * </p>
   * 
   */
  public static ContactSystemAdministrationAsync getContactAdministration() {
    
    if ( contactSystemAdministration == null) {
      // Instanziierung von ContactSystemAdministrationAsync
    	contactSystemAdministration = GWT.create(ContactSystemAdministration.class);
    }
    return contactSystemAdministration;
  }

 
  /*
   
   /
    
   * <p>
   * Anlegen und Auslesen des applikationsweit eindeutigen ReportGenerators.
   * Diese Methode erstellt den ReportGenerator, sofern dieser noch nicht
   * existiert. Bei wiederholtem Aufruf dieser Methode wird stets das bereits
   * zuvor angelegte Objekt zurückgegeben.
   * </p>
   * 
   * <p>
   * Der Aufruf dieser Methode erfolgt im Client z.B. durch
   * <code>ReportGeneratorAsync reportGenerator = ClientSideSettings.getReportGenerator()</code>.
   * </p> 
   * 
   *
  
  // TODO: ReportGenerator ausformulieren und Klassen erzeugen 
  
  public static ReportGeneratorAsync getReportGenerator() {
    // Gab es bislang noch keine ReportGenerator-Instanz, dann...
    if (reportGenerator == null) {
      reportGenerator = GWT.create(ReportGenerator.class);

      final AsyncCallback<Void> initReportGeneratorCallback = new AsyncCallback<Void>() {
        public void onFailure(Throwable caught) {
          ClientsideSettings.getLogger().severe(
              "The ReportGenerator could not be initialized!");
        }

        public void onSuccess(Void result) {
          ClientsideSettings.getLogger().info(
              "The ReportGenerator could be initialized succesfully.");
        }
      };

      reportGenerator.init(initReportGeneratorCallback);
    }
    
    return reportGenerator;
  }

  */
  
}
