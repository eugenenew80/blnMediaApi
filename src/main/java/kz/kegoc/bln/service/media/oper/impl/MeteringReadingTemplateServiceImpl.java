package kz.kegoc.bln.service.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.MeteringReadingTemplate;
import kz.kegoc.bln.repository.media.oper.MeteringReadingTemplateRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.oper.MeteringReadingTemplateService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class MeteringReadingTemplateServiceImpl
        extends AbstractEntityService<MeteringReadingTemplate>
                implements MeteringReadingTemplateService {

	@Inject
    public MeteringReadingTemplateServiceImpl(MeteringReadingTemplateRepository repository, Validator validator) {
        super(repository, validator);
    }
}
