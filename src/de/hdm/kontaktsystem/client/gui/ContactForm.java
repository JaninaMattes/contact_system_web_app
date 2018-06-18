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
import com.google.gwt.user.client.ui.PopupPanel;
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
		
		// Lade overlay
		PopupPanel loadPanel;
		
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
		
		final VerticalPanel vp  = new VerticalPanel();
				
		HorizontalPanel ePanel = new HorizontalPanel();
		HorizontalPanel btnPanel = new HorizontalPanel();
		HorizontalPanel gpPanel = new HorizontalPanel();
		Grid gp = new Grid(4,3);
		
		Label cLabel = new Label("Kontakt:");
		
		Label labelShare = new Label("Kontakt teilen mit: ");
		Label labelSharedWith = new Label("Kontakt Teilhaber: ");
		Label labelReceivedFrom = new Label("Kontakt Ersteller: ");
		Label labelAddElement = new Label("Ein weiteres Feld hinzufügen:");
		Label contactStatus = new Label("");
				
		Button deleteButton = new Button("Löschen");
		Button saveButton = new Button("Speichern");
		Button shareButton = new Button("Teilen");
		Button addButton = new Button("Hinzufügen");
		Button emailButton = new Button("Email Prüfen");
		Button editButton = new Button("Bearbeiten");
		
		Button cancelButton = new Button("Abbrechen");
		Button createButton = new Button("Speichern");
		
		ListBox addElement = new ListBox();
		ListBox sharedWithUser = new ListBox();

		TextBox email = new TextBox();
								
		/**
		 * Startpunkt ist die onLoad() Methode
		 */
		
		public void onLoad(){
			vp.clear();
			
			super.onLoad();
			this.add(vp);
							
			/*
			 * Panel für Anordnung der Button
			 */
			
			btnPanel.add(deleteButton);
			btnPanel.add(saveButton);
			btnPanel.add(shareButton);	
					
			/*
			 * Zuordnung zum VP			
			 */
			vp.add(cLabel);
			vp.add(contactStatus);
			vp.add(ft);
			vp.add(gpPanel);
			vp.add(btnPanel);
			
		}
		
		public ContactForm() {
			
			final User user = new User();
			
			//CSS
			addButton.setStyleName("add");
			emailButton.setStyleName("check");	
			saveButton.setStyleName("save");
			shareButton.setStyleName("share");
			
			cancelButton.setStyleName("cancel");
			editButton.setStyleName("edit");
			
			gp.setStyleName("grid-panel");
			
			//Teilhaberschaften
			labelSharedWith.setVisible(false);
			labelReceivedFrom.setVisible(false);			
			sharedWithUser.setVisible(false);	
			
			/*
		     * GridPanel für Abbildung der Teilhaber
		     */								
			
			gp.setWidget(0, 0, labelAddElement);
			gp.setWidget(0, 1, addElement);
			gp.setWidget(0, 2, addButton);
			
			gp.setWidget(1, 0, labelShare);
			gp.setWidget(1, 1, email);
			gp.setWidget(1, 2, emailButton);
			
			gp.setWidget(2, 0, labelSharedWith);
			gp.setWidget(2, 1, sharedWithUser);	
			gp.setWidget(2, 2, editButton);
			
			gp.setWidget(3, 0, labelReceivedFrom);
			
			
			gpPanel.add(gp);
			
			//Click-Handler
			shareButton.addClickHandler(new ClickHandler(){
				
				@Override
				public void onClick(ClickEvent event) {
					loadPanel.setVisible(true);
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
					loadPanel.setVisible(true);
					// TODO Auto-generated method stub				
					if(Window.confirm("Bitte bestätigen Sie dass der Kontakt gelöscht werden soll.")) {				
					contactSystemAdmin.deleteContact(contactToDisplay, new AsyncCallback<Contact>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							loadPanel.setVisible(false);
							Window.alert("Der Kontakt konnte nicht gelöscht werden.");
						}

						@Override
						public void onSuccess(Contact result) {
						loadPanel.setVisible(false);
						contactToDisplay= null;
//						tvm.removeBusinessObject(result);
						RootPanel.get("Details").clear();
						loadPanel.setVisible(false);
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
				
				@Override
				public void onClick(ClickEvent event) {
					loadPanel.setVisible(true);
					if(contactToDisplay!=null) {
						log("Der Kontakt besteht bereits. "+tbv.size());
						for(TextBox tb: tbv) {
							log("Inhalt: "+tb);
							 if(tb.getText().isEmpty()&&Integer.parseInt(tb.getTitle())==1){ //1.0 verhindern dass Kontakt Name leer
									Window.alert("Das Feld Name darf nicht leer sein.");
									return;							
							 }
							 
							 if(!tb.getText().isEmpty()) {

								 log("Titel:"+tb.getTitle());
								 if(tb.getTitle().contains("Neu")) {
									 	String[]s=tb.getTitle().split(":");
										log("Split String:"+s[1]);
										log("Split String:"+s[0]);
										Property p = new Property();//1.1.1.1 Erzeuge neuesObjekt		
										p.setId(Integer.parseInt(s[1]));
										p.setDescription(s[0]);
										
										PropertyValue ppv = new PropertyValue();
										ppv.setContact(contactToDisplay);
										ppv.setOwner(contactToDisplay.getOwner());
										ppv.setProperty(p);
										ppv.setValue(tb.getText());
										editResult.add(ppv);
										log("Neuen Pv:"+ppv);
								 	}
								 else{
									 log("Update");
									 for(PropertyValue pv:contactToDisplay.getPropertyValues()) { //editieren + hinzufügen
										 	log(pv.getBoId()+"=="+Integer.parseInt(tb.getTitle()));
											if(pv.getBoId()==Integer.parseInt(tb.getTitle())) {
												pv.setValue(tb.getText());
												editResult.add(pv);
												log("Editieren des Pv:"+pv);
											}
									   }									
								 }

							 }
												
							}
						contactToDisplay.setPropertyValues(editResult);
						contactSystemAdmin.editContact(contactToDisplay, new SaveCallback());
						}
					}
				});
			
			/**
			 * Create-Button ClickHandler
			 */
			createButton.addClickHandler(new ClickHandler() {
				Vector<PropertyValue>createResult = new Vector<PropertyValue>();

				@Override
				public void onClick(ClickEvent event) {
					loadPanel.setVisible(true);
					for(TextBox tb: tbv) {
						 if(tb.getText().isEmpty()&tb.getTitle().equals("Neu:1")) { //1.0 verhindern dass Kontakt Name leer
								Window.alert("Das Feld Name darf nicht leer sein.");
								return;
						 }
						 if(!tb.getText().isEmpty()) {
							 	Property p = new Property();//1.1.1.1 Erzeuge neuesObjekt		
							 	String[]s=tb.getTitle().split(":");
								log("Split String:"+s[1]);
								log("Split String:"+s[0]);
								p.setId(Integer.parseInt(s[1]));
								p.setDescription(s[0]);
								
								PropertyValue ppv = new PropertyValue();
								ppv.setContact(contactToDisplay);
								ppv.setOwner(contactToDisplay.getOwner());
								ppv.setProperty(p);
								ppv.setValue(tb.getText());
								createResult.add(ppv);
								log("Erzeugen eines neuen Pv:"+ppv);
						 }
					}
					contactToDisplay.setPropertyValues(createResult);
					contactSystemAdmin.createContact(contactToDisplay, new SaveCallback());					
				}
			});
			
			/**
			 * Email-ClickHandler 
			 */
			
			emailButton.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					contactSystemAdmin.getUserBygMail(email.getText(), new AsyncCallback<User>(){
						@Override
						public void onFailure(Throwable caught) {
							loadPanel.setVisible(false);
							log("User nicht gefunden");
							emailButton.setStyleName("check_notFound");
							emailButton.setText("X");
//							emailButton.setEnabled(false);
						}

						@Override
						public void onSuccess(User result) {
							log("User"+result);
							loadPanel.setVisible(false);
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
					emailButton.setText("Geprüft");	
					emailButton.setEnabled(true);
				}
			});
			
			cancelButton.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
				 contactToDisplay = null;
				 RootPanel.get("Details").clear();
				}
				
			});
			
			addButton.addClickHandler(new ClickHandler() {
				
				@Override 
				public void onClick(ClickEvent event) {
					
					Property p = new Property();					
					
					p.setId(addElement.getSelectedIndex()+2);
					
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
			
			editButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					loadPanel.setVisible(true);
					for(CheckBox cb: cbv) {
						cb.setValue(false, false);
					}
					if(sharedWithUser.getSelectedIndex()>=0) {
					String mail= sharedWithUser.getSelectedItemText();
					log("Mail gefunden: "+mail);
					contactSystemAdmin.getUserBygMail(mail, new AsyncCallback<User>() {

						@Override
						public void onFailure(Throwable caught) {
							loadPanel.setVisible(false);
							log("Nutzer Abruf fehlgeschlagen"+caught);
						}

						@Override
						public void onSuccess(final User userResult) {
							log("User abgerufen:"+userResult);

							contactSystemAdmin.getAllPVFromContactSharedWithUser(contactToDisplay, userResult, new AsyncCallback<Vector<PropertyValue>>(){
						
								@Override
								public void onFailure(Throwable caught) {
									loadPanel.setVisible(false);
									log("Teilhaberschaft Abruf fehlgeschlagen"+caught);									
								}

								@Override
								public void onSuccess(Vector<PropertyValue> result) {
									loadPanel.setVisible(false);
									for(PropertyValue pv: result) {	
												for(CheckBox cb: cbv) {
														if(Integer.parseInt(cb.getTitle())==pv.getBoId()){	
															cb.setValue(true, true); //Anzeigen triggern
														}												
											}											
										
									}
								}
								
							});			
						}
						
					});
				}else if(addElement.getSelectedIndex()==-1){
					Window.alert("Bitte wählen Sie zuerst einen Kontakt Teilhaber "
							+ "vor der Bearbeitung dessen Teilhaberschaft aus.");
				}
				}
					
			});


		}
		
		public void setSelected(Contact contact) {
						
			addElement.clear();
			sharedWithUser.clear();
			
			tbv.clear();
			cbv.clear();
			ft.clear();
			log("MyUser: "+ myUser);
//			myUser = new User();
//			myUser.setGoogleID(170); //TODO: später über AppEngine angebunden
			
			if(contact!=null) {
				this.contactToDisplay = contact;
				contactSystemAdmin.getContactById(contact.getBoId(), new AsyncCallback<Contact>() {
				
					@Override
					public void onFailure(Throwable caught) {
						log("Kontakt Callback fehlgeschlagen");
					}

					@Override
					public void onSuccess(Contact result) {
						log("############ Callback Kontakt Pvs: "+ result.getPropertyValues());
						contactToDisplay= result;	
						
						labelAddElement.setVisible(true);
						labelShare.setVisible(true);
						editButton.setVisible(true);
						
						email.setVisible(true);
						emailButton.setVisible(true);
						
						addElement.setVisible(true);
						addButton.setVisible(true);
						
						int row = 0;				
						//Flextable befüllen
						for(PropertyValue pv : result.getPropertyValues()){
							log("PropertyValue" +pv);
							Label label = new Label();
							CheckBox cb = new CheckBox();
							TextBox tb = new TextBox();
							
							label.setTitle(pv.getProperty().getId()+"");
							label.setText(pv.getProperty().getDescription());
							cb.setTitle(pv.getBoId()+"");
							tb.setTitle(pv.getBoId()+"");
							tb.setText(pv.getValue());
						
							cbv.add(cb);
							tbv.add(tb);
										
							ft.setWidget(row, 0, label);
							ft.setWidget(row, 1, tb);
							ft.setWidget(row, 2, cb);
							
							log("Table row:" + row);
							row++;
						}
						
						if(myUser.getGoogleID()!=result.getOwner().getGoogleID()) {
							log("Kontakt Besitzer anzeigen:" +result.getOwner().getGMail());
							labelReceivedFrom.setVisible(true);
							labelReceivedFrom.setText("Geteilt von: "+result.getOwner().getGMail());
						}else if(myUser.getGoogleID()==result.getOwner().getGoogleID()){	
							log("My User "+myUser.getGoogleID());
							labelSharedWith.setVisible(true);
							sharedWithUser.setVisible(true);
							contactSystemAdmin.getAllParticipationsByBusinessObject(contactToDisplay, new AsyncCallback<Vector<Participation>>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									loadPanel.setVisible(false);
									Window.alert("Teilhaberschaften konnten nicht abgerufen werden.");
								}

								@Override
								public void onSuccess(Vector<Participation> result) {
									loadPanel.setVisible(false);
									int i = 0;
									for(Participation part: result) {
										if(part.getParticipant().getGoogleID()==myUser.getGoogleID()) {
											result.remove(part);
										}else {
											sharedWithUser.addItem(part.getParticipant().getGMail());										
											log("Teilhaber:"+part.getParticipant().getGMail());
										}  
										i++;								
									}
								}						
							});					
						}
					}					
				});	
				
				cLabel.setText("Kontakt Id: " + contactToDisplay.getBoId());			

				if(contactToDisplay.isShared_status()) {
					contactStatus.setText("Status: Geteilt");
				} else { 
					contactStatus.setText("Status: Nicht geteilt");
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
						 log("###########################ListBox Element: "+p.getId());
						 addElement.addItem(p.getDescription());
						 addElement.setTitle(p.getId()+"");
						 }
					 }					
				}
				});
			
			vp.remove(ePanel);
			vp.add(btnPanel);
							
			} else if(contact==null) {
				contact = new Contact();
				contactToDisplay=contact;
				log("Neues Kontakt" + contact);
				
				cLabel.setText("Kontakt Id: " + 0);			
				contactStatus.setText("Status: Noch nicht erstellt");	
				
				//Teilen Funktionen
				labelShare.setVisible(false);
				email.setVisible(false);
				emailButton.setVisible(false);
				
				labelAddElement.setVisible(false);
				addElement.setVisible(false);
				addButton.setVisible(false);
				
				labelSharedWith.setVisible(false);
				sharedWithUser.setVisible(false);
				editButton.setVisible(false);
				
				contactSystemAdmin.getAllProperties(new AsyncCallback<Vector<Property>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						loadPanel.setVisible(false);
						Window.alert("Eigenschaften konnten nicht abgerufen");						
					}

					@Override
					public void onSuccess(Vector<Property> result) {	
						loadPanel.setVisible(false);
						log("Kontakt Properties: " + result);
						
						int row = 0;
						//Flextable befüllen						
						for(Property p : result){
							log("PropertyValue" +p);
							Label label = new Label();
							TextBox tb = new TextBox();
							
							label.setTitle("Neu:"+p.getId());
							label.setText(p.getDescription());
							tb.setTitle("Neu:"+p.getId());
							tb.setText("");
							
							tbv.add(tb);
										
							ft.setWidget(row, 0, label);
							ft.setWidget(row, 1, tb);
							
							log("Table row:" + row);
							row++;
							
							ePanel.add(createButton);
							ePanel.add(cancelButton);
							
							vp.remove(btnPanel);
							vp.add(ePanel);
							
							RootPanel.get("Details").clear();
							RootPanel.get("Details").add(vp);
						}	
					}					
				});	
				
			}
		}

		private class SaveCallback implements AsyncCallback<Contact> {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				loadPanel.setVisible(false);
				Window.alert("Speichern ist fehlgeschlagen.");
			}

			@Override
			public void onSuccess(Contact result) {
				loadPanel.setVisible(false);
				log("####### Der Kontakt wurde gespeichert: "+result);
				RootPanel.get("Details").clear();
				setSelected(result);
//				tvm.updateBusinessObject(result); 
				RootPanel.get("Details").add(vp);
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
				loadPanel.setVisible(false);
				Window.alert("Kontakt konnte nicht geteilt werden. :(");						
			}

			@Override
			public void onSuccess(Participation result) {
				loadPanel.setVisible(false);
				log("Neue Teilhaberschaft: " + result);	
//				tvm.updateBusinessObject(result.getReferencedObject());
				Window.alert("Der Kontakt wurde geteilt." + "\n" + result.getReferencedObject().getBoId());
//			    tvm.updateBusinessObject(contactToDisplay);
			}				
		}
				
		/*
		 * TreeViewModel setter
		 */
		
		void setTree(CellTreeViewModel tvm) {
			this.tvm = tvm;
		}
		
		/*
		 * LoadPanel setter
		 */
		void setLoad(PopupPanel load) {
			this.loadPanel = load;
		}
		
		/*
		 * Eigenen User setzen
		 */
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