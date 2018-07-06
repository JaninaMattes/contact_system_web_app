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
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
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
 * @author Sandra Prestel
 */
public class ReportGenerator implements EntryPoint {

	ReportGeneratorAsync reportGenerator = null;
	
	/**
	 * Definition der verwendeten Bilder, hier die Symbole für die Such-Buttons, 
	 * mit denen Kontakte gefiltert werden können, sowie das Logo im Header.
	 */
	Image searchSymbol1 = new Image();
	Image searchSymbol2 = new Image();
	Image logo = new Image();
	Image load = new Image();
	
	/**
	 * Definition der grundlegenden Widgets des Report-Generators. 
	 * Dieser enthält einen Header, ein Menü mit verschiedenen Buttons, Labels 
	 * und Auswahlmöglichkeiten sowie eine Detailansicht für die Reports. Ein Trailer
	 * mit allgemeinen Informationen wird im HTML-Dokument des ReportGenerators definiert.
	 * 
	 * @see de.hdm.kontaktsystem.war.ReportGenerator.html
	 */
	HorizontalPanel headerPanel = new HorizontalPanel();
	Label headerText = new Label("Report-Generator");
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
	 * Definition der Widgets für den Login.
	 */
	private Anchor signOutLink = new Anchor("Logout");
	private Anchor editorLink = new Anchor("Editor");
	private User userInfo = null;
	
	// Lade Animation
	private PopupPanel loadPanel = new PopupPanel();
	
	/**
	 * <p>
	 * Da diese Klasse das Interface <code>EntryPoint</code> implementiert, wird 
	 * die Methode <code>public void onModuleLoad()</code> benötigt. 
	 * Diese entspricht der <code>main()</code>-Methode in Java-Applikationen.
	 * </p><p>
	 * In dieser Methode wird zunächst der Login-Service aufgerufen, um zu prüfen, ob ein Nutzer
	 * aktuell im Kontaktsystem eingeloggt ist. Ist dies nicht der Fall, wird die Login-Seite
	 * aufgerufen ({@link #loadLogin()}. Ist der Nutzer eingeloggt, wird die Oberfläche des 
	 * ReportGenerators geladen ({@link #loadReportGenerator()}).
	 * </p>
	 */	
	@Override
	public void onModuleLoad() {
		
		/**
		 * Login-Status feststellen mit LoginService
		 */	
		RootPanel.get("Navigator").setVisible(false);
		RootPanel.get("Details").setVisible(false);
		loadPanel.setVisible(false);
		reportGenerator = ClientsideSettings.getReportGenerator();
		reportGenerator.login(GWT.getHostPageBaseURL()+"ReportGenerator.html", new AsyncCallback<User>() {
			
			public void onFailure(Throwable error) {
				Window.alert("Login Error");
			}
				
			//Wenn der User eingeloggt ist, wird die Startseite aufgerufen, andernfalls die Login-Seite
			public void onSuccess(User result) {
				userInfo = result;
				if(userInfo == null || userInfo.isLoggedIn()){
					log("Load ReportGenerator");
					RootPanel.get("Navigator").setVisible(true);
					RootPanel.get("Details").setVisible(true);
					loadReportGenerator();
				}else{
					log("Load login");
					// Anzeige der Loginseite
					RootPanel.get("Content").add(new Login(userInfo)); 			
				}
			}
		});
		
		
	}
	
