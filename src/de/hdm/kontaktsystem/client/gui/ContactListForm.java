package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
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

/**
 * Die Klasse <code>ContactListForm</code> enthält Elemente für die Darstellung
 * eines Kontaktlistenformulars der jeweilig selektierten Kontaktliste aus dem
 * Kontaktsystem.
 * 
 * Darüber hinaus ermöglicht dies auch durch Austausch einzelner Elemente, wie
 * bspw. Buttons die Darstellung eines leeren Kontaktformulars zur
 * Ausformulierung der Kontakteigenschaften, vor der Erzeugung eines neuen
 * <code>Contact</code> Objektes in der DB.
 * 
 * Analog zur Klasse ContactListForm wurde auch die Klasse ContactForm
 * {@link ContactForm} für das Anzeigen und die Darstellung der Kontaktlisten
 * angelegt.
 * 
 * @author Kim-Ly Le
 */

public class ContactListForm extends VerticalPanel {

	ContactSystemAdministrationAsync contactSystemAdmin = ClientsideSettings.getContactAdministration();
	ContactList contactListToDisplay = null;
	CellTreeViewModel tvm = null;
	User myUser = null;

	// Lade overlay
	PopupPanel loadPanel;

	// Share DialogBox
	DialogBox shareDialog = new DialogBox();
	
	

	/**
	 * Widgets um die Attribute einer Kontaktliste anzuzeigen.
	 */

	Label contactListLabel = new Label("Kontaktliste: ");
	Label contactLabel = new Label("Kontakte: ");

	HorizontalPanel btnPanel = new HorizontalPanel();
	Button deleteConButton = new Button("Kontakt entfernen");

	Button deleteClButton = new Button("Löschen");
	Button saveButton = new Button("Speichern");
	Button shareButton = new Button("Teilen");
	Button cancelShareButton = new Button("Abbrechen"); // Teilen abbrechen
	Button cancelNewButton = new Button("Abbrechen"); // Neu anlegen abbrechen
	Button okButton = new Button("Teilen");
	Button unShareButton = new Button("Teilhaberschaft löschen");
	Button addConToList = new Button("Hinzufügen");

	Label isShared = new Label("Geteilt mit:");
	Label labelShare = new Label("Teilen mit:");
	Label contactListStatus = new Label("Status:");
	Label contactListStatusValue = new Label("");
	Label labelSharedWith = new Label("Geteilt mit:");
	Label labelReceivedFrom = new Label("Eigentümer:");
	Label labelAddConsToList = new Label("Kontakt hinzufügen:");
	Label clOwner = new Label();

	ListBox listBoxShareWith = new ListBox();
	ListBox listBoxSharedWith = new ListBox();
	TextBox nameContactList = new TextBox();
	ListBox contactNames = new ListBox();
	ListBox contactsToAdd = new ListBox();
	TextBox email = new TextBox();
	

	/**
	 * Wenn update = True, ContactListForm neu anlegen wenn update = false
	 */

	boolean update = true;

	/**
	 * Aufbau des Kontaktlisten Formulars
	 */

	public ContactListForm() {

		/**
		 * Grid auf das die Widgets für das Kontaktformular aufgesetzt werden
		 */

		Grid contactListGrid = new Grid(6, 3);
		this.add(contactListGrid);
		this.add(btnPanel);
		
		contactListGrid.setWidget(0, 0, contactListLabel);
		contactListGrid.setWidget(0, 1, nameContactList);

		contactListGrid.setWidget(1, 0, contactListStatus);
		contactListGrid.setWidget(1, 1, contactListStatusValue);

		contactListGrid.setWidget(2, 0, contactLabel);
		contactListGrid.setWidget(2, 1, contactNames);
		contactListGrid.setWidget(2, 2, deleteConButton);

		contactListGrid.setWidget(3, 0, labelAddConsToList);
		contactListGrid.setWidget(3, 1, contactsToAdd);
		contactListGrid.setWidget(3, 2, addConToList);

		contactListGrid.setWidget(4, 0, labelSharedWith);
		contactListGrid.setWidget(4, 1, listBoxSharedWith);
		contactListGrid.setWidget(4, 2, unShareButton);

		contactListGrid.setWidget(5, 0, labelReceivedFrom);
		contactListGrid.setWidget(5, 1, clOwner);

		email.getElement().setPropertyString("placeholder", "Email");
		nameContactList.getElement().setPropertyString("placeholder", "Listenbezeichnung");
		contactNames.getElement().setPropertyString("placeholder", "Keine Kontakte");

		/**
		 * Panels mit Widgets zum Teilen
		 */

		VerticalPanel shareDialogvp = new VerticalPanel();
		HorizontalPanel shareDialoghp = new HorizontalPanel();
		shareDialogvp.add(labelShare);
		shareDialogvp.add(listBoxShareWith);
		shareDialogvp.add(shareDialoghp);
		shareDialoghp.add(okButton);
		shareDialoghp.add(cancelShareButton);
		shareDialog.add(shareDialogvp);
		shareDialog.center();
		shareDialog.setVisible(false);
		this.add(shareDialog);
	
		/*
		 * CSS
		 */
		contactListStatus.setStyleName("Status"); //Status der Kontaktliste
		nameContactList.setStyleName("ListBox");
		nameContactList.setStyleName("TextBox");
		contactsToAdd.setStyleName("ListBox");
		contactNames.setStyleName("ListBox");
		listBoxShareWith.setStyleName("ListBox");
		listBoxShareWith.getElement().getStyle().setColor("white");
		/*
		 * Css für die Labels
		 */
		
		contactListStatus.getElement().setId("Label");
		contactListStatusValue.setStyleName("Label");
		contactLabel.setStyleName("Label");
		labelAddConsToList.setStyleName("Label");
		labelSharedWith.setStyleName("Label");
		labelReceivedFrom.setStyleName("Label");
		
		/**
		 * Panel für Anordnung der Buttons
		 */

		btnPanel.add(saveButton);
		btnPanel.add(deleteClButton);
		btnPanel.add(shareButton);
		btnPanel.add(cancelNewButton);
		saveButton.addClickHandler(new saveAndUpdateClickHandler());
		deleteClButton.addClickHandler(new deleteContactListClickHandler());
		deleteConButton.addClickHandler(new deleteConFromListClickHandler());
		addConToList.addClickHandler(new addContactToClClickHandler());
		okButton.addClickHandler(new shareClickHandler());
		shareButton.addClickHandler(new shareClickHandler());
		cancelShareButton.addClickHandler(new cancelClickHandler());
		cancelNewButton.addClickHandler(new cancelClickHandler());
		unShareButton.addClickHandler(new unShareClickHandler());
		

		/*
		 * CSS Zuweisungen für Widgets
		 */

		clOwner.getElement().setId("contactlabel");

		// Labels in CSS

		contactListLabel.setStyleName("ueberschriftlabel");
		contactLabel.getElement().setId("namelabel");

		// Anzeige des Labels für Vorname und Nachname gleicher StyleName

		contactLabel.getElement().setId("contactlabel");


		contactNames.setStyleName("ListBox");
		listBoxShareWith.setStyleName("ListBox");
		listBoxSharedWith.setStyleName("ListBox");
		contactsToAdd.setStyleName("ListBox");

		labelShare.getElement().setId("teilenlabel");
		contactListStatus.setStyleName("label");

		// Buttons in CSS
		// delete + share + save-Buttons müssen jeweils auch gleich sein

		addConToList.setStyleName("sideButton");
		unShareButton.setStyleName("sideButton");
		deleteConButton.setStyleName("sideButton");
		
		
		// Style der Hauptbuttons
		saveButton.setStyleName("mainButton");
		shareButton.setStyleName("mainButton");
		btnPanel.setStyleName("mainButtonPanel");
		deleteClButton.setStyleName("mainButton");
		cancelNewButton.setStyleName("mainButton");
		email.setStyleName("Status");
	
		deleteConButton.setStyleName("sideButton");
		
		

		shareDialog.setStyleName("shareDialog");

	}

	/**
	 * ClickHandler zum Speichern und Updaten der KontaktListe Prüfung ob
	 * Speicherung oder Aktualisierung in App Logik
	 * 
	 * @author Kim-Ly
	 */

	private class saveAndUpdateClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {

			contactListToDisplay.setOwner(myUser);

			if (contactListToDisplay == null) {
				Window.alert("Keine Kontaktliste ausgewählt");

			} else if(nameContactList.getText().isEmpty()) { // Namensfeld der Liste darf nicht leer sein
				Window.alert("Das Namensfeld darf nicht leer gelassen werden.");
				nameContactList.setText(contactListToDisplay.getName());
			}else{
				log("Save/Update");
				String newClName = nameContactList.getText();
				contactListToDisplay.setName(newClName);

				if (update) {

					contactSystemAdmin.editContactList(contactListToDisplay, new UpdateCallback());

				} else {

					contactSystemAdmin.createContactList(contactListToDisplay, new SaveCallback());
				}
			}
		}
	}

	/**
	 * Callback Klasse zum Speichern der neu angelegten Kontaktliste
	 * 
	 * @author Kim-Ly
	 */

	private class SaveCallback implements AsyncCallback<ContactList> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Anlegen einer neuen KontaktListe ist fehlgeschlagen!");

		}

		@Override
		public void onSuccess(ContactList cl) {
			if (cl != null) {
				tvm.addToRoot(cl);
				tvm.setSelectedContactContactlist(cl);
				ContactSystem.triggerNotify("Neue Kontaktliste gespeichert!");
			}

		}

	}

	/**
	 * Callback Klasse zum Updaten einer überarbeiteten Kontaktliste im Treeview,
	 * neues Setzen des Kontaktlistformular Objekts
	 * 
	 * @author Kim-Ly
	 */

	private class UpdateCallback implements AsyncCallback<ContactList> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Aktualisieren der KontaktListe fehlgeschlagen!");
			// log("Aktualisierte Kontaktliste nicht angelegt" + cl.toString());

		}

		@Override
		public void onSuccess(ContactList cl) {
			if (cl != null) {
				tvm.updateBO(cl);
				setSelected(cl);
				ContactSystem.triggerNotify("Kontaktliste gespeichert!");
			}
		}
	}

	/**
	 * ClickHandler zum Teilen der KontaktListe mit dem ausgewählten User
	 * 
	 * @author Kim-Ly
	 */

	private class shareClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {

			if (contactListToDisplay == null) {
				Window.alert("Kontaktliste auswählen");

			} else {
				if (!shareDialog.isVisible()) {
					shareDialog.setVisible(true);
				} else {
					loadPanel.setVisible(true);
					
					contactSystemAdmin.getUserBygMail(listBoxShareWith.getSelectedValue(), new AsyncCallback<User>() {
						@Override
						public void onFailure(Throwable caught) {
							loadPanel.setVisible(false);
							Window.alert("User wurde nicht gefunden");
						}

						@Override
						public void onSuccess(User result) {
							log("User: " + result);
							Participation p = new Participation();
							p.setParticipant(result);
							p.setReference(contactListToDisplay);
							contactSystemAdmin.createParticipation(p, new shareCallback());
							email.setText("");
							shareDialog.setVisible(false);
						}
					});

				}

			}
		}
	}
	

	/**
	 * ClickHandler um den ShareDialog zu schließen
	 * 
	 * @author Oli
	 */

	private class cancelClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			
			if (contactListToDisplay == null) {
				Window.alert("Kontaktliste auswählen");

			} else {
				if(shareDialog.isVisible()){
					shareDialog.setVisible(false);
				}else{
					tvm.restSelection();
					RootPanel.get("Details").clear();
				}

			}
		}
	}

	/**
	 * ClickHandler zum Löschen der Teilhaberschaft zu einer KontaktListe Weitergabe
	 * an unShareCallback
	 * 
	 * @author Kim-Ly
	 */

	private class unShareClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			if (contactListToDisplay == null) {
				Window.alert("Kontaktliste auswählen");

			} else {
				loadPanel.setVisible(true);
				contactSystemAdmin.getUserBygMail(listBoxSharedWith.getSelectedItemText(), new AsyncCallback<User>() {
					@Override
					public void onFailure(Throwable caught) {
						loadPanel.setVisible(false);
						Window.alert("User wurde nicht gefunden");
					}

					@Override
					public void onSuccess(User result) {
						log("User: " + result);
						Participation p = new Participation();
						p.setParticipant(result);
						p.setReference(contactListToDisplay);
						contactSystemAdmin.deleteParticipation(p, new unShareCallback());

					}
				});

			}
		}
	}

	/**
	 * Callback Methode für das Teilen von Kontaktliste
	 * 
	 * @author Kim-Ly
	 */

	private class shareCallback implements AsyncCallback<Participation> {

		@Override
		public void onFailure(Throwable caught) {
			loadPanel.setVisible(false);
			ContactSystem.triggerNotify("Teilen fehlgeschlagen ");
		}

		// @Override
		public void onSuccess(Participation result) {
			ContactSystem.triggerNotify("Die Liste wurde geteilt");
			loadPanel.setVisible(false);
			labelSharedWith.setVisible(true);
			listBoxSharedWith.setVisible(true);
			unShareButton.setVisible(true);
			User userSharedWith = result.getParticipant();
			listBoxSharedWith.addItem(userSharedWith.getGMail());

		}
	}

	/**
	 * Callback Klasse für das Löschen der Teilhaberschaft zu einer Kontaktliste
	 * 
	 * @author Kim-Ly
	 */

	private class unShareCallback implements AsyncCallback<Participation> {

		@Override
		public void onFailure(Throwable caught) {
			loadPanel.setVisible(false);
			ContactSystem.triggerNotify("Teilhaberschaft löschen ist fehlgeschlagen: ");
		}

		@Override
		public void onSuccess(Participation result) {
			loadPanel.setVisible(false);
			ContactSystem.triggerNotify("Teilhaberschaft gelöscht");

			listBoxSharedWith.removeItem(listBoxSharedWith.getSelectedIndex());

		}
	}

	/**
	 * Clickhandler zum Löschen eines Kontakts aus der Kontaktliste
	 * 
	 * @author Kim-Ly
	 */

	private class deleteConFromListClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			log("Event: "+event);
			if (contactListToDisplay == null) {
				Window.alert("Keine Kontaktliste ausgewählt");

			} else {
				Vector<Contact> conVec = new Vector<Contact>();

				int contactID = Integer.parseInt(contactNames.getSelectedValue());

				// Suche und Rückgabe nach Kontakt aus Kontaktliste, der mit ausgewählten
				// Kontakt aus ListBox übereinstimmt
				for (Contact con : contactListToDisplay.getContacts()) {
					if (con.getBoId() == contactID) {

						Contact conToDelete = con;
						// Der Callback kann nur bei bestehenden listen durchgeführt werden.
						if(update){
							contactSystemAdmin.removeContactFromList(conToDelete, contactListToDisplay,
									new deleteConfromListCallback());
							tvm.removeFromLeef(contactListToDisplay.getBoId(), con);
						}else{
							// Bei neuen Listen wird der Kontakt aus dem Vector entfernt;
							contactListToDisplay.getContacts().remove(conToDelete);
							contactNames.removeItem(contactNames.getSelectedIndex());
							
						}
					}
				}
			}
		}
	}

	/**
	 * Callback Klasse zum Löschen eines Kontakts aus der Kontaktliste
	 * Aktualisierung des Kontaktlisten Objekts
	 * 
	 * @author Kim-Ly
	 */

	private class deleteConfromListCallback implements AsyncCallback<ContactList> {

		public void onFailure(Throwable caught) {
			ContactSystem.triggerNotify("Entfernen des Kontakts fehlgeschlagen");
		}

		public void onSuccess(ContactList result) {
			if (result != null) {
				setSelected(result);
				ContactSystem.triggerNotify("Kontakt wurde aus der Liste entfernt");
			}
		}
	}

	/**
	 * Clickhandler zum Loeschen einer Kontaktliste
	 * 
	 * @author Kim-Ly
	 */

	private class deleteContactListClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			if (contactListToDisplay == null) {
				Window.alert("Keine Kontaktliste ausgewählt");

			} else {
				if(Window.confirm("Wollen Sie die Liste " + contactListToDisplay.getName() + " wirklich löschen?")){
					contactSystemAdmin.deleteContactList(contactListToDisplay, new deleteContactListCallback());
				}
			}
		}
	}

	/**
	 * Callback Klasse zum Löschen der ausgewählten Kontaktliste aus TreeView durch
	 * Auflösung der Teilhaberschaft, Setzen des Kontaktlistenobjekts auf null
	 * 
	 * @author Kim-Ly
	 */
	
	private class deleteContactListPartCallback implements AsyncCallback<Participation> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Löschen der Kontaktliste Teilhaberschaft fehlgeschlagen!");
		}

		@Override
		public void onSuccess(Participation p) {

			ContactList clPart = (ContactList) p.getReferencedObject();
			// log(clPart.toString());

			if (clPart != null) {
				// log(clPart.toString());
				setSelected(null);
				tvm.removeBO(clPart);
				Window.alert("Teilhaberschaft der Kontaktliste gelöscht!");
			}
		}
	}

	/**
	 * Callback Klasse zum Löschen einer Kontaktliste aus TreeView, dem RootPanel und 
	 * setzen des Kontaktlistenobjekts auf null
	 * 
	 * @author Kim-Ly
	 *
	 */

	private class deleteContactListCallback implements AsyncCallback<ContactList> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Löschen der Kontaktliste fehlgeschlagen!");
		}

		@Override
		public void onSuccess(ContactList result) {
			if (result != null) {
				setSelected(null);
				RootPanel.get("Details").clear();
				tvm.removeBO(result);
				ContactSystem.triggerNotify("Kontaktliste gelöscht!");
			}
		}
	}


	/**
	 * Callback Klasse zum Befüllen der Listbox mit allen Usern aus dem System. Mit
	 * diesen kann die Kontaktliste geteilt werden.
	 * 
	 * @author Kim-Ly
	 */

	private class UserToShareCallback implements AsyncCallback<Vector<User>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Der Aufruf der Nutzer ist misglückt! ");
		}

		@Override
		public void onSuccess(Vector<User> result) {

			// Leeren der bisherigen Listbox mit
			listBoxShareWith.clear();
			if (result != null) {
				for (User user : result) {

					// User Liste updaten
					listBoxShareWith.addItem(user.getUserContact().getName().getValue() + " / " + user.getGMail(), user.getGMail());
				}

			} else {
				Window.alert("Kein Nutzer gefunden");
			}
		}
	}

	/**
	 * Callback Klasse zum Befüllen der Listbox mit Usern an die die Kontaktliste
	 * geteilt wurde.
	 * 
	 * @author Kim-Ly
	 */

	private class UserSharedWithCallback implements AsyncCallback<Vector<Participation>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Der Aufruf der Nutzer ist misglückt! ");
		}

		@Override
		public void onSuccess(Vector<Participation> result) {

			// Listbox mit Usern an die geteilt wurde leeren
			listBoxSharedWith.clear();
			if (result != null) {
				if(result.size()>0){
					labelSharedWith.setVisible(true);
					listBoxSharedWith.setVisible(true);
					unShareButton.setVisible(true);
					for (Participation part : result) {
						// User Liste updaten
						listBoxSharedWith.addItem(part.getParticipant().getGMail());
					}
				}
			} else {
				Window.alert("Kontaktliste ist mit keinem Nutzer geteilt!");
			}
		}
	}

	/**
	 * Callback Klasse zur Anzeige des Users der der Eigentümer der Kontaktliste ist.
	 * 
	 * @author Kim-Ly
	 */

	private class UserOwnerClCallback implements AsyncCallback<Vector<ContactList>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Eigentümer der Kontaktliste konnte nicht gefunden werden");
		}

		@Override
		public void onSuccess(Vector<ContactList> clVec) {
			if (clVec != null) {

				clOwner.setText(contactListToDisplay.getOwner().getGMail());

			} else {
				Window.alert("Eigentümer der Kontaktliste konnte nicht gefunden werden");
			}
		}
	}

	/**
	 * ClickHandler zum Hinzufügen eines Kontakts zur Kontaktliste, Auswahl aus allen Kontakten
	 * die dem User geteilt und vom User erstellt wurden
	 * 
	 * @author Kim-Ly
	 */

	private class addContactToClClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			if (contactListToDisplay == null) {
				Window.alert("Keine Kontaktliste ausgewählt");

			} else {

				// Extraktion des ausgewählten Kontakts als String
				
				final int contactToAddID = Integer.parseInt(contactsToAdd.getSelectedValue());
				log("Add new Contact");
				
				contactSystemAdmin.getMyContactsPrev(new AsyncCallback<Vector<Contact>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("");
					}

					@Override
					public void onSuccess(Vector<Contact> allConFromUser) {

						for (Contact conFromUser : allConFromUser) {
							// Wenn ausgewählter Contact aus Listbox mit einem der Contacte des Users übereinstimmt
							if (conFromUser.getBoId() == contactToAddID) {
								
								if (contactListToDisplay.getContacts().size() > 0) { // Eine leere Liste muss nicht
									// überprüft werden und hinzuzufügender Kontakt nicht bereits als Kontakt in Kontaktlist vorhanden
									if(contactListToDisplay.getContacts().contains(conFromUser)){		
										ContactSystem.triggerNotify("Kontakt bereits in Kontaktliste vorhanden");
									} else {
										addContact(conFromUser);
										break;
									}
									
								} else {
									addContact(conFromUser);
									break;
								}
							}
						}
					}
				});
			}
		}
	}
	
	/**
	 * Fügt den Kontakt einer neuen Liste direkt, oder einer existierende liste über einen Callback hinzu.
	 * @param Contact to add
	 */
	private void addContact(Contact c){
		if(update){
			// Bei bestehende listen wird der Kontakt direkt übe reinen Callback der Liste hinzugefügt.
			contactSystemAdmin.addContactToList(c, contactListToDisplay,
					new ContactsToAddClCallback());
			tvm.addToLeef(contactListToDisplay.getBoId(), c);
		}else{
			// Neuen KontaktListen kann der Kontakt noch ncitn über einen Callback hinzugefügt wrden, da die liste in der Datenbank noch nicht existiert.
			// Der Kontakt wird bei der erstellung der Liste aus dem Vector im Listen Objekt ausgelesen und zur liste hinzugefügt.
			ContactSystem.triggerNotify("Kontakt hinzugefügt");
			contactNames.addItem(c.getName().getValue(), c.getBoId()+"");
			contactListToDisplay.addContact(c);
			
		}
		
	}
	

	/**
	 * Callback Klasse für aktualisiertes Kontaktlistenobjekt nachdem neuer Kontakt
	 * hinzugefügt wurde, Befüllung der Listbox mit Kontakten die der User erstellt hat oder die ihm
	 * geteilt wurden
	 * 
	 * @author Kim-Ly
	 */

	private class ContactsToAddClCallback implements AsyncCallback<ContactList> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Keine Kontaktliste vorhanden");
		}

		@Override
		public void onSuccess(ContactList result) {			
			if (result != null) {
				contactListToDisplay = result;
				contactNames.clear();
				Vector<Contact> resultCon = result.getContacts();
				for (Contact con : resultCon) {
					contactNames.addItem(con.getName().getValue(), con.getBoId()+"");
				}
				ContactSystem.triggerNotify("Kontakt hinzugefügt");
			} 
		}

	}

	/**
	 * Callback Klasse für alle vom User erstellten und dem User geteilten Kontakte,
	 * um einen daraus selektierten Kontakt zur Kontaktliste hinzuzufügen. Befüllung
	 * der Listbox mit Kontakten die zu Kontaktliste hinzugefügt werden können
	 * 
	 * @author Kim-Ly
	 */

	private class ContactsToAddConCallback implements AsyncCallback<Vector<Contact>> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Keine Kontakte im System vorhanden.");

		}

		@Override
		public void onSuccess(Vector<Contact> result) {

			// Leeren der Kontakte die hinzugefügt werden können
			contactsToAdd.clear();
			if (result != null) {
				for (Contact con : result) {
					contactsToAdd.addItem(con.getName().getValue(), con.getBoId()+"");

				}
			} else {
				Window.alert("Kontaktliste ist mit keinem Nutzer geteilt!");
			}
		}
	}

	/**
	 * Setzen des Kontaktlisten Objekts, aller zugehörigen Kontakte aus TreeView und
	 * Anzeige aller im System vorhandenen User
	 * 
	 * @author Kim-Ly
	 */
	
	void setSelected(ContactList cl) {

		// Listen und Textfelder  leeren
		contactNames.clear();
		contactsToAdd.clear();
		listBoxSharedWith.clear();
		nameContactList.setText("");
		email.setText("");
		clOwner.setText("");
		
		// Blendet Share Dialog aus falls er von einem vorherige formular noch eingeblendet war
		shareDialog.setVisible(false);
		cancelNewButton.setVisible(false);
		// Blendet eventuell ausgeblendete Gui elemente wieder ein
		clOwner.setVisible(true);
		shareButton.setVisible(true);
		deleteClButton.setVisible(true);
		labelReceivedFrom.setVisible(true);
		labelSharedWith.setVisible(false);
		listBoxSharedWith.setVisible(false);
		unShareButton.setVisible(false);

		if (cl != null) {
			update = true;
			
			/*
			 * Callback für Kontakte der Kontaktliste, da TreeView nur bo_ID &
			 * Kontaktlistenname weitergibt
			 */
			
			contactSystemAdmin.getContactListById(cl.getBoId(), new AsyncCallback<ContactList>() {

				@Override
				public void onFailure(Throwable caught) {
					log("Listekonnte nicht geladen werden");

				}

				@Override
				public void onSuccess(ContactList list) {
					log("Liste geladen");
					contactListToDisplay = list;
	

					/**
					 * Abfrage für die Status Anzeige "Nicht geteilt", "Von mir geteilt" oder "An mich geteilt"
					 * der Kontaktliste
					 * 
					 * @author Kim-Ly
					 */

					if (contactListToDisplay.getShared_status()) {
						if(contactListToDisplay.getOwner().getGoogleID() == myUser.getGoogleID()){
							contactListStatusValue.setText("Von mir geteilt");
						}else{
							contactListStatusValue.setText("An mich geteilt");
						}	
					} else {
						contactListStatusValue.setText("Nicht geteilt");
					}
					//Status
					contactListStatusValue.setStyleName("Status");
					
					int count = 0;
					for (Contact con : list.getContacts()) {
						contactNames.addItem(con.getName().getValue(), con.getBoId()+"");

					}
	
					/* Alle User mit denen Kontaktliste geteilt wurde 
					 * -> Nur Callback abrufen wenn User Besitzer der Kontaktliste, sonst Listbox + Label unsichtbar
					 */
					
					if (contactListToDisplay.getOwner().getGoogleID() == myUser.getGoogleID()) {
						contactSystemAdmin.getAllParticipationsByBusinessObject(contactListToDisplay,
								new UserSharedWithCallback());
					} else {
						labelSharedWith.setVisible(false);
						listBoxSharedWith.setVisible(false);
					}

					/*
					 * Eigentümer der Kontaktliste anzeigen
					 */
					if (contactListToDisplay != null) {
						clOwner.setText(contactListToDisplay.getOwner().getGMail());
					}

					deleteClButton.setEnabled(true);
					deleteConButton.setEnabled(true);
					nameContactList.setText(contactListToDisplay.getName());
				}

			});
			
			// Laden der User mit dem Die Liste geteilt werden kann.
			contactSystemAdmin.getAllUsers(new UserToShareCallback());

			

		} else {
			update = false;
			contactListToDisplay = new ContactList();
			nameContactList.setText("");
			cancelNewButton.setVisible(true);
			
			// Blendet GUI eletemnt wie Teilen, Geteilt mit und EIgentümer aus
			clOwner.setVisible(false);
			shareButton.setVisible(false);
			labelSharedWith.setVisible(false);
			listBoxShareWith.setVisible(false);
			deleteClButton.setVisible(false);
			labelReceivedFrom.setVisible(false);
			unShareButton.setVisible(false);
			listBoxSharedWith.setVisible(false);
			
		}
		/*
		 * Alle Kontakte die der User angelegt hat oder die ihm geteilt wurden werden angezeigt, um sie einer Liste hinzuzufügen
		 */

		contactSystemAdmin.getMyContactsPrev(new ContactsToAddConCallback());

	}
	
	/*
	 * LoadPanel setter
	 */
	void setLoad(PopupPanel load) {
		this.loadPanel = load;
	}
	
	void setTree(CellTreeViewModel tvm) {
		this.tvm = tvm;
	}

	void setMyUser(User user) {

		this.myUser = user;
	}

	native void log(String s)/*-{
								console.log(s);
								}-*/;

}
