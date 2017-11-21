package kz.kegoc.bln.service.doc.impl;

import kz.kegoc.bln.entity.doc.DocMeterReplacingHeader;
import kz.kegoc.bln.repository.doc.DocMeterReplacingHeaderRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.doc.DocMeterReplacingHeaderService;

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
