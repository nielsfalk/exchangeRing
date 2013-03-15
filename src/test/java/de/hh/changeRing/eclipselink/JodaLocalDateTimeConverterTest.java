package de.hh.changeRing.eclipselink;

/*
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
 */

import org.eclipse.persistence.mappings.converters.Converter;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.fest.assertions.Assertions.assertThat;

public class JodaLocalDateTimeConverterTest {
    private final Converter testConverter = new JodaLocalDateTimeConverter();
    private Date someDate;
    private LocalDateTime someDateTime;

    @Test
    public void convertDataValueToObjectValueTest() {
        assertThat(testConverter.convertDataValueToObjectValue(someDate, null)).isEqualTo(someDateTime);
    }

    @Test
    public void convertObjectValueToDataValueTest() {
        assertThat(testConverter.convertObjectValueToDataValue(someDateTime, null)).isEqualTo(someDate);
    }

    @Before
    public void init() throws ParseException {
        testConverter.initialize(null, null);
        someDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse("26.12.1996 12:50:00");
        someDateTime = new LocalDateTime(1996, 12, 26, 12, 50, 0, 0);
    }
}
