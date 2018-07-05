package de.hdm.kontaktsystem.server.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
 * Diese Klasse bildet die Applikationslogik des ReportGenerators ab und 
 * entspricht damit der Klasse {@link ContactSystemAdministrationImpl}. 
 * Weitere Details zu Funktion und Umsetzung von Service-Klasssen siehe dort.
 * 
 * @see ReportGenerator
 * @see ContactSystemAdministrationImpl
 * @author Sandra Prestel
 */
public class ReportGeneratorImpl extends RemoteServiceServlet implements ReportGenerator {

	private static final long serialVersionUID = 1L;

	/**
	 * Zugriff auf die Verwaltung des Kontaktsystems, da diese wichtige Methoden
	 * fuer die Koexistenz von Datenobjekten (vgl. bo-Package) bietet.
	 */
	private ContactSystemAdministration administration = null;
		
	/**
	 * Objekt, das ein Date-Objekt in einen String umwandeln kann, der das Format
	 * Tag.Monat.Jahr Stunde:Minute:Sekunde hat.
	 */
	private DateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	/**
	 * No-Argument-Konstruktor. Dieser wird bei der Client-seitigen Erzeugung mittels
	 * <code>GWT.create(Klassenname.class)</code> aufgerufen. Da hierbei keine Argumente
	 * uebergeben werden koennen, wird eine separate Instanzenmethode erstellt, die
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
		ContactSystemAdministrationImpl systemImpl = new ContactSystemAdministrationImpl();
		systemImpl.init();
		this.setAdministration(systemImpl);
	}
	
	
	/**
	 * Zurueckgeben der zugehoerigen ContactSystemAdministration
	 * @return Das ContactSystemAdministration-Objekt
	 */
	public ContactSystemAdministration getAdministration() {
		return this.administration;
	}

	/**
	 * Setzen der zugehoerigen ContactSystemAdministration
	 * @param administration ContactSystemAdministration-Objekt
	 */
	public void setAdministration(ContactSystemAdministration administration) {
		this.administration = administration;
	}

	
	/**
	 * Rueckgabe des aktuellen Users (ueber Login-Service)
	 * @return User-Objekt des aktuell eingeloggten Users
	 */
	public User getLoggedInUser(){
		return administration.getAccountOwner();
	}
	
	
	/**
	 * Login weiterleitung
	 */
	public User login(String requestUri){
		return administration.login(requestUri);
	}
	
	/**
	 * Erzeugt "Zelle" fuer einen einzelnen Kontakt. Diese Zelle enthaelt eine Tabelle 
	 * aus {@link Row}- und {@link Colum}-Objekten, die die einzelnen Eigenschaften und
	 * deren Auspraegungen des Kontaktes darstellen. Weiterhin ist in der Tabelle der 
	 * Eigentuemer des Kontakts und der Geteilt-Status ersichtlich. An die Tabelle schliesst
	 * sich eine Liste der Teilhaber, die Zugriff auf den Kontakt haben, an.
	 * Diese Methode wird von den anderen Methoden aufgerufen, die daraus die Reports zusammensetzen.
	 */
	private void addSingleContact(Contact contact, Report report) {
		SingleContact contactElement = new SingleContact();
		Vector<PropertyValue> allPropertyValues = contact.getPropertyValues();
		
		/* Tabelle mit Eigenschaften und Auspraegungen erstellen */
		for(PropertyValue singleProperty : allPropertyValues) {
			Row row = new Row();
			Column property = new Column(singleProperty.getProperty().getDescription());
			Column propertyValue = new Column(singleProperty.getValue());
			row.addColumn(property);
			row.addColumn(propertyValue);
			contactElement.addPropertyRow(row);
		}
		
		/* Name des Eigentuemers zur Liste der Eigenschaften hinzufuegen */
		Row ownerRow = new Row();
		Column ownerProperty = new Column("Eigentümer");
		String ownerName = contact.getOwner().getUserContact().getName().getValue();
		Column ownerPropertyValue = new Column(ownerName);
		
		ownerRow.addColumn(ownerProperty);
		ownerRow.addColumn(ownerPropertyValue);
		contactElement.addPropertyRow(ownerRow);
		
		/* Erstellungsdatum zur Liste der Eigenschaften hinzufuegen */
		Row createDateRow = new Row();
		Column createDateProperty = new Column("Erstellt");
		String createDateString = dateformat.format(contact.getCreationDate());
		Column createDateValue = new Column(createDateString);
		
		createDateRow.addColumn(createDateProperty);
		createDateRow.addColumn(createDateValue);
		contactElement.addPropertyRow(createDateRow);		
		
		/* Modifizierungsdatum zur Liste der Eigenschaften hinzufuegen */
		Row modifyDateRow = new Row();
		Column modifyDateProperty = new Column("Zuletzt geändert");
		String modifyDateString = dateformat.format(contact.getModifyDate());
		Column modifyDateValue = new Column(modifyDateString);
		
		modifyDateRow.addColumn(modifyDateProperty);
		modifyDateRow.addColumn(modifyDateValue);
		contactElement.addPropertyRow(modifyDateRow);		
		
		
		/* Status zur Tabelle hinzufuegen */
		Row statusRow = new Row();
		Column statusProperty = new Column("Status");
		String status = "";
		if(contact.getShared_status()) {
			status = "geteilt";
		}else {
			status = "nicht geteilt";
		}
		Column statusPropertyValue = new Column(status);
		statusRow.addColumn(statusProperty);
		statusRow.addColumn(statusPropertyValue);
		contactElement.addPropertyRow(statusRow);
		
		/* Liste mit Teilhabern erstellen */	
		if(contact.getShared_status()) {
			Vector<Participation> participations = administration.getAllParticipationsByBusinessObject(contact);
			for(Participation singleParticipation : participations) {
				String participantName = singleParticipation.getParticipant().getUserContact().getName().getValue();
				SimpleParagraph singleParticipant = new SimpleParagraph(participantName);
				contactElement.addElementToParticipantList(singleParticipant);
			}
		}else {
			SimpleParagraph noParticipant = new SimpleParagraph("Keine Teilhaber");
			contactElement.addElementToParticipantList(noParticipant);
		}
				
		/* Hinzufuegen des erstellten Elements zum Report */
		report.addContactElement(contactElement);
		
	}
	
