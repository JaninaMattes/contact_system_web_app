package de.hdm.kontaktsystem.client.gui;

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

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
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
	TextBox contactName = new TextBox();
	TextBox firstNameContact = new TextBox();
	TextBox lastNameContact = new TextBox();
	// TextBoox sharedContact = new TextBox();

	/**
	 * WIdgets um die Attribute einer Kontaktliste anzuzeigen.
	 */
	Label contactListLabel = new Label("Kontaktliste: ");
	Label contactLabel = new Label("Kontakte: "); 
	Label firstNameLabel = new Label("Vorname: ");
	Label lastNameLabel = new Label("Nachname: ");
	
	HorizontalPanel btnPanel = new HorizontalPanel();
	Button deleteButton = new Button("Kontakt aus einer Liste löschen");
	Button deleteClButton = new Button("KontaktLliste löschen");
	Button saveButton = new Button("Kontakt Liste speichern");
	Button shareButton = new Button("Teilen");
	
	Label labelShare = new Label("Teilen mit: ");
	Label contactStatus = new Label("");
	
	

	CheckBox checkBox1 = new CheckBox();
	CheckBox checkBox2 = new CheckBox();
	CheckBox checkBox3 = new CheckBox();
	CheckBox checkBox4 = new CheckBox();

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
		contactListGrid.setWidget(1, 1, contactName);
		contactListGrid.setWidget(1, 2, checkBox2);

		contactListGrid.setWidget(2, 0, firstNameLabel);
		contactListGrid.setWidget(2, 1, firstNameContact);
		contactListGrid.setWidget(2, 2, checkBox3);

		contactListGrid.setWidget(3, 0, lastNameLabel);
		contactListGrid.setWidget(3, 1, lastNameContact);
		contactListGrid.setWidget(3, 2, checkBox4);
		
		contactListGrid.setWidget(4, 0, labelShare);
		contactListGrid.setWidget(4, 1, shareUser);
		contactListGrid.setWidget(4, 2, shareButton);	
		
		contactListGrid.setWidget(5, 1, btnPanel);

		 
		
		/**
		 * Panel für Anordnung der Button
		 */

		deleteButton.setPixelSize(110, 30);
		saveButton.setPixelSize(110, 30);
		btnPanel.add(saveButton);
		saveButton.addClickHandler(new saveClickHandler());
		btnPanel.add(deleteButton);
		
		
		/*
		 * CheckBoxen für das Teilen einer ContactList Form
		 * per Default auf "false" setzen.
		 * -> Anordnung neben TextBoxen der Form?
		 */
		checkBox1.setEnabled(false);
		checkBox2.setEnabled(false);
		checkBox3.setEnabled(false);
		checkBox4.setEnabled(false);
		
		/**
		 * CSS
		 */
		
		//Textboxen in CSS
		//Für die Textboxen gleicher StyleName (wie auch in ContactForm.java)
		nameContactList.setStyleName("Textbox");
		contactName.setStyleName("Textbox");
		firstNameContact.setStyleName("Textbox");
		lastNameContact.setStyleName("Textbox");
		
		//Labels in CSS
		contactListLabel.setStyleName("contactListlabel");
		contactLabel.setStyleName("contactLabel");
		//Anzeige des Labels für Vorname und Nachname gleicher StyleName
		firstNameLabel.setStyleName("namelabel");
		lastNameLabel.setStyleName("namelabel");
		
		labelShare.setStyleName("labelshare");
		contactStatus.setStyleName("contactStatus");
		
		//Buttons in CSS
		//delete + share + save-Buttons müssen jeweils auch gleich sein
		deleteButton.setStyleName("deleteButton");
		deleteClButton.setStyleName("deleteButton");
		saveButton.setStyleName("saveButton");
		shareButton.setStyleName("shareButton");
		
		
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

			contactListToDisplay = cl;
			deleteButton.setEnabled(true);
			nameContactList.setText(cl.getName());

		} else {
			nameContactList.setText("");
			firstNameContact.setText("");
			lastNameContact.setText("");
			// sharedContact.setText("");

			deleteButton.setEnabled(false);
		}

	}

	public void setSelected(Object object) {
		// TODO Auto-generated method stub

	}

}