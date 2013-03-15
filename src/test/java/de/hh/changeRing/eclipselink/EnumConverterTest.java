package de.hh.changeRing.eclipselink;

import static org.fest.assertions.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.persistence.mappings.converters.Converter;
import org.joda.time.LocalDate;
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