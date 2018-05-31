package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;

/**
 * 
 * @author Katalin
 * Formular für die Darstellung und Anzeige eines Kontaktes
 * 
 */

public class ContactForm extends VerticalPanel{
	
	ContactSystemAdministrationAsync contactSystemAdmin = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();
	Contact contactToDisplay = null;
	ContactListTreeViewModel ctvm = null;
	
	/**
	 * Instanziieren der Widgets
	 */
	private TextBox textBoxName = new TextBox();
	private TextBox textBoxNickName = new TextBox();
	private TextBox textBoxFirma = new TextBox();
	private TextBox textBoxTelefonnummer = new TextBox();
	private TextBox textBoxMobilnummer = new TextBox();
	private TextBox textBoxEmail = new TextBox();
	private TextBox textBoxGeburtsdatum = new TextBox();
	private TextBox textBoxAdresse = new TextBox();
	
	private Label label = new Label("Kontakt:");
	private Label labelName = new Label("Name:");
	private Label labelNickName = new Label("Nick-Name:");
	private Label labelFirma = new Label("Firma:");
	private Label labelTeleNr = new Label("Telefonnummer:");
	private Label labelMobilNr = new Label("Mobilnummer:");
	private Label labelEmail = new Label("Email:");
	private Label labelGeburtsdatum = new Label("Geburtsdatum:");
	private Label labelAdresse = new Label("Adresse:");
	
	private Label labelShare = new Label("Teilen mit: ");
	private Label contactStatus = new Label("");
		
	private Button deleteButton = new Button("Kontakt löschen");
	private Button saveButton = new Button("Kontakt speichern");
	
	private CheckBox checkBox1 = new CheckBox();
	private CheckBox checkBox2 = new CheckBox();
	private CheckBox checkBox3 = new CheckBox();
	private CheckBox checkBox4 = new CheckBox();
	private CheckBox checkBox5 = new CheckBox();
	private CheckBox checkBox6 = new CheckBox();
	private CheckBox checkBox7 = new CheckBox();
	private CheckBox checkBox8 = new CheckBox();
	
	private ListBox shareUser = new ListBox();
	
	/**
	 * Instanziieren der Panels
	 */
	
	/**
	 * HauptPanel
	 */
	private VerticalPanel vp = new VerticalPanel();
	
	/**
	 * Panel für Anordnung der Button
	 */
	private HorizontalPanel btnPanel = new HorizontalPanel();
	
	/**
	 * Startpunkt
	 */
	
