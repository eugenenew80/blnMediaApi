package kz.kegoc.bln.entity.media.doc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import kz.kegoc.bln.entity.media.Lang;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocMeteringReadingHeaderDto  {
    private Long id;
    private String name;
    private Lang lang;
    private LocalDate docDate;
    private Long docTypeId;
    private String docTypeName;
    private Long groupId;
    private String groupName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
