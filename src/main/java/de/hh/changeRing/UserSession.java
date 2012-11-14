package de.hh.changeRing;

import de.bripkens.gravatar.DefaultImage;
import de.bripkens.gravatar.Gravatar;
import de.bripkens.gravatar.Rating;
import de.hh.changeRing.domain.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import java.util.ArrayList;
import java.util.List;

/**
 * User: nielsfalk Date: 08.11.12 10:18
 */
@ManagedBean
@SessionScoped
public class UserSession {
    private String id, password;
    private User user;
	private ArrayList<User> otherUsers;

	public String getGravatarUrl() {
        return new Gravatar().setSize(80).setHttps(true).setRating(Rating.PARENTAL_GUIDANCE_SUGGESTED)
                .setStandardDefaultImage(DefaultImage.MONSTER).getUrl("niles@elbtrial.com");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void login() {
        System.out.println(id);
        User user = InitTestData.findUser(id);
        if (user != null && user.getPassword().equals(password)) {
            this.user = user;
			otherUsers = null;
        }
    }


	public List<User> getOtherUsers() {
		if (otherUsers == null) {
			otherUsers = new ArrayList<User>();
			otherUsers.addAll(InitTestData.getUsers());
			otherUsers.remove(user);
		}
		return otherUsers;
	}

    public List<User> getAllUsers() {
        return InitTestData.getUsers();
    }

    public void logout() {
        user = null;
		otherUsers = null;
    }

    public String activeMenu(String viewIdPrefix) {
        return FacesContext.getCurrentInstance().getViewRoot().getViewId().substring(1).startsWith(viewIdPrefix)
                ? "ui-state-active" : "";
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public boolean isNotLoggedIn() {
        return !isLoggedIn();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }
}
