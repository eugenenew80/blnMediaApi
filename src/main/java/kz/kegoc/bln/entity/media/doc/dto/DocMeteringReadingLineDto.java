package kz.kegoc.bln.entity.media.doc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import kz.kegoc.bln.entity.media.DataSource;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocMeteringReadingLineDto {
	private Long id;
	private Long headerId;
	private Long meteringPointId;
	private String meteringPointCode;
	private String meteringPointExternalCode;
	private String meteringPointName;
	private Long meteringPointTypeId;
	private String meteringPointTypeCode;
	private String meteringPointTypeName;
	private Long meterId;
	private String meterCode;
	private String meterName;
	private String meterSerialNumber;
	private String paramCode;
	private String unitCode;
	private DataSource dataSource;
	private Double startBalance;
	private Double endBalance;
	private Double flow;
}
