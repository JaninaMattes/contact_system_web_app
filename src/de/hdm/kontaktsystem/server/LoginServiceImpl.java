package de.hdm.kontaktsystem.server;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.kontaktsystem.client.LoginService;
import de.hdm.kontaktsystem.server.db.UserMapper;
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
		System.out.println(requestUri);
		if (guser != null) {
			
			
			double id = Double.parseDouble(guser.getUserId());
			
			System.out.println("Create User: " + guser.getUserId() + " -> " + id);
			
			user.setGoogleID(id);
			user.setGMail(guser.getEmail());
			//user.setNickname(guser.getNickname()); // Not used
			
			user.setLoggedIn(false); // norm True
			System.out.println(userService.createLogoutURL(requestUri));
			user.setLogoutUrl(userService.createLogoutURL(requestUri));

			UserMapper.userMapper().insertUser(user);
		} else {
			
			user.setLoggedIn(false);
			System.out.println(userService.createLogoutURL(requestUri));
			user.setLoginUrl(userService.createLoginURL(requestUri));
			
		}
		
		System.out.println("Insert User to DB -> " + user);
		
		
		return user;
		
	}
}
