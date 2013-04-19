package de.hh.changeRing.transaction;

import de.hh.changeRing.FunctionalTest;
import de.hh.changeRing.user.DepotItem;
import de.hh.changeRing.user.DepotItemType;
import de.hh.changeRing.user.User;
import de.hh.changeRing.user.UserUpdateEvent;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

import static de.hh.changeRing.user.DepotItemType.in;
import static de.hh.changeRing.user.DepotItemType.out;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class MoneyTest extends FunctionalTest {
    protected static final String SUBJECT = MoneyTest.class.getName();
    protected static boolean refresheEventWasFired;
    @PersistenceContext
    protected EntityManager entityManager;

    protected void expectTransactionProcessed(User owner, User receiver) {

        owner = refresh(owner);
        receiver = refresh(receiver);
        assertThat(owner.getBalance(), is(new BigDecimal("-30.00")));

        assertThat(receiver.getBalance(), is(new BigDecimal("30.00")));
        expectBalance(owner, new BigDecimal("-30.00"));
        expectOneDepotItem(owner, new BigDecimal("-30.00"), receiver, out);
        expectBalance(receiver, new BigDecimal("30.00"));
        expectOneDepotItem(receiver, new BigDecimal("30.00"), owner, in);
        expectSameTransAction(owner, receiver);
    }

    private void expectSameTransAction(User owner, User receiver) {
        assertThat(owner.getDepotItems().get(0).getTransaction(),
                is(receiver.getDepotItems().get(0).getTransaction()));
    }

    private void expectBalance(User user, BigDecimal amount) {
        assertThat(user.getBalance(), is(amount));
    }

    private void expectOneDepotItem(User user, BigDecimal amount, User other, DepotItemType type) {
        assertThat(user.getDepotItems().size(), is(1));
        assertThat(user.getDepotItems(), hasItems(withProperties(amount, other, type, SUBJECT, new BigDecimal("0.00"), amount)));
    }

    protected static Matcher<Iterable<DepotItem>> hasItems(Matcher<DepotItem> ... depotItemMatcher) {
        return Matchers.<DepotItem>hasItems(depotItemMatcher);
    }

    protected Matcher<DepotItem> withProperties(final BigDecimal amount, final User other, final DepotItemType type, final String subject, final BigDecimal oldBalance, final BigDecimal newBalance) {
        return new TypeSafeMatcher<DepotItem>() {
            @Override
            public boolean matchesSafely(DepotItem depotItem) {
                return depotItem.getAmount().equals(amount)
                        && depotItem.getOther().equals(other)
                        && depotItem.getType().equals(type)
                        && depotItem.getSubject().equals(subject)
                        && depotItem.getFormattedDate() != null
                        && depotItem.getOldBalance().equals(oldBalance)
                        && depotItem.getNewBalance().equals(newBalance);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    public void eventListener(@Observes UserUpdateEvent event) {
        refresheEventWasFired = true;
    }


    private Matcher<List<DepotItem>> shouldContain(final BigDecimal amount, final User other, final DepotItemType type, final String subject) {
        return new TypeSafeMatcher<List<DepotItem>>() {
            @Override
            public boolean matchesSafely(List<DepotItem> depotItems) {
                for (DepotItem depotItem : depotItems) {
                    if (depotItem.getAmount().equals(amount)
                            && depotItem.getOther().equals(other)
                            && depotItem.getType().equals(type)
                            && depotItem.getSubject().equals(subject)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("does not contain expected entry");
            }
        };
    }


    protected User refresh(User user) {
        return entityManager.find(User.class, user.getId());
    }
}
