package kz.kegoc.bln.producer.emcos.helper.impl;

import com.google.common.collect.BiMap;
import jodd.util.StringUtil;
import kz.kegoc.bln.annotation.ParamCodes;
import kz.kegoc.bln.producer.emcos.helper.*;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class EmcosCfgServiceImpl implements EmcosCfgService {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public List<EmcosPointCfg> requestCfg()  {
        try {
            String answer = new HttpReqesterImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(buildBody())
                .build()
                .doRequest();

            return parseAnswer(answer);
        }

        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String buildBody() {
        String data = registryTemplate.getTemplate("EMCOS_REQCFG_DATA")
        	.replace("#points#", "");

        String property = registryTemplate.getTemplate("EMCOS_REQCFG_PROPERTY")
        	.replace("#user#", config.getUser())
        	.replace("#isPacked#", config.getIsPacked().toString())
        	.replace("#func#", "REQCFG")
        	.replace("#attType#", config.getAttType());
        
        String body = registryTemplate.getTemplate("EMCOS_REQCFG_BODY")
        	.replace("#property#", property)
        	.replace("#data#", Base64.encodeBase64String(data.getBytes()));

        return body;
    }
    
    private List<EmcosPointCfg> parseAnswer(String answer) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(new InputSource(new StringReader( new String(Base64.decodeBase64(answer), "Cp1251") )));

        NodeList nodes =  doc.getDocumentElement().getParentNode()
            .getFirstChild()
            .getChildNodes();    	
        
        List<EmcosPointCfg> pointsCfg = new ArrayList<>();
        for(int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeName() == "ROWDATA") {
                NodeList rowData = nodes.item(i).getChildNodes();
                for(int j = 0; j < rowData.getLength(); j++) {
                    if (rowData.item(j).getNodeName() == "ROW") {
                        EmcosPointCfg pointCfg = parseNode(rowData.item(j));
                        if (StringUtil.isNotEmpty(pointCfg.getParamCode()))
                            pointsCfg.add(pointCfg);
                    }
                }
            }
        }    
        
        return pointsCfg;
    }

    private EmcosPointCfg parseNode(Node node) {
        EmcosPointCfg pointCfg = new EmcosPointCfg();

        String pointCode = node.getAttributes()
                .getNamedItem("POINT_CODE")
                .getNodeValue();

        String emcosParamCode = node.getAttributes()
                .getNamedItem("ML_ID")
                .getNodeValue();

        String unitCode = node.getAttributes()
                .getNamedItem("EU_CODE")
                .getNodeValue();

        pointCfg.setPointCode(pointCode);
        pointCfg.setEmcosParamCode(emcosParamCode);
        pointCfg.setUnitCode(unitCode);

        if (paramCodes.containsValue(emcosParamCode))
            pointCfg.setParamCode(paramCodes.inverse().get(emcosParamCode));

        return pointCfg;
    }


    @Inject
    private RegistryTemplate registryTemplate;

    @Inject
    private EmcosConfig config;

    @Inject @ParamCodes
    private BiMap<String, String> paramCodes;
}
