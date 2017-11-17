package kz.kegoc.bln.entity.media.oper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocUnderAccountingHeaderDto {
    private Long id;
    private String name;
    private String header;
    private LocalDate docDate;
    private Long docTypeId;
    private String docTypeName;
    private Long meteringPointId;
    private String meteringPointCode;
    private String meteringPointExternalCode;
    private String meteringPointName;
    private Long meterId;
    private String meterCode;
    private String meterName;
    private String meterSerialNumber;
}
