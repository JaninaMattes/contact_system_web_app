package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.TreeViewModel.DefaultNodeInfo;
import com.google.gwt.view.client.TreeViewModel.NodeInfo;


import de.hdm.kontaktsystem.shared.bo.Contact;

/**
 * 
 * @author Katalin
 *
 */
public class ContactsTreeViewModel implements TreeViewModel {

	private ContactForm contactForm;
	
	private Contact selectedContact;
	
	private ContactSystemAdministrationAsync contactSystemVerwaltung = null;
	private ListDataProvider<Contact> ContactDataProvider = null;
	
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
			return new Integer(Integer.parseInt(String.valueOf(c.getBo_Id())));
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
		public ContactsTreeViewModel() {
			contactSystemVerwaltung = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();
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
				ContactDataProvider = new ListDataProvider<Contact>();
				contactSystemVerwaltung.getAllContacts(new AsyncCallback<Vector<Contact>>() {
					public void onFailure(Throwable t) {
				}
			
				public void onSuccess(Vector<Contact> contacts) {
					for (Contact c : contacts) {
						ContactDataProvider.getList().add(c);
					}
				}
			
			});
			return new DefaultNodeInfo<Contact>(ContactDataProvider, new ContactCell(), selectionModel, null);

		}
	}
	

	@Override
	public boolean isLeaf(Object value) {
		return false;
	}

	public void removeContact(Contact contact) {
		ContactDataProvider.getList().remove(contact);
		
	}

}
