package de.hh.changeRing.transaction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.xml.ws.developer.Stateful;
import de.hh.changeRing.user.Administrator;
import de.hh.changeRing.user.Member;
import de.hh.changeRing.user.SystemAccount;
import de.hh.changeRing.user.User;
import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static de.hh.changeRing.transaction.FeeCreatorTest.FeeCreator.FeeCalculationResult;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
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

@RunWith(Arquillian.class)
@Ignore("Work in Progres - depends on pending account hirachie")
public class FeeCreatorTest extends MoneyTest {
    private static User userWithNegativeBalance = createTestMember(new BigDecimal("-30.00"));
    private static User userWithPositiveBalance = createTestMember(new BigDecimal("30.00"));
    private static User noFee = createNoFeeTestMember(new BigDecimal("30.00"));
    private static SystemAccount system = createSystemAccount();
    private static Administrator administrator = createAdministrator();


    @Deployment
    public static Archive<?> createDeployment() {
        return functionalJarWithEntities().addClasses(DataPump.class, FeeCreator.class);
    }

    @PersistenceContext
    private EntityManager entityManager;

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
        assertThat(calculationResult.userAmounts.get(userWithNegativeBalance), is(new BigDecimal("2.00")));


    }

    private void expectTransactionProcessed() {
        super.expectTransactionProcessed(userWithPositiveBalance, userWithNegativeBalance);
    }


    @Model
    @Stateful
    public static class FeeCreator {
        public static final BigDecimal TAX_AMOUNT = new BigDecimal("2.00");
        @PersistenceContext
        private EntityManager entityManager;

        //Demurrage

        public FeeCalculationResult previewTax() {
            FeeCalculationResult calculationResult = new FeeCalculationResult();
            List<Member> relevantMemebers = entityManager.createNamedQuery("membersWithFee", Member.class).setParameter("fee",1L).getResultList();
            for (Member member : relevantMemebers) {
                calculationResult.userAmounts.put(member, TAX_AMOUNT);
            }
            return calculationResult;
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
