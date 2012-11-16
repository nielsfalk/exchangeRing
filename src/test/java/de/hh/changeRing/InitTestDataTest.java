package de.hh.changeRing;

import de.hh.changeRing.domain.Transaction;
import de.hh.changeRing.domain.User;
import org.junit.Ignore;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: nielsfalk
 * Date: 08.11.12 17:34
 */
public class InitTestDataTest extends SupertTest {


    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy.MM.dd");

    @Test
    public void loadUsers() {
        User niels = users.get(0);
        assertThat(niels.getNickName(), is("niles"));
        assertThat(niels.getId(), is(577));
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
        assertThat(transaction.getFrom(), is(InitTestData.findUser(577)));
        assertThat(transaction.getTo(), is(InitTestData.findUser(13)));
        assertThat(transaction.getAmount(), is(1l));
        assertThat(transaction.getDate(), is(FORMAT.parse("2012.11.07")));
    }


    @Test
    @Ignore
    public void accounts() {
        User niles = InitTestData.findUser(577);
        User blau = InitTestData.findUser(13);
        User oswald = InitTestData.findUser(14);
        assertThat(niles.getBalance(), is(-3l));
        assertThat(niles.getDepotItems().size(), is(2));
        User.DepotItem depotItem = niles.getDepotItems().get(0);
        assertThat(depotItem.getOldBalance(), is(0l));
        assertThat(depotItem.getNewBalance(), is(-1l));


        assertThat(blau.getBalance(), is(7l));
        assertThat(blau.getDepotItems().size(), is(3));
        assertThat(oswald.getBalance(), is(-4l));
        assertThat(oswald.getDepotItems().size(), is(1));

        assertThat(InitTestData.getTotalRevenue(), is(7L));
        assertThat(InitTestData.debtsAndAssetsAreEqual(), is(true));
    }

    @Test
    public void transactions() {
        User niles = InitTestData.findUser(577);
        long nilesInitialBalance = niles.getBalance();
        User blau = InitTestData.findUser(13);
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
