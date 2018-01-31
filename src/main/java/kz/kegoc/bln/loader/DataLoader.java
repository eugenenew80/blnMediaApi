package kz.kegoc.bln.loader;

import javax.ejb.Local;

@Local
public interface DataLoader<T> {
	void load();
}
