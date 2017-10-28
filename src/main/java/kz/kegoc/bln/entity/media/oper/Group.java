package kz.kegoc.bln.entity.media.oper;

import java.util.List;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class Group implements HasId {
    private Long id;
    private String name;
    private List<GroupMeteringPoint> meteringPoints;
}
