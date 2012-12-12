package de.hh.changeRing.controller;

import de.hh.changeRing.InitTestData;
import de.hh.changeRing.domain.User;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static de.hh.changeRing.domain.User.isEmpty;


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
public class Users implements Serializable{
    @Inject
    private UserSession session;
    private ArrayList<User> otherUsers;
    private User selectedUser;

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

    public List<User> getOtherUsers() {
        if (otherUsers == null) {
            otherUsers = new ArrayList<User>();
            otherUsers.addAll(InitTestData.getUsers());
            otherUsers.remove(getLoggedInUser());
        }
        return otherUsers;
    }

    private User getLoggedInUser() {
        return session.getUser();
    }

    public List<User> getMembers() {
        return InitTestData.getUsers();
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void setSelectedUserId(Long id) {
        this.selectedUser = InitTestData.findUser(id);
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
