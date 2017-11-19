package kz.kegoc.bln.service.media.doc.impl;

import kz.kegoc.bln.entity.media.doc.*;
import kz.kegoc.bln.repository.media.doc.DocMeteringReadingLineRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.doc.DocMeteringReadingLineService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class DocMeteringReadingLineServiceImpl
    extends AbstractEntityService<DocMeteringReadingLine>
        implements DocMeteringReadingLineService {

	@Inject
    public DocMeteringReadingLineServiceImpl(DocMeteringReadingLineRepository repository, Validator validator) {
        super(repository, validator);
    }
}
