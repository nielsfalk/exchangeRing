package de.hh.changeRing.controller;

import de.hh.changeRing.InitTestData;
import de.hh.changeRing.domain.Advertisement;
import de.hh.changeRing.domain.Transaction;
import de.hh.changeRing.domain.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
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
@ManagedBean
@SessionScoped
public class TransactionCreator {
    private static final Logger LOGGER = Logger.getLogger(TransactionCreator.class.getName());

    private User receiver;

    private Long amount;

    private String subject;

    @ManagedProperty(value = "#{userSession}")
    private UserSession session;

	private Advertisement advertisement;

    public void submit() {
        LOGGER.info(session.getUser().getId() + " created transaction");
        Transaction.create(session.getUser(), receiver, amount, subject).wire();
        setClear("clear");
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

    @FacesConverter("userConverter")
    public static class UserConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
            for (User user : InitTestData.getUsers()) {
                if (user.getId().toString().equals(s)) {
                    return user;
                }
            }
            return null;
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
            if (o instanceof User) {
                User user = (User) o;
                return user.getId().toString();
            }
            return "Bitter wählen";
        }
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
