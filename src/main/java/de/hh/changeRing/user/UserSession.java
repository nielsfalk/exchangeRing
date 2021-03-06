package de.hh.changeRing.user;

import com.google.common.collect.Lists;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static de.hh.changeRing.Context.context;
import static de.hh.changeRing.user.User.hashPassword;

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

    private String idOrEmail, password;
    private User user;

    @PersistenceContext
    private
    EntityManager entityManager;
    private ArrayList<User> members;

    public void login() {
        User user;
        try {
            user = entityManager.find(User.class, Long.parseLong(this.idOrEmail));
        } catch (NumberFormatException e) {
            List<User> resultList = entityManager.createNamedQuery("loginWithEmail", User.class).setParameter("email", idOrEmail).getResultList();
            user = resultList.isEmpty() ? null : resultList.get(0);
        }

        //don't inline to prevent timing attacs
        String passwordHash = hashPassword(user == null ? 14 : user.getId(), password);
        if (user != null && user.getPasswordHash().equals(passwordHash) && user.isNotSystem()) {
            this.user = user;
            context().leavePublicEvents();
            user.applyLastLogin();
        }
        LOGGER.info(isLoggedIn() ? idOrEmail + " logged in" : "tried to login " + idOrEmail);
        idOrEmail = null;
        password = null;
        //noinspection ConstantConditions
        message(isLoggedIn() ? ("Wilkommen " + user.getDisplayName() + "!") : "Id oder Passwort falsch");
        //return isLoggedIn()?"/internal/events/browse.xhtml":"";
    }

    private void message(String message) {
        context().addMessage(message);
    }

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

    public String getIdOrEmail() {
        return idOrEmail;
    }

    public void setIdOrEmail(String idOrEmail) {
        this.idOrEmail = idOrEmail;
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
        members = null;
    }

    public List<User> getMembers() {
        if (members == null) {
            TypedQuery<User> allUsers = isNoAdmin() ? entityManager.createNamedQuery("allMembers", User.class) : entityManager.createNamedQuery("allUsers", User.class);
            members = Lists.newArrayList(allUsers.getResultList());
        }
        return members;
    }

    @Named
    @Produces
    public boolean isAdmin() {
        return isLoggedIn() && user.isAdmin();
    }

    public boolean isNoAdmin() {
        return isNotLoggedIn() || !user.isAdmin();
    }
}
