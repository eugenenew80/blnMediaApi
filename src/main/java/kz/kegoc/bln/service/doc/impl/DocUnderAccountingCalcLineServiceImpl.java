package kz.kegoc.bln.service.doc.impl;

import kz.kegoc.bln.entity.doc.DocUnderAccountingCalcLine;
import kz.kegoc.bln.repository.doc.DocUnderAccountingCalcLineRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.doc.DocUnderAccountingCalcLineService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class DocUnderAccountingCalcLineServiceImpl
        extends AbstractEntityService<DocUnderAccountingCalcLine>
                implements DocUnderAccountingCalcLineService {

	@Inject
    public DocUnderAccountingCalcLineServiceImpl(DocUnderAccountingCalcLineRepository repository, Validator validator) {
        super(repository, validator);
    }
}
