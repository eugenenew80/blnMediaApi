package kz.kegoc.bln.entity.common;

public interface MeteringValue extends HasId, HasDates {
    SourceSystemEnum getSourceSystemCode();
    void setSourceSystemCode(SourceSystemEnum sourceSystemCode);
    
	String getSourceParamCode();
    void setSourceParamCode(String sourceParamCode);

    String getSourceUnitCode();
    void setSourceUnitCode(String sourceUnitCode);
}
