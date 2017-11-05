package kz.kegoc.bln.service.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocMeteringReadingLine;
import kz.kegoc.bln.repository.media.oper.DocMeteringReadingLineRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.oper.DocMeteringReadingLineService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class DocMeteringReadingLineServiceImpl
        extends AbstractEntityService<DocMeteringReadingLine>
                implements DocMeteringReadingLineService {

	@Inject
    public DocMeteringReadingLineServiceImpl(DocMeteringReadingLineRepository repository, Validator validator) {
        super(repository, validator);
    }
}
