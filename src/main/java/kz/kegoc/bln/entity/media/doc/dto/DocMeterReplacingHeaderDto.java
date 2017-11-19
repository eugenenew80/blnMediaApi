package kz.kegoc.bln.entity.media.doc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import kz.kegoc.bln.entity.media.Lang;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocMeterReplacingHeaderDto {
    private Long id;
    private String name;
    private Lang lang;
    private LocalDate docDate;
    private Long meteringPointId;
    private String meteringPointCode;
    private String meteringPointExternalCode;
    private String meteringPointName;
    private Long oldMeterId;
    private String oldMeterCode;
    private String oldMeterName;
    private String oldMeterSerialNumber;
    private Long newMeterId;
    private String newMeterCode;
    private String newMeterName;
    private String newMeterSerialNumber;
}
