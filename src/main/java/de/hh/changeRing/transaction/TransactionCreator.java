package de.hh.changeRing.transaction;

import de.hh.changeRing.Context;
import de.hh.changeRing.InitTestData;
import de.hh.changeRing.advertisement.Advertisement;
import de.hh.changeRing.user.User;
import de.hh.changeRing.user.UserSession;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;

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
public class TransactionCreator implements Serializable{
    private static final Logger LOGGER = Logger.getLogger(TransactionCreator.class.getName());

    private User receiver;

    private Long amount;

    private String subject;

    @Inject
    private UserSession session;

    private Advertisement advertisement;

    public String submit() {
        if (amount < 0) {
            amount = amount * -1;
        }
        LOGGER.info(session.getUser().getId() + " created transaction");
        Transaction.create(session.getUser(), receiver, amount, subject).wire();
        setClear("clear");
        message("Überweisung Durchgeführt");
        return "/internal/transactions.xhtml";
    }

    protected void message(String message) {
        new Context().addMessage(message);
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
