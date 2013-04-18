package de.hh.changeRing.infrastructure;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Date;


/**
 * ----------------GNU General Public License--------------------------------
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 * <p/>
 * ----------------in addition-----------------------------------------------
 * <p/>
 * In addition, each military use, and the use for interest profit will be excluded. Environmental damage caused by the
 * use must be kept as small as possible.
 */

public class JaxBUtils {
    static <T> String toXml(T element) {
        try {
            Marshaller marshaller = JAXBContext.newInstance(element.getClass()).createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(element, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    public static  <T> T fromXml(String xml, Class<T> type) {
        try {
            //noinspection unchecked
            return (T)JAXBContext.newInstance(type).createUnmarshaller().unmarshal(new ByteArrayInputStream(xml.getBytes()));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static class DateTimeAdapter extends XmlAdapter<Date, DateTime> {
        @Override
        public DateTime unmarshal(Date date) throws Exception {
            return new DateTime(date);
        }

        @Override
        public Date marshal(DateTime dateTime) throws Exception {
            return dateTime.toDate();
        }
    }

    public static class DateMidnightAdapter extends XmlAdapter<Date, DateMidnight> {
        @Override
        public DateMidnight unmarshal(Date date) throws Exception {
            return new DateMidnight(date);
        }

        @Override
        public Date marshal(DateMidnight dateMidnight) throws Exception {
            return dateMidnight.toDate();
        }
    }
}
