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

import com.google.common.collect.Maps;
import de.hh.changeRing.Context;
import de.hh.changeRing.reflection.Reflection;
import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/**
 * User this class on entity classes with the annotation @Customizer(MappingCustomizer) to apply some type conversion between database and business layer.
 *
 * @author mhoennig
 */
public class MappingCustomizer implements DescriptorCustomizer {
    private static final Logger LOGGER = Logger.getLogger(Context.class.getName());
    private static final Map<Class<?>, Converter> converters = initilizeConverters();

    private static Map<Class<?>, Converter> initilizeConverters() {
        ConcurrentMap<Class<?>, Converter> result = Maps.newConcurrentMap();
        result.put(DateTime.class, new JodaDateTimeConverter());
        result.put(LocalDate.class, new JodaLocalDateConverter());
        result.put(LocalTime.class, new JodaLocalTimeConverter());
        result.put(LocalDateTime.class, new JodaLocalDateTimeConverter());
        return result;
    }

    @Override
    public void customize(ClassDescriptor descriptor) throws Exception {
        LOGGER.fine("customize mapping for " + descriptor.getJavaClassName());
        for (DatabaseMapping mapping : descriptor.getMappings()) {
            customize(descriptor, mapping);
        }
    }

    private void customize(ClassDescriptor descriptor, DatabaseMapping mapping) {
        customizeJodaMappings(descriptor, mapping);
    }

    private final void customizeJodaMappings(ClassDescriptor descriptor, DatabaseMapping mapping) {
        Class<?> clazz = descriptor.getJavaClass();
        String attribName = mapping.getAttributeName();

        if (mapping instanceof DirectToFieldMapping) {
            DirectToFieldMapping dtfMapping = (DirectToFieldMapping) mapping;
            Field field = Reflection.forClass(clazz).findField(attribName);
            if (converters.containsKey(field.getType())) {
                dtfMapping.setConverter(converters.get(field.getType()));
            }
        }
    }
}
