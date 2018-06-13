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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
//import com.mysql.jdbc.log.Log;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.server.db.ContactMapper;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;



public class ContactListForm extends VerticalPanel {
	
	ContactSystemAdministrationAsync contactSystemAdmin = ClientsideSettings.getContactAdministration();
	ContactList contactListToDisplay = null;
	CellTreeViewModel tvm = null;
	User myUser = null;

	/**
	 * Widgets mit variablen Inhalten.
	 */

	/**
	 * WIdgets um die Attribute einer Kontaktliste anzuzeigen.
	 */

	Label contactListLabel = new Label("Kontaktliste: ");
	Label contactLabel = new Label("Kontakte: "); 
	
	HorizontalPanel btnPanel = new HorizontalPanel();
	Button deleteConButton = new Button("Kontakt entfernen");


	Button deleteClButton = new Button("Kontaktliste löschen");
	Button saveButton = new Button("Kontaktliste speichern");
	Button shareButton = new Button("Teilen");
	Button addConToList = new Button("Hinzufügen");
	
	Label isShared = new Label("Geteilt: ");
	Label labelShare = new Label("Teilen mit: ");
	Label contactStatus = new Label("");
	Label labelSharedWith = new Label("Geteilt mit: ");
	Label labelReceivedFrom = new Label("Eigentümer: ");
	Label labelAddConsToList = new Label("Kontakt hinzufügen ");
	Label clOwner = new Label();
	

	CheckBox checkBox1 = new CheckBox();
	ListBox listBoxShareWith = new ListBox();	
	ListBox listBoxSharedWith = new ListBox();
	TextBox nameContactList = new TextBox();
	ListBox contactNames = new ListBox();
	ListBox contactsToAdd = new ListBox();

	// Update = True, Neu anlegen = false
	boolean update = true;

	
	public void onLoad() {		
		super.onLoad();

		Grid contactListGrid = new Grid(8, 3);
		this.add(contactListGrid);

		contactListGrid.setWidget(0, 0, contactListLabel);
		contactListGrid.setWidget(0, 1, nameContactList);
		contactListGrid.setWidget(0, 2, checkBox1);
		
		contactListGrid.setWidget(1, 0, contactLabel);
		contactListGrid.setWidget(1, 1, contactNames);
		contactListGrid.setWidget(1, 2, deleteConButton);
		
		contactListGrid.setWidget(2, 0, labelAddConsToList);
		contactListGrid.setWidget(2, 1, contactsToAdd);
		contactListGrid.setWidget(2, 2, addConToList);

		
		contactListGrid.setWidget(3, 0, labelShare);
		contactListGrid.setWidget(3, 1, listBoxShareWith);
		contactListGrid.setWidget(3, 2, shareButton);	
		
		contactListGrid.setWidget(4, 0, labelSharedWith);
		contactListGrid.setWidget(4, 1, listBoxSharedWith);
		
		contactListGrid.setWidget(5, 0, labelReceivedFrom);
		contactListGrid.setWidget(5, 1, clOwner);
		
		contactListGrid.setWidget(6, 1, btnPanel);

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
		saveButton.addClickHandler(new saveAndUpdateClickHandler());
		deleteClButton.addClickHandler(new deleteContactListClickHandler());
		deleteConButton.addClickHandler(new deleteConFromListClickHandler());
		addConToList.addClickHandler(new addContactToClClickHandler());
		
		
		
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
		clOwner.getElement().setId("contactlabel");
		listBoxShareWith.getElement().setId("ListBox");
		listBoxSharedWith.getElement().setId("ListBox");
		contactsToAdd.getElement().setId("ListBox");
		contactNames.getElement().setId("Listbox");

		
		//Labels in CSS

		contactListLabel.getElement().setId("ueberschriftlabel");
		contactLabel.getElement().setId("namelabel");


		//Anzeige des Labels für Vorname und Nachname gleicher StyleName

		//firstNameLabel.getElement().setId("namelabel");
		//lastNameLabel.getElement().setId("namelabel");
		contactLabel.getElement().setId("contactlabel");
		contactStatus.getElement().setId("contactstatus");


		labelShare.getElement().setId("teilenlabel");
		contactStatus.getElement().setId("contactstatus");
		

		deleteConButton.removeStyleName("gwt-Button"); //um den von GWT f�r Buttons vorgegebenen Style zu l�schen
		deleteConButton.getElement().setId("deleteButton");
		//Buttons in CSS
		//delete + share + save-Buttons müssen jeweils auch gleich sein

		
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

	private class saveAndUpdateClickHandler implements ClickHandler {
		
		public void onClick(ClickEvent event) {
			
			String clName = nameContactList.getText();
			//contactListToDisplay.setName(clName);
			myUser.setGoogleID(170);
			contactListToDisplay.setOwner(myUser);

			
			if (contactListToDisplay == null) {				
				Window.alert("Keine Kontaktliste ausgewählt");
				
			} else {				
				
				//if (contactListToDisplay.getName() != nameContactList.getText()) {
				String newClName = nameContactList.getText();
				contactListToDisplay.setName(newClName); 
				if (update) {	
					
					contactSystemAdmin.editContactList(contactListToDisplay, new UpdateCallback(contactListToDisplay));
					
					Window.alert("Aktualisierte Kontaktliste" + contactListToDisplay.toString());
					
				} else {
					
					Window.alert("Gespeichert" + contactListToDisplay.toString());
					
					contactSystemAdmin.createContactList(contactListToDisplay, new SaveCallback(contactListToDisplay));
				}	
			}
		}
	}
	
	private class SaveCallback implements AsyncCallback<ContactList> {

		ContactList cl = null;
		SaveCallback (ContactList cl) {
			this.cl = cl;
		}
		
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Anlegen einer neuen KontaktListe ist fehlgeschlagen!");
			
		}

		@Override
		public void onSuccess(ContactList cl) {
			if (cl != null) {
				tvm.addBusinessObject(cl);
			setSelected(cl);
				Window.alert("Neue Kontaktliste gespeichert!");
			}
			
		}
		
	}
	
