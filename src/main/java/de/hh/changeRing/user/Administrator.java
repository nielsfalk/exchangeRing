package de.hh.changeRing.user;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("admin")
public class Administrator extends User{
	@Override
	public boolean isAdmin() {
		return true;
	}
}
