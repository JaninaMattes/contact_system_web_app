package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.google.appengine.api.rdbms.AppEngineDriver;
import com.google.appengine.api.utils.SystemProperty;

/**
 * Verwalten einer Verbindung zur Datenbank.
 * Aufbau der Klasse nach dem Singleton Pattern.
 * Dadurch wird bei jedem Datenbankaufruf die selbe Datenbankverbindung verwendet.
 * In der Klasse wird jeh nach Laufzeitumgebung(Lokal, Server) die dafür 
 * vorgesehene Datenbank verwendet.  
 * Auf die Goolge Cloud SQL Datenbank kann local nicht zugegriffen werden.
 * 
 * @author Oliver Gorges
 *
 */

public class DBConnection {
	
	 /**
     * Die Klasse DBConnection wird nur einmal instantiiert. 
     * Durch den Bezeichner <code>static</code> gilt diese Variable 
     * global für alle Instanzen der DBConnection. 
     * Die Variable speichert dabei die einzige Instanz dieser Klasse.
     */
	private static Connection con = null;
	
	
	// URL zur verbindung mit der Goolge und der lokalen Datenbank
	// Logindaten sollten im normalfall in ein Konfigurationsdokument ausgelagert werden.
	private static String googleUrl = "jdbc:google:mysql://itprojektss18-t9:europe-west3:itprojektss18-t9/ITProjekt?user=xxx&password=1234";
	private static String localUrl =  "jdbc:mysql://127.0.0.1/ITProjekt?user=ITProjekt&password=ITProjekt"; 

	
	/**
     * Diese statische Methode kann aufgrufen werden durch
     * <code>DBConnection.connection()</code>. Sie stellt die
     * Singleton-Eigenschaft sicher, indem Sie dafuer sorgt, dass nur eine einzige
     * Instanz von <code>DBConnection</code> existiert.<p>
     *
     * <b>Fazit:</b> DBConnection sollte nicht mittels <code>new</code>
     * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.<p>
     *
     * <b>Nachteil:</b> Bei Zusammenbruch der Verbindung zur Datenbank - dies kann
     * z.B. durch ein unbeabsichtigtes Herunterfahren der Datenbank ausgeloest
     * werden - wird keine neue Verbindung aufgebaut, so dass die in einem solchen
     * Fall die gesamte Software neu zu starten ist. In einer robusten Loesung
     * wuerde man hier die Klasse dahingehend modifizieren, dass bei einer nicht
     * mehr funktionsfuehigen Verbindung stets versucht wuerde eine neue Verbindung
     * aufzubauen. Dies wuerde allerdings ebenfalls den Rahmen dieses Projekts
     * sprengen.
     *
     * @return gültiges <code>DBConncetion</code>-Objekt.
     * @see con
     */
	public static Connection connection() {
		try {
		/**
		 *  Wenn noch keine Verbindung zur DB hergestellt wurde oder 
		 *  die aktuelle Verbindung nicht mehr gültig ist wird eine neue Verbindung aufgebaut.
		 */
			if ( con == null || !con.isValid(2000) ) {
				String url = null;
				
					// Laden des Treibers
					DriverManager.registerDriver(new AppEngineDriver());
					
					/**
					 * Überprüft ob das Projekt lokal in der Appengine läuft, oder ob das Projekt auf dem Google Server 
					 * läuft.
					 * Auf dem Google Server wird die Google Cloud SQL Datenbank verwendet.
					 * Bei localen Tests wird eine locale Datenbank mit Testdaten verwendet.
					 */
					if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
						
						 // Datenbank Treiber der zur Verbindung mit der lokalen Datenbank venötigt wird
	                    Class.forName("com.mysql.jdbc.GoogleDriver");
	                    url = googleUrl;
	                } else {
	                    // Datenbank Treiber der zur Verbindung mit der lokalen Datenbank venötigt wird
	                	Class.forName("com.mysql.jdbc.Driver");
	                    url = localUrl;
	                }
					
					// Treiber Manager baut Verbindung mit Informationen aus url auf
					con = DriverManager.getConnection(url);
				} 
			}
		catch (SQLException e1) {
			con = null;
			e1.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		// Rückgabe einer laufenden Datenbank verbindung
		return con;
	}

}