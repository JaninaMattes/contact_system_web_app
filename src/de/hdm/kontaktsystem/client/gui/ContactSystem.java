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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.TreeViewModel;

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
	 * Link zum Asynchronen Interface
	 */
	ContactSystemAdministrationAsync contactSystemVerwaltung = null;
	

	/** 
	 * Instanziieren der GWT Widgets und Panels
	 */
	
//	private Image saveSymbol = new Image();
//	private Image cancelSymbol = new Image();
//	//Symbole für ContactForm und ContactListForm
//	private Image oneContactSymbol = new Image();
//	private Image ContactListSymbol = new Image();
//		
//	//Symbole für Navigationsmenü-Buttons
//	private Image contactsSymbol = new Image();
//	private Image listSymbol = new Image(); //Alternatives Symbol für Kontaktliste
	
	//Suchfunktion
	private TextBox search = new TextBox();
	private Button searchButton = new Button("Suche");
	
	//Buttons Menü links
	private Button contactButton = new Button("Kontakte");
	private Button contactListsButton = new Button("Kontaktlisten");
	private Button myParticipationsButton = new Button("Von mir geteilt");
	private Button receivedParticipationsButton = new Button("An mich geteilt");
	//Trailer Text
	private Label trailerText = new Label("Software Praktikum, Team 9, Hochschule der Medien"); //Impressum hinzufügen	
					
	//Symbole für Modify-Buttons
	private Image createSymbol = new Image();
	private Image updateSymbol = new Image();
	private Image deleteSymbol = new Image();
	private Image shareSymbol = new Image();	
	private Image searchSymbol = new Image();			
		
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

	
	/**
	 * EntryPoint
	 */
	
	public void loadTree() {
		ScrollPanel sp = new ScrollPanel();
		TreeViewModel tvm = new TreeViewModelTest();
		CellTree ct = new CellTree(tvm, "Liste");
		sp.setHeight("80vh");
		sp.add(ct);
		
		RootPanel.get("Lists").add(sp);
		
	}
	
	public void onModuleLoad() {
		
		loadTree(); // für Test
		
		this.loadContactSystem(); // für Test		
		
		/**
		 * Login-Status feststellen mit LoginService
		 */		
		/*
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
		});	*/
		
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
	 * Aufbau der Startseite des Kontaktsystems. Elemente werden in Listenform dargestellt, 
	 * ausgewählte Elemente werden als Formulare rechts im Bildschirm aufgerufen
	 */
	public void loadContactSystem() {

		//Detail
		VerticalPanel detailsPanel = new VerticalPanel();
			
		//Header
		HorizontalPanel header = new HorizontalPanel();				
				
		//Navigator
	    VerticalPanel navigation = new VerticalPanel();	
	 
	    //Trailer
	    HorizontalPanel trailer = new HorizontalPanel();
	    
		//Formulare

		ContactForm cf = new ContactForm();
		ContactListForm clf = new ContactListForm();
		MyParticipationForm mpf = new MyParticipationForm();
		ReceivedParticipationForm rpf = new ReceivedParticipationForm();
		
		//CellTree Model
		ContactListTreeViewModel ctvm = new ContactListTreeViewModel();
		ContactListsTreeViewModel cltvm = new ContactListsTreeViewModel();
		MyParticipationsTreeViewModel mptvm = new MyParticipationsTreeViewModel();
		ReceivedParticipationTreeViewModel rptvm = new ReceivedParticipationTreeViewModel();
	  				
		//Logo 
		//chainSymbolLogo.setUrl(GWT.getHostPageBaseURL() + "images/LogoTransparent.png");	    
				    
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
	  			
	    //Trailer
	    trailer.setTitle("Copyright Team09, Hochschule der Medien Stuttgart");
	    
		//Header mit SignOut-Link
		if(userInfo != null){ 
			signOutLink.setHref(userInfo.getLogoutUrl());
		}
		

		/** Siehe aus BankingProjekt Hinweis: -> Set ids using widget.getElement().setId("idOfElement") */
		reportLink.getElement().setId("Report-Button");
		signOutLink.getElement().setId("Log-Out-Button");		
		contactButton.getElement().setId("Menue-Button"); 	
		contactListsButton.getElement().setId("Menue-Button"); 
		myParticipationsButton.getElement().setId("Menue-Button"); 
		receivedParticipationsButton.getElement().setId("Menue-Button"); 
		searchButton.getElement().setId("Search-Button");  
		
		/** 
		 * Der Name, mit welchem das Search-Textfeld in CSS formatiert werden kann, wird festgelegt. 
		 */
		search.getElement().setId("Search-Text"); 		
		/**
		 * CSS Identifier für das Logo
		 */
		chainSymbolLogo.getElement().setId("Logo"); 
		
		/**
		 * CSS Identifier für Elemente
		 */		
		header.setStyleName("Header");		
		navigation.setStyleName("Navigation");
		detailsPanel.setStyleName("Details");
		trailer.setStyleName("Trailer");
		
		/**
		 * Verlinkung der Listen und der dazugehörigen Formulare
		 */
		ctvm.setContactForm(cf);
		cf.setCtvm(ctvm);
		
		cltvm.setContactListForm(clf);
		clf.setCltvm(cltvm);
		cf.setCltvm(cltvm);
		
		mptvm.setParticipationForm(mpf);
		mpf.setMptvm(mptvm);
				
		/**
		 * Wird im Navigator ein Button ausgewählt, wird die zugehörige Liste (CellTree) aufgerufen. 
		 * Handelt es sich bei den Listenelementen um Kontakte oder Kontaktlisten, wird oberhalb des 
		 * CellTrees ein Button hinzugefügt, mit dem neue Elemente erzeugt werden können.
		 */
	
		//TEST -> ClickHandler für ContactButton
		contactButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				/**
				 * Definition des CellTrees, der durch das TreeViewModel aufgebaut wird
				 */
				CellTree.Resources contactListTreeRecource = GWT.create(ContactSystemTreeResources.class);
				CellTree cellTree = new CellTree(cltvm, "Root", contactListTreeRecource);
				cellTree.setAnimationEnabled(true);		
				// Für Test->
				detailsPanel.add(cf);
				RootPanel.get("Details").clear(); /*Alle Widgets von Parent entfernen*/
				RootPanel.get("Details").add(detailsPanel);
			}
			
		});
	
		
		//Clickhandler für ContactListButton
		contactListsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				/**
				 * Definition des CellTrees, der durch das TreeViewModel aufgebaut wird
				 */
				CellTree.Resources contactListTreeRecource = GWT.create(ContactSystemTreeResources.class);
				CellTree cellTree = new CellTree(cltvm, "Root", contactListTreeRecource);
				cellTree.setAnimationEnabled(true);				
			}
			
		});
		
		
		//Clickhandler für MyParticipationsButton
		myParticipationsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				/**
				 * Definition des CellTrees, der durch das TreeViewModel aufgebaut wird
				 */
				CellTree.Resources myParticipationTreeRecource = GWT.create(ContactSystemTreeResources.class);
				CellTree cellTree = new CellTree(mptvm, "Root", myParticipationTreeRecource);
				cellTree.setAnimationEnabled(true);				
			}
		});
	
		
		//Clickhandler für ReceivedParticipationsButton
		receivedParticipationsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				/**
				 * Definition des CellTrees, der durch das TreeViewModel aufgebaut wird
				 */
				CellTree.Resources receivedParticipationTreeRecource = GWT.create(ContactSystemTreeResources.class);
				CellTree cellTree = new CellTree(rptvm, "Root", receivedParticipationTreeRecource);
				cellTree.setAnimationEnabled(true);				
			}
		});
		
		//Header
	    header.add(chainSymbolLogo);
 		header.add(reportLink);
	    header.add(signOutLink);
		
		//Menu Leiste
	  	navigation.add(sg);
	  	navigation.add(contactButton);
	  	navigation.add(contactListsButton);
	  	navigation.add(myParticipationsButton);
	  	navigation.add(receivedParticipationsButton); 

	  	//Detail Panel
	  	detailsPanel.add(cf);
	  	detailsPanel.add(clf);
	  	detailsPanel.add(mpf );
	  	detailsPanel.add(rpf);
	  	
	  	//Contact Cell Tree
		CellTree.Resources contactTreeRecource = GWT.create(ContactSystemTreeResources.class);
		CellTree cellTree = new CellTree(ctvm, "Root", contactTreeRecource);
		cellTree.setAnimationEnabled(true);
		
	  	
	  	RootPanel.get("Header").add(header);
	  	RootPanel.get("Navigator").add(navigation);
	  	RootPanel.get("Lists").add(cellTree);
	  	RootPanel.get("Details").add(detailsPanel);
	  	RootPanel.get("Trailer").add(trailer);
		
	}
	
	//Clickhandler für Suchfeld Button 
	private class SearchClickHandler implements ClickHandler {
		
		@Override
		public void onClick(ClickEvent event) {
			if (search.getText().equals("")) {
				Window.alert("Das Suchfeld ist leer!");
			} else {
			 String s = search.getText();
			 // Suche der Kontakte
			 contactSystemVerwaltung.searchContacts(s, 
					 new SearchCallback(s));
			}
		}
	}

	private class SearchCallback implements AsyncCallback<Vector<Contact>> {

		String search = null;
		
		SearchCallback(String s){
			this.search = s;
		}

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Die Suche ist fehlgeschlagen!");
		}

		@Override
		public void onSuccess(Vector<Contact> result) {
			if (result != null) {
				//Kontakt Objekt der Liste hinzufügen
				for(Contact c : result) {
				//ctvm.addContact(c); -> TODO
				}
			} else {
				Window.alert("Keine Kontakte gefunden :(");
			}
		}
	}
	
	
			
	

}
