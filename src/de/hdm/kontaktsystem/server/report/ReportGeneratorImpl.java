package de.hdm.kontaktsystem.server.report;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.kontaktsystem.server.ContactSystemAdministrationImpl;
import de.hdm.kontaktsystem.shared.ContactSystemAdministration;
import de.hdm.kontaktsystem.shared.ReportGenerator;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;
import de.hdm.kontaktsystem.shared.report.AllContactsForParticipantReport;
import de.hdm.kontaktsystem.shared.report.AllContactsForPropertyReport;
import de.hdm.kontaktsystem.shared.report.AllContactsOfUserReport;
import de.hdm.kontaktsystem.shared.report.Column;
import de.hdm.kontaktsystem.shared.report.CompositeParagraph;
import de.hdm.kontaktsystem.shared.report.Paragraph;
import de.hdm.kontaktsystem.shared.report.Report;
import de.hdm.kontaktsystem.shared.report.Row;
import de.hdm.kontaktsystem.shared.report.SimpleParagraph;
import de.hdm.kontaktsystem.shared.report.SingleContact;

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
	
	/**
	 * TODO: Klären, ob überflüssig
	 */
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
	
	/**
	 * Zurückgeben des aktuellen Users
	 * TODO: Klären, ob überflüssig
	 */
	public User getUserInfo() {
		return this.currentUser;
	}
	
	/**
	 * Setzen des aktuell eingeloggten Users
	 * TODO: Klären, ob überflüssig
	 */
	public void setUserInfo(User userInfo) {
		this.currentUser = userInfo;
	}

	/**
	 * Rückgabe des aktuellen Users (über Login-Service)
	 * @return User-Objekt des aktuell eingeloggten Users
	 */
	public User getCurrentUser(){
		// Test
		return administration.getUserByID(170d);//userService.getCurrentUser();
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

	/**
	 * Erzeugt "Zelle" für einen einzelnen Kontakt. Wird von den anderen Methoden aufgerufen,
	 * die daraus die Reports zusammensetzen.
	 */
	protected void addSingleContact(Contact contact, Report report) {
		SingleContact contactElement = new SingleContact();
		Vector<PropertyValue> allPropertyValues = administration.getPropertyValuesForContact(contact); //TODO: in Interfaces ergänzen
		
		for(PropertyValue singleProperty : allPropertyValues) {
			Row row = new Row();
			Column property = new Column(singleProperty.getProperty().getDescription());
			Column propertyValue = new Column(singleProperty.getValue());
			row.addColumn(property);
			row.addColumn(propertyValue);
			contactElement.addPropertyRow(row);
		}
		
		/**
		 * Name des Eigentümers zur Liste der Eigenschaften hinzufügen
		 */
		Row ownerRow = new Row();
		Column ownerProperty = new Column("Eigentümer");
		Column ownerPropertyValue = new Column(contact.getOwner().getUserContact().getName().getValue());
		ownerRow.addColumn(ownerProperty);
		ownerRow.addColumn(ownerPropertyValue);
		contactElement.addPropertyRow(ownerRow);
		
		/**
		 * Status hinzufügen
		 */
		Row statusRow = new Row();
		Column statusProperty = new Column("Status");
		String status = "";
		if(contact.isShared_status()) {
			status = "geteilt";
		}else {
			status = "nicht geteilt";
		}
		Column statusPropertyValue = new Column(contact.getOwner().getUserContact().getName().getValue());
		statusRow.addColumn(statusProperty);
		statusRow.addColumn(statusPropertyValue);
		contactElement.addPropertyRow(statusRow);
		
		/**
		 * Liste mit Teilhabern erstellen
		 */
		if(contact.isShared_status()) {
			Vector<Participation> participations = administration.getAllParticipationsByBusinessObject(contact); //TODO: in Interfaces ergänzen
			for(Participation singleParticipation : participations) {
				String name = singleParticipation.getParticipant().getUserContact().getName().getValue();
				SimpleParagraph singleParticipant = new SimpleParagraph(name);
				contactElement.addElementToParticipantList(singleParticipant);
			}
		}else {
			SimpleParagraph noParticipant = new SimpleParagraph("Keine Teilhaber");
		}
				
		//Hinzufügen des erstellten Elements zum Report
		report.addContactElement(contactElement);
	}
	
	/**
	 * Erzeugt Paragraph mit den User-Daten, für den der Report erstellt wird.
	 * Aus den Report-Methoden ausgelagert.
	 */
	protected void addUserParagraph(User user, Report report) {
		SimpleParagraph userName = new SimpleParagraph(user.getUserContact().getName().getValue());
		SimpleParagraph userMail = new SimpleParagraph(user.getGMail());
		
		CompositeParagraph userInfo = new CompositeParagraph();
		userInfo.addSubParagraph(userName);
		userInfo.addSubParagraph(userMail);

		report.setUserData(userInfo);
	}
	
	/**
	 * Erstellen eines AllContactsOfUserReport. Dieser Report stellt alle Kontakte
	 * eines Users dar.
	 * TODO: Alle mit dem User geteilte Kontakte mit aufnehmen
	 * 
	 * @return Das fertige Reportobjekt
	 * @throws IllegalArgumentException
	 * @see AllContactsOfUserReport
	 */
	@Override
	public AllContactsOfUserReport createAllContactsReport() throws IllegalArgumentException {
		
		//Erstellen des noch leeren Reports
		AllContactsOfUserReport report = new AllContactsOfUserReport();
		
		//Titel des Reports
		report.setTitle("Alle Kontakte des Nutzers");
		
		//Daten des Benutzers, für den der Report erstellt wird
		this.addUserParagraph(getCurrentUser(), report);
		
		//Datum der Erstellung
		report.setCreated(new Date()); //TODO: aktuelles Datum setzen
		
		//Hinzufügen der einzelnen Kontakt-Elemente
		Vector<Contact> allContacts = administration.getAllContactsFromUser();
		if(allContacts.isEmpty()) {
			//TODO: Fehlerbehandlung
		}else {
			for(Contact singleContact : allContacts) {
				this.addSingleContact(singleContact, report);
			}
		}
		
		/**
		 * Zurückgeben des erstellten Reports
		 */
		return report;
	}

	//TODO: DropDown-Liste in der GUI
	@Override
	public AllContactsForParticipantReport createAllContactsForParticipantReport(int participantId)
			throws IllegalArgumentException {
		//User-Objekt zu Namen zuordnen
		User searchedParticipant = administration.getUserByID(participantId);
		
		//Erstellen des noch leeren Reports
		AllContactsForParticipantReport report = new AllContactsForParticipantReport();
				
		//Titel des Reports
		report.setTitle("Alle mit "
				+ administration.getNameOfContact(searchedParticipant.getUserContact()) 
				+ " geteilten Kontakte des Nutzers");
				
		//Daten des Benutzers, für den der Report erstellt wird
		this.addUserParagraph(getCurrentUser(), report);
				
		//Datum der Erstellung
		report.setCreated(new Date()); //TODO: aktuelles Datum setzen
		
		//Alle Teilhaberschaften, die der aktuelle User hat, ermitteln
		Vector<Participation> allParticipations = administration.getAllParticipationsByOwner(currentUser);
		//Alle Teilhaberschaften mit dem gesuchten Teilhaber ermitteln
		Vector<Participation> allParticipationsToParticipant = new Vector<Participation>();
		if(allParticipations.isEmpty()) {
			//TODO: Was wenn Nutzer keine Teilhaberschaften hat
		}else {
			for(Participation participation : allParticipations) {
				if(participation.getParticipant().equals(searchedParticipant)) {
					allParticipationsToParticipant.add(participation);
				}
			}
		}
		
		//Alle Teilhaberschaften von Kontakten ermitteln
		Vector<Contact> allContacts = new Vector<Contact>();
		if(allParticipationsToParticipant.isEmpty()) {
			//TODO: Was wenn Nutzer keine Teilhaberschaften mit Teilhaber hat
		} else {
			for(Participation participation : allParticipationsToParticipant) {
				if(participation.getReferencedObject() instanceof Contact) {
					Contact contact = (Contact) participation.getReferencedObject();
					allContacts.add(contact);
				}
			}
		}
		
		//Hinzufügen der einzelnen Kontakt-Elemente
		if(allContacts.isEmpty()) {
			//TODO: Fehlerbehandlung
		}else {
			for(Contact singleContact : allContacts) {
				this.addSingleContact(singleContact, report);
			}
		}
				
		/**
		 * Zurückgeben des erstellten Reports
		 */
		return report;
	}

	@Override
	public AllContactsForPropertyReport createAllContactsForPropertyReport(String property, String propertyvalue)
			throws IllegalArgumentException {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Property> getAllProperties() throws IllegalArgumentException {
		return administration.getAllProperties();
	}
	
}
