package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import org.omg.PortableServer.ServantActivatorHelper;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.ContactSystemAdministration;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.ReportGeneratorAsync;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.User;
import de.hdm.kontaktsystem.shared.report.AllContactsForParticipantReport;
import de.hdm.kontaktsystem.shared.report.AllContactsForPropertyReport;
import de.hdm.kontaktsystem.shared.report.AllContactsOfUserReport;
import de.hdm.kontaktsystem.shared.report.HTMLReportWriter;


/**
 * Entry-Point-Klasse des Report-Generators im Projekt <b>Kontaktsystem</b>
 * 
 * @see de.hdm.thies.bankProjekt.client.gui.BankProjektReport
 * @author Sandra
 *
 */
public class ReportGenerator implements EntryPoint {

	ReportGeneratorAsync reportGenerator = null;
	
	/**
	 * Bilder: Symbol für die Such-Buttons, Logo im Header
	 */
	Image searchSymbol1 = new Image();
	Image searchSymbol2 = new Image();
	Image logo = new Image();
	
	/**
	 * Definition der grundlegenden Widgets des Report-Generators
	 */
	HorizontalPanel headerPanel = new HorizontalPanel();
	Label headerText = new Label("Report Generator");
	VerticalPanel navigationPanel = new VerticalPanel();
	Button showAllButton = new Button("Alle Kontakte");
	
	Label findByParticipantLabel = new Label("Nach Teilhaber filtern");
	HorizontalPanel findByParticipantPanel = new HorizontalPanel();
	ListBox usersDropDownList = new ListBox();
	PushButton findByParticipantButton = new PushButton(searchSymbol1);
	
	Label findByValueLabel = new Label("Nach Eigenschaftsausprägung filtern");
	ListBox propertiesDropDownList = new ListBox();
	HorizontalPanel findByValuePanel = new HorizontalPanel();
	TextBox findByValueText = new TextBox();
	PushButton findByValueButton = new PushButton(searchSymbol2); 
	
	
	/**
	 * Attribute für den Login
	 */
	private User userInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Melden Sie sich mit Ihrem Google Konto an, um auf das Kontaktsystem zuzugreifen.");
	private Anchor signInLink = new Anchor("Login");
	private Anchor signOutLink = new Anchor("Logout");
	private Anchor editorLink = new Anchor("Editor");				

	
	/**
	 * Da diese Klasse die Implementierung des Interface <code>EntryPoint</code>
	 * zusichert, benötigen wir eine Methode
	 * <code>public void onModuleLoad()</code>. Diese ist das GWT-Pendant der
	 * <code>main()</code>-Methode normaler Java-Applikationen.
	 */	
	@Override
	public void onModuleLoad() {
		
		loadReportGenerator(); //Test, solange Login nicht funktioniert
		
		/**
		 * Login-Status feststellen mit LoginService
		 *		
		ContactSystemAdministrationAsync contactSystemAdmin = ClientsideSettings.getContactAdministration();
		contactSystemAdmin.login(GWT.getHostPageBaseURL(), new AsyncCallback<User>() {
			public void onFailure(Throwable error) {
				Window.alert("Login Error :(");
			}
				
			//Wenn der User eingeloggt ist, wird die Startseite aufgerufen, andernfalls die Login-Seite
			public void onSuccess(User result) {
				userInfo = result;
				if(userInfo.isLoggedIn()){
					loadReportGenerator();
				}else{
					loadLogin();					
				}
			}
		});*/
	}
		
