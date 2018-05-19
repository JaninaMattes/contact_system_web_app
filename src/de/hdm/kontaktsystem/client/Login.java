package de.hdm.kontaktsystem.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.shared.UserData;

public class Login implements EntryPoint {

	private UserData loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private VerticalPanel root = new VerticalPanel();
	private Label loginLabel = new Label(
	"Please sign in to your Google Account to access the StockWatcher application.");
	private Label Label = new Label("Hello :)");
	private Anchor signInLink = new Anchor("Sign In");
	
	
	@Override
	public void onModuleLoad() {
		// Check login status using login service.
		
		root.add(Label);
		
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<UserData>() {
			public void onFailure(Throwable error) {
				
			
			}
			public void onSuccess(UserData result) {
				loginInfo = result;
				loadLogin();
			}
		});
		
		RootPanel.get().add(root);
	
	}
	
	
	private void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		root.add(loginPanel);
		}
}
