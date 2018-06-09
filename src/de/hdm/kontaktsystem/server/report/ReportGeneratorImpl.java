package de.hdm.kontaktsystem.server.report;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.kontaktsystem.server.ContactSystemAdministrationImpl;
import de.hdm.kontaktsystem.shared.ContactSystemAdministration;
import de.hdm.kontaktsystem.shared.ReportGenerator;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
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
		report.setTitle("Alle Kontakte");
		
		//Daten des Benutzers, für den der Report erstellt wird
		this.addUserParagraph(getCurrentUser(), report);
		
		//Datum der Erstellung
		report.setCreated(new Date());
		
		//Abrufen aller Kontakte, deren Eigentümer der aktuelle User ist
		Vector<Contact> allContacts = administration.getAllContactsFromUser();
		//Abrufen aller Kontakte, die mit dem aktuellen User geteilt wurden
		Vector<Contact> allSharedContacts = administration.findAllCSharedByOthersToMe();
		
		//Hinzufügen der einzelnen Kontakt-Elemente
		allContacts.addAll(allSharedContacts);
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
	public AllContactsForParticipantReport createAllContactsForParticipantReport(double participantId)
			throws IllegalArgumentException {
		
		System.out.println("Methode aufgerufen");
		//User-Objekt zu Namen zuordnen
		User searchedParticipant = administration.getUserByID(participantId);
		System.out.println("User gefunden, ID: " + searchedParticipant.getGoogleID());
		
		//Erstellen des noch leeren Reports
		AllContactsForParticipantReport report = new AllContactsForParticipantReport();
				
		//Titel des Reports
		report.setTitle("Alle mit "
				+ searchedParticipant.getUserContact().getName().getValue() 
				+ " geteilten Kontakte");
		System.out.println("Titel gesetzt");
				
		//Daten des Benutzers, für den der Report erstellt wird
		this.addUserParagraph(getCurrentUser(), report);
				
		//Datum der Erstellung
		report.setCreated(new Date());
		
		Vector<Participation> allParticipations = null;		
		/*
		 * Falls der gesuchte Teilhaber der aktuelle User ist, werden alle Teilhaberschaften
		 * ermittelt, in denen er Teilhaber ist und mit dem nächsten Schritt weiter gemacht.
		 */
		if(searchedParticipant.getGoogleID() == this.getCurrentUser().getGoogleID()) {
			//Alle Teilhaberschaften, bei denen der aktuelle User Teilhaber ist, ermitteln
			allParticipations = 
					administration.getAllParticipationsByParticipant(this.getCurrentUser());
		} else {
			/*
			 * Falls der gesuchte Teilhaber nicht der aktuelle User ist, werden zunächst alle 
			 * Teilhaberschaften ermittelt, die sich auf Objekte beziehen, deren Eigentümer der
			 * aktuelle User ist und die mit dem gesuchten User geteilt wurden. 
			 * Anschließend werden alle Teilhaberschaften ermittelt, die der
			 * gesuchte und der aktuelle User gemeinsam haben. 
			 * Diese Vectoren werden dann kombiniert.
			 */
			//Alle Teilhaberschaften, die der aktuelle User besitzt, ermitteln
			Vector<Participation> allParticipationsOfUser = 
					administration.getAllParticipationsByOwner(this.getCurrentUser());
			//Alle Teilhaberschaften mit dem gesuchten Teilhaber ermitteln
			allParticipations = new Vector<Participation>();
			if(allParticipationsOfUser.isEmpty()) {
				System.out.println("Nutzer besitzt keine Teilhaberschaften");
			}else {
				System.out.println("Teilhaberschaften des Nutzers abgerufen");
				for(Participation participation : allParticipationsOfUser) {
					if(participation.getParticipant().getGoogleID() == searchedParticipant.getGoogleID()) {
						allParticipations.add(participation);
					}
				}
			}
			
			
			//Alle Teilhaberschaften ermitteln, bei denen der aktuelle User Teilhaber ist
			Vector<Participation> allParticipationsWithUser = 
					administration.getAllParticipationsByParticipant(this.getCurrentUser());	
			
			//Zugehörige Kontakt-Objekte ermitteln
			Vector<Contact> allContacts= new Vector<Contact>();
			if(allParticipationsWithUser.isEmpty()) {
				System.out.println("Keine Teilhaberschaften mit aktuellem User gefunden");
			} else {
				for(Participation part : allParticipationsWithUser) {				
					if(part.getReferencedObject() instanceof Contact) {
						allContacts.addElement((Contact) part.getReferencedObject());
					}				
				}
			}	
			
			//Alle Teilhaberschaften raus filtern, bei denen auch der gesuchte User Teilhaber ist
			Vector<Participation> allSharedParticipations = new Vector<Participation>();
			if(allContacts.isEmpty()) {
				System.out.println("Keine mit aktuellem User geteilten Kontakte gefunden");
			} else {
				for(Contact contact : allContacts) {
					Vector<Participation> participationsForContact = 
							administration.getAllParticipationsByBusinessObject(contact);
					for(Participation participation : participationsForContact) {
						if(participation.getParticipant().getGoogleID() == searchedParticipant.getGoogleID()) {
							allSharedParticipations.add(participation);
						}
					}
				}
			}		
			
			//Vektoren zusammen führen
			allParticipations.addAll(allSharedParticipations);
		}
				
		
		
		//Alle Teilhaberschaften von Kontakten ermitteln
		Vector<Contact> allContacts = new Vector<Contact>();
		if(allParticipations.isEmpty()) {
			System.out.println("Nutzer hat keine Teilhaberschaften mit Teilhaber");
		} else {
			System.out.println("Teilhaberschaften mit Teilhaber abgerufen");
			for(Participation participation : allParticipations) {
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
		
		//Erstellen des noch leeren Reports
		AllContactsForPropertyReport report = new AllContactsForPropertyReport();
				
		//Titel des Reports
		report.setTitle("Alle Kontakte mit der Eigenschaftsausprägung "
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
		
		/*
		 * Es werden alle Kontakte angezeigt, bei denen der aktuelle User entweder
		 * Eigentümer oder Teilhaber ist.
		 * Zunächst werden die Kontakte nach Eigentümer = aktueller User gefiltert, die
		 * Ergebnisse werden im Vektor "foundContacts" gespeichert.
		 * Dann werden dieselben Kontakte noch einmal gefiltert, um diejenigen 
		 * zu finden, bei denen der aktuelle User Teilhaber ist. Diese werden im
		 * Vektor "participatedContacts" zwischengespeichert und anschließend dem 
		 * Ergebnisvektor "foundContacts" hinzugefügt.
		 */
		Vector<Contact> foundContacts = new Vector<Contact>();
		Vector<Contact> participatedContacts = new Vector<Contact>();
		if(allContacts.isEmpty()) {
			//TODO
			System.out.println("Keine Kontakte gefunden");
		} else {
			
			for(Contact contact : allContacts) {
				System.out.println(contact.getOwner());
				
				//Gefundene Kontakte nach dem Eigentümer (currentUser) filtern
				if(contact.getOwner().getGoogleID() == this.getCurrentUser().getGoogleID()) {
					foundContacts.add(contact);
				}
				
				//Gefundene Kontakte nach dem Teilhaber (currentUser) filtern
				Vector<Participation> participationsForContact = 
						administration.getAllParticipationsByBusinessObject(contact);
				if(participationsForContact.isEmpty()) {
					System.out.println("Keine Teilhaber für Kontakt gefunden");
				} else {
					for(Participation part : participationsForContact) {
						if(part.getParticipant().getGoogleID() == this.getCurrentUser().getGoogleID()) {
							participatedContacts.add(contact);
						}
					}
				}
			}
		}

		//Vektoren vereinen
		foundContacts.addAll(participatedContacts);
		
				
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
