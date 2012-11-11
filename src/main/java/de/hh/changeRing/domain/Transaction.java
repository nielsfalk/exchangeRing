package de.hh.changeRing.domain;

import de.hh.changeRing.InitTestData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class Transaction {
    @XmlElement
    private Long id;
    @XmlElement
    private Date date;
    @XmlElement
    private int fromId;
    private User from;
    @XmlElement
    private int toId;
    private User to;
    @XmlElement
    private long amount;

    private static long idCounter = 0;

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

    public long getId() {
        if (id == null) {
            id = idCounter++;
        }
        return id;
    }

    public static Transaction create(User from, User to, long amount) {
        Transaction transaction = new Transaction();
        transaction.from = from;
        transaction.to = to;
        transaction.amount = amount;
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

}
