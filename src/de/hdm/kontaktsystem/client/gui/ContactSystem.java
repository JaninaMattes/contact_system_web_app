package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.User;


/**
 * Die Klasse <code>ContactSystem</code> enthält alle Elemente zur Darstellung eines
 * Dashboards für das KontaktSystem bzw. <em>Editor</em>. Dieses ist in verschiedene Bereiche aufgebaut
 * und enthält einen drei teiligen Split-View, welcher mit unterschiedlichen 
 * Vertical Panels erreicht wird. 
 * 
 * Über die Klasse ContyctSystem kann mittels einer Menüleiste auf die Klassen
 * ContactForm {@link ContactForm} und ContactListForm {@link ContactListForm}
 * navigiert werden. 
 * 
 *@author Janina Mattes
 */

public class ContactSystem implements EntryPoint {

	/**
	 * Implementierung des statischen Interface für das TreeView Model aus BankProjekt übernommen
	 * @see de.hdm.thies.bankProjekt.client.BankProjekt
	 */
	static interface ContactSystemTreeResources extends CellTree.Resources {
		@Override
		@Source("cellTreeClosedItem.gif")
	    ImageResource cellTreeClosedItem();

	    @Override
		@Source("cellTreeOpenItem.gif")
	    ImageResource cellTreeOpenItem();

	    @Override
		@Source("CellTree.css")
	    CellTree.Style cellTreeStyle(); 
	    
	    
	}
	
	/**
	 * Remote Service Proxy zur Verbindungsaufnahme mit dem Serverseitgen Dienst
	 * namens <code>ContactSystemAdministration</code>.
	 */
	
	ContactSystemAdministrationAsync contactSystemAdmin = null;	
	
	/**
	 * Widgets, deren Inhalte variable sind, werden als Attribute angelegt.
	 */	
	
	//TreeView
	ScrollPanel treeScrollPanel = new ScrollPanel();
	final CellTreeViewModel tvm = new CellTreeViewModel();
	CellTree ct = null;
	
	//Header
	HorizontalPanel headerPanel = new HorizontalPanel();
	Label headerText = new Label("Editor");
	
	//Suchfunktion
	private TextBox search = new TextBox();
	private Button searchButton = new Button("Suche");
	
	//Buttons Menü links
	private Button contactButton = new Button("Kontakte");
	private Button contactListsButton = new Button("Kontaktlisten");
	private Button myParticipationsButton = new Button("Von mir geteilt");
	private Button receivedParticipationsButton = new Button("An mich geteilt");
	private Button accountButton = new Button("Account");
	//Trailer Text
	private Label trailerText = new Label("Software Praktikum, Team 9, Hochschule der Medien"); //Impressum hinzufügen	
					
	//Symbole für Modify-Buttons
	private Image createSymbol = new Image();
	private Image updateSymbol = new Image();
	private Image deleteSymbol = new Image();
	private Image shareSymbol = new Image();	
	private Image searchSymbol = new Image();
	
	//Logo-Bild
	private Image logo = new Image();
		
	//Symbol für Cells (in Cell-Klasse verschieben?)
	private Image chainSymbolLogo = new Image(); //Symbol für Status geteilt/nicht geteilt
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
	
	// Add Button/Panel
	private FocusPanel addPanel = new FocusPanel();
	
	// Lade Animation
	private PopupPanel loadPanel = new PopupPanel();

	//Header
	private HorizontalPanel header = new HorizontalPanel();				
			
	//Navigator
    private VerticalPanel navigation = new VerticalPanel();	
 
    //Trailer
    private HorizontalPanel trailer = new HorizontalPanel();
    
	//Formulare

	private ContactForm cf = new ContactForm();
	private ContactListForm clf = new ContactListForm();
	private UserForm uf = new UserForm();
	
	//Add
	private boolean addContact = true;
	
	
	/**
	 * Die Methode <code>loadTree()</code> ruft das TreeView Model auf
	 * und übergibt diesem Standart gemäß per Default Contact Objekte
	 * zur Darstellung in Listenform. Diese wird später über die 
	 * <em>public void onModuleLoad()</em> Methode des Interfaces EntryPoint
	 * aufgerufen. 
	 * 
	 */
	
