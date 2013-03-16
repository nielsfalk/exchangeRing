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

import org.eclipse.persistence.mappings.converters.Converter;
import org.junit.Before;
import org.junit.Test;

import de.hh.changeRing.DatabaseMappableEnum;


public class EnumConverterTest {

	private final Converter testConverter = new EnumConverter<Character>(TestCharEnum.class);

    @Test
    public void convertDataValueToObjectValueTest() {
        assertThat(testConverter.convertDataValueToObjectValue('B', null)).isEqualTo(TestCharEnum.BLUE);
    }

    @Test
    public void convertObjectValueToDataValueTest() {
        assertThat(testConverter.convertObjectValueToDataValue(TestCharEnum.GREEN, null)).isEqualTo('G');
    }

    @Before
    public void init() throws ParseException {
        testConverter.initialize(null, null);
    }
}

enum TestCharEnum implements DatabaseMappableEnum<Character> {
	RED('R'), GREEN('G'), BLUE('B');

	private Character dbValue;

	TestCharEnum(Character dbValue) {
		this.dbValue = dbValue;
	}
	
	@Override
	public Character getDatabaseValue() {
		return dbValue;
	}
	

}