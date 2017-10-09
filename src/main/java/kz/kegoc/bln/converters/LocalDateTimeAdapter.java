package kz.kegoc.bln.converters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

	public LocalDateTime unmarshal(String dateString) throws Exception {
		return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);
	}

	public String marshal(LocalDateTime localDateTime) throws Exception {
		return DateTimeFormatter.ISO_DATE_TIME.format(localDateTime);
	}
}
