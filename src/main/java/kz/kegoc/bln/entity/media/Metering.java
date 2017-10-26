package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.entity.common.HasDates;
import kz.kegoc.bln.entity.common.HasId;

public interface Metering extends HasId, HasDates {
    String getDataSourceCode();
    void setDataSourceCode(String dataSourceCode);    

    WayEntering getWayEntering();
    void setWayEntering(WayEntering dataSourceCode);   
    
	String getParamCode();
    void setParamCode(String paramCode);    

    String getUnitCode();
    void setUnitCode(String unitCode);  
}
