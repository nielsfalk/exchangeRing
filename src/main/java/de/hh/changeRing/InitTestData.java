package de.hh.changeRing;

import com.google.common.collect.Ordering;
import de.hh.changeRing.domain.Advertisement;
import de.hh.changeRing.domain.Categorie;
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
import java.util.*;

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
    private static HashMap<Categorie, LinkedList<Advertisement>> sortedAds;

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
            advertisement.setType(Advertisement.AdvertisementType.values()[user.getId() % 2]);
            advertisement.setContent(loremYpsum());
            advertisement.setLocation("egal");
            advertisement.setTitle("anzeige von " + user.getId());
            advertisement.setValidUntil(new Date(System.currentTimeMillis() + new Random().nextInt(1000 * 24 * 60 * 60 * 1000)));
            advertisement.setLinkLocation(true);
            advertisement.setCategorie(Categorie.values()[new Random().nextInt(Categorie.values().length - 1)]);
            advertisements.add(advertisement);
        }
    }

    public static Map<Categorie, LinkedList<Advertisement>> getSortedAds() {
        if (sortedAds == null) {
            sortedAds = new HashMap<Categorie, LinkedList<Advertisement>>();
            for (Categorie categorie : Categorie.values()) {
                sortedAds.put(categorie, new LinkedList<Advertisement>());
            }
            for (Advertisement advertisement : advertisements) {
                boolean notExpired = advertisement.getValidUntil().getTime() > System.currentTimeMillis();
                if (notExpired) {
                    sortedAds.get(advertisement.getCategorie()).add(advertisement);
                }
            }
            //sort
            for (Categorie categorie : Categorie.values()) {
                List<Advertisement> sorted = new Ordering<Advertisement>() {
                    @Override
                    public int compare(Advertisement advertisement, Advertisement advertisement1) {
                        return advertisement.getCreationDate().compareTo(advertisement1.getCreationDate());
                    }
                }.sortedCopy(sortedAds.get(categorie));
                LinkedList<Advertisement> linkedList = new LinkedList<Advertisement>();
                linkedList.addAll(sorted);
                sortedAds.put(categorie, linkedList);
            }
        }
        return sortedAds;
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

    public static String loremYpsum() {
        return loremYpsum(new Random().nextInt(4));
    }

    public static String loremYpsum(int type) {
        switch (type) {
            case 1:
                return "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam";
            case 2:
                return "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, " +
                        "sed diam nonumy eirmod tempor invidunt ut labore et " +
                        "dolore magna aliquyam erat, sed diam voluptua. At " +
                        "vero eos et accusam et justo duo dolores et ea " +
                        "rebum. Stet clita kasd gubergren, no sea takimata sanctus est" +
                        " Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, " +
                        "consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
                        "invidunt ut labore et dolore magna aliquyam erat, sed diam" +
                        " voluptua. At vero eos et accusam et justo duo dolores et ea reb" +
                        "um. Stet clita kasd gubergren, no sea takimata sanctus est Lorem " +
                        "ipsum dolor sit amet.";
            case 3:
                return "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.   \n" +
                        "\n" +
                        "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.   \n" +
                        "\n" +
                        "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.   \n" +
                        "\n" +
                        "Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.   \n" +
                        "\n" +
                        "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis.   \n" +
                        "\n" +
                        "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, At accusam aliquyam diam diam dolore dolores duo eirmod eos erat, et nonumy sed tempor et et invidunt justo labore Stet clita ea et gubergren, kasd magna no rebum. sanctus sea sed takimata ut vero voluptua. est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat.   \n" +
                        "\n" +
                        "Consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus.   \n" +
                        "\n" +
                        "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.   \n" +
                        "\n" +
                        "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.   \n" +
                        "\n" +
                        "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.   \n" +
                        "\n" +
                        "Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo";
        }
        return "";
    }


}
