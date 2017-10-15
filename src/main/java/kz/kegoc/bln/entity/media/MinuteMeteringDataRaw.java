package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class MinuteMeteringDataRaw implements HasId {
	private Long id;
	private LocalDateTime meteringDate;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private String dataSourceCode;
	private WayEntering wayEntering;
	private DataStatus status;
	private Double val;
}
