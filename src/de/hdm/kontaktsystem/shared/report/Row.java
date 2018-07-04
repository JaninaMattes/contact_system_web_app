package de.hdm.kontaktsystem.shared.report;

import java.io.Serializable;
import java.util.Vector;

/**
 * Zeile einer Tabelle eines <code>Report</code>-Objekts. <code>Row</code>
 * -Objekte implementieren das <code>Serializable</code>-Interface und koennen
 * dadurch als Kopie z.B. vom Server an den Client uebertragen werden.
 * 
 * @see Report
 * @see Column
 * @see de.hdm.thies.bankProjekt.shared.report.Row
 * @author Sandra Prestel
 *
 */
public class Row implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Speicherplatz fuer die Spalten der Zeile.
	 */
	private Vector<Column> columns = new Vector<Column>();
	
	
	/**
	 * Hinzufuegen einer Spalte.
	 * 
	 * @param column das Spaltenobjekt
	 */
	public void addColumn(Column column) {
		this.columns.addElement(column);
	}

	/**
	 * Entfernen einer Spalte
	 * 
	 * @param column das zu entfernende Spaltenobjekt
	 */
	public void removeColumn(Column column) {
		this.columns.removeElement(column);
	}

	/**
	 * Auslesen aller Spalten.
	 * 
	 * @return <code>Vector</code>-Objekt mit allen Spalten
	 */
	public Vector<Column> getColumns() {
		return this.columns;
	}

	/**
	 * Auslesen der Anzahl saemtlicher Spalten.
	 * 
	 * @return int Anzahl der Spalten
	 */
	public int getNumColumns() {
		return this.columns.size();
	}
	
	/**
	 * Auslesen eines einzelnen Spalten-Objekts.
	 * 
	 * @param i der Index der auszulesenden Spalte (0 <= i < n), mit n = Anzahl
	 *          der Spalten.
	 * @return Das gewuenschte Spaltenobjekt.
	 */
	public Column getColumnAt(int i) {
		return this.columns.elementAt(i);
	}
}
