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
		Double userID = contact.getOwner().getGoogleID();
		Column ownerPropertyValue = new Column(administration.getUserByID(userID).getUserContact().getName().getValue());
		
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
		Column statusPropertyValue = new Column(status);
		statusRow.addColumn(statusProperty);
		statusRow.addColumn(statusPropertyValue);
		contactElement.addPropertyRow(statusRow);
		
		/**
		 * Liste mit Teilhabern erstellen
		 */	
		if(contact.isShared_status()) {
			Vector<Participation> participations = administration.getAllParticipationsByBusinessObject(contact);
			for(Participation singleParticipation : participations) {
				Double participantID = singleParticipation.getParticipant().getGoogleID();
				//momentan noch null, da Testkontakt zu User 666 keinen Namen hat
				String name = administration.getUserByID(participantID).getUserContact().getName().getValue();
				SimpleParagraph singleParticipant = new SimpleParagraph(name);
				contactElement.addElementToParticipantList(singleParticipant);
			}
		}else {
			SimpleParagraph noParticipant = new SimpleParagraph("Keine Teilhaber");
			contactElement.addElementToParticipantList(noParticipant);
		}
				
		//Hinzufügen des erstellten Elements zum Report
		report.addContactElement(contactElement);
		
		//TEST
		System.out.println("Kontakt " + contact.getBoId() + " hinzugefügt");
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
		report.setCreated(new Date());
		
		//Hinzufügen der einzelnen Kontakt-Elemente
		Vector<Contact> allContacts = administration.getAllContactsFromUser();
		if(allContacts.isEmpty()) {
			System.out.println("Keine Kontakte gefunden");
		}else {
			//TEST
			System.out.println("Alle Kontakte abgerufen");
			for(Contact singleContact : allContacts) {
				this.addSingleContact(singleContact, report);
			}
		}
		//TEST
		System.out.println("Report fertig erstellt.");
		
		/**
		 * Zurückgeben des erstellten Reports
		 */
		return report;
	}

	
	@Override
	public AllContactsForParticipantReport createAllContactsForParticipantReport(int participantId)
			throws IllegalArgumentException {
		
		System.out.println("Methode aufgerufen");
		//User-Objekt zu Namen zuordnen
		User searchedParticipant = administration.getUserByID(participantId);
		System.out.println("User gefunden");
		
		//Erstellen des noch leeren Reports
		AllContactsForParticipantReport report = new AllContactsForParticipantReport();
		System.out.println("Leerer Report erstellt");
				
		//Titel des Reports
		report.setTitle("Alle mit "
				+ searchedParticipant.getUserContact().getName().getValue() 
				+ " geteilten Kontakte des Nutzers");
		System.out.println("Titel gesetzt");
				
		//Daten des Benutzers, für den der Report erstellt wird
		this.addUserParagraph(getCurrentUser(), report);
				
		//Datum der Erstellung
		report.setCreated(new Date());
		
		//Alle Teilhaberschaften, die der aktuelle User hat, ermitteln
		Vector<Participation> allParticipations = administration.getAllParticipationsByOwner(this.getCurrentUser());
		//Alle Teilhaberschaften mit dem gesuchten Teilhaber ermitteln
		Vector<Participation> allParticipationsToParticipant = new Vector<Participation>();
		if(allParticipations.isEmpty()) {
			//TODO: Was wenn Nutzer keine Teilhaberschaften hat
			System.out.println("Nutzer hat keine Teilhaberschaften");
		}else {
			System.out.println("Teilhaberschaften des Nutzers abgerufen");
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
			System.out.println("Nutzer hat keine Teilhaberschaften mit Teilhaber");
		} else {
			System.out.println("Teilhaberschaften mit Teilhaber abgerufen");
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
			System.out.println("Nutzer hat keine geteilten Kontakte mit Teilhaber");
		}else {
			for(Contact singleContact : allContacts) {
				this.addSingleContact(singleContact, report);
			}
		}
		
		//TEST
		System.out.println("Report fertig erstellt.");
				
		
		/**
		 * Zurückgeben des erstellten Reports
		 */
		return report;
	}

	@Override
	public AllContactsForPropertyReport createAllContactsForPropertyReport(int propertyId, String propertyvalue)
			throws IllegalArgumentException {
		//Property-Objekt zu Id suchen
		Property searchedProperty = administration.getPropertyByID(propertyId);
		System.out.println("Property gefunden");
		
		//Erstellen des noch leeren Reports
		AllContactsForPropertyReport report = new AllContactsForPropertyReport();
				
		//Titel des Reports
		report.setTitle("Alle Kontakte des Nutzers mit der Eigenschaftsausprägung "
				+ searchedProperty.getDescription() + " = "
				+ propertyvalue);
				
		//Daten des Benutzers, für den der Report erstellt wird
		this.addUserParagraph(getCurrentUser(), report);
				
		//Datum der Erstellung
		report.setCreated(new Date());
		
		//Alle PropertyValues, die dem Suchtext entsprechen, ermitteln
		Vector<PropertyValue> allPVforString = administration.searchPropertyValues(propertyvalue);
		Vector<PropertyValue> allPropertyValuesForProperty = new Vector<PropertyValue>();
		//Alle PropertyValues, denen die richtige Property zugeordnet ist, ermitteln
		if(allPVforString.isEmpty()) {
			//TODO: Was keine passenden PropertyValues vorhanden
			System.out.println("Keine PV gefunden");
		} else {
			for(PropertyValue propertyValue : allPVforString) {
//				administration.getPropertyByID(propertyValue.getBoId());

				System.out.println(propertyValue.getProperty().getId());
				System.out.println(searchedProperty.getId());
				if(propertyValue.getProperty().getId() == searchedProperty.getId()) {
					allPropertyValuesForProperty.add(propertyValue);
				}
			}
		}
		
		//Alle Kontakte für die gefundenen Property-Values finden
		Vector<Contact> allContacts = new Vector<Contact>();
		if(allPropertyValuesForProperty.isEmpty()) {
			//TODO: Was keine passenden Properties vorhanden
			System.out.println("Keine passenden Properties gefunden");
		} else {
			for(PropertyValue propertyValue : allPropertyValuesForProperty) {
				Contact c = administration.getContactByPropertyValue(propertyValue);
				allContacts.add(c);
			}
		}
		
		//Gefundene Kontakte nach dem Eigentümer (currentUser) filtern
		Vector<Contact> foundContacts = new Vector<Contact>();
		if(allContacts.isEmpty()) {
			//TODO
			System.out.println("Keine Kontakte gefunden");
		} else {
			for(Contact contact : allContacts) {
				System.out.println(contact.getOwner());
				if(contact.getOwner().getGoogleID() == this.getCurrentUser().getGoogleID()) {
					foundContacts.add(contact);
				}
			}
		}
				
		//Hinzufügen der einzelnen Kontakt-Elemente
		
		if(foundContacts.isEmpty()) {
			System.out.println("Keine Kontakte mit PropertyValue gefunden");
		}else {
			for(Contact singleContact : foundContacts) {
				this.addSingleContact(singleContact, report);
			}
		}
		
		//TEST
		System.out.println("Report fertig erstellt.");			
		
		/**
		 * Zurückgeben des erstellten Reports
		 */
		return report;
	}

	@Override
	public Vector<Property> getAllProperties() throws IllegalArgumentException {
		return administration.getAllProperties();
	}

	@Override
	public Vector<User> getAllUsers() throws IllegalArgumentException {
		return administration.getAllUsers();
	}
	
}
