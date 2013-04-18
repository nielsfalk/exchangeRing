package de.hh.changeRing.transaction;

import de.hh.changeRing.FunctionalTest;
import de.hh.changeRing.user.DepotItem;
import de.hh.changeRing.user.DepotItemType;
import de.hh.changeRing.user.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

import static de.hh.changeRing.user.DepotItemType.in;
import static de.hh.changeRing.user.DepotItemType.out;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class MoneyTest extends FunctionalTest {
    protected static final String SUBJECT = MoneyTest.class.getName();
    @PersistenceContext
    private
    EntityManager entityManager;

    protected void expectTransactionProcessed(User owner, User receiver) {
        
        owner = refresh(owner);
        receiver = refresh(receiver);
        assertThat(owner.getBalance(), is(new BigDecimal("-30.00")));

        assertThat(receiver.getBalance(), is(new BigDecimal("30.00")));
        expectBalance(owner, new BigDecimal("-30.00"));
        expectDepotItem(owner, new BigDecimal("-30.00"), receiver, out);
        expectBalance(receiver, new BigDecimal("30.00"));
        expectDepotItem(receiver, new BigDecimal("30.00"), owner, in);
        expectSameTransAction(owner, receiver);
    }

    private void expectSameTransAction(User owner, User receiver) {
        assertThat(owner.getDepotItems().get(0).getTransaction(),
                is(receiver.getDepotItems().get(0).getTransaction()));
    }

    private void expectBalance(User user, BigDecimal amount) {
        assertThat(user.getBalance(), is(amount));
    }

    private void expectDepotItem(User user, BigDecimal amount, User other, DepotItemType type) {
        List<DepotItem> depotItems = user.getDepotItems();
        assertThat(depotItems.size(), is(1));

        DepotItem depotItem = depotItems.get(0);
        assertThat(depotItem.getAmount(), is(amount));
        assertThat(depotItem.getFormattedDate(), is(not(nullValue())));
        assertThat(depotItem.getOldBalance(), is(new BigDecimal("0.00")));
        assertThat(depotItem.getNewBalance(), is(amount));
        assertThat(depotItem.getOther(), is(other));
        assertThat(depotItem.getTransaction().getSubject(), is(SUBJECT));
        assertThat(depotItem.getType(), is(type));
    }

    private User refresh(User user) {
        return entityManager.find(User.class, user.getId());
    }
}