	/**
	 * Erzeugt Paragraph mit den Daten des Nutzers, fuer den der Report erstellt wird. 
	 * Aus den Report-Methoden ausgelagert.
	 */
	private void addUserParagraph(User user, Report report) {
		SimpleParagraph userName = new SimpleParagraph(user.getUserContact().getName().getValue());
		SimpleParagraph userMail = new SimpleParagraph(user.getGMail());
		
		CompositeParagraph userInfo = new CompositeParagraph();
		userInfo.addSubParagraph(userName);
		userInfo.addSubParagraph(userMail);

		report.setUserData(userInfo);
	}
	

	@Override
	public AllContactsOfUserReport createAllContactsReport() throws IllegalArgumentException {
		
		/* Aktuell eingeloggter User */
		User currentUser = this.getLoggedInUser();
		
		//Erstellen des noch leeren Reports
		AllContactsOfUserReport report = new AllContactsOfUserReport();
		
		//Titel des Reports
		report.setTitle("Alle Kontakte");
		
		//Daten des Benutzers, fuer den der Report erstellt wird
		this.addUserParagraph(currentUser, report);
		
		//Datum der Erstellung
		Date currentDate = new Date();
		String currentDateString = dateformat.format(currentDate);
		report.setCreated("Erstellt: " + currentDateString);
		
		/* Abrufen aller Kontakte, auf die der Nutzer Zugriff hat */
		//Abrufen aller Kontakte, deren Eigentuemer der aktuelle User ist
		Vector<Contact> allContacts = administration.getAllContactsFromUser();
		//Abrufen aller Kontakte, die mit dem aktuellen User geteilt wurden
		Vector<Contact> allSharedContacts = administration.getAllCSharedByOthersToMe();
		
		//Hinzufuegen der einzelnen Kontakt-Elemente
		allContacts.addAll(allSharedContacts);
		if(allContacts.isEmpty()) {
			System.out.println("Keine Kontakte gefunden");
		}else {
			for(Contact singleContact : allContacts) {
				this.addSingleContact(singleContact, report);
			}
		}
		
		/* Zurueckgeben des erstellten Reports */
		return report;
	}


