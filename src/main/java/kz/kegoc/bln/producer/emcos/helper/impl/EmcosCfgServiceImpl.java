package kz.kegoc.bln.producer.emcos.helper.impl;

import com.google.common.collect.BiMap;
import kz.kegoc.bln.annotation.ParamCodes;
import kz.kegoc.bln.producer.emcos.helper.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class EmcosCfgServiceImpl implements EmcosCfgService {
    private static Logger logger = LoggerFactory.getLogger(EmcosCfgServiceImpl.class);

    public List<EmcosPointCfg> requestCfg()  {
        logger.info("Request list of points started...");

        try {
            logger.info("send http request...");
            String answer = new HttpReqesterImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(buildBody())
                .build()
                .doRequest();

            List<EmcosPointCfg> list = parseAnswer(answer);
            logger.info("Request list of points completed");
            return list;
        }

        catch (Exception e) {
            logger.error("Request list of points failed: " + e.toString());
            return new ArrayList<>();
        }
    }

    private String buildBody() {
        logger.info("Build body for request list of points...");

        String data = registryTemplate.getTemplate("EMCOS_REQCFG_DATA")
        	.replace("#points#", "");
        logger.info("data: " + data);

        String property = registryTemplate.getTemplate("EMCOS_REQCFG_PROPERTY")
        	.replace("#user#", config.getUser())
        	.replace("#isPacked#", config.getIsPacked().toString())
        	.replace("#func#", "REQCFG")
        	.replace("#attType#", config.getAttType());
        logger.info("property: " + property);

        String body = registryTemplate.getTemplate("EMCOS_REQCFG_BODY")
        	.replace("#property#", property)
        	.replace("#data#", Base64.encodeBase64String(data.getBytes()));
        logger.info("body: " + body);

        return body;
    }
    
    private List<EmcosPointCfg> parseAnswer(String answer) throws Exception {
        logger.info("Parse answer for list of points...");
        //logger.info("answer: " + new String(Base64.decodeBase64(answer), "Cp1251"));

        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(new InputSource(new StringReader( new String(Base64.decodeBase64(answer), "Cp1251") )));

        NodeList nodes =  doc.getDocumentElement().getParentNode()
            .getFirstChild()
            .getChildNodes();    	
        
        List<EmcosPointCfg> list = new ArrayList<>();
        for(int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeName() == "ROWDATA") {
                NodeList rowData = nodes.item(i).getChildNodes();
                for(int j = 0; j < rowData.getLength(); j++) {
                    if (rowData.item(j).getNodeName() == "ROW") {
                        EmcosPointCfg pointCfg = parseNode(rowData.item(j));
                        if (StringUtils.isNotEmpty(pointCfg.getParamCode()))
                            list.add(pointCfg);
                    }
                }
            }
        }

        logger.info("Parse answer for list of points completed, count of rows: " + list.size());
        return list;
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
