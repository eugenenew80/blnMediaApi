package kz.kegoc.bln.entity.media;

import java.util.Date;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class MinuteMeteringDataRaw implements HasId {
	private Long id;
	private Date meteringDate;
	private String meteringPointCode;
	private String externalMeteringPointCode;
	private String paramCode;
	private String unitCode;
	private WayEnteringData wayEntering;
	private MeteringDataStatus status;
	private String dataSourceCode;
	private Double val;
}