	public void loadTree() {		
		
		tvm.setClForm(clf);
		tvm.setCForm(cf);
		clf.setTree(tvm);
		cf.setTree(tvm);
		
		//CSS
		treeScrollPanel.setHeight("80vh");
		
		contactSystemAdmin.getMyContactsPrev(new AsyncCallback<Vector<Contact>>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				log("Keine Kontakte gefunden");
			}

			@Override
			public void onSuccess(Vector<Contact> result) {
				// TODO Auto-generated method stub
				log("Es wurden " + result.size() + " Kontakte gefunden");
				CellTree.Resources res = GWT.create(ContactSystemTreeResources.class);
				ct = new CellTree(tvm, result, res);
				ct.setAnimationEnabled(true);
//				ct.setDefaultNodeSize(result.size());
				treeScrollPanel.add(ct);				
				
			}

		});
		
		RootPanel.get("Lists").add(treeScrollPanel);	
		
	}
	
	/**
	 * Da die Klasse <code>ContactSystem</code> die Implementierung des Interface <code>EntryPoint</code>
	 * zusichert, wird die Methode <code>public void onModuleLoad()</code> als Einstieg benötigt. 
	 * Diese ist das GWT typische Gegenstück der <code>main()</code> Methode einer normalen Java-Applikationen.
	 * Wenn der Nutzer im System noch nicht eingeloggt ist wird die Login-Seite aufgerufen, andernfalls
	 * gelangt der Nutzer auf die Startseite, bzw. das Dashboard mit der Default-Anzeige aller seiner Kontakte.
	 *  
	 */
	
	public void onModuleLoad() {
	
//		contactSystemAdmin = ClientsideSettings.getContactAdministration();
//		contactSystemAdmin.getUserByID(510, new AsyncCallback<User>() {
//			public void onFailure(Throwable error) {
//				
//			}
//				
//			//Wenn der User eingeloggt ist, wird die Startseite aufgerufen, andernfalls die Login-Seite
//			public void onSuccess(User result) {
//					log("Set User: "+ result);
//					uf.setMyUser(result);
//					cf.setMyUser(result);
//					cf.setLoad(loadPanel);
//					clf.setMyUser(result);
//					clf.setLoad(loadPanel);
//					loadTree(); // für Test
//					loadContactSystem(); // für Test
//					loadPanel.setVisible(false);
//			}
//		});	
		
		
		/**
		 * Login-Status feststellen mit LoginService
		 */		

		loadPanel.setVisible(false);
		contactSystemAdmin = ClientsideSettings.getContactAdministration();
		contactSystemAdmin.login(GWT.getHostPageBaseURL(), new AsyncCallback<User>() {
			public void onFailure(Throwable error) {
				Window.alert("Login Error");
			}
				
			//Wenn der User eingeloggt ist, wird die Startseite aufgerufen, andernfalls die Login-Seite
			public void onSuccess(User result) {
				userInfo = result;
				if(userInfo.isLoggedIn()){
					log("Load Editor");
					uf.setMyUser(result);
					cf.setMyUser(result);
					clf.setMyUser(result);
					uf.setTree(tvm);
					cf.setLoad(loadPanel);
					clf.setLoad(loadPanel);
					loadTree(); // für Test
					loadContactSystem(); // für Test
					loadPanel.setVisible(false);	
				}else{
					loadLogin();					
				}
			}
		});	
		
	}	
	
	/**
	 * Die Methode <code>loadLogin()</code> führt zum Aufbau der Login-Seite.
	 * Wenn der Nutzer ausgeloggt wurde, kann er sich hierüber wieder neu im 
	 * System einloggen. 
	 */
	
	private void loadLogin() {	
		log("Login");
		signInLink.setHref(userInfo.getLoginUrl());
		signInLink.getElement().setId("link");
		loginPanel.add(new HTML("<center>"));
		loginPanel.add(loginLabel);
		loginPanel.add(new HTML("<br /> <br /> "));
		FocusPanel login = new FocusPanel();
		login.add(new Image(GWT.getHostPageBaseURL() + "images/login.png"));
		login.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.assign(userInfo.getLoginUrl());				
			}		
		});
		loginPanel.add(login);
		loginPanel.add(new HTML("</center>"));
		RootPanel.get("Lists").add(loginPanel); //TODO: prüfen ob richtige HTML
	}
		
	
	/**
	 * Aufbau der Startseite des Kontaktsystems. Elemente werden in Listenform dargestellt, 
	 * ausgewählte Elemente werden als Formulare rechts im Bildschirm aufgerufen
	 */
	public void loadContactSystem() {
					
		//Header
		HorizontalPanel header = new HorizontalPanel();				
				
		//Navigator
	    VerticalPanel navigation = new VerticalPanel();	
	 
	    //Trailer
	    HorizontalPanel trailer = new HorizontalPanel();

		//Logo Kontaktsystem
		logo.setUrl(GWT.getHostPageBaseURL() + "images/LogoWeiss.png");	
		logo.setHeight("70px");
		logo.setAltText("Logo");
				    
	    searchButton.addClickHandler(new SearchClickHandler());
		searchButton.setEnabled(true);
		
	    //Suchfunktion
  		Grid sg = new Grid(1,2);
  		sg.setWidget(0, 0, search);
  		sg.setWidget(0, 1, searchButton);
  		
  		//Enable Buttons
	    contactButton.setEnabled(true);
	    contactListsButton.setEnabled(true);
	    myParticipationsButton.setEnabled(true);
	    receivedParticipationsButton.setEnabled(true);
	    accountButton.setEnabled(true);
	  			
	    //Trailer
	    trailer.setTitle("Copyright Team09, Hochschule der Medien Stuttgart");
	    
		//Header mit SignOut-Link
		if(userInfo != null){ 
			signOutLink.setHref(userInfo.getLogoutUrl());
		}
		
		reportLink.setHref(GWT.getHostPageBaseURL() + "ReportGenerator.html");

		/** 
		 * Namen für CSS festlegen 
		 */
		reportLink.getElement().setId("switch-button");
		signOutLink.getElement().setId("log-out-button");
		
		//Label der �berschrift im Header
		headerText.getElement().setId("headertext");
		
		//Der Search-Button bekommt den gleichen Style wie bei Report-Generator.java
		searchButton.getElement().setId("searchButton"); 
		
		/** Menü-Buttons bekommen den gleichen Style und haben deshalb den gleichen StyleName */
		contactButton.removeStyleName("gwt-Button");
		contactButton.getElement().setId("menu-button");
		contactListsButton.removeStyleName("gwt-Button");
		contactListsButton.getElement().setId("menu-button");
		myParticipationsButton.removeStyleName("gwt-Button");
		myParticipationsButton.getElement().setId("menu-button");
		receivedParticipationsButton.removeStyleName("gwt-Button");
		receivedParticipationsButton.getElement().setId("menu-button");
		accountButton.removeStyleName("gwt-Button");
		accountButton.getElement().setId("menu-button");
		
		/** 
		 * Der Name, mit welchem das Search-Textfeld in CSS formatiert werden kann, wird festgelegt. 
		 */
		search.getElement().setId("search-textfeld");

		/**
		 * CSS Identifier für das Logo
		 */
		logo.getElement().setId("logo");
		
		/*
		 * CSS für Add Panel
		 */
		addPanel.getElement().setId("add");
		
		/*
		 * CSS für Load Panel
		 */
		loadPanel.getElement().setId("load");
		
		/**
		 * CSS Identifier für die Elemente
		 */		

		header.getElement().setId("Header");		
		navigation.getElement().setId("Navigation");
		trailer.getElement().setId("Trailer");
		
		
		/**
		 * Wird im Navigator ein Button ausgewählt, wird die zugehörige Liste (CellTreeViewModel) aufgerufen. 
		 * Handelt es sich bei den Listenelementen um Kontakte oder Kontaktlisten, wird unterhalb des
		 * CellTrees ein Button hinzugefügt, mit dem neue Elemente erzeugt werden können.
		 * Je nachdem ob der Nutzer sich nun in der Ansicht der Kontakte oder Kontaktlisten befindet,
		 * kann diesem mittels Einstieg über den Add-Buttons ein neues, leeres Kontaktformular oder Kontaktlistformular
		 * angezeigt werden. 
		 * Die Änderungen werden mittels Aufruf des Datenproviders <code>updateData()</code> auch an 
		 * den CellTreeViewModel übergeben werden. Die Listen Anzeige ändert sich mit. 
		 */
	
		contactButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
			tvm.restSelection();
			addContact = true;
			addPanel.setVisible(true);
			loadPanel.setVisible(true);
			contactSystemAdmin.getMyContactsPrev(new AsyncCallback<Vector<Contact>>() {
					@Override
					public void onFailure(Throwable caught) {
						
						loadPanel.setVisible(false);
						log("Keine Kontakte gefunden");
					}

					@Override
					public void onSuccess(Vector<Contact> result) {
						
						loadPanel.setVisible(false);
						log("Es wurden " + result.size() + " Kontakte gefunden");
						Vector<BusinessObject> bov = new Vector<BusinessObject>();
						for(Contact cl : result){
							bov.add(cl);
						}
						
						tvm.updateData(bov);
					
					}

				});			
			
			}
		});
	
		
		//Clickhandler für ContactListButton
		contactListsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				tvm.restSelection();
				addContact = false;
				addPanel.setVisible(true);
				loadPanel.setVisible(true);
				contactSystemAdmin.getMyContactListsPrev(new AsyncCallback<Vector<ContactList>>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						loadPanel.setVisible(false);
						log("Keine Listen gefunden");
					}

					@Override
					public void onSuccess(Vector<ContactList> result) {
						// TODO Auto-generated method stub
						loadPanel.setVisible(false);
						log("Es wurden " + result.size() + " Listen gefunden");
						Vector<BusinessObject> bov = new Vector<BusinessObject>();
						for(ContactList cl : result){
							bov.add(cl);
						}
						
						tvm.updateData(bov);
					}

				});			
				log("Load ContactList");
			}
			
		});
		
		/**
		 * Bei Auswahl des Menü-Buttons <em>Von Mir Geteilt</em> erfolgt der Aufruf der
		 * Service-Methode "getAllSharedByMe". Dies führt zur Anzeige aller vom Nutzer
		 * geteilten Kontakte und Kontaktlisten. Über den Aufruf des Dataproviders 
		 * des CellTreeViewModels werden Änderungen an diesen weiter gegeben. 
		 */
		
		//Clickhandler für MyParticipationsButton
		myParticipationsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				tvm.restSelection();
				addPanel.setVisible(false);
				loadPanel.setVisible(true);
				contactSystemAdmin.getAllSharedByMe(new AsyncCallback<Vector<BusinessObject>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						loadPanel.setVisible(false);
						log("Keine Listen gefunden");
					}

					@Override
					public void onSuccess(Vector<BusinessObject> result) {
						// TODO Auto-generated method stub
						loadPanel.setVisible(false);
						log("Es wurden " + result.size() + " SharedObjects gefunden");
						
						
						tvm.updateData(result);
					}

				});	
				
					log("Load Shared By Me");
			}
		});
	
		/**
		 * Bei Auswahl des Menü-Buttons <em>An Mich Geteilt</em> erfolgt der Aufruf der
		 * Service-Methode "getAllSharedByOthersToMe". Über den Aufruf des Dataproviders 
		 * des CellTreeViewModels werden Änderungen an diesen weiter gegeben und analog zu
		 * der Funktionalität des Menü-Buttons myParticipationsButton alle 
		 * von anderen Nutzern geteilten Kontakte und Kontaktlisten angezeigt.
		 */
		
		//Clickhandler für ReceivedParticipationsButton
		receivedParticipationsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				tvm.restSelection();
				addPanel.setVisible(false);
				loadPanel.setVisible(true);
				contactSystemAdmin.getAllSharedByOthersToMe(new AsyncCallback<Vector<BusinessObject>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						loadPanel.setVisible(false);
						log("Keine Listen gefunden");
					}

					@Override
					public void onSuccess(Vector<BusinessObject> result) {
						// TODO Auto-generated method stub
						loadPanel.setVisible(false);
						log("Es wurden " + result.size() + " SharedObjects gefunden");						
						
						tvm.updateData(result);
					}

				});	
				
				log("Load Shared With Me");
				
			}
		});
		
		accountButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// Resettet den TreeView da in der account ansicht nihts von dem Tree ausgewählt ist
				tvm.restSelection(); // Account wird danach nicht mehr angezeigt
