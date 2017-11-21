package kz.kegoc.bln.entity.doc;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
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
