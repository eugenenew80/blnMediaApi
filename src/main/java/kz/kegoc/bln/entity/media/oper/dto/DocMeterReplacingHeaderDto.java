package kz.kegoc.bln.entity.media.oper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.media.oper.DocMeterReplacingLine;
import kz.kegoc.bln.entity.media.oper.DocType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocMeterReplacingHeaderDto {
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
    private Long oldMeterId;
    private String oldMeterCode;
    private String oldMeterName;
    private String oldMeterSerialNumber;
    private Long newMeterId;
    private String newMeterCode;
    private String newMeterName;
    private String newMeterSerialNumber;
}
