package kz.kegoc.bln.common.interfaces;

import java.time.*;

public interface HasDates  {
	LocalDateTime getCreateDate();
	LocalDateTime getLastUpdateDate();
	
	void setCreateDate(LocalDateTime createDate);
	void setLastUpdateDate(LocalDateTime updateDate) ;
}
