package de.hdm.kontaktsystem.client.gui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.client.LoginService;
import de.hdm.kontaktsystem.client.LoginServiceAsync;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
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
	private VerticalPanel root = new VerticalPanel();
	private HorizontalPanel header = new HorizontalPanel(); //Logo, Logout, Report-Link
	private HorizontalPanel content = new HorizontalPanel(); //Menü, Liste/cellTree und Buttons+Suche, Forms
	private VerticalPanel navigation = new VerticalPanel(); //Menü links mit 4 Buttons
	
	//Buttons Menü links
	private Button ContactButton = new Button("Kontakte");
	private Button ContactListsButton = new Button("Kontaktlisten");
	private Button MyParticipationsButton = new Button("Von mir geteilt");
	private Button ReceivedParticipationsButton = new Button("An mich geteilt");
	
	private VerticalPanel addPanel = new VerticalPanel(); //Umfasst Liste/cellTree, Buttons+Suche und Forms
	private HorizontalPanel listAndForm = new HorizontalPanel(); //Liste/cellTree und Forms
	private VerticalPanel detailsPanel = new VerticalPanel(); //Enthält Formulare
	private HorizontalPanel modifyPanel = new HorizontalPanel(); //Enthält Buttons zum bearbeiten und Suchfunktion
	private HorizontalPanel searchPanel = new HorizontalPanel(); //enthält Suchfeld und Button
	
	private VerticalPanel trailer = new VerticalPanel();
	private Label trailerText = new Label("Software Praktikum, Team 9, Hochschule der Medien"); //Impressum hinzufügen
	
	
	/**
	 * Attribute für den Login
	 */
	private User userInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Melden Sie sich mit Ihrem Google Konto an, um auf das Kontaktsystem zuzugreifen ");
	private Anchor signInLink = new Anchor("Login mit Google");
	private Anchor signOutLink = new Anchor("Logout");
	
	@Override
	public void onModuleLoad() {
		//Window.alert("ModuleLoad");
		/**
		 * Login-Status feststellen mit LoginService
		 */
		loadContactSystem();
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
		});	
		*/
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
	 * Aufbau der Startseite des Kontaktsystems
	 */
	public void loadContactSystem() {
		//Window.alert("Content :D");
		//Header mit SignOut-Link
		//TODO: Überschrift und Logo hinzufügen
		if(userInfo != null){ signOutLink.setHref(userInfo.getLogoutUrl());}
		signOutLink.setStyleName("link");
		header.add(signOutLink);
		root.add(header);
		
		
		//Navigation in HTML-Seite einbinden
		navigation.add(ContactButton);
		navigation.add(ContactListsButton);
		navigation.add(MyParticipationsButton);
		navigation.add(ReceivedParticipationsButton);
		content.add(navigation);
		
		
		/**
		 * Elemente werden in Listenform dargestellt, ausgewählte Elemente werden 
		 * als Formulare rechts im Bildschirm aufgerufen
		 */
		
		//Listen
		//final nach Fehlermeldung hinzugefügt
		final ContactsTreeViewModel ctvm = new ContactsTreeViewModel();
		final ContactListsTreeViewModel cltvm = new ContactListsTreeViewModel();
		final MyParticipationsTreeViewModel mptvm = new MyParticipationsTreeViewModel();
		final ReceivedParticipationTreeViewModel rptvm = new ReceivedParticipationTreeViewModel();
				
		//Formulare
		final ContactForm cf = new ContactForm();
		final ContactListForm clf = new ContactListForm();
		final MyParticipationForm mpf = new MyParticipationForm();
		final ReceivedParticipationForm rpf = new ReceivedParticipationForm();

		//Verlinkung der Listen und der dazugehörigen Formulare
		ctvm.setContactForm(cf);
		cf.setCatvm(ctvm);
		
		cltvm.setContactListForm(clf);
		clf.setCltvm(cltvm);
		
		mptvm.setParticipationForm(mpf);
		mpf.setMptvm(mptvm);
		
		rptvm.setParticipationForm(rpf);
		rpf.setMptvm(mptvm);
		
		//Erzeugen des Details-Panels und Zuordnung zur GUI
		detailsPanel.add(cf);
		detailsPanel.add(clf);
		detailsPanel.add(mpf);
		detailsPanel.add(rpf);
		
		
		/**
		 * Wird im Navigator ein Button ausgewählt, wird die zugehörige Liste (CellTree) aufgerufen. 
		 * Handelt es sich bei den Listenelementen um Kontakte oder Kontaktlisten, wird oberhalb des 
		 * CellTrees ein Button hinzugefügt, mit dem neue Elemente erzeugt werden können.
		 */
		//ClickHandler für ContactButton
		ContactButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				/**
				 * Definition des CellTrees, der durch das TreeViewModel aufgebaut wird und
				 * Hinzufügen zur Webseite
				 */
				CellTree.Resources contactTreeRecource = GWT.create(TreeResources.class);
				CellTree cellTree = new CellTree(ctvm, "Root", contactTreeRecource);
				cellTree.setAnimationEnabled(true);	
				listAndForm.add(cellTree);
				
				
				/**
				 * Definition der Buttons und der Suchfunktion, die sich auf Kontakte beziehen
				 */
				Button addContactButton = new Button("Add");
				Button editContactButton = new Button("Edit");
				Button shareContactButton = new Button("Share");
				Button deleteContactButton = new Button("Delete");
				TextBox searchContactText = new TextBox();
				Button searchContactButton = new Button("Search");
				
				//TODO: Funktionalität, Symbole statt Schrift
				
				/**
				 * Hinzufügen der Buttons zum modifyPanel
				 */
				modifyPanel.add(addContactButton);
				modifyPanel.add(editContactButton);
				modifyPanel.add(shareContactButton);
				modifyPanel.add(deleteContactButton);
				
				/**
				 * Aufbau des Suchfeldes
				 */
				searchPanel.add(searchContactText);
				searchPanel.add(searchContactButton);

				modifyPanel.add(searchPanel);
			}
			
		});
		
		
		//Clickhandler für ContactListButton
		ContactListsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				/**
				 * Definition des CellTrees, der durch das TreeViewModel aufgebaut wird
				 */
				CellTree.Resources contactListTreeRecource = GWT.create(TreeResources.class);
				CellTree cellTree = new CellTree(ctvm, "Root", contactListTreeRecource);
				cellTree.setAnimationEnabled(true);
				listAndForm.add(cellTree);	
				
				/**
				 * Definition der Buttons und der Suchfunktion, die sich auf Kontaktlisten beziehen
				 */
				Button addContactListButton = new Button("Add");
				Button editContactListButton = new Button("Edit");
				Button shareContactListButton = new Button("Share");
				Button deleteContactListButton = new Button("Delete");
				TextBox searchContactListText = new TextBox();
				Button searchContactListButton = new Button("Search");
				
				//TODO: Funktionalität, Symbole statt Schrift
				
				/**
				 * Hinzufügen der Buttons zum modifyPanel
				 */
				modifyPanel.add(addContactListButton);
				modifyPanel.add(editContactListButton);
				modifyPanel.add(shareContactListButton);
				modifyPanel.add(deleteContactListButton);
				
				/**
				 * Aufbau des Suchfeldes
				 */
				searchPanel.add(searchContactListText);
				searchPanel.add(searchContactListButton);

				modifyPanel.add(searchPanel);
			}
			
		});
		
		
		//Clickhandler für MyParticipationsButton
		MyParticipationsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				/**
				 * Definition des CellTrees, der durch das TreeViewModel aufgebaut wird
				 */
				CellTree.Resources myParticipationTreeRecource = GWT.create(TreeResources.class);
				CellTree cellTree = new CellTree(ctvm, "Root", myParticipationTreeRecource);
				cellTree.setAnimationEnabled(true);
				listAndForm.add(cellTree);				
				
				/**
				 * Definition der Buttons und der Suchfunktion, die sich auf Teilhaberschaften beziehen, die mir gehören
				 */
				Button deleteParticipationButton = new Button("Delete");
				//Suche anhand des Users, mit dem geteilt wurde
				TextBox searchParticicipationByParticipantText = new TextBox();
				Button searchParticicipationByParticipantButton = new Button("Search");
				
				//TODO: Funktionalität, Symbole statt Schrift
				
				/**
				 * Hinzufügen der Buttons zum modifyPanel
				 */

				modifyPanel.add(deleteParticipationButton);
				
				/**
				 * Aufbau des Suchfeldes
				 */
				searchPanel.add(searchParticicipationByParticipantText);
				searchPanel.add(searchParticicipationByParticipantButton);

				modifyPanel.add(searchPanel);
			}
		});
	
		
		//Clickhandler für ReceivedParticipationsButton
		ReceivedParticipationsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				/**
				 * Definition des CellTrees, der durch das TreeViewModel aufgebaut wird
				 */
				CellTree.Resources receivedParticipationTreeRecource = GWT.create(TreeResources.class);
				CellTree cellTree = new CellTree(ctvm, "Root", receivedParticipationTreeRecource);
				cellTree.setAnimationEnabled(true);	
				listAndForm.add(cellTree);				
				
				/**
				 * Definition der Buttons und der Suchfunktion, die sich auf Teilhaberschaften beziehen, die mit mir geteilt wurden
				 */
				Button deleteParticipationButton = new Button("Delete");
				//Suche anhand des Users, von dem Objekte erhalten wurden
				TextBox searchParticicipationByOwnerText = new TextBox();
				Button searchParticicipationByOwnerButton = new Button("Search");
				
				//TODO: Funktionalität, Symbole statt Schrift
				
				/**
				 * Hinzufügen der Buttons zum modifyPanel
				 */

				modifyPanel.add(deleteParticipationButton);
				
				/**
				 * Aufbau des Suchfeldes
				 */
				searchPanel.add(searchParticicipationByOwnerText);
				searchPanel.add(searchParticicipationByOwnerButton);

				modifyPanel.add(searchPanel);
			}
		});
	
		//Hinzufügen des Detail-Panels zum Content
		listAndForm.add(detailsPanel);	
		
		//Aufbau der restlichen Webseite
		addPanel.add(listAndForm);
		addPanel.add(modifyPanel);
		
		content.add(addPanel);
		
		root.add(content);
		
		trailer.add(trailerText);
		root.add(trailer);
		
		RootPanel.get("Editor").add(root);
		
	
	}
	

}
