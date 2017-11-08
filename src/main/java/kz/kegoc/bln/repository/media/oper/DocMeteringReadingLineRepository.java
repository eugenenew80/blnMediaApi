package kz.kegoc.bln.repository.media.oper;

import kz.kegoc.bln.entity.media.oper.DocMeteringReadingLine;
import kz.kegoc.bln.repository.common.Repository;

import javax.ejb.Local;
import java.time.LocalDate;
import java.util.List;

@Local
public interface DocMeteringReadingLineRepository extends Repository<DocMeteringReadingLine> {
    List<DocMeteringReadingLine> findByHeader(Long headerId);
}
