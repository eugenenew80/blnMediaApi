package kz.kegoc.bln.producer.emcos.helper.impl;

import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.annotation.ParamCodes;
import kz.kegoc.bln.producer.emcos.helper.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Singleton
public class EmcosCfgServiceImpl implements EmcosCfgService {
    private static final Logger logger = LoggerFactory.getLogger(EmcosCfgServiceImpl.class);

    @Lock(LockType.WRITE)
    public List<EmcosPointCfg> requestCfg()  {
        logger.info("Request list of points started...");

        if (pointsCfg!=null && !pointsCfg.isEmpty()) {
            logger.info("Returning list of points from cache");
            return Collections.unmodifiableList(pointsCfg);
        }

        try {
            logger.info("Send http request for list of points...");
            String answer = new HttpReqesterImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(buildBody())
                .build()
                .doRequest();
            
            pointsCfg.addAll(parseAnswer(answer));
            pointsCfg.expire(1, TimeUnit.HOURS);
            logger.info("Request list of points completed");
        }

        catch (Exception e) {
            logger.error("Request list of points failed: " + e.toString());
        }

        return Collections.unmodifiableList(pointsCfg);
    }

    private String buildBody() {
        logger.info("Build body for request list of points...");

        String data = registryTemplate.getTemplate("EMCOS_REQCFG_DATA")
        	.replace("#points#", "");
        logger.debug("data: " + data);

        String property = registryTemplate.getTemplate("EMCOS_REQCFG_PROPERTY")
        	.replace("#user#", config.getUser())
        	.replace("#isPacked#", config.getIsPacked().toString())
        	.replace("#func#", "REQCFG")
        	.replace("#attType#", config.getAttType());
        logger.debug("property: " + property);

        String body = registryTemplate.getTemplate("EMCOS_REQCFG_BODY")
        	.replace("#property#", property)
        	.replace("#data#", Base64.encodeBase64String(data.getBytes()));
        logger.debug("body: " + body);

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

    @Inject
    private RList<EmcosPointCfg> pointsCfg;
}
