package de.hh.changeRing.domain;

import com.google.common.base.Joiner;
import com.google.common.collect.Ordering;
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

    private List<DepotItem> depotItems = new ArrayList<DepotItem>();

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
        DepotItem depotItem = DepotItem.create(transaction, this);
        depotItems.add(depotItem);
        balance = depotItem.newBalance;
    }

    public static class DepotItem {
        private long id;
        private final Transaction transaction;
        private final User user;
        private final long newBalance;
        private User other;
        private long oldBalance;

        public DepotItem(Transaction transaction, User user, long newBalance, User other) {
            this.transaction = transaction;
            this.user = user;
            this.newBalance = newBalance;
            this.other = other;
            this.oldBalance = user.getBalance();
        }


        public static DepotItem create(Transaction transaction, User user) {
            if (transaction.getFrom() == transaction.getTo()) {
                throw new RuntimeException("invalid transaction from == to" + transaction);
            }
            if (transaction.getFrom() == user) {
                return new DepotItem(transaction, user, user.balance - transaction.getAmount(), transaction.getTo());

            }
            if (transaction.getTo() == user) {
                return new DepotItem(transaction, user, user.balance + transaction.getAmount(), transaction.getFrom());
            }
            throw new RuntimeException("not for this user");
        }

        public long getOldBalance() {
            return oldBalance;
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
    }

    public List<DepotItem> getDepotItems() {
        return new Ordering<DepotItem>() {

            @Override
            public int compare(DepotItem depotItem, DepotItem depotItem1) {
                return depotItem1.getTransaction().getDate().compareTo(depotItem.getTransaction().getDate());
            }
        }.sortedCopy(depotItems);
    }

    public String getGravatarUrl() {
        return new Gravatar().setSize(80).setHttps(true).setRating(Rating.PARENTAL_GUIDANCE_SUGGESTED)
                .setStandardDefaultImage(DefaultImage.MONSTER).getUrl(getEmail());
    }

    public String getGravatarHeaderUrl() {
        return new Gravatar().setSize(49).setHttps(true).setRating(Rating.PARENTAL_GUIDANCE_SUGGESTED)
                .setStandardDefaultImage(DefaultImage.MONSTER).getUrl(getEmail());
    }

    public String getDisplayName() {
        return isEmpty(nickName) ? getName() : nickName;
    }

    private boolean isEmpty(String string) {
        return string == null || string.equals("");
    }

    public String getName() {

        String result = "";
        if (firstNameVisible && !isEmpty(firstName)) {
            result += firstName;
        }
        if (lastNameVisible && !isEmpty(lastName)) {
            result += isEmpty(result) ? lastName : (" - " + lastName);

        }
        return result;
    }


}
