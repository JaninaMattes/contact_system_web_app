package de.hdm.kontaktsystem.client.gui;

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
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;


/**
 * Anzeige und Bearbeiten einer Teilhaberschaft
 * 
 * @author Sandra
 *
 */

public class MyParticipationForm extends VerticalPanel{
	
//	Zu Testzwecken ausgeblendet
//	ContactSystemAdministrationAsync contactSystemVerwaltung = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();
	
	Participation participationToDisplay = null;
	MyParticipationsTreeViewModel mptvm = null;
	
	/**
	 * Widgets, deren Inhalte variabel sind, werden als Attribute angelegt.
	 */
	TextBox nameParticipant = new TextBox();
	TextBox sharedObject = new TextBox();
	TextBox objectType = new TextBox();
	TextBox sharedDate = new TextBox();
	
	/**
	 * Beschriftung der Felder
	 */
	Label participantLabel = new Label("Geteilt mit: ");
	Label objectLabel = new Label("Geteiltes Objekt: ");
	Label typeLabel = new Label("Objekttyp: ");
	Label dateLabel = new Label("Zuletzt geändert am: ");
	
	/**
	 * Button zum Löschen der Participation
	 */
	Button deleteButton = new Button("Löschen");
	
	
	/**
	 * Beim Anzeigen werden die anderen Widgets erzeugt. Alle werden in
	 * einem Raster angeordnet, dessen Größe sich aus dem Platzbedarf
	 * der enthaltenen Widgets bestimmt.
	 */
	public void onLoad() {
		super.onLoad();
		
		Grid participationGrid = new Grid(5, 2);
		this.add(participationGrid);
		
		participationGrid.setWidget(0, 0, participantLabel);
		participationGrid.setWidget(0, 1, nameParticipant);

		participationGrid.setWidget(1, 0, objectLabel);
		participationGrid.setWidget(1, 1, sharedObject);
		
		participationGrid.setWidget(2, 0, typeLabel);
		participationGrid.setWidget(2, 1, objectType);
		
		participationGrid.setWidget(3, 0, dateLabel);
		participationGrid.setWidget(3, 1, sharedDate);

		deleteButton.addClickHandler(new DeleteClickHandler());
		deleteButton.setEnabled(false);
		participationGrid.setWidget(4, 1, deleteButton);
	}
	
	/**
	 * Click Handler zum Button "Löschen"
	 */
	private class DeleteClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			if (participationToDisplay == null) {
				Window.alert("kein Kunde ausgewählt");
			} else {
//				Zu Testzwecken ausgeblendet
//				contactSystemVerwaltung.delete(participationToDisplay,
//						new deleteParticipationCallback(participationToDisplay));
				
				//Testdaten
				Window.alert("Kunde gelöscht");
			}
		}
	}

	class deleteParticipationCallback implements AsyncCallback<Void> {

		Participation participation = null;

		deleteParticipationCallback(Participation p) {
			participation = p;
		}

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Das Löschen der Teilhaberschaft ist fehlgeschlagen!");
		}

		@Override
		public void onSuccess(Void result) {
			if (participation != null) {
				setSelected(null);
				mptvm.removeParticipation(participation);
			}
		}
	}

	// Setzen des TreeViewModels
	void setMptvm(MyParticipationsTreeViewModel mptvm) {
		this.mptvm = mptvm;
	}

	/**
	 * Wenn die anzuzeigende Teilhaberschaft gelöscht wird, werden die
	 * zugehörenden Textfelder gelöscht.
	*/
	void setSelected(Participation p) {
		if (p != null) {
			participationToDisplay = p;
			deleteButton.setEnabled(true);
//			Zu Testzwecken ausgeblendet
//			nameParticipant.setText(p.getParticipant().getContact().getpropertyValue().getName());
			//Testdaten
			nameParticipant.setText("MusterTeilhaber");
			
			if(p.getReferencedObject() instanceof Contact) {
		    	  Contact c = (Contact) p.getReferencedObject();
//		    	  Zu Testzwecken ausgeblendet
//		    	  sharedObject.setText(c.getpropertyValue().getName());
		    	//Testdaten
		    	  sharedObject.setText("MusterKontakt");
		    	  
		    	  objectType.setText("Kontakt");
		      }else if(p.getReferencedObject() instanceof ContactList) {
		    	  ContactList cl = (ContactList) p.getReferencedObject();
//		    	  Zu Testzwecken ausgeblendet
//		    	  sharedObject.setText(cl.getName());
		    	  //Testdaten
		    	  sharedObject.setText("MusterKontaktliste");
		    	  
		    	  objectType.setText("Kontaktliste");
		    	  
		    	  //Zugehörigen Kontakt angeben?
		      }else if(p.getReferencedObject() instanceof PropertyValue) {
		    	  PropertyValue pv = (PropertyValue) p.getReferencedObject();
//		    	  Zu Testzwecken ausgeblendet
//		    	  sharedObject.setText(pv.getValue());
		    	//Testdaten
		    	  sharedObject.setText("MusterEigenschaft");
		    	  
		    	  objectType.setText("Eigenschaftswert");
		      }
			
			//Zu Testzwecken ausgeblendet
//			sharedDate.setText(p.getReferencedObject().getModifyDate().toString());
			sharedDate.setText("01.01.2001");
			
		} else {
			nameParticipant.setText("");
			sharedObject.setText("");
			objectType.setText("");
			sharedDate.setText("");

			deleteButton.setEnabled(false);
		}
	}
	
	
}
