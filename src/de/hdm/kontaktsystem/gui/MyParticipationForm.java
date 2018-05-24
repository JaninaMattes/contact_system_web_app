package de.hdm.kontaktsystem.gui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.thies.bankProjekt.client.gui.CustomerForm.ChangeClickHandler;
import de.hdm.thies.bankProjekt.client.gui.CustomerForm.DeleteClickHandler;
import de.hdm.thies.bankProjekt.client.gui.CustomerForm.NewClickHandler;

/**
 * Anzeige und Bearbeiten einer Teilhaberschaft
 * 
 * @author Sandra
 *
 */

public class  extends VerticalPanel{
	ContactSystemAdministrationAsync contactSystemVerwaltung = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();
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


		
		deleteButton.addClickHandler(new DeleteClickHandler());
		deleteButton.setEnabled(false);
		customerGrid.setWidget(4, 1, deleteButton);



	}
	
}
