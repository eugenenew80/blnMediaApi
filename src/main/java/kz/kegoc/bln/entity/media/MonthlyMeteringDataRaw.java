package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.entity.common.HasCode;
import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class MonthlyMeteringDataRaw implements HasId, HasCode {
	private Long id;
	private Short year;
	private Short month;
	private String code;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private WayEnteringData wayEntering;
	private MeteringDataStatus status;
	private String dataSourceCode;
	private Double val;
}
