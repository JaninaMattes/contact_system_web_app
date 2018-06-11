package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FocusPanel;
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

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.User;


/**
 * Die Klasse <code>ContactSystem</code>
 *@author Janina
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
	ContactSystemAdministrationAsync contactSystemAdmin = null;	

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
	

	//TreeView
	ScrollPanel treeScrollPanel = new ScrollPanel();
	final TreeViewModelTest tvm = new TreeViewModelTest();
	CellTree ct = null;
	
	//Header
	HorizontalPanel headerPanel = new HorizontalPanel();
	Label headerText = new Label("KontaktSystem");
	VerticalPanel suchundlogoPanel = new VerticalPanel(); //um die Suche unter dem Logo anzuordnen
	
	//Suchfunktion
	private TextBox search = new TextBox();
	private Button searchButton = new Button("Suche");
	
	private Label menueLabel = new Label("Menue:");
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
	
	//Logo
	//logo.setUrl(GWT.getHostPageBaseURL() + "images/LogoTransparent.png");
	//logo.setHeight("100px");
	//logo.setAltText("Logo");
	//TODO: Logo einbinden
	
	// Add Button/Panel
	private FocusPanel addPanel = new FocusPanel();

	//Header
	private HorizontalPanel header = new HorizontalPanel();				
			
	//Navigator
    private VerticalPanel navigation = new VerticalPanel();	
 
    //Trailer
    private HorizontalPanel trailer = new HorizontalPanel();
    
	//Formulare

	private ContactForm cf = new ContactForm();
	private ContactListForm clf = new ContactListForm();
	private MyParticipationForm mpf = new MyParticipationForm();
	private ReceivedParticipationForm rpf = new ReceivedParticipationForm();
//	private UserForm uf = new UserForm();
	
	//CellTree Model
	private ContactListTreeViewModel ctvm = new ContactListTreeViewModel();
	//ContactListsTreeViewModel cltvm = new ContactListsTreeViewModel();
	private MyParticipationsTreeViewModel mptvm = new MyParticipationsTreeViewModel();
	private ReceivedParticipationTreeViewModel rptvm = new ReceivedParticipationTreeViewModel();
	
	/**
	 * EntryPoint
	 */
	
	public void loadTree() {		
		
		tvm.setClForm(clf);
		tvm.setCForm(cf);
		treeScrollPanel.setHeight("80vh");
	//	contactSystemAdmin.getAllContactsFromUser(new AsyncCallback<Vector<Contact>>() {
		contactSystemAdmin.getAllContacts(new AsyncCallback<Vector<Contact>>() {
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
				ct.setDefaultNodeSize(result.size());
				treeScrollPanel.add(ct);
				
				
			}

		});
		
		RootPanel.get("Lists").add(treeScrollPanel);
		
		
	}
	
	public void onModuleLoad() {
		
		contactSystemAdmin = ClientsideSettings.getContactAdministration();
		
		loadTree(); // für Test
		loadContactSystem(); // für Test		

		
		/**
		 * Login-Status feststellen mit LoginService
		 */		

		
//		contactSystemVerwaltung = ClientsideSettings.getContactAdministration();
//		contactSystemVerwaltung.login(GWT.getHostPageBaseURL(), new AsyncCallback<User>() {
//			public void onFailure(Throwable error) {
//				Window.alert("Login Error :(");
//			}
//				
//			//Wenn der User eingeloggt ist, wird die Startseite aufgerufen, andernfalls die Login-Seite
//			public void onSuccess(User result) {
//				userInfo = result;
//				if(userInfo.isLoggedIn()){
//					loadTree(); // für Test
//					uf.setUser(userInfo);
//					loadContactSystem(); // für Test	
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
		menueLabel.getElement().setId("menue-label");
		reportLink.getElement().setId("switch-button");
		signOutLink.getElement().setId("log-out-button");
		
		//Label der �berschrift im Header
		headerText.getElement().setId("headertext");
		
		//Der Search-Button bekommt den gleichen Style wie bei Report-Generator.java
		searchButton.getElement().setId("searchButton"); 
		
		/** Menü-Buttons bekommen den gleichen Style und haben deshalb den gleichen StyleName */
		contactButton.getElement().setId("menue-button");
		contactListsButton.getElement().setId("menue-button");
		myParticipationsButton.getElement().setId("menue-button");
		receivedParticipationsButton.getElement().setId("menue-button");
		accountButton.getElement().setId("menue-button");
		
		/** 
		 * Der Name, mit welchem das Search-Textfeld in CSS formatiert werden kann, wird festgelegt. 
		 */
		search.getElement().setId("search-textfeld");

		/**
		 * CSS Identifier für das Logo
		 */
		chainSymbolLogo.getElement().setId("logo");
		
		/*
		 * CSS für Add Panel
		 */
		addPanel.getElement().setId("add");
		
		/**
		 * CSS Identifier für die Elemente
		 */		

		header.getElement().setId("Header");		
		navigation.getElement().setId("Navigation");
		trailer.getElement().setId("Trailer");

		
		/**
		 * Verlinkung der Listen und der dazugehörigen Formulare
		 */
		/**
		ctvm.setContactForm(cf);
		cf.setCtvm(ctvm);
		
		cltvm.setContactListForm(clf);
		clf.setCltvm(cltvm);
		cf.setCltvm(cltvm);
		
		mptvm.setParticipationForm(mpf);
		mpf.setMptvm(mptvm);
		*/	
		/**
		 * Wird im Navigator ein Button ausgewählt, wird die zugehörige Liste (CellTree) aufgerufen. 
		 * Handelt es sich bei den Listenelementen um Kontakte oder Kontaktlisten, wird oberhalb des 
		 * CellTrees ein Button hinzugefügt, mit dem neue Elemente erzeugt werden können.
		 */
	
		contactButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
			//contactSystemAdmin.getAllContactsFromUser(new AsyncCallback<Vector<Contact>>() {
			contactSystemAdmin.getAllContacts(new AsyncCallback<Vector<Contact>>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						log("Keine Kontakte gefunden");
					}

					@Override
					public void onSuccess(Vector<Contact> result) {
						// TODO Auto-generated method stub
						log("Es wurden " + result.size() + " Kontakte gefunden");
						Vector<BusinessObject> bov = new Vector<BusinessObject>();
						for(Contact cl : result){
							bov.add(cl);
						}
						
						tvm.updateData(bov);
						ct.setDefaultNodeSize(result.size());
					}

				});			
			
			}
		});
	
		
		//Clickhandler für ContactListButton
		contactListsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
			//	contactSystemAdmin.getAllContactListsFromUser(new AsyncCallback<Vector<ContactList>>() {
				contactSystemAdmin.getAllContactLists(new AsyncCallback<Vector<ContactList>>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						log("Keine Listen gefunden");
					}

					@Override
					public void onSuccess(Vector<ContactList> result) {
						// TODO Auto-generated method stub
						log("Es wurden " + result.size() + " Listen gefunden");
						Vector<BusinessObject> bov = new Vector<BusinessObject>();
						for(ContactList cl : result){
							bov.add(cl);
						}
						
						tvm.updateData(bov);
						ct.setDefaultNodeSize(result.size());
					}

				});			
				log("Load ContactList");
			}
			
		});
		
		
		//Clickhandler für MyParticipationsButton
		myParticipationsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				contactSystemAdmin.getAllSharedByMe(new AsyncCallback<Vector<BusinessObject>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						log("Keine Listen gefunden");
					}

					@Override
					public void onSuccess(Vector<BusinessObject> result) {
						// TODO Auto-generated method stub
						log("Es wurden " + result.size() + " SharedObjects gefunden");
						
						
						tvm.updateData(result);
						ct.setDefaultNodeSize(result.size());
					}

				});	
				
					log("Load Shared By Me");
			}
		});
	
		
		//Clickhandler für ReceivedParticipationsButton
		receivedParticipationsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				contactSystemAdmin.getAllSharedByOthersToMe(new AsyncCallback<Vector<BusinessObject>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						log("Keine Listen gefunden");
					}

					@Override
					public void onSuccess(Vector<BusinessObject> result) {
						// TODO Auto-generated method stub
						log("Es wurden " + result.size() + " SharedObjects gefunden");
						
						
						tvm.updateData(result);
						ct.setDefaultNodeSize(result.size());
					}

				});	
				
				log("Load Shared With Me");
				
			}
		});
		
		accountButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub		
				
				//RootPanel.get("Details").add(uf); 
			}
			
		});
		
		addPanel.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				// Window.alert("Add something");
				RootPanel.get("Details").clear();
				
				Contact c = null;
				cf.setSelected(c);	
				
				RootPanel.get("Details").add(cf); 
			}
			
		});
		Image add = new Image("/images/add.png");
		add.setPixelSize(50, 50);
		addPanel.add(add);
		
		//Header
		suchundlogoPanel.add(logo);
		suchundlogoPanel.add(sg);
	    suchundlogoPanel.add(chainSymbolLogo);
		headerPanel.add(suchundlogoPanel);
		headerPanel.add(headerText);
 		headerPanel.add(reportLink);
	    headerPanel.add(signOutLink);
	    
		
		//Menu Leiste
	  	navigation.add(menueLabel);
	  	navigation.add(contactButton);
	  	navigation.add(contactListsButton);
	  	navigation.add(myParticipationsButton);
	  	navigation.add(receivedParticipationsButton); 
	  	navigation.add(accountButton);

		
	  	RootPanel.get("Header").add(headerPanel);
	  	RootPanel.get("Navigator").add(navigation);
	  	RootPanel.get("Trailer").add(trailer);
	  	RootPanel.get().add(addPanel);
		
	}
	
	/**
	 * Clickhandler für Search-Button.
	 * @author janina
	 *
	 */
	private class SearchClickHandler implements ClickHandler {
		
		@Override
		public void onClick(ClickEvent event) {
			if (search.getText().equals("")) {
				Window.alert("Das Suchfeld ist leer!");
			} else {
			 String s = search.getText();
			 log("Suche: "+ s);
			 // Suche der Kontakte
			 contactSystemAdmin.searchContacts(s, new AsyncCallback<Vector<Contact>>(){
				 @Override
					public void onFailure(Throwable caught) {
						Window.alert("Die Suche ist fehlgeschlagen!");
					}

					@Override
					public void onSuccess(Vector<Contact> result) {
						if (result != null) {
							Vector<BusinessObject> bov = new Vector<BusinessObject>();
							for(BusinessObject bo : result) {
								bov.add(bo);
							}
							tvm.updateData(bov);
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
