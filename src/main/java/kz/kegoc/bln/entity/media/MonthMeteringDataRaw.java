package kz.kegoc.bln.entity.media;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class MonthMeteringDataRaw implements MeteringData {
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
