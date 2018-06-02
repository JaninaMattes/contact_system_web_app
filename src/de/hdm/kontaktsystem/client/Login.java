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

	private User userInfo = null;
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
				userInfo = result;
				if(userInfo.isLoggedIn()){
					loadDashboard();
				}else{
					loadLogin();
				}
				
			}
		});
		
		RootPanel.get("content").add(root);
	
	}
	
	
	private void loadLogin() {
		// Assemble login panel.
		
		signInLink.setHref(userInfo.getLoginUrl());
		signInLink.setStyleName("link");
		loginPanel.add(new HTML("<center>"));
		loginPanel.add(loginLabel);
		loginPanel.add(new HTML("<br /> <br /> "));
		loginPanel.add(signInLink);
		loginPanel.add(new HTML("</center>"));
		root.add(loginPanel);
	}
	
	
	private void loadDashboard(){
		root.add(new HTML("<h2> Dashboard </h2>"));
		root.add(new HTML("<br /> <br /> "));
		
		/**
		 * ####### Start #######
		 * Ab hier können die Komponenten in das Dashbord engebunden werden
		 */
		
		
		
		
		signOutLink.setHref(userInfo.getLogoutUrl());
		signOutLink.setStyleName("link");
		root.add(signOutLink);
	}
}
