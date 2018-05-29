package de.hdm.kontaktsystem.client.gui;



import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.server.ContactSystemAdministrationImpl;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

//Anzeige und Bearbeiten einer Kontaktliste

public class ContactListForm extends VerticalPanel{
	ContactSystemAdministrationAsync contactSystemAdmin = ClientsideSettings.getContactAdministration();
	ContactList contactListToDisplay = null;
	ContactListsTreeViewModel cltvm = null;
	
	/**
	 * Widgets mit variablen Inhalten.
	 */
	
	TextBox nameContactList = new TextBox();
	TextBox firstNameContact = new TextBox();
	TextBox lastNameContact = new TextBox();
	//TextBoox sharedContact = new TextBox();
	
	/**
	 * WIdgets um die Attributte einer Kontaktliste anzuzeigen.
	 */
	
	Label contactListLabel = new Label("Kontaktliste: ");
	Label firstNameLabel = new Label("Vorname: ");
	Label lastNameLabel = new Label("Nachname: ");
	//Label sharedContactLabel = new Label("X");
	
	/**
	 * Loeschenbutton um eine Kontaktliste zu loeschen.
	 */

	Button addButton = new Button ("Kontakt der Liste hinzufï¿½gen");
	Button shareButton = new Button ("Kontaktliste teilen");
	Button deleteButton = new Button ("Kontakt aus eine Liste lï¿½schen");
	Button deleteClButton = new Button ("Kontaktliste lï¿½schen");

	public void onLoad() {
		super.onLoad();
		// Keine Tabelle sondern ein VertialPanel / ScrollPanel dem Kontaktelemente aus dem ContactVector hinzugefügt werden.
		// ... Mit einer while oder for-each Schleife die Kontakt-Elemente erzeugen.
		Grid contactListGrid = new Grid(8, 2);
		this.add(contactListGrid);
		
		contactListGrid.setWidget(0, 0, contactListLabel);
		contactListGrid.setWidget(0, 1, nameContactList);
		
		contactListGrid.setWidget(1, 0, firstNameLabel);
		contactListGrid.setWidget(1, 1, firstNameContact);
		
		contactListGrid.setWidget(2, 0, lastNameLabel);
		contactListGrid.setWidget(2, 1, lastNameContact);
		
		//contactListGrid.setWidget(3, 0, sharedContactLabel);  //widget um anzuzeigen, welche Kontakte einer Liste geteilt wurden
		//contactListGrid.setWidget(3, 1, sharedContact);
		
		addButton.addClickHandler(new addClickHandler());
		addButton.setEnabled(false);
		contactListGrid.setWidget(4, 1, addButton);
		
		shareButton.addClickHandler(new shareClickHandler());
		shareButton.setEnabled(false);
		contactListGrid.setWidget(5, 1, shareButton);
		
		//deleteButton kommt in das Kontakt-Element rein. Es löscht einzelne Kontakte aus der Liste.
		
		deleteButton.addClickHandler(new DeleteClickHandler());
		deleteButton.setEnabled(false);
		contactListGrid.setWidget(6, 1, deleteButton);
		
		deleteClButton.addClickHandler(new DeleteClClickHandler());
		deleteClButton.setEnabled(false);
		contactListGrid.setWidget(7, 1, deleteClButton);
		
	}


	class addClickHandler implements ClickHandler {
	public void onClick(ClickEvent event) {
		if (contactListToDisplay == null) {
			Window.alert("Keine Kontaktliste ausgewï¿½hlt");
			} else {
				
			}
	}
}

	class shareClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			if (contactListToDisplay == null) {
				Window.alert("Keine Kontaktliste ausgewï¿½hlt");
			} else {
				User u = new User();
				u.setGMail("NeuerUser@gmail.com"); // Testdaten, sollte beim Teilen über ein Popup abgefragt werden.
				Participation part = new Participation();
				part.setParticipant(u); 
				part.setReference(contactListToDisplay);
				contactSystemAdmin.shareContactListWith(part,
						new shareContactListWithCallback(part));
				//ALTERNATIV: contactSystemAdmin.createParticipation(contactListToDisplay, u , new AsyncCallback<Participation>() { 
				//In andere NestedKlasse ausgliedern, wie bei "delete Clickhandler"
			}
		}
	}
	class shareContactListWithCallback implements AsyncCallback<Participation> {

		Participation participation = null;
	
		shareContactListWithCallback(Participation part) {
			participation = part;
		}
	
		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			// User nicht gefunden
		}
	
		//@Override
		public void onSuccess(Participation result) {
			// Eventuell ein Update der Liste
			
			Window.alert("KontaktListe wurde erfolgreich geteilt.");
		}
				
	}
		

/**
 * ClickHandler zum Loeschen eines Kontaktes in einer Kontaktliste
 */

class DeleteClickHandler implements ClickHandler {
	
	public void onClick(ClickEvent event) {
		/** Nach Erstellung der kompletten GUI, nochmals überprüfen, wie man den Kontakt übergeben kann um ihn mit einem X zu loeschen.
		 if (contact == null) {
			Window.alert("Keinen Kontakt ausgewï¿½hlt");
		}else {
			contactSystemAdmin.removeContactFromList(contact, contactListToDisplay, 
					new deleteContactfromListCallback());
		}*/
	}
}
// NOCHMAL PRÜFEN
class deleteContactfromListCallback implements AsyncCallback<ContactList> {
	
	
	public void onFailure(Throwable caught) {
		Window.alert("LÃ¶schen eines Kontaktes fehlgeschlagen");
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
 * @author Marco
 *
 */

class DeleteClClickHandler implements ClickHandler {

	@Override
	public void onClick(ClickEvent event) {
		if (contactListToDisplay == null) {
			Window.alert("keine Kontaktliste ausgewï¿½hlt");
		} else {
			contactSystemAdmin.deleteContactList(contactListToDisplay,
					new deleteContactListCallback());
		}
	}

class deleteContactListCallback implements AsyncCallback<ContactList> {

	@Override
	public void onFailure(Throwable caught) {
		Window.alert("Lï¿½schen einer Kontaktliste fehlgeschlagen");
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
	 *  cstvm setter
	 */

	void setCltvm(ContactListsTreeViewModel cltvm) {
	this.cltvm = cltvm;
	}

	void setSelected(ContactList cl) {
		
		if (cl != null) {
			
			contactListToDisplay = cl;
			deleteButton.setEnabled(true);
			nameContactList.setText(p.getParticipant().getContact().getpropertyValue().getName());
			
			if(cl.getReferencedObject() instanceof Contact) {
		    	  Contact c = (Contact) cl.getReferencedObject();
		    	  sharedObject.setText(c.getpropertyValue().getName());
		    	  objectType.setText("Kontakt");
		      }else if(cl.getReferencedObject() instanceof ContactList) {
		    	  ContactList cl = (ContactList) cl.getReferencedObject();
		    	  objectType.setText("Kontaktliste");
		      }else if(cl.getReferencedObject() instanceof PropertyValue) {
		    	  PropertyValue pv = (PropertyValue) cl.getReferencedObject();
		    	  objectType.setText("Eigenschaftswert");
		      }
			
			sharedDate.setText(cl.getReferencedObject().getModifyDate().toString());
			
		} else {
			nameContactList.setText("");
			firstNameContact.setText("");
			lastNameContact.setText("");
			//sharedContact.setText("");

			deleteButton.setEnabled(false);
		}
		*/
	}
		public void setSelected(Object object) {
			// TODO Auto-generated method stub
			

		}



}