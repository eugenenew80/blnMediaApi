package kz.kegoc.bln.repository.media.oper;

import kz.kegoc.bln.entity.media.oper.DocMeterReplacingLine;
import kz.kegoc.bln.repository.common.Repository;
import javax.ejb.Local;
import java.util.List;

@Local
public interface DocMeterReplacingLineRepository extends Repository<DocMeterReplacingLine> {
    List<DocMeterReplacingLine> findByHeader(Long headerId);
}
