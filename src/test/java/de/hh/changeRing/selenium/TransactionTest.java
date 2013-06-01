package de.hh.changeRing.selenium;

import de.hh.changeRing.transaction.Transaction;
import de.hh.changeRing.transaction.TransactionCreator;
import de.hh.changeRing.user.DepotItemType;
import de.hh.changeRing.user.Member;
import de.hh.changeRing.user.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.servlet.AfterServlet;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.joda.time.DateMidnight;
import org.junit.Test;
import org.openqa.selenium.By;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

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
@WarpTest
public class TransactionTest extends SeleniumTest {
    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("1000.00");
    private static final User USER = createTestMember(INITIAL_BALANCE);
    private static final User RECEIVER = createTestMember(INITIAL_BALANCE);
    private static final BigDecimal TRANSACTION_AMOUNT = new BigDecimal("15.00");
    private static final BigDecimal RECEIVERS_NEW_BALANCE = INITIAL_BALANCE.add(TRANSACTION_AMOUNT);
    private static final BigDecimal USERS_NEW_BALANCE = INITIAL_BALANCE.subtract(TRANSACTION_AMOUNT);
    private static final String SUBJECT = "Vielen Dank für die gute Arbeit";


    @Deployment
    public static WebArchive createDeployment() {
        return warWithBasics().addClasses(DataPump.class, TransactionCreator.class, TransactionVerifier.class);
    }

    @Test
    public void transaction() {
        login(USER.getEmail(), PASSWORD);
        fillTransactionForm();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                browser.findElement(By.id("transactionForm-confirm")).click();
            }
        }).inspect(new VerifyTransaction());

        headerContainsNewAmount(USERS_NEW_BALANCE);
        expectTransactionLog(out, TRANSACTION_AMOUNT.negate(), USERS_NEW_BALANCE);

        logout();
        login(RECEIVER.getEmail(), PASSWORD);
        headerContainsNewAmount(RECEIVERS_NEW_BALANCE);
        expectTransactionLog(in, TRANSACTION_AMOUNT, RECEIVERS_NEW_BALANCE);
    }

    private void expectTransactionLog(DepotItemType depotItemType, BigDecimal amount, BigDecimal balance) {
        browser.navigate().to(deploymentUrl + "/internal/account.xhtml");

        assertThat(textFirstRowCell(4), is(depotItemType.toString()));
        assertThat(textFirstRowCell(5), is(SUBJECT));
        assertThat(textFirstRowCell(6), is(amount.toString()));
        assertThat(textFirstRowCell(7), is(balance.toString()));

    }

    private String textFirstRowCell(int ddd) {
        return browser.findElement(By.xpath("//*[@id=\"transactionTable_data\"]/tr[1]/td[" + ddd + "]/div")).getText();
    }

    private void fillTransactionForm() {
        browser.navigate().to(deploymentUrl + "/internal/transaction.xhtml?clear=clear&receiverId=" + RECEIVER.getId());
        browser.findElement(By.id("transactionForm-amount")).sendKeys(String.valueOf(TRANSACTION_AMOUNT.intValue()));
        browser.findElement(By.id("transactionForm-subject")).sendKeys(SUBJECT);
        browser.findElement(By.id("transactionForm-showDialogButton")).click();
        waitForAllResourceThreads();
    }

    private void headerContainsNewAmount(BigDecimal expectedAmount) {
        String text = browser.findElement(By.xpath("//*[@id=\"loggedInHeaderForm-personalMenu_button\"]/span")).getText();
        assertThat(text, is("Kontostand: " + expectedAmount));
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

    @Stateless
    public static class TransactionVerifier {
        @PersistenceContext
        EntityManager entityManager;

        public void verifyTransaction() {
            List<Transaction> sentTransactions = entityManager.find(Member.class, USER.getId()).getSentTransactions();
            assertThat(sentTransactions.size(), is(1));
            Transaction transaction = sentTransactions.get(0);
            assertThat(transaction.getAmount(), is(TRANSACTION_AMOUNT));
            assertThat(transaction.getDate().toDateMidnight(), is(new DateMidnight()));
            assertThat(transaction.getAmount(), is(TRANSACTION_AMOUNT));
            assertThat(transaction.getFrom().getId(), is(USER.getId()));
            assertThat(transaction.getTo().getId(), is(RECEIVER.getId()));
            assertThat(transaction.getFromNewBalance(), is(USERS_NEW_BALANCE));
            assertThat(transaction.getToNewBalance(), is(RECEIVERS_NEW_BALANCE));
            assertThat(transaction.getSubject(), is(SUBJECT));
        }
    }

    public static class VerifyTransaction extends Inspection {
        private static final long serialVersionUID = 1L;

        @EJB
        private TransactionVerifier transactionVerifier;

        @AfterServlet
        public void verifyTransaction() {
            transactionVerifier.verifyTransaction();
        }
    }
}
