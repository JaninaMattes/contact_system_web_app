package de.hdm.kontaktsystem.server;

import java.util.logging.Logger;
import de.hdm.kontaktsystem.shared.CommonSettings;

/**
 * <p>
 * Die Klasse ServersideSettings enthaelt alle Eigenschaften und Diensten, 
 * die fuer alle Server-seitigen Klassen relevant sind.
 * </p>
 * <p>
 * Hierdurch wird ein applikationszentraler Logger realisiert, 
 * der mittels<code>ServerSideSettings.getLogger()</code> genutzt werden kann.
 * </p>
 * <pre>
 * Nachrichten koennen beispielsweise ueber  logger.severe("");
 * erstellt werden. Ebenso kann auch <pre> logger.info(""); </pre>
 * angewandt werden. Hierbei muss aber auf angemessene Log Level geachtet werden. 
 * den Log Level kann man so "von aussen" durch die Datei <code>logging.properties</code> 
 * steuern.
 * </pre>
 */

public class ServersideSettings extends CommonSettings {
	
	/**
	 * <p>
	 * Auslesen des applikationsweiten, Server-seitigen zentralen Loggers.
	 * </p>
	 * <pre>
	 * Der Zugriff auf den Logger kann ueber den Aufruf:
	 * Logger logger = ServerSideSettings.getLogger(); hergestellt werden 
	 * </pre>
	 */
	
  private static final String LOGGER_NAME = "Contact System Server";
  private static final Logger log = Logger.getLogger(LOGGER_NAME);

  /**
   * Ein applikationszentraler Logger kann mittels <code> ServerSideSettings.getLogger() </code>
   * aufgerufen und genutzt werden. @return die Logger-Instanz fuer die Server-Seite
   */
  public static Logger getLogger() {
    return log;
  }

}
