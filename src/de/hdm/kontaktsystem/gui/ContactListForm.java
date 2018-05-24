package de.hdm.kontaktsystem.gui;

import java.awt.Label;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.bo.ContactList;

//Anzeige und Bearbeiten einer Kontaktliste

public class ContactListForm extends VerticalPanel{
	ContactSystemAdministrationAsync contactSystem = ClientsideSettings.getContactAdministration();
	ContactList contactListDisplay = null;
	ContactListsTreeViewModel cltvm = null;
	
	/**
	 * WIdgets um die Attributte einer Kontaktliste zu speichern
	 */
	
	Label cLValueLabel = new Label("Kontaktliste: ");
	TextBox NameTextBox = new TextBox();
	Button newButton = new Button ("Kontaktliste speichern");

	public void onLoad() {
		super.onLoad();
		
		Grid contactListGrid = new Grid(4, 2);
		this.add(contactListGrid);
		
		contactListGrid.setWidget(0, 0, contactListGrid);
		
		
		
	}
	
	
}
