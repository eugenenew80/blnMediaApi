package kz.kegoc.bln.service.media.doc.impl;

import kz.kegoc.bln.entity.media.doc.DocMeterReplacingLine;
import kz.kegoc.bln.repository.media.doc.DocMeterReplacingLineRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.doc.DocMeterReplacingLineService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class DocMeterReplacingLineServiceImpl
        extends AbstractEntityService<DocMeterReplacingLine>
                implements DocMeterReplacingLineService {

	@Inject
    public DocMeterReplacingLineServiceImpl(DocMeterReplacingLineRepository repository, Validator validator) {
        super(repository, validator);
    }
}
