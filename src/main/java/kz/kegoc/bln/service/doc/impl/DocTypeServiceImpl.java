package kz.kegoc.bln.service.doc.impl;

import kz.kegoc.bln.entity.doc.DocType;
import kz.kegoc.bln.service.common.EntityHelperService;
import kz.kegoc.bln.repository.doc.DocTypeRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.doc.DocTypeService;
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

    public DocType create(DocType entity) {
        return super.create(entityMapper.addDependencies(entity));
    }

    public DocType update(DocType entity) {
        return super.update(entityMapper.addDependencies(entity));
    }

    @Inject
    private EntityHelperService<DocType> entityMapper;
}
