package kz.kegoc.bln.service.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocTemplate;
import kz.kegoc.bln.repository.media.oper.DocTemplateRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.oper.DocTemplateService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class DocTemplateServiceImpl
        extends AbstractEntityService<DocTemplate>
                implements DocTemplateService {

	@Inject
    public DocTemplateServiceImpl(DocTemplateRepository repository, Validator validator) {
        super(repository, validator);
    }
}
