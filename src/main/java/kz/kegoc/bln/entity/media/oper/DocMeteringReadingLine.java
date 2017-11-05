package kz.kegoc.bln.entity.media.oper;

import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.media.WayEntering;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class DocMeteringReadingLine implements HasId {
    private Long id;
    private DocMeteringReadingHeader header;
    private MeteringPoint meteringPoint;
    private Meter meter;
    private LocalDate operDate;
    private String paramCode;
    private String unitCode;
    private String dataSourceCode;
    private WayEntering wayEntering;
    private Double startBalance;
    private Double endBalance;
    private Double flow;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
