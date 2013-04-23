package de.hh.changeRing.user;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import de.bripkens.gravatar.DefaultImage;
import de.bripkens.gravatar.Gravatar;
import de.bripkens.gravatar.Rating;
import de.hh.changeRing.BaseEntity;
import de.hh.changeRing.advertisement.Advertisement;
import de.hh.changeRing.calendar.Event;
import de.hh.changeRing.infrastructure.JaxBUtils;
import de.hh.changeRing.infrastructure.eclipselink.MappingCustomizer;
import de.hh.changeRing.transaction.Transaction;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.persistence.annotations.Customizer;
import org.joda.time.DateMidnight;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.*;

import static com.google.common.collect.Iterables.concat;
import static de.hh.changeRing.Context.formatGermanDate;
import static de.hh.changeRing.user.User.Status.active;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.SECOND;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.DATE;

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

@Entity(name = "tr_user")
@Customizer(MappingCustomizer.class)
@XmlAccessorType(XmlAccessType.PROPERTY)
@DiscriminatorColumn(name = "type") //art
@NamedQueries({
        @NamedQuery(name = "loginWithEmail", query = "select user from tr_user user where user.email =:email"),
        @NamedQuery(name = "findOthers", query = "select user from tr_user user where user <> :me order by user.id"),
        @NamedQuery(name = "allUsers", query = "select user from tr_user user order by user.id"),
        @NamedQuery(name = "newestUser", query = "select user from tr_user user order by user.activated desc")
})
public abstract class User extends BaseEntity {
    private String nickName;

    private int userGroup; // gruppe

    // TODO mhoennig: converter from Boolean
    private char payd; // bezahlt -- was auch immer, wann auch immer?!?

    @XmlElement
    private String firstName;

    private boolean firstNameVisible = true;

    @XmlElement
    private String lastName;

    private boolean lastNameVisible = true;

    //Legacy = md5
    private String passwordHash;

    private String email;

    private boolean emailVisible = true;

    private boolean addressVisible = true;

    @XmlElement
    @Transient
    private List<String> e;

    @XmlElement
    @Transient
    private List<String> f;

    @XmlElement
    @Transient
    private List<String> s;

    private String street = "";

    private String houseNumber = "";

    private int plz;

    private String city = "";

    private String district;

    private String phone;

    private String phoneJob;

    private String phoneMobile;

    private String fax;

    private String url;

    private String urlDescription;

    @Column(scale = 2, precision = 7)
    private final BigDecimal euroBalance = new BigDecimal("0.00");

    private DateMidnight birthDay;

    @Column(scale = 2, precision = 7)
    private final BigDecimal euroFee = new BigDecimal("6.00");

    // TODO mhoennig: proper name as soon as we know what this is
    private long umlauf; // ?!? 0/10/20

    private long fee;

    @Column(scale = 2, precision = 7)
    private BigDecimal minBalance;

    @Column(scale = 2, precision = 7)
    private BigDecimal maxBalance;

    @Column(length = 512)
    private String profile;

    private String accessibility;

    @Enumerated(STRING)
    // TODO mhoennig: converter to use single char like in tron database
    private Status status = active;

    @Temporal(DATE)
    private Date activated = new Date();

    @Temporal(DATE)
    private Date deActivated;

    @Temporal(DATE)
    private Date lastLogin;

    @Temporal(DATE)
    private Date lastWork;

    @Transient
    private List<DepotItem> depotItems;

    @SuppressWarnings("JpaDataSourceORMInspection")
    @OneToMany(cascade = PERSIST)
    @JoinColumn(name = "to_user_id")
    private List<Transaction> receivedTransactions = new ArrayList<Transaction>();

    @SuppressWarnings("JpaDataSourceORMInspection")
    @OneToMany(cascade = PERSIST)
    @JoinColumn(name = "from_user_id")
    private List<Transaction> sentTransactions = new ArrayList<Transaction>();

    @Column(scale = 2, precision = 7)
    private BigDecimal initialBalance; // startKapital 

