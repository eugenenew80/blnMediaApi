package kz.kegoc.bln.entity.common;

public interface Metering extends HasId, HasDates {
    DataSource getDataSourceCode();
    void setDataSourceCode(DataSource dataSourceCode);
    
	String getSourceParamCode();
    void setSourceParamCode(String sourceParamCode);

    String getSourceUnitCode();
    void setSourceUnitCode(String sourceUnitCode);
}
