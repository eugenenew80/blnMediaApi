package kz.kegoc.bln.imp.raw;

import kz.kegoc.bln.common.interfaces.MeteringValue;
import kz.kegoc.bln.entity.media.Batch;
import kz.kegoc.bln.entity.media.InputMethod;
import kz.kegoc.bln.entity.media.ReceivingMethod;
import kz.kegoc.bln.common.enums.ProcessingStatusEnum;
import kz.kegoc.bln.common.enums.SourceSystemEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class AtTimeValueRaw implements MeteringValue {
	private Long id;
	private SourceSystemEnum sourceSystemCode;
	private String sourceMeteringPointCode;
	private String sourceParamCode;
	private String sourceUnitCode;
	private LocalDateTime meteringDate;
	private ReceivingMethod receivingMethod;
	private InputMethod inputMethod;
	private ProcessingStatusEnum status;
	private Double val;
	private Batch batch;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
}
