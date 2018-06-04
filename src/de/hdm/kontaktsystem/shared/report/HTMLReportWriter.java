/**
 * 
 */
package de.hdm.kontaktsystem.shared.report;

import java.util.Vector;

/**
 * Ein <code>ReportWriter</code>, der Reports mittels HTML formatiert. Das im
 * Zielformat vorliegende Ergebnis wird in der Variable <code>reportText</code>
 * abgelegt und kann nach Aufruf der entsprechenden Prozessierungsmethode mit
 * <code>getReportText()</code> ausgelesen werden.
 * 
 * @see de.hdm.thies.bankProjekt.shared.report.HTMLReportWriter
 * @author Sandra
 *
 */
public class HTMLReportWriter extends ReportWriter {

	/**
	 * Diese Variable speichert das Ergebnis der Umwandlung als HTML-Text
	 */
	private String reportText = "";
	
	/**
	 * Zurücksetzen der Variable <code>reportText</code>.
	 */
	public void resetReportText() {
	    this.reportText = "";
	}
	  
	  
	/**
	 * Umwandeln eines <code>Paragraph</code>-Objekts in HTML.
	 * @param paragraph Der umzuwandelnde Paragraph
	 * @return HTML-Text
	 */
	public String paragraph2HTML(Paragraph paragraph) {
		if (paragraph instanceof CompositeParagraph) {
			return this.paragraph2HTML((CompositeParagraph) paragraph);
	    }
	    else {
	    	return this.paragraph2HTML((SimpleParagraph) paragraph);
	    }
	}
	
	/**
	 * Umwandeln eines <code>CompositeParagraph</code>-Objekts in HTML.
	 * @param paragraph Der umzuwandelnde CompositeParagraph
	 * @return HTML-Text
	 */
	public String paragraph2HTML(CompositeParagraph paragraph) {
	    StringBuffer result = new StringBuffer();
	    for (int i = 0; i < paragraph.getNumParagraphs(); i++) {
	    	result.append("<p>" + paragraph.getParagraphAt(i) + "</p>");
	    }
	    return result.toString();
	}
	
	/**
	 * Umwandeln eines <code>SimpleParagraph</code>-Objekts in HTML.
	 * @param paragraph Der umzuwandelnde SimpleParagraph
	 * @return HTML-Text
	 */
	public String paragraph2HTML(SimpleParagraph paragraph) {
		return "<p>" + paragraph.toString() + "</p>";
	}
	
	
	/**
	 * HTML-Header-Text produzieren.
	 * @return HTML-Text
	 */
	public String getHeader() {
		StringBuffer result = new StringBuffer();
	    result.append("<html><head><title>Report</title></head><body>");
	    return result.toString();
	}
	
	/**
	 * HTML-Trailer-Text produzieren.
	 * @return HTML-Text
	 */
	public String getTrailer() {
		return "</body></html>";
	}
	
	
	/**
	 * Verarbeiten des übergebenen Reports und Ablage im Zielformat. Ein Auslesen
	 * des Ergebnisses kann später mittels <code>getReportText()</code> erfolgen.
	 * 
	 * @param report Der zu verarbeitende Report
	 */
	@Override
	public void process(Report report) {
		
		//Ergebnis vorhergehender Prozessierungen löschen
		this.resetReportText();
		
		/*
		 * Leeres <code>StringBuffer</code>-Objekt, in das sukzessive 
		 * die Ergebnisse geschrieben werden.
		 */
		StringBuffer result = new StringBuffer();
		
		/*
		 * Schrittweises Auslesen der Report-Bestandteile und Übersetzung in HTML-Text
		 */
		//Tabelle mit allgemeinen Daten:
		//Titel
	    result.append("<H1>" + report.getTitle() + "</H1>");
	    //Header-Daten
	    result.append("<table style=\"width:400px;border:1px solid silver\"><tr>");
	    result.append("<td valign=\"top\"><b>" + paragraph2HTML(report.getHeaderData())
	        + "</b></td>");
	    //User-Daten
	    result.append("<td valign=\"top\">" + paragraph2HTML(report.getUserData()) + "</td>");
		//Erstellungsdatum
	    result.append("</tr><tr><td></td><td>" + report.getCreated().toString()
	            + "</td></tr></table>");
	    
	    //Kontakt-Elemente: je eine Tabelle pro Element
	    Vector<SingleContact> elements = report.getAllSingleContacts();
		for(SingleContact element : elements) {
			result.append("<table style=\"width:400px\">");
			
			//Umwandeln der Eigenschafts-Tabelle in HTML-Form
			Vector<Row> contactRows = element.getPropertyRows();
			result.append("<tr><td>");
			
			result.append("<table style=\"width:400px\">");

		    for (int i = 0; i < contactRows.size(); i++) {
		    	Row row = contactRows.elementAt(i);
		    	result.append("<tr>");
		    	for (int k = 0; k < row.getNumColumns(); k++) {
		    		if (i == 0) {
		    			result.append("<td style=\"background:silver;font-weight:bold\">" + row.getColumnAt(k)
		    			+ "</td>");
		    		}
		    		else {
		    			if (i > 1) {
		    				result.append("<td style=\"border-top:1px solid silver\">"
		    						+ row.getColumnAt(k) + "</td>");
		    			}
		    			else {
		    				result.append("<td valign=\"top\">" + row.getColumnAt(k) + "</td>");
		    			}
		    		}
		    	}
		    	result.append("</tr>");
		    }
		    result.append("</table>");
			
			result.append("</td></tr>");
			
			//Umwandeln der Liste mit Teilhabern in HTML
			result.append("<tr><td>Teilhaber: </td><td><ul>");
			Vector<SimpleParagraph> participants = element.getParticipantList().getSubParagraphs();
			for(SimpleParagraph participant : participants) {
				result.append("<li>" + participant.toString() + "</li>");
			}		
			result.append("</ul></td></tr>");
			
			result.append("</table>");
		}
		
		/*
		 * Umwandeln des <code>StringBuffer</code>-Objekts in einen String, der
		 * als Wert des Attributs reportText gesetzt wird. Dadurch wird es möglich, 
		 * anschließend das Ergebnis mittels getReportText() auszulesen.
		 */
		this.reportText = result.toString();		
	}

	/**
	 * Auslesen des Ergebnisses der zuletzt aufgerufenen Prozessierungsmethode.
	 * 
	 * @return Ein String im HTML-Format
	 */
	public String getReportText() {
		return this.getHeader() + this.reportText + this.getTrailer();
	}
	
}
