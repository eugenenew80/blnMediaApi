package kz.kegoc.bln.service.doc.impl;

import kz.kegoc.bln.entity.doc.DocUnderAccountingHeader;
import kz.kegoc.bln.repository.doc.DocUnderAccountingHeaderRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.doc.DocUnderAccountingHeaderService;

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
