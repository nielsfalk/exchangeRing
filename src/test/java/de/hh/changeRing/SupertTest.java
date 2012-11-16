package de.hh.changeRing;

import de.hh.changeRing.domain.Transaction;
import de.hh.changeRing.domain.User;
import org.junit.BeforeClass;

import java.util.List;

public class SupertTest {
    private static InitTestData initTestData;
    static List<Transaction> transactions;
    static List<User> users;

    @BeforeClass
    public static void parse() {
        //initTestData = new InitTestData();
        //InitTestData.init();
        users = InitTestData.getUsers();
        transactions = InitTestData.getTransactions();
    }
}
