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
			
			user.setGoogleID(id);
			user.setGMail(guser.getEmail());
			//user.setNickname(guser.getNickname()); // Not used
			
			user.setLoggedIn(true); // norm True
			System.out.println(userService.createLogoutURL(requestUri));
			user.setLogoutUrl(userService.createLogoutURL(requestUri));
			if(UserMapper.userMapper().findById(id) == null){
				System.out.println("Create new User: " + user);
				UserMapper.userMapper().insert(user);
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
