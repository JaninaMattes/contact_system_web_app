package de.hdm.kontaktsystem.gui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.client.LoginService;
import de.hdm.kontaktsystem.client.LoginServiceAsync;
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
	
//	/*
//	 * Zunächst weisen wir der BankAdministration eine Bank-Instanz zu, die
//	 * das Kreditinstitut repräsentieren soll, für das diese Applikation
//	 * arbeitet.
//	 */
//	if (bankVerwaltung == null) {
//		bankVerwaltung = ClientsideSettings.getBankVerwaltung();
//	}
//	Bank bank = new Bank();
//	bank.setName("HdM Bank");
//	bank.setStreet("Nobelstr. 10");
//	bank.setZip(70569);
//	bank.setCity("Stuttgart");
//	bankVerwaltung.setBank(bank, new SetBankCallback());

	
	

	/** Instanziieren der GWT Widgets und Panels
	 */
	
	//Panels
	private VerticalPanel navigation = new VerticalPanel();
	private HorizontalPanel header = new HorizontalPanel();
	
	//Widgets
	private Button ContactButton = new Button("Kontakte");
	private Button ContactListsButton = new Button("Kontaktlisten");
	private Button MyParticipationsButton = new Button("von mir geteilt");
	private Button ReceivedParticipationsButton = new Button("an mich geteilt");

	
	/**
	 * Attribute aus der Klasse Login
	 */
	private User userInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private VerticalPanel root = new VerticalPanel();
	private Label loginLabel = new Label(
	"Melden Sie sich mit Ihrem Google Konto an um auf das Kontaktsystem zuzugreifen ");
	private Anchor signInLink = new Anchor("Login mit Google");
	private Anchor signOutLink = new Anchor("Logout");
	
	@Override
	public void onModuleLoad() {

		/**
		 * Inhalt der Klasse Login
		 */
			// Check login status using login service.
			LoginServiceAsync loginService = GWT.create(LoginService.class);
			loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<User>() {
				public void onFailure(Throwable error) {
				
				}
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
		
		private void loadLogin() {
			// Assemble login panel.
			signInLink.setHref(userInfo.getLoginUrl());
			signInLink.setStyleName("link");
			loginPanel.add(new HTML("<center>"));
			loginPanel.add(loginLabel);
			loginPanel.add(new HTML("<br /> <br /> "));
			loginPanel.add(signInLink);
			loginPanel.add(new HTML("</center>"));
			root.add(loginPanel);
		}
		
		
	
	public void loadContactSystem() {
		
		//Header mit SignOut-Link
		//TODO: Überschrift und Logo hinzufügen
		signOutLink.setHref(userInfo.getLogoutUrl());
		signOutLink.setStyleName("link");
		header.add(signOutLink);
		RootPanel.get("Header").add(header);
		
		
		//Navigation in HTML-Seite einbinden
		navigation.add(ContactButton);
		navigation.add(ContactListsButton);
		navigation.add(MyParticipationsButton);
		navigation.add(ReceivedParticipationsButton);
		RootPanel.get("Navigator").add(navigation);
		
		//Die Anwendung besteht aus einem Navigationsteil in Listenform und einem Datenteil mit Formularen
		//Formulare
		ContactForm cf = new ContactForm();
		ContactListForm clf = new ContactListForm();
		MyParticipationForm mpf = new MyParticipationForm();
		ReceivedParticipationForm rpf = new ReceivedParticipationForm();
		
		//Listen
		ContactsTreeViewModel ctvm = new ContactsTreeViewModel();
		ContactListsTreeViewModel cltvm = new ContactListsTreeViewModel();
		MyParticipationsTreeViewModel mptvm = new MyParticipationsTreeViewModel();
		ReceivedParticipationTreeViewModel rptvm = new ReceivedParticipationTreeViewModel();
		
//		Verlinkung der Formulare und der dazugehörigen Listen
		ctvm.setContactForm(cf);
		cltvm.setContactListForm(clf);
		mptvm.setParticipationForm(mpf);
		rptvm.setParticipationForm(rpf);
		
		
		//Erzeugen der Panels und des CellTrees
		VerticalPanel detailsPanel = new VerticalPanel();
		detailsPanel.add(cf);
		detailsPanel.add(clf);
		detailsPanel.add(mpf);
		detailsPanel.add(rpf);
		
		
		//ClickHandler für ContactButton
		ContactButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				CellTree.Resources contactTreeRecource = GWT.create(TreeResources.class);
				CellTree cellTree = new CellTree(ctvm, "Root", contactTreeRecource);
				cellTree.setAnimationEnabled(true);	
				
				VerticalPanel addPanel = new VerticalPanel();
				Button addButton = new Button("Hinzufügen");
				addPanel.add(addButton);
				addPanel.add(cellTree);
				
				RootPanel.get("Liste").add(addPanel);
			}
			
			//TODO: ClickHandler AddButton
			
		});
		
		//Clickhandler für ContactListButton
		ContactListsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				CellTree.Resources contactListTreeRecource = GWT.create(TreeResources.class);
				CellTree cellTree = new CellTree(ctvm, "Root", contactListTreeRecource);
				cellTree.setAnimationEnabled(true);
				
				VerticalPanel addPanel = new VerticalPanel();
				Button addButton = new Button("Hinzufügen");
				addPanel.add(addButton);
				addPanel.add(cellTree);
				
				RootPanel.get("Liste").add(addPanel);
			}
			
			//TODO: ClickHandler AddButton
			
		});
		
		
		//Clickhandler für MyParticipationsButton
		MyParticipationsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				CellTree.Resources myParticipationTreeRecource = GWT.create(TreeResources.class);
				CellTree cellTree = new CellTree(ctvm, "Root", myParticipationTreeRecource);
				cellTree.setAnimationEnabled(true);
				
				RootPanel.get("Liste").add(cellTree);
			}
		});
	
		//Clickhandler für ReceivedParticipationsButton
		ReceivedParticipationsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				CellTree.Resources receivedParticipationTreeRecource = GWT.create(TreeResources.class);
				CellTree cellTree = new CellTree(ctvm, "Root", receivedParticipationTreeRecource);
				cellTree.setAnimationEnabled(true);	
				
				RootPanel.get("Liste").add(cellTree);
			}
		});
	
		
		RootPanel.get("Details").add(detailsPanel);

		
		
		
//		/**
//		 * Diese Nested Class wird als Callback für das Setzen des Bank-Objekts bei
//		 * der BankAdministration und bei dem ReportGenerator benötigt.
//		 * 
//		 * @author thies
//		 * @version 1.0
//		 */
//		class SetBankCallback implements AsyncCallback<Void> {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				/*
//				 * Wenn ein Fehler auftritt, dann geben wir eine kurze Log Message
//				 * aus.
//				 */
//				ClientsideSettings.getLogger().severe(
//						"Setzen der Bank fehlgeschlagen!");
//			}
//
//			@Override
//			public void onSuccess(Void result) {
//				/*
//				 * Wir erwarten diesen Ausgang, wollen aber keine Notifikation
//				 * ausgeben.
//				 */
//			}
//
//		}
	}
	

}
