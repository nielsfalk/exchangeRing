package de.hh.changeRing.controller;

import org.junit.Before;
import org.junit.Test;

import static de.hh.changeRing.domain.User.dummyUser;
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
public class UsersTest {

    private final UserSession session = new UserSession();
    private final Users users = new Users();
    private final de.hh.changeRing.domain.User me = dummyUser(1L);
    private final de.hh.changeRing.domain.User other = dummyUser(2L);

    @Before
    public void setup() {
        users.setSession(session);
        session.setUser(me);
    }

    @Test
    public void isMe() {
        users.setSelectedUser(me);
        assertThat(users.isMe(), is(true));
        users.setSelectedUser(other);
        assertThat(users.isMe(), is(false));
    }

    @Test
    public void isOther() {
        users.setSelectedUser(me);
        assertThat(users.isOther(), is(false));
        users.setSelectedUser(other);
        assertThat(users.isOther(), is(true));
    }

    @Test
    public void meOrFilled() {
        users.setSelectedUser(other);
        assertThat(users.meOrFilled("ö"), is(true));
        assertThat(users.meOrFilled(""), is(false));
        users.setSelectedUser(me);
        assertThat(users.meOrFilled(null), is(true));
    }


    @Test
    public void otherAndFilled() {
        users.setSelectedUser(other);
        assertThat(users.otherAndFilled("ö"), is(true));
        assertThat(users.otherAndFilled(""), is(false));
        assertThat(users.otherAndFilled(null), is(false));
        users.setSelectedUser(me);
        assertThat(users.otherAndFilled("fd"), is(false));
    }
}
