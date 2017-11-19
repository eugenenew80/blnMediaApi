package kz.kegoc.bln.service.media.doc.impl;

import kz.kegoc.bln.entity.media.doc.DocUnderAccountingMeasLine;
import kz.kegoc.bln.repository.media.doc.DocUnderAccountingMeasLineRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.doc.DocUnderAccountingMeasLineService;

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
