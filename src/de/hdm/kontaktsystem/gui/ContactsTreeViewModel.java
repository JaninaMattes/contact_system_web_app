package de.hdm.kontaktsystem.gui;

import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

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
	
	private ContactKeyProvider pKeyProvider = null;
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
		 */
		public ContactTreeViewModel() {
			contactSystemVerwaltung = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();
			pKeyProvider = new ContactKeyProvider();
			selectionModel = new SingleSelectionModel<Contact>(pKeyProvider);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLeaf(Object value) {
		return false;
	}

	public void removeContact(Contact contact) {
		ContactDataProvider.getList().remove(contact);
		
	}

}
