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
 * ----------------GNU General Public License--------------------------------
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * ----------------in addition-----------------------------------------------
 * <p/>
 * In addition, each military use, and the use for interest profit will be
 * excluded.
 * Environmental damage caused by the use must be kept as small as possible.
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
        // TODO new Context().leaveInternalAreaView();
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
