package kz.kegoc.bln.loader;

import javax.ejb.Local;

@Local
public interface MeteringDataLoader<T> {
	void load();
}
