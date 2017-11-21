package kz.kegoc.bln.service.doc.impl;

import kz.kegoc.bln.entity.doc.*;
import kz.kegoc.bln.repository.doc.DocMeteringReadingLineRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.doc.DocMeteringReadingLineService;
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

    public DocMeteringReadingLine create(DocMeteringReadingLine entity) {
	    return super.create(entity.calcFlow());
    }

    public DocMeteringReadingLine update(DocMeteringReadingLine entity) {
        return super.update(entity.calcFlow());
    }
}
