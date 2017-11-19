package kz.kegoc.bln.entity.media.doc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

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
