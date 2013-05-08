package de.hh.changeRing.user;

import org.junit.Before;
import org.junit.Test;

import static de.hh.changeRing.TestUtils.createAdministrator;
import static de.hh.changeRing.TestUtils.createTestMember;
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
    private final User me = createTestMember();
    private final User other = createTestMember();
	private final Administrator administrator = createAdministrator();
	private UserModel adminUserModel = new UserModel();

	@Before
    public void setup() {
	    UserSession session = new UserSession();
        userModel.setSession(session);
        session.setUser(me);

	    UserSession adminSession = new UserSession();
	    adminUserModel.setSession(adminSession);
		adminSession.setUser(administrator);
    }

    @Test
    public void isMeOrAsAdmin() {
	    selectUser(me);
	    assertThat(userModel.isMeOrAsAdmin(), is(true));
	    assertThat(adminUserModel.isMeOrAsAdmin(), is(true));
	    selectUser(other);
	    assertThat(userModel.isMeOrAsAdmin(), is(false));
    }

	@Test
    public void isOtherAndNotAsAdmin() {
		selectUser(me);
		assertThat(userModel.isOtherAndNotAsAdmin(), is(false));
		assertThat(adminUserModel.isOtherAndNotAsAdmin(), is(false));
		selectUser(other);
		assertThat(userModel.isOtherAndNotAsAdmin(), is(true));
    }

    @Test
    public void meOrAsAdminOrFilled() {
	    selectUser(other);
	    assertThat(userModel.meOrAsAdminOrFilled("ö"), is(true));
        assertThat(userModel.meOrAsAdminOrFilled(""), is(false));
	    selectUser(me);
	    assertThat(userModel.meOrAsAdminOrFilled(null), is(true));
	    assertThat(adminUserModel.meOrAsAdminOrFilled(null), is(true));
    }

    @Test
    public void otherAndFilledAndNotAsAdmin() {
	    selectUser(other);
	    assertThat(userModel.otherAndFilledAndNotAsAdmin("ö"), is(true));
        assertThat(userModel.otherAndFilledAndNotAsAdmin(""), is(false));
        assertThat(userModel.otherAndFilledAndNotAsAdmin(null), is(false));
	    selectUser(me);
	    assertThat(userModel.otherAndFilledAndNotAsAdmin("fd"), is(false));
	    assertThat(adminUserModel.otherAndFilledAndNotAsAdmin("fd"), is(false));
    }


	private void selectUser(User user) {
		userModel.setSelectedUser(user);
		adminUserModel.setSelectedUser(user);
	}
}
