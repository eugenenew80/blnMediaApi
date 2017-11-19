package kz.kegoc.bln.entity.media.doc;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class DocUnderAccountingCalcLine implements HasId {
    private Long id;

    @NotNull
    private DocMeterReplacingHeader header;

    @NotNull
    private String paramCode;

    @NotNull
    private LocalDateTime turnOnTime;

    @NotNull
    private LocalDateTime turnOffTime;

    private Double downtime;
    private Double value;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
