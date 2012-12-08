package de.hh.changeRing.domain;

import com.google.common.base.Joiner;
import com.google.common.collect.Ordering;
import de.bripkens.gravatar.DefaultImage;
import de.bripkens.gravatar.Gravatar;
import de.bripkens.gravatar.Rating;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static de.hh.changeRing.Context.formatGermanDate;
import static de.hh.changeRing.domain.User.DepotItemType.in;
import static de.hh.changeRing.domain.User.DepotItemType.out;
import static de.hh.changeRing.domain.User.Status.active;

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

@XmlAccessorType(XmlAccessType.PROPERTY)
public class User extends BaseEntity {
    private String nickName;

    @XmlElement
    private String firstName;

    private boolean firstNameVisible = true;

    @XmlElement
    private String lastName;

    private boolean lastNameVisible = true;

    @XmlElement
    private String password;

    private String email;

    private boolean emailVisible = true;

    private boolean addressVisible = true;

    @XmlElement
    private List<String> e;

    @XmlElement
    private List<String> f;

    @XmlElement
    private List<String> s;

    private String street = "";

    private String houseNumber = "";

    private int plz;

    private String city = "";

    private String district;

    private String phone;

    private String phoneMobile;

    private String url;

    private String urlDescription;

    private long limit = 0;

    private BigDecimal missingFee = new BigDecimal("0.00");

    private Date birthDay;

    private BigDecimal fee = new BigDecimal("6.00");

    private String wordsOfWisdom;

    private String accessibility;

    private Status status = active;

    private Date activated;

    private Date deActivated;

    private List<DepotItem> depotItems = new ArrayList<DepotItem>();

    private long balance;

    private String facebook;

    private String skype;

    @XmlElement
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFirstName() {
        return firstName;
    }

    @XmlElement
    public boolean isFirstNameVisible() {
        return firstNameVisible;
    }

    public String getLastName() {
        return lastName;
    }

    @XmlElement
    public boolean isLastNameVisible() {
        return lastNameVisible;
    }

    public void setFirstNameVisible(boolean firstNameVisible) {
        this.firstNameVisible = firstNameVisible;
    }

    public void setLastNameVisible(boolean lastNameVisible) {
        this.lastNameVisible = lastNameVisible;
    }

    public String getPassword() {
        return password;
    }

