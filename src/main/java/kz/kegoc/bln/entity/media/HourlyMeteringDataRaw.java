package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.converters.LocalDateTimeAdapter;
import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@Data
@EqualsAndHashCode(of= {"id"})
public class HourlyMeteringDataRaw implements HasId {
	private Long id;
	
	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime meteringDate;
	
	private Byte hour;
	private String meteringPointCode;
	private String externalMeteringPointCode;
	private String paramCode;
	private String unitCode;
	private WayEnteringData wayEntering;
	private MeteringDataStatus status;
	private String dataSourceCode;
	private Double val;
}
