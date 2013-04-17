package de.hh.changeRing.infrastructure.jaxB;

import org.joda.time.DateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

public class DateTimeAdapter extends XmlAdapter<Date, DateTime> {
    @Override
    public DateTime unmarshal(Date date) throws Exception {
        return new DateTime(date);
    }

    @Override
    public Date marshal(DateTime dateTime) throws Exception {
        return dateTime.toDate();
    }
}
