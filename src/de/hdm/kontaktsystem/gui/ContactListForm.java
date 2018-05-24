package de.hdm.kontaktsystem.gui;



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
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;

//Anzeige und Bearbeiten einer Kontaktliste

public class ContactListForm extends VerticalPanel{
	ContactSystemAdministrationAsync contactSystem = ClientsideSettings.getContactAdministration();
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
	 * Löschenbutton um eine Kontaktliste zu löschen.
	 */

	Button addButton = new Button ("Kontakt der Liste hinzufügen");
	Button shareButton = new Button ("Kontaktliste teilen");
	Button deleteButton = new Button ("Kontakt aus eine Liste löschen");
	Button deleteClButton = new Button ("Kontaktliste löschen");

	public void onLoad() {
		super.onLoad();
		
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
			Window.alert("Keine Kontaktliste ausgewählt");
			} else {
				
			}
	}
}

//class shareClickHandler implements ClickHandler {
//	public void onClick(ClickEvent event) {
//		if (contactListToDisplay == null) {
//			Window.alert("Keine Kontaktliste ausgewählt");
//		} else {
//			contactSystemVerwaltung.
//		}
//	}
//}

/**
 * ClickHandler zum Löschen eines Kontaktes in einer Kontaktliste
 */

class DeleteClickHandler implements ClickHandler {
	
	public void onClick(ClickEvent event) {
		if (contactListToDisplay == null) {
			Window.alert("Keinen Kontaktliste ausgewählt");
		}else {
			contactSystemVerwaltung.delete(contactListToDisplay, 
					new deleteContactCallback(contactListToDisplay));
		}
	}
}

class deleteContactCallback implements AsyncCallback<Void> {
	
	ContactList contactList = null;
	
	deleteContactCallback(ContactList cl){
		contactList = cl;
		}
	
	public void onFailure(Throwable caught) {
		Window.alert("Löschen eines Kontaktes fehlgeschlagen");
	}
	
	public void onSuccess(Void result) {
		if (contactList != null) {
			setSelected(null);
			cltvm.removeContactList(contactList);
		}
	}
}

/**
 * Clickhandler zum Löschen einer Kontaktliste.
 * @author Marco
 *
 */

class DeleteClClickHandler implements ClickHandler {

	@Override
	public void onClick(ClickEvent event) {
		if (contactListToDisplay == null) {
			Window.alert("keine Kontaktliste ausgewählt");
		} else {
			ContactSystemAdministrationImpl.deleteContactList(contactListToDisplay,
					new deleteContactListCallback(contactListToDisplay));
		}
	}

class deleteContactListCallback implements AsyncCallback<Void> {

	ContactList contactList = null;

	deleteContactListCallback(ContactList cl) {
		contactList = cl;
	}

	@Override
	public void onFailure(Throwable caught) {
		Window.alert("Löschen einer Kontaktliste fehlgeschlagen");
	}

	@Override
	public void onSuccess(Void result) {
		if (contactList != null) {
			setSelected(null);
			cltvm.removeContactList(contactList);
		}
	}
}

	/**
	 *  cstvm setter
	 */

	void setCStvm(ContactListsTreeViewModel cltvm) {
	this.cltvm = cltvm;
	}

	void setSelected(ContactList cl) {
		if (cl != null) {
			contactListToDisplay = cl;
			deleteButton.setEnabled(true);
			nameContactList.setText(p.getParticipant().getContact().getpropertyValue().getName());
			
			if(cl.getReferencedObject() instanceof Contact) {
		    	  Contact c = (Contact) p.getReferencedObject();
		    	  sharedObject.setText(c.getpropertyValue().getName());
		    	  objectType.setText("Kontakt");
		      }else if(p.getReferencedObject() instanceof ContactList) {
		    	  ContactList cl = (ContactList) p.getReferencedObject();
		    	  objectType.setText("Kontaktliste");
		      }else if(p.getReferencedObject() instanceof PropertyValue) {
		    	  PropertyValue pv = (PropertyValue) p.getReferencedObject();
		    	  objectType.setText("Eigenschaftswert");
		      }
			
			sharedDate.setText(p.getReferencedObject().getModifyDate().toString());
			
		} else {
			nameContactList.setText("");
			firstNameContact.setText("");
			lastNameContact.setText("");
			//sharedContact.setText("");

			deleteButton.setEnabled(false);
		}
	}

}
}