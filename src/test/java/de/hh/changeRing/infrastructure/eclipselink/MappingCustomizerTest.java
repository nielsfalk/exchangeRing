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

import static org.fest.assertions.Assertions.assertThat;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;

import com.sun.xml.ws.api.tx.at.Transactional;
import org.eclipse.persistence.annotations.Customizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.hh.changeRing.DatabaseMappableEnum;
import de.hh.changeRing.FunctionalTest;

@RunWith(Arquillian.class)
public class MappingCustomizerTest extends FunctionalTest {

	@Deployment
    public static Archive<?> createDeployment() {
        return functionalJarWithEntities().addClasses(TestEntity.class, DataPump.class);
    }

    @PersistenceContext
    private EntityManager entityManager;

    
	@Test
	public void testJodaLocalDateMappings() {
		assertConverter(JodaDateTimeConverter.class, TestEntity.class, "someDateTimeField");
		assertConverter(JodaLocalDateConverter.class, TestEntity.class, "someLocalDateField");
		assertConverter(JodaLocalTimeConverter.class, TestEntity.class, "someLocalTimeField");
		assertConverter(JodaLocalDateTimeConverter.class, TestEntity.class, "someLocalDateTimeField");		
	}

	@Test
	public void testEnumMapping() {
		assertConverter(EnumConverter.class, TestEntity.class, "someEnumField");		
	}

    @Test
    @Transactional
    public void readFromDb() {
        entityManager.getEntityManagerFactory().getCache().evictAll();
        entityManager.find(TestEntity.class, 1);
    }

	private void assertConverter(Class<? extends Converter> expectedConverterClass, Class<?> entityClass, String attributeName) {
		ClassDescriptor descriptor = getDescriptor(entityClass);
		DirectToFieldMapping dtf = (DirectToFieldMapping) descriptor.getMappingForAttributeName(attributeName);
		assertThat(dtf.getConverter()).isInstanceOf(expectedConverterClass);

	}

	private ClassDescriptor getDescriptor(Class<?> entityClass) {
		Object delegate = entityManager.getDelegate();
		org.eclipse.persistence.internal.jpa.EntityManagerImpl emi = (org.eclipse.persistence.internal.jpa.EntityManagerImpl)delegate;
		Session session = emi.getSession();
		ClassDescriptor descriptor = session.getDescriptor(entityClass);
		return descriptor;
	}
    @Entity
     @Customizer(MappingCustomizer.class)
     public static class TestEntity {
        @Id
        public int id;

        public DateTime someDateTimeField;
        public LocalDate someLocalDateField;
        public LocalTime someLocalTimeField;
        public LocalDateTime someLocalDateTimeField;

        public TestEnum someEnumField;
    }

    public static enum TestEnum implements DatabaseMappableEnum<String> {
        X("ixx"), Y("yps"), Z("zet");

        private String dbValue;

        TestEnum(String dbValue) {
            this.dbValue = dbValue;
        }

        @Override
        public String getDatabaseValue() {
            return dbValue;
        }
    }

    @Singleton
    @Startup
    public static class DataPump {
        @PersistenceContext
        EntityManager entityManager;

        @PostConstruct
        public void create() {
            TestEntity testEntity = new TestEntity();
            testEntity.id = 1;
            testEntity.someDateTimeField = new DateTime();
            testEntity.someLocalTimeField = new LocalTime();
            testEntity.someLocalDateField = new LocalDate();
            testEntity.someEnumField = TestEnum.Y;
            entityManager.persist(testEntity);
        }
    }
}

