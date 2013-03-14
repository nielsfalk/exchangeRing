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

import static org.fest.assertions.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.persistence.mappings.converters.Converter;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

public class JodaLocalTimeConverterTest {
	private Converter testConverter = JodaLocalTimeConverter.instance();
	private Date someDate;
	private LocalTime someDateTime;
	
	@Test
	public void convertDataValueToObjectValueTest() {		
		assertThat( testConverter.convertDataValueToObjectValue(someDate, null) ).isEqualTo(someDateTime);
	}

	@Test
	public void convertObjectValueToDataValueTest() {
		assertThat( testConverter.convertObjectValueToDataValue(someDateTime, null) ).isEqualTo(someDate);
	}
	
	@Before
	public void init() throws ParseException {
		testConverter.initialize(null, null);
		someDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 12:50:42");
		someDateTime = new LocalTime(12, 50, 42);
	}
}
