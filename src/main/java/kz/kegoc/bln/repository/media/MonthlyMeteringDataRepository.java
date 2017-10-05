package kz.kegoc.bln.repository.media;

import java.util.List;
import kz.kegoc.bln.entity.media.MonthlyMeteringData;

public interface MonthlyMeteringDataRepository {
	MonthlyMeteringData insert(MonthlyMeteringData entity); 

	void insertAll(List<MonthlyMeteringData> list);
}