	/**
	 * Aufbau der Startseite des Report-Generators. 
	 * Diese Methode wird aufgerufen, wenn der Login erfolgreich war.
	 * @see #onModuleLoad()
	 */
	public void loadReportGenerator() {
		
		/*
		 * Zuweisung des Asynchronen Interface.
		 */
		if(reportGenerator == null) {
			reportGenerator = ClientsideSettings.getReportGenerator();
		}
		
		/*
		 * Zuweisung von ids zu den HTML-Elementen, die aus den gwt-Widgets generiert
		 * werden, um sie mit CSS gezielt ansprechen zu können.
		 */
		
		//Labels
		findByParticipantLabel.getElement().setId("filtern");
		findByValueLabel.getElement().setId("filtern");
		headerText.setStyleName("headertext");
		
		//Textbox
		findByValueText.getElement().setId("findByTextbox");
		
		//DropDownList
		propertiesDropDownList.setStyleName("ReportDropDownList");
		usersDropDownList.setStyleName("ReportDropDownList");

		//Buttons
		//Der Search-Button bekommt den gleichen Style wie bei ContactSystem.java (Bessere Usability)
		findByParticipantButton.setStyleName("searchButton");
		//Der Search-Button bekommt den gleichen Style wie die anderen Searchbuttons
		findByValueButton.setStyleName("searchButton");
		showAllButton.setStyleName("searchAllContacts");
	
		//Links
		signOutLink.getElement().setId("log-out-button");
		editorLink.getElement().setId("switch-button");
		
		//Logo
		logo.setStyleName("logo");
		
		/*
		 * CSS für Load Panel
		 */
		loadPanel.getElement().setId("load");

		/*
		 * Zuweisen von Bilddateien zu den Image-Elementen und Setzen der Größe.
		 */
		// Ladeanimation
		load.setUrl(GWT.getHostPageBaseURL() + "images/load.gif");
		load.setStyleName("load_Animation");
		loadPanel.add(load);
		loadPanel.setVisible(false);
		
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
		
		// Header
		headerText.setText("ReportGenerator");
		headerPanel.add(signOutLink);		
		headerPanel.add(editorLink);
		
		/*
		 * Setzen des Links zum Editor des Kontaktsystems. Hiermit kann die Webseite
		 * "Editor" geöffnet werden, mit der Inhalte bearbeitet werden können.
		 * @see ContactSystem.java
		 */
		editorLink.setHref(GWT.getHostPageBaseURL() + "ContactSystem.html");
		
		/*
		 * Aufbau der Oberfläche des ReportGenerators.
		 * Die Reportanwendung besteht aus einem Header mit Links zum Logout und dem Editor,
		 * einem "Navigationsteil" mit den Schaltflächen zum Auslösen der Reportgenerierung 
		 * und einem "Datenteil" für die HTML-Version des Reports.
		 */
		if(userInfo != null){ 
			signOutLink.setHref(userInfo.getLogoutUrl());
		}
		
		/**
		 * Das Ladeoverlay dem HTML-Body hinzufügen
		 */
		RootPanel.get().add(loadPanel);
		
		/**
		 * Aufbau des Headers
		 */	
		headerPanel.clear();
		headerPanel.add(logo);
		headerPanel.add(headerText);
		headerPanel.add(signOutLink);		
		headerPanel.add(editorLink);		
		
		RootPanel.get("Header").add(headerPanel);
		
		//Aufbau der Navigation und der Detail-Ansicht
		Label noDetails = new Label("Kein Report ausgewählt");
		RootPanel.get("Details").add(noDetails);;
		
		/*
		 * Durch Klick auf den Button <code>showAllButton</code> werden alle Kontakte angezeigt,
		 * auf die der Nutzer Zugriff hat.
		 */
		navigationPanel.add(showAllButton);
		
		showAllButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loadPanel.setVisible(true);
				reportGenerator.createAllContactsReport(new CreateAllContactsReportCallback());
			}
		});
		
		/*
		 * Durch Klick auf den Button <code>findByParticipantButton</code> werden alle Kontakte 
		 * angezeigt, auf die der Nutzer Zugriff hat und die mit einem bestimmten Nutzer
		 * geteilt wurden. Dieser kann aus einer DropDown-Liste ausgewählt werden.
		 */
		// DropDown-Liste für alle User
		reportGenerator.getAllUsers(new getAllUsersCallback());
		
		// FindByParticipant-Button
		findByParticipantButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(usersDropDownList.getSelectedValue() == null) {
					Window.alert("Kein Teilhaber ausgewählt!");
				} else if (usersDropDownList.getSelectedValue().equals("empty")){
					Window.alert("Keine Teilhaber vorhanden!");
				} else {
					loadPanel.setVisible(true);
					double participantId = Double.parseDouble(usersDropDownList.getSelectedValue());
					reportGenerator.createAllContactsForParticipantReport(participantId,
							new CreateAllContactsForParticipantReportCallback(participantId));
				}		
			}
		});
		
		// Hinzufügen zur Oberfläche
		navigationPanel.add(findByParticipantLabel);
		findByParticipantPanel.add(usersDropDownList);
		findByParticipantPanel.add(findByParticipantButton);
		navigationPanel.add(findByParticipantPanel);
		
		
		/*
		 * Durch Klick auf den Button <code>findByValueButton</code> werden alle Kontakte 
		 * angezeigt, auf die der Nutzer Zugriff hat und die eine bestimmte Eigenschaft 
		 * und Eigenschaftsausprägung besitzen. Die Eigenschaft kann aus einer DropDown-Liste 
		 * ausgewählt und die Ausprägung in ein Textfeld eingegeben werden.
		 */
		// DropDown-Liste für Eigenschaften
		reportGenerator.getAllProperties(new GetAllPropertiesCallback());
		
		// FindByValue-Button		
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
						loadPanel.setVisible(true);
						reportGenerator.createAllContactsForPropertyReport(propertyId, propertyvalue,
								new CreateAllContactsForPropertyReportCallback(propertyId, propertyvalue));
					}
				}	
			}		
		});		
		
		// Hinzufügen zur Oberfläche
		navigationPanel.add(findByValueLabel);
		navigationPanel.add(propertiesDropDownList);
		findByValuePanel.add(findByValueText);
		findByValuePanel.add(findByValueButton);
		navigationPanel.add(findByValuePanel);
		
		/*
		 * Hinzufügen der gesamten Navigation zur Benutzungsoberfläche.
		 */
		RootPanel.get("Navigator").add(navigationPanel);
			
	}
		
	/**
	 * Innere Klassen, die die Callbacks zum Server definieren. Dazu implementieren sie
	 * das Interface {@link: AsyncCallback} und ermöglichen so den Aufruf von Methoden 
	 * des Interfaces {@link ReportGeneratorAsync}.
	 */
	/**
	 * Innere Klasse zum Erzeugen des AllContactsOfUserReport.
	 * @author Sandra Prestel
	 */
	class CreateAllContactsReportCallback implements AsyncCallback<AllContactsOfUserReport> {
		@Override
		public void onFailure(Throwable caught) {
			loadPanel.setVisible(false);
			Window.alert("Suche fehlgeschlagen!");
		}

		@Override
		public void onSuccess(AllContactsOfUserReport report) {
			loadPanel.setVisible(false);
			if (report != null) {				
				HTMLReportWriter writer = new HTMLReportWriter();
				writer.process(report);
				RootPanel.get("Details").clear();
				RootPanel.get("Details").add(new HTML(writer.getReportText()));
			}
		}
	}

	/**
	 * Innere Klasse zum Erzeugen des AllContactsForParticipantReport.
	 * @author Sandra Prestel
	 */
	class CreateAllContactsForParticipantReportCallback 
	implements AsyncCallback<AllContactsForParticipantReport> {
		
		double searchedParticipant = 0d;
		
		public CreateAllContactsForParticipantReportCallback(double participantId) {
			this.searchedParticipant = participantId;
		}
		
		@Override
		public void onFailure(Throwable caught) {
			loadPanel.setVisible(false);
			Window.alert("Suche fehlgeschlagen!");		
		}

		@Override
		public void onSuccess(AllContactsForParticipantReport report) {
			loadPanel.setVisible(false);
			if (report != null) {
				HTMLReportWriter writer = new HTMLReportWriter();
				writer.process(report);
				RootPanel.get("Details").clear();
				RootPanel.get("Details").add(new HTML(writer.getReportText()));
				usersDropDownList.setSelectedIndex(0);
			}		
		}		
	}
	
	/**
	 * Innere Klasse zum Erzeugen des AllContactsForPropertyReport.
	 * @author Sandra Prestel
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
			loadPanel.setVisible(false);
			Window.alert("Suche fehlgeschlagen!");		
		}

		@Override
		public void onSuccess(AllContactsForPropertyReport report) {
			loadPanel.setVisible(false);
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
	 * Innere Klasse zum Befüllen der DropDown-Liste mit den Namen der aktuellen Properties
	 * @author Sandra Prestel
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
	 * Innere Klasse zum Befüllen der DropDown-Liste mit den Namen der aktuellen User
	 * @author Sandra Prestel
	 */
	class getAllUsersCallback implements AsyncCallback<Vector<User>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Abruf der User fehlgeschlagen!");			
		}

		@Override
		public void onSuccess(Vector<User> result) {
			if(result.isEmpty()) {
	                        String noUsers = "keine Teilhaber vorhanden";
				usersDropDownList.addItem(noUsers, "empty");
			} else {
				for(User element : result) {
					String name = element.getUserContact().getName().getValue();
					String userIdAsString = String.valueOf(element.getGoogleID());
					usersDropDownList.addItem(name, userIdAsString);
				}
			}						
		}
		
	}
	
	native void log(String s)/*-{
		console.log(s);
	}-*/;
}
