package de.hh.changeRing;

import com.google.common.collect.Ordering;
import de.hh.changeRing.domain.Advertisement;
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
import java.util.ArrayList;
import java.util.List;

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
public class InitTestData {
    private static InitialData data;
    private static List<Transaction> transactions;
    private static List<Advertisement> advertisements = new ArrayList<Advertisement>();

    public static void init() {
        try {
            JAXBContext context = JAXBContext.newInstance(InitialData.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = InitialData.class.getResourceAsStream("/initialData.xml");

            data = (InitialData) unmarshaller.unmarshal(is);

            List<User> users = InitTestData.getUsers();
            for (int i = 1000; i < 2000; i++) {
                users.add(User.dummy(i));

            }
            initAds();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initAds() {
        for (User user : getUsers()) {
            Advertisement advertisement = new Advertisement();
            advertisement.setOwner(user);
        }
    }

    public static List<User> getUsers() {
        return data.users;
    }

    public static List<Transaction> getTransactions() {
        if (transactions == null) {
            transactions = new Ordering<Transaction>() {
                @Override
                public int compare(Transaction transaction, Transaction transaction1) {
                    return transaction.getDate().compareTo(transaction1.getDate());
                }
            }.sortedCopy(data.transactions);
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

    public static List<Advertisement> getAdvertisements() {
        return advertisements;
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
