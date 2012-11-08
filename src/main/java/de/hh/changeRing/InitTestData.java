package de.hh.changeRing;

import de.hh.changeRing.domain.Transaction;
import de.hh.changeRing.domain.User;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.InputStream;
import java.util.List;


/**
 * User: nielsfalk Date: 08.11.12 17:23
 */
public class InitTestData {
    private static InitialData data;
    private static List<Transaction> transactions;

    public static void init() {
        try {
            JAXBContext context = JAXBContext.newInstance(InitialData.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = InitialData.class.getResourceAsStream("/initialData.xml");

            data = (InitialData) unmarshaller.unmarshal(is);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<User> getUsers() {
        return data.users;
    }

    public static List<Transaction> getTransactions() {
        if (transactions == null) {
            transactions = data.transactions;
            for (Transaction transaction : transactions) {
                process(transaction);
            }
        }
        return transactions;
    }

    static void process(Transaction transaction) {
        transaction.wire();
    }

    public static User findUser(int id) {
        for (User user : getUsers()) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public static User findUser(String idEmailOrNick) {
        for (User user : getUsers()) {
            if (user.getId().toString().equals(idEmailOrNick)
                    || user.getEmail().equals(idEmailOrNick)
                    || user.getNickName().equals(idEmailOrNick)) {
                return user;
            }
        }
        return null;
    }

    public static long getTotalRevenue() {
        long result = 0;
        for (Transaction transaction : transactions) {
            result += transaction.getAmount();
        }
        return result;
    }

    public static Boolean debtsAndAssetsAreEqual() {
        long debtsAndAssets = 0;
        for (User user : getUsers()) {
            debtsAndAssets += user.getBalance();
        }
        return debtsAndAssets == 0;
    }

    @XmlRootElement(name = "exchangeRingInitial")
    @XmlAccessorType(XmlAccessType.PROPERTY)
    public static class InitialData {
        @XmlElement(name = "user")
        List<User> users;

        @XmlElement(name = "transaction")
        List<Transaction> transactions;

    }


}
