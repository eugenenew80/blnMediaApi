package kz.kegoc.bln.service.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocMeteringReadingHeader;
import kz.kegoc.bln.repository.media.oper.DocMeteringReadingHeaderRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.oper.DocMeteringReadingHeaderService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class DocMeteringReadingHeaderServiceImpl
        extends AbstractEntityService<DocMeteringReadingHeader>
                implements DocMeteringReadingHeaderService {

	@Inject
    public DocMeteringReadingHeaderServiceImpl(DocMeteringReadingHeaderRepository repository, Validator validator) {
        super(repository, validator);
    }
}
