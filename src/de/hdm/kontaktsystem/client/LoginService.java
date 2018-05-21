package de.hdm.kontaktsystem.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.kontaktsystem.shared.bo.User;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	public User login(String requestUri);
}