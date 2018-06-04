package de.hdm.kontaktsystem.server.report;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.kontaktsystem.server.ContactSystemAdministrationImpl;
import de.hdm.kontaktsystem.shared.ContactSystemAdministration;
import de.hdm.kontaktsystem.shared.ReportGenerator;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.User;
import de.hdm.kontaktsystem.shared.report.AllContactsForParticipantReport;
import de.hdm.kontaktsystem.shared.report.AllContactsForPropertyReport;
import de.hdm.kontaktsystem.shared.report.AllContactsOfUserReport;

/**
 * Implementierung des <code>ReportGenerator</code>-Interface.
 * 
 * @see ReportGenerator
 * @author Sandra
 */
public class ReportGeneratorImpl extends RemoteServiceServlet implements ReportGenerator {

	private static final long serialVersionUID = 1L;

	/**
	 * Zugriff auf die Verwaltung des Kontaktsystems, da diese wichtige Methoden
	 * für die Koexistenz von Datenobjekten (vgl. bo-Package) bietet.
	 */
	private ContactSystemAdministration administration = null;
	
	private User currentUser = null;
	
	/**
	 * No-Argument-Konstruktor. Dieser wird bei der Client-seitigen Erzeugung mittels
	 * <code>GWT.create(Klassenname.class)</code> aufgerufen. Da hierbei keine Argumente
	 * übergeben werden können, wird eine separate Instanzenmethode erstellt, die
	 * Client-seitig direkt nach <code>GWT.create(Klassenname.class)</code>
	 * aufgerufen wird, um eine Initialisierung der Instanz vorzunehmen.
	 * 
	 * @see #init()
	 */
	public ReportGeneratorImpl() {		
	}
	
	/**
	 * Initialisierungsmethode
	 * 
	 * @see #ReportGeneratorImpl()
	 */
	public void init() throws IllegalArgumentException {
		//TODO Klären, ob hier ein User-Objekt gespeichert wird, das den aktuell eingeloggten User darstellt
		
		ContactSystemAdministrationImpl systemImpl = new ContactSystemAdministrationImpl();
		systemImpl.init();
		this.setAdministration(systemImpl);
	}
	
	public void setUserInfo(User userInfo) {
		this.currentUser = userInfo;
	}
	
	/**
	 * Zurückgeben der zugehörigen ContactSystemAdministration
	 * @return Das ContactSystemAdministration-Objekt
	 */
	public ContactSystemAdministration getAdministration() {
		return this.administration;
	}

	/**
	 * Setzen der zugehörigen ContactSystemAdministration
	 * @param administration ContactSystemAdministration-Objekt
	 */
	public void setAdministration(ContactSystemAdministration administration) {
		this.administration = administration;
	}

	
	
	@Override
	public AllContactsOfUserReport createAllContactsReport() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AllContactsForParticipantReport createAllContactsForParticipantReport(String participant)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AllContactsForPropertyReport createAllContactsForPropertyReport(String property, String propertyvalue)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Property> getAllProperties() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
