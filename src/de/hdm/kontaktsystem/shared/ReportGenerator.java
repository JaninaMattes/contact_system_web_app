package de.hdm.kontaktsystem.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
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
	  
	  // TODO: Anpassen
	  

}
