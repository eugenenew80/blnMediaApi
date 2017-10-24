package kz.kegoc.bln.producer.emcos.gateway.impl;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import kz.kegoc.bln.producer.emcos.gateway.TemplateRegistry;


@Stateless
public class TemplateRegistryImpl implements TemplateRegistry {
	private Map<String, String> templates = new HashMap<>();
	
	@PostConstruct
	public void init() {
        
		//REQML
		
		String template = ""
                + "<?xml version=\"1.0\" encoding=\"windows-1251\"?>"
                + "<DATAPACKET Version=\"2.0\">"
                + "<METADATA>"
                + "<FIELDS>"
                + "<FIELD attrname=\"PPOINT_CODE\" fieldtype=\"string\" required=\"true\" WIDTH=\"50\" />"
                + "<FIELD attrname=\"PML_ID\" fieldtype=\"fixed\" required=\"true\" WIDTH=\"6\" />"
                + "<FIELD attrname=\"PBT\" fieldtype=\"SQLdateTime\" />"
                + "<FIELD attrname=\"PET\" fieldtype=\"SQLdateTime\" />"
                + "</FIELDS>"
                + "<PARAMS LCID=\"0\" />"
                + "</METADATA>"
                + "<ROWDATA>"
                + "#points#"
                + "</ROWDATA>"
                + "</DATAPACKET>";		
        registerTemplate("EMCOS_REQML_DATA", template);
        
        
        template = ""
                + "<UserId>#user#</UserId>"
                + "<aPacked>#isPacked#</aPacked>"
                + "<Func>#func#</Func>"
                + "<Reserved></Reserved>"
                + "<AttType>#attType#</AttType>";        		
        registerTemplate("EMCOS_REQML_PROPERTY", template);        
        
        
        template = ""
                + "<?xml version=\"1.0\"?>"
                + "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<SOAP-ENV:Body>"
                + "<TransferEMCOSData xmlns=\"http://www.sigmatelas.lt/webservices\">"
                + "<parameters>"
                + "<aDProperty>"
                + "#property#"
                + "</aDProperty>"
                + "<aData>"
                + "#data#"
                + "</aData>"
                + "</parameters>"
                + "</TransferEMCOSData>"
                + "</SOAP-ENV:Body>"
                + "</SOAP-ENV:Envelope>";                
        registerTemplate("EMCOS_REQML_BODY", template);
        

        
        //REQCFG
        
        template = ""
    	 		+ "<?xml version=\"1.0\" encoding=\"windows-1251\"?>"
    	 		+ "<DATAPACKET Version=\"2.0\">"
    	 		+ "<METADATA>"
    	 		+ "<FIELDS>"
    	 		+ "<FIELD attrname=\"CFG\" fieldtype=\"fixed\" WIDTH=\"6\" />"
    	 		+ "</FIELDS>"
    	 		+ "<PARAMS LCID=\"0\" />"
    	 		+ "</METADATA>"
    	 		+ "<ROWDATA>"
    	 		+ "</ROWDATA>"
    	 		+ "</DATAPACKET>";			
        registerTemplate("EMCOS_REQCFG_DATA", template);    
        
        template = ""
                + "<UserId>#user#</UserId>"
                + "<aPacked>#isPacked#</aPacked>"
                + "<Func>#func#</Func>"
                + "<Reserved></Reserved>"
                + "<AttType>#attType#</AttType>";        		
        registerTemplate("EMCOS_REQCFG_PROPERTY", template);    
        
        template = ""
                + "<?xml version=\"1.0\"?>"
                + "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<SOAP-ENV:Body>"
                + "<TransferEMCOSData xmlns=\"http://www.sigmatelas.lt/webservices\">"
                + "<parameters>"
                + "<aDProperty>"
                + "#property#"
                + "</aDProperty>"
                + "<aData>"
                + "#data#"
                + "</aData>"
                + "</parameters>"
                + "</TransferEMCOSData>"
                + "</SOAP-ENV:Body>"
                + "</SOAP-ENV:Envelope>";                
        registerTemplate("EMCOS_REQCFG_BODY", template);        
	}
	
	
	public void registerTemplate(String key, String template) {
		templates.put(key, template);
	}

	public void unRegisterTemplate(String key, String template) {
		templates.remove(key);
	}
		
	public String getTemplate(String key) {
		return templates.get(key);
	}
}
