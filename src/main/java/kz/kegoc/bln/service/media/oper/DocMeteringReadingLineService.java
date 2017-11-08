package kz.kegoc.bln.service.media.oper;

import kz.kegoc.bln.entity.media.oper.DocMeteringReadingLine;
import kz.kegoc.bln.service.common.EntityService;

import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;

@Local
public interface DocMeteringReadingLineService extends EntityService<DocMeteringReadingLine> {
    List<DocMeteringReadingLine> createLines(Long headerId);
    List<DocMeteringReadingLine> autoFill(Long headerId);
}
