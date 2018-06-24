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
import com.google.gwt.user.client.ui.DialogBox;
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
		User editPartUser = null;
		
		// Lade overlay
		PopupPanel loadPanel;
		
		// Share DialogBox
		DialogBox shareDialog = new DialogBox();
		
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
				
		HorizontalPanel newPanel = new HorizontalPanel(); // Buttons zum anlegen eines Kontakts
		HorizontalPanel editPanel = new HorizontalPanel();// Buttons zum bearbeiten eines Kontakts
		HorizontalPanel btnPanel = new HorizontalPanel(); // Standart Buttons
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
		Button editButton = new Button("Bearbeiten");
		Button editPartButton = new Button("Bearbeiten");
		//Button cancelButton = new Button("Abbrechen"); // Teilen abbrechen
		Button okButton = new Button("Teilen");
		Button cancelNewButton = new Button("Abbrechen");
		Button cancelEditButton = new Button("Abbrechen");
		Button cancelShareButton = new Button("Abbrechen");
		Button createButton = new Button("Speichern");
		
		ListBox addElement = new ListBox();
		ListBox sharedWithUser = new ListBox();

		TextBox email = new TextBox();
								
		boolean edit = false;
		boolean editPart = false;
		
		/**
		 * Startpunkt ist die onLoad() Methode
		 */
		
		public void onLoad(){
//			vp.clear();
//			
//			super.onLoad();
//			this.add(vp);
			log("Kontakt Formular Laden");
			
		}
		
		public ContactForm() {
			
			
			final User user = new User();
			
			//CSS
			addButton.getElement().setId("addedit");	
			saveButton.getElement().setId("saveButton");
			editButton.getElement().setId("saveButton");
			shareButton.getElement().setId("shareButton");
			deleteButton.getElement().setId("deleteButton");
			cancelNewButton.getElement().setId("cancel");
			cancelEditButton.getElement().setId("cancel");
			cancelShareButton.getElement().setId("cancel");
			editPartButton.getElement().setId("addedit");
			
			shareDialog.setStyleName("shareDialog");
			gp.getElement().setId("grid-panel");
			
			labelAddElement.getElement().setId("labelfeldhinzu");
			
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
			
			gp.setWidget(1, 0, labelSharedWith);
			gp.setWidget(1, 1, sharedWithUser);	
			gp.setWidget(1, 2, editPartButton);
			
			gp.setWidget(2, 0, labelReceivedFrom);
			
			
			gpPanel.add(gp);
			
			email.getElement().setPropertyString("placeholder", "Email");
			VerticalPanel shareDialogvp = new VerticalPanel();
			HorizontalPanel shareDialoghp = new HorizontalPanel();
			shareDialogvp.add(labelShare);
			shareDialogvp.add(email);
			shareDialogvp.add(shareDialoghp);
			shareDialoghp.add(okButton);
			shareDialoghp.add(cancelShareButton);	
			shareDialog.add(shareDialogvp);
			shareDialog.center();
			shareDialog.setVisible(false);
			
			
			/*
			 * Standard ButtonPanel 
			 */
			
			btnPanel.add(deleteButton);
			btnPanel.add(editButton);
			btnPanel.add(shareButton);	
					
			/*
			 * ButtonPanelbeimanlegen eines neuen Kontakts
			 */
			newPanel.add(createButton);
			newPanel.add(cancelNewButton);
			
			/*
			 * ButtonPanel beim beabriten eines Kontakts
			 */
			editPanel.add(saveButton);
			editPanel.add(cancelEditButton);
			
			/*
			 * Zuordnung zum VP			
			 */
			vp.add(cLabel);
			vp.add(contactStatus);
			vp.add(ft);
			vp.add(gpPanel);
			vp.add(btnPanel);
			vp.add(shareDialog);
			
			
			this.add(vp);
			
			// Bearbeiten Button
			editButton.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					// Ändern der Standart Buttons
					for(TextBox tb : tbv){
						tb.setReadOnly(false);
					}
					labelAddElement.setVisible(true);
					addElement.setVisible(true);
					addButton.setVisible(true);
					edit = true;
					vp.remove(btnPanel);
					vp.add(editPanel);
				}
				
			});
			
			//Click-Handler
			shareButton.addClickHandler(new ClickHandler(){
				
				@Override
				public void onClick(ClickEvent event) {
					shareDialog.setVisible(true);
				}
				
			});
			okButton.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					loadPanel.setVisible(true);
					contactSystemAdmin.getUserBygMail(email.getText(), new AsyncCallback<User>(){
						@Override
						public void onFailure(Throwable caught) {
							loadPanel.setVisible(false);
							Window.alert("User wurde nicht gefunden");
						}
	
						@Override
						public void onSuccess(User result) {
							Participation part = new Participation();
							Contact c = contactToDisplay;
							boolean somethingChecked = false;
							// Überprüft ob eine Checkbox ausgewählt wurde, wenn nicht dann wird derKontaktebenso vollständig geteilt
							for(CheckBox cb : cbv){
								log("CB "+cb.getTitle() +" = "+cb.getValue());
								if(cb.getValue()){
									somethingChecked = true; 
									break;
								}
							}
							log("Something Checked? " + somethingChecked);
							
							
							if(!cbv.get(0).getValue() && somethingChecked){ 
								part.setShareAll(false);
								log("Kontakt nicht vollständig Geteilt");
								// Wenn nicht der Vollständige Kontakt geteilt wird, muss überprüft werden, was geteilt werden soll
								Vector<PropertyValue> pvv = new Vector<PropertyValue>();
								for(CheckBox cb: cbv){
									// Eine Ausprägung wird geteilt wenn die Checkbox True ist. Der Name (cbv[1]) wird immer geteilt
									if(cb.getValue() || cb.equals(cbv.get(1))){ 
										for(PropertyValue pv : c.getPropertyValues()){
											if(pv.getBoId() == Integer.parseInt(cb.getTitle())){
												pvv.add(pv);
												log("Share: " + pv.getValue() + " With " + email.getText());
											}
										}
									}
								}
								c.setPropertyValues(pvv);
							}else{
								// Wenn der Kontakt vollständig geteilt werden soll werden keine PropertyValues geteilt
								c.setPropertyValues(null);
								part.setShareAll(true);
							}
							part.setParticipant(result);
							part.setReference(c);
							log("### Share:" + part);
							shareDialog.setVisible(false);
							email.setText("");
							contactSystemAdmin.createParticipation(part, new CreateParticipationCallback());						
						}
					});
				}
			});
			
			deleteButton.addClickHandler(new ClickHandler(){
				Vector <PropertyValue> result = new Vector <PropertyValue>();
				@Override
				public void onClick(ClickEvent event) {
					
					// TODO Auto-generated method stub				
					if(Window.confirm("Bitte bestätigen Sie dass der Kontakt gelöscht werden soll.")) {	
						loadPanel.setVisible(true);
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
								tvm.removeFromRoot(result);
								tvm.removeFromLeef(result);
								RootPanel.get("Details").clear();
							}						
						});
					}					
				} 				
			});
			
			/*
			 * Save-Button ClickHandler 
			 */
			saveButton.addClickHandler(new ClickHandler() {
				
				
				@Override
				public void onClick(ClickEvent event) {
				
					loadPanel.setVisible(true);
					if(editPart){
						log("Teilhaberschaft bearbeiten");
						Contact c = contactToDisplay;
						Participation part = new Participation();
						
						if(!cbv.get(0).getValue()){
							Vector<PropertyValue> pvv = new Vector<PropertyValue>();
							for(CheckBox cb: cbv){
								// Eine Ausprägung wird geteilt wenn die Checkbox True ist. Der Name (cbv[1]) wird immer geteilt
								if(cb.getValue() || cb.equals(cbv.get(1))){ 
									for(PropertyValue pv : c.getPropertyValues()){
										if(pv.getBoId() == Integer.parseInt(cb.getTitle())){
											pvv.add(pv);
											log("EditShare: " + pv.getValue() + " With " + email.getText());
										}
									}
								}
							}
							c.setPropertyValues(pvv);
						}
						part.setReference(c);
						part.setShareAll(cbv.get(0).getValue());
						part.setParticipant(editPartUser);
						contactSystemAdmin.editParticpation(part, new AsyncCallback<Participation>(){

							@Override
							public void onFailure(Throwable caught) {
								loadPanel.setVisible(false);
								log("Teilhaberschaft kann nicht bearbeitet werden");
							}

							@Override
							public void onSuccess(Participation result) {
								loadPanel.setVisible(false);
								log("Teilhaberschaft wurde bearbeitet");
								
							}
							
						});
						
						// Speichern der Bearbeitetet Teilhaberschaften
						for(CheckBox cb: cbv){
							cb.setValue(false);
						}
						editPart = false;
					}else{
						// Speichern des bearbeiteten Kontakts
						Vector<PropertyValue>editResult = new Vector<PropertyValue>();
						if(contactToDisplay!=null) {
							log("Der Kontakt besteht bereits. "+tbv.size());
							for(TextBox tb: tbv) {
								log("Inhalt: "+tb);
								 if(tb.getText().isEmpty()&&Integer.parseInt(tb.getTitle())==1){ //1.0 verhindern dass Kontakt Name leer
										Window.alert("Das Feld Name darf nicht leer sein.");
										return;							
								 }
								 
								 log("Titel: "+tb.getTitle());
								 if(tb.getTitle().contains("Neu")) {
									 	String[]s=tb.getTitle().split(":");
										Property p = new Property();//1.1.1.1 Erzeuge neuesObjekt		
										p.setId(Integer.parseInt(s[1]));
										p.setDescription(s[0]);
										
										PropertyValue ppv = new PropertyValue();
										ppv.setContact(contactToDisplay);
										ppv.setOwner(contactToDisplay.getOwner());
										ppv.setProperty(p);
										ppv.setValue(tb.getText());
										editResult.add(ppv);
										log("Neuen Pv: "+ppv);
								 	}
								 else{
									 log("Update");
									 for(PropertyValue pv:contactToDisplay.getPropertyValues()) { //editieren + hinzufügen
											if(pv.getBoId()==Integer.parseInt(tb.getTitle())) {
												pv.setValue(tb.getText());
												editResult.add(pv);
												log("Editieren des Pv: "+pv);
											}
									   }									
								 }
	
												
							}
							contactToDisplay.setPropertyValues(editResult);
							log("Save: "+contactToDisplay);
							contactSystemAdmin.editContact(contactToDisplay, new SaveCallback());
						}
						
					}
					// Entfernen der Gui Elemente
					labelAddElement.setVisible(false);
					addElement.setVisible(false);
					addButton.setVisible(false);
					vp.remove(editPanel);
					vp.add(btnPanel);
				}
			});
			
			/**
			 * Create-Button ClickHandler
			 */
			createButton.addClickHandler(new ClickHandler() {
				

				@Override
				public void onClick(ClickEvent event) {
					Vector<PropertyValue>createResult = new Vector<PropertyValue>();
					loadPanel.setVisible(true);
					for(TextBox tb: tbv) {
						 if(tb.getText().isEmpty()&tb.getTitle().equals("Neu:1")) { //1.0 verhindern dass Kontakt Name leer
								Window.alert("Das Feld Name darf nicht leer sein.");
								return;
						 }
						 if(!tb.getText().isEmpty()) {
							 	Property p = new Property();//1.1.1.1 Erzeuge neuesObjekt		
							 	String[]s=tb.getTitle().split(":");
								p.setId(Integer.parseInt(s[1]));
								p.setDescription(s[0]);
								
								PropertyValue ppv = new PropertyValue();
								ppv.setContact(contactToDisplay);
								ppv.setOwner(contactToDisplay.getOwner());
								ppv.setProperty(p);
								ppv.setValue(tb.getText());
								createResult.add(ppv);
								log("Erzeugen eines neuen Pv: "+ppv);
						 }
					}
					
					contactToDisplay.setPropertyValues(createResult);
					contactSystemAdmin.createContact(contactToDisplay, new SaveCallback());					
				}
			});
			
			
			cancelNewButton.addClickHandler(new CancelClickHandler());
			cancelEditButton.addClickHandler(new CancelClickHandler());
			cancelShareButton.addClickHandler(new CancelClickHandler());
			
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
			
			editPartButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					loadPanel.setVisible(true);
					for(CheckBox cb: cbv) {
						cb.setValue(false);
					}
					if(sharedWithUser.getSelectedIndex()>=0) {
					String mail= sharedWithUser.getSelectedItemText();
					log("Mail gefunden: "+mail);
					contactSystemAdmin.getUserBygMail(mail, new AsyncCallback<User>() {

						@Override
						public void onFailure(Throwable caught) {
							loadPanel.setVisible(false);
							log("Nutzer Abruf fehlgeschlagen "+caught);
						}

						@Override
						public void onSuccess(final User userResult) {
							log("User abgerufen: "+userResult);
							editPartUser = userResult;
							
							contactSystemAdmin.getAllPVFromContactSharedWithUser(contactToDisplay, userResult, new AsyncCallback<Vector<PropertyValue>>(){
						
								@Override
								public void onFailure(Throwable caught) {
									loadPanel.setVisible(false);
									log("Teilhaberschaft Abruf fehlgeschlagen "+caught);									
								}

								@Override
								public void onSuccess(Vector<PropertyValue> result) {
									// Ändern der Buttons
									vp.remove(btnPanel);
									vp.add(editPanel);
									editPart = true;
									loadPanel.setVisible(false);
									if(result.size() > 0){
										for(PropertyValue pv: result) {
												for(CheckBox cb: cbv) {
														if(Integer.parseInt(cb.getTitle())==pv.getBoId()){	
															cb.setValue(true); //Anzeigen triggern
														}												
												}
										}
									}else{
										log("All Shared");
										for(CheckBox cb: cbv) {
											cb.setValue(true);
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
			
			labelReceivedFrom.setVisible(false);
			labelReceivedFrom.setText("");
			
			tbv.clear();
			cbv.clear();
			ft.clear();
			log("MyUser: "+ myUser);
			
			if(contact!=null) {
				
				//Teilen Funktionen einblenden
				labelShare.setVisible(false);
				email.setVisible(false);
				
				labelAddElement.setVisible(false);
				addElement.setVisible(false);
				addButton.setVisible(false);
				
				labelSharedWith.setVisible(false);
				sharedWithUser.setVisible(false);
				editPartButton.setVisible(false);
				
				// Eigensschaten hinzufügen standartmäßig nicht angezeigt
				labelAddElement.setVisible(false);
				addElement.setVisible(false);
				addButton.setVisible(false);
				
				
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
						
						
						labelShare.setVisible(true);
						
						
						email.setVisible(true);
						
//						labelAddElement.setVisible(true);
//						addElement.setVisible(true);
//						addButton.setVisible(true);
						
						int row = 0;
						Label label = new Label();
						CheckBox cb = new CheckBox();
						TextBox tb = new TextBox();
						
						cb.setText("All");
						cb.setTitle(0+"");
						cb.addClickHandler(new ClickHandler(){
							@Override
							public void onClick(ClickEvent event) {
								for(CheckBox cb : cbv) cb.setValue(cbv.get(0).getValue());
							}
						});
						cbv.add(cb);
						ft.setWidget(row, 2, cb);
						row ++;
						
						//Flextable befüllen
						for(PropertyValue pv : result.getPropertyValues()){
							
							label = new Label();
							cb = new CheckBox();
							tb = new TextBox();
//							log("PropertyValue" +pv);
//							Label label = new Label();
//							//Entfernen des prim�ren Stylename und ersetzen durch CSS-Namen
//							label.setStylePrimaryName(getElement(), "contactlabels");
//							CheckBox cb = new CheckBox();
//							//Entfernen des prim�ren Stylename und ersetzen durch CSS-Namen
//							cb.setStylePrimaryName(getElement(), "checkbox");
//							TextBox tb = new TextBox();
//							//Entfernen des prim�ren Stylename und ersetzen durch CSS-Namen
//							tb.setStylePrimaryName(getElement(), "textbox");
							
							label.setTitle(pv.getProperty().getId()+"");
							label.setText(pv.getProperty().getDescription());
							cb.setTitle(pv.getBoId()+"");
							tb.setTitle(pv.getBoId()+"");
							tb.setText(pv.getValue());
							
							// Textboxen können nicht bearbeitet werden
							tb.setReadOnly(true);
//							tb.setEnabled(false);
							// Ändert den zustand der ShareAllCheckBox (cbv[0])
							cb.addClickHandler(new ClickHandler(){
								@Override
								public void onClick(ClickEvent event) {
									cbv.get(0).setValue(false);
								}
							});
							
							cbv.add(cb);
							tbv.add(tb);
										
							ft.setWidget(row, 0, label);
							ft.setWidget(row, 1, tb);
							ft.setWidget(row, 2, cb);
							
							row++;
						}
						
						if(myUser.getGoogleID()!=result.getOwner().getGoogleID()) {
							log("Kontakt Besitzer anzeigen: " +result.getOwner().getGMail());
							labelReceivedFrom.setVisible(true);
							labelReceivedFrom.setText("Geteilt von: "+result.getOwner().getGMail());
						}else if(myUser.getGoogleID()==result.getOwner().getGoogleID()){	
							log("My User: "+myUser.getGoogleID());
							labelSharedWith.setVisible(true);
							sharedWithUser.setVisible(true);
							editPartButton.setVisible(true);
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
											log("Teilhaber: "+part.getParticipant().getGMail());
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
							 addElement.addItem(p.getDescription());
							 addElement.setTitle(p.getId()+"");
							 }
						 }					
					}
				});
				
			
				vp.remove(newPanel);
				vp.add(btnPanel);
							
			} else if(contact==null) {
				contact = new Contact();
				contactToDisplay=contact;
				log("Neuer Kontakt " + contact);
				
				cLabel.setText("Kontakt Id: " + 0);			
				contactStatus.setText("Status: Noch nicht erstellt");	
				
				//Teilen Funktionen
				labelShare.setVisible(false);
				email.setVisible(false);
				labelSharedWith.setVisible(false);
				sharedWithUser.setVisible(false);
				editPartButton.setVisible(false);
				
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
							Label label = new Label();
							TextBox tb = new TextBox();
							
							label.setTitle("Neu:"+p.getId());
							label.setText(p.getDescription());
							tb.setTitle("Neu:"+p.getId());
							tb.setText("");
							
							tbv.add(tb);
										
							ft.setWidget(row, 0, label);
							ft.setWidget(row, 1, tb);
							
							row++;
						}	
					}					
				});	
				vp.remove(btnPanel);
				vp.add(newPanel);
//				
//				RootPanel.get("Details").clear();
//				RootPanel.get("Details").add(vp);
				
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
				if(edit){
					tvm.updateRoot(result); 
					tvm.updateLeef(result); 
				}else{
					log("Add New Contact to Root");
					tvm.addToRoot(result);
				}
				tvm.setSelectedContactContactlist(result);
				// Update des TreeViewModels
				
				edit = false;
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
				sharedWithUser.addItem(result.getParticipant().getGMail());
				for(CheckBox cb : cbv){
					cb.setValue(false);
				}
				log("Neue Teilhaberschaft: " + result);	
				Window.alert("Der Kontakt wurde geteilt.");
			}				
		}
		
		// Cancel Clickhandler
		private class CancelClickHandler implements ClickHandler{

			@Override
			public void onClick(ClickEvent event) {
				
				if(shareDialog.isVisible()){
					shareDialog.setVisible(false);
				}else if(edit || editPart){
					
					edit = false;
					editPart = false;
					labelAddElement.setVisible(false);
					addElement.setVisible(false);
					addButton.setVisible(false);
					//CheckBox auf false setzten
					for(CheckBox cb: cbv){
						cb.setValue(false);
					}
					// Texboxen können nicht mehr bearbeitet werden
					for(TextBox tb : tbv){
						tb.setReadOnly(true);
					}
					
					vp.remove(editPanel);
					vp.add(btnPanel);
				}else{
					contactToDisplay = null;
					RootPanel.get("Details").clear();
				}
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