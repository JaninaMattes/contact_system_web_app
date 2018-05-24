package de.hdm.kontaktsystem.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.shared.bo.Contact;

/**
 * 
 * @author Katalin
 *Formular für die Darstellung und Anzeige eines Kontaktes
 */

public class ContactForm extends VerticalPanel{
	
	Contact angezeigterKontakt = null;
	
	/**
	 * Instanziieren der Widgets
	 */
	private TextBox TextBoxName = new TextBox();
	private Label label = new Label("Kontakt:");
	private Button Kontaktloeschen = new Button("Kontakt löschen");
	private Button Kontakthinzufuegen = new Button("Kontakt hinzufügen");
	
	/**
	 * Instanziieren der Panels
	 */
	//Hauptpanel
	private VerticalPanel vp = new VerticalPanel();
	
	//Panel um die Buttons nebeneinander anzuordnen
	private HorizontalPanel Buttonpanel = new HorizontalPanel();
	
	
	
	public void onLoad() {
		super.onLoad();
		
		/**
		 * Widgets/Panels anordnen
		 */
		//Die TextBox um den Kontakt einzugeben, das label und das Panel für die Buttons werden 
		//untereinander auf dem VerticalPanel angeordnet.
		vp.add(label);
		vp.add(TextBoxName);
		vp.add(Buttonpanel);
		//Die Buttons werden auf dem HorizontalPanel angeordnet
		Buttonpanel.add(Kontaktloeschen);
		Buttonpanel.add(Kontakthinzufuegen);
		
		//Chlickhandler
		Kontakthinzufuegen.addClickHandler(new hinzufClickHandler());
		
		
		Kontaktloeschen.addClickHandler(new loeschenClickHandler());
		
	}

		
		
		private class hinzufClickHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//TODO: def. was passieren soll wenn man auf den Button klickt
					//If-Abfrage um zu prüfen ob etwas in die Textbox eingegeben wurde
				if(TextBoxName != null) {
					
					//Name für einen Kontakt eingeben
					String name = TextBoxName.getText();
					//TODO: Verbindung
					//angezeigterKontakt.getpropertyValue().setName(TextBoxName.getText());
					
				} else {
					Window.alert("kein Kunde eingegeben");
				}
			}
			
		}
		private class loeschenClickHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//TODO: def. was passieren soll wenn man auf den Button klickt
				if (angezeigterKontakt == null) {
					Window.alert("kein Kunde ausgewÃ¤hlt");
				} else {
					//TODO: Verbindung
				}
			}
			
		}
	
}