	@Override
	public AllContactsForParticipantReport createAllContactsForParticipantReport(double participantId)
			throws IllegalArgumentException {
		
		/* Aktuell eingeloggter User */
		User currentUser = this.getLoggedInUser();
		
		//User-Objekt zu Namen zuordnen
		User searchedParticipant = administration.getUserByID(participantId);
		
		//Erstellen des noch leeren Reports
		AllContactsForParticipantReport report = new AllContactsForParticipantReport();
				
		//Titel des Reports
		report.setTitle("Alle mit "
				+ searchedParticipant.getUserContact().getName().getValue() 
				+ " geteilten Kontakte");
				
		//Daten des Benutzers, fuer den der Report erstellt wird
		this.addUserParagraph(currentUser, report);
				
		//Datum der Erstellung
		Date currentDate = new Date();
		String currentDateString = dateformat.format(currentDate);
		report.setCreated("Erstellt: " + currentDateString);
		
		Vector<Participation> allParticipations = new Vector<Participation>();		
		/**
		 * Falls der gesuchte Teilhaber der aktuelle User ist, werden alle Teilhaberschaften
		 * ermittelt, in denen er Teilhaber ist und mit dem naechsten Schritt weiter gemacht.
		 */
		if(searchedParticipant.getGoogleID() == currentUser.getGoogleID()) {
			//Alle Teilhaberschaften, bei denen der aktuelle User Teilhaber ist, ermitteln
			Vector<Participation> allreceivedParticipations = 
					administration.getAllParticipationsByParticipant(currentUser);
			//Teilhaberschaften, die sich auf Kontakte beziehen, herausfiltern
			for(Participation part : allreceivedParticipations) {
				if(part.getReferencedObject() instanceof Contact) {
					allParticipations.add(part);
				}
			}
					
		} else {
			/**
			 * Falls der gesuchte Teilhaber nicht der aktuelle User ist, werden zunaechst alle 
			 * Teilhaberschaften ermittelt, die sich auf Objekte beziehen, deren Eigentuemer der
			 * aktuelle User ist und die mit dem gesuchten User geteilt wurden. 
			 * Anschliessend werden alle Teilhaberschaften ermittelt, die der
			 * gesuchte und der aktuelle User gemeinsam haben. 
			 * Diese Vektoren werden dann kombiniert.
			 */
			//Alle Teilhaberschaften, die der aktuelle User besitzt, ermitteln
			Vector<Participation> allParticipationsOfUser = 
					administration.getAllParticipationsByOwner(currentUser);
			//Alle Teilhaberschaften mit dem gesuchten Teilhaber ermitteln
			allParticipations = new Vector<Participation>();
			if(allParticipationsOfUser.isEmpty()) {
				System.out.println("Nutzer besitzt keine Teilhaberschaften");
			}else {
				for(Participation participation : allParticipationsOfUser) {
					if(participation.getParticipant().getGoogleID() == searchedParticipant.getGoogleID()) {
						//Teilhaberschaften, die sich auf Kontakte beziehen, herausfiltern
						if(participation.getReferencedObject() instanceof Contact){
							allParticipations.add(participation);
						}						
					}
				}
			}
			
			
			//Alle Teilhaberschaften ermitteln, bei denen der aktuelle User Teilhaber ist
			Vector<Participation> allParticipationsWithUser = 
					administration.getAllParticipationsByParticipant(currentUser);	
			
			//Zugehoerige Kontakt-Objekte ermitteln
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
			//Vektoren zusammen fuehren
			allParticipations.addAll(allSharedParticipations);
		}
				
		
		
		//Ermitteln der Kontakte aus den Participation-Objekten
		Vector<Contact> allContacts = new Vector<Contact>();
		if(allParticipations.isEmpty()) {
			System.out.println("Nutzer hat keine Teilhaberschaften mit Teilhaber");
		} else {
			for(Participation participation : allParticipations) {
				//Sicherheitshalber Prüfung, auch wenn normalerweise nur noch Teilhaberschaften 
				//an Kontakten drin sein duerften
				if(participation.getReferencedObject() instanceof Contact) {
					Contact contact = (Contact) participation.getReferencedObject();
					allContacts.add(contact);
				}
			}
		}
		
		//Hinzufuegen der einzelnen Kontakt-Elemente
		if(allContacts.isEmpty()) {
			System.out.println("Nutzer hat keine geteilten Kontakte mit Teilhaber");
		}else {
			for(Contact singleContact : allContacts) {
				this.addSingleContact(singleContact, report);
			}
		}
					
		/* Zurueckgeben des erstellten Reports */
		return report;
	}

