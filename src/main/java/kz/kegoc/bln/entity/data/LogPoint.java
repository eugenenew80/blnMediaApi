package kz.kegoc.bln.entity.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class LogPoint {
    private Long id;
}
