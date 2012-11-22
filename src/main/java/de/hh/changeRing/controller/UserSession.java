package de.hh.changeRing.controller;

import de.hh.changeRing.InitTestData;
import de.hh.changeRing.domain.Advertisement;
import de.hh.changeRing.domain.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.logging.Logger;

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
    private static final Logger LOGGER = Logger.getLogger(UserSession.class.getName());

    private String id, password;
    private User user;
    private Advertisement selectedAdvertisement;

    public void login() {
        User user = InitTestData.findUser(id);
        if (user != null && user.getPassword().equals(password)) {
            this.user = user;
        }
        LOGGER.info(isLoggedIn() ? id + " logged in" : "tried to login " + id);
    }

	public void updateUser() {
	}

    public String activeMenu(String viewIdPrefix) {
        return FacesContext.getCurrentInstance().getViewRoot().getViewId().substring(1).startsWith(viewIdPrefix)
                ? "ui-state-active" : "";
    }

    @SuppressWarnings("UnusedDeclaration")
    public void selectOffer(Long id) {
        selectedAdvertisement = InitTestData.findAd(id);
    }

    public boolean isNotLoggedIn() {
        return user == null;
    }


    public boolean isLoggedIn() {
        return user != null;
    }

    public void setSelectedAdvertisement(Advertisement selectedAdvertisement) {
        this.selectedAdvertisement = selectedAdvertisement;
    }

    public Advertisement getSelectedAdvertisement() {
        return selectedAdvertisement;
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

	public void setPassword(String password) {
		this.password = password;
	}

	public User getUser() {
		return user;
	}

	void setUser(User user) {
		this.user = user;
	}
}
