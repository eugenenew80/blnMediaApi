package kz.kegoc.bln.entity.media.oper;

import java.util.List;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(of= {"id"})
public class Group implements HasId {
    private Long id;

    @NotNull @Size(max = 100)
    private String name;

    private List<GroupMeteringPoint> meteringPoints;
}
