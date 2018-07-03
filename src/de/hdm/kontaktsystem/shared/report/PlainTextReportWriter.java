package de.hdm.kontaktsystem.shared.report;

import java.util.Vector;

/**
 * Ein <code>ReportWriter</code>, der Reports mittels Plain Text formatiert. Das
 * im Zielformat vorliegende Ergebnis wird in der Variable
 * <code>reportText</code> abgelegt und kann nach Aufruf der entsprechenden
 * Prozessierungsmethode mit <code>getReportText()</code> ausgelesen werden.
 * 
 * @see de.hdm.thies.bankProjekt.shared.report.PlainTextReportWriter
 * @author Sandra Prestel
 *
 */
public class PlainTextReportWriter extends ReportWriter {

	/**
	 * Diese Variable speichert das Ergebnis der Umwandlung im einfachen text-Format
	 */
	private String reportText = "";
	
	/**
	 * Zuruecksetzen der Variable <code>reportText</code>.
	 */
	public void resetReportText() {
		this.reportText = "";
	}
	
	
	/**
	 * Produzieren des Header-Textes
	 * @return Header in text-Form
	 */
	public String getHeader() {
		return "Report in TextForm \n";
	}
	
	/**
	 * Produzieren des Trailer-Textes
	 * @return Trailer in Text-Form
	 */
	public String getTrailer() {
		//Einfache Trennlinie, um das Ende des Reports zu markieren
		return "___________________________________________";
	}
	
	
	/**
	 * Verarbeiten des uebergebenen Reports und Ablage im Zielformat. Ein Auslesen
	 * des Ergebnisses kann spaeter mittels <code>getReportText()</code> erfolgen.
	 * 
	 * @param report Der zu verarbeitende Report
	 */
	@Override
	public void process(Report report) {
		
		//Ergebnis vorhergehender Prozessierungen loeschen
		this.resetReportText();
		
		/*
		 * Leeres <code>StringBuffer</code>-Objekt, in das sukzessive 
		 * die Ergebnisse geschrieben werden.
		 */
		StringBuffer result = new StringBuffer();
		
		/* Schrittweises Auslesen der Report-Bestandteile und uebersetzung in Text-Form */
		//Titel
		result.append("*** " + report.getTitle() + " ***\n\n");		
		//User-Daten
		result.append("User: " + report.getUserData().toString() + "\n");
		//Erstellungsdatum
		result.append("Erstellt am: " + report.getCreated() + "\n\n");
		
		//Kontakt-Elemente
		Vector<SingleContact> elements = report.getAllSingleContacts();
		for(SingleContact element : elements) {
			//Einfache Trennlinie, um die einzelnen Elemente voneinander abzutrennen
			result.append("___________________________________________");
			
			//Umwandeln der Eigenschafts-Tabelle in Text-Form
			Vector<Row> contactRows = element.getPropertyRows();
			for(Row row : contactRows) {
				for (int k = 0; k < row.getNumColumns(); k++) {
			        result.append(row.getColumnAt(k) + "\t ; \t");
			      }
			      result.append("\n");
			}
			
			//Umwandeln der Liste mit Teilhabern in Text-Form
			result.append("Teilhaber: " + "\n");
			CompositeParagraph participants = element.getParticipantList();
			result.append(participants);
		}
		
		result.append("\n");
		
		/*
		 * Umwandeln des <code>StringBuffer</code>-Objekts in einen String, der
		 * als Wert des Attributs reportText gesetzt wird. Dadurch wird es moeglich, 
		 * anschliessend das Ergebnis mittels getReportText() auszulesen.
		 */
		this.reportText = result.toString();
	}

	
	/**
	 * Auslesen des Ergebnisses der zuletzt aufgerufenen Prozessierungsmethode.
	 * 
	 * @return Ein String bestehend aus einfachem Text
	 */
	public String getReportText() {
		return this.getHeader() + this.reportText + this.getTrailer();
	}
	
}
