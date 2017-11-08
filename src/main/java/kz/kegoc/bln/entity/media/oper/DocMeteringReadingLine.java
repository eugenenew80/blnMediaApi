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
public class DocMeteringReadingLine implements HasId {
    private Long id;

    @NotNull
    private DocMeteringReadingHeader header;

    @NotNull
    private MeteringPoint meteringPoint;

    private Meter meter;

    @NotNull
    private LocalDate operDate;

    @NotNull
    private String paramCode;

    @NotNull
    private String unitCode;

    @NotNull
    private DataSource dataSource;

    @NotNull
    private WayEntering wayEntering;

    private Double startBalance;
    private Double endBalance;
    private Double flow;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
