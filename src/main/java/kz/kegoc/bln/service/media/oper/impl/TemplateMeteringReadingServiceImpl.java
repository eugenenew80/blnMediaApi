package kz.kegoc.bln.service.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.TemplateMeteringReading;
import kz.kegoc.bln.repository.media.oper.TemplateMeteringReadingRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.oper.TemplateMeteringReadingService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class TemplateMeteringReadingServiceImpl
        extends AbstractEntityService<TemplateMeteringReading>
                implements TemplateMeteringReadingService {

	@Inject
    public TemplateMeteringReadingServiceImpl(TemplateMeteringReadingRepository repository, Validator validator) {
        super(repository, validator);
    }
}
