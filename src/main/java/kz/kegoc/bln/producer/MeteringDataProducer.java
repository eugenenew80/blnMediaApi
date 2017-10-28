package kz.kegoc.bln.producer;

import javax.ejb.Local;

@Local
public interface MeteringDataProducer {
	void execute();
}