	@Override
	public AllContactsForPropertyReport createAllContactsForPropertyReport(int propertyId, String propertyvalue)
			throws IllegalArgumentException {
		
		/* Aktuell eingeloggter User */
		User currentUser = this.getLoggedInUser();
		
		//Property-Objekt zu Id suchen
		Property searchedProperty = administration.getPropertyByID(propertyId);
		
		//Erstellen des noch leeren Reports
		AllContactsForPropertyReport report = new AllContactsForPropertyReport();
				
		//Titel des Reports
		report.setTitle("Alle Kontakte mit der Eigenschaftsausprägung "
				+ searchedProperty.getDescription() + " = "
				+ propertyvalue);
				
		//Daten des Benutzers, fuer den der Report erstellt wird
		this.addUserParagraph(currentUser, report);
				
		//Datum der Erstellung
		Date currentDate = new Date();
		String currentDateString = dateformat.format(currentDate);
		report.setCreated("Erstellt: " + currentDateString);
		
		//Alle PropertyValues, die dem Suchtext entsprechen, ermitteln
		Vector<PropertyValue> allPVforString = administration.searchPropertyValues(propertyvalue);
		Vector<PropertyValue> allPropertyValuesForProperty = new Vector<PropertyValue>();
		
		//Alle PropertyValues, denen die richtige Property zugeordnet ist, ermitteln
		if(allPVforString.isEmpty()) {
			System.out.println("Keine PV gefunden");
		} else {
			for(PropertyValue propertyValue : allPVforString) {
				if(propertyValue.getProperty().getId() == searchedProperty.getId()) {
					allPropertyValuesForProperty.add(propertyValue);
				}
			}
		}
		
		//Alle Kontakte fuer die gefundenen Property-Values finden
		Vector<Contact> allContacts = new Vector<Contact>();
		if(allPropertyValuesForProperty.isEmpty()) {
			System.out.println("Keine passenden Properties gefunden");
		} else {
			for(PropertyValue propertyValue : allPropertyValuesForProperty) {
				Contact c = administration.getContactByPropertyValue(propertyValue);
				allContacts.add(c);
			}
		}
		
		/**
		 * Es werden alle Kontakte angezeigt, bei denen der aktuelle User entweder
		 * Eigentuemer oder Teilhaber ist.
		 * Zunaechst werden die Kontakte nach Eigentuemer = aktueller User gefiltert, die
		 * Ergebnisse werden im Vektor "foundContacts" gespeichert.
		 * Dann werden dieselben Kontakte noch einmal gefiltert, um diejenigen 
		 * zu finden, bei denen der aktuelle User Teilhaber ist. Diese werden im
		 * Vektor "participatedContacts" zwischengespeichert und anschliessend dem 
		 * Ergebnisvektor "foundContacts" hinzugefuegt.
		 */
		Vector<Contact> foundContacts = new Vector<Contact>();
		Vector<Contact> participatedContacts = new Vector<Contact>();
		if(allContacts.isEmpty()) {
			System.out.println("Keine Kontakte gefunden");
		} else {			
			for(Contact contact : allContacts) {			
		
				//Gefundene Kontakte nach dem Eigentuemer (currentUser) filtern
				if(contact.getOwner().getGoogleID() == currentUser.getGoogleID()) {
					foundContacts.add(contact);
				}
				
				//Gefundene Kontakte nach dem Teilhaber (currentUser) filtern
				Vector<Participation> participationsForContact = 
						administration.getAllParticipationsByBusinessObject(contact);
				if(participationsForContact.isEmpty()) {
					System.out.println("Keine Teilhaber fuer Kontakt gefunden");
				} else {
					for(Participation part : participationsForContact) {
						if(part.getParticipant().getGoogleID() == currentUser.getGoogleID()) {
							participatedContacts.add(contact);
						}
					}
				}
			}
		}
		
		//Vektoren vereinen
		foundContacts.addAll(participatedContacts);		
				
		//Hinzufuegen der einzelnen Kontakt-Elemente		
		if(foundContacts.isEmpty()) {
			System.out.println("Keine Kontakte mit PropertyValue gefunden");
		}else {
			for(Contact singleContact : foundContacts) {
				this.addSingleContact(singleContact, report);
			}
		}		
		
		/* Zurueckgeben des erstellten Reports */
		return report;
	}

	@Override
	public Vector<Property> getAllProperties() throws IllegalArgumentException {
		return administration.getAllProperties();
	}

	@Override
	public Vector<User> getAllUsers() throws IllegalArgumentException {
		return administration.findAllKnownUsers();
	}
	
}
