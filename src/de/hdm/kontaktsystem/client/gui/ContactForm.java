package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
		ListBox receivedFrom = new ListBox();
		
		TextBox email = new TextBox();
		
		
		//Statische Anzeige
		Label label = new Label("Kontakt:");
		Label labelName = new Label("Name:");
		Label labelNickName = new Label("Nick-Name:");
		Label labelFirma = new Label("Firma:");
		Label labelTeleNr = new Label("Telefonnummer:");
		Label labelMobilNr = new Label("Mobilnummer:");
		Label labelEmail = new Label("Email:");
		Label labelGeburtsdatum = new Label("Geburtsdatum:");
		Label labelAdresse = new Label("Adresse:");
						
		Button cancelButton = new Button("Abbrechen");
		Button createButton = new Button("Erstellen");
				
		CheckBox checkBox1 = new CheckBox();
		CheckBox checkBox2 = new CheckBox();
		CheckBox checkBox3 = new CheckBox();
		CheckBox checkBox4 = new CheckBox();
		CheckBox checkBox5 = new CheckBox();
		CheckBox checkBox6 = new CheckBox();
		CheckBox checkBox7 = new CheckBox();
		CheckBox checkBox8 = new CheckBox();
		
		TextBox textBoxName = new TextBox();
		TextBox textBoxNickName = new TextBox();
		TextBox textBoxFirma = new TextBox();
		TextBox textBoxTelefonnummer = new TextBox();
		TextBox textBoxMobilnummer = new TextBox();
		TextBox textBoxEmail = new TextBox();
		TextBox textBoxGeburtsdatum = new TextBox();
		TextBox textBoxAdresse = new TextBox();				
		
	
		
		/**
		 * Startpunkt ist die onLoad() Methode
		 */
		public void  onLoad() {
			super.onLoad();

			final User user = new User();

			VerticalPanel vp  = new VerticalPanel();
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
					setSelected(null);
					RootPanel.get("Details").remove(vp);
				}
				
			});
			
			addButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Property p = new Property();
					p.setId(Integer.parseInt(addElement.getTitle()));
					p.setDescription(addElement.getSelectedItemText());
					PropertyValue pv = new PropertyValue();
					pv.setBo_Id(1);//TODO neue Id vergeben
					pv.setProperty(p);
					pv.setContact(contactToDisplay);
					pv.setOwner(myUser);
					pv.setValue("");
					contactSystemAdmin.createPropertyValue(pv, new AsyncCallback<PropertyValue>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub							
						}

						@Override
						public void onSuccess(PropertyValue result) {
							contactToDisplay.addPropertyValue(result);
						}
						
					});
					
					Label label = new Label(addElement.getSelectedItemText());
					TextBox tb = new TextBox();
					CheckBox cb = new CheckBox();	
					
					int row = ft.getRowCount()+1;
					
					ft.setWidget(row, 0, label);
					ft.setWidget(row, 1, tb);
					ft.setWidget(row, 2, cb);
				}
				
			});
			
			
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
			
			vp.add(cLabel);
			vp.add(contactStatus);
			vp.add(ft);
			vp.add(ePanel);
			vp.add(addPanel);
			vp.add(btnPanel);
			
			RootPanel.get("Details").add(vp);
		}
		
		
		public void setSelected(Contact contact) {
			log("Kontakt" + contact);
			if(contact.getBoId()!=0 & contact!=null) {
				this.contactToDisplay = contact;
				
				cLabel.setText("Kontakt ID: " + contact.getBoId());			

				if(contact.isShared_status()) {
					contactStatus.setText("Status: Geteilt");
//					this.setUpCheckBoxValues(contact);
				} else { 
					contactStatus.setText("Status: Nicht geteilt");
				}	
				
				Boolean isChecked = new Boolean(false);
				
				//Flextable befüllen
				for(PropertyValue pv : contact.getPropertyValues()){
					Label label = new Label();
					CheckBox cb = new CheckBox();
					TextBox tb = new TextBox();
					
					label.setTitle(pv.getBoId()+"");
					label.setText(pv.getProperty().getDescription());
					cb.setTitle(pv.getBoId()+"");
					tb.setTitle(pv.getBoId()+"");
					tb.setText(pv.getValue());
					
					cbv.add(cb);
					tbv.add(tb);
					
					int row = 0;			
					ft.setWidget(row, 0, label);
					ft.setWidget(row, 1, tb);
					ft.setWidget(row, 2, cb);
					
//					ft.getFlexCellFormatter().setColSpan(1, 0, 3);
					log("Table row:" + row);
					row++;
				}
			//Elemente füllen
			contactSystemAdmin.getAllProperties(new AsyncCallback<Vector<Property>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub					
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
			
			 addElement.setVisibleItemCount(contact.getPropertyValues().size());
				
			} else {
				this.contactToDisplay = null;				
				
				Grid contactGrid = new Grid(12, 3);
				
				contactGrid.setWidget(0, 0, label);
				contactGrid.setWidget(0, 1, contactStatus);
				contactGrid.setWidget(0, 2, isShared);
				
				contactGrid.setWidget(1, 0, labelName);
				contactGrid.setWidget(1, 1, textBoxName);
				contactGrid.setWidget(1, 2, checkBox1);
						
				contactGrid.setWidget(2, 0, labelNickName);
				contactGrid.setWidget(2, 1, textBoxNickName);
				contactGrid.setWidget(2, 2, checkBox2);
						
				contactGrid.setWidget(3, 0, labelFirma);
				contactGrid.setWidget(3, 1, textBoxFirma);
				contactGrid.setWidget(3, 2, checkBox3);
						
				contactGrid.setWidget(4, 0, labelTeleNr);
				contactGrid.setWidget(4, 1, textBoxTelefonnummer);
				contactGrid.setWidget(4, 2, checkBox4);
				
				contactGrid.setWidget(5, 0, labelMobilNr);
				contactGrid.setWidget(5, 1, textBoxMobilnummer);
				contactGrid.setWidget(5, 2, checkBox5);
				
				contactGrid.setWidget(6, 0, labelEmail);
				contactGrid.setWidget(6, 1, textBoxEmail);
				contactGrid.setWidget(6, 2, checkBox6);
				
				contactGrid.setWidget(7, 0, labelGeburtsdatum);
				contactGrid.setWidget(7, 1, textBoxGeburtsdatum);
				contactGrid.setWidget(7, 2, checkBox7);
				
				contactGrid.setWidget(8, 0, labelAdresse);
				contactGrid.setWidget(8, 1, textBoxAdresse);
				contactGrid.setWidget(8, 2, checkBox8);
								
				btnPanel.add(createButton);
				btnPanel.add(cancelButton);
				
				this.add(cLabel);
				this.add(contactGrid);
				this.add(btnPanel);
				
				RootPanel.get("Details").clear();
				RootPanel.get("Details").add(this);
				
				
				
			}
		}

		/**
		 * Eine neue Teilhaberschaft anlegen.
		 * @author janina
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
		
		native void log(String s)/*-{
		console.log(s);
		}-*/;
}
