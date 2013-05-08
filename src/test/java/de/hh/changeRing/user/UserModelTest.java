package de.hh.changeRing.user;

import de.hh.changeRing.user.User;
import de.hh.changeRing.user.UserModel;
import de.hh.changeRing.user.UserSession;
import org.junit.Before;
import org.junit.Test;

import static de.hh.changeRing.user.User.dummyUser;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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
public class UserModelTest {

    private final UserModel userModel = new UserModel();
    private final User me = dummyUser(1L);
    private final User other = dummyUser(2L);

    @Before
    public void setup() {
	    UserSession session = new UserSession();
        userModel.setSession(session);
        session.setUser(me);
    }

    @Test
    public void isMe() {
        userModel.setSelectedUser(me);
        assertThat(userModel.isMe(), is(true));
        userModel.setSelectedUser(other);
        assertThat(userModel.isMe(), is(false));
    }

    @Test
    public void isOther() {
        userModel.setSelectedUser(me);
        assertThat(userModel.isOther(), is(false));
        userModel.setSelectedUser(other);
        assertThat(userModel.isOther(), is(true));
    }

    @Test
    public void meOrFilled() {
        userModel.setSelectedUser(other);
        assertThat(userModel.meOrFilled("ö"), is(true));
        assertThat(userModel.meOrFilled(""), is(false));
        userModel.setSelectedUser(me);
        assertThat(userModel.meOrFilled(null), is(true));
    }


    @Test
    public void otherAndFilled() {
        userModel.setSelectedUser(other);
        assertThat(userModel.otherAndFilled("ö"), is(true));
        assertThat(userModel.otherAndFilled(""), is(false));
        assertThat(userModel.otherAndFilled(null), is(false));
        userModel.setSelectedUser(me);
        assertThat(userModel.otherAndFilled("fd"), is(false));
    }
}
