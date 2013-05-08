package de.hh.changeRing.transaction;

import de.hh.changeRing.TestUtils;
import de.hh.changeRing.user.*;
import org.hamcrest.Matcher;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

import static de.hh.changeRing.TestUtils.createAdministrator;
import static de.hh.changeRing.TestUtils.createNoFeeTestMember;
import static de.hh.changeRing.TestUtils.createSystemAccount;
import static de.hh.changeRing.TestUtils.createTestMember;
import static de.hh.changeRing.transaction.FeeCreator.FeeCalculationResult;
import static de.hh.changeRing.user.DepotItemType.out;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsNot.not;
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
public class FeeCreatorTest extends MoneyTest {
    public static final String EXPECTED_TAX_SUBJECT = "Fixe Gebühr von 2.00 Motten für ";
    public static final String DEMURRAGE_SUBJECT = "Umlaufsicherung für ";
    private static User userWithNegativeBalance = createTestMember(new BigDecimal("-30.00"));
    private static User userWithPositiveBalance = createTestMember(new BigDecimal("34.56"));
    private static User noFee = createNoFeeTestMember(new BigDecimal("12.34"));
    private static SystemAccount system = createSystemAccount();
    private static Administrator administrator = createAdministrator();


    @Deployment
    public static Archive<?> createDeployment() {
        return functionalJarWithEntities().addClasses(DataPump.class, FeeCreator.class);
    }

    @EJB
    private FeeCreator feeCreator;

    @Test
    public void feeAplicable() {
        for (User user : new User[]{userWithPositiveBalance, userWithNegativeBalance}) {
            assertThat(user.isFeeApplicable(), is(true));
        }
        for (User user : new User[]{noFee, system, administrator}) {
            assertThat(user.isFeeApplicable(), is(false));
        }
    }

    @Test
    public void previewTax() {
        FeeCalculationResult calculationResult = feeCreator.previewTax();
        for (User user : new User[]{noFee, system, administrator}) {
            assertThat(calculationResult.userAmounts.keySet(), not(hasItem(user)));
        }
        for (User user : new User[]{userWithNegativeBalance, userWithPositiveBalance}) {
            assertThat(calculationResult.userAmounts.get(user), is(new BigDecimal("2.00")));
        }
        assertThat(calculationResult.getTotalAmount(), is(new BigDecimal("4.00")));
    }


    @Test
    public void previewDemurrage() {
        FeeCalculationResult calculationResult = feeCreator.previewDemurrage();
        for (User user : new User[]{noFee, system, administrator, userWithNegativeBalance}) {
            assertThat(calculationResult.userAmounts.keySet(), not(hasItem(user)));
        }
        assertThat(calculationResult.userAmounts.get(userWithPositiveBalance), is(new BigDecimal("0.69")));
        assertThat(calculationResult.getTotalAmount(), is(new BigDecimal("0.69")));
    }

    @Test
    public void executeFees() {
        feeCreator.executeFees();

        assertThat(refresheEventWasFired, is(true));
        refreshUsers();
        assertThat(administrator.getDepotItems().size(), is(0));
        assertThat(noFee.getDepotItems().size(), is(0));
        expectDepotItems(userWithNegativeBalance, 1,
                withProperties(FeeCreator.TAX_AMOUNT.negate(), system, out, EXPECTED_TAX_SUBJECT, new BigDecimal("-30.00"), new BigDecimal("-32.00")));

        expectDepotItems(userWithPositiveBalance, 2,
                withProperties(new BigDecimal("-0.69"), system, out, DEMURRAGE_SUBJECT, new BigDecimal("34.56"), new BigDecimal("33.87")),
                withProperties(FeeCreator.TAX_AMOUNT.negate(), system, out, EXPECTED_TAX_SUBJECT, new BigDecimal("33.87"), new BigDecimal("31.87")));

        assertThat(system.getDepotItems().size(), is(3));
        assertThat(system.getBalance(), is(new BigDecimal("4.69")));
    }

    private void expectDepotItems(User user, int count, Matcher<DepotItem>... depotItemMatcher) {
        assertThat(user.getDepotItems().size(), is(count));
        assertThat(user.getDepotItems(), MoneyTest.hasItems(depotItemMatcher));
    }

    private void refreshUsers() {
        userWithPositiveBalance = refresh(userWithPositiveBalance);
        userWithNegativeBalance = refresh(userWithNegativeBalance);
        noFee = refresh(noFee);
        system = (SystemAccount) refresh(system);
        administrator = (Administrator) refresh(administrator);
    }

    @Override
    protected User refresh(User user) {
        return entityManager.find(User.class, user.getId());
    }


    @Singleton
    @Startup
    public static class DataPump {
        @PersistenceContext
        private EntityManager entityManager;

        @PostConstruct
        public void createUser() {
            for (User user : new User[]{userWithPositiveBalance, userWithNegativeBalance, noFee, system, administrator}) {
                entityManager.persist(user);
            }
        }
    }
}
