package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.common.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class PeriodTimeValueRaw implements MeteringValue {
	private Long id;
	private SourceSystemEnum sourceSystemCode;
	private String sourceMeteringPointCode;
	private String sourceParamCode;
	private String sourceUnitCode;
	private LocalDateTime meteringDate;
	private ReceivingMethod receivingMethod;
	private InputMethod inputMethod;
	private ProcessingStatusEnum status;
	private Integer interval;
	private Double val;
	private Batch batch;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
}
