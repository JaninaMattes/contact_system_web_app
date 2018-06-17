package de.hdm.kontaktsystem.shared.report;

import java.io.Serializable;
import java.util.Vector;


/**
 * Bildet Informationen zu einem einzelnen Kontakt ab. Dazu enthält
 * diese Klasse ein Tabelle, die auf zwei Hilfsklassen namens 
 * <code>Row</code> und <code>Column</code> zurück greift und die 
 * Eigenschaften des Kontakts abbildet, inkl. des Eigentümers und des Status.
 * Weiterhin enthält die Klasse eine Liste (CompositeParagraph), die 
 * auf die Namen der Teilhaber verweist.
 * 
 * @author Sandra Prestel
 *
 */
public class SingleContact implements Serializable {

	private static final long serialVersionUID = 1L;
	
	Vector<Row> propertyTable = new Vector<Row>();	
	CompositeParagraph participantList = new CompositeParagraph();
	
	/**
	 * Hinzufügen einer Zeile zur Property-Tabelle
	 * @param propertyRow Die hinzuzufügende Zeile
	 */
	public void addPropertyRow(Row propertyRow) {
		this.propertyTable.addElement(propertyRow);
	}
	
	/**
	 * Hinzufügen eines Elements zur Teilhaber-Liste
	 * @param element Das hinzuzufügende Element
	 */
	public void addElementToParticipantList(SimpleParagraph element) {
		this.participantList.addSubParagraph(element);
	}
	
	/**
	 * Entfernen einer Zeile von der Property-Tabelle
	 * @param propertyRow Die zu entfernende Zeile
	 */
	public void removePropertyRow(Row propertyRow) {
		this.propertyTable.remove(propertyRow);
	}
	
	/**
	 * Entfernen eines Elements von der Teilhaber-Liste
	 * @param element Das zu entfernende Element
	 */
	public void removeElementFromParticipantList(SimpleParagraph element) {
		this.participantList.removeSubParagraph(element);
	}
	
	/**
	 * Zurückgeben der Property-Tabelle
	 * @return Tabelle mit Informationen zum Objekt
	 */
	public Vector<Row> getPropertyRows() {
		return this.propertyTable;
	}
	
	
	/**
	 * Zurückgeben der Teilhaber-Liste
	 * @return Teilhaber-Liste
	 */
	public CompositeParagraph getParticipantList() {
		return this.participantList;
	}
}
