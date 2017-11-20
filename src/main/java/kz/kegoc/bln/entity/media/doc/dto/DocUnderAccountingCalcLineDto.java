package kz.kegoc.bln.entity.media.doc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocUnderAccountingCalcLineDto {
    private Long id;
    private Long headerId;
    private String paramCode;
    private LocalDateTime turnOnTime;
    private LocalDateTime turnOffTime;
    private Double downtime;
    private Double value;
}
