package kz.kegoc.bln.entity.media.doc;

import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.media.DataSource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotNull;
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
    private String paramCode;

    @NotNull
    private String unitCode;

    private DataSource dataSource;
    private Double startBalance;
    private Double endBalance;
    private Double flow;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;


    public DocMeteringReadingLine calcFlow() {
        if (getMeteringPoint().getMeteringPointType().getCode().equals("01")) {
            if (getStartBalance() == null)
                setStartBalance(0d);

            if (getEndBalance() == null)
                setEndBalance(0d);

            setFlow(Math.round((getEndBalance() - getStartBalance()) * 100d) / 100d);
        }

        if (getMeteringPoint().getMeteringPointType().getCode().equals("02")) {
            setStartBalance(null);
            setEndBalance(null);
        }

        return this;
    }
}
