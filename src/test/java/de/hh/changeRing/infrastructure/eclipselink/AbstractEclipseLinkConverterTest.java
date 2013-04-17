package de.hh.changeRing.infrastructure.eclipselink;

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

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class AbstractEclipseLinkConverterTest {

	private final AbstractEclipseLinkConverter<Integer, String> testConverter = new TestEclipseLinkConverter();

	@Test
	public void convertDataValueToObjectValueTest() {
		assertThat( testConverter .convertDataValueToObjectValue("123", null) ).isEqualTo(123);
	}

	@Test
	public void convertObjectValueToDataValueTest() {
		assertThat( testConverter.convertObjectValueToDataValue(4711, null) ).isEqualTo("4711");
	}
	
	@Before
	public void init() {
		testConverter.initialize(null, null);
	}
}

@SuppressWarnings("serial")
class TestEclipseLinkConverter extends AbstractEclipseLinkConverter<Integer, String> {

	@Override
	public String toDatabaseLayerType(Integer dataValue) {
		return Integer.toString(dataValue);
	}

	@Override
	public Integer toBusinessLayerType(String dataValue) {
		return Integer.parseInt(dataValue);
	}
	
}