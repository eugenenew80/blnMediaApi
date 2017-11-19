package kz.kegoc.bln.service.media.doc.impl;

import kz.kegoc.bln.entity.media.doc.DocMeterReplacingHeader;
import kz.kegoc.bln.repository.media.doc.DocMeterReplacingHeaderRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.doc.DocMeterReplacingHeaderService;

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
