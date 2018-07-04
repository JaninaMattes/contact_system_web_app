package de.hdm.kontaktsystem.client.gui;

import java.util.HashMap;
import java.util.Vector;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.mysql.jdbc.log.Log;

import de.hdm.kontaktsystem.client.ClientsideSettings;
import de.hdm.kontaktsystem.shared.ContactSystemAdministrationAsync;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.ContactList;

public class CellTreeViewModel implements TreeViewModel {

	private ContactSystemAdministrationAsync csa = ClientsideSettings.getContactAdministration();
	private ListDataProvider<BusinessObject> dataProvider = null;
	private BusinessObject selectedContactContactlist;
	private ContactListForm clForm;
	private ContactForm cForm;
	private BoKeyProvider boKey = null;
	private SingleSelectionModel<BusinessObject> selectionModel;
	private Vector<BusinessObject> serverData = null;
	private HashMap<Integer, ListDataProvider<BusinessObject>> dpMap = new HashMap<Integer, ListDataProvider<BusinessObject>>();
	
	/**
	 * Gibt eindeutigen Schluessel f�r das BusinessObject zur�ck.
	 * @author Marco
	 *
	 */
	
	
	
	private class BoKeyProvider implements ProvidesKey<BusinessObject> {

		@Override
		public Integer getKey(BusinessObject item) {
			if (item != null) {
				return item.getBoId();
			}
			return null;
		}
	}

	private class SelectionChangeEventHandler implements SelectionChangeEvent.Handler {
		
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			
			setSelectedContactContactlist(selectionModel.getSelectedObject());
			log("Change Selection: "+event.toDebugString());
			
			
			
		}
		
	}
	
	public CellTreeViewModel() {
		boKey = new BoKeyProvider();
		selectionModel = new SingleSelectionModel<BusinessObject>(boKey);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
		
	}
	

	public BusinessObject getSelectedContactContactlist() {
		return selectedContactContactlist;
	}

	
	/**
	 * Im Setter wird abgefragt, ob das BusinessObject ein Kontakt oder eine Kontaktliste ist.
	 * Diese wird dann entsprechend der Form gesetzt.
	 * @param sccl
	 */
	
	public void setSelectedContactContactlist(BusinessObject sccl) {
		this.selectedContactContactlist = sccl;
		log("Selected: "+ sccl);
		if(sccl != null){
			RootPanel.get("Details").clear();
			if(sccl instanceof ContactList) {
				clForm.setSelected((ContactList) sccl);
				log("Update clForm");
				RootPanel.get("Details").add(clForm);
			}
			else if (sccl instanceof Contact) {
				cForm.setSelected((Contact) sccl);
				log("Update cForm");
				RootPanel.get("Details").add(cForm);
			}
		}
	}

	public void restSelection(){
		log("Reset");
		RootPanel.get("Details").clear();
		selectionModel.clear();
	}



	public ContactListForm getClForm() {
		return clForm;
	}





	public void setClForm(ContactListForm clForm) {
		this.clForm = clForm;
	}





	public ContactForm getCForm() {
		return cForm;
	}





	public void setCForm(ContactForm cForm) {
		this.cForm = cForm;
	}

	/*
	 * Update Data in Dataprovider
	 */
	public void updateData(Vector<BusinessObject> bov){
		log("Update Data");
		dpMap.get(0).getList().clear();
		dataProvider = null;
		for (BusinessObject bo : bov) {
			dpMap.get(0).getList().add(bo);
		}
		dpMap.get(0).refresh();
	}
	
	
	public void updateBO(BusinessObject bo){
		log("Update Leef");		
		for(Integer i : dpMap.keySet()){
			if(dpMap.get(i).getList().contains(bo)){
				int index = 0;
				for(BusinessObject listElement : dpMap.get(0).getList()){
					if(listElement.equals(bo)) break;
					index++;
				}
				dpMap.get(i).getList().set(index, bo);
				dpMap.get(i).refresh();
			}
		}
			
	}
	
	/**
	 * AddBusinessObjekt Methode f�r den AddButton im CellTree
	 * Die Methode muss erst pr�fen, ob das BusinessObject ein Kontakt,
	 * oder eine Kontaktliste ist.
	 * @param bo
	 */
	
	public void addToRoot(BusinessObject bo){
		
		log("Add to root "+bo);
		dpMap.get(0).getList().add(bo);
		dpMap.get(0).refresh();
	}
	
	public void addToLeef(int key, BusinessObject bo){
		
		log("Add to Leef");
		dpMap.get(key).getList().add(bo);
		dpMap.get(key).refresh();
	}
	
	/**
	 * RemoveBusinessObjekt Methode f�r den RemoveButton im CellTree
	 * Die Methode muss erst pr�fen, ob das BusinessObject ein Kontakt,
	 * oder eine Kontaktliste ist.
	 * @param bo
	 */
	
	public void removeBO(BusinessObject bo){
		log("Delete from Tree");
		for(Integer i : dpMap.keySet()){
			dpMap.get(i).getList().remove(bo);
			dpMap.get(i).refresh();
		}
		
	}
	
	public void removeFromLeef(int key, BusinessObject bo){
		
		log("Delete from Leef");
		dpMap.get(key).getList().remove(bo);
		dpMap.get(key).refresh();	
	}
	


	/**
	 * Hier wird die Baumstruktur getestet.
	 */

	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		log("NodeInfo: " + value.getClass());
		dataProvider = new ListDataProvider<BusinessObject>();

		if (value instanceof Vector) {
			log("NodeInfo: is Vector" );
			dpMap.put(0, dataProvider); // Speichert RootListe damit die UpdateData methode die Daten Überschreiben kann;
			Vector v = (Vector) value;
			Vector<BusinessObject> bov = v;	
			
				for (BusinessObject bo : bov) {
					
					dataProvider.getList().add(bo);
				}
		}else 
		if(value instanceof ContactList){
			log("Open List: "+ ((ContactList) value).getName());
			dpMap.put(((ContactList) value).getBoId(), dataProvider);
			this.csa.getContactsFromList(((ContactList) value), new AsyncCallback<Vector<Contact>>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					log("Kontaktsuche Fehlerhaft");
				}

				@Override
				public void onSuccess(Vector<Contact> result) {
					
					for(Contact c : result){
						dataProvider.getList().add(c);
					}
				}
				
			});
		}
		

		return new DefaultNodeInfo<BusinessObject>(dataProvider, new DataCell(), selectionModel, null);
	}

	@Override
	public boolean isLeaf(Object value) {
		// log(value.toString());
		// Anzahl der Ebenen
		return (value instanceof Contact); // value.toString().length() > 8;
	}

	native void log(String s)/*-{
								console.log(s);
								}-*/;

	







	

}

class DataCell extends AbstractCell<BusinessObject> {

	@Override
	public void render(Context context, BusinessObject value, SafeHtmlBuilder sb) {
		if (value != null) {
		if (value instanceof ContactList) {
			
			sb.appendHtmlConstant("<p class='Cell'>" + "<img src='/images/group.png' height='22px' width='22px' >  " + ((ContactList) value).getName() + "</p>");

		} else if (value instanceof Contact) {
			sb.appendHtmlConstant("<p class='Cell'>" + "<img src='/images/person.png' height='22px' width='22px'>  " + ((Contact) value).getName().getValue() + "</p>");

		} else {

			sb.appendHtmlConstant("<p class='Cell'>" + value.getBoId() + "</p>");

		}
		}
	}
	native void log(String s)/*-{
	console.log(s);
	}-*/;
}