package de.hh.changeRing.controller;

import static de.hh.changeRing.domain.User.dummyUser;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

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
public class UserTest {

	private final UserSession session = new UserSession();
	private final User user= new User();
	private final de.hh.changeRing.domain.User me = dummyUser(1L);
	private final de.hh.changeRing.domain.User other = dummyUser(2L);

	@Before
	public void setup(){
		user.setSession(session);
		session.setUser(me);
	}

	@Test
	public void isMe()  {
		user.setSelectedUser(me);
		assertThat(user.isMe(), is(true));
		user.setSelectedUser(other);
		assertThat(user.isMe(), is(false));
	}

	@Test
	public void isOther()  {
		user.setSelectedUser(me);
		assertThat(user.isOther(), is(false));
		user.setSelectedUser(other);
		assertThat(user.isOther(), is(true));
	}

	@Test
	public void meOrFilled()  {
		user.setSelectedUser(other);
		assertThat(user.meOrFilled("ö"), is(true));
		assertThat(user.meOrFilled(""), is(false));
		user.setSelectedUser(me);
		assertThat(user.meOrFilled(null), is(true));
	}


	@Test
	public void otherAndFilled()  {
		user.setSelectedUser(other);
		assertThat(user.otherAndFilled("ö"), is(true));
		assertThat(user.otherAndFilled(""), is(false));
		assertThat(user.otherAndFilled(null), is(false));
		user.setSelectedUser(me);
		assertThat(user.otherAndFilled("fd"), is(false));
	}
}
