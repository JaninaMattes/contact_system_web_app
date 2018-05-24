package de.hdm.kontaktsystem.gui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellBrowser;
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
	 * Instanziieren der GWT Widgets und Panels
	 */
	
	//Panels
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel addP = new VerticalPanel();
	private HorizontalPanel Panel = new HorizontalPanel();
	private HeaderPanel hp = new HeaderPanel();
	
	//Widgets
	
	private Button ContactButton = new Button("Kontakte");
	private Button ContactListsButton = new Button("Kontaktlisten");
	private Button SharedButton = new Button("Geteilt");
	private Label label = new Label();
	
	
	//Oder mit CellBrowser-Widget umsetzen?
	//private CellBrowser cellB = new CellBrowser();
	
	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		
		/**
		 * Widgets/Panels anordnen
		 */
		
		addP.add(ContactButton);
		addP.add(ContactListsButton);
		addP.add(SharedButton);
		Panel.add(addP);
		
		
		//Panels und Widgets zum Hauptpanel hinzufügen
		mainPanel.add(hp);
		mainPanel.add(Panel);
		mainPanel.add(label);
		
		//Verbindung des Hauptpanels mit dem Rootpanel TODO:Html-Dok.
		RootPanel.get("HTMLPage").add(mainPanel);	
	
		
		
		//Text für Label setzen
		label.setText("Label");
		
		
		//ClickHandler für ContactButton
		ContactButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
			//TODO: def. was passieren soll wenn man auf den Button klickt	
			}
		});
		
		//Clickhandler für ContactListButton
		ContactListsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
			//TODO: def. was passieren soll wenn man auf den Button klickt	
			
			}
		});
		
		//Clickhandler für SharedButton
		SharedButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
			//TODO: def. was passieren soll wenn man auf den Button klickt	
			
			}
		});
	
	
	

}
}
