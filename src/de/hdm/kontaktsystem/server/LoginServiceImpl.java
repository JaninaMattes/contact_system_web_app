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
			
			
			double id = Double.parseDouble(user.getUserId());
			//long idl = Long.parseLong((user.getUserId()));

			System.out.println(user.getUserId() + "\t -> Google ID");
			System.out.println(Integer.MAX_VALUE + "\t \t -> Max Integer");
			System.out.println(Long.MAX_VALUE + "\t -> Max Long");
			//System.out.println(idl);
			System.out.println(Double.MAX_VALUE + "\t -> Max Double");
			System.out.println(id + "\t -> Google ID as Double");
			
			System.out.println("\n\n\n");
			
			
			System.out.println("Create User: " + user.getUserId() + " -> " + id);
			
			u.setGoogleID(id);
			u.setGMail(user.getEmail());
			System.out.println(u);
			System.out.println(user);
			UserMapper.userMapper().insertUser(u);;
			
			data.setLoggedIn(true);
			data.setEmailAddress(user.getEmail());
			data.setNickname(user.getNickname());
			data.setLogoutUrl(userService.createLogoutURL("/TestLogut"));
			System.out.println(data.getLoginUrl());
		} else {
			data.setLoggedIn(false);
			data.setLoginUrl(userService.createLoginURL("/TestLogin"));
		}
		
		return data;
		
	}
}
