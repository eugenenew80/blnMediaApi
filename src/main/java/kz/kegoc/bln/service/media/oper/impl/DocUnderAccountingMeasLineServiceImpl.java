package kz.kegoc.bln.service.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocUnderAccountingHeader;
import kz.kegoc.bln.entity.media.oper.DocUnderAccountingMeasLine;
import kz.kegoc.bln.repository.media.oper.DocUnderAccountingMeasLineRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.oper.DocUnderAccountingHeaderService;
import kz.kegoc.bln.service.media.oper.DocUnderAccountingMeasLineService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.List;

@Stateless
public class DocUnderAccountingMeasLineServiceImpl
        extends AbstractEntityService<DocUnderAccountingMeasLine>
                implements DocUnderAccountingMeasLineService {

	@Inject
    public DocUnderAccountingMeasLineServiceImpl(DocUnderAccountingMeasLineRepository repository, Validator validator) {
        super(repository, validator);
        this.lineRepository = repository;
    }


    public List<DocUnderAccountingMeasLine> findByHeader(Long headerId) {
        DocUnderAccountingHeader header = headerService.findById(headerId);
        return header.getMeasLines();
    }


    private DocUnderAccountingMeasLineRepository lineRepository;

    @Inject
    private DocUnderAccountingHeaderService headerService;
}
