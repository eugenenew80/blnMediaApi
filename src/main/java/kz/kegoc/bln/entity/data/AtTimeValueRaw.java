package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.common.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class AtTimeValueRaw implements MeteringValue {
	private Long id;
	private SourceSystem sourceSystemCode;
	private String sourceMeteringPointCode;
	private String sourceParamCode;
	private String sourceUnitCode;
	private LocalDateTime meteringDate;
	private ReceivingMethod receivingMethod;
	private InputMethod inputMethod;
	private DataStatus status;
	private Double val;
	private Long batchId;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
}
