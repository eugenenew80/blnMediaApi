package kz.kegoc.bln.entity.media;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class HourMeteringDataRaw implements MeteringData {
	private Long id;
	private LocalDate meteringDate;
	private Integer hour;
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
