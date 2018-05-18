package de.hdm.kontaktsystem.server;

import com.google.appengine.api.users.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.kontaktsystem.client.LoginService;
import de.hdm.kontaktsystem.server.db.UserMapper;
import de.hdm.kontaktsystem.shared.UserData;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserData login(String requestUri) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		UserData data = new UserData();
		de.hdm.kontaktsystem.shared.bo.User u = new de.hdm.kontaktsystem.shared.bo.User();
		
		if (user != null) {
			int id = Integer.parseInt(user.getUserId().split("@")[0]);
			System.out.println("Create User");
			u.setGoogleID(id);
			u.setGMail(user.getEmail());
			
			UserMapper.userMapper().insertUser(u);
			
			data.setLoggedIn(true);
			data.setEmailAddress(user.getEmail());
			data.setNickname(user.getNickname());
			data.setLogoutUrl(userService.createLogoutURL("http://google.de"));
		} else {
			data.setLoggedIn(false);
			data.setLoginUrl(userService.createLoginURL("http://google.de"));
		}
		
		return data;
		
	}
}
