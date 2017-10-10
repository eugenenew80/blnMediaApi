package kz.kegoc.bln.converter.jpa;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Date> { 
	
	public Date convertToDatabaseColumn(LocalDateTime localDateTime) {
		return (localDateTime == null ? null : Date.from(localDateTime.toInstant(ZoneOffset.UTC )));
	}
	
	public LocalDateTime convertToEntityAttribute(Date date) {
		return (date == null ? null : LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
	}
}