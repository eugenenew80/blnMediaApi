package kz.kegoc.bln.service.doc.impl;

import kz.kegoc.bln.entity.doc.DocType;
import kz.kegoc.bln.repository.doc.DocTypeRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.common.Filter;
import kz.kegoc.bln.service.doc.DocTypeService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class DocTypeServiceImpl extends AbstractEntityService<DocType>
        implements DocTypeService {

	@Inject
    public DocTypeServiceImpl(DocTypeRepository repository, Validator validator, Filter<DocType> prePersistFilter) {
        super(repository, validator, prePersistFilter);
    }
}
