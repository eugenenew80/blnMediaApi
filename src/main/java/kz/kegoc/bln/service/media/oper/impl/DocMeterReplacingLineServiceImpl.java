package kz.kegoc.bln.service.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocMeterReplacingLine;
import kz.kegoc.bln.repository.media.oper.DocMeterReplacingLineRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.oper.DocMeterReplacingLineService;
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
