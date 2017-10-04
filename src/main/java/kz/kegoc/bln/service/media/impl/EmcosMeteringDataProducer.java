package kz.kegoc.bln.service.media.impl;

import kz.kegoc.bln.service.media.MeteringDataProducer;

public class EmcosMeteringDataProducer implements MeteringDataProducer {
	public void execute() {
		System.out.println("Emcos data producer started!");
	}
}
