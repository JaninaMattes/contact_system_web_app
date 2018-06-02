package de.hdm.kontaktsystem.client.gui;

import java.util.Vector;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.ContactList;

public class TreeViewModelTest implements TreeViewModel {
	
	private ContactSystemAdministrationAsync csa = ClientsideSettings.getContactAdministration();

	/**
	 * Hier wird die Baumstruktur getestet.
	 */
	
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		//log("NodeInfo: "+ value);
		ListDataProvider<ContactList> dataProvider = new ListDataProvider<ContactList>();
		
		csa.getAllContactLists(new AsyncCallback<Vector<ContactList>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				log("Keine Listen gefunden");
			}

			@Override
			public void onSuccess(Vector<ContactList> result) {
				// TODO Auto-generated method stub
				log("Es wurden "+ result.size() +" Listen gefunden");
				for (ContactList cl : result) {
					//dataProvider.getList().add(cl);
				}
			}
			
		});
		
		
		//Anzahl der Elemente
		for (int i = 0; i < 2; i++) {
			ContactList cl = new ContactList();
			cl.setName("Liste " + i);

			dataProvider.getList().add(cl);
			log(cl.getName()); 
		}
		return new DefaultNodeInfo<ContactList>(dataProvider, new DataCell());
	}

	@Override
	public boolean isLeaf(Object value) {
		log(value.toString());
		// Anzahl der Ebenen
		return value.toString().length() > 8;
	}

	

	native void log(String s)/*-{
		console.log(s);
	}-*/;
	
	
}


class DataCell extends AbstractCell<ContactList>{

	@Override
	public void render(Context context, ContactList value, SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("<p>" + value.getName() + "</p>" );
		
		
	}
}