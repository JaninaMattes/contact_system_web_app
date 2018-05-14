package test;

import java.util.Random;

import de.hdm.kontaktsystem.server.db.ParticipationMapper;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.Participation;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class ParticipationTest {
	
	public static void main(String args[]) {
		
		//Generate User as participant
		User participant = new User();
		Random rng = new Random();
		participant.setGMail("mail@gmail.com");
		participant.setGoogleID(rng.nextInt(1000)+1);
		//insert participant in Table
		UserMapper.userMapper().insertUser(participant);
		
		//Test-Ausgabe des eingefügten Users
		System.out.println(
			UserMapper.userMapper().getUserByEmail("mail@gmail.com").getGMail()
			);
		
//		//Generate PropertyValue (as referenced BusinessObject)
//		PropertyValue shared = new PropertyValue("Musterweg");
//		//insert into table
//		PropertyValueMapper.propertyValueMapper().insert(shared);
//		
//		//Test-Aufgabe des eingefügten Objekts
//		System.out.println(
//			PropertyValueMapper.propertyValueMapper().findByValue("Musterweg", shared)
//			);
		
		
		//Generate Participation
		Participation participation = new Participation(participant.getGoogleID(), 3);
		//insert Participation into table
		ParticipationMapper.participationMapper().insertParticipation(participation);
		
		//Ausgabe der Participation
		System.out.println(
				ParticipationMapper.participationMapper().getParticipationsByParticipant(participant)
				);
		
	}

}
