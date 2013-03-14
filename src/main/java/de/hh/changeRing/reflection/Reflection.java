package de.hh.changeRing.reflection;

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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Eases using Java reflection.  Implemntation so far is pretty simple, caching could be introduced if necessary.
 * 
 * @author mhoennig
 */
public class Reflection {

	private Class<?> clazz;

	/**
	 * Creates a reflection instance for the given class.
	 * 
	 * @param clazz the class to reflect
	 * @return the reflector for the class, newly created if not existing yet
	 */
	public static Reflection forClass(Class<?> clazz) {
		return new Reflection(clazz);
	}

	private Reflection(Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * @return the field with the given name in the class or first superclass which defines it, or null if no such field
	 * @param fieldName the name of the field
	 */
	public Field findField(String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchFieldException e) {
			if ( clazz.getSuperclass() != null ) {
				return forClass(clazz.getSuperclass()).findField(fieldName);
			}
			return null;
		}
	}

	/**
	 * For 'class Example extends SuperExample<A, B>' this method makes it possible to determine A.class and B.class.  
	 * 
	 * @param argNo the generic parameter number, starting with 0
	 * @return the generic type at position argNo
	 */
	public <C> Class<C> getGenericTypeArgument(int argNo) {	
		ParameterizedType genericSuperclass = ((ParameterizedType) clazz.getGenericSuperclass());
		Type parameterType = genericSuperclass.getActualTypeArguments()[argNo];
		@SuppressWarnings("unchecked")
		Class<C> parameterClass = (Class<C>) parameterType;
		return parameterClass;
	}

}
