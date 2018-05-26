package de.hdm.kontaktsystem.client.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.TreeViewModel.DefaultNodeInfo;

import de.hdm.kontaktsystem.shared.ContactSystemAdministration;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.User;

public class MyParticipationsTreeViewModel implements TreeViewModel {

	private MyParticipationForm participationForm;
	
	private Participation selectedParticipation;
	
	//Zu Testzwecken ausgeblendet
//	private ContactSystemAdministrationAsync contactSystemAdmin = null;
	
	private ListDataProvider<Participation> participationDataProvider = null;
	
	
	/**
	 * Bildet Participations auf eindeutige Zahlenobjekte ab, die als Schlüssel
	 * für Baumknoten dienen. Dadurch werden im Selektionsmodell alle Objekte
	 * mit derselben id selektiert, wenn eines davon selektiert wird.
	 * Die ID wird aus den eindeutigen IDs des Teilhabers und des geteilten Objekts zusammengesetzt.
	 */
	private class ParticipationKeyProvider implements ProvidesKey<Participation> {
		
		public Integer getKey(Participation p) {
			if (p == null) {
				return null;
			}
			return new Integer(Integer.parseInt(String.valueOf(p.getParticipantID()+""+p.getReferenceID())));
		}
	};
	
	private ParticipationKeyProvider pKeyProvider = null;
	private SingleSelectionModel<Participation> selectionModel = null;
	
	/**
	 * Nested Class für die Reaktion auf Selektionsereignisse. Als Folge einer
	 * Baumknotenauswahl wird das Attribut "selectedParticipation" gesetzt.
	 */
	private class SelectionChangeEventHandler implements
			SelectionChangeEvent.Handler {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			Participation selection = selectionModel.getSelectedObject();
			setSelectedParticipation((Participation) selection);
		}
	}
	
	/*
	 * Im Konstruktor werden die für den Kunden- und Kontobaum wichtigen lokalen
	 * Variaben initialisiert.
	 */
	public MyParticipationsTreeViewModel() {
		//Zu Testzwecken ausgeblendet
//		contactSystemAdmin = de.hdm.kontaktsystem.client.ClientsideSettings.getContactAdministration();
		
		pKeyProvider = new ParticipationKeyProvider();
		selectionModel = new SingleSelectionModel<Participation>(pKeyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
	}

	void setParticipationForm(MyParticipationForm mpf) {
		this.participationForm = mpf;
	}
	

	Participation getSelectedParticipation() {
		return selectedParticipation;
	}

	void setSelectedParticipation(Participation p) {
		selectedParticipation = p;
		participationForm.setSelected(p);
	}

	void removeParticipation(Participation p) {
		participationDataProvider.getList().remove(p);
	}

	
	
	
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		if (value.equals("Root")) {
			// Erzeugen eines ListDataproviders für Customerdaten
			participationDataProvider = new ListDataProvider<Participation>();
			//All Participations oder nur die für den User??
			
			//Zu Testzwecken ausgeblendet
//			contactSystemVerwaltung.getAllParticipations(new AsyncCallback<Vector<Participation>>() {
//						@Override
//						public void onFailure(Throwable t) {
//						}
//
//						@Override
//						public void onSuccess(Vector<Participation> participations) {
//							for (Participation p : participations) {
//								participationDataProvider.getList().add(p);
//							}
//						}
//					});
			
			//Testdaten
			Vector<Participation> participations = new Vector<Participation>();
			Contact c1 = new Contact();
			User u1 = new User();
			Participation p1 = new Participation(u1, c1);
			participations.add(p1);
			
			for (Participation p : participations) {
				participationDataProvider.getList().add(p);
			}
			//Ende Testdaten
			
			
			// Return a node info that pairs the data with a cell.
			return new DefaultNodeInfo<Participation>(participationDataProvider,
					new MyParticipationCell(), selectionModel, null);
		}
		return null;
		
	}

	@Override
	public boolean isLeaf(Object value) {
		return false;
	}



}
