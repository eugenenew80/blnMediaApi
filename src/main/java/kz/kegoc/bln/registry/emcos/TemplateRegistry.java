package kz.kegoc.bln.registry.emcos;

public interface TemplateRegistry {
	void registerTemplate(String key, String template);
	
	void unRegisterTemplate(String key, String template);
	
	String getTemplate(String key);
}
