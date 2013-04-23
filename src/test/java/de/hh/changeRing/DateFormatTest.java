package de.hh.changeRing;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DateFormatTest {

    public static final DateTime DATE_TIME = new DateTime(2012, 12, 31, 23, 59, 59, 999);
    public static final String EXPECTED_FORMATTED_TIME = "23:59";
    public static final String EXPECTED_FORMATTED_DATE = "31.12.2012";

    @Test
    public void formatDate() {
        assertThat(Context.formatGermanTime(DATE_TIME.toDate()), is(EXPECTED_FORMATTED_TIME));
        assertThat(Context.formatGermanTime(DATE_TIME), is(EXPECTED_FORMATTED_TIME));
        assertThat(Context.formatGermanDate(DATE_TIME.toDate()), is(EXPECTED_FORMATTED_DATE));
        assertThat(Context.formatGermanDate(DATE_TIME), is(EXPECTED_FORMATTED_DATE));

    }
}
