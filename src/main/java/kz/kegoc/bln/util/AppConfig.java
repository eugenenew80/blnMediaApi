package kz.kegoc.bln.util;

import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class AppConfig {

	@PostConstruct
	private void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
}
