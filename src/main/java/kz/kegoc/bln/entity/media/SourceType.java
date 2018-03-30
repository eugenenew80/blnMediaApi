package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.enums.SourceSystemEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class SourceType {
    private Long id;
    private SourceSystemEnum sourceSystemCode;
    private ReceivingMethod receivingMethod;
    private InputMethod inputMethod;
}
