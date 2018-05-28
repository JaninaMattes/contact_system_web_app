package de.hdm.kontaktsystem.server;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.kontaktsystem.client.LoginService;
import de.hdm.kontaktsystem.server.db.PropertyValueMapper;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.bo.Contact;
import de.hdm.kontaktsystem.shared.bo.PropertyValue;
import de.hdm.kontaktsystem.shared.bo.User;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public User login(String requestUri) {
		UserService userService = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User guser = userService.getCurrentUser();
		User user = new User();
		Contact own = new Contact();
		PropertyValue name = new PropertyValue();
		PropertyValue email = new PropertyValue();
		System.out.println("Test: "+requestUri);
		
		if (guser != null) {			
			
			double id = Double.parseDouble(guser.getUserId());			
			user.setGoogleID(id);
			user.setGMail(guser.getEmail());
			//user.setNickname(guser.getNickname()); // Not used
			
			user.setLoggedIn(true); // norm True
			System.out.println(userService.createLogoutURL(requestUri));
			user.setLogoutUrl(userService.createLogoutURL(requestUri));
			if(UserMapper.userMapper().findById(id) == null){
				System.out.println("Create new User: " + user);
				name.setValue("My Contact"); //force initial name
				email.setValue(user.getGMail());
			    own.setBo_Id(1); //updated in db
				own.setOwner(user);
				own.setName(name);
				own.addPropertyValue(email);
				//PropertyValueMapper.propertyValueMapper().insert(name);
				//PropertyValueMapper.propertyValueMapper().insert(email);
				//UserMapper.userMapper().insert(user, own);
			}else{
				System.out.println("Login User: " + guser.getUserId() + " -> " + id);
			}
			
		} else {
			
			user.setLoggedIn(false);
			System.out.println(userService.createLogoutURL(requestUri));
			user.setLoginUrl(userService.createLoginURL(requestUri));
			
		}
			
		
		return user;
		
	}
}
