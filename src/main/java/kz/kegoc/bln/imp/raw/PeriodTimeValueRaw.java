package kz.kegoc.bln.imp.raw;

import kz.kegoc.bln.entity.common.*;
import kz.kegoc.bln.entity.data.Batch;
import kz.kegoc.bln.entity.data.InputMethod;
import kz.kegoc.bln.entity.data.ReceivingMethod;
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
