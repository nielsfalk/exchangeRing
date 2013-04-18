package de.hh.changeRing.infrastructure;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import static de.hh.changeRing.infrastructure.JaxBUtils.DateMidnightAdapter;
import static de.hh.changeRing.infrastructure.JaxBUtils.DateTimeAdapter;
import static de.hh.changeRing.infrastructure.JaxBUtils.fromXml;
import static de.hh.changeRing.infrastructure.JaxBUtils.toXml;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class JaxBUtilsTest {

    private static final String MIDNIGHT_IN_XML = "2013-02-02T00:00:00+01:00";
    private static final DateMidnight MIDNIGHT = new DateMidnight(2013, 2, 2);
    private static final DateTime DATE_TIME = new DateTime(2013, 2, 2, 23, 1, 2, 59);
    private static final String DATE_TIME_IN_XML = "2013-02-02T23:01:02.059+01:00";

    @Test
    public void dateMidnightToXml() {
        assertThat(toXml(MidnightValue.midnightValue()), containsString(MIDNIGHT_IN_XML));
    }

    @Test
    public void dateMidnightFromXml() {
        assertThat(fromXml(xml(MIDNIGHT_IN_XML), MidnightValue.class).toTest, is(MIDNIGHT));
    }

    @Test
    public void dateTimeToXml() {
        assertThat(toXml(DateTimeValue.dateTimeValue()), containsString(DATE_TIME_IN_XML));
    }

    @Test
    public void dateTimeFromXml() {
        assertThat(fromXml(xml(DATE_TIME_IN_XML), DateTimeValue.class).toTest, is(DATE_TIME));
    }


    private String xml(String toTest) {
        return "<value><toTest>" + toTest + "</toTest></value>";
    }

    @XmlRootElement(name = "value")
    @XmlAccessorType(XmlAccessType.PROPERTY)
    private static class MidnightValue {
        @XmlElement
        @XmlJavaTypeAdapter(DateMidnightAdapter.class)
        public DateMidnight toTest;

        private static MidnightValue midnightValue() {
            MidnightValue midnightValue = new MidnightValue();
            midnightValue.toTest = JaxBUtilsTest.MIDNIGHT;
            return midnightValue;
        }
    }

    @XmlRootElement(name = "value")
    @XmlAccessorType(XmlAccessType.PROPERTY)
    private static class DateTimeValue {
        @XmlElement
        @XmlJavaTypeAdapter(DateTimeAdapter.class)
        public DateTime toTest;

        private static DateTimeValue dateTimeValue() {
            DateTimeValue midnightValue = new DateTimeValue();
            midnightValue.toTest = JaxBUtilsTest.DATE_TIME;
            return midnightValue;
        }
    }

}
