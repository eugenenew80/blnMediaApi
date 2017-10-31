package kz.kegoc.bln.repository.media.oper;

import kz.kegoc.bln.entity.media.oper.DayMeteringDataOper;
import kz.kegoc.bln.repository.common.Repository;

import java.util.List;

import javax.ejb.Local;

@Local
public interface DayMeteringDataOperRepository extends Repository<DayMeteringDataOper> {
	List<DayMeteringDataOper> selectByGroup(Long groupId);
}
