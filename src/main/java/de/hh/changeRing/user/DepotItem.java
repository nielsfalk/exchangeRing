package de.hh.changeRing.user;

import com.google.common.base.Joiner;
import de.hh.changeRing.BaseEntity;
import de.hh.changeRing.transaction.Transaction;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static de.hh.changeRing.user.DepotItemType.in;
import static de.hh.changeRing.user.DepotItemType.out;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.EnumType.STRING;


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
@Entity
public class DepotItem extends BaseEntity{
    @SuppressWarnings("JpaDataSourceORMInspection")
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @SuppressWarnings({"JpaDataSourceORMInspection", "UnusedDeclaration"})
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private long amount;

    private long newBalance;

    @SuppressWarnings("JpaDataSourceORMInspection")
    @ManyToOne
    @JoinColumn(name = "other_user_id", nullable = false)
    private User other;

    @Enumerated(STRING)
    private DepotItemType type;
    private long oldBalance;

    public DepotItem(Transaction transaction, User user, long amount, User other, DepotItemType type) {
        this.transaction = transaction;
        this.user = user;
        this.amount = amount;
        this.newBalance = user.getBalance() + amount;
        this.other = other;
        this.type = type;
        this.oldBalance = user.getBalance();
    }

    public DepotItem() {
    }

    public String getFormattedDate() {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(transaction.getDate());
    }

    public static DepotItem create(Transaction transaction, User user) {
        if (transaction.getFrom() == transaction.getTo()) {
            throw new RuntimeException("invalid transaction from == to" + transaction);
        }
        if (transaction.getFrom() == user) {
            return new DepotItem(transaction, user, -transaction.getAmount(), transaction.getTo(), out);

        }
        if (transaction.getTo() == user) {
            return new DepotItem(transaction, user, transaction.getAmount(), transaction.getFrom(), in);
        }
        throw new RuntimeException("not for this user");
    }

    public long getOldBalance() {
        return oldBalance;
    }

    public long getAmount() {
        return amount;
    }

    public DepotItemType getType() {
        return type;
    }

    public long getNewBalance() {
        return newBalance;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public User getOther() {
        return other;
    }

    public String getSubject() {
        String subject = transaction.getSubject();
        if (subject == null) {
            return "";
        }
        if (subject.length() <= 35) {
            return subject;
        }
        ArrayList<String> splitted = new ArrayList<String>();
        while (subject.length() > 35) {
            splitted.add(subject.substring(0, 35));
            subject = subject.substring(35);
        }
        splitted.add(subject);

        return Joiner.on('\n').join(splitted);

    }

}
