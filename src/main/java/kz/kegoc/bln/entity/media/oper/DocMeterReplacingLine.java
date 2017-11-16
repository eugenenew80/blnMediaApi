package kz.kegoc.bln.entity.media.oper;

import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.media.DataSource;
import kz.kegoc.bln.entity.media.WayEntering;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class DocMeterReplacingLine implements HasId {
    private Long id;

    @NotNull
    private DocMeterReplacingHeader header;

    @NotNull
    private String paramCode;

    private Double oldBalance;
    private Double newBalance;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