    @XmlElement
    public String getEmail() {
        if (email == null && e != null) {
            email = Joiner.on("").join(e);
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVisibleEmail() {
        return emailVisible ? getEmail() : "";
    }

    public String getVisibleFirstName() {
        return firstNameVisible ? getFirstName() : "";
    }

    public String getVisibleLastName() {
        return lastNameVisible ? getLastName() : "";
    }

    public String getVisibleAddress() {
        return addressVisible ? String.format("%s %s\n%s %s", street, houseNumber, plz, city) : "";
    }

    public long getBalance() {
        return balance;
    }

    public void execute(Transaction transaction) {
        DepotItem depotItem = DepotItem.create(transaction, this);
        depotItems.add(depotItem);
        balance = depotItem.newBalance;
        sortDepot();
    }

    public static User dummyUser(Long i) {
        User user = new User();
        user.id = i;
        user.email = "email" + i + "@sonst-was.de";
        user.password = "bll" + "lll";
        user.nickName = randomName();
        user.firstName = randomName();
        user.lastName = randomName();
        user.firstNameVisible = new Random().nextBoolean();
        user.lastNameVisible = new Random().nextBoolean();
        user.emailVisible = new Random().nextBoolean();
        user.addressVisible = new Random().nextBoolean();
        user.street = randomName();
        user.houseNumber = "" + new Random().nextInt(1000);
        user.plz = new Random().nextInt(99999);
        user.city = randomName();
        user.district = randomName();

        return user;
    }

    private static String randomName() {
        return DUMMY_NAMES[new Random().nextInt(DUMMY_NAMES.length)];
    }

    private static final String[] DUMMY_NAMES = ("Lind-ald'o,Noez-ves-teuf,L'per-eg,Chpsst,Ouiooea,M-shy-llont,Ieoa," +
            "Wor-unt'ee,Sllcnuooy,Eyieauoigdls,Uoeuae,Yoieyotlghld,Enth-em-i,Aouoeo," +
            "Aeoaie,Stfmr,Iaee,Eieeoayrdtshph,Ough-lor'shol,Stsckc,K'er-meep," +
            "Ryrtoeaiea,Uiaoaisllnnl,Yndllnd,Tsrmiay,Ouioie,Vndnnld,B-rod'eeg," +
            "Ooieoie,Eayaeellcthd,Kmmseyaei,Weul'it'nann,Ayinghw,Y'ryn-phei,Aieaigbrtnn," +
            "Oeououa,Oieeuilrghd,Ghsttnd,K-nys'yph,Rntghlt,Iaeuiaolmldk,Srntsteiai," +
            "Aeaoeerrntthl,Aeaooirtpsn,Lwnnoeauoo,Fltrrsiaeiay,Uiaeoeermlr,Rdmbnua,Ieaieyui," +
            "Ach-qua-cu,Nchnthaiui,Naent-aw'y,Rnnllpoeioi,C-shy-el,Eieoazrntll,Ieiauidslgh," +
            "Aeuiauo,Yot-nys-woi,Oeayiai,Ghsdnoie,Uiayoeurchtph,Yayueldllrdg,Rtllndl," +
            "Oouieo,Lshdgh,Ayaiey,Em'er-oi,Ououayauntsrdh,Oeeyucndls,S'hon-u," +
            "Shrnw,Nltthk,Ooeyehcmch,Slshldyooe,Wlrrchaooi,Lstboau,Eyeaiuisnths," +
            "Um'ser'stuit,Ang-wor-i,Phlmsee,Nys-eld-oo,Kphbstiauie,Sttgeuiei,Auiaieouctshv," +
            "Ooiaeytshprt,Frydou,Ntbkth,Tlddooey,Rvsz,Vpknn,Ver'em'thoeg," +
            "Oayaeei,Lndnnt,Aiyoie,Rssckqoaee,Eieaoua,Noos'ight'eesh,Aoeooltlk," +
            "Kmphduiyey,Shsstld,Tnltbeaeui,Vthrchiaeui,Pdfl,Oaeailmqn,Lsghd," +
            "Drtlq,Tnlheoue,Euie,Uouayoe,W'tin'iad,Eeioo,Eeyooe," +
            "Oioaoednht,Fnmss,Chlndleeui,Uioeeaisbsf,Uiayaieynmqll,Cllnnieiey,Znthloeiay").split(",");

    public static enum DepotItemType {
        out("ausgegeben"), in("eingenommen");
        private final String string;

        DepotItemType(String string) {
            this.string = string;
        }

        @Override
        public String toString() {
            return string;
        }
    }

    public static class DepotItem {
        private long id;
        private final Transaction transaction;
        private final User user;
        private long amount;
        private final long newBalance;
        private final User other;
        private final DepotItemType type;
        private final long oldBalance;

        public DepotItem(Transaction transaction, User user, long amount, User other, DepotItemType type) {
            this.transaction = transaction;
            this.user = user;
            this.amount = amount;
            this.newBalance = user.balance + amount;
            this.other = other;
            this.type = type;
            this.oldBalance = user.getBalance();
        }

        public String getFormattedDate() {
            return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(transaction.getDate());
        }

        public static DepotItem create(Transaction transaction, User user) {
            if (transaction.getFrom() == transaction.getTo()) {
                throw new RuntimeException("invalid transaction from == to" + transaction);
            }
            if (transaction.getFrom() == user) {
                return new DepotItem(transaction, user, -transaction.getAmount(), transaction.getTo(), out);

            }
            if (transaction.getTo() == user) {
                return new DepotItem(transaction, user, transaction.getAmount(), transaction.getFrom(), in);
            }
            throw new RuntimeException("not for this user");
        }

        public long getOldBalance() {
            return oldBalance;
        }

        public long getAmount() {
            return amount;
        }

        public DepotItemType getType() {
            return type;
        }

        public long getNewBalance() {
            return newBalance;
        }

        public Transaction getTransaction() {
            return transaction;
        }

        public User getOther() {
            return other;
        }

        public String getSubject() {
            String subject = transaction.getSubject();
            if (subject == null) {
                return "";
            }
            if (subject.length() <= 35) {
                return subject;
            }
            ArrayList<String> splitted = new ArrayList<String>();
            while (subject.length() > 35) {
                splitted.add(subject.substring(0, 35));
                subject = subject.substring(35);
            }
            splitted.add(subject);

            return Joiner.on('\n').join(splitted);

        }

    }

    public List<DepotItem> getDepotItems() {
        return depotItems;
    }

    private void sortDepot() {
        depotItems = new Ordering<DepotItem>() {

            @Override
            public int compare(DepotItem depotItem, DepotItem depotItem1) {
                return depotItem1.getTransaction().getDate().compareTo(depotItem.getTransaction().getDate());
            }
        }.sortedCopy(depotItems);
    }

    public String getGravatarUrl30() {
        return new Gravatar().setSize(30).setHttps(true).setRating(Rating.PARENTAL_GUIDANCE_SUGGESTED)
                .setStandardDefaultImage(DefaultImage.MONSTER).getUrl(getEmail());
    }

    public String getGravatarHeaderUrl49() {
        return new Gravatar().setSize(49).setHttps(true).setRating(Rating.PARENTAL_GUIDANCE_SUGGESTED)
                .setStandardDefaultImage(DefaultImage.MONSTER).getUrl(getEmail());
    }

    public String getGravatarUrl250() {
        return new Gravatar().setSize(250).setHttps(true).setRating(Rating.PARENTAL_GUIDANCE_SUGGESTED)
                .setStandardDefaultImage(DefaultImage.MONSTER).getUrl(getEmail());
    }

    public String getDisplayName() {
        return isEmpty(nickName) ? getName() : nickName;
    }

    public static boolean isEmpty(Object string) {
        return string == null || "".equals(string);
    }

    String getName() {
        String result = "";
        if (firstNameVisible && !isEmpty(firstName)) {
            result += firstName;
        }
        if (lastNameVisible && !isEmpty(lastName)) {
            result += isEmpty(result) ? lastName : (" " + lastName);

        }
        return result;
    }

    @SuppressWarnings("CanBeFinal")
    public static enum Status {
        active("aktiv"),
        inActive("inactive"),
        disabled("gesperrt");

        private String translation;

        Status(String translation) {
            this.translation = translation;
        }

        public String getTranslation() {
            return translation;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "nickName='" + nickName + '\'' +
                ", id=" + getId() +
                ", firstName='" + firstName + '\'' +
                ", firstNameVisible=" + firstNameVisible +
                ", lastName='" + lastName + '\'' +
                ", lastNameVisible=" + lastNameVisible +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", emailVisible=" + emailVisible +
                ", addressVisible=" + addressVisible +
                ", e=" + e +
                ", depotItems=" + depotItems +
                ", balance=" + balance +
                '}';
    }

    @XmlElement
    public boolean isEmailVisible() {
        return emailVisible;
    }

    public void setEmailVisible(boolean emailVisible) {
        this.emailVisible = emailVisible;
    }

    @XmlElement
    public boolean isAddressVisible() {
        return addressVisible;
    }

    public void setAddressVisible(boolean addressVisible) {
        this.addressVisible = addressVisible;
    }

    @XmlElement
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @XmlElement
    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    @XmlElement
    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    @XmlElement
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @XmlElement
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @XmlElement
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @XmlElement
    public String getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    @XmlElement
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @XmlElement
    public String getUrlDescription() {
        return urlDescription;
    }

    public void setUrlDescription(String urlDescription) {
        this.urlDescription = urlDescription;
    }

    @XmlElement
    public Date getBirthDay() {
        return birthDay;
    }

    public String getFormattedBirthDay() {
        return formatGermanDate(birthDay);
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @XmlElement
    public String getWordsOfWisdom() {
        return wordsOfWisdom;
    }

    public void setWordsOfWisdom(String wordsOfWisdom) {
        this.wordsOfWisdom = wordsOfWisdom;
    }

    @XmlElement
    public String getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(String accessibility) {
        this.accessibility = accessibility;
    }

    @XmlElement
    public BigDecimal getMissingFee() {
        return missingFee;
    }

    @XmlElement
    public BigDecimal getFee() {
        return fee;
    }

    @XmlElement
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setActivated(Date activated) {
        this.activated = activated;
    }

    public void setDeActivated(Date deActivated) {
        this.deActivated = deActivated;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    @XmlElement
    public Date getActivated() {
        return activated;
    }

    @XmlElement
    public Date getDeActivated() {
        return deActivated;
    }

    public String getFormattedActivated() {
        return formatGermanDate(activated);
    }

    public String getFormattedDeActivated() {
        return formatGermanDate(deActivated);
    }

    @XmlElement
    public long getLimit() {
        return limit;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            User otherUser = (User) other;
            return getId().equals(otherUser.getId());
        }
        return false;
    }

    @XmlElement
    public String getFacebook() {
        if (facebook == null && f != null) {
            facebook = Joiner.on("").join(f);
        }
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    @XmlElement
    public String getSkype() {
        if (skype == null && s != null) {
            skype = Joiner.on("").join(s);
        }
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }
}
