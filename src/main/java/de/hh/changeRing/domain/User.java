package de.hh.changeRing.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

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
	private boolean firstNameVisible=true;

	@XmlElement
	private String lastName;

	@XmlElement
	private boolean lastNameVisible=true;

	@XmlElement
	private String password;

	@XmlElement
	private String email;



	public String getNickName() {
		return nickName;
	}

	public String getFirstName() {
		return firstName;
	}

	public boolean isFirstNameVisible() {
		return firstNameVisible;
	}

	public int getId() {
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
		return email;
	}
}
