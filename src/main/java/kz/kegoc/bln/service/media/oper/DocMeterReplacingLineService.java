package kz.kegoc.bln.service.media.oper;

import kz.kegoc.bln.entity.media.oper.DocMeterReplacingLine;
import kz.kegoc.bln.service.common.EntityService;
import javax.ejb.Local;
import java.util.List;

@Local
public interface DocMeterReplacingLineService extends EntityService<DocMeterReplacingLine> {
    List<DocMeterReplacingLine> findByHeader(Long headerId);
}