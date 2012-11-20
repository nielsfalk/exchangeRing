package de.hh.changeRing;

import static de.hh.changeRing.domain.User.isEmpty;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;


/**
 * User: nielsfalk Date: 19.11.12 12:51
 */
@ManagedBean
@SessionScoped
public class User {
	@ManagedProperty(value = "#{userSession}")
	private UserSession session;
	private ArrayList<de.hh.changeRing.domain.User> otherUsers;
	private de.hh.changeRing.domain.User selectedUser;

	public List<de.hh.changeRing.domain.User> getOtherUsers() {
		if (otherUsers == null) {
			otherUsers = new ArrayList<de.hh.changeRing.domain.User>();
			otherUsers.addAll(InitTestData.getUsers());
			otherUsers.remove(getLoggedInUser());
		}
		return otherUsers;
	}

	private de.hh.changeRing.domain.User getLoggedInUser() {
		return session.getUser();
	}

	public List<de.hh.changeRing.domain.User> getMembers() {
		return InitTestData.getUsers();
	}

	public de.hh.changeRing.domain.User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUserId(Long id) {
		this.selectedUser = InitTestData.findUser(id);
	}

	public Long getSelectedUserId() {
		if (selectedUser== null) {
			return null;
		}
		return selectedUser.getId();
	}

	public void setSession(UserSession session) {
		this.session = session;
	}

	public void setSelectedMe(String selectedMe) {
		selectedUser = session.getUser();
	}

	public String getSelectedMe() {
		return "";
	}

	public boolean isMe() {
		return getLoggedInUser().equals(selectedUser);
	}

	public boolean isOther() {
		return !isMe();
	}

	public boolean meOrFilled(String s) {
		return isMe() || !isEmpty(s);
	}

	public boolean otherAndFilled(String s){
		return isOther() && !isEmpty(s);
	}
}
