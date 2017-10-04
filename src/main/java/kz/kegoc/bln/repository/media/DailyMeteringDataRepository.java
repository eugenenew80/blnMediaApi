package kz.kegoc.bln.repository.media;

import java.util.List;
import kz.kegoc.bln.entity.media.DailyMeteringData;

public interface DailyMeteringDataRepository {
	DailyMeteringData insert(DailyMeteringData entity); 

	void insertAll(List<DailyMeteringData> list);
}
