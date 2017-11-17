package kz.kegoc.bln.service.media.oper;

import kz.kegoc.bln.entity.media.oper.DocUnderAccountingCalcLine;
import kz.kegoc.bln.service.common.EntityService;

import javax.ejb.Local;
import java.util.List;

@Local
public interface DocUnderAccountingCalcLineService extends EntityService<DocUnderAccountingCalcLine> {
    List<DocUnderAccountingCalcLine> findByHeader(Long headerId);
}
