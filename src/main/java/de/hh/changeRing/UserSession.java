package de.hh.changeRing;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import de.bripkens.gravatar.DefaultImage;
import de.bripkens.gravatar.Gravatar;
import de.bripkens.gravatar.Rating;

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

	public String getGravatarUrl() {
		return new Gravatar().setSize(80).setHttps(true).setRating(Rating.PARENTAL_GUIDANCE_SUGGESTED)
				.setStandardDefaultImage(DefaultImage.MONSTER).getUrl("niles@elbtrial.com");
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
