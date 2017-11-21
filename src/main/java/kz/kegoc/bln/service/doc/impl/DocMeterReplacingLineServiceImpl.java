package kz.kegoc.bln.service.doc.impl;

import kz.kegoc.bln.entity.doc.DocMeterReplacingLine;
import kz.kegoc.bln.repository.doc.DocMeterReplacingLineRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.doc.DocMeterReplacingLineService;
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
