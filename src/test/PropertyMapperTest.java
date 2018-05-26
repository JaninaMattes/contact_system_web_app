package test;

import java.util.Vector;

import de.hdm.kontaktsystem.server.db.BusinessObjectMapper;
import de.hdm.kontaktsystem.server.db.ContactListMapper;
import de.hdm.kontaktsystem.server.db.ContactMapper;
import de.hdm.kontaktsystem.server.db.ParticipationMapper;
import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.Property;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;


public class PropertyMapperTest {
	
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
		
		Contact c = new Contact();	
		c.setBo_Id(3);	
		
		User u = new User();
		u.setGoogleID(126);
		c.setOwner(u);
		
		PropertyValue name = new PropertyValue();
		name.setBo_Id(8);
		name.setOwner(u);
		
		name.setValue("JaninaTestKontakt");
		name.setContact(c);	
		
		c.setName(name);
		
		System.out.println("\n ############ Test Contact insert ################ \n");
		cMapper.insertContact(c);
		System.out.println("\n ############ Test User FindByMail ################ \n");
		u = uMapper.findByEmail("NeuerUser@gmail.com");
		System.out.println(uMapper.findByEmail("NeuerUser@gmail.com"));
		
		Property p = new Property();
		p.setDescription("Arbeitgeber");
				
		Property p1 = new Property();
		p1 = pMapper.findBy("Name");
		System.out.println("\n ############ Test Prop FindBy String ################ \n");
		System.out.println(pMapper.findBy("Name"));
		
		name.setProp(p1);
		
		PropertyValue pv = new PropertyValue();
		pv.setBo_Id(6);
		pv.setOwner(u);
		pv.setProp(p);
		pv.setValue("Traumfabrik AG");
		pv.setContact(c);
		
		PropertyValue pv1 = new PropertyValue();
		pv1.setBo_Id(7);
		pv1.setOwner(u);
		pv1.setProp(p);
		pv1.setValue("Blüten GmbH");
		pv.setContact(c);
		
		p.setPropertyValue(pv);
		p.setPropertyValue(pv1);
		
	
		
		c.setPropertyValue(name);
						
		// Test insert method 
		System.out.println("\n ############ Test Prop ################ \n");
		pMapper.insert(p);
		
		// Test insert method 
		System.out.println("\n ############ Test PropVal ################ \n");
		pvMapper.insert(pv);
		pvMapper.insert(pv1);
		pvMapper.insert(name);
		
	}
}
	
