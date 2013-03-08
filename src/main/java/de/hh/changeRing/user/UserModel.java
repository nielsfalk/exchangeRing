package de.hh.changeRing.user;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

import static de.hh.changeRing.user.User.isEmpty;


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
@Named
@SessionScoped
public class UserModel implements Serializable {
    @Inject
    private UserSession session;
    private User selectedUser;

    @PersistenceContext
    private EntityManager entityManager;

    public boolean isMe() {
        return getLoggedInUser().equals(selectedUser);
    }

    public boolean isOther() {
        return !isMe();
    }

    public boolean meOrFilled(String s) {
        return isMe() || !isEmpty(s);
    }

    public boolean otherAndFilled(String s) {
        return isOther() && !isEmpty(s);
    }

    private User getLoggedInUser() {
        return session.getUser();
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void setSelectedUserId(Long id) {
        this.selectedUser = entityManager.find(User.class, id);
    }

    public Long getSelectedUserId() {
        if (selectedUser == null) {
            return null;
        }
        return selectedUser.getId();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setSession(UserSession session) {
        this.session = session;
    }

    public void setSelectedMe(@SuppressWarnings("UnusedParameters") String selectedMe) {
        selectedUser = session.getUser();
    }

    @SuppressWarnings("SameReturnValue")
    public String getSelectedMe() {
        return "";
    }


}
