package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.common.SourceSystemEnum;
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
