package kz.kegoc.bln.entity.media.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupMeteringPointDto {
    private Long id;
    private Long listOrder;
    private Long groupId;
    private Long meteringPointId;
    private String meteringPointCode;
    private String meteringPointExternalCode;
    private String meteringPointName;
	private LocalDate startDate;
	private LocalDate endDate; 
}
