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
    @Test
    public void dateMidnightToXml() {
        assertThat(toXml(MidnightValue.midnightValue(new DateMidnight(2013, 2, 2))), containsString("2013-02-02T00:00:00+01:00"));
    }

    @Test
    public void dateMidnightFromXml() {
        assertThat(fromXml(xml("2013-02-02T00:00:00+01:00"), MidnightValue.class).toTest, is(new DateMidnight(2013, 2, 2)));
    }

    @Test
    public void dateTimeToXml() {
        assertThat(toXml(DateTimeValue.dateTimeValue(new DateTime(2013, 2, 2, 23, 1, 2, 59))), containsString("2013-02-02T23:01:02.059+01:00"));
    }

    @Test
    public void dateTimeFromXml() {
        assertThat(fromXml(xml("2013-02-02T23:01:02.059+01:00"), DateTimeValue.class).toTest, is(new DateTime(2013, 2, 2,23,1,2,59)));
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

        private static MidnightValue midnightValue(DateMidnight dateMidnight) {
            MidnightValue midnightValue = new MidnightValue();
            midnightValue.toTest = dateMidnight;
            return midnightValue;
        }
    }

    @XmlRootElement(name = "value")
    @XmlAccessorType(XmlAccessType.PROPERTY)
    private static class DateTimeValue {
        @XmlElement
        @XmlJavaTypeAdapter(DateTimeAdapter.class)
        public DateTime toTest;

        private static DateTimeValue dateTimeValue(DateTime dateTime) {
            DateTimeValue midnightValue = new DateTimeValue();
            midnightValue.toTest = dateTime;
            return midnightValue;
        }
    }

}
