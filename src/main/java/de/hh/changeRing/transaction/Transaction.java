package de.hh.changeRing.transaction;

import de.hh.changeRing.BaseEntity;
import de.hh.changeRing.InitTestData;
import de.hh.changeRing.user.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

import static javax.persistence.TemporalType.DATE;

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
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
public class Transaction extends BaseEntity {
    @XmlElement
    @Temporal(DATE)
    private Date date;

    @XmlElement
    @Transient
    private Long fromId;

    @SuppressWarnings("JpaDataSourceORMInspection")
    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private User from;

    @XmlElement
    @Transient
    private Long toId;

    @SuppressWarnings("JpaDataSourceORMInspection")
    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    private User to;

    @XmlElement
    private long amount;

    private static long idCounter = 0;

    @XmlElement
    private String subject;

    public User getFrom() {
        return from;
    }

    public Date getDate() {
        return date;
    }

    public User getTo() {
        return to;
    }

    public long getAmount() {
        return amount;
    }

    public Long getId() {
        if (id == null) {
            id = idCounter++;
        }
        return id;
    }

    public static Transaction create(User from, User to, long amount, String subject) {
        Transaction transaction = new Transaction();
        transaction.from = from;
        transaction.to = to;
        transaction.amount = amount;
        transaction.subject = subject;
        transaction.date = new Date();
        return transaction;
    }

    public void wire() {
        if (from == null) {
            from = InitTestData.findUser(fromId);
        }
        if (to == null) {
            to = InitTestData.findUser(toId);
        }
        from.execute(this);
        to.execute(this);
    }

    public String getSubject() {
        return subject;
    }
}