	/**
	 * Aufbau der Login-Seite
	 */
	private void loadLogin() {	
			
		Window.alert("Login :D");
		signInLink.setHref(userInfo.getLoginUrl());
		signInLink.getElement().setId("link");
		loginPanel.add(new HTML("<center>"));
		loginPanel.add(loginLabel);
		loginPanel.add(new HTML("<br /> <br /> "));
		loginPanel.add(signInLink);
		loginPanel.add(new HTML("</center>"));
		RootPanel.get("TopLevelFrame").add(loginPanel); //TODO: prüfen ob richtige HTML
	}
	
	
	/**
	 * Aufbau der Startseite des Report-Generators
	 */
	public void loadReportGenerator() {
		
		/**
		 * Zuweisung Asynchrones Interface
		 */
		if(reportGenerator == null) {
			reportGenerator = ClientsideSettings.getReportGenerator();
		}

		
		/**
		 * CSS
		 */
		
		//Labels in CSS
		findByParticipantLabel.getElement().setId("filtern");
		findByValueLabel.getElement().setId("filtern");
		loginLabel.getElement().setId("loginlabel");
		headerText.getElement().setId("headertext");
		
		//Textbox in CSS

		findByValueText.getElement().setId("findByTextbox");
		
		//DropDownList in CSS
		propertiesDropDownList.getElement().setId("ReportDropDownList");
		usersDropDownList.getElement().setId("ReportDropDownList");

		//Button in CSS
		//Der Search-Button bekommt den gleichen Style wie bei ContactSystem.java (Bessere Usability)
		findByParticipantButton.getElement().setId("searchButton");
		//Der Search-Button bekommt den gleichen Style wie die anderen Searchbuttons
		findByValueButton.getElement().setId("searchButton");
		showAllButton.getElement().setId("searchAllContacts");
	
		//Links
		signInLink.setStyleName("link");//??
		signOutLink.getElement().setId("log-out-button");
		editorLink.getElement().setId("switch-button");
		
		//Logo
		logo.getElement().setId("logo");

		/**
		 * Zuweisen von Bilddateien zu den Image-Elementen, Setzen der Größe
		 */
		//Logo
		logo.setUrl(GWT.getHostPageBaseURL() + "images/LogoWeiss.png");
		logo.setHeight("70px");
		logo.setAltText("Logo");
		//Such-Symbole
		searchSymbol1.setHeight("25px");
		searchSymbol1.setUrl(GWT.getHostPageBaseURL() + "images/search.png");
		searchSymbol1.setAltText("Suche");
		searchSymbol2.setHeight("25px");
		searchSymbol2.setUrl(GWT.getHostPageBaseURL() + "images/search.png");
		searchSymbol2.setAltText("Suche");
		editorLink.setHref(GWT.getHostPageBaseURL() + "ContactSystem.html");
		/**
		 * Aufbau der Oberfläche.
		 * Die Reportanwendung besteht aus einem "Navigationsteil" mit den
		 * Schaltflächen zum Auslösen der Reportgenerierung und einem "Datenteil"
		 * für die HTML-Version des Reports.
		 */
		
		/**
		 * Aufbau des Headers
		 */	
		headerPanel.add(logo);
		headerPanel.add(headerText);
		headerPanel.add(signOutLink);		
		headerPanel.add(editorLink);		
		
		RootPanel.get("Header").add(headerPanel);
		
		/**
		 * Aufbau der Navigation und der Detail-Ansicht
		 */
		/*
		 * Anfangsansicht des Detail-Fensters (rechte Bildschirmseite)
		 */
		Label noDetails = new Label("Kein Report ausgewählt");
		RootPanel.get("Details").add(noDetails);;
		
		/**
		 * Funktion: alle Kontakte anzeigen
		 */
		/*
		 * ShowAll-Button
		 */
		navigationPanel.add(showAllButton);
		
		showAllButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				reportGenerator.createAllContactsReport(new CreateAllContactsReportCallback());
			}
		});
		
		/**
		 * Funktion: nach Teilhaber filtern
		 */
		/*
		 * DropDown-Liste für alle User
		 */		
		reportGenerator.getAllUsers(new getAllUsersCallback());
		
		/*
		 * FindByParticipant-Button
		 */
		
		findByParticipantButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(usersDropDownList.getSelectedValue() == null) {
					Window.alert("Kein Teilhaber ausgewählt!");
				} else {
					double participantId = Double.parseDouble(usersDropDownList.getSelectedValue());
					reportGenerator.createAllContactsForParticipantReport(participantId,
							new CreateAllContactsForParticipantReportCallback(participantId));
				}		
			}
		});
		
		/*
		 * Hinzufügen zur Oberfläche
		 */
		navigationPanel.add(findByParticipantLabel);
		findByParticipantPanel.add(usersDropDownList);
		findByParticipantPanel.add(findByParticipantButton);
		navigationPanel.add(findByParticipantPanel);
		
		
		/**
		 * Funktion: nach Eigenschaft und Ausprägung filtern
		 */
		/*
		 * DropDown-Liste für Eigenschaften
		 */
		reportGenerator.getAllProperties(new GetAllPropertiesCallback());
		
		/*
		 * FindByValue-Button
		 */		
		findByValueButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int propertyId = Integer.parseInt(propertiesDropDownList.getSelectedValue());
				String propertyvalue = findByValueText.getValue();
				
				if(usersDropDownList.getSelectedValue() == null) {
					Window.alert("Keine Eigenschaft ausgewählt!");
				} else {
					if(propertyvalue.equals("")) {
						Window.alert("Keine Ausprägung eingegeben");
						return;
					} else {
						reportGenerator.createAllContactsForPropertyReport(propertyId, propertyvalue,
								new CreateAllContactsForPropertyReportCallback(propertyId, propertyvalue));
					}
				}	
			}		
		});		
		
		/*
		 * Hinzufügen zur Oberfläche
		 */
		navigationPanel.add(findByValueLabel);
		navigationPanel.add(propertiesDropDownList);
		findByValuePanel.add(findByValueText);
		findByValuePanel.add(findByValueButton);
		navigationPanel.add(findByValuePanel);
		
		/**
		 * Hinzufügen der gesamten Navigation zur Benutzungsoberfläche
		 */
		RootPanel.get("Navigator").add(navigationPanel);
		
		
				
	}
	
	
	/**
	 * Nested Class zum Setzen der User-Informationen auf dem Server
	 */
	class SetUserInfoCallback implements AsyncCallback<Void> {

		User userInfo;
		
		@Override
		public void onFailure(Throwable caught) {			
		}

		@Override
		public void onSuccess(Void result) {			
		}
		
	}
	
	
	/**
	 * Nested Class zum Erzeugen des AllContactsOfUserReports
	 * @author Sandra
	 */
	class CreateAllContactsReportCallback implements AsyncCallback<AllContactsOfUserReport> {
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Suche fehlgeschlagen!");
		}

		@Override
		public void onSuccess(AllContactsOfUserReport report) {
			
			if (report != null) {				
				HTMLReportWriter writer = new HTMLReportWriter();
				writer.process(report);
				RootPanel.get("Details").clear();
				RootPanel.get("Details").add(new HTML(writer.getReportText()));
			}
		}
	}

	/**
	 * Nested Class zum Erzeugen des AllContactsForParticipantReport
	 * @author Sandra
	 */
	class CreateAllContactsForParticipantReportCallback 
	implements AsyncCallback<AllContactsForParticipantReport> {
		
		double searchedParticipant = 0d;
		
		public CreateAllContactsForParticipantReportCallback(double participantId) {
			this.searchedParticipant = participantId;
		}
		
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Suche fehlgeschlagen!");		
		}

		@Override
		public void onSuccess(AllContactsForParticipantReport report) {
			if (report != null) {
				HTMLReportWriter writer = new HTMLReportWriter();
				writer.process(report);
				RootPanel.get("Details").clear();
				RootPanel.get("Details").add(new HTML(writer.getReportText()));	
			}		
		}		
	}
	
	/**
	 * Nested Class zum Erzeugen des AllContactsForPropertyReport
	 * @author Sandra
	 */
	class CreateAllContactsForPropertyReportCallback 
	implements AsyncCallback<AllContactsForPropertyReport> {

		int searchedPropertyId = 0;
		String searchedValue = null;
		
		public CreateAllContactsForPropertyReportCallback(int p, String pv) {
			this.searchedPropertyId = p;
			this.searchedValue = pv;
		}
		
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Suche fehlgeschlagen!");		
		}

		@Override
		public void onSuccess(AllContactsForPropertyReport report) {
			if (report != null) {
				HTMLReportWriter writer = new HTMLReportWriter();
				writer.process(report);
				RootPanel.get("Details").clear();
				RootPanel.get("Details").add(new HTML(writer.getReportText()));			
				findByValueText.setText("");
				propertiesDropDownList.setSelectedIndex(0);
			}
		}		
	}
	

	/**
	 * Nested Class zum Befüllen der DropDown-Liste mit den Namen der aktuellen Properties
	 * @author Sandra
	 */
	class GetAllPropertiesCallback 
	implements AsyncCallback<Vector<Property>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Abruf der Eigenschaften fehlgeschlagen!");				
		}

		@Override
		public void onSuccess(Vector<Property> result) {
			if(! result.isEmpty()) {
				for(Property element : result) {
					String idAsString = String.valueOf(element.getId());
					propertiesDropDownList.addItem(element.getDescription(), idAsString);
				}
				
			} else {
				Window.alert("Keine Eigenschaften vorhanden!");
			}			
		}
	}
	
	/**
	 * Nested Class zum Befüllen der DropDown-Liste mit den Namen der aktuellen User
	 * @author Sandra
	 */
	class getAllUsersCallback implements AsyncCallback<Vector<User>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Abruf der User fehlgeschlagen!");			
		}

		@Override
		public void onSuccess(Vector<User> result) {
			if(result.isEmpty()) {
				Window.alert("Keine User vorhanden!");				
			} else {
				for(User element : result) {
					String name = element.getUserContact().getName().getValue();
					String userIdAsString = String.valueOf(element.getGoogleID());
					usersDropDownList.addItem(name, userIdAsString);
				}
			}						
		}
		
	}
	
}
