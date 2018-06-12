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
			emailButton.setStyleName("check");	
			shareButton.setStyleName("share");	
					
			
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
							Window.alert("Die Eigenschaftsausprägungen des Kontakts konnten nicht gelöscht werden.");
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
			
			saveButton.addClickHandler(new ClickHandler() {
				Vector<PropertyValue> editResult = new Vector<PropertyValue>();
				Vector<PropertyValue> createResult = new Vector<PropertyValue>();			
			
				@Override
				public void onClick(ClickEvent event) {
					if(contactToDisplay!=null) {
						for(TextBox tb : tbv) {
							if(!tb.getText().isEmpty()) { //editieren
								for(PropertyValue pv: contactToDisplay.getPropertyValues()) {
									if(pv.getBoId()==Integer.parseInt(tb.getTitle())) {
										pv.setValue(tb.getText());
										editResult.add(pv);
									}
								}
							}else { //löschen
								for(PropertyValue pv: contactToDisplay.getPropertyValues()) {
									if(pv.getBoId()==Integer.parseInt(tb.getTitle())) {
										pv.setValue("");
//										editResult.add(pv);
									}
								}
							}							
						}
						contactToDisplay.setPropertyValues(editResult);
						contactSystemAdmin.editContact(contactToDisplay, new AsyncCallback<Contact>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								Window.alert("Speichern ist fehlgeschlagen.");
							}

							@Override
							public void onSuccess(Contact result) {
								// TODO Auto-generated method stub
								contactToDisplay = result;
								tvm.updateContact(result);
							}							
						});
					} 				
					if(contactToDisplay==null) {
						for(TextBox tb : tbv) {
							if(!tb.getText().isEmpty()) { //neu erstellen
								for(PropertyValue pv: contactToDisplay.getPropertyValues()) {
									if(pv.getBoId()==Integer.parseInt(tb.getTitle())) {
										pv.setValue(tb.getText());
										editResult.add(pv);
									}
								}
							}else { //nicht erstellen
								for(PropertyValue pv: contactToDisplay.getPropertyValues()) {
									if(pv.getBoId()==Integer.parseInt(tb.getTitle())) {
										pv.setValue("");
//										editResult.add(pv);
									}
								}
							}
						}
					} 
					contactToDisplay.setPropertyValues(createResult);
					contactSystemAdmin.createContact(contactToDisplay, new AsyncCallback<Contact>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							Window.alert("Speichern ist fehlgeschlagen.");
						}

						@Override
						public void onSuccess(Contact result) {
							// TODO Auto-generated method stub
							contactToDisplay = result;
							tvm.updateContact(result);
						}						
					});
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
					
				}
			});
			
			cancelButton.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					//TODO: Zurück setzen
//					setSelected(null);
//					RootPanel.get("Details").remove(vp);
				}
				
			});
			
			addButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Property p = new Property();
					p.setId(Integer.parseInt(addElement.getTitle()));
					p.setDescription(addElement.getSelectedItemText());
					PropertyValue pv = new PropertyValue();
					pv.setBo_Id(0);
					pv.setProperty(p);
					pv.setContact(contactToDisplay);
					pv.setOwner(contactToDisplay.getOwner());
					pv.setValue("");
					contactToDisplay.addPropertyValue(pv);
					
					contactSystemAdmin.editContact(contactToDisplay, new AsyncCallback<Contact>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub							
						}

						@Override
						public void onSuccess(Contact result) {
							contactToDisplay = result;
							
						}
						
					});
					
					Label label = new Label(addElement.getSelectedItemText());
					TextBox tb = new TextBox();
					CheckBox cb = new CheckBox();	
					
					tb.setTitle("Neu: "+p.getId());
					cb.setTitle("Neu: "+p.getId());
					
					tbv.add(tb);
					cbv.add(cb);
					
					int row = ft.getRowCount()+1;
					
					ft.setWidget(row, 0, label);
					ft.setWidget(row, 1, tb);
					ft.setWidget(row, 2, cb);
				}
				
			});
			
			
		    /*
			 * Panel für Anordnung der Button
			 */
//			//Geteilt mit
//			labelSharedWith
//			sharedWithUser
//			//Geteilt von
//			labelReceivedFrom
//			receivedFrom
			
			btnPanel.add(deleteButton);
			btnPanel.add(saveButton);
			btnPanel.add(shareButton);			
			
			
			ePanel.add(email);
			ePanel.add(emailButton);
			
			addPanel.add(addElement);
			addPanel.add(addButton);
			
			vp.add(cLabel);
			vp.add(contactStatus);
			vp.add(ft);
			vp.add(ePanel);
			vp.add(addPanel);
			vp.add(btnPanel);
			
			RootPanel.get("Details").add(vp);
		}
		
		
		public void setSelected(Contact contact) {
			
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
				log("Kontakt" + contact);
				
				final Vector<Property> ppv = new Vector <Property>();
				contactSystemAdmin.getAllProperties(new AsyncCallback<Vector<Property>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						Window.alert("Eigenschaften konnten nicht abgerufen");						
					}

					@Override
					public void onSuccess(Vector<Property> result) {
						ppv.addAll(result);					
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
					
					label.setTitle("Neu: "+p.getId());
					label.setText("Neu: "+p.getId());
					cb.setTitle("Neu: "+p.getId());
					tb.setTitle("Neu: "+p.getId());
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
