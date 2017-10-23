package kz.kegoc.bln.producer.emcos.reader.helper.impl;

import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.annotation.ParamCodes;
import kz.kegoc.bln.ejb.annotation.EmcosParamUnits;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.LastLoadInfo;
import kz.kegoc.bln.producer.emcos.reader.helper.EmcosCfgService;
import kz.kegoc.bln.producer.emcos.reader.helper.EmcosConfig;
import kz.kegoc.bln.producer.emcos.reader.helper.EmcosDataService;
import kz.kegoc.bln.producer.emcos.reader.helper.EmcosPointCfg;
import kz.kegoc.bln.producer.emcos.reader.helper.MinuteMeteringDataDto;
import kz.kegoc.bln.producer.emcos.reader.helper.RegistryTemplate;
import kz.kegoc.bln.service.media.LastLoadInfoService;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.*;

@Singleton
public class EmcosDataServiceImpl implements EmcosDataService {
    private static final Logger logger = LoggerFactory.getLogger(EmcosDataServiceImpl.class);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");

    private List<LastLoadInfo> lastLoadInfoList;
    private List<EmcosPointCfg> pointsCfg;
    
    public List<MinuteMeteringDataDto> request(String paramCode, LocalDateTime requestedTime) {
        logger.info("EmcosDataServiceImpl.request started");
        logger.info("Param: " + paramCode);
        logger.info("Time: " + requestedTime);

        this.pointsCfg = emcosCfgService.request();
        if (pointsCfg==null || pointsCfg.isEmpty()) {
            logger.warn("List of points is empty, EmcosDataServiceImpl.request interrupted");
            return emptyList();
        }
        
        this.lastLoadInfoList = lastLoadInfoService.findAll();

        List<MinuteMeteringDataDto> list;
        try {
            logger.info("Send http request for metering data...");
            String body = buildBody(paramCodes.get(paramCode), requestedTime);
            if (StringUtils.isEmpty(body)) {
            	logger.info("Request body is empty, EmcosDataServiceImpl.request interrupted");
                return emptyList();
            }

            String answer = new HttpReqesterImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(body)
                .build()
                .doRequest();

            list = parseAnswer(answer);
            logger.info("EmcosDataServiceImpl.request competed");
        }

        catch (Exception e) {
            logger.error("EmcosDataServiceImpl.request failed: " + e.toString());
            list = emptyList();
        }

        return list;
    }

    private String buildBody(String emcosParamCode, LocalDateTime requestedTime) {
    	logger.debug("EmcosDataServiceImpl.buildBody started");

        String strPoints = pointsCfg.stream()
    		//.filter(p -> p.getPointCode().equals("120620300070020001") || p.getPointCode().equals("121420300070010003") )	
    		.filter(p -> p.getEmcosParamCode().equals(emcosParamCode))
            .map( p-> serializePointCfg(p, requestedTime))
            .filter(p -> StringUtils.isNotEmpty(p))
            .collect(Collectors.joining());

        if (StringUtils.isEmpty(strPoints)) {
        	logger.debug("List of points is empty, EmcosDataServiceImpl.buildBody interrupted");
            return "";
        }

        String data = registryTemplate.getTemplate("EMCOS_REQML_DATA")
        	.replace("#points#", strPoints);
        logger.trace("data: " + data);

        String property = registryTemplate.getTemplate("EMCOS_REQML_PROPERTY")
        	.replace("#user#", config.getUser())
        	.replace("#isPacked#", config.getIsPacked().toString())
        	.replace("#func#", "REQML")
        	.replace("#attType#", config.getAttType());
        logger.trace("property: " + property);

        String body = registryTemplate.getTemplate("EMCOS_REQML_BODY")
        	.replace("#property#", property)
        	.replace("#data#", Base64.encodeBase64String(data.getBytes()));
        logger.trace("body for request metering data: " + body);

        logger.debug("EmcosDataServiceImpl.buildBody completed");
        return body;
    }

