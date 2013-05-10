package de.hh.changeRing;

import de.hh.changeRing.user.Administrator;
import de.hh.changeRing.user.Member;
import de.hh.changeRing.user.SystemAccount;
import de.hh.changeRing.user.User;

import java.math.BigDecimal;

public class TestUtils {
	public static final String PASSWORD = "321";

	public static User createNoFeeTestMember(BigDecimal bigDecimal) {
	    Member result = createTestMember(bigDecimal);
	    result.setNoFee(true);
	    return result;
	}

	public static Member createTestMember(BigDecimal balance) {
	    Member result = createTestMember();
	    result.setBalance(balance);
	    return result;
	}

	public static Member createTestMember() {
	    return (Member) init(new Member());
	}

	public static Administrator createAdministrator() {
	    return (Administrator) init(new Administrator());
	}

	public static SystemAccount createSystemAccount() {
	    return (SystemAccount) init(new SystemAccount());
	}

	private static User init(User user) {
	    user.setEmail(user.getId()+"hans@meiser.de");
	    user.setPassword(PASSWORD);
	    return user;
	}
}
