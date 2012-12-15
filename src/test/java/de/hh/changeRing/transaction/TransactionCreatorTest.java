package de.hh.changeRing.transaction;

import de.hh.changeRing.user.DepotItem;
import de.hh.changeRing.user.User;
import de.hh.changeRing.user.UserSession;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static de.hh.changeRing.user.User.DepotItemType.in;
import static de.hh.changeRing.user.User.DepotItemType.out;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
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
public class TransactionCreatorTest {

    private static final String SUBJECT = "test";
    private TransactionCreator transactionCreator;
    private final User owner = User.dummyUser(1L);
    private final User receiver = User.dummyUser(2L);

    @Before
    public void login() {
        UserSession session = new TestUserSession(owner);
        transactionCreator = new TransactionCreator() {
            @Override
            protected void message(String message) {
            }
        };
        transactionCreator.setSession(session);
    }

    @Test
    public void submit() {
        transactionCreator.setReceiver(receiver);
        transactionCreator.setAmount(30l);
        transactionCreator.setSubject(SUBJECT);

        transactionCreator.submit();
        expectBalance(owner, -30l);
        expectDepotItem(owner, -30l, receiver, out);
        expectBalance(receiver, 30l);
        expectDepotItem(receiver, 30l, owner, in);
        expectSameTransAction(owner, receiver);
    }

    private void expectSameTransAction(User owner, User receiver) {
        assertThat(owner.getDepotItems().get(0).getTransaction(),
                is(receiver.getDepotItems().get(0).getTransaction()));
    }

    private void expectBalance(User user, long amount) {
        assertThat(user.getBalance(), is(amount));
    }

    private void expectDepotItem(User user, long amount, User other, User.DepotItemType type) {
        List<DepotItem> depotItems = user.getDepotItems();
        assertThat(depotItems.size(), is(1));

        DepotItem depotItem = depotItems.get(0);
        assertThat(depotItem.getAmount(), is(amount));
        assertThat(depotItem.getFormattedDate(), is(not(nullValue())));
        assertThat(depotItem.getOldBalance(), is(0l));
        assertThat(depotItem.getNewBalance(), is(amount));
        assertThat(depotItem.getOther(), is(other));
        assertThat(depotItem.getSubject(), is(SUBJECT));
        assertThat(depotItem.getType(), is(type));
    }

    public static class TestUserSession extends UserSession{

        public TestUserSession(User owner) {
            user = owner;
        }
    }
}