	/**
	 * Updaten einer überarbeiteten ContactList
	 * @author Kim-Ly
	 */
	
	private class UpdateCallback implements AsyncCallback<ContactList> {

		ContactList cl = null;
		UpdateCallback (ContactList cl) {
			this.cl = cl;		
		}
		
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Aktualisieren der KontaktListe fehlgeschlagen!");
			//log("Aktualisierte Kontaktliste nicht angelegt" + cl.toString());
			
		}

		@Override
		public void onSuccess(ContactList cl) {
			if (cl != null) {
				tvm.updateBusinessObject(cl);
				setSelected(cl);
				Window.alert("Neue Kontaktliste gespeichert!");
				//log("Aktualisierte Kontaktliste angelegt" + cl.toString());
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
				Vector<Contact> conVec = new Vector<Contact>();
				
				Integer lbItemIndex = contactNames.getSelectedIndex();
				String contactName = contactNames.getValue(lbItemIndex);


				conVec = contactListToDisplay.getContacts();
				
				// Suche und Rückgabe nach Kontakt aus Kontaktliste, der mit ausgewählten Kontakt aus ListBox übereinstimmt
				for (Contact con : conVec) {
					if (con.getName().getValue() == contactName) {
						Contact conToDelete = con;
						contactSystemAdmin.removeContactFromList(conToDelete, contactListToDisplay, 
								new deleteConfromListCallback());
					}
				}
				
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
			Window.alert("Entfernen des Kontakts aus Liste fehlgeschlagen");
		}

		public void onSuccess(ContactList result) {
			if (result != null) {
				setSelected(result);
				tvm.updateBusinessObject(result);
				Window.alert("Kontakt wurde erfolgreich aus der Liste entfernt");
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
			
			// Setzen des eingeloggten Users, seines eigenen Kontakts
			myUser.setGoogleID(170);
			contactListToDisplay.setOwner(myUser);
			
			//log(contactListToDisplay.toString());
			
			// Setzen des teilhabenden Users
			User partUser = new User();
			partUser.setGoogleID(666);
			
			if (contactListToDisplay == null) {
				Window.alert("Keine Kontaktliste ausgewählt");
				
			} else {	
				contactSystemAdmin.deleteContactList(contactListToDisplay, new deleteContactListCallback());
				}
			}
		}
		
		/**
		 * Löschen der Teilhaberschaft zur ausgewählten Kontaktliste
		 * @author Kim-Ly
		 *
		 */
		private class deleteContactListPartCallback 
		implements AsyncCallback<Participation> {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Löschen der Kontaktliste Teilhaberschaft fehlgeschlagen!");
			}

			@Override
			public void onSuccess(Participation p) {
				
				ContactList clPart = (ContactList) p.getReferencedObject();
				//log(clPart.toString());
				
				if (clPart != null) {
					//log(clPart.toString());
					setSelected(null);
					tvm.removeBusinessObject(clPart);
					Window.alert("Teilhaberschaft mit Kontaktliste gelöscht!");
				}
			}
		}

