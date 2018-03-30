package kz.kegoc.bln.common.interfaces;

public interface MeteringValue extends HasId, HasDates {
	String getSourceParamCode();
    void setSourceParamCode(String sourceParamCode);

    String getSourceUnitCode();
    void setSourceUnitCode(String sourceUnitCode);
}
