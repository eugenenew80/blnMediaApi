package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.entity.media.MeteringDataStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(of= {"id"})
public class HourlyMeteringData {
	private Long id;
	private Date meteringDate;
	private Byte hour;
	private String meteringPointCode;
	private String paramCode;
	private String unitCode;
	private String dataSourceCode;
	private MeteringDataStatus status;
	private Double val;
}
