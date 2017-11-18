package kz.kegoc.bln.mapper.impl;

import kz.kegoc.bln.entity.media.oper.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.media.oper.DocType;
import kz.kegoc.bln.mapper.EntityMapper;
import kz.kegoc.bln.service.media.oper.DocMeteringReadingHeaderService;
import kz.kegoc.bln.service.media.oper.DocTypeService;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class DocMeteringReadingHeaderMapper implements EntityMapper<DocMeteringReadingHeader> {
    public DocMeteringReadingHeader map(DocMeteringReadingHeader entity) {
        DocType docType = docTypeService.findByCode("DocMeteringReading");
        entity.setDocType(docType);

        if (entity.getLines()==null && entity.getId()!=null) {
            DocMeteringReadingHeader header = headerService.findById(entity.getId());
            entity.setLines(header.getLines());
        }

        return entity;
    }

    @Inject
    private DocTypeService docTypeService;

    @Inject
    private DocMeteringReadingHeaderService headerService;
}
