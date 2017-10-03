package kz.kegoc.bln.entity.common;

import java.util.Date;

public interface HasDates  {
	Date getCreateDate();
	Date getUpdateDate();
	
	void setCreateDate(Date createDate);
	void setUpdateDate(Date updateDate) ;
}