		/**
		 * Callback Methode zum Löschen einer Kontaktliste
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
					RootPanel.get("Details").clear();
					tvm.removeBusinessObject(result);
					Window.alert("Kontaktliste gelöscht!");
				}
			}
		}


	/**
	 * Setzen des ContactList Objekts, aller zugehörigen Kontakte aus TreeView
	 * und Anzeige aller im System vorhandenen User
	 * @author Kim-Ly
	 */

	void setTree(CellTreeViewModel tvm) {
		this.tvm = tvm;
	}

	void setSelected(ContactList cl) {

//		if(cl.getBoId()!=0 & cl !=null) {
//			this.contactListToDisplay = cl;
//		}
		// Listen leeren
		contactNames.clear();
		contactsToAdd.clear();
		
		if (cl != null) {
			int count = 0;
			User u = new User();
			update = true;
			
			/* User setzen, sodass Programm Ownership zuordnen kann 
			 * Sobald App Engine -> entfernen
			*/
			setMyUser(u);
   			Vector<User> uVec = new Vector<User>();
			Vector <Participation> partVec = new Vector<Participation>();
   			Vector<Contact> cVec = new Vector<Contact>();
   			Vector<ContactList> clVec = new Vector<ContactList>();
   			
   			// Alle User mit denen Kontaktliste geteilet werden kann
   			contactSystemAdmin.getAllUsers(new UserToShareCallback(uVec));
   			
   			// Alle User mit denen Kontaktliste geteilt wurde
   			contactSystemAdmin.getAllParticipationsByBusinessObject(cl, new UserSharedWithCallback(partVec));
   			
   			// User, von dem Kontaktliste geteilt wurde
   			contactSystemAdmin.getAllContactListsFromUser(new UserOwnerClCallback(clVec));
   			
   			// Alle Kontakte die User angelegt oder die ihm geteilt wurden 
   			contactSystemAdmin.getAllContactsFromUser(new ContactsToAddConCallback(cVec));
   			
   			// Kontakte, die der ausgewählten Kontaktliste hinzugefügt werden können
   			//contactSystemAdmin.getAllContacts(new ContactsToAddCallback());
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
			
			//contactSystemAdmin.getAllContactsFromUser(new ContactsToAddCallback(c));

		} else {
			update = false;
			contactListToDisplay = new ContactList();
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
	
	private class UserSharedWithCallback implements AsyncCallback<Vector<Participation>> {
		
		// Alle User (participant) mit denen Contactlist (reference) geteilt wurde
		Vector <Participation> part = null;	
		// Ich (angemeldeter User)
		User u = new User();
		
		//findAllSharedByMe
		
		UserSharedWithCallback(Vector<Participation> part){
			this.part = part;
	
		}

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Der Aufruf der Nutzer ist misglückt! :( ");
		}

		@Override
		public void onSuccess(Vector <Participation> result) {
			int count = 0;
			if (result != null) {
				
				for(Participation part: result) {						
				//User Liste updaten
				listBoxSharedWith.addItem(part.getParticipant().getGMail());
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
	
	private class UserOwnerClCallback implements AsyncCallback<Vector<ContactList>> {
		Vector <ContactList> clVec = null;				
		UserOwnerClCallback(Vector<ContactList> clVec) {
			this.clVec = clVec;
		}

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Eigentümer der Kontaktliste konnte nicht gefunden werden");
		}

		@Override
		public void onSuccess(Vector <ContactList> clVec) {
			if (clVec != null) {						
				clOwner.setText(contactListToDisplay.getOwner().getGMail());		
				
			} else {
				Window.alert("Eigentümer der Kontaktliste konnte nicht gefunden werden");
			}
		}
	}
	
	/**
	 * ClickHandler zum Hinzufügen eines Kontakts, 
	 * Auswahl aus aller für User geteilten und von User erstellten Kontakte z
	 */
	
	private class addContactToClClickHandler implements ClickHandler {
		
		@Override
		public void onClick(ClickEvent event) {
			
			if (contactListToDisplay == null) {
				Window.alert("Keine Kontaktliste ausgewählt");
				
			} else {

				Integer lbItemIndex = contactsToAdd.getSelectedIndex();
				final String contactToAddName = contactsToAdd.getValue(lbItemIndex);
				
				//TODO: Überarbeiten, über final können 
				
				contactSystemAdmin.getAllContactsFromUser(new AsyncCallback<Vector<Contact>>(){

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("");					
					}

					@Override
					public void onSuccess(Vector<Contact> allConFromUser) {
						
						for (Contact con : allConFromUser) {						
							if (con.getName().getValue() == contactToAddName) {
								contactSystemAdmin.addContactToList(con, contactListToDisplay, new ContactsToAddClCallback());
								//log("Hinzuzufügender Kontakt zu Kontaktliste: " + con.toString());
							}
						}
					}
				});
			}
		}
	}
	
	/**
	 * Callback des aktualisierten Kontaktlistenobjekts nachdem neuer Kontakt hinzugefügt wurde
	 * Befüllung der Listbox mit Kontakten die zu Kontaktliste hinzugefügt wurden
	 * @author Kim-Ly
	 */
	
	private class ContactsToAddClCallback implements AsyncCallback<ContactList> {
		
		
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Keine Kontaktliste vorhanden");			
		}

		@Override
		public void onSuccess(ContactList result) {	
			
			log("Kontaktliste aus Callback" + result);
			int count = 0;
				if (result != null) {
					Vector<Contact> resultCon = result.getContacts();
					
				for(Contact con: resultCon) {						
				
					contactNames.addItem(con.getName().getValue());
					++count;
					}

					contactNames.setVisibleItemCount(count);
				} else {
					Window.alert("Kontakt konnte nicht hinzugefügt werden!");
				}
			}
			
		}
		
	
	/**
	 * Callback aller vom User erstellten und dem User geteilten Kontakte, um einen daraus selektierten Kontakt zur Kontaktliste hinzuzufügen.
	 * Befüllung der Listbox mit Kontakten die zu Kontaktliste hinzugefügt werden können
	 * @author Kim-Ly
	 */
	
	private class ContactsToAddConCallback implements AsyncCallback<Vector<Contact>> {

		Vector<Contact> c= new Vector<Contact>(); 
		
		ContactsToAddConCallback(Vector<Contact> con) {
			this.c = con;
		}
		
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Keine Kontakte im System vorhanden.");
			
		}

		@Override
		public void onSuccess(Vector<Contact> result) {
			
			
			int count = 0;
				if (result != null) {
				for(Contact con: result) {										
					contactsToAdd.addItem(con.getName().getValue());
					++count;
					}

					contactsToAdd.setVisibleItemCount(count);
				} else {
					Window.alert("Kontaktliste ist mit keinem Nutzer geteilt!");
				}
			}
	}

	
	
	void setMyUser(User user) {
		this.myUser = user;
		Contact c = new Contact();
		PropertyValue pV = new PropertyValue();
		pV.setValue("Janina");
		c.setName(pV);
		user.setUserContact(c);
	}
	
	native void log(String s)/*-{
	console.log(s);
	}-*/;

}