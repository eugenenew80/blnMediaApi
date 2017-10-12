package kz.kegoc.bln.producer.emcos;

public interface RegistryTemplate {
	void registerTemplate(String key, String template);
	void unRegisterTemplate(String key, String template);
	String getTemplate(String key);
}
