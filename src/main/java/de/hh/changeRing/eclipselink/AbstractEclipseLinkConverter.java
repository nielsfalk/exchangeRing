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

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

import de.hh.changeRing.reflection.Reflection;

/** Represents a base class for EclipseLink type converters.
 * 
 * @author mhoennig
 *
 * @param <B> the type in the business layer 
 * @param <D> the type in the database layer
 */
public abstract class AbstractEclipseLinkConverter<B, D> implements Converter {

	private static final long serialVersionUID = 7625241047430215268L;

	private Class<B> businessLayerType;
	private Class<D> databaseLayerType;

	protected void initialize(DatabaseMapping dbMapping, Session session, Class<B> businessLayerType, Class<D> databaseLayerType ) {
		this.businessLayerType = businessLayerType;
		this.databaseLayerType = databaseLayerType;
		initializeImpl(dbMapping, session);
	}
	
	@Override
	public void initialize(DatabaseMapping dbMapping, Session session) {
		initializeImpl(dbMapping, session);
	}
	
	private void initializeImpl(DatabaseMapping dbMapping, Session session) {
		Reflection reflection = Reflection.forClass(getClass());
		if ( businessLayerType == null || databaseLayerType == null ) {
			businessLayerType = reflection.getGenericTypeArgument(0);
			databaseLayerType = reflection.getGenericTypeArgument(1);
		}
	}

    @Override
    @SuppressWarnings("unchecked")
    public final B convertDataValueToObjectValue(Object dataValue, Session session) {
        if (dataValue == null) {
            return null;
        }
        checkType(databaseLayerType, dataValue);
        return toBusinessLayerType((D) dataValue);
    }

	@Override
    @SuppressWarnings("unchecked")
    public final Object convertObjectValueToDataValue(Object objectValue, Session session) {
        if (objectValue == null) {
            return null;
        }
        checkType(businessLayerType, objectValue);
        return toDatabaseLayerType((B) objectValue);
    }


    private void checkType(Class<?> expectedType, Object value) {
    	if ( !expectedType.isAssignableFrom(value.getClass()) ) {
    		throw new IllegalArgumentException("expected " + expectedType + ", but got " + value.getClass());
    	}
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	protected abstract B toBusinessLayerType(D dataValue);

	protected abstract D toDatabaseLayerType(B dataValue);

}