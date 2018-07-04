package de.hdm.kontaktsystem.shared.report;

/**
* <p>
 * Diese Klasse wird benoetigt, um auf dem Client die ihm vom Server zur
 * Verfuegung gestellten <code>Report</code>-Objekte in ein lesbares
 * Format zu ueberfuehren.
 * </p>
 * <p>
 * Das Zielformat kann prinzipiell beliebig sein. Methoden zum Auslesen der in
 * das Zielformat ueberfuehrten Information wird den Subklassen ueberlassen. In
 * dieser Klasse werden die Signaturen der Methoden deklariert, die fuer die
 * Prozessierung der Quellinformation zustaendig sind.
 * </p>
 * 
 * @see de.hdm.thies.bankProjekt.shared.report.ReportWriter
 * @author Sandra
 *
 */
public abstract class ReportWriter {

	/**
	 * uebersetzen eines <code>Report</code> in das Zielformat
	 * 
	 * @param report der zu uebersetzende Report
	 */
	public abstract void process(Report report);

	
}
