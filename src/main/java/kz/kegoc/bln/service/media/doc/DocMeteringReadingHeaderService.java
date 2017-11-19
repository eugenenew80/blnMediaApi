package kz.kegoc.bln.service.media.doc;

import kz.kegoc.bln.entity.media.doc.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.media.doc.DocMeteringReadingLine;
import kz.kegoc.bln.service.common.EntityService;

import javax.ejb.Local;
import java.util.List;

@Local
public interface DocMeteringReadingHeaderService extends EntityService<DocMeteringReadingHeader> {
    List<DocMeteringReadingLine> autoFill(Long headerId);
}
