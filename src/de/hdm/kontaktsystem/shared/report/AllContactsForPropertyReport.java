/**
 * 
 */
package de.hdm.kontaktsystem.shared.report;

import java.io.Serializable;

/**
 * Report, der alle Kontakte eines Users, die mit bestimmten Usern geteilt wurden, darstellt.
 * Die Klasse traegt keine weiteren Attribute- und Methoden-Implementierungen,
 * da alles Notwendige schon in den Superklassen vorliegt. Ihre Existenz ist 
 * dennoch wichtig, um bestimmte Typen von Reports deklarieren und mit ihnen 
 * objektorientiert umgehen zu koennen.
 * 
 * @see Report
 * @see de.hdm.thies.bankProjekt.shared.report.AllAccountsOfCustomerReport
 * @author Sandra Prestel
 *
 */
public class AllContactsForPropertyReport extends Report implements Serializable {

	private static final long serialVersionUID = 1L;

}
