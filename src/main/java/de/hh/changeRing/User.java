package de.hh.changeRing;

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
			otherUsers.remove(getMe());
		}
		return otherUsers;
	}

	private de.hh.changeRing.domain.User getMe() {
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
}
