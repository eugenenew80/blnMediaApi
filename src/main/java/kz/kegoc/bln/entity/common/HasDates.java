package kz.kegoc.bln.entity.common;

import java.time.LocalDate;

public interface HasDates  {
	LocalDate getCreateDate();
	LocalDate getUpdateDate();
	
	void setCreateDate(LocalDate createDate);
	void setUpdateDate(LocalDate updateDate) ;
}
