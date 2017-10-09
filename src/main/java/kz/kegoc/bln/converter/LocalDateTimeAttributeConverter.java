package kz.kegoc.bln.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Date> { 
	
	public Date convertToDatabaseColumn(LocalDateTime locDateTime) {
		return (locDateTime == null ? null : Date.from(locDateTime.toInstant(ZoneOffset.UTC )));
	}
	
	public LocalDateTime convertToEntityAttribute(Date date) {
		return (date == null ? null : LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
	}
}