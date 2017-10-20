package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.entity.common.HasDates;
import kz.kegoc.bln.entity.common.HasId;

public interface MeteringData extends HasId, HasDates {
    Double getVal();
    void setVal(Double val);
    
    String getExternalCode();
    void setExternalCode(String externalCode);
    
    String getParamCode();
    void setParamCode(String paramCode);    
}
