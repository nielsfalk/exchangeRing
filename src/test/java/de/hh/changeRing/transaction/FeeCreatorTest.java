package de.hh.changeRing.transaction;

import com.google.common.collect.Maps;
import com.sun.xml.ws.developer.Stateful;
import de.hh.changeRing.Context;
import de.hh.changeRing.user.*;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.joda.time.DateMidnight;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static de.hh.changeRing.transaction.FeeCreatorTest.FeeCreator.FeeCalculationResult;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
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
    private static User userWithNegativeBalance = createTestMember(new BigDecimal("-30.00"));
    private static User userWithPositiveBalance = createTestMember(new BigDecimal("34.56"));
    private static User noFee = createNoFeeTestMember(new BigDecimal("12.34"));
    private static SystemAccount system = createSystemAccount();
    private static Administrator administrator = createAdministrator();


    @Deployment
    public static Archive<?> createDeployment() {
        return functionalJarWithEntities().addClasses(DataPump.class, FeeCreator.class);
    }

    @Inject
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
    }


    @Model
    @Stateful
    public static class FeeCreator {
        public static final BigDecimal TAX_AMOUNT = new BigDecimal("2.00");
        public static final BigDecimal DEMURRAGE_PERCENT = new BigDecimal("0.02");
        @PersistenceContext
        private EntityManager entityManager;

        @Inject
        private Event<UserUpdateEvent> events;

        public FeeCalculationResult previewTax() {
            return calculateTax();
        }

        private FeeCalculationResult calculateTax() {
            FeeCalculationResult calculationResult = new FeeCalculationResult();
            List<Member> relevantMembers = entityManager.createNamedQuery("allMembers", Member.class).getResultList();
            for (Member member : relevantMembers) {
                calculationResult.userAmounts.put(member, TAX_AMOUNT);
            }
            return calculationResult;
        }

        public FeeCalculationResult previewDemurrage() {
            return calculateDemurage();
        }

        private FeeCalculationResult calculateDemurage() {
            FeeCalculationResult calculationResult = new FeeCalculationResult();
            List<Member> relevantMembers = entityManager.createNamedQuery("allMembers", Member.class).getResultList();
            for (Member member : relevantMembers) {
                if (member.getBalance().compareTo(ZERO) > 0) {
                    BigDecimal amount = member.getBalance().multiply(DEMURRAGE_PERCENT).setScale(2, HALF_UP);
                    calculationResult.userAmounts.put(member, amount);
                }
            }
            return calculationResult;
        }

        public void executeFees() {
            executeDemurage();
            executeTax();


        }

        private void executeTax() {
            FeeCalculationResult tax = calculateTax();
            for (Map.Entry<User, BigDecimal> entry : tax.userAmounts.entrySet()) {
                Transaction.create(entry.getKey(), system, entry.getValue(), String.format("Fixe Gebühr von %s Motten für %s", entry.getValue(), Context.formatGermanDate(new DateMidnight().toDate())));
            }
            events.fire(new UserUpdateEvent(tax.userAmounts.keySet()));
        }

        private void executeDemurage() {
            calculateDemurage();
            //Umlaufsicherung für 04.2013, 20.00 Promille von 70.96ergibt: 1.4192Motten

        }

        public static class FeeCalculationResult {
            private Map<User, BigDecimal> userAmounts = Maps.newHashMap();

            public BigDecimal getTotalAmount() {
                BigDecimal result = new BigDecimal("0.00");
                for (User user : userAmounts.keySet()) {
                    result = result.add(userAmounts.get(user));
                }
                return result;
            }
        }

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
