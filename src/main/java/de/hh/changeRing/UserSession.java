package de.hh.changeRing;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


/**
 * User: nielsfalk Date: 08.11.12 10:18
 */
@ManagedBean
@SessionScoped
public class UserSession {
	private String userName;

	public String getStuff() {
		return "stuff";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
