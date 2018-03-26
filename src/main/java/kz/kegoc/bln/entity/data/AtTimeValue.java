package kz.kegoc.bln.entity.data;

import java.time.*;

import kz.kegoc.bln.entity.adm.User;
import kz.kegoc.bln.entity.common.*;
import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.dict.Unit;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class AtTimeValue implements MeteringValue {
	private Long id;
	private SourceType sourceType;
	private String sourceMeteringPointCode;
	private String sourceParamCode;
	private String sourceUnitCode;
	private LocalDateTime meteringDate;
	private Integer interval;
	private Double val;
	private MeteringPoint meteringPoint;
	private Meter meter;
	private ProcessingStatusEnum status;
	private Long paramId;
	private Unit unit;
	private Batch batch;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
	private User createBy;
	private User lastUpdateBy;
}
