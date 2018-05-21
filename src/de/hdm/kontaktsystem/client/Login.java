package de.hdm.kontaktsystem.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.shared.bo.User;


public class Login implements EntryPoint {

	private User loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private VerticalPanel root = new VerticalPanel();
	private Label loginLabel = new Label(
	"Melden Sie sich mit Ihrem Google Konto an um auf das Kontaktsystem zuzugreifen ");
	private Anchor signInLink = new Anchor("Login mit Google");
	private Anchor signOutLink = new Anchor("Logout");
	
	
	@Override
	public void onModuleLoad() {
		// Check login status using login service.
		
		root.add(new HTML("<h1> Willkommen im Kontaktsystem </h1>"));
		
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<User>() {
			public void onFailure(Throwable error) {
				
			
			}
			public void onSuccess(User result) {
				loginInfo = result;
				loadLogin();
			}
		});
		
		RootPanel.get("content").add(root);
	
	}
	
	
	private void loadLogin() {
		// Assemble login panel.
		
		signInLink.setHref(loginInfo.getLoginUrl());
		signInLink.setStyleName("link");
		signOutLink.setHref(loginInfo.getLogoutUrl());
		signOutLink.setStyleName("link");
		loginPanel.add(loginLabel);
		loginPanel.add(new HTML("<br /> <br /> "));
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(signInLink);
		hp.add(signOutLink);
		loginPanel.add(hp);
		root.add(loginPanel);
		}
}