    @Column(scale = 2, precision = 7)
    private BigDecimal balance = new BigDecimal("0.00");

    private String facebook;

    private String skype;

    @SuppressWarnings("JpaDataSourceORMInspection")
    @OneToMany(cascade = PERSIST)
    @JoinColumn(name = "owner_user_id")
    private final List<Advertisement> advertisements = new ArrayList<Advertisement>();

    @SuppressWarnings("JpaDataSourceORMInspection")
    @OneToMany(cascade = PERSIST)
    @JoinColumn(name = "user_id")
    private List<Event> events = new ArrayList<Event>();


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

    @XmlElement
    public String getPassword() {
        throw new IllegalAccessError("password not readable, UserId:" + id);
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

    public BigDecimal getBalance() {
        return balance;
    }

    public static User dummyUser(Long i) {
        User user = new Member();
        user.id = i;
        user.email = "email" + i + "@sonst-was.de";
        user.setPassword("bll" + "lll");
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

        GregorianCalendar from = new GregorianCalendar();
        from.set(Calendar.HOUR_OF_DAY, 19);
        from.set(Calendar.MINUTE, 0);
        from.add(DAY_OF_MONTH, -30);
        from.add(SECOND, i.intValue());
        user.activated = from.getTime();

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

    public List<DepotItem> getDepotItems() {
        if (depotItems == null) {
            depotItems = Lists.newArrayList();
            for (Transaction transaction : concat(receivedTransactions, sentTransactions)) {
                depotItems.add(transaction.toDepotItem(this));
            }
            sortDepot();
        }
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

    public String getGravatarHeaderUrl51() {
        return getGravatarUrl(51);
    }

    public String getGravatarUrl(int size) {
        return new Gravatar().setSize(size).setHttps(true).setRating(Rating.PARENTAL_GUIDANCE_SUGGESTED)
                .setStandardDefaultImage(DefaultImage.MONSTER).getUrl(getEmail());
    }

    public String getDisplayName() {
        return getName();
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

    public DepotItem getNewestDepotItem() {
        List<DepotItem> sorted = new Ordering<DepotItem>() {
            @Override
            public int compare(DepotItem o1, DepotItem o2) {
                return o2.getTransaction().getDate().compareTo(o1.getTransaction().getDate());
            }
        }.sortedCopy(getDepotItems());
        return sorted.isEmpty() ? null : sorted.get(0);
    }

    public String getLink() {
        return "/internal/members/user.xhtml?userId=" + getId();
    }

    public void initialStuffAfterParsing() {
        getEmail();
        getFacebook();
        getSkype();
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isFeeApplicable() {
        return false;
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
                "  id=" + getId() +
                ", firstName='" + firstName + '\'' +
                ", firstNameVisible=" + firstNameVisible +
                ", lastName='" + lastName + '\'' +
                ", lastNameVisible=" + lastNameVisible +
                ", passwordHash='" + passwordHash + '\'' +
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
    @XmlJavaTypeAdapter(JaxBUtils.DateMidnightAdapter.class)
    public DateMidnight getBirthDay() {
        return birthDay;
    }

    public String getFormattedBirthDay() {
        return formatGermanDate(birthDay);
    }

    public void setBirthDay(DateMidnight birthDay) {
        this.birthDay = birthDay;
    }

    @XmlElement
    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @XmlElement
    public String getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(String accessibility) {
        this.accessibility = accessibility;
    }

    @XmlElement
    public BigDecimal getEuroBalance() {
        return euroBalance;
    }

    @XmlElement
    public BigDecimal getEuroFee() {
        return euroFee;
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

    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setPassword(String password) {
        this.passwordHash = hashPassword(id, password);
    }

    static String hashPassword(Long id, String password) {
        return DigestUtils.md5Hex(id + password);
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public List<Transaction> getReceivedTransactions() {
        return receivedTransactions;
    }

    public List<Transaction> getSentTransactions() {
        return sentTransactions;
    }
}
