package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.User;


/**
 * Formular in dem der User Eigenschaften bearbeiten oder Löschen kann.
 * Es wird durch einen Button im User Formular aufgerufen.
 * @author Oliver Gorges
 *
 */
public class PropertyForm extends VerticalPanel{
	
	ContactSystemAdministrationAsync contactSystemAdmin = null;
	
	HTML infoText = new HTML("<h1> Eigenschaften bearbeiten / löschen </h1>");
	TextBox editTextBox = new TextBox();
	ListBox allPropertys = new ListBox();
	
	String confirmText  = "Wollen Sie wirklich die Eigenschaft %s? \n"
						+ "Diese Änderung betrifft alle Nutzer dieses Systems. \n"
						+ "Alle Eigenschaftsausprägungen, die mit dieser Eigenschaft erstellt wurden, werden verändert oder gelöscht. \n"
						+ "Diese Änderung kann nicht rückgängig gemacht werden. ";
	Button deleteButton = new Button("Eigenschaft löschen");
	Button saveButton = new Button("Eigenschaft speichern");
	Button editButton = new Button("Eigenschaft bearbeiten");
	Button cancelButton = new Button("Abbrechen");
	VerticalPanel vp = new VerticalPanel();
	HorizontalPanel btnPanel = new HorizontalPanel();
	
	boolean edit = false;
	
	/**
	 * Initialisiert die Panels und die Clicklisener für das PropertyForm
	 */
	public PropertyForm(){
		super();
		contactSystemAdmin = ClientsideSettings.getContactAdministration();
		
		//Style der Buttons
		deleteButton.setStyleName("mainButton");
		saveButton.setStyleName("mainButton");
		editButton.setStyleName("mainButton");
		cancelButton.setStyleName("mainButton");
		btnPanel.setStyleName("mainButtonPanel");
		
		this.add(infoText);
		this.add(allPropertys);
		this.add(editTextBox);
		this.add(btnPanel);
		btnPanel.add(deleteButton);
		btnPanel.add(saveButton);
		btnPanel.add(editButton);
		btnPanel.add(cancelButton);
		
		// Einige Elemente werden nur beim Bearbeiten angezeigt
		saveButton.setVisible(false);
		editTextBox.setVisible(false);
		
		// ClickHandler für die Buttons
		// Löscht die ausgewählte Eigenschaft
		deleteButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				deleteSelectedProperty();
			}	
		});
		
		// Blendet die Elemente ein um eine Eigenchaft zu bearbeiten
		editButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				edit = true;
				saveButton.setVisible(true);
				editTextBox.setVisible(true);
				editTextBox.setText(allPropertys.getSelectedItemText());
				
				allPropertys.setVisible(false);
				editButton.setVisible(false);
				deleteButton.setVisible(false);
			}	
		});
		
		// Speichert die geänderte Eigeschaft
		saveButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if(editTextBox.getText() != ""){
					// Ergänzung in dem confirm Text
					String msg = allPropertys.getSelectedItemText() + " zu " + editTextBox.getText() + " ändern";
					if(Window.confirm(confirmText.replaceAll("%s", msg))){
						
						log("Neue Eigenschaft: "+ editTextBox.getText() +" = "+ allPropertys.getSelectedValue());
						Property p = new Property();
						p.setId(Integer.parseInt(allPropertys.getSelectedValue()));
						p.setDescription(editTextBox.getText());
						if(p.getId() != 0 && p.getId() != 1){
							
							contactSystemAdmin.editProperty(p, new AsyncCallback<Property>(){
								@Override
								public void onFailure(Throwable caught) {
									log("Eigenschaft konnte nicht geändert werden.");	
									resetForm();
								}
		
								@Override
								public void onSuccess(Property result) {
									log("Ergebnis: "+ result.getDescription());
									allPropertys.setItemText(allPropertys.getSelectedIndex(), result.getDescription());
									resetForm();
								}
								
							});
							
						}
					}
				}else{
					deleteSelectedProperty();
				}
			}	
		});
		
		cancelButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if(edit){
					resetForm();
				}else{
					RootPanel.get("Details").clear();
				}
			}
			
		});
	}
	
	/**
	 * Befüllt das Propertyform mit den den Eigenschaften aus der Property Tabelle
	 */
	public void  onLoad() {
		//Alle Eigenschaften in einer ListBox auflisten
		contactSystemAdmin.getAllProperties(new AsyncCallback<Vector<Property>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub	
				log("Properties nicht abgerufen");
			}

			@Override
			public void onSuccess(Vector<Property> result) {
				 allPropertys.clear();
				 for(Property p : result) {
					 // Bis auf den Namen, können alle EIgenschaften bearbeitet oder gelöscht werden.
					 if(p.getId()!=1) { 
						 allPropertys.addItem(p.getDescription(), p.getId()+"");
					 }
				 }					
			}
		});
	}
	
	/**
	 * Löscht die in der ListBox ausgewählte Eigenschaft aus der Datenbank.
	 * wurde ausgelagert, da sowohl der DeleteButton als auch der Savebutton( Wenn das Textfeld leer ist) Eigenschaften löschen können.
	 */
	private void deleteSelectedProperty(){
		// Fügt die Beschreibung der Eigenschaft in den confirmText ein.
		if(Window.confirm(confirmText.replaceAll("%s", allPropertys.getSelectedItemText()))){
			Property p = new Property();
			p.setId(Integer.parseInt(allPropertys.getSelectedValue()));
			p.setDescription(allPropertys.getSelectedItemText());
			if(p.getId() != 0 && p.getId() != 1){
				contactSystemAdmin.deleteProperty(p, new AsyncCallback<Property>(){
					@Override
					public void onFailure(Throwable caught) {
						log("Eigenschaft konnte nicht gelöscht werden.");	
						resetForm();
					}

					@Override
					public void onSuccess(Property result) {
						allPropertys.removeItem(allPropertys.getSelectedIndex());
						resetForm();
					}
					
				});
				
			}
		}
	}
	
	/**
	 * Resettet die Formular Buttuns und leert das EditFeld für die nächste Eingabe.
	 * Aufruf im Cancel, Save und Delete Button;
	 */
	private void resetForm(){
		edit = false;
		saveButton.setVisible(false);
		editTextBox.setVisible(false);
		editTextBox.setText("");
		
		allPropertys.setVisible(true);
		editButton.setVisible(true);
		deleteButton.setVisible(true);
	}
	
	native void log(String s)/*-{
	console.log(s);
	}-*/;
	
}
