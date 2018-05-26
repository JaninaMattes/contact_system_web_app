package test;

import de.hdm.kontaktsystem.server.db.ContactMapper;
import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.Property;

public class PropertyMapperTestEinzeln {


	private final static PropertyMapper pMapper = PropertyMapper.propertyMapper(); 
	private final static UserMapper uMapper = UserMapper.userMapper();
	private final static PropertyValueMapper pvMapper = PropertyValueMapper.propertyValueMapper();
	private final static ContactMapper cMapper = ContactMapper.contactMapper();
	
	// Gültige IDs zu Testen
	private final static double vUID = 666;
	private final static int vCID = 19;
	private final static int vCLID = 3;
	private final static int vPID = 1; // = Sternzeichen
	private final static int vPVID = 20; // = Janina
	
	
	public static void main(String args[]) {	
		
		Property prop = new Property();
		prop.setId(vPID);
		//prop.setDescription("Sternzeichen");
		
		Property p = new Property();
		p.setId(12);
		p.setDescription("Telefonnummer");
		
		// #### insert ####
		pMapper.insert(p);		
		// #### find all ####
		System.out.println(pMapper.findAll());
		// #### find all ####
		for (int i = 1; i<= 9; i++) System.out.println(pMapper.findBy(i));
		// #### find by id ####
		System.out.println(pMapper.findBy(vPID));
		// #### find by prop object ####
		System.out.println(pMapper.findBy(prop));
		// #### find by description ####
		System.out.println(pMapper.findBy("Name"));
		
		
	}
		
}
