package de.hh.changeRing.controller;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import de.hh.changeRing.SuperTest;
import de.hh.changeRing.user.UserSession;
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
public class UserSessionTest extends SuperTest{
	private static final String PASSWORD = "123";
	private static final String NICK = "niles";
	private final UserSession session = new UserSession(){
        @Override
        protected void message(String message) {
        }
    };
	@Test
	public void loginWithId()  {
		logIn("13");
	}

	@Test
	public void loginWithEmail()  {
		logIn(email());
	}

	@Test
	public void loginWithNickname()  {
		logIn(NICK);
	}

	private String email() {return users.get(0).getEmail();}

	private void logIn(String id) {
		expectLoggedOut();
		session.setId(id);
		session.setPassword(UserSessionTest.PASSWORD);
		session.login();
		expectLoggedIn();
	}

	private void expectLoggedOut() {
		assertThat(session.isLoggedIn(), is(false));
		assertThat(session.isNotLoggedIn(), is(true));
	}

	private void expectLoggedIn() {
		assertThat(session.isLoggedIn(), is(true));
		assertThat(session.isNotLoggedIn(), is(false));
	}
}
