package de.hdm.kontaktsystem.client.gui;

import com.google.gwt.i18n.client.DateTimeFormat;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.User;


/**
 * Anzeige des Userprofils, bietet dem user die Möglichkeit, die im User Object und dem Eigene Kontakt abgelegten Daten einzusehen.
 * Ebenso erhält er die möglichkeit, über einen Button seinen eigenen Kontakt anzuzeigen, EIgenschaften zu bearbeiten und seinen Account zu löschen.
 * @author Oliver Gorges
 *
 */
public class UserForm extends VerticalPanel{
	
	ContactSystemAdministrationAsync contactSystemAdmin = null;
	
	HTML infoText = new HTML("<h1> Mein Profil </h1>");
	Label welcome = new Label();
	Label id = new Label();
	Label email = new Label();
	Label creationDate = new Label();
	Label contact = new Label();
	// Abfrage, die den Nutzer über die Auswirkung der Löschung informiert.
	String deleteText = " Wollen Sie wirklich Ihren Account löschen? \n"
						+ "Alle Ihre Kontakte und Kontaktlisten werden gelöscht \n"
						+ "und die dazugehörigen Teilhaberschaften aufgelöst. \n"
						+ "Diese Änderung kann nicht rückgängig gemacht werden. ";
	Button deleteButton = new Button("Account Löschen");
	Button propertyButton = new Button("Eigenschaften bearbeiten");
	Button contactButton = new Button("Kontakt anzeigen");
	VerticalPanel vp = new VerticalPanel();
	HorizontalPanel btnPanel = new HorizontalPanel();
	CellTreeViewModel tvm = null;
	PropertyForm pf = new PropertyForm();
	User myUser;
	
	/**
	 * Startpunkt
	 */
	
	public void  onLoad() {
		contactSystemAdmin = ClientsideSettings.getContactAdministration();
		
		this.add(vp);
		vp.add(infoText);
		vp.add(welcome);
		vp.add(id);
		vp.add(email);
		vp.add(creationDate);
		vp.add(contact);
		vp.add(btnPanel);
		
		// Menüpanel
		btnPanel.add(contactButton);
		btnPanel.add(propertyButton);
		btnPanel.add(deleteButton);
		
		// CSS 
		
		welcome.setStyleName("Label");
		id.setStyleName("Label");
		email.setStyleName("Label");
		creationDate.setStyleName("Label");
		contact.setStyleName("Label");
		btnPanel.setStyleName("mainButtonPanel");
		contactButton.setStyleName("mainButton");
		propertyButton.setStyleName("mainButton");
		deleteButton.setStyleName("mainButton");
		
	}
	
	// Übergibt den Tree für die anzeige des eigene Kontakts
	public void setTree(CellTreeViewModel tree){
		this.tvm = tree;
	}
	
	/**
	 * Setzt den User der in dem Formular angezeigt werden soll
	 * @param User-Objekt
	 */
	public void setMyUser(User user){
		myUser = user;
		if(myUser != null){
			// Anzeige der Userinformationen
			welcome.setText("Hallo " + user.getUserContact().getName().getValue());
			id.setText("Meine User ID: " + user.getGoogleID());
			email.setText("Meine Email Adresse: " + user.getGMail());
			contact.setText("Kontakt ID: " + user.getUserContact().getBoId());
			
			// Formtiert das Erstellungsdatum für die Anzeige
			DateTimeFormat date = DateTimeFormat.getFormat("dd.MM.yyyy");
			creationDate.setText("Mitglied seit: " + date.format(user.getUserContact().getCreationDate()));
			
			deleteButton.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					if(Window.confirm(deleteText)){
						contactSystemAdmin.deleteUser(myUser, new AsyncCallback<User>(){

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								Window.alert("Ihr User konnte nicht gelöscht werden. \n"
										+ "Bitte wenden Sie sich an den Seite Betreiber für weiter Informationen.");
							}

							@Override
							public void onSuccess(User result) {
								// Weiterleitung auf die Loginseite 
								Window.Location.assign(myUser.getLogoutUrl());
							}
							
						});
					}
				}
			});
			
			// Anzeige des Eigenen Kontakts im Kontaktform
			contactButton.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					
					log("MyContact" + myUser.getUserContact());
					tvm.setSelectedContactContactlist(myUser.getUserContact());
					
				}
				
			});
			
			// Laden des Propertyforms um Eigenschaften bearbeiten und löschenzu können
			propertyButton.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					// Öffnet ein Formular zum bearbeiten und löschen von Eigenschaften
					RootPanel.get("Details").clear();
					RootPanel.get("Details").add(pf);			
				}
				
			});
		}
	}

	/*
	 * Logger für die Javascript Konsole.
	 */
	native void log(String s)/*-{
	console.log(s);
	}-*/;
	
}
