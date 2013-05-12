package de.hh.changeRing.selenium;

import de.hh.changeRing.transaction.TransactionCreator;
import de.hh.changeRing.user.DepotItemType;
import de.hh.changeRing.user.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

import static de.hh.changeRing.TestUtils.PASSWORD;
import static de.hh.changeRing.TestUtils.createTestMember;
import static de.hh.changeRing.user.DepotItemType.in;
import static de.hh.changeRing.user.DepotItemType.out;
import static org.hamcrest.CoreMatchers.is;
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
@RunWith(Arquillian.class)
@RunAsClient
public class TransactionTest extends SeleniumTest {
    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("1000.00");
    private static final User USER = createTestMember(INITIAL_BALANCE);
    private static final User RECEIVER = createTestMember(INITIAL_BALANCE);
    private static final BigDecimal TRANSACTION_AMOUNT = new BigDecimal("15.00");
    private static final String SUBJECT = "Vielen Dank für die gute Arbeit";


    @Deployment
    public static WebArchive createDeployment() {
        return warWithBasics().addClasses(DataPump.class, TransactionCreator.class);
    }

    @Test
    public void transaction() {
        login(USER.getEmail(), PASSWORD);
        doTransaction();
        headerContainsNewAmount(INITIAL_BALANCE.subtract(TRANSACTION_AMOUNT));
        expectTransactionLog(out, TRANSACTION_AMOUNT.negate(), INITIAL_BALANCE.subtract(TRANSACTION_AMOUNT));

        logout();
        login(RECEIVER.getEmail(), PASSWORD);
        headerContainsNewAmount(INITIAL_BALANCE.add(TRANSACTION_AMOUNT));
        expectTransactionLog(in, TRANSACTION_AMOUNT, INITIAL_BALANCE.add(TRANSACTION_AMOUNT));
    }

    private void expectTransactionLog(DepotItemType depotItemType, BigDecimal amount, BigDecimal balance) {
        browser.open(deploymentUrl.toString() + "/internal/account.xhtml");
        assertThat(browser.getText("xpath=//*[@id=\"transactionTable_data\"]/tr[1]/td[4]/div"), is(depotItemType.toString()));
        assertThat(browser.getText("xpath=//*[@id=\"transactionTable_data\"]/tr[1]/td[5]/div"), is(SUBJECT));
        assertThat(browser.getText("xpath=//*[@id=\"transactionTable_data\"]/tr[1]/td[6]/div"), is(amount.toString()));
        assertThat(browser.getText("xpath=//*[@id=\"transactionTable_data\"]/tr[1]/td[7]/div"), is(balance.toString()));

    }

    private void doTransaction() {
        browser.open(deploymentUrl.toString() + "/internal/transaction.xhtml?clear=clear&receiverId=" + RECEIVER.getId());
        browser.type("id=transactionForm-amount", String.valueOf(TRANSACTION_AMOUNT.intValue()));
        browser.type("id=transactionForm-subject", SUBJECT);
        browser.click("id=transactionForm-showDialogButton");
        browser.click("id=transactionForm-confirm");
        browser.waitForPageToLoad("15000");
    }

    private void headerContainsNewAmount(BigDecimal expectedAmount) {
        assertThat(browser.getText("xpath=//*[@id=\"loggedInHeaderForm-personalMenu_button\"]/span"), is("Kontostand: " + expectedAmount));
    }

    @Singleton
    @Startup
    public static class DataPump {
        @PersistenceContext
        EntityManager entityManager;

        @PostConstruct
        public void createUser() {
            entityManager.persist(USER);
            entityManager.persist(RECEIVER);
        }
    }
}
