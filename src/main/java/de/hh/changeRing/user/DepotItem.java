package de.hh.changeRing.user;

import com.google.common.base.Joiner;
import de.hh.changeRing.BaseEntity;
import de.hh.changeRing.transaction.Transaction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static de.hh.changeRing.user.DepotItemType.in;
import static de.hh.changeRing.user.DepotItemType.out;
import static java.util.Locale.GERMANY;
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
public class DepotItem{
    private final Transaction transaction;

    private final DepotItemType type;

    public DepotItem(Transaction transaction, DepotItemType type) {
        this.transaction = transaction;
        this.type = type;
    }

    public String getFormattedDate() {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm", GERMANY).format(transaction.getDate().getMillis());
    }

    public String getShortFormattedDate() {
        return new SimpleDateFormat("dd.MM.yy", GERMANY).format(transaction.getDate().getMillis());
    }

    public BigDecimal getOldBalance() {
        return getNewBalance().subtract(getAmount());
    }

    public BigDecimal getAmount() {
        BigDecimal amount = transaction.getAmount();
        return type.equals(in)?amount:amount.negate();
    }

    public DepotItemType getType() {
        return type;
    }

    public BigDecimal getNewBalance() {
        return type.equals(in) ? transaction.getToNewBalance() : transaction.getFromNewBalance();
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public User getOther() {
        return type.equals(in)?transaction.getFrom():transaction.getTo();
    }

    public String getSubject() {
        /*String subject = transaction.getSubject();
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
        return Joiner.on('\n').join(splitted);*/
        return getTransaction().getSubject();
    }

    @Override
    public String toString() {
        return "DepotItem{" +
                "type=" + type +
                ", amount=" + getAmount() +
                ", other=" + getOther().getId() +
                ", subject=" + getSubject() +
                ", newBalance=" + getNewBalance() +
                ", oldBalance=" + getOldBalance() +
                ", date=" + getFormattedDate() +
                ", subject=" + getSubject() +
                ", transactionId=" + getTransaction().getId() +
                '}';
    }
}
