package de.hh.changeRing.infrastructure.jsfExtension;

import de.hh.changeRing.Context;
import org.joda.time.DateMidnight;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

public class JodaConverters {
	@FacesConverter("midnightConverter")
	public static class UserConverter implements Converter{

		@Override
		public Object getAsObject(FacesContext context, UIComponent uiComponent, String s) {
			if (s.isEmpty()){
				return null;
			}
			return new DateMidnight(DateTimeFormat.forPattern(Context.GERMAN_DATE).parseDateTime(s));
		}

		@Override
		public String getAsString(FacesContext context, UIComponent uiComponent, Object o) {
			return DateTimeFormat.forPattern(Context.GERMAN_DATE).print((ReadableInstant) o);
		}
	}
}
