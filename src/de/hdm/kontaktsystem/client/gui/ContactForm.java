package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class ContactForm extends VerticalPanel {
	
		//Referenzattribute
		User myUser = null;
		
		Contact contactToDisplay = null;
		CellTreeViewModel tvm = null;
		
		Grid contactGrid = new Grid(12, 3);
		
		//Proxy-Objekt erzeugen
		ContactSystemAdministrationAsync contactSystemAdmin = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();		
		
		/**
		 * Widgets, deren Inhalte variable sind, werden als Attribute angelegt.
		 */	
		Vector <TextBox> tbv = new Vector <TextBox>();
		Vector<CheckBox> cbv = new Vector<CheckBox>();
		FlexTable ft = new FlexTable();
		
		HorizontalPanel ePanel = new HorizontalPanel();
		HorizontalPanel btnPanel = new HorizontalPanel();
		HorizontalPanel addPanel = new HorizontalPanel();
		
		Label cLabel = new Label("Kontakt:");
				
		Label isShared = new Label("Auswahl: ");
		Label labelShare = new Label("Teilen mit: ");
		Label labelSharedWith = new Label("Geteilt mit: ");
		Label labelReceivedFrom = new Label("Geteilt von: ");
		Label contactStatus = new Label("");
				
		Button deleteButton = new Button("Löschen");
		Button saveButton = new Button("Speichern");
		Button shareButton = new Button("Teilen");
		Button addButton = new Button("Hinzufügen");
		Button emailButton = new Button("Prüfen");
		
		ListBox addElement = new ListBox();
		ListBox sharedWithUser = new ListBox();
		Label receivedFrom = new Label();
		
		TextBox email = new TextBox();
						
		Button cancelButton = new Button("Abbrechen");
		Button createButton = new Button("Erstellen");
					
		
		/**
		 * Startpunkt ist die onLoad() Methode
		 */
		public void  onLoad() {
			super.onLoad();
						
			final User user = new User();

			final VerticalPanel vp  = new VerticalPanel();
			this.add(vp);
			
			//CSS
			addButton.setStyleName("add");
			emailButton.setStyleName("check");	
			saveButton.setStyleName("save");
			shareButton.setStyleName("share");
			
			//Teilhaberschaften
			labelSharedWith.setVisible(false);
			labelReceivedFrom.setVisible(false);
			
			sharedWithUser.setVisible(false);
			receivedFrom.setVisible(false);
			
			
			shareButton.addClickHandler(new ClickHandler(){
				
				@Override
				public void onClick(ClickEvent event) {
					if(user.getUserContact().getName().getValue() == email.getText()){
						Participation part = new Participation();
						Contact c = contactToDisplay;
						Vector<PropertyValue> pvv = new Vector<PropertyValue>();
						for(CheckBox cb: cbv){
							if(cb.getValue()){
								log(cb.getTitle());
								for(PropertyValue pv : c.getPropertyValues()){
									if(pv.getBoId() == Integer.parseInt(cb.getTitle())){
										pvv.add(pv);
										log("Share: " + pv.getValue() + " With " + email.getText());
									}
								}
							}
						}
						part.setParticipant(user);
						c.setPropertyValues(pvv);
						part.setReference(c);
						log("Share:" + part);
						contactSystemAdmin.createParticipation(part, new CreateParticipationCallback());						
					}else{
						Window.alert("Bitte geben Sie die Email eines Nutzers ein und bestätigen Sie diese mit dem CheckButton");
					}
				}
				
			});
			
			deleteButton.addClickHandler(new ClickHandler(){
				Vector <PropertyValue> result = new Vector <PropertyValue>();
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub				
					if(Window.confirm("Bitte bestätigen Sie dass der Kontakt gelöscht werden soll.")) {				
					contactSystemAdmin.deleteContact(contactToDisplay, new AsyncCallback<Contact>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							Window.alert("Der Kontakt konnte nicht gelöscht werden.");
						}

						@Override
						public void onSuccess(Contact result) {
						contactToDisplay = null;
						tvm.removeBusinessObject(result);
						}						
					});
					}					
				} 				
			});
			
			/*
			 * Save-Button ClickHandler
			 */
			saveButton.addClickHandler(new ClickHandler() {
				Vector<PropertyValue>editResult = new Vector<PropertyValue>();
				Vector<PropertyValue>createResult = new Vector<PropertyValue>();
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					
					if(contactToDisplay!=null) { //1. Kontakt exisitiert -> Edit
						for(TextBox tb:tbv) {
							if(!tb.getText().isEmpty()) {//1.1 TextBox nicht leer
								if(tb.getTitle().contains("Neu")) {//1.1.1 TextBox neu?
									String[] s=tb.getTitle().split(":", 2);
									log("Spliterator: "+s[1]);
									Property p = new Property();//1.1.1.1 Erzeuge neuesObjekt		
									p.setId(Integer.parseInt(s[1]));
									PropertyValue ppv = new PropertyValue();
									ppv.setBo_Id(0);
									ppv.setContact(contactToDisplay);
									ppv.setOwner(contactToDisplay.getOwner());
									ppv.setProperty(p);
									ppv.setValue(tb.getText());
									editResult.add(ppv);
								} else{//1.1.2 TextBox alt?
									for(PropertyValue pv:contactToDisplay.getPropertyValues()) { //editieren + hinzufügen
										if(pv.getBoId()==Integer.parseInt(tb.getTitle())) {
											pv.setValue(tb.getText());
											editResult.add(pv);
										}
									}
								}
							} else { //1.2TextBox leer -> löschen
								for(PropertyValue pv: contactToDisplay.getPropertyValues()) {
									if(pv.getBoId()==Integer.parseInt(tb.getTitle())) {
										pv.setValue("");
										editResult.add(pv);
										log("Lösche aus Vector:" +tb);
										tbv.remove(tb);
									}
								}						
							}
						 } 
						 log("Kontakte Editieren:"+editResult);
						 contactToDisplay.setPropertyValues(editResult);
						 contactSystemAdmin.editContact(contactToDisplay, new SaveCallback());
						 
						} else {
						for(TextBox tb:tbv) {//2. Kontakt existiert nicht -> CREATE
							if(!tb.getText().isEmpty()) { //1.1 TextBox nicht leer
								if(tb.getTitle().contains("Neu")) {//1.1.1 TextBox neu?
									String[] s=tb.getTitle().split(":", 2);
									log("Spliterator: "+s[1]);
									Property p = new Property();//1.1.1.1 Erzeuge neues Objekt		
									p.setId(Integer.parseInt(s[1]));
									PropertyValue ppv = new PropertyValue();
									ppv.setBo_Id(0);
									ppv.setContact(contactToDisplay);
									ppv.setOwner(contactToDisplay.getOwner());
									ppv.setProperty(p);
									ppv.setValue(tb.getText());
									createResult.add(ppv);
								}								
							}else {//1.2TextBox leer -> löschen
								log("Lösche aus Vector:" +tb);
								tbv.remove(tb);
							}					
								
						}
						 log("Kontakte Erstellen:"+createResult);
						contactToDisplay.setPropertyValues(createResult);
						contactSystemAdmin.createContact(contactToDisplay, new SaveCallback());
					} 
				}
				
			});
			
			emailButton.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					contactSystemAdmin.getUserBygMail(email.getText(), new AsyncCallback<User>(){
						@Override
						public void onFailure(Throwable caught) {
							log("User nicht gefunden");
							emailButton.setStyleName("check_notFound");
							emailButton.setText("X");
							emailButton.setEnabled(false);
						}

						@Override
						public void onSuccess(User result) {
							log("User"+result);
							emailButton.setStyleName("check_Found");
							emailButton.setText("OK");
							user.setGoogleID(result.getGoogleID());
							user.setGMail(result.getGMail());
							user.setUserContact(result.getUserContact());
							email.setText(result.getUserContact().getName().getValue());
						}
					});
				}
				
			});
			
			email.addChangeHandler(new ChangeHandler() {
		
				@Override
				public void onChange(ChangeEvent event) {
					emailButton.setText("Prüfen");	
					emailButton.setEnabled(true);
				}
			});
			
			cancelButton.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
				 contactToDisplay = null;
				 RootPanel.get("Details").clear();
				 //TODO: Werte des Flextables zurück setzen
				}
				
			});
			
			addButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Property p = new Property();
					p.setId(Integer.parseInt(addElement.getTitle()));
					p.setDescription(addElement.getSelectedItemText());
		
					Label label = new Label(addElement.getSelectedItemText());
					TextBox tb = new TextBox();
					CheckBox cb = new CheckBox();	
					
					tb.setTitle("Neu:"+p.getId());
					cb.setTitle("Neu:"+p.getId());
					
					tbv.add(tb);
					cbv.add(cb);
					
					int row = ft.getRowCount()+1;
					
					ft.setWidget(row, 0, label);
					ft.setWidget(row, 1, tb);
					ft.setWidget(row, 2, cb);
				}
				
			});
					
		    /*
		     * GridPanel für Abbildung der Teilhaber
		     */
						
			Grid gp = new Grid(2,2);
			gp.setWidget(0, 0, labelSharedWith);
			gp.setWidget(0, 1, sharedWithUser);
			
			gp.setWidget(1, 0, labelReceivedFrom);
			gp.setWidget(1, 1, receivedFrom);
			
			/*
			 * Panel für Anordnung der Button
			 */
			
			btnPanel.add(deleteButton);
			btnPanel.add(saveButton);
			btnPanel.add(shareButton);	
			
			ePanel.add(email);
			ePanel.add(emailButton);
			
			addPanel.add(addElement);
			addPanel.add(addButton);
			
			/*
			 * Zuordnung zum VP			
			 */
			vp.add(cLabel);
			vp.add(contactStatus);
			vp.add(ft);
			vp.add(gp);
			vp.add(ePanel);
			vp.add(addPanel);
			vp.add(btnPanel);
			
			RootPanel.get("Details").add(vp);
		}
		
		
		public void setSelected(Contact contact) {
			
			myUser = new User();
			myUser.setGoogleID(170); //TODO: später über AppEngine angebunden
			
			if(contact!=null&contact.getBoId()!=0) {
				log("Kontakt" + contact);
				this.contactToDisplay = contact;
				
				cLabel.setText("Kontakt Id: " + contact.getBoId());			

				if(contact.isShared_status()) {
					contactStatus.setText("Status: Geteilt");
//					this.setUpCheckBoxValues(contact);
				} else { 
					contactStatus.setText("Status: Nicht geteilt");
				}	
				if(myUser.getGoogleID()!=contact.getOwner().getGoogleID()) {
					labelReceivedFrom.setVisible(true);
					receivedFrom.setVisible(true);
					receivedFrom.setText(contact.getOwner().getGMail());
				}else{				
					labelSharedWith.setVisible(false);
					sharedWithUser.setVisible(false);
					contactSystemAdmin.getAllParticipationsByBusinessObject(contactToDisplay, new AsyncCallback<Vector<Participation>>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							Window.alert("Teilhaberschaften konnten nicht abgerufen werden.");
						}

						@Override
						public void onSuccess(Vector<Participation> result) {
							for(Participation part: result) {
								if(part.getParticipant().getGoogleID()==myUser.getGoogleID()) {
									result.remove(part);
								}else {
									sharedWithUser.addItem(part.getParticipant().getGMail());
								}
							}
						}
						
					});
					
				}
				Boolean isChecked = new Boolean(true);
				int row = 0;
				
				//Flextable befüllen
				for(PropertyValue pv : contact.getPropertyValues()){
					log("PropertyValue" +pv);
					Label label = new Label();
					CheckBox cb = new CheckBox();
					TextBox tb = new TextBox();
					
					label.setTitle(pv.getBoId()+"");
					label.setText(pv.getProperty().getDescription());
					cb.setTitle(pv.getBoId()+"");
					tb.setTitle(pv.getBoId()+"");
					tb.setText(pv.getValue());
					
					cb.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub					
						}						
					});
					
					cbv.add(cb);
					tbv.add(tb);
								
					ft.setWidget(row, 0, label);
					ft.setWidget(row, 1, tb);
					ft.setWidget(row, 2, cb);
					
					log("Table row:" + row);
					row++;
				}
			
			//Elemente füllen
			contactSystemAdmin.getAllProperties(new AsyncCallback<Vector<Property>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub	
					log("Properties nicht abgerufen");
				}

				@Override
				public void onSuccess(Vector<Property> result) {
					 addElement.clear();					 
					 for(Property p : result) {
						 if(p.getId()!=1) { 
						 addElement.addItem(p.getDescription());
						 addElement.setTitle(p.getId()+"");
						 }
					 }					
				}
				});
							
			} else {
				this.contactToDisplay = null;				
				log("Neuer Kontakt" + contact);
				
//				final Vector<Property> ppv = new Vector <Property>(); TODO prüfen
				final Vector<Property> ppv = null;
				contactSystemAdmin.getAllProperties(new AsyncCallback<Vector<Property>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						Window.alert("Eigenschaften konnten nicht abgerufen");						
					}

					@Override
					public void onSuccess(Vector<Property> result) {
						ppv.addAll(result);					
						log("Kontakt Properties: " + result);
					}
					
				});	
				
				cLabel.setText("Kontakt Id: " + 0);			
				contactStatus.setText("Status: Noch nicht erstellt");				
				
				Boolean isChecked = new Boolean(true);
				int row = 0;
				//Flextable befüllen
				
				for(Property p : ppv){
					log("PropertyValue" +p);
					Label label = new Label();
					CheckBox cb = new CheckBox();
					TextBox tb = new TextBox();
					
					label.setTitle("Neu:"+p.getId());
					label.setText("Neu:"+p.getId());
					cb.setTitle("Neu:"+p.getId());
					tb.setTitle("Neu:"+p.getId());
					tb.setText("");
					
					cb.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub					
						}						
					});
					
					cbv.add(cb);
					tbv.add(tb);
								
					ft.setWidget(row, 0, label);
					ft.setWidget(row, 1, tb);
					ft.setWidget(row, 2, cb);
					
					log("Table row:" + row);
					row++;
				}				
				
			}
		}

		private class SaveCallback implements AsyncCallback<Contact> {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert("Speichern ist fehlgeschlagen.");
			}

			@Override
			public void onSuccess(Contact result) {
				// TODO Auto-generated method stub
				log("Kontakt gespeichert: "+result);
				contactToDisplay = result;
				tvm.updateBusinessObject(result);
			}							
		}
		
		/**
		 * Eine neue Teilhaberschaft anlegen.
		 * @author Janina
		 *
		 */
		private class CreateParticipationCallback implements AsyncCallback<Participation>{

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Kontakt konnte nicht geteilt werden. :(");						
			}

			@Override
			public void onSuccess(Participation result) {
				log("Neue Teilhaberschaft: " + result);	
				//TODO: 
			}				
		}
				
		/*
		 * TreeViewModel setter
		 */
		
		void setTree(CellTreeViewModel tvm) {
			this.tvm = tvm;
		}

		void setMyUser(User user) {
			this.myUser = user;
		}
		
		/*
		 * Logger
		 */
		native void log(String s)/*-{
		console.log(s);
		}-*/;
}