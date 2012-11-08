package de.hh.changeRing;

import de.bripkens.gravatar.DefaultImage;
import de.bripkens.gravatar.Gravatar;
import de.bripkens.gravatar.Rating;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * User: nielsfalk Date: 08.11.12 10:18
 */
@ManagedBean
@SessionScoped
public class UserSession {
    private String id, password;

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
    }
}
