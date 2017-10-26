package kz.kegoc.bln.entity.media.month;

import java.time.*;

import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.entity.media.WayEntering;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class MonthMeteringDataOper implements Metering {
	private Long id;
	private Long operYear;
	private Long operMonth;
	private String paramCode;
	private String unitCode;
	private String dataSourceCode;
	private WayEntering wayEntering;
	private Double val;
	private DataStatus status;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
}
