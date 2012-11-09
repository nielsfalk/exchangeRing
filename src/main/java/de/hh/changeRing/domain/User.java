package de.hh.changeRing.domain;

import com.google.common.base.Joiner;
import de.bripkens.gravatar.DefaultImage;
import de.bripkens.gravatar.Gravatar;
import de.bripkens.gravatar.Rating;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: nielsfalk
 * Date: 08.11.12 17:16
 */

@XmlAccessorType(XmlAccessType.PROPERTY)
public class User {
    @XmlElement
    private String nickName;

    @XmlElement
    private int id;

    @XmlElement
    private String firstName;

    @XmlElement
    private boolean firstNameVisible = true;

    @XmlElement
    private String lastName;

    @XmlElement
    private boolean lastNameVisible = true;

    @XmlElement
    private String password;

    @XmlElement
    private String email;

    @XmlElement
    private List<String> e;

    private List<Transaction> transactions = new ArrayList<Transaction>();

    private long balance;


    public String getNickName() {
        return nickName;
    }

    public String getFirstName() {
        return firstName;
    }

    public boolean isFirstNameVisible() {
        return firstNameVisible;
    }

    public Integer getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isLastNameVisible() {
        return lastNameVisible;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        if (email == null) {
            email = Joiner.on("").join(e);
        }
        return email;
    }

    public long getBalance() {
        return balance;
    }

    public void execute(Transaction transaction) {
        if (transaction.getFrom() != this && transaction.getTo() != this) {
            throw new RuntimeException("not a transaction of" + this);
        }
        if (transaction.getFrom() == this) {
            balance -= transaction.getAmount();
        }
        if (transaction.getTo() == this) {
            balance += transaction.getAmount();
        }
        transactions.add(transaction);


    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getGravatarUrl() {
        return new Gravatar().setSize(80).setHttps(true).setRating(Rating.PARENTAL_GUIDANCE_SUGGESTED)
                .setStandardDefaultImage(DefaultImage.MONSTER).getUrl(getEmail());
    }

	public String getGravatarHeaderUrl() {
		return new Gravatar().setSize(35).setHttps(true).setRating(Rating.PARENTAL_GUIDANCE_SUGGESTED)
				.setStandardDefaultImage(DefaultImage.MONSTER).getUrl(getEmail());
	}
}
