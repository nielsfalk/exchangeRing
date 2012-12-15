package de.hh.changeRing.user;

import com.google.common.base.Joiner;
import de.hh.changeRing.BaseEntity;
import de.hh.changeRing.transaction.Transaction;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static de.hh.changeRing.user.User.DepotItemType.in;
import static de.hh.changeRing.user.User.DepotItemType.out;
import static javax.persistence.EnumType.STRING;

/**
* Created with IntelliJ IDEA.
* User: niles
* Date: 15.12.12
* Time: 14:36
* To change this template use File | Settings | File Templates.
*/
@Entity
public class DepotItem extends BaseEntity{
    @Transient
    private Transaction transaction;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private long amount;
    private long newBalance;
    @ManyToOne
    @JoinColumn(name = "other_user_id", nullable = false)
    private User other;

    @Enumerated(STRING)
    private User.DepotItemType type;
    private long oldBalance;

    public DepotItem(Transaction transaction, User user, long amount, User other, User.DepotItemType type) {
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

    public User.DepotItemType getType() {
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
