package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.client.LoginService;
import de.hdm.kontaktsystem.client.LoginServiceAsync;
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
	
	//TEST
//	ContactSystemAdministrationAsync contactSysAdmin = null;
	
	/**
	 * Definition der grundlegenden Widgets des Report-Generators
	 */
	HorizontalPanel headerPanel = new HorizontalPanel();
	Label headerText = new Label("Report Generator");
	Image logo = new Image();
	VerticalPanel navigationPanel = new VerticalPanel();
	Button showAllButton = new Button("Alle Kontakte");
	
	Label findByParticipantLabel = new Label("Nach Teilhaber filtern");
	HorizontalPanel findByParticipantPanel = new HorizontalPanel();
	ListBox usersDropDownList = new ListBox();
	Button findByParticipantButton = new Button("Suche"); //TODO: Symbol statt Text einfügen
	
	Label findByValueLabel = new Label("Nach Eigenschaftsausprägung filtern");
	ListBox propertiesDropDownList = new ListBox();
	HorizontalPanel findByValuePanel = new HorizontalPanel();
	TextBox findByValueText = new TextBox();
	Button findByValueButton = new Button("Suche"); //TODO: Symbol statt Text einfügen
	

	
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
		
//		/**
//		 * Login-Status feststellen mit LoginService
//		 */		
//		LoginServiceAsync loginService = GWT.create(LoginService.class);
//		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<User>() {
//			public void onFailure(Throwable error) {
//				Window.alert("Login Error :(");
//			}
//				
//			//Wenn der User eingeloggt ist, wird die Startseite aufgerufen, andernfalls die Login-Seite
//			public void onSuccess(User result) {
//				userInfo = result;
//				if(userInfo.isLoggedIn()){
//					loadReportGenerator();
//				}else{
//					loadLogin();					
//				}
//			}
//		});
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

		//TEST
//		if(contactSysAdmin == null) {
//			contactSysAdmin = ClientsideSettings.getContactAdministration();
//		}
		
		
		/**
		 * Setzen der User-Informationen im Server
		 */
		reportGenerator.setUserInfo(userInfo, new SetUserInfoCallback());
		
		
		/**
		 * CSS
		 */
		
		//Labels in CSS
		findByParticipantLabel.getElement().setId("findbylabel");
		findByValueLabel.getElement().setId("filtern");
		loginLabel.getElement().setId("loginlabel");
		
		//Textbox in CSS

		findByValueText.getElement().setId("findByTextbox");
		
		//DropDownList in CSS
		propertiesDropDownList.getElement().setId("DropDownList");

		//Button in CSS
		//Der Search-Button bekommt den gleichen Style wie bei ContactSystem.java (Bessere Usability)
		findByParticipantButton.getElement().setId("searchButton");
		//Der Search-Button bekommt den gleichen Style wie die anderen Searchbuttons
		findByValueButton.getElement().setId("searchButton");
	
		//Links
		signInLink.setStyleName("link");
		signOutLink.setStyleName("link");
		editorLink.setStyleName("link");

		/**
		 * Aufbau der Oberfläche.
		 * Die Reportanwendung besteht aus einem "Navigationsteil" mit den
		 * Schaltflächen zum Auslösen der Reportgenerierung und einem "Datenteil"
		 * für die HTML-Version des Reports.
		 */
		
		/**
		 * Aufbau des Headers
		 */
		logo.setUrl(GWT.getHostPageBaseURL() + "images/LogoTransparent.png");
		logo.setHeight("50px");
		logo.setAltText("Logo");
		headerPanel.add(logo);
		headerPanel.add(headerText); //TODO: Durch Logo ersetzen
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
					int participantId = Integer.parseInt(usersDropDownList.getSelectedValue());
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
		
		int searchedParticipant = 0;
		
		public CreateAllContactsForParticipantReportCallback(int participantId) {
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
				propertiesDropDownList.setVisibleItemCount(result.size());
				
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
				usersDropDownList.setVisibleItemCount(result.size());
			}						
		}
		
	}
	
}
