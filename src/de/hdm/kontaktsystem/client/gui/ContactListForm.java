package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mysql.jdbc.log.Log;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.User;
//import com.smartgwt.client.types.Alignment;  
//import com.smartgwt.client.types.DragDataAction;  
//import com.smartgwt.client.widgets.Canvas;  
//import com.smartgwt.client.widgets.TransferImgButton;  
//import com.smartgwt.client.widgets.events.ClickEvent;  
//import com.smartgwt.client.widgets.events.ClickHandler;  
//import com.smartgwt.client.widgets.layout.HStack;  
//import com.smartgwt.client.widgets.layout.VStack;  
//import com.smartgwt.sample.showcase.client.data.PartData;  


public class ContactListForm extends VerticalPanel {
	ContactSystemAdministrationAsync contactSystemAdmin = ClientsideSettings.getContactAdministration();
	ContactList contactListToDisplay = null;
	ContactListTreeViewModel cltvm = null;

	/**
	 * Widgets mit variablen Inhalten.
	 */

	TextBox nameContactList = new TextBox();
	ListBox contactNames = new ListBox();


	/**
	 * WIdgets um die Attribute einer Kontaktliste anzuzeigen.
	 */
	Label contactListLabel = new Label("Kontaktliste: ");
	Label contactLabel = new Label("Kontakt: "); 
	
	HorizontalPanel btnPanel = new HorizontalPanel();
	Button deleteButton = new Button("Kontakt löschen");
	Button deleteClButton = new Button("Kontaktliste löschen");
	Button saveButton = new Button("Kontaktliste speichern");
	Button shareButton = new Button("Teilen");
	
	Label labelShare = new Label("Teilen mit: ");
	Label contactStatus = new Label("");
	

	CheckBox checkBox1 = new CheckBox();
	CheckBox checkBox2 = new CheckBox();

	ListBox shareUser = new ListBox();	

	

	public void onLoad() {
		super.onLoad();
		// Keine Tabelle sondern ein VertialPanel / ScrollPanel dem Kontaktelemente aus
		// dem ContactVector hinzugef�gt werden.
		// ... Mit einer while oder for-each Schleife die Kontakt-Elemente erzeugen.
		Grid contactListGrid = new Grid(8, 3);
		this.add(contactListGrid);

		contactListGrid.setWidget(0, 0, contactListLabel);
		contactListGrid.setWidget(0, 1, nameContactList);
		contactListGrid.setWidget(0, 2, checkBox1);
		
		contactListGrid.setWidget(1, 0, contactLabel);
		contactListGrid.setWidget(1, 1, contactNames);
		contactListGrid.setWidget(1, 2, deleteButton);
		//contactListGrid.setWidget(1, 3, checkBox2);
		
		contactListGrid.setWidget(2, 0, labelShare);
		contactListGrid.setWidget(2, 1, shareUser);
		contactListGrid.setWidget(2, 2, shareButton);	
		
		contactListGrid.setWidget(3, 1, btnPanel);

		contactNames.getElement().setId("ListBox");
		shareUser.getElement().setId("ListBox");
		
		/**
		 * Panel für Anordnung der Button
		 */

		deleteButton.setPixelSize(110, 30);
		saveButton.setPixelSize(110, 30);
		deleteClButton.setPixelSize(110, 30);
		btnPanel.add(saveButton);
		saveButton.addClickHandler(new saveClickHandler());
		btnPanel.add(deleteClButton);
		
		/*
		 * CheckBoxen für das Teilen einer ContactList Form
		 * per Default auf "false" setzen.
		 * -> Anordnung neben TextBoxen der Form?
		 */
		checkBox1.setEnabled(false);
		checkBox2.setEnabled(false);

		
		/**
		 * CSS
		 */
		
		//Textboxen in CSS
		//Für die Textboxen gleicher StyleName (wie auch in ContactForm.java)

		nameContactList.getElement().setId("Textbox");
		contactNames.getElement().setId("Listbox");

		
		//Labels in CSS
		contactListLabel.getElement().setId("ueberschriftlabel");
		contactLabel.getElement().setId("contactlabel");

		
		labelShare.getElement().setId("teilenlabel");
		contactStatus.getElement().setId("contactstatus");
		
		//Buttons in CSS
		//delete + share + save-Buttons müssen jeweils auch gleich sein
		deleteButton.getElement().setId("deleteButton");
		deleteClButton.getElement().setId("deleteButton");
		saveButton.getElement().setId("saveButton");
		shareButton.getElement().setId("shareButton");
		
		
	}
	
	
	/**
	 * ClickHandelr zum Speichern der KontaktListe 
	 * @author Kim-Ly
	 *
	 */

