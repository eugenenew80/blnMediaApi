package kz.kegoc.bln.service.media.doc.impl;

import kz.kegoc.bln.entity.media.doc.DocType;
import kz.kegoc.bln.repository.media.doc.DocTypeRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.doc.DocTypeService;
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
