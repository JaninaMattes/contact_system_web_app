package de.hdm.kontaktsystem.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.kontaktsystem.shared.bo.User;

public interface LoginServiceAsync {
	public void login(String requestUri, AsyncCallback<User> async);
}
