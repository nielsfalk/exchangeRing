package de.hh.changeRing;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import de.hh.changeRing.domain.Transaction;
import de.hh.changeRing.domain.User;

@ManagedBean
@SessionScoped
public class TransactionCreator {

	private User receiver;

	private Long amount;


	private String subject;

	@ManagedProperty(value = "#{userSession}")
	private UserSession session;

	private List<User> users;

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

	public void submit() {
		Transaction.create(session.getUser(), receiver, amount, subject).wire();
		receiver = null;
		amount = null;
		subject = null;
	}

	public void setSession(UserSession session) {
		this.session = session;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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
}
