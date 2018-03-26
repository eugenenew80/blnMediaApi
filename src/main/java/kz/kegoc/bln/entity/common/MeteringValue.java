package kz.kegoc.bln.entity.common;

public interface MeteringValue extends HasId, HasDates {
	String getSourceParamCode();
    void setSourceParamCode(String sourceParamCode);

    String getSourceUnitCode();
    void setSourceUnitCode(String sourceUnitCode);
}
