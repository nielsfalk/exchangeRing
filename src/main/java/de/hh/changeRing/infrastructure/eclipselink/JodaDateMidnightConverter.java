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

import org.joda.time.DateMidnight;

import java.util.Date;

public class JodaDateMidnightConverter extends AbstractEclipseLinkConverter<DateMidnight, java.util.Date> {
	private static final long serialVersionUID = -5357838659653049340L;


	@Override
	protected DateMidnight toBusinessLayerType(Date dataValue) {
		return new DateMidnight(dataValue);
	}

	@Override
	protected Date toDatabaseLayerType(DateMidnight dataValue) {
		return dataValue.toDate();
	}
}
