package kz.kegoc.bln.service.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocUnderAccountingCalcLine;
import kz.kegoc.bln.repository.media.oper.DocUnderAccountingCalcLineRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.oper.DocUnderAccountingCalcLineService;
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
