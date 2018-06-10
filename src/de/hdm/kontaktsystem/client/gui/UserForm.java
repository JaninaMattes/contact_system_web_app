package de.hdm.kontaktsystem.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.User;

public class UserForm extends VerticalPanel{
	
	ContactSystemAdministrationAsync contactSystemAdmin = null;
	
	Label welcome = new Label();
	Label id = new Label();
	Label email = new Label();
	Label contact = new Label();
	
	String deleteText = " Wollen Sie wirklich Ihren Account löschen? \n"
						+ "Alle Ihre Kontakte und Kontaktlisten werden gelöscht \n"
						+ "und die dazugehörigen Teilhaberschaften aufgelöst. \n"
						+ "Diese Änderung kann nicht rückgängig gemacht werden. ";
	Button delete = new Button("Account Löschen");
	VerticalPanel vp = new VerticalPanel();
	
	User myUser;
	
	/**
	 * Startpunkt
	 */
	
	public void  onLoad() {
		contactSystemAdmin = ClientsideSettings.getContactAdministration();
		
		this.add(vp);
		welcome.setText("Hallo ");
		vp.add(welcome);
		id.setText("Meine User ID: ");
		vp.add(id);
		email.setText("Meine Email Adresse: ");
		vp.add(email);
		contact.setText("Mein Kontakt: ");
		vp.add(contact);
		vp.add(delete);
		
	}
	
	public void setUser(User user){
		myUser = user;
		if(myUser != null){
			welcome.setText(welcome.getText() + user.getUserContact().getName());
			id.setText(id.getText() + user.getGoogleID());
			email.setText(email.getText() + user.getGMail());
			contact.setText(contact.getText() + user.getUserContact().toString());
			
			delete.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					if(Window.confirm(deleteText)){
						contactSystemAdmin.deleteUser(myUser, new AsyncCallback<User>(){

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								Window.alert("Ihr User konnte nicht gelöscht werden. \n"
										+ "Bitte wenden Sie sich an den Seite Betreiber fürweiter Informationen.");
							}

							@Override
							public void onSuccess(User result) {
								// TODO Auto-generated method stub
								Window.Location.assign(myUser.getLogoutUrl());
							}
							
						});
					}
				}
			});
		}
	}
	
	
}
