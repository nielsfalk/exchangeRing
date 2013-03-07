package de.hh.changeRing.transaction;

import de.hh.changeRing.InitTestData;
import de.hh.changeRing.advertisement.Advertisement;
import de.hh.changeRing.user.User;
import de.hh.changeRing.user.UserSession;
import de.hh.changeRing.user.UserUpdateEvent;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static de.hh.changeRing.Context.context;

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
@Named
@SessionScoped
@Stateful
public class TransactionCreator implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(TransactionCreator.class.getName());

    private User receiver;

    private Long amount;

    private String subject;

    @Inject
    private UserSession session;

    @PersistenceContext
    EntityManager entityManager;

    private Advertisement advertisement;

    @Inject
    private Event<UserUpdateEvent> events;

    public String submit() {
        if (amount < 0) {
            amount = amount * -1;
        }
        LOGGER.info(session.getUser().getId() + " created transaction");
        User owner = entityManager.find(User.class, session.getUser().getId());
        User receiver = entityManager.find(User.class, this.receiver.getId());
        Transaction transaction = Transaction.create(owner, receiver, amount, subject);
        owner.execute(transaction);
        receiver.execute(transaction);
        events.fire(new UserUpdateEvent(owner, receiver));

        setClear("clear");
        message("Überweisung Durchgeführt");
        return "/internal/transactions.xhtml";
    }

    public List<User> getOtherUsers() {
        return entityManager.createNamedQuery("findOthers").setParameter("me", session.getUser()).getResultList();
    }

    protected void message(String message) {
        context().addMessage(message);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setSession(UserSession session) {
        this.session = session;
    }

    public void setClear(@SuppressWarnings("UnusedParameters") String clear) {
        receiver = null;
        amount = null;
        subject = null;
        advertisement = null;
    }

    @SuppressWarnings("SameReturnValue")
    public String getClear() {
        return "";
    }

    public Long getReceiverId() {
        if (receiver == null) {
            return null;
        }
        return receiver.getId();
    }

    public void setReceiverId(Long receiverId) {
        setClear("clear");
        receiver = InitTestData.findUser(receiverId);
    }


    public void setAdId(Long adId) {
        advertisement = InitTestData.findAd(adId);
        subject = adId + " - " + advertisement.getContent();
        if (subject.length() > 140) {
            subject = subject.substring(0, 140);
        }

    }

    public Long getAdId() {
        if (advertisement == null) {
            return null;
        }
        return advertisement.getId();
    }

    public User getReceiver() {
        return receiver;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
