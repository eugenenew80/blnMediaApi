package kz.kegoc.bln.entity.media.oper;

import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Data
@EqualsAndHashCode(of= {"id"})
public class DocMeterReplacingHeader implements HasId {
    private Long id;

    @NotNull @Size(max = 100)
    private String name;

    @Size(max = 300)
    private String header;

    @NotNull
    private LocalDate docDate;

    @NotNull
    private DocType docType;

    @NotNull
    private MeteringPoint meteringPoint;

    @NotNull
    private Meter oldMeter;

    @NotNull
    private Meter newMeter;

    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<DocMeterReplacingLine> lines;
}
