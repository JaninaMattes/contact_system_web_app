package de.hdm.kontaktsystem.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Die Kalsse <code>ReportGeneratorAsync</code> ist das asynchrone Gegenstück des Interface {@link ReportGenerator}. 
 * Es wird semiautomatisch durch das Google Plugin erstellt und gepflegt. Daher erfolgt hier keine weitere Dokumentation. 
 * Für weitere Informationen siehe das synchrone Interface {@link ReportGenerator}.
 * 
 */

public interface ReportGeneratorAsync {
	
	void init(AsyncCallback<Void> callback);
	
	// TODO: Anpassen

}
