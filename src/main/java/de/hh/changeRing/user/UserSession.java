package de.hh.changeRing.user;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import static de.hh.changeRing.Context.context;

/**
 * ----------------GNU General Public License--------------------------------
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 * <p/>
 * ----------------in addition-----------------------------------------------
 * <p/>
 * In addition, each military use, and the use for interest profit will be excluded. Environmental damage caused by the
 * use must be kept as small as possible.
 */
@Named
@SessionScoped
@Stateful
public class UserSession implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(UserSession.class.getName());

    private String id, password;
    private User user;

    @PersistenceContext
    private
    EntityManager entityManager;

    public void login() {
        User user;
        try {
            user = entityManager.find(User.class, Long.parseLong(this.id));
        } catch (NumberFormatException e) {
            List<User> resultList = entityManager.createNamedQuery("loginWithEmail", User.class).setParameter("email", id).getResultList();
            user = resultList.isEmpty() ? null : resultList.get(0);
        }

        // User user = InitTestData.findUser(id);
        if (user != null && user.getPassword().equals(password)) {
            this.user = user;
            context().leavePublicEvents();
        }
        LOGGER.info(isLoggedIn() ? id + " logged in" : "tried to login " + id);
        id = null;
        password = null;
        //noinspection ConstantConditions
        message(isLoggedIn() ? ("Wilkommen " + user.getDisplayName() + "!") : "Id oder Passwort falsch");
        //return isLoggedIn()?"/internal/events/browse.xhtml":"";
    }

    protected void message(String message) {
        context().addMessage(message);
    }

    public void updateUser() {}

    @Named
    @Produces
    public boolean isNotLoggedIn() {
        return user == null;
    }

    @Named
    @Produces
    public boolean isLoggedIn() {
        return user != null;
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

    public void eventListener(@Observes UserUpdateEvent event) {
        if (event.regards(user)) {
            user = entityManager.find(User.class, user.getId());
        }
    }
}
