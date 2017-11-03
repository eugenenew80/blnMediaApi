package kz.kegoc.bln.service.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocType;
import kz.kegoc.bln.repository.media.oper.DocTypeRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.oper.DocTypeService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class DocTypeServiceImpl extends AbstractEntityService<DocType>
        implements DocTypeService {

	@Inject
    public DocTypeServiceImpl(DocTypeRepository repository, Validator validator) {
        super(repository, validator);
    }
}
