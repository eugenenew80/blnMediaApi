package kz.kegoc.bln.service.media.doc.impl;

import kz.kegoc.bln.entity.media.doc.DocUnderAccountingHeader;
import kz.kegoc.bln.repository.media.doc.DocUnderAccountingHeaderRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.doc.DocUnderAccountingHeaderService;

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
