package kz.kegoc.bln.entity.common;

public interface MeteringValue extends HasId, HasDates {
    SourceSystem getSourceSystemCode();
    void setSourceSystemCode(SourceSystem sourceSystemCode);
    
	String getSourceParamCode();
    void setSourceParamCode(String sourceParamCode);

    String getSourceUnitCode();
    void setSourceUnitCode(String sourceUnitCode);
}
