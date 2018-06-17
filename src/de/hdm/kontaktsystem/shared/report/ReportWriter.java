package de.hdm.kontaktsystem.shared.report;

/**
* <p>
 * Diese Klasse wird benötigt, um auf dem Client die ihm vom Server zur
 * Verfügung gestellten <code>Report</code>-Objekte in ein lesbares
 * Format zu überführen.
 * </p>
 * <p>
 * Das Zielformat kann prinzipiell beliebig sein. Methoden zum Auslesen der in
 * das Zielformat überführten Information wird den Subklassen überlassen. In
 * dieser Klasse werden die Signaturen der Methoden deklariert, die für die
 * Prozessierung der Quellinformation zuständig sind.
 * </p>
 * 
 * @see de.hdm.thies.bankProjekt.shared.report.ReportWriter
 * @author Sandra
 *
 */
public abstract class ReportWriter {

	/**
	 * Übersetzen eines <code>Report</code> in das Zielformat
	 * 
	 * @param report der zu übersetzende Report
	 */
	public abstract void process(Report report);

	
}
