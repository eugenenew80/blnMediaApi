package kz.kegoc.bln.imp;

import javax.ejb.Local;

@Local
public interface Importer {
	void execute();
}
