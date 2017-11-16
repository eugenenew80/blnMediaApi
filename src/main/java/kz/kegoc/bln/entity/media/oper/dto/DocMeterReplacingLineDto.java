package kz.kegoc.bln.entity.media.oper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.media.oper.DocMeterReplacingHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocMeterReplacingLineDto {
    private Long id;
    private Long headerId;
    private String paramCode;
    private Double oldBalance;
    private Double newBalance;
}
