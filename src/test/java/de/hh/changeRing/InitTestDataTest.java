package de.hh.changeRing;

import de.hh.changeRing.domain.User;
import org.hamcrest.core.Is;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

/**
 * User: nielsfalk
 * Date: 08.11.12 17:34
 */
public class InitTestDataTest {

	@Test
	public void loadFromXml(){
		InitTestData initTestData = new InitTestData();
		initTestData.init();
		List<User> users = initTestData.getUsers();
		assertThat(users.size(), is(3));
		User niels = users.get(0);
		assertThat(niels.getNickName(), is("niles"));
		assertThat(niels.getId(), is(577));
		assertThat(niels.getFirstName(),is("Niels"));
		assertThat(niels.isFirstNameVisible(),is(true));
		assertThat(niels.getLastName(), is("Falk"));
		assertThat(niels.isLastNameVisible(),is(false));
		assertThat(niels.getPassword(),is("123"));
		assertThat(niels.getEmail(), is("niles@elbtrial.com"));
	}
}
