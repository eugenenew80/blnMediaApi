package kz.kegoc.bln.entity.media.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DailyMeteringDataDto {
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date date;
	
	private String code;
	private String param;
	private String unit;
	private Double val;
}