//				RootPanel.get("Details").clear();
				log("Add: Account");
				RootPanel.get("Details").add(uf); 
			}
			
		});
		
		/**
		 * Bei Auswahl des Buttons <em>Hinzufügen</em>, welcher als runder Button dargestellt wird,
		 * erfolgt der Aufruf der Methode "setSelected()". Wenn sich der Nutzer in dem Menü für 
		 * Kontakte befindet erhält er über den Aufruf der Methode die Darstellung einer leeren
		 * Kontaktformulars, während dieser wenn er sich im Menü Kontaktliste befindet ein leeres
		 * KontaktlistenFormular angezeigt bekommt. 
		 */
		addPanel.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				tvm.restSelection();
//				RootPanel.get("Details").clear();
				log("Add: New");
				if(addContact){
					Contact c = null;
					cf.setSelected(c);
					RootPanel.get("Details").add(cf);
				}else{
					ContactList cl = null;
					clf.setSelected(cl);
					RootPanel.get("Details").add(clf);
				}
							
			}
			
		});
		
		Image add = new Image("/images/add.png");
		add.setPixelSize(50, 50);
		addPanel.add(add);
		
		Image load = new Image("/images/load.gif");
		load.setStyleName("load_Animation");
		loadPanel.add(load);
		
		//Header
		headerPanel.add(logo);
		headerPanel.add(headerText);
 		headerPanel.add(reportLink);
	    headerPanel.add(signOutLink);
	    
		
		//Menu Leiste
	    navigation.add(sg);
	  	navigation.add(contactButton);
	  	navigation.add(contactListsButton);
	  	navigation.add(myParticipationsButton);
	  	navigation.add(receivedParticipationsButton); 
	  	navigation.add(accountButton);


	  	RootPanel.get().add(loadPanel);
	  	RootPanel.get("Header").add(headerPanel);
	  	RootPanel.get("Navigator").add(navigation);
	  	RootPanel.get("Trailer").add(trailer);
	  	RootPanel.get().add(addPanel);
		
	}
	
	/**
	 * Die innere Klasse <code>SearchClickHandler</code> implementiert das Clickhandler 
	 * Interface und dessen dazugehörige <code>onClick(ClickEvent event)</code> Methode.
	 * Bei einem Click-Event über den Suchen-Button wird die Service-Methode "search"
	 * aufgerufen. Anhand des übergebenen Strings aus dem Suchen-Textfeld wird diese
	 * Methode ausgeführt und liefert das gewünschte Ergebnis mittels einem Vector
	 * an BusinessObject Objekten. Diese werden an den zugehörigen CellTreeViewModel
	 * über die Methode des Dataproviders <code>updateData()</code> weitergegeben und
	 * ermöglichen es die Anzeige des CellTreeViewModels analog anzupassen.
	 * 
	 * @author Janina Mattes
	 *
	 */
	private class SearchClickHandler implements ClickHandler {
		
		@Override
		public void onClick(ClickEvent event) {
			
			if (search.getText().equals("")) {
				Window.alert("Das Suchfeld ist leer!");
			} else {
				loadPanel.setVisible(true);
			 String s = search.getText();
			 log("Suche: "+ s);
			 // Suche der Kontakte
			 contactSystemAdmin.search(s, new AsyncCallback<Vector<BusinessObject>>(){
				 @Override
					public void onFailure(Throwable caught) {
					 	loadPanel.setVisible(false);
						Window.alert("Die Suche ist fehlgeschlagen!");
					}

					@Override
					public void onSuccess(Vector<BusinessObject> result) {
						loadPanel.setVisible(false);
						if (result != null) {							
							tvm.updateData(result);
						} else {
							//Window.alert("Keine Kontakte gefunden :(");
						}
					}
			 });
			}
		}
	}

	
	
	native void log(String s)/*-{
	console.log(s);
	}-*/;
			
	

}