	private class saveClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			String clName = nameContactList.getText();
			ContactList cl = new ContactList();
			User u = new User();
			u.setGoogleID(126);
			cl.setName(clName);	
			cl.setOwner(u);
//			if (cl == null) {
//				Window.alert("Keine Kontaktliste ausgewählt");
//			} else {
				Window.alert(cl.toString());
				contactSystemAdmin.createContactList
				(cl, new CreateClCallback());
			//}
		}
	}
	
	private class CreateClCallback implements AsyncCallback<ContactList> {

		
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Anlegen einer neuen KontaktListe ist fehlgeschlagen!");
			
		}

		@Override
		public void onSuccess(ContactList cl) {
			if (cl != null) {
				Window.alert("Neue Kontaktliste gespeichert!");
			}
			
		}
		
	}

	/**
	 * ClickHandler zum Teilen der KontaktListe mit dem ausgewählten User
	 * @author Kim-Ly
	 *
	 */
	
	private class shareClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			if (contactListToDisplay == null) {
				Window.alert("Keine Kontaktliste ausgewählt");
			} else {
				User u = new User();
				u.setGMail("NeuerUser@gmail.com"); // Testdaten, sollte beim Teilen �ber ein Popup abgefragt werden.
				Participation part = new Participation();
				part.setParticipant(u);
				part.setReference(contactListToDisplay);
				// TODO:
				// contactSystemAdmin.shareContactListWith(part, new
				// shareContactListWithCallback(part));
				// ALTERNATIV: contactSystemAdmin.createParticipation(contactListToDisplay, u ,
				// new AsyncCallback<Participation>() {
				// In andere NestedKlasse ausgliedern, wie bei "delete Clickhandler"
			}
		}
	}
	
	/**
	 * Callback Methode für das Teilen von Kontaktliste
	 * @author Kim-Ly
	 *
	 */

	private class shareContactListWithCallback 
	implements AsyncCallback<Participation> {

		Participation participation = null;

		shareContactListWithCallback(Participation part) {
			participation = part;
		}

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			// User nicht gefunden
		}

		// @Override
		public void onSuccess(Participation result) {
			// Eventuell ein Update der Liste

			Window.alert("KontaktListe wurde erfolgreich geteilt.");
		}

	}


	/**
	 * ClickHandler zum Löschen von Kontakten aus Kontaktliste
	 * @author Kim-Ly
	 *
	 */
	private class deleteContactfromListCallback 
	implements AsyncCallback<ContactList> {

		public void onFailure(Throwable caught) {
			Window.alert("Löschen eines Kontaktes fehlgeschlagen");
		}

		public void onSuccess(ContactList result) {
			if (result != null) {
				setSelected(null);
				cltvm.removeContactList(result);
				Window.alert("Kontakt wurde erfolgreich aus der Liste gelöscht");
			}
		}
	}

	/**
	 * Clickhandler zum Loeschen einer Kontaktliste.
	 * 
	 * @author Kim-Ly
	 *
	 */

	private class deleteClClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			if (contactListToDisplay == null) {
				Window.alert("keine Kontaktliste ausgew�hlt");
			} else {
				contactSystemAdmin.deleteContactList(contactListToDisplay, new deleteContactListCallback());
			}
		}

		private class deleteContactListCallback 
		implements AsyncCallback<ContactList> {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Löschen der Kontaktliste fehlgeschlagen");
			}

			@Override
			public void onSuccess(ContactList result) {
				if (result != null) {
					setSelected(null);
					cltvm.removeContactList(result);
				}
			}
		}
	}

	/**
	 * cstvm setter
	 */

	void setCltvm(ContactListTreeViewModel cltvm) {
		this.cltvm = cltvm;
	}

	void setSelected(ContactList cl) {

		if (cl != null) {
			int count = 0;
			contactListToDisplay = cl;
			deleteClButton.setEnabled(true);
			nameContactList.setText(cl.getName());
			Vector<Contact> conVec = cl.getContacts();

				for (Contact con : conVec) {	
					contactNames.addItem(con.getName().getValue());
					++count;
				}
			contactNames.setVisibleItemCount(count);

		} else {
			nameContactList.setText("");
			deleteClButton.setEnabled(false);
			contactNames.addItem("Keine Kontakte vorhanden");
			deleteButton.setEnabled(false);
		}

	}
	
	/**
	 * UserCallback Klasse zum befüllen der ListBox mit User -Objekten 
	 * aus dem System. 
	 * @author janina
	 *
	 */
	
	private class UserCallback implements AsyncCallback<Vector<User>> {
		Vector <User> user = null;				
		UserCallback(Vector<User> user){
			this.user = user;
		}

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Der Aufruf der Nutzer ist misglückt! :( ");
		}

		@Override
		public void onSuccess(Vector <User> result) {
			int count = 0;
			if (result != null) {
				for(User user: result) {						
				//User Liste updaten
				shareUser.addItem(user.getUserContact().getName().getValue() + " /" + user.getGMail());	
				++count;
				}
			//Genug Platz schaffen für alle Elemente
			shareUser.setVisibleItemCount(count);
			} else {
				Window.alert("Kein Nutzer gefunden :(");
			}
		}
	}
	
	

	public void setSelected(Object object) {
		// TODO Auto-generated method stub

	}
	
	native void log(String s)/*-{
	console.log(s);
	}-*/;

}