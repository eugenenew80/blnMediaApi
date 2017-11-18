package kz.kegoc.bln.entity.media.raw;

import kz.kegoc.bln.entity.media.DataSource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.time.LocalDateTime;

import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.Metering;

@Data
@EqualsAndHashCode(of= {"id"})
public class HourMeteringFlowRaw implements Metering {
	private Long id;
	private LocalDate meteringDate;
	private Integer hour;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private DataSource dataSource;
	private DataStatus status;
	private Double val;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
}
