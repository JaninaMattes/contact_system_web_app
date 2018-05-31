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
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.User;

public class ParticipationMapperTestEinzeln {
	
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
		System.out.println("\n ############ Test Participation findAll ################ \n");
		Vector<Participation> r = new Vector <Participation>();
		r = partMapper.findAllParticipations();
		for(Participation p : r) {
			System.out.println(p.toString());		
			}	
		
		
		System.out.println("\n ############ Test Participation findBy ################ \n");
		BusinessObject bo = boMapper.findBusinessObjectByID(11);
		Vector <Participation> z = new Vector<Participation>();		
		z = partMapper.findParticipationsByBusinessObject(bo);
			for(Participation a : z) {
				System.out.println("bo-id: " + a.getReferencedObject().getBo_Id());
				System.out.println(a.toString()); }			
		
		
		System.out.println("\n ############ Test Participation findByOwner ################ \n");
		Vector <Participation> y = new Vector<Participation>();		
		User u = uMapper.findById(vUID1);
		y = partMapper.findParticipationsByOwner(u);
		
		
		System.out.println("\n ############ Test Participation findByParticipant ################ \n");
		Vector <Participation> part = new Vector<Participation>();		
		User us = uMapper.findById(vUID1);
		part = partMapper.findParticipationsByParticipant(us);		
		
		
		System.out.println("\n ############ Test Participation insert ################ \n");
		Participation p1 = new Participation();
		User u = uMapper.findById(vUID1);
		p1.setParticipant(u);
		BusinessObject b = boMapper.findBy(44);
		p1.setReference(b);
		partMapper.insertParticipation(p1);				
				
	
		System.out.println("\n ############ Test Participation findAll ################ \n");
		Vector <Participation> part = new Vector<Participation>();		
		part = partMapper.findAllParticipations();
		for(Participation a : part) {
			System.out.println("bo-id: " + a.getReferencedObject().getBo_Id());
			System.out.println(a.toString()); }			
	
		System.out.println("\n ############ Test Participation delteByParticipant ################ \n");
		User u = uMapper.findById(vUID1);
		partMapper.deleteParticipationForParticipant(u);
				
		System.out.println("\n ############ Test Participation delteByOwner ################ \n");
		User u = uMapper.findById(vUID1);
		partMapper.deleteParticipationForOwner(u);
		*/
		
		System.out.println("\n ############ Test Participation deleteByBo ################ \n");		
		//BusinessObject b = boMapper.findBusinessObjectByID(44);
		//partMapper.deleteParticipationForBusinessObject(b);		
	
	}
}
		
		


