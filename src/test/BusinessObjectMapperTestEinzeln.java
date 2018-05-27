package test;

import java.util.Vector;

import de.hdm.kontaktsystem.server.db.BusinessObjectMapper;
import de.hdm.kontaktsystem.server.db.ContactListMapper;
import de.hdm.kontaktsystem.server.db.ContactMapper;
import de.hdm.kontaktsystem.server.db.ParticipationMapper;
import de.hdm.kontaktsystem.server.db.PropertyMapper;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.BusinessObject;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.User;
import de.hdm.kontaktsystem.server.db.*;

public class BusinessObjectMapperTestEinzeln {
	
	private final static BusinessObjectMapper boMapper = BusinessObjectMapper.businessObjectMapper();	
	private final static ContactListMapper clMapper = ContactListMapper.contactListMapper();
	private final static ContactMapper cMapper = ContactMapper.contactMapper();
	private final static ParticipationMapper partMapper = ParticipationMapper.participationMapper();
	private final static PropertyMapper pMapper = PropertyMapper.propertyMapper(); 
	private final static PropertyValueMapper pvMapper = PropertyValueMapper.propertyValueMapper();
	private final static UserMapper uMapper = UserMapper.userMapper();
	
	// Gültige IDs zu Testen
	private final static double vUID = 777;
	private final static double vUID1 = 666;
	private final static double vUID2 = 798019057881227.4;
	private final static int coID = 22;
	private final static int vCID = 19;
	private final static int vCLID = 3;
	private final static int vPID = 1; // = Sternzeichen
	private final static int vPVID = 20; // = Janina
	
	
	public static void main(String args[]) {
		
		/*
		System.out.println("\n ############ Test BO findBOById ################ \n");
		Vector <Integer> boId = boMapper.findAllBusinessObjectIDs();
		System.out.println(boId);
		
		System.out.println("\n ############ Test BO findBOById ################ \n");
		for(Integer i : boId) {
			BusinessObject bo = boMapper.findBusinessObjectByID(i);
		}		
		
		
		System.out.println("\n ############ Test BO findBOById ################ \n");
		Vector <Integer> uId = boMapper.findBusinessObjectIDsByUserID(vUID1);
		System.out.println(uId);
		
		System.out.println("\n ############ Test BO findBy() ################ \n");
		BusinessObject bo = boMapper.findBy(coID);
		System.out.println(bo);
		
		
		System.out.println("\n ############ Test BO setStatusFalse() ################ \n");
		boMapper.setStatusFalse(bo.getBo_Id());
		BusinessObject bo1 = boMapper.findBy(coID);
		System.out.println(bo1.isShared_status());
		
		System.out.println("\n ############ Test BO setStatusTrue() ################ \n");
		boMapper.setStatusTrue(bo.getBo_Id());
		BusinessObject bo2 = boMapper.findBy(coID);
		System.out.println(bo2.isShared_status());
		
		System.out.println("\n ############ Test BO update ################ \n");
		//bo2.setBo_Id(100010);
		boMapper.update(bo2);		
		
		System.out.println("\n ############ Test BO insert ################ \n");
		User u = uMapper.findById(vUID);
		BusinessObject b = new Contact();
		b.setBo_Id(10000);
		b.setOwner(u);
		boMapper.insert(b);
		
		System.out.println("\n ############ Test BO delete ################ \n");
		BusinessObject bo3 = boMapper.findBusinessObjectByID(23);
		boMapper.deleteBusinessObject(bo3);
		
		*/
	
		System.out.println("\n ############ Test BO delteByUser ################ \n");
		User u = uMapper.findById(vUID2);
		boMapper.deleteBusinessObjectByUser(u);
		
	}

}
