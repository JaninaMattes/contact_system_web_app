package de.hdm.kontaktsystem.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.kontaktsystem.shared.UserData;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	public UserData login(String requestUri);
}