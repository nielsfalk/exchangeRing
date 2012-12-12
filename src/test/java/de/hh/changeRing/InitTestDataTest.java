package de.hh.changeRing;

import de.hh.changeRing.transaction.Transaction;
import de.hh.changeRing.user.User;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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
public class InitTestDataTest extends SuperTest {


    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy.MM.dd");

    @Test
    public void loadUsers() {
        User niels = users.get(0);
        assertThat(niels.getNickName(), is("niles"));
        assertThat(niels.getId(), is(577L));
        assertThat(niels.getFirstName(), is("Niels"));
        assertThat(niels.isFirstNameVisible(), is(true));
        assertThat(niels.getLastName(), is("Falk"));
        assertThat(niels.isLastNameVisible(), is(false));
        assertThat(niels.getPassword(), is("123"));
        assertThat(niels.getEmail(), is("niles@elbtrial.com"));
    }

    @Test
    public void findUser() {
        assertThat(InitTestData.findUser("577"), is(users.get(0)));
        assertThat(InitTestData.findUser("blau"), is(users.get(1)));
        assertThat(InitTestData.findUser("niels.falk@blau.de"), is(users.get(2)));
    }

    @Test
    public void transaction() throws ParseException {
        Transaction transaction = transactions.get(0);
        assertThat(transaction.getFrom(), is(InitTestData.findUser(577l)));
        assertThat(transaction.getTo(), is(InitTestData.findUser(13l)));
        assertThat(transaction.getAmount(), is(1l));
        assertThat(transaction.getDate(), is(FORMAT.parse("2012.11.07")));
    }

    @Test
    public void transactions() {
        User niles = InitTestData.findUser(577l);
        long nilesInitialBalance = niles.getBalance();
        User blau = InitTestData.findUser(13l);
        long blauInitialBalance = blau.getBalance();
        InitTestData.process(Transaction.create(niles, blau, 5l, null));
        assertThat(niles.getBalance() - nilesInitialBalance, is(-5l));
        assertThat(blau.getBalance() - blauInitialBalance, is(5l));
    }

    @Test
    public void advertisements() {
        assertThat(InitTestData.getAdvertisements().size() > 100, is(true));
    }
}