	public void  onLoad() {
		super.onLoad();
		
		/**
		 * Anordnung der einzelnen Inhalte für ContactForm.
		 * 
		 */
		Grid contactGrid = new Grid(9, 3);
		
		contactGrid.setWidget(0, 0, label);
		contactGrid.setWidget(0, 1, contactStatus);
		
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
		
		
		Grid shareGrid = new Grid (2, 1);
		shareGrid.setWidget(0, 0, labelShare);
		shareGrid.setWidget(0, 1, shareUser);
		
		deleteButton.setPixelSize(110, 30);
		saveButton.setPixelSize(110, 30);
		 
		btnPanel.add(deleteButton);
		btnPanel.add(saveButton);
		
		/*
		 * CheckBoxen für das Teilen einzelner Elemente einer ContactForm 
		 * per Default auf "false" setzen.
		 */
		checkBox1.setEnabled(false);
	    checkBox2.setEnabled(false);
	    checkBox3.setEnabled(false);
	    checkBox4.setEnabled(false);
	    checkBox5.setEnabled(false);
	    checkBox6.setEnabled(false);
	    checkBox7.setEnabled(false);
	    checkBox8.setEnabled(false);
	    
	    /*
	     * ClickHandler Verbindung zu CheckBoxen
	     */
	    
	    checkBox1.addClickHandler(new ClickHandler() {
	      PropertyValue pv = null;
	      
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactName = textBoxName.getText();
	    	  if(contactName.equals("")) Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  else pv = contactToDisplay.getName();
	    	  
	      }
	    });
	    
	    
	    checkBox2.addClickHandler(new ClickHandler() {
	      PropertyValue pv = null;
	    	
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactNickName = textBoxName.getText();
	    	  if(contactNickName.equals("")) Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  //else pv = getValue()
	      }
	    });
	    	  
	    checkBox3.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactFirma = textBoxFirma.getText();
	    	  if(contactFirma.equals("")) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });

	    checkBox4.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactPhone = textBoxTelefonnummer.getText();
	    	  if(contactPhone.equals("")) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	    
	    checkBox5.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactMobile = textBoxMobilnummer.getText();
	    	  if(contactMobile.equals("")) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	    
	    checkBox6.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactEmail = textBoxEmail.getText();
	    	  if(contactEmail.equals("")) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	 
	    checkBox7.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactGebuDatum = textBoxGeburtsdatum.getText();
	    	  if(contactGebuDatum.equals("")) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	    
	    checkBox8.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  String contactAdresse = textBoxAdresse.getText();
	    	  if(contactAdresse.equals("")) {
	    		  Window.alert("Das gewählte Feld enthält keinen Wert.");
	    	  }
	      }
	    });
	

	     
		/**
		 * Die TextBox um den Kontakt einzugeben, das Label und das Panel für die Buttons werden 
		 * über das Grid Widget untereinander auf dem VerticalPanel angeordnet.
		 * 
		 */		
	    
		vp.add(label);
		vp.add(contactGrid);
		vp.add(shareGrid);
		vp.add(btnPanel);
				
	}		
		
	  /**
	   * Methode um die Informationen aus den Checkboxen als PropertyValue
	   * einzusammeln und als Vector zur weiteren Verarbeitung durch den 
	   * entsprechenden Button Aufruf in der Menü Leiste zurück zugeben. 
	   * 
	   * @return Vector <PropertyValue>
	   */
	
	   public Vector <PropertyValue> getCheckedValues() {
		   Vector <PropertyValue> result = null;
		   Vector <PropertyValue> pvList = null;
		   
		   PropertyValue pv = null;
		   
		   if(contactToDisplay!=null && contactToDisplay.isShared_status() == true) {
			   if(checkBox1.isChecked()) {
				  //pvList = 
				 // pv = new PropertyValue();
			   }
		   }
		   return result;
	   }
	   
	   
		/**
		 * Wenn der anzuzeigende Kontakt gesetzt bzw. gelöscht wird, werden die
		 * zugehörenden Textfelder mit den Informationen aus dem Kontaktobjekt
		 * gefüllt bzw. gelöscht.
		*/
		
		public void setSelected(Contact c) {
			
			if (c != null) {
				contactToDisplay = c;		
				if(c.isShared_status()) contactStatus.setText("Geteilt");
				label.setText("Kontakt: " + c.getBoId());
				
				Vector <PropertyValue> pv = new Vector <PropertyValue>();
				pv = contactToDisplay.getPropertyValues();
				
				for(PropertyValue p : pv) {	
					
				if(p.getProperty().getId() == 1) textBoxName.setText(p.getValue()); 
				if(p.getProperty().getId() == 2) textBoxNickName.setText(p.getValue());
				if(p.getProperty().getId() == 3) textBoxFirma.setText(p.getValue());
				if(p.getProperty().getId() == 4) textBoxTelefonnummer.setText(p.getValue());
				if(p.getProperty().getId() == 5) textBoxMobilnummer.setText(p.getValue());
				if(p.getProperty().getId() == 6) textBoxEmail.setText(p.getValue());
				if(p.getProperty().getId() == 7) textBoxGeburtsdatum.setText(p.getValue());
				if(p.getProperty().getId() == 8) textBoxAdresse.setText(p.getValue());				
			}
				
			} else {
				contactStatus.setText("Nicht geteilt");
				label.setText("Kontakt: ");
				textBoxName.setText("");
				textBoxNickName.setText("");
				textBoxFirma.setText("");
				textBoxTelefonnummer.setText("");
				textBoxMobilnummer.setText("");
				textBoxEmail.setText("");
				textBoxGeburtsdatum.setText("");
				textBoxAdresse.setText("");
				
			}
		}
		
		
			/*
			 *  catvm setter
			 */
			
			void setCatvm(ContactListTreeViewModel ctvm) {
				this.ctvm = ctvm;
			}
			

}