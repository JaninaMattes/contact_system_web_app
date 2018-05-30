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
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.client.LoginService;
import de.hdm.kontaktsystem.client.LoginServiceAsync;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.User;


/**
 *
 */
public class ContactSystem implements EntryPoint {

	/**
	 * Interface aus BankProjekt übernommen
	 */
	static interface TreeResources extends CellTree.Resources {
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
	 * Link zum Asynchronen Interface
	 */
	ContactSystemAdministrationAsync contactSystemVerwaltung = null;
	

	/** 
	 * Instanziieren der GWT Widgets und Panels
	 */
	
	//Buttons Menü links
	private Button contactButton = new Button("Kontakte");
	private Button contactListsButton = new Button("Kontaktlisten");
	private Button myParticipationsButton = new Button("Von mir geteilt");
	private Button receivedParticipationsButton = new Button("An mich geteilt");
	//Trailer Text
	private Label trailerText = new Label("Software Praktikum, Team 9, Hochschule der Medien"); //Impressum hinzufügen	
	private Label headerLabel = new Label ("Kontaktsystem");
	private Image logo = new Image();
			
	//Symbole für Modify-Buttons
	private Image createSymbol = new Image();
	private Image updateSymbol = new Image();
	private Image deleteSymbol = new Image();
	private Image shareSymbol = new Image();
		
//	private Image saveSymbol = new Image();
//	private Image cancelSymbol = new Image();
//	//Symbole für ContactForm und ContactListForm
//	private Image oneContactSymbol = new Image();
//	private Image ContactListSymbol = new Image();
//		
//	//Symbole für Navigationsmenü-Buttons
//	private Image contactsSymbol = new Image();
//	private Image listSymbol = new Image(); //Alternatives Symbol für Kontaktliste
	
	private Image searchSymbol = new Image();
			
		
	//Symbol für Cells (in Cell-Klasse verschieben?)
	private Image chainSymbol = new Image(); //Symbol für Status geteilt/nicht geteilt
	
	//DockPanel als Root
	DockPanel root = new DockPanel();
	
	//HeaderPanel
	HorizontalPanel header = new HorizontalPanel();
	
	//Suchfunktion
	TextBox search = new TextBox();
	Button searchButton = new Button("Suche");

	//CellTree Model
	ContactsTreeViewModel ctvm = new ContactsTreeViewModel();
	ContactListsTreeViewModel cltvm = new ContactListsTreeViewModel();
	MyParticipationsTreeViewModel mptvm = new MyParticipationsTreeViewModel();
	ReceivedParticipationTreeViewModel rptvm = new ReceivedParticipationTreeViewModel();
					
	//Formulare
	ContactForm cf = new ContactForm();
	ContactListForm clf = new ContactListForm();
	MyParticipationForm mpf = new MyParticipationForm();
	ReceivedParticipationForm rpf = new ReceivedParticipationForm();
			
	/**
	 * Attribute für den Login
	 */
	private User userInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Melden Sie sich mit Ihrem Google Konto an, um auf das Kontaktsystem zuzugreifen ");
	private Anchor signInLink = new Anchor("Login mit Google");
	private Anchor signOutLink = new Anchor("Logout");
	
	
	/**
	 * EntryPoint
	 */
	
	@Override
	public void onModuleLoad() {
		
		// Test -> loadContactSystem();
		
		/**
		 * Login-Status feststellen mit LoginService
		 */		
		
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<User>() {
			public void onFailure(Throwable error) {
				Window.alert("Login Error :(");
			}
				
			//Wenn der User eingeloggt ist, wird die Startseite aufgerufen, andernfalls die Login-Seite
			public void onSuccess(User result) {
				userInfo = result;
				if(userInfo.isLoggedIn()){
					loadContactSystem();
				}else{
					loadLogin();					
				}
			}
		});	
		
	}	
	
	/**
	 * Aufbau der Login-Seite
	 */
	private void loadLogin() {
		Window.alert("Login :D");
		signInLink.setHref(userInfo.getLoginUrl());
		signInLink.setStyleName("link");
		loginPanel.add(new HTML("<center>"));
		loginPanel.add(loginLabel);
		loginPanel.add(new HTML("<br /> <br /> "));
		loginPanel.add(signInLink);
		loginPanel.add(new HTML("</center>"));
		RootPanel.get().add(loginPanel);
	}
		
	
	/**
	 * Aufbau der Startseite des Kontaktsystems. Elemente werden in Listenform dargestellt, 
	 * ausgewählte Elemente werden als Formulare rechts im Bildschirm aufgerufen
	 */


