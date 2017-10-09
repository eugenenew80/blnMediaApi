package kz.kegoc.bln.entity.media;

import java.time.LocalDateTime;

import kz.kegoc.bln.entity.common.HasCode;
import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class DailyMeteringDataRaw implements HasId, HasCode {
	private Long id;
	private LocalDateTime meteringDate;
	private String code;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private WayEnteringData wayEntering;
	private MeteringDataStatus status;
	private String dataSourceCode;
	private Double val;
}
