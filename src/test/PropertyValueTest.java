package test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.hdm.kontaktsystem.server.db.DBConnection;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;

public class PropertyValueTest {

	public static void main(String args[]) {
			
		PropertyValue pv = new PropertyValue("Bussenstraße");
		PropertyValueMapper.propertyValueMapper().insert(pv);
		
		System.out.println(
				PropertyValueMapper.propertyValueMapper().findByValue("Bussenstrasse", pv)
				);
		
	}
	
}

