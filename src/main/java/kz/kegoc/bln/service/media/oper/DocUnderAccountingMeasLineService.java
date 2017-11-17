package kz.kegoc.bln.service.media.oper;

import kz.kegoc.bln.entity.media.oper.DocUnderAccountingMeasLine;
import kz.kegoc.bln.service.common.EntityService;

import javax.ejb.Local;
import java.util.List;

@Local
public interface DocUnderAccountingMeasLineService extends EntityService<DocUnderAccountingMeasLine> {
    List<DocUnderAccountingMeasLine> findByHeader(Long headerId);
}