	public void loadContactSystem() {
		
		//Logo
		logo.setWidth("100px");
		logo.setUrl(GWT.getHostPageBaseURL() + "images/LogoTransparent.png");
		
		//RootPanel		
		root.setSpacing(4);
	    root.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
	    
	    //Header
	    header.add(signOutLink);
	    header.add(headerLabel);
	    header.add(logo);
	    
	    //Suchfunktion
  		Grid sg = new Grid(1,2);
  		sg.setWidget(0, 1, search);
  		sg.setWidget(1, 1, searchButton);
  		
	    //Navigation
	    VerticalPanel navigation = new VerticalPanel();	  
	  
	  	navigation.add(sg);
	  	navigation.add(contactButton);
	  	navigation.add(contactListsButton);
	  	navigation.add(myParticipationsButton);
	  	navigation.add(receivedParticipationsButton);  			
	  	
	  	//Default 
	  	VerticalPanel dp = new VerticalPanel();
	  	root.add(dp, DockPanel.EAST);
		
	    //Trailer
	    HorizontalPanel trailer = new HorizontalPanel();
	    trailer.add(trailerText);
	    
		//Header mit SignOut-Link
		if(userInfo != null){ signOutLink.setHref(userInfo.getLogoutUrl());}
		signOutLink.setStyleName("link");
		root.add(header, DockPanel.NORTH);
		root.add(trailer, DockPanel.SOUTH);		
		root.add(navigation, DockPanel.WEST);		
		
		
		// Füllen der Image-Objekte mit Symbolen
		createSymbol.setUrl(GWT.getHostPageBaseURL() + "images/baseline_add_black_18dp.png");
		updateSymbol.setUrl(GWT.getHostPageBaseURL() + "images/baseline_create_black_18dp.png");
		deleteSymbol.setUrl(GWT.getHostPageBaseURL() + "images/baseline_delete_black_18dp.png");
		shareSymbol.setUrl(GWT.getHostPageBaseURL() + "images/baseline_share_black_18dp.png");	
		searchSymbol.setUrl(GWT.getHostPageBaseURL() + "images/baseline_search_black_18dp.png");
		
		//Verlinkung der Listen und der dazugehörigen Formulare
		ctvm.setContactForm(cf);
		cf.setCatvm(ctvm);
		
		cltvm.setContactListForm(clf);
		clf.setCltvm(cltvm);
		
		mptvm.setParticipationForm(mpf);
		mpf.setMptvm(mptvm);
		
		//Zuordnung zur GUI 
		//-> ToDo: Test & Korrigieren, erst zugeordnet, wenn Cell im CellTree ausgewählt wurde
		root.add(cf, DockPanel.EAST);
		root.add(clf, DockPanel.EAST);
		root.add(mpf, DockPanel.EAST);
		root.add(rpf, DockPanel.EAST);	
		
		searchButton.addClickHandler(new SearchClickHandler());
		searchButton.setEnabled(true);
		
		
		/**
		 * Wird im Navigator ein Button ausgewählt, wird die zugehörige Liste (CellTree) aufgerufen. 
		 * Handelt es sich bei den Listenelementen um Kontakte oder Kontaktlisten, wird oberhalb des 
		 * CellTrees ein Button hinzugefügt, mit dem neue Elemente erzeugt werden können.
		 */
		//ClickHandler für ContactButton
		contactButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				/**
				 * Definition des CellTrees, der durch das TreeViewModel aufgebaut wird und
				 * Hinzufügen zur Webseite
				 */
				CellTree.Resources contactTreeRecource = GWT.create(TreeResources.class);
				CellTree cellTree = new CellTree(ctvm, "Root", contactTreeRecource);
				cellTree.setAnimationEnabled(true);	
				root.add(cellTree, DockPanel.CENTER);			
			}
			
		});
		
		
		//Clickhandler für ContactListButton
		contactListsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				/**
				 * Definition des CellTrees, der durch das TreeViewModel aufgebaut wird
				 */
				CellTree.Resources contactListTreeRecource = GWT.create(TreeResources.class);
				CellTree cellTree = new CellTree(ctvm, "Root", contactListTreeRecource);
				cellTree.setAnimationEnabled(true);
				root.add(cellTree, DockPanel.CENTER);		
			}
			
		});
		
		
		//Clickhandler für MyParticipationsButton
		myParticipationsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				/**
				 * Definition des CellTrees, der durch das TreeViewModel aufgebaut wird
				 */
				CellTree.Resources myParticipationTreeRecource = GWT.create(TreeResources.class);
				CellTree cellTree = new CellTree(ctvm, "Root", myParticipationTreeRecource);
				cellTree.setAnimationEnabled(true);
				root.add(cellTree, DockPanel.CENTER);			
			}
		});
	
		
		//Clickhandler für ReceivedParticipationsButton
		receivedParticipationsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				/**
				 * Definition des CellTrees, der durch das TreeViewModel aufgebaut wird
				 */
				CellTree.Resources receivedParticipationTreeRecource = GWT.create(TreeResources.class);
				CellTree cellTree = new CellTree(ctvm, "Root", receivedParticipationTreeRecource);
				cellTree.setAnimationEnabled(true);	
				root.add(cellTree, DockPanel.CENTER);		
			}
		});
			
		RootPanel.get("ContactSystem").add(root);	
	
	}
	
	//Clickhandler für Suchfeld Button -> TODO: Methode überprüfen
	private class SearchClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			if (search.getText().equals("")) {
				Window.alert("Nichts zur Suche ausgewählt");
			} else {
			 String s = search.getText();
			// contactSystemVerwaltung.getPropertyValuesByValue(s, new SearchCallback(Vector<Contact>));
			}
		}
	}

	class SearchCallback implements AsyncCallback<Vector<Contact>> {

		Vector <Contact> contacts = null;
		SearchCallback(Vector<Contact> c) {
			contacts = c;
		}

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Die Suche ist fehlgeschlagen!");
		}

		@Override
		public void onSuccess(Vector<Contact> result) {
			if (contacts != null) {
				for(Contact c : contacts) {
				ctvm.updateContact(c);
				// TODO: 
				}
			}
		}
	}
	
	
			
	

}
