package de.hdm.kontaktsystem.shared.report;

import java.io.Serializable;
import java.util.Vector;


/**
 * Bildet Informationen zu einem einzelnen Kontakt ab. Dazu enthaelt
 * diese Klasse ein Tabelle, die auf zwei Hilfsklassen namens 
 * <code>Row</code> und <code>Column</code> zurueckgreift und die 
 * Eigenschaften des Kontakts abbildet, inkl. des Eigentuemers und des Status.
 * Weiterhin enthaelt die Klasse eine Liste (CompositeParagraph), die 
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
	 * Hinzufuegen einer Zeile zur Property-Tabelle
	 * @param propertyRow Die hinzuzufuegende Zeile
	 */
	public void addPropertyRow(Row propertyRow) {
		this.propertyTable.addElement(propertyRow);
	}
	
	/**
	 * Hinzufuegen eines Elements zur Teilhaber-Liste
	 * @param element Das hinzuzufuegende Element
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
	 * Zurueckgeben der Property-Tabelle
	 * @return Tabelle mit Informationen zum Objekt
	 */
	public Vector<Row> getPropertyRows() {
		return this.propertyTable;
	}
	
	
	/**
	 * Zurueckgeben der Teilhaber-Liste
	 * @return Teilhaber-Liste
	 */
	public CompositeParagraph getParticipantList() {
		return this.participantList;
	}
}
