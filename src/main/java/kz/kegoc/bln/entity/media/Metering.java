package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.entity.common.HasDates;
import kz.kegoc.bln.entity.common.HasId;

public interface Metering extends HasId, HasDates {
    DataSource getDataSource();
    void setDataSource(DataSource dataSource);
    
	String getParamCode();
    void setParamCode(String paramCode);    

    String getUnitCode();
    void setUnitCode(String unitCode);  
}