    private List<MinuteMeteringDataDto> parseAnswer(String answer) throws Exception {
    	logger.info("EmcosDataServiceImpl.parseAnswer started");
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

        List<MinuteMeteringDataDto> list = new ArrayList<>();
        for(int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeName() == "ROWDATA") {
                NodeList rowData = nodes.item(i).getChildNodes();
                for(int j = 0; j < rowData.getLength(); j++) {
                    if (rowData.item(j).getNodeName() == "ROW") {
                    	logger.debug("row: " + (j+1));
                        list.add(parseNode(rowData.item(j)));
                    }
                }
            }
        }
        logger.debug("convert xml to list completed");
        
        logger.info("EmcosDataServiceImpl.parseAnswer completed, count of rows: " + list.size());
        return list;
    }

    private String serializePointCfg(EmcosPointCfg emcosCfg, LocalDateTime requestedTime) {
    	LastLoadInfo lastLoadInfo = lastLoadInfoList.stream()
    		.filter(t -> t.getExternalCode().equals(emcosCfg.getPointCode()) && t.getParamCode().equals(emcosCfg.getParamCode()) )
    		.findFirst()
    		.orElse(null);

        LocalDateTime startTime = buildStartTime(lastLoadInfo);
        if (startTime.isEqual(requestedTime) || startTime.isAfter(requestedTime))
            return "";

        return ""
		        + "<ROW PPOINT_CODE=\"" + emcosCfg.getPointCode() + "\" "
		        + "PML_ID=\"" + emcosCfg.getEmcosParamCode() + "\" "
		        + "PBT=\"" + startTime.format(timeFormatter) + "\" "
		        + "PET=\"" + requestedTime.format(timeFormatter) + "\" />";
    }

    
    private LocalDateTime buildStartTime(LastLoadInfo lastLoadInfo) {
        LocalDateTime startTime;
        if (lastLoadInfo!=null && lastLoadInfo.getLastLoadDate()!=null) {
            LocalDateTime lastLoadTime = lastLoadInfo.getLastLoadDate();            
            if (lastLoadTime.getMinute() < 45)
            	startTime = lastLoadTime.minusMinutes(lastLoadTime.getMinute());
            else
            	startTime = lastLoadTime.plusMinutes(15);
        }
        else {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC+1"));
            startTime =  LocalDateTime.of(
                    now.getYear(),
                    now.getMonth(),
                    now.getDayOfMonth(),
                    0,
                    0
            );
        }
        return startTime;
    }

    
    private MinuteMeteringDataDto parseNode(Node node) {
        String externalCode = node.getAttributes()
            .getNamedItem("PPOINT_CODE")
            .getNodeValue() ;

        String emcosParamCode = node.getAttributes()
            .getNamedItem("PML_ID")
            .getNodeValue() ;
        
        LocalDateTime time = null;
        String timeStr = node.getAttributes()
            .getNamedItem("PBT")
            .getNodeValue() ;

        if (timeStr!=null) {
            if (timeStr.indexOf("T")<0) {
                timeStr = timeStr+"T00:00:00000";
            }
            time = LocalDateTime.parse(timeStr, timeFormatter);
        }

        Double val = null;
        String valStr = node.getAttributes()
            .getNamedItem("PVAL")
            .getNodeValue();

        if (valStr!=null)
            val = Double.parseDouble(valStr);

        MinuteMeteringDataDto data = new MinuteMeteringDataDto();
        data.setExternalCode(externalCode);
        data.setMeteringDate(time);
        data.setWayEntering(WayEntering.EMCOS);
        data.setDataSourceCode("EMCOS");
        data.setStatus(DataStatus.RAW);
        data.setUnitCode(emcosParamUnits.get(emcosParamCode));
        data.setParamCode(paramCodes.inverse().get(emcosParamCode));
        data.setVal(val);
        
        return data;
    }


    @Inject
    private LastLoadInfoService lastLoadInfoService;

    @Inject
    private RegistryTemplate registryTemplate;

    @Inject
    private EmcosCfgService emcosCfgService;

    @Inject
    private EmcosConfig config;

    @Inject @ParamCodes
    private BiMap<String, String> paramCodes;

    @Inject @EmcosParamUnits
    private Map<String, String> emcosParamUnits;
}
