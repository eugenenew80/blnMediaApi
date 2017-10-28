package kz.kegoc.bln.registry.emcos;

import javax.ejb.Local;

@Local
public interface TemplateRegistry {
	void registerTemplate(String key, String template);
	
	void unRegisterTemplate(String key, String template);
	
	String getTemplate(String key);
}
