package de.hdm.kontaktsystem.client.gui;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.TreeViewModel.DefaultNodeInfo;
import com.google.gwt.view.client.TreeViewModel.NodeInfo;

import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;

/**
 * 
 * @author Katalin
 *
 */
public class ContactListTreeViewModel implements TreeViewModel {

	private ContactForm contactForm;
	
	private Contact selectedContact;
	
	private ContactSystemAdministrationAsync contactSystemAdmin = null;
	private ListDataProvider<Contact> contactDataProvider = null;
	
	
	/**
	 * Bildet Contacts auf eindeutige Zahlenobjekte ab, die als Schlüssel
	 * für Baumknoten dienen. Dadurch werden im Selektionsmodell alle Objekte
	 * mit derselben id selektiert, wenn eines davon selektiert wird.
	 * 
	*/
	private class ContactKeyProvider implements ProvidesKey<Contact> {
		
		public Integer getKey(Contact c) {
			if (c == null) {
				return null;
			}
			return new Integer(Integer.parseInt(String.valueOf(c.getBoId())));
		}
	};
	
	private ContactKeyProvider cKeyProvider = null;
	private SingleSelectionModel<Contact> selectionModel = null;
	
	/**
	 * Nested Class für die Reaktion auf Selektionsereignisse. Als Folge einer
	 * Baumknotenauswahl wird das Attribut "selectedContacts" gesetzt.
	 */
	private class SelectionChangeEventHandler implements
			SelectionChangeEvent.Handler {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			Contact selection = selectionModel.getSelectedObject();
			setSelectedContact((Contact) selection);
		}
	}
		/*
		 * Konstruktor
		 * Hier werden die lokalen Variablen initialisiert
		 */
		public ContactListTreeViewModel() {
			contactSystemAdmin = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();
			cKeyProvider = new ContactKeyProvider();
			selectionModel = new SingleSelectionModel<Contact>(cKeyProvider);
			selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());

		}
		void setContactForm(ContactForm cf) {
			this.contactForm = cf;
		}
		Contact getSelectedContact() {
			return selectedContact;
		}
		
		private void setSelectedContact(Contact contact) {
			// TODO Auto-generated method stub
			selectedContact = contact;
			contactForm.setSelected(contact);	
		}
		
	
	public <T> NodeInfo<?> getNodeInfo(T value) {

			if(value.equals("Root")) {				
				//Erzeugt eine ListDataProvider fuer Contact-Daten
				contactDataProvider = new ListDataProvider<Contact>();
				contactSystemAdmin.getAllContacts(new AsyncCallback<Vector<Contact>>() {
					
					public void onFailure(Throwable t) {
				}
			
				public void onSuccess(Vector<Contact> contacts) {
					for (Contact c : contacts) {
						contactDataProvider.getList().add(c);
					}
				}
			
			});
			

		}
			return new DefaultNodeInfo<Contact>(contactDataProvider, new ContactCell(), selectionModel, null);
	}
	

	@Override
	public boolean isLeaf(Object value) {
		return false;
	}

	public void removeContact(Contact c) {
		contactDataProvider.getList().remove(c);		
	}	
	 
	/*
	 * Kontakt hinzufügen.
	 */
	void addContact(Contact c) {
		contactDataProvider.getList().add(c);
		selectionModel.setSelected(c, true);
	}
	
	/*
	 * Kontakt updaten.
	 */	
	public void updateContact(Contact c) {
		List<Contact> contactList = contactDataProvider.getList();
		int i = 0;
		for (Contact con : contactList) {
			if (con.getBoId() == c.getBoId()) {
				contactList.set(i, c);
				break;
			} else {
				i++;
			}
		}
		contactDataProvider.refresh();
	}
	
	


}
