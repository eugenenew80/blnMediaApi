package kz.kegoc.bln.updater;

import javax.ejb.Local;

@Local
public interface MeteringDataUpdater<T> {
	void update();
}
