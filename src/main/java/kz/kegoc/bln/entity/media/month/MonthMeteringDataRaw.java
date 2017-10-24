package kz.kegoc.bln.entity.media.month;

import java.time.LocalDateTime;

import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.entity.media.WayEntering;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class MonthMeteringDataRaw implements Metering {
	private Long id;
	private Short year;
	private Short month;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private String dataSourceCode;
	private WayEntering wayEntering;
	private DataStatus status;
	private Double val;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
}
