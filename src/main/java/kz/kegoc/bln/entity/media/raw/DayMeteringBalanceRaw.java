package kz.kegoc.bln.entity.media.raw;

import java.time.*;

import kz.kegoc.bln.entity.media.DataSource;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.entity.media.WayEntering;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class DayMeteringBalanceRaw implements Metering {
	private Long id;
	private LocalDateTime meteringDate;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private DataSource dataSource;
	private WayEntering wayEntering;
	private DataStatus status;
	private Double val;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
}
