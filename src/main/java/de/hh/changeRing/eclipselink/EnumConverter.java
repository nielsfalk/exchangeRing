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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.sessions.Session;

import de.hh.changeRing.DatabaseMappableEnum;

/**
* Converts enum instances whose enum implements DatabaseMappableEnum (business layer) to a database representation instances (database layer) and vice versa.
* <p/>
* <p>
* Use @Customizer(MappingCustomizer.class) at entity classes which need this converter,
* do NOT use @Converter/@Convert on the fields.
* </p>
*
* @author mhoennig
*/
public class EnumConverter<D> extends AbstractEclipseLinkConverter<DatabaseMappableEnum<D>, D>{

	private static final long serialVersionUID = -4989621363873951913L;

	private final Class<DatabaseMappableEnum<D>> enumClass;

    private final Method valuesMethod;
    
    /**
     * Constructor with enumeration class.
     * 
     * @param enumClass
     *            Enumeration class.
     */
    public EnumConverter(final Class<? extends DatabaseMappableEnum<D>> enumClass) {
        if (enumClass == null) {
            throw new IllegalArgumentException("enumClass == null");
        }
        this.enumClass = asDatabaseMappableEnumClass(enumClass);
        try {
            this.valuesMethod = enumClass.getMethod("values");
        } catch (final SecurityException ex) {
            throw new RuntimeException("Error getting method '" + enumClass.getName() + ".values() - (Should never happen...)", ex);
        } catch (final NoSuchMethodException ex) {
            throw new IllegalArgumentException("Standard enum method not found '" + enumClass.getName() + ".values() - make sure it's an enum type" + "' (Should never happen...)", ex);
        }
    }
    
	@Override
    public void initialize(DatabaseMapping dbMapping, Session session) {    	
    	super.initialize(dbMapping, session, enumClass, (Class<D>) getDatabaseMappableEnumGenericTypeArg(enumClass));
    }
    

	// because in Java, class X<? extends D> is not a subclass of X<D> 
    @SuppressWarnings("unchecked")
	private Class<DatabaseMappableEnum<D>> asDatabaseMappableEnumClass(Class<? extends DatabaseMappableEnum<D>> enumClass) {
    	return (Class<DatabaseMappableEnum<D>>) enumClass; 
    }

	private DatabaseMappableEnum<D>[] enumValues() {
        try {
            @SuppressWarnings("unchecked")
            DatabaseMappableEnum<D>[] dbValues = (DatabaseMappableEnum<D>[]) valuesMethod.invoke(enumClass, new Object[] {});
            return dbValues;
        } catch (final IllegalAccessException ex) {
            throw new RuntimeException("Access denied for standard enum method '" + enumClass.getName() + ".values()" + "' (Should never happen...)", ex);
        } catch (final InvocationTargetException ex) {
            throw new RuntimeException("Error calling standard enum method '" + enumClass.getName() + ".values()" + "' (Should never happen...)", ex);
        }
    }
	
    @SuppressWarnings("unchecked")
	private Class<D> getDatabaseMappableEnumGenericTypeArg(Class<? extends DatabaseMappableEnum<D>> enumClass) {
        final ParameterizedType pt = findDatabaseMappableEnumInterface(enumClass);
        final Type argType = pt.getActualTypeArguments()[0];
        return (Class<D>) argType;
	}

    private static ParameterizedType findDatabaseMappableEnumInterface(Class<? extends DatabaseMappableEnum<?>> clasz) {
        for (Type intf : clasz.getGenericInterfaces()) {
            if (intf instanceof ParameterizedType) {
                final ParameterizedType pt = (ParameterizedType) intf;
                if (DatabaseMappableEnum.class.isAssignableFrom((Class<?>) pt.getRawType())) {
                    return pt;
                }
            }
        }
        throw new IllegalArgumentException("can't determine generic parameter f DatabaseMappableEnum");
    }

    @Override
    protected DatabaseMappableEnum<D> toBusinessLayerType(D databaseValue) {
        final DatabaseMappableEnum<D>[] enumValues = enumValues();
        for (final DatabaseMappableEnum<D> enumValue : enumValues) {
            if (enumValue.getDatabaseValue().equals(databaseValue)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("Unknown database value '" + databaseValue + "' for " + enumClass.getName());
    }

    @Override
    protected D toDatabaseLayerType(DatabaseMappableEnum<D> enumValue) {
        D dbValue = enumValue.getDatabaseValue();
        return dbValue;
    }

}
