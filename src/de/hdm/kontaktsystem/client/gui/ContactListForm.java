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
	Label contactStatus = new Label("Status");
	Label contactStatusValue = new Label("");
	Label labelSharedWith = new Label("Geteilt mit: ");
	Label labelReceivedFrom = new Label("Eigentümer: ");
	Label labelAddConsToList = new Label("Kontakt hinzufügen ");
	Label clOwner = new Label();
	

	ListBox listBoxShareWith = new ListBox();	
	ListBox listBoxSharedWith = new ListBox();
	TextBox nameContactList = new TextBox();
	ListBox contactNames = new ListBox();
	ListBox contactsToAdd = new ListBox();

	// Update = True, Neu anlegen = false
	boolean update = true;

	
	public ContactListForm() {		
		

		Grid contactListGrid = new Grid(8, 3);
		this.add(contactListGrid);

		contactListGrid.setWidget(0, 0, contactStatus);
		contactListGrid.setWidget(0, 1, contactStatusValue);
		
		contactListGrid.setWidget(1, 0, contactListLabel);
		contactListGrid.setWidget(1, 1, nameContactList);
		
		contactListGrid.setWidget(2, 0, contactLabel);
		contactListGrid.setWidget(2, 1, contactNames);
		contactListGrid.setWidget(2, 2, deleteConButton);
		
		contactListGrid.setWidget(3, 0, labelAddConsToList);
		contactListGrid.setWidget(3, 1, contactsToAdd);
		contactListGrid.setWidget(3, 2, addConToList);

		
		contactListGrid.setWidget(4, 0, labelShare);
		contactListGrid.setWidget(4, 1, listBoxShareWith);
		contactListGrid.setWidget(4, 2, shareButton);	
		
		contactListGrid.setWidget(5, 0, labelSharedWith);
		contactListGrid.setWidget(5, 1, listBoxSharedWith);
		
		contactListGrid.setWidget(6, 0, labelReceivedFrom);
		contactListGrid.setWidget(6, 1, clOwner);
		
		contactListGrid.setWidget(7, 1, btnPanel);

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

		
		/*
		 * CSS
		 */
		
		//Textboxen in CSS
		//Für die Textboxen gleicher StyleName (wie auch in ContactForm.java)


//		nameContactList.getElement().setId("Textbox");
//		contactNames.getElement().setId("ListBox");
		clOwner.getElement().setId("contactlabel");
//		listBoxShareWith.getElement().setId("ListBox");
//		listBoxSharedWith.getElement().setId("ListBox");
//		contactsToAdd.getElement().setId("ListBox");
//		contactNames.getElement().setId("Listbox");

		
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
					
					contactSystemAdmin.editContactList(contactListToDisplay, new UpdateCallback());
					
					Window.alert("Aktualisierte Kontaktliste" + contactListToDisplay.toString());
					
				} else {
					
					Window.alert("Gespeichert" + contactListToDisplay.toString());
					
					contactSystemAdmin.createContactList(contactListToDisplay, new SaveCallback());
				}	
			}
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
			}	
		}
	}

	/**
	 * *************************** ÜBERARBEITEN ***************************************
	 * ClickHandler zum Teilen der KontaktListe mit dem ausgewählten User
	 * @author Kim-Ly
	 **********************************************************************************
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
		
		private class ContactListStatusCallback
		implements AsyncCallback<Vector<Participation>> {

					
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Fehler beim Abruf des Teilhaber");
;				
			}


			@Override
			public void onSuccess(Vector<Participation> partVec) {
				
				for (Participation part : partVec) {
					
					if (part.getReferencedObject().getBoId() == contactListToDisplay.getBoId()) {
						contactStatusValue.setText("Teilhaber");
					} else {
						contactStatusValue.setText("Besitzer");
					}
					
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
		
		int cl_boID = cl.getBoId();
		
		/*
		 * Callback für Kontakte der Kontaktliste, da TreeView nur bo_ID & Kontaktlistenname weitergibt 
		 */
		contactSystemAdmin.getContactsFromList(cl, new AsyncCallback<Vector<Contact>>() {

			@Override
			public void onFailure(Throwable caught) {
				log("");
				
			}

			@Override
			public void onSuccess(Vector<Contact> conVec) {
				int count = 0;
				for (Contact con : conVec) {

						contactNames.addItem(con.getName().getValue());
						++count;
				}
				
				contactNames.setVisibleItemCount(count);
			}
			
		});
		
		// Listen leeren
		contactNames.clear();
		contactsToAdd.clear();
		
		if (cl != null) {
			int count = 0;
			User u = new User();
			u.setGoogleID(170);
			update = true;
			
			/* User setzen, sodass Programm Ownership zuordnen kann 
			 * Sobald App Engine -> entfernen
			*/
			setMyUser(u);
			
			/*
			 *  Status der Kontaktliste anzeigen
			 */
			contactSystemAdmin.getAllParticipationsByParticipant(myUser, new ContactListStatusCallback());
   			
   			/*
   			 *  Alle User mit denen Kontaktliste geteilt werden kann
   			 */
   			contactSystemAdmin.getAllUsers(new UserToShareCallback());
   			
   			
			/* Alle User mit denen Kontaktliste geteilt wurde 
			 * -> Nur Callback abrufen wenn User Besitzer der Kontaktliste, sonst Listbox + Label unsichtbar
			 */ 			
			if (contactListToDisplay.getOwner() == myUser) {
				contactSystemAdmin.getAllParticipationsByBusinessObject(cl, new UserSharedWithCallback());
			} else {
				labelSharedWith.setVisible(false);
				listBoxSharedWith.setVisible(false);
			}

			/* 
			 * Eigentümer der Kontaktliste anzeigen, wenn User nicht Eigentümer ist
			 */
			if (contactListToDisplay.getOwner() != myUser) {			
				clOwner.setText(contactListToDisplay.getOwner().getGMail());
				contactSystemAdmin.getAllContactListsFromUser(new UserOwnerClCallback());
			} else {
				labelReceivedFrom.setVisible(false);
				clOwner.setVisible(false);
			}
			
   			
   			/*
   			 *  Alle Kontakte die User angelegt hat oder die ihm geteilt wurden 
   			 */
			
   			contactSystemAdmin.getAllContactsFromUser(new ContactsToAddConCallback());
   			
			contactListToDisplay = cl;
			deleteClButton.setEnabled(true);
			deleteConButton.setEnabled(true);
			nameContactList.setText(cl.getName());
			
			

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

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Der Aufruf der Nutzer ist misglückt! :( ");
		}

		@Override
		public void onSuccess(Vector <User> result) {
			int count = 0;
			if (result != null) {
				for(User user: result) {						
				
				// Leeren der bisherigen Listbox mit 
				listBoxShareWith.clear();	
				
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
	

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Der Aufruf der Nutzer ist misglückt! :( ");
		}

		@Override
		public void onSuccess(Vector <Participation> result) {
			int count = 0;
			if (result != null) {
				
				// Listbox mit Usern an die geteilt wurde leeren
				listBoxSharedWith.clear();
				
				for(Participation part: result) {	
					
				//log("User mit denen Kontaktliste geteilt wurde" + part.getParticipant().getGMail());
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
	 * UserSharedByCallback Klasse zum befüllen der labelReceivedFrom mit User 
	 * von dem die Kontaktliste geteilt wurde.
	 * @author Kim-Ly
	 *
	 */
	
	private class UserOwnerClCallback implements AsyncCallback<Vector<ContactList>> {


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

				// Extraktion des ausgewählten Kontakts als String 
				Integer lbItemIndex = contactsToAdd.getSelectedIndex();
				final String contactToAddName = contactsToAdd.getValue(lbItemIndex);
				
				//TODO: Überarbeiten, Callback verursacht Zeitverzögerung?
				
				contactSystemAdmin.getAllContactsFromUser(new AsyncCallback<Vector<Contact>>(){

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("");					
					}

					@Override
					public void onSuccess(Vector<Contact> allConFromUser) {	
						 
						for (Contact conFromUser : allConFromUser) {
							
							// Wenn ausgewählter Contact aus Listbox mit Contact des Users übereinstimmt
							if (conFromUser.getName().getValue() == contactToAddName) {
								
								for (Contact conFromCl : contactListToDisplay.getContacts()) {
									// und hinzuzufügender Kontakt nicht bereits als Kontakt in Kontaktlist vorhanden
									if (conFromUser.getName().getValue() != conFromCl.getName().getValue()) {
										// ausgewählten Kontakt zu Kontaktliste hinzufügen
										contactSystemAdmin.addContactToList(conFromUser, contactListToDisplay, new ContactsToAddClCallback());
										//log("Hinzuzufügender Kontakt zu Kontaktliste: " + con.toString());
									} else {
										Window.alert("Kontakt bereits in Kontaktliste vorhanden");
									}
								}
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
					
					// Leeren der Kontaktliste Listbox, damit Werte nicht doppelt angelegt werden
					contactNames.clear();
	
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

		
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Keine Kontakte im System vorhanden.");
			
		}

		@Override
		public void onSuccess(Vector<Contact> result) {
			
			
			int count = 0;
				if (result != null) {
				for(Contact con: result) {	
					
					// Leeren der Kontakte die hinzugefügt werden können
					contactsToAdd.clear();
					
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
	}
	
	native void log(String s)/*-{
	console.log(s);
	}-*/;

}