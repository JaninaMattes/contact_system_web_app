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
//import com.mysql.jdbc.log.Log;

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


	/**
	 * WIdgets um die Attribute einer Kontaktliste anzuzeigen.
	 */
	Label contactListLabel = new Label("Kontaktliste: ");
	Label contactLabel = new Label("Kontakt: "); 
	
	HorizontalPanel btnPanel = new HorizontalPanel();
	Button deleteConButton = new Button("Kontakt entfernen");
	Button deleteClButton = new Button("Kontaktliste löschen");
	Button saveButton = new Button("Kontaktliste speichern");
	Button shareButton = new Button("Teilen");
	
	Label isShared = new Label("Geteilt: ");
	Label labelShare = new Label("Teilen mit: ");
	Label contactStatus = new Label("");
	Label labelSharedWith = new Label("Geteilt mit: ");
	Label labelReceivedFrom = new Label("Geteilt von: ");
	

	CheckBox checkBox1 = new CheckBox();
	ListBox listBoxShareWith = new ListBox();	
	TextBox textBoxReceivedFrom = new TextBox();
	ListBox listBoxSharedWith = new ListBox();
	TextBox nameContactList = new TextBox();
	ListBox contactNames = new ListBox();

	

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
		contactListGrid.setWidget(1, 2, deleteConButton);

		
		contactListGrid.setWidget(2, 0, labelShare);
		contactListGrid.setWidget(2, 1, listBoxShareWith);
		contactListGrid.setWidget(2, 2, shareButton);	
		
		contactListGrid.setWidget(3, 0, labelSharedWith);
		contactListGrid.setWidget(3, 1, listBoxSharedWith);
		
		contactListGrid.setWidget(4, 0, labelReceivedFrom);
		contactListGrid.setWidget(4, 1, textBoxReceivedFrom);
		
		contactListGrid.setWidget(5, 1, btnPanel);

		contactNames.getElement().setId("ListBox");
		listBoxShareWith.getElement().setId("ListBox");
		
		/**
		 * Panel für Anordnung der Button
		 */

		deleteConButton.setPixelSize(110, 30);
		saveButton.setPixelSize(110, 30);
		deleteClButton.setPixelSize(110, 30);
		btnPanel.add(saveButton);
		btnPanel.add(deleteClButton);
		saveButton.addClickHandler(new saveClickHandler());
		deleteClButton.addClickHandler(new deleteContactListClickHandler());
		
		
		
		/**
		 * CheckBoxen für das Teilen einer ContactList Form
		 * per Default auf "false" gesetzt.
		 * -> Anordnung neben TextBox der Form
		 */
		checkBox1.setEnabled(false);

		
		/*
		 * CSS
		 */
		
		//Textboxen in CSS
		//Für die Textboxen gleicher StyleName (wie auch in ContactForm.java)

		nameContactList.getElement().setId("Textbox");
		contactNames.getElement().setId("ListBox");
		textBoxReceivedFrom.getElement().setId("TextBox");
		listBoxShareWith.getElement().setId("ListBox");
		listBoxSharedWith.getElement().setId("ListBox");

		
		//Labels in CSS
		contactListLabel.getElement().setId("ueberschriftlabel");
		contactLabel.getElement().setId("namelabel");
		contactLabel.getElement().setId("contactlabel");
		contactStatus.getElement().setId("contactstatus");

		
		labelShare.getElement().setId("teilenlabel");
		contactStatus.getElement().setId("contactstatus");
		
		deleteConButton.removeStyleName("gwt-Button"); //um den von GWT f�r Buttons vorgegebenen Style zu l�schen
		deleteConButton.getElement().setId("deleteButton");
		deleteClButton.removeStyleName("gwt-Button"); //um den von GWT f�r Buttons vorgegebenen Style zu l�schen
		deleteClButton.getElement().setId("deleteButton");
		saveButton.removeStyleName("gwt-Button"); //um den von GWT f�r Buttons vorgegebenen Style zu l�schen
		saveButton.getElement().setId("saveButton");
		shareButton.removeStyleName("gwt-Button"); //um den von GWT f�r Buttons vorgegebenen Style zu l�schen
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
				(cl, new SaveCallback());
			//}
		}
	}
	
	private class SaveCallback implements AsyncCallback<ContactList> {

		
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
				
			}
		}
	}
	
	/**
	 * Callback Methode für das Teilen von Kontaktliste
	 * @author Kim-Ly
	 *
	 */

	private class shareCallback 
	implements AsyncCallback<Participation> {

		Participation participation = null;

		shareCallback(Participation part) {
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
	 * Clickhandler zum Löschen von Kontakt aus Kontaktliste
	 * @author Kim-Ly
	 *
	 */
	
	private class deleteConFromListClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			if (contactListToDisplay == null) {
				Window.alert("Keine Kontaktliste ausgewählt");
			} else {
			
			}
		}
	}

	/**
	 * Callback zum Löschen von Kontakt aus Kontaktliste
	 * @author Kim-Ly
	 *
	 */
	private class deleteConfromListCallback 
	implements AsyncCallback<ContactList> {

		public void onFailure(Throwable caught) {
			Window.alert("Löschen eines Kontaktes fehlgeschlagen");
		}

		public void onSuccess(ContactList result) {
			if (result != null) {
				setSelected(null);
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

	private class deleteContactListClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			if (contactListToDisplay == null) {
				Window.alert("keine Kontaktliste ausgewählt");
			} else {
				contactSystemAdmin.deleteContactList(contactListToDisplay, new deleteContactListCallback());
			}
		}

		/**
		 * Callback Methode zum Lösschen einer Kontaktliste
		 * @author Kim-Ly
		 *
		 */
		
		private class deleteContactListCallback 
		implements AsyncCallback<ContactList> {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Löschen der Kontaktliste fehlgeschlagen!");
			}

			@Override
			public void onSuccess(ContactList result) {
				if (result != null) {
					setSelected(null);
					cltvm.removeContactList(result);
					Window.alert("Kontaktliste gelöscht!");
				}
			}
		}
	}

	/**
	 * Setzen des ContactList Objekts, aller zugehörigen Kontakte aus TreeView
	 * und Anzeige aller im System vorhandenen User
	 * @author Kim-Ly
	 */

	void setCltvm(ContactListTreeViewModel cltvm) {
		this.cltvm = cltvm;
	}

	void setSelected(ContactList cl) {

		if (cl != null) {
			int count = 0;
			//Befüllen der Listbox mit allen User Objekten aus dem System
   			Vector <User> u = new Vector<User>();
   			contactSystemAdmin.getAllUsers(new UserToShareCallback(u));
   			contactSystemAdmin.getAllUsers(new UserSharedWithCallback(u));
   			contactSystemAdmin.getAllUsers(new UserSharedByCallback(u));
			contactListToDisplay = cl;
			deleteClButton.setEnabled(true);
			deleteConButton.setEnabled(true);
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
			deleteConButton.setEnabled(false);
		}

	}
	
	/**
	 * UserCallback Klasse zum befüllen der listBoxShareWith mit allen User
	 * aus dem System. Mit diesen kann die Kontaktliste geteilt werden. 
	 * @author Kim-Ly
	 *
	 */
	
	private class UserToShareCallback implements AsyncCallback<Vector<User>> {
		Vector <User> user = null;				
		UserToShareCallback(Vector<User> user){
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
				listBoxShareWith.addItem(user.getUserContact().getName().getValue() + " /" + user.getGMail());	
				++count;
				}
			//Genug Platz schaffen für alle Elemente
				listBoxShareWith.setVisibleItemCount(count);
			} else {
				Window.alert("Kein Nutzer gefunden :(");
			}
		}
	}
	
	/**
	 * UserSharedWithCallback Klasse zum befüllen der listBoxSharedWith mit Usern 
	 * an die die Kontaktliste geteilt wurde.
	 * @author Kim-Ly
	 *
	 */
	
	private class UserSharedWithCallback implements AsyncCallback<Vector<User>> {
		Vector <User> user = null;	
		User u = new User();
		
		//findAllShared
		
		UserSharedWithCallback(Vector<User> user){
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
				listBoxSharedWith.addItem(user.getUserContact().getName().getValue() + " /" + user.getGMail());	
				++count;
				}
			//Genug Platz schaffen für alle Elemente
				listBoxSharedWith.setVisibleItemCount(count);
			} else {
				Window.alert("Kontaktliste ist mit keinem Nutzer geteilt!");
			}
		}
	}
	
	/**
	 * UserSharedByCallback Klasse zum befüllen der listBoxReceivedFrom mit User 
	 * von dem die Kontaktliste geteilt wurde.
	 * @author Kim-Ly
	 *
	 */
	
	private class UserSharedByCallback implements AsyncCallback<Vector<User>> {
		Vector <User> user = null;				
		UserSharedByCallback(Vector<User> user){
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
				textBoxReceivedFrom.setText("");	
			} else {
				Window.alert("Kontaktliste ist mit keinem Nutzer geteilt!");
			}
		}
	}
	
	native void log(String s)/*-{
	console.log(s);
	}-*/;

}