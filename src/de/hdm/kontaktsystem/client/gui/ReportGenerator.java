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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.client.LoginService;
import de.hdm.kontaktsystem.client.LoginServiceAsync;
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
	 * Definition der grundlegenden Widgets des Report-Generators
	 */
	Button showAllButton = new Button("Alle Kontakte");
	
	Label findByParticipantLabel = new Label("Nach Teilhaber filtern");
	HorizontalPanel findByParticipantPanel = new HorizontalPanel();
	TextBox findByParticipantText = new TextBox();
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
	private Anchor reportLink = new Anchor("Report");				

	
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
		if (reportGenerator == null) {
			reportGenerator = ClientsideSettings.getReportGenerator();
		}

		
		/**
		 * Setzen der User-Informationen im Server
		 */
		reportGenerator.setUserInfo(userInfo, new SetUserInfoCallback());
		
		
		/**
		 * Die Reportanwendung besteht aus einem "Navigationsteil" mit den
		 * Schaltflächen zum Auslösen der Reportgenerierung und einem "Datenteil"
		 * für die HTML-Version des Reports.
		 */
		/*
		 * Funktionalität des ShowAll-Buttons
		 */
		showAllButton.setStylePrimaryName(""); //TODO CSS Style-Namen anpassen
		showAllButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				reportGenerator.createAllContactsReport(new CreateAllContactsReportCallback());
			}
		});
		
		RootPanel.get("Navigator").add(showAllButton);
		
		/*
		 * Funktionalität des FindByParticipant-Buttons
		 */
		findByParticipantButton.setStylePrimaryName(""); //TODO CSS Style-Namen anpassen
		
		findByParticipantButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String participant = findByParticipantText.getValue();
				if(participant.equals("")) {
					Window.alert("Kein Teilhaber eingegeben!");
					return;
				}
				reportGenerator.createAllContactsForParticipantReport(participant,
						new CreateAllContactsForParticipantReportCallback(participant));
			}
		});
		
		RootPanel.get("Navigator").add(findByParticipantLabel);
		findByParticipantPanel.add(findByParticipantText);
		findByParticipantPanel.add(findByParticipantButton);
		RootPanel.get("Navigator").add(findByParticipantPanel);
		
		
		/**
		 * CSS
		 */
		
		findByParticipantLabel.setStyleName("findbylabel");
		findByParticipantText.setStyleName("findbytext");
		//Der Search-Button bekommt den gleichen Style wie bei ContactSystem.java (Bessere Usability)
		findByParticipantButton.setStyleName("search-Button");

		findByValueLabel.setStyleName("filtern");
		findByValueText.setStyleName("findByText");
		//Der Search-Button bekommt den gleichen Style wie die anderen Searchbuttons
		findByValueButton.setStyleName("search-button");
		
		
		
		/*
		 * Aufbau der DropDown-Liste
		 */
		reportGenerator.getAllProperties(new GetAllPropertiesCallback());
		
		/*
		 * Funktionalität des FindByValue-Buttons
		 */
		
		findByValueButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String property = propertiesDropDownList.getSelectedItemText();
				String propertyvalue = findByValueText.getValue();
				if(propertyvalue.equals("")) {
					Window.alert("Keine Ausprägung eingegeben");
					return;
				}
				reportGenerator.createAllContactsForPropertyReport(property, propertyvalue,
						new CreateAllContactsForPropertyReportCallback(property, propertyvalue));
			}		
		});		
		
		RootPanel.get("Navigator").add(findByValueLabel);
		RootPanel.get("Navigator").add(propertiesDropDownList);
		findByValuePanel.add(findByValueText);
		findByValuePanel.add(findByValueButton);
		RootPanel.get("Navigator").add(findByValuePanel);
		
		
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
		
		String searchedParticipant = null;
		
		public CreateAllContactsForParticipantReportCallback(String participant) {
			this.searchedParticipant = participant;
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

		String searchedProperty = null;
		String searchedValue = null;
		
		public CreateAllContactsForPropertyReportCallback(String p, String pv) {
			this.searchedProperty = p;
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
					propertiesDropDownList.addItem(element.getDescription());
				}
				propertiesDropDownList.setVisibleItemCount(result.size());
				
			} else {
				Window.alert("Keine Eigenschaften vorhanden!");
			}			
		}
	}
	
}
