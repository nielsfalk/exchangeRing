package de.hh.changeRing.infrastructure;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import static de.hh.changeRing.infrastructure.JaxBAdapters.DateMidnightAdapter;
import static de.hh.changeRing.infrastructure.JaxBAdapters.DateTimeAdapter;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class JaxBAdaptersTest {
    @Test
    public void dateMidnightToXml() {
        assertThat(toXml(midnightValue(new DateMidnight(2013, 2, 2))), containsString("2013-02-02T00:00:00+01:00"));
    }

    @Test
    public void dateMidnightFromXml() {
        assertThat(fromXml(xml("2013-02-02T00:00:00+01:00"), MidnightValue.class).toTest, is(new DateMidnight(2013, 2, 2)));
    }

    @Test
    public void dateTimeToXml() {
        assertThat(toXml(dateTimeValue(new DateTime(2013, 2, 2, 23, 1, 2, 59))), containsString("2013-02-02T23:01:02.059+01:00"));
    }

    @Test
    public void dateTimeFromXml() {
        assertThat(fromXml(xml("2013-02-02T23:01:02.059+01:00"), DateTimeValue.class).toTest, is(new DateTime(2013, 2, 2,23,1,2,59)));
    }


    private <T> String toXml(T element) {
        try {
            Marshaller marshaller = JAXBContext.newInstance(element.getClass()).createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(element, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }
    private  <T> T fromXml(String xml, Class<T> type) {
        try {
            //noinspection unchecked
            return (T)JAXBContext.newInstance(type).createUnmarshaller().unmarshal(new ByteArrayInputStream(xml.getBytes()));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private String xml(String toTest) {
        return "<value><toTest>" + toTest + "</toTest></value>";
    }

    private static MidnightValue midnightValue(DateMidnight dateMidnight) {
        MidnightValue midnightValue = new MidnightValue();
        midnightValue.toTest = dateMidnight;
        return midnightValue;
    }

    @XmlRootElement(name = "value")
    @XmlAccessorType(XmlAccessType.PROPERTY)
    private static class MidnightValue {
        @XmlElement
        @XmlJavaTypeAdapter(DateMidnightAdapter.class)
        public DateMidnight toTest;
    }

    private static DateTimeValue dateTimeValue(DateTime dateTime) {
        DateTimeValue midnightValue = new DateTimeValue();
        midnightValue.toTest = dateTime;
        return midnightValue;
    }

    @XmlRootElement(name = "value")
    @XmlAccessorType(XmlAccessType.PROPERTY)
    private static class DateTimeValue {
        @XmlElement
        @XmlJavaTypeAdapter(DateTimeAdapter.class)
        public DateTime toTest;
    }

}
