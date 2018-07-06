package de.hdm.kontaktsystem.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.kontaktsystem.shared.bo.User;

/**
 * Panel das dem User die Möglichkeit bietet, sich in dem KontaktSystem 
 * anzumelden.
 * Es wird dem Nutzer angezeigt, wenn er den Editor oder den Reportgenerator 
 * aufruft und noch nicht im System eingeloggt ist.
 * Das Pandel bietet dem User einen link, der es Ihm ermöglicht,
 * sich über den Googleaccount in das Kontaktsystem anzumelden.
 * @author Oliver Gorges
 */
public class Login extends VerticalPanel{
	// Header
	private HorizontalPanel headerPanel = new HorizontalPanel();
	private Label headerText = new Label("KontaktSystem");
	
	// Content
	private User userInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label welcomeLabel = new Label("Willkommen im KontaktSystem");
	private Label loginLabel = new Label(
			"Melden Sie sich mit Ihrem Google Konto an, um auf das Kontaktsystem zuzugreifen.");			
	private VerticalPanel vp = new VerticalPanel();
	
	// Übergibt die Userinfo vom Server an das Formular
	public Login(User u){
		userInfo = u;
	}
	
	@Override
	public void onLoad(){
		super.onLoad();
		loginLabel.getElement().setId("loginlabel");
		headerText.setStyleName("loginheader");
		vp.setStyleName("login");
		
		/**
		 * Aufbau des Login Headers
		 */	
		headerPanel.add(headerText);		
		
		RootPanel.get("Header").add(headerPanel);
		headerPanel.setCellHorizontalAlignment(headerText, HasHorizontalAlignment.ALIGN_CENTER);
		vp.add(welcomeLabel);
		vp.add(loginLabel);
		vp.setCellHorizontalAlignment(welcomeLabel, HasHorizontalAlignment.ALIGN_CENTER);
		vp.setCellHorizontalAlignment(loginLabel, HasHorizontalAlignment.ALIGN_CENTER);
		loginLabel.getElement().getStyle().setFontSize(1.8d, Unit.EM);
		welcomeLabel.getElement().getStyle().setFontSize(2.2d, Unit.EM);
		vp.add(new HTML("<br /> <br />"));
		
		// Fläche über die sich der User ginloggen kann.
		FocusPanel login = new FocusPanel();
		login.add(new Image(GWT.getHostPageBaseURL() + "images/login.png"));
		login.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.assign(userInfo.getLoginUrl());				
			}		
		});
		// Zentrierung der Elemente
		vp.add(login);
		vp.setCellHorizontalAlignment(login, HasHorizontalAlignment.ALIGN_CENTER);
		loginPanel.add(vp);
		loginPanel.setWidth("100vw");
		loginPanel.setHeight("81vh");
		loginPanel.setCellHorizontalAlignment(vp, HasHorizontalAlignment.ALIGN_CENTER);
		loginPanel.setCellVerticalAlignment(vp,HasVerticalAlignment.ALIGN_MIDDLE);
		this.add(loginPanel);
	}

}
