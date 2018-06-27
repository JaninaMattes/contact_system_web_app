package de.hdm.kontaktsystem.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
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
	Button deleteButton = new Button("Account Löschen");
	Button contactButton = new Button("Kontakt anzeigen");
	VerticalPanel vp = new VerticalPanel();
	ContactForm cf = new ContactForm();
	User myUser;
	
	/**
	 * Startpunkt
	 */
	
	public void  onLoad() {
		contactSystemAdmin = ClientsideSettings.getContactAdministration();
		
		this.add(vp);
		//welcome.setText("Hallo ");
		vp.add(welcome);
		//id.setText("Meine User ID: ");
		vp.add(id);
		//email.setText("Meine Email Adresse: ");
		vp.add(email);
		vp.add(contact);
		vp.add(contactButton);

		vp.add(deleteButton);
		
	}
	
	public void setMyUser(User user){
		myUser = user;
		if(myUser != null){
			welcome.setText("Hallo " + user.getUserContact().getName().getValue());
			id.setText("Meine User ID: "+ user.getGoogleID());
			email.setText("Meine Email Adresse: "+ user.getGMail());
			contact.setText("Kontakt ID: "+user.getUserContact().getBoId());
		
			
			deleteButton.addClickHandler(new ClickHandler(){
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
								Window.Location.assign(myUser.getLogoutUrl());
							}
							
						});
					}
				}
			});
			contactButton.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {

					RootPanel.get("Details").clear();
					cf.setSelected(myUser.getUserContact());
					
					RootPanel.get("Details").add(cf);
					
				}
				
			});
		}
	}
	
	native void log(String s)/*-{
	console.log(s);
	}-*/;
	
}
