package de.hdm.kontaktsystem.gui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


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

	
	
	/**
	 * Instantiieren der GWT Widgets und Panels
	 */
	
	//Panels
	private VerticalPanel navigation = new VerticalPanel();
	
	//Widgets
	private Button ContactButton = new Button("Kontakte");
	private Button ContactListsButton = new Button("Kontaktlisten");
	private Button MyParticipationsButton = new Button("von mir geteilt");
	private Button ReceivedParticipationsButton = new Button("an mich geteilt");

	
	
	@Override
	public void onModuleLoad() {
		
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
		mptvm.setMyParticipationForm(mpf);
		rptvm.setReceivedParticipationForm(rpf);
		
		
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
				
				RootPanel.get("Liste").add(cellTree);
			}
		});
		
		//Clickhandler für ContactListButton
		ContactListsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				CellTree.Resources contactListTreeRecource = GWT.create(TreeResources.class);
				CellTree cellTree = new CellTree(ctvm, "Root", contactListTreeRecource);
				cellTree.setAnimationEnabled(true);
				
				RootPanel.get("Liste").add(cellTree);
			}
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
