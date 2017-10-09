package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(of= {"id"})
public class HourlyMeteringDataRaw implements HasId {
	private Long id;
	private LocalDateTime meteringDate;
	private Byte hour;
	private String meteringPointCode;
	private String externalMeteringPointCode;
	private String paramCode;
	private String unitCode;
	private WayEnteringData wayEntering;
	private MeteringDataStatus status;
	private String dataSourceCode;
	private Double val;
}
