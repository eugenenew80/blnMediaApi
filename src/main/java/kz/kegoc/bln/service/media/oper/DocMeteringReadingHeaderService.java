package kz.kegoc.bln.service.media.oper;

import kz.kegoc.bln.entity.media.oper.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.media.oper.DocMeteringReadingLine;
import kz.kegoc.bln.service.common.EntityService;

import javax.ejb.Local;
import java.util.List;

@Local
public interface DocMeteringReadingHeaderService extends EntityService<DocMeteringReadingHeader> {
    List<DocMeteringReadingLine> autoFill(Long headerId);
}
