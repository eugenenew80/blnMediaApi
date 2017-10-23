package kz.kegoc.bln.producer.emcos.helper.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableList;
import kz.kegoc.bln.ejb.annotation.ParamCodes;
import kz.kegoc.bln.producer.emcos.helper.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.ejb.*;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Singleton
public class EmcosCfgServiceImpl implements EmcosCfgService {
    private static final Logger logger = LoggerFactory.getLogger(EmcosCfgServiceImpl.class);

    @Lock(LockType.WRITE)
    public List<EmcosPointCfg> request()  {
        logger.info("EmcosCfgServiceImpl.request started");

        if (pointsCfg==null && pointsCfg.isEmpty()) {
            logger.info("List of points not found in cache");
            try {
                String answer = new HttpReqesterImpl.Builder()
                    .url(new URL(config.getUrl()))
                    .method("POST")
                    .body(buildBody())
                    .build()
                    .doRequest();

                pointsCfg.addAll(parseAnswer(answer));
                pointsCfg.expire(1, TimeUnit.HOURS);
                logger.info("EmcosCfgServiceImpl.request successfully completed");
            }
            catch (Exception e) {
                logger.error("EmcosCfgServiceImpl.request failed: " + e.toString());
            }
        }

        return ImmutableList.copyOf(pointsCfg);
    }

    private String buildBody() {
        logger.debug("EmcosCfgServiceImpl.buildBody started");

        String data = registryTemplate.getTemplate("EMCOS_REQCFG_DATA")
        	.replace("#points#", "");
        logger.trace("data: " + data);

        String property = registryTemplate.getTemplate("EMCOS_REQCFG_PROPERTY")
        	.replace("#user#", config.getUser())
        	.replace("#isPacked#", config.getIsPacked().toString())
        	.replace("#func#", "REQCFG")
        	.replace("#attType#", config.getAttType());
        logger.trace("property: " + property);

        String body = registryTemplate.getTemplate("EMCOS_REQCFG_BODY")
        	.replace("#property#", property)
        	.replace("#data#", Base64.encodeBase64String(data.getBytes()));
        logger.trace("body: " + body);

        logger.debug("EmcosCfgServiceImpl.buildBody completed");
        return body;
    }
    
    private List<EmcosPointCfg> parseAnswer(String answer) throws Exception {
        logger.info("EmcosCfgServiceImpl.parseAnswer started");
        logger.trace("answer: " + new String(Base64.decodeBase64(answer), "Cp1251"));

        logger.debug("parsing xml started");
        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(new InputSource(new StringReader( new String(Base64.decodeBase64(answer), "Cp1251") )));
        logger.debug("parsing xml completed");
        
        
        logger.debug("convert xml to list started");
        NodeList nodes =  doc.getDocumentElement().getParentNode()
            .getFirstChild()
            .getChildNodes();    	
        
        List<EmcosPointCfg> list = new ArrayList<>();
        for(int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeName() == "ROWDATA") {
                NodeList rowData = nodes.item(i).getChildNodes();
                for(int j = 0; j < rowData.getLength(); j++) {
                    if (rowData.item(j).getNodeName() == "ROW") {
                    	logger.debug("row: " + (j+1));
                        EmcosPointCfg pointCfg = parseNode(rowData.item(j));
                        if (StringUtils.isNotEmpty(pointCfg.getParamCode()))
                            list.add(pointCfg);
                    }
                }
            }
        }
        logger.debug("convert xml to list completed");
        
        logger.info("EmcosCfgServiceImpl.parseAnswer completed, count of rows: " + list.size());
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
