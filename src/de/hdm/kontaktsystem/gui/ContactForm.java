package de.hdm.kontaktsystem.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;

/**
 * 
 * @author Katalin
 *Formular für die Darstellung und Anzeige eines Kontaktes
 */

public class ContactForm extends VerticalPanel{
	
	//ContactSystemAdministrationAsync muss noch erstellt werden
	ContactSystemAdministrationAsync contactSystemVerwaltung = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();
	Contact angezeigterKontakt = null;
	ContactsTreeViewModel ctvm = null;
	
	/**
	 * Instanziieren der Widgets
	 */
	private TextBox TextBoxName = new TextBox();
	private Label label = new Label("Kontakt:");
	private Label labelname = new Label("Name:");
	private Button Kontaktloeschen = new Button("Kontakt löschen");
	private Button Kontaktteilen = new Button("Kontakt teilen");
	private Button Kontaktbearbeiten = new Button("Kontakt bearbeiten");
	
	/**
	 * Instanziieren der Panels
	 */
	//Hauptpanel
	private VerticalPanel vp = new VerticalPanel();
	
	//Panel um die Buttons nebeneinander anzuordnen
	private HorizontalPanel Buttonpanel = new HorizontalPanel();
	
	
	
	public void onLoad() {
		super.onLoad();
		
		Grid contactGrid = new Grid(0, 1);
		
		contactGrid.setWidget(0, 0, labelname);
		contactGrid.setWidget(0, 1, TextBoxName);
		
		//TODO: While-Schleife um alle Eigenschaften eines Kontaktes anzuzeigen
		while (angezeigterKontakt.getpropertyValue() != null) {
			PropertyValue Eigenschaft = angezeigterKontakt.getpropertyValue();
			
		}
		
		/**
		 * Widgets/Panels anordnen
		 */
		//Die TextBox um den Kontakt einzugeben, das label und das Panel für die Buttons werden 
		//untereinander auf dem VerticalPanel angeordnet.
		vp.add(label);
		vp.add(contactGrid);
		vp.add(Buttonpanel);
		
		//Die Buttons werden auf dem HorizontalPanel angeordnet
		
		Kontaktloeschen.addClickHandler(new loeschenClickHandler());
		Kontaktloeschen.setEnabled(false);
		Buttonpanel.add(Kontaktloeschen);
		
		Kontaktteilen.addClickHandler(new teilenClickHandler());
		Buttonpanel.add(Kontaktteilen);
		
		Kontaktbearbeiten.addClickHandler(new bearbeitenClickHandler());
		Buttonpanel.add(Kontaktbearbeiten);
		
	}

		
		
		//Klassen erstellen welche die Clickhandler-Klasse implementieren
	
		//ChlickHandler um einen Kontakt zu loeschen (Bei Klick auf den "Kontakt loeschen"-Button)
		private class loeschenClickHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//TODO: def. was passieren soll wenn man auf den Button klickt
				if (angezeigterKontakt == null) {
					Window.alert("kein Kontakt ausgewählt");
				} else {
					//TODO: Verbindung
				}
			}
		}
			
			//Chlickhandler um einen Kontakt zu Teilen (Bei Klick auf den "Kontakt Teilen"-Button)
			private class teilenClickHandler implements ClickHandler {

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					//TODO: def. was passieren soll wenn man auf den Button klickt
					Window.alert("Kontakt wurde geteilt");
						//TODO: Verbindung
					}
				}
			
			//Chlickhandler um einen Kontakt zu bearbeiten (Bei Klick auf den "Kontakt bearbeiten"-Button)
			private class bearbeitenClickHandler implements ClickHandler {

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					//TODO: def. was passieren soll wenn man auf den Button klickt
					Window.alert("Änderungen wurden gespeichert");
						//TODO: Verbindung
					}
				}
			class deleteContactCallback implements AsyncCallback<Void> {

				Contact contact = null;

				deleteContactCallback(Contact c) {
					contact = c;
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Das Löschen des Kontakts ist fehlgeschlagen!");
				}

				@Override
				public void onSuccess(Void result) {
					if (contact != null) {
						setSelected(null);
						ctvm.removeContact(contact);
					}
				}


			}
			// catvm setter
			void setCatvm(ContactsTreeViewModel ctvm) {
				this.ctvm = ctvm;
			}
			/**
			 * Wenn der anzuzeigende Kontakt gesetzt bzw. gelöscht wird, werden die
			 * zugehörenden Textfelder mit den Informationen aus dem Kontaktobjekt
			 * gefüllt bzw. gelöscht.
			*/
				private void setSelected(Object object) {
					// TODO Auto-generated method stub
					
				}

}


