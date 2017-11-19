package kz.kegoc.bln.entity.media.doc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocTemplateDto {
    private Long id;
    private String name;
    private String header;
    private Long docTypeId;
    private String docTypeName;
    private Long groupId;
    private String groupName;
}
