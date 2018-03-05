package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.adm.User;
import kz.kegoc.bln.entity.common.*;
import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.dict.Unit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class PeriodTimeValue implements MeteringValue {
	private Long id;
	private SourceSystemEnum sourceSystemCode;
	private String sourceMeteringPointCode;
	private String sourceParamCode;
	private String sourceUnitCode;
	private LocalDateTime meteringDate;
	private ReceivingMethod receivingMethod;
	private InputMethod inputMethod;
	private Integer interval;
	private ProcessingStatusEnum status;
	private Double val;
	private MeteringPoint meteringPoint;
	private Meter meter;
	private Long paramId;
	private Unit unit;
	private Batch batch;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
	private User createBy;
	private User lastUpdateBy;
}
