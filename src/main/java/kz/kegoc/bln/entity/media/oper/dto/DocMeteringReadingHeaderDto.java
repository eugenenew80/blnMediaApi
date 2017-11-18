package kz.kegoc.bln.entity.media.oper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocMeteringReadingHeaderDto  {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long templateId;
    private String templateName;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
