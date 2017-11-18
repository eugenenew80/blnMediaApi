package kz.kegoc.bln.service.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocUnderAccountingHeader;
import kz.kegoc.bln.repository.media.oper.DocUnderAccountingHeaderRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.oper.DocUnderAccountingHeaderService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class DocUnderAccountingHeaderServiceImpl
        extends AbstractEntityService<DocUnderAccountingHeader>
                implements DocUnderAccountingHeaderService {

	@Inject
    public DocUnderAccountingHeaderServiceImpl(DocUnderAccountingHeaderRepository repository, Validator validator) {
        super(repository, validator);
    }
}