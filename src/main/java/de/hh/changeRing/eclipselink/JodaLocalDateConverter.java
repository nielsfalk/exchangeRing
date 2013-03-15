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
import org.joda.time.LocalDate;

/**
 * Converts Joda LocalDate instances (business layer) to Date instances (database layer).
 * <p/>
 * <p>
 * Use @Customizer(MappingCustomizer.class) at entity classes which need this converter,
 * do NOT use @Converter/@Convert on the fields.
 * </p>
 *
 * @author mhoennig
 */
public class JodaLocalDateConverter extends AbstractEclipseLinkConverter<LocalDate, java.util.Date> {
    private static final long serialVersionUID = -5357838659653049340L;
    private static JodaLocalDateConverter instance;

    @Override
    public org.joda.time.LocalDate toBusinessLayerType(java.util.Date dataValue) {
        return new org.joda.time.LocalDate(dataValue);
    }

    @Override
    public java.util.Date toDatabaseLayerType(org.joda.time.LocalDate objectValue) {
        return objectValue.toDateMidnight().toDate();
    }

    /**
     * @return the singleton of this converter
     */
    public static Converter instance() {
        if (instance == null) {
            instance = new JodaLocalDateConverter();
        }
        return instance;
    }

}
