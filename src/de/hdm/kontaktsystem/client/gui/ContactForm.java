package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

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

import de.hdm.kontaktsystem.client.gui.MyParticipationForm.deleteParticipationCallback;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
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
	Contact contactToDisplay = null;
	ContactsTreeViewModel ctvm = null;
	
	/**
	 * Instanziieren der Widgets
	 */
	private TextBox textBoxName = new TextBox();
	private TextBox textBoxNickName = new TextBox();
	private TextBox textBoxFirma = new TextBox();
	//private TextBox textBoxTitel = new TextBox();
	private TextBox textBoxTelefonnummer = new TextBox();
	private TextBox textBoxMobilnummer = new TextBox();
	private TextBox textBoxEmail = new TextBox();
	private TextBox textBoxGeburtsdatum = new TextBox();
	private TextBox textBoxAdresse = new TextBox();
	
	private Label label = new Label("Kontakt:");
	private Label labelName = new Label("Name:");
	private Label labelNickName = new Label("Nick-Name:");
	private Label labelFirma = new Label("Firma:");
	//private Label labelTitel = new Label("Titel:");
	private Label labelTeleNr = new Label("Telefonnummer:");
	private Label labelMobilNr = new Label("Mobilnummer:");
	private Label labelEmail = new Label("Email:");
	private Label labelGeburtsdatum = new Label("Geburtsdatum:");
	private Label labelAdresse = new Label("Adresse:");
	
	private Button deleteButton = new Button("Kontakt löschen");
	private Button shareButton = new Button("Kontakt teilen");
	private Button editButton = new Button("Kontakt bearbeiten");
	private Button saveButton = new Button("Kontakt bearbeiten");
	private Button cancelButton = new Button("Kontakt bearbeiten");
	
	/**
	 * Instanziieren der Panels
	 */
	
	//Hauptpanel
	private VerticalPanel vp = new VerticalPanel();
	
	//Panel um die Buttons nebeneinander anzuordnen
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	
	
	
	public void  onLoad() {
		super.onLoad();
		
		Grid contactGrid = new Grid(14, 2);
		
		contactGrid.setWidget(0, 0, label);
		contactGrid.setWidget(0, 1, labelName);

		contactGrid.setWidget(1, 0, textBoxName);
		contactGrid.setWidget(1, 1, labelNickName);
		
		contactGrid.setWidget(2, 0, textBoxNickName);
		contactGrid.setWidget(2, 1, textBoxName);
		
		contactGrid.setWidget(3, 0, labelFirma);
		contactGrid.setWidget(3, 1, textBoxFirma);
		
		contactGrid.setWidget(4, 0, labelTeleNr);
		contactGrid.setWidget(4, 1, textBoxTelefonnummer);
		
		contactGrid.setWidget(5, 0, labelMobilNr);
		contactGrid.setWidget(5, 1, textBoxMobilnummer);
		
		contactGrid.setWidget(6, 0, labelEmail);
		contactGrid.setWidget(6, 1, textBoxEmail);
		
		contactGrid.setWidget(7, 0, labelGeburtsdatum);
		contactGrid.setWidget(7, 1, textBoxGeburtsdatum);
		
		contactGrid.setWidget(8, 0, labelAdresse);
		contactGrid.setWidget(8, 1, textBoxAdresse);
		
		/** 
		 * Click Handler Button zum editieren von Inhalten
		 * des Kontaktformulars.
		 */
		
		editButton.addClickHandler(new EditClickHandler());
		editButton.setEnabled(false);
		buttonPanel.add(editButton);
		
		/**
		 * Click Handler Button zum teilen von Kontakten.
		 * Nach der Auswahl "editieren" wird der Button dynamisch 
		 * ersetzt durch einen Save Button
		 * TODO: Prüfen
		 */
		
		shareButton.addClickHandler(new ShareClickHandler());
		shareButton.setEnabled(false);
		buttonPanel.add(shareButton);		
		
				
		/**
		 * Click Handler Button zum löschen von Kontakten.
		 * Nach der Auswahl "editieren" wird der Button dynamisch 
		 * ersetzt durch einen Cancel Button
		 * TODO: Prüfen	
		 */
		
		deleteButton.addClickHandler(new DeleteClickHandler());
		deleteButton.setEnabled(false);
		buttonPanel.add(deleteButton);
		
		/**
		 * Click Handler Button zum speichern von Kontakten. 
		 */
		
		saveButton.addClickHandler(new SaveClickHandler());
		saveButton.setEnabled(false);
		buttonPanel.add(saveButton);
		
		/**
		 * Click Handler Button zum abbrechen von Kontakten.
		 */
		cancelButton.addClickHandler(new CancelClickHandler());
		cancelButton.setEnabled(false);
		buttonPanel.add(cancelButton);		
		
		
		/**
		 * Die TextBox um den Kontakt einzugeben, das Label und das Panel für die Buttons werden 
		 * über das Grid Widget untereinander auf dem VerticalPanel angeordnet.
		 * 
		 * Ebenso werden alle Buttons auf dem HorizontalPanel für eine Button Leiste angeordnet. 
		 */
		
		vp.add(label);
		vp.add(contactGrid);
		vp.add(buttonPanel);
		
		/**
		 * Die Änderung eines Kontos bezieht sich auf seinen Vor- und/oder
		 * Nachnamen. Es erfolgt der Aufruf der Service-Methode "save".
		 * 
		 */
		
		private class EditClickHandler implements ClickHandler {
			@Override
			public void onClick(ClickEvent event) {
				if (customerToDisplay != null) {
					Vector <PropertyValue> pv = new Vector <PropertyValue>();
					pv = contactToDisplay.getPropertyValues();	
					
					for(PropertyValue p : pv) {
						//if(p.getProperty().getId() == 1) label.setText("Kontakt: " + ...);
						if(p.getProperty().getId() == 1) textBoxName.setText(p.getValue()); 
						if(p.getProperty().getId() == 2) textBoxNickName.setText(p.getValue());
						if(p.getProperty().getId() == 3) textBoxFirma.setText(p.getValue());
						if(p.getProperty().getId() == 4) textBoxTelefonnummer.setText(p.getValue());
						if(p.getProperty().getId() == 5) textBoxMobilnummer.setText(p.getValue());
						if(p.getProperty().getId() == 6) textBoxEmail.setText(p.getValue());
						if(p.getProperty().getId() == 7) textBoxGeburtsdatum.setText(p.getValue());
						if(p.getProperty().getId() == 8) textBoxAdresse.setText(p.getValue());				
						}
					contactSystemVerwaltung.createContact(contactToDisplay, new EditCallback());
				} else {
					Window.alert("kein Bearbeitungsfeld ausgewählt");
				}
			}
		}
		
		/*
		 * Die Änderungen an einem Kontakt erfolgen über den 
		 * Aufruf der Service-Methode "update"
		 */

		private class EditCallback implements AsyncCallback<Void> {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Die Änderungen konnten nicht gespeichert werden!");
			}

			@Override
			public void onSuccess(Void result) {
				// Die Änderung wird zum Kunden- und Kontenbaum propagiert.
				ctvm.updateContat(contactToDisplay);
			}
		}
		
		
		/**
		 * Die Änderung eines Kunden bezieht sich auf seinen Vor- und/oder
		 * Nachnamen. Es erfolgt der Aufruf der Service-Methode "save".
		 * 
		 */
		
		// TODO: private
		class SaveClickHandler implements ClickHandler {
			@Override
			public void onClick(ClickEvent event) {
				if (contactToDisplay != null) {					
					Vector <PropertyValue> pv = new Vector <PropertyValue>();
					pv = contactToDisplay.getPropertyValues();
									
					for(PropertyValue p : pv) {
						
						//if(p.getProperty().getId() == 1) label.setText("Kontakt: " + ...);
						if(p.getProperty().getId() == 1) textBoxName.setText(p.getValue()); 
						if(p.getProperty().getId() == 2) textBoxNickName.setText(p.getValue());
						if(p.getProperty().getId() == 3) textBoxFirma.setText(p.getValue());
						if(p.getProperty().getId() == 4) textBoxTelefonnummer.setText(p.getValue());
						if(p.getProperty().getId() == 5) textBoxMobilnummer.setText(p.getValue());
						if(p.getProperty().getId() == 6) textBoxEmail.setText(p.getValue());
						if(p.getProperty().getId() == 7) textBoxGeburtsdatum.setText(p.getValue());
						if(p.getProperty().getId() == 8) textBoxAdresse.setText(p.getValue());				
						}
					
					contactSystemVerwaltung.save(contactToDisplay, new SaveCallback());
				} else {
					Window.alert("kein Kontakt ausgewählt");
				}
			}
		}
		
		
		/*
		 * Die Änderungen an einem Kontakt erfolgen über den 
		 * Aufruf der Service-Methode "update"
		 */

		class SaveCallback implements AsyncCallback<Void> {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Die Änderungen konnten nicht gespeichert werden!");
			}

			@Override
			public void onSuccess(Void result) {
				// Die Änderung wird zum Kunden- und Kontenbaum propagiert.
				ctvm.updateContat(contactToDisplay);
			}
		}
		
		/*
		 * Die Löschung eines Kontaktes über die Eingabefelder erfolgt über den 
		 * Aufruf der Service-Methode "update"
		 */
			class DeleteContactCallback implements AsyncCallback<Void> {

				Contact contact = null;
				public void deleteContactCallback(Contact c) {
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
			
			/**
			 * Die Änderung eines Kunden bezieht sich auf seinen Vor- und/oder
			 * Nachnamen. Es erfolgt der Aufruf der Service-Methode "remove".
			 * 
			 */
			class DeleteClickHandler implements ClickHandler {
				@Override
				public void onClick(ClickEvent event) {
					if (contactToDisplay != null) {
						Vector <PropertyValue> pv = new Vector <PropertyValue>();
						pv = contactToDisplay.getPropertyValues();
						
						for(PropertyValue p : pv) {
						
						//if(p.getProperty().getId() == 1) label.setText("Kontakt: " + ...);
						if(p.getProperty().getId() == 1) textBoxName.setText(p.getValue()); 
						if(p.getProperty().getId() == 2) textBoxNickName.setText(p.getValue());
						if(p.getProperty().getId() == 3) textBoxFirma.setText(p.getValue());
						if(p.getProperty().getId() == 4) textBoxTelefonnummer.setText(p.getValue());
						if(p.getProperty().getId() == 5) textBoxMobilnummer.setText(p.getValue());
						if(p.getProperty().getId() == 6) textBoxEmail.setText(p.getValue());
						if(p.getProperty().getId() == 7) textBoxGeburtsdatum.setText(p.getValue());
						if(p.getProperty().getId() == 8) textBoxAdresse.setText(p.getValue());				
						}
						
						// TODO !!! 
						
						//String partMail = TextBoxShareTo.getText();
						//if(partMail != null) -> Finde das UserObjekt aus der DB zur zugehörigen Email
						
						contactSystemVerwaltung.shareContactWith(participant, new ShareCallback()); ;
					} else {
						Window.alert("kein Kunde ausgewählt");
					}
				}
			}
			
			/**
			 * Die Änderung eines Kunden bezieht sich auf seinen Vor- und/oder
			 * Nachnamen. Es erfolgt der Aufruf der Service-Methode "save".
			 * 
			 */
			class ShareClickHandler implements ClickHandler {
				@Override
				public void onClick(ClickEvent event) {
					if (contactToDisplay != null) {
						Vector <PropertyValue> pv = new Vector <PropertyValue>();
						pv = contactToDisplay.getPropertyValues();
						
						for(PropertyValue p : pv) {
						
						//if(p.getProperty().getId() == 1) label.setText("Kontakt: " + ...);
						if(p.getProperty().getId() == 1) textBoxName.setText(p.getValue()); 
						if(p.getProperty().getId() == 2) textBoxNickName.setText(p.getValue());
						if(p.getProperty().getId() == 3) textBoxFirma.setText(p.getValue());
						if(p.getProperty().getId() == 4) textBoxTelefonnummer.setText(p.getValue());
						if(p.getProperty().getId() == 5) textBoxMobilnummer.setText(p.getValue());
						if(p.getProperty().getId() == 6) textBoxEmail.setText(p.getValue());
						if(p.getProperty().getId() == 7) textBoxGeburtsdatum.setText(p.getValue());
						if(p.getProperty().getId() == 8) textBoxAdresse.setText(p.getValue());				
						}
						
						// TODO !!! 
						
						//String partMail = TextBoxShareTo.getText();
						//if(partMail != null) -> Finde das UserObjekt aus der DB zur zugehörigen Email
						
						contactSystemVerwaltung.shareContactWith(participant, new ShareCallback()); ;
					} else {
						Window.alert("kein Kunde ausgewählt");
					}
				}
			}
			
			/*
			 * Die Änderungen an einem Kontakt erfolgen über den 
			 * Aufruf der Service-Methode "update"
			 */

			class ShareCallback implements AsyncCallback<Void> {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Der Kontakt konnte nicht geteilt werden!");
				}

				@Override
				public void onSuccess(Void result) {
					// Die Änderung wird zum Kunden- und Kontenbaum propagiert.
					ctvm.updateContat(contactToDisplay);
				}
			}
			
			/**
			 * Die Änderung eines Kunden bezieht sich auf seinen Vor- und/oder
			 * Nachnamen. Es erfolgt der Aufruf der Service-Methode "save".
			 * 
			 */
			class CancelClickHandler implements ClickHandler {
				@Override
				public void onClick(ClickEvent event) {
					if (contactToDisplay != null) {
						Vector <PropertyValue> pv = new Vector <PropertyValue>();
						pv = contactToDisplay.getPropertyValues();
						
						for(PropertyValue p : pv) {
						
						//if(p.getProperty().getId() == 1) label.setText("Kontakt: " + ...);
						if(p.getProperty().getId() == 1) textBoxName.setText(p.getValue()); 
						if(p.getProperty().getId() == 2) textBoxNickName.setText(p.getValue());
						if(p.getProperty().getId() == 3) textBoxFirma.setText(p.getValue());
						if(p.getProperty().getId() == 4) textBoxTelefonnummer.setText(p.getValue());
						if(p.getProperty().getId() == 5) textBoxMobilnummer.setText(p.getValue());
						if(p.getProperty().getId() == 6) textBoxEmail.setText(p.getValue());
						if(p.getProperty().getId() == 7) textBoxGeburtsdatum.setText(p.getValue());
						if(p.getProperty().getId() == 8) textBoxAdresse.setText(p.getValue());				
						}
						//contactSystemVerwaltung.wait(); //TODO: Methode hierfür prüfen
						//-> remove cancelButton/saveButton and show editButton/deleteButton
					} else {
						Window.alert("kein Kunde ausgewählt");
					}
				}
			}
			
			/**
			 * 
			 */

			class CancelCallback implements AsyncCallback<Void> {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Der Kontakt konnte nicht geteilt werden!");
				}

				@Override
				public void onSuccess(Void result) {
					// Die Änderung wird zum Kunden- und Kontenbaum propagiert.
					ctvm.updateContat(contactToDisplay);
				}
			}
			
		
		/**
		 * Wenn der anzuzeigende Kontakt gesetzt bzw. gelöscht wird, werden die
		 * zugehörenden Textfelder mit den Informationen aus dem Kontaktobjekt
		 * gefüllt bzw. gelöscht.
		*/
		
		public void setSelected(Contact c) {
			
			if (c != null) {
				contactToDisplay = c;
				
				deleteButton.setEnabled(true);
				editButton.setEnabled(true);
				shareButton.setEnabled(true);
				
				Vector <PropertyValue> pv = new Vector <PropertyValue>();
				pv = contactToDisplay.getPropertyValues();
				
				for(PropertyValue p : pv) {
				
				//if(p.getProperty().getId() == 1) label.setText("Kontakt: " + ...);
				if(p.getProperty().getId() == 1) textBoxName.setText(p.getValue()); 
				if(p.getProperty().getId() == 2) textBoxNickName.setText(p.getValue());
				if(p.getProperty().getId() == 3) textBoxFirma.setText(p.getValue());
				if(p.getProperty().getId() == 4) textBoxTelefonnummer.setText(p.getValue());
				if(p.getProperty().getId() == 5) textBoxMobilnummer.setText(p.getValue());
				if(p.getProperty().getId() == 6) textBoxEmail.setText(p.getValue());
				if(p.getProperty().getId() == 7) textBoxGeburtsdatum.setText(p.getValue());
				if(p.getProperty().getId() == 8) textBoxAdresse.setText(p.getValue());				
				}
				
			} else {
				label.setText("Kontakt: ");
				textBoxName.setText("");
				textBoxNickName.setText("");
				textBoxFirma.setText("");
				textBoxTelefonnummer.setText("");
				textBoxMobilnummer.setText("");
				textBoxEmail.setText("");
				textBoxGeburtsdatum.setText("");
				textBoxAdresse.setText("");
				
				deleteButton.setEnabled(false);
				editButton.setEnabled(false);
				shareButton.setEnabled(false);
			}
		}
				
			/*
			 *  catvm setter
			 */
			
			void setCatvm(ContactsTreeViewModel ctvm) {
				this.ctvm = ctvm;
			}
			
			

}
	
}


