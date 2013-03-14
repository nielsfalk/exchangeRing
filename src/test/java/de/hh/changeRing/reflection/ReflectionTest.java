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

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

public class ReflectionTest {

	private Reflection reflection = Reflection.forClass(TestClassToReflect.class);

	@Test
	public void findFieldTest() {
		assertThat( reflection.findField("directField").getType() ).isEqualTo(int.class);
		assertThat( reflection.findField("indirectField").getType() ).isEqualTo(long.class);
		assertThat( reflection.findField("genericField1").getType() ).isEqualTo(Object.class);
		assertThat( reflection.findField("nonExistentField") ).isNull();
	}
	
	@Test
	public void getGenericTypeArgumentTest() {
		assertThat( reflection.getGenericTypeArgument(0) ).isEqualTo( String.class );
		assertThat( reflection.getGenericTypeArgument(1) ).isEqualTo( BigDecimal.class );
	}
}

class TestClassToReflect extends TestSuperclassToReflect<String, BigDecimal> {
	@SuppressWarnings("unused")
	private int directField;
}

class TestSuperclassToReflect<T1, T2> {
	@SuppressWarnings("unused")
	private long indirectField;
	
	public T1 genericField1;
	public T2 genericField2;
}
