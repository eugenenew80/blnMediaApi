package kz.kegoc.bln.service.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocMeterReplacingHeader;
import kz.kegoc.bln.repository.media.oper.DocMeterReplacingHeaderRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.oper.DocMeterReplacingHeaderService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class DocMeterReplacingHeaderServiceImpl
        extends AbstractEntityService<DocMeterReplacingHeader>
                implements DocMeterReplacingHeaderService {

	@Inject
    public DocMeterReplacingHeaderServiceImpl(DocMeterReplacingHeaderRepository repository, Validator validator) {
        super(repository, validator);
    }
}
