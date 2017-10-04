package kz.kegoc.bln.entity.media;

import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class DailyMeteringData {
	private Long id;
	private Date meteringDate;
	private String meteringPointCode;
	private String paramCode;
	private String unitCode;
	private String dataSourceCode;
	private MeteringDataStatus status;
	private Double val;
}
