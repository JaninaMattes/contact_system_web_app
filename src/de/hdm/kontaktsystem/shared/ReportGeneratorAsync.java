package de.hdm.kontaktsystem.shared;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.User;
import de.hdm.kontaktsystem.shared.report.AllContactsForParticipantReport;
import de.hdm.kontaktsystem.shared.report.AllContactsForPropertyReport;
import de.hdm.kontaktsystem.shared.report.AllContactsOfUserReport;

/**
 * Die Klasse <code>ReportGeneratorAsync</code> ist das asynchrone Gegenstück des Interface {@link ReportGenerator}. 
 * Es wird semiautomatisch durch das Google Plugin erstellt und gepflegt. Daher erfolgt hier keine weitere Dokumentation. 
 * Für weitere Informationen siehe das synchrone Interface {@link ReportGenerator}.
 * 
 */

public interface ReportGeneratorAsync {
	
	void init(AsyncCallback<Void> callback);
	
	void createAllContactsReport(AsyncCallback<AllContactsOfUserReport> callback);
	
	void createAllContactsForParticipantReport(String participant, 
			AsyncCallback<AllContactsForParticipantReport> callback);
	
	void createAllContactsForPropertyReport(String property, String propertyvalue,
			AsyncCallback<AllContactsForPropertyReport> callback);
	
	void getAllProperties(AsyncCallback<Vector<Property>> callback);

	void setUserInfo(User userInfo, AsyncCallback<Void> callback);

}
