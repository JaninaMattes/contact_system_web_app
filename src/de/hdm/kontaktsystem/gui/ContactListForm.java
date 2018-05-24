package de.hdm.kontaktsystem.gui;

import java.awt.Label;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.thies.bankProjekt.client.gui.CustomerForm.deleteCustomerCallback;
import de.hdm.thies.bankProjekt.shared.bo.Customer;

//Anzeige und Bearbeiten einer Kontaktliste

public class ContactListForm extends VerticalPanel{
	ContactSystemAdministrationAsync contactSystem = ClientsideSettings.getContactAdministration();
	ContactList contactListDisplay = null;
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
	
	Button deleteButton = new Button ("Kontaktliste löschen");

	public void onLoad() {
		super.onLoad();
		
		Grid contactListGrid = new Grid(5, 2);
		this.add(contactListGrid);
		
		contactListGrid.setWidget(0, 0, contactListLabel);
		contactListGrid.setWidget(0, 1, nameContactList);
		
		contactListGrid.setWidget(1, 0, firstNameLabel);
		contactListGrid.setWidget(1, 1, firstNameContact);
		
		contactListGrid.setWidget(2, 0, lastNameLabel);
		contactListGrid.setWidget(2, 1, lastNameContact);
		
		//contactListGrid.setWidget(3, 0, sharedContactLabel);
		//contactListGrid.setWidget(3, 1, sharedContact);
		
		deleteButton.addClickHandler(new DeleteClickhandler());
		deleteButton.setEnabled(false);
		contactListGrid.setWidget(4, 1, deleteButton);
		
	}
}

/**
 * Clickhandler zum Löschen einer Kontaktliste.
 * @author Marco
 *
 */

private class DeleteClClickHandler implements ClickHandler {

	@Override
	public void onClick(ClickEvent event) {
		if (contactListToDisplay == null) {
			Window.alert("keine KontaktListe ausgewählt");
		} else {
			contactSystem.delete(customerToDisplay,
					new deleteCustomerCallback(customerToDisplay));
		}
	}
}

class deleteCustomerCallback implements AsyncCallback<Void> {

	Customer customer = null;

	deleteCustomerCallback(Customer c) {
		customer = c;
	}

	@Override
	public void onFailure(Throwable caught) {
		Window.alert("Das Löschen des Kunden ist fehlgeschlagen!");
	}

	@Override
	public void onSuccess(Void result) {
		if (customer != null) {
			setSelected(null);
			catvm.removeCustomer(customer);
		}
	}
}

