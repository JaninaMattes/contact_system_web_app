package de.hdm.kontaktsystem.gui;

import com.google.gwt.view.client.TreeViewModel;

import de.hdm.kontaktsystem.shared.bo.Contact;

public class ContactsTreeViewModel implements TreeViewModel {

	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLeaf(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

}
