package kz.kegoc.bln.service.doc.impl;

import kz.kegoc.bln.entity.doc.DocUnderAccountingMeasLine;
import kz.kegoc.bln.repository.doc.DocUnderAccountingMeasLineRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.doc.DocUnderAccountingMeasLineService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class DocUnderAccountingMeasLineServiceImpl
        extends AbstractEntityService<DocUnderAccountingMeasLine>
                implements DocUnderAccountingMeasLineService {

	@Inject
    public DocUnderAccountingMeasLineServiceImpl(DocUnderAccountingMeasLineRepository repository, Validator validator) {
        super(repository, validator);
    }
}
