package kz.kegoc.bln.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.*;
import java.util.Date;

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {
	
	public Date convertToDatabaseColumn(LocalDate localDate) {
		return (localDate == null ? null : Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}
	
	public LocalDate convertToEntityAttribute(Date date) {
		return (date == null ? null : Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
	}
}