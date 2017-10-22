package kz.kegoc.bln.producer.emcos.helper.impl;

import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.annotation.ParamCodes;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.LastLoadInfo;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.producer.emcos.helper.*;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Singleton
public class EmcosBalanceServiceImpl implements EmcosBalanceService {
    private static final Logger logger = LoggerFactory.getLogger(EmcosBalanceServiceImpl.class);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    private List<LastLoadInfo> lastLoadInfoList;
    private List<EmcosPointCfg> pointsCfg;

    public List<DayMeteringBalanceRaw> request(String paramCode, LocalDateTime requestedTime) {
        logger.info("Request balances started...");
        logger.info("Param: " + paramCode);
        logger.info("Time: " + requestedTime);

        logger.info("Get list of points...");
        this.pointsCfg = emcosCfgService.requestCfg();
        if (pointsCfg==null || pointsCfg.isEmpty()) {
            logger.warn("List of points is empty, request balance terminated");
            return emptyList();
        }

        logger.info("Get last load info...");
        lastLoadInfoList = lastLoadInfoService.findAll();

        List<DayMeteringBalanceRaw> list;
        try {
            logger.info("Send http request for metering data...");
            String body = buildBody(paramCodes.get(paramCode), requestedTime);
            if (StringUtils.isEmpty(body)) {
                logger.info("Request body is empty, data is up to date");
                return emptyList();
            }

            String answer = new HttpReqesterImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(body)
                .build()
                .doRequest();

            list = parseAnswer(answer);
            logger.info("Request balances completed");
        }

        catch (Exception e) {
            logger.error("Request balances failed: " + e.toString());
            list = emptyList();
        }

        return list;
    }

    private String buildBody(String emcosParamCode, LocalDateTime requestedTime) {
        logger.info("Build body for request balances...");

        String strPoints = pointsCfg.stream()
    		.filter(p -> p.getPointCode().equals("120620300070020001") || p.getPointCode().equals("121420300070010003") )
    		.filter(p -> p.getEmcosParamCode().equals(emcosParamCode))
            .map( p-> serializePointCfg(p, requestedTime))
            .filter(p -> StringUtils.isNotEmpty(p))
            .collect(Collectors.joining());

        if (StringUtils.isEmpty(strPoints)) {
            logger.info("Metering data is p to date");
            return "";
        }

        String data = registryTemplate.getTemplate("EMCOS_REQML_DATA")
        	.replace("#points#", strPoints);
        logger.debug("data: " + data);

        String property = registryTemplate.getTemplate("EMCOS_REQML_PROPERTY")
        	.replace("#user#", config.getUser())
        	.replace("#isPacked#", config.getIsPacked().toString())
        	.replace("#func#", "REQML")
        	.replace("#attType#", config.getAttType());
        logger.debug("property: " + property);

        String body = registryTemplate.getTemplate("EMCOS_REQML_BODY")
        	.replace("#property#", property)
        	.replace("#data#", Base64.encodeBase64String(data.getBytes()));
        logger.debug("body for request balances: " + body);

        return body;
    }
    
    private List<DayMeteringBalanceRaw> parseAnswer(String answer) throws Exception {
        logger.info("Parse answer for balances...");
        logger.debug("answer: " + new String(Base64.decodeBase64(answer), "Cp1251"));

        List<DayMeteringBalanceRaw> list = new ArrayList<>();
        
        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(new InputSource(new StringReader( new String(Base64.decodeBase64(answer), "Cp1251") )));
        
        NodeList nodes =  doc.getDocumentElement().getParentNode()
            .getFirstChild()
            .getChildNodes();

        for(int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeName() == "ROWDATA") {
                NodeList rowData = nodes.item(i).getChildNodes();
                for(int j = 0; j < rowData.getLength(); j++) {
                    if (rowData.item(j).getNodeName() == "ROW")
                    	list.add(parseNode(rowData.item(j)));
                }
            }
        }

        logger.info("Parse answer for balances completed, count of rows: " + list.size());
        return list;
    }
    

    private String serializePointCfg(EmcosPointCfg pointCfg, LocalDateTime requestedTime) {
        LastLoadInfo lastLoadInfo = lastLoadInfoList.stream()
            .filter(t -> t.getExternalCode().equals(pointCfg.getPointCode()) && t.getParamCode().equals(pointCfg.getParamCode()) )
            .findFirst()
            .orElse(null);

        LocalDateTime startTime = buildStartTime(lastLoadInfo);
        if (startTime.isEqual(requestedTime) || startTime.isAfter(requestedTime))
            return "";

        return ""
                + "<ROW PPOINT_CODE=\"" + pointCfg.getPointCode() + "\" "
                + "PML_ID=\"" + pointCfg.getEmcosParamCode() + "\" "
                + "PBT=\"" + startTime.format(timeFormatter) + "\" "
                + "PET=\"" + requestedTime.format(timeFormatter) + "\" />";
    }    


    private LocalDateTime buildStartTime(LastLoadInfo lastLoadInfo) {
        LocalDateTime startDateTime;
        if (lastLoadInfo!=null && lastLoadInfo.getLastLoadDate()!=null) {
        	LocalDateTime lastLoadTime = lastLoadInfo.getLastLoadDate().plusDays(1);
            startDateTime = LocalDateTime.of(
            	lastLoadTime.getYear(), 
            	lastLoadTime.getMonth(), 
            	lastLoadTime.getDayOfMonth(), 
            	0, 
            	0
            );
        }
        else {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC+1"));
            startDateTime =  LocalDateTime.of(
                    now.getYear(),
                    now.getMonth(),
                    now.getDayOfMonth(),
                    0,
                    0
            );
        }
        return startDateTime;
    }
    
    private DayMeteringBalanceRaw parseNode(Node node) {
        String externalCode = node.getAttributes()
            .getNamedItem("PPOINT_CODE")
            .getNodeValue() ;

        String emcosParamCode = node.getAttributes()
            .getNamedItem("PML_ID")
            .getNodeValue() ;
        
        LocalDate date = null;
        String dateStr = node.getAttributes()
            .getNamedItem("PBT")
            .getNodeValue() ;

        if (dateStr!=null) 
            date = LocalDate.parse(dateStr, dateFormatter);

        Double val = null;
        String valStr = node.getAttributes()
            .getNamedItem("PVAL")
            .getNodeValue();

        if (valStr!=null)
            val = Double.parseDouble(valStr);

        DayMeteringBalanceRaw balance = new DayMeteringBalanceRaw();
        balance.setExternalCode(externalCode);
        balance.setMeteringDate(date.atStartOfDay());
        balance.setWayEntering(WayEntering.EMCOS);
        balance.setDataSourceCode("EMCOS");
        balance.setStatus(DataStatus.RAW);
        balance.setUnitCode("-");
        balance.setVal(val);

        EmcosPointCfg pointCfg = pointsCfg.stream()
        	.filter(t -> t.getPointCode().equals(balance.getExternalCode()) && t.getEmcosParamCode().equals(emcosParamCode))
        	.findFirst()
        	.orElse(null);

        if (pointCfg!=null) {
            balance.setParamCode(pointCfg.getParamCode());
            balance.setUnitCode(pointCfg.getUnitCode());
        }
        
        return balance;
    }


    @Inject
    private LastLoadInfoService lastLoadInfoService;

    @Inject
    private RegistryTemplate registryTemplate;

    @Inject
    private EmcosConfig config;

    @Inject EmcosCfgService emcosCfgService;

    @Inject @ParamCodes
    private BiMap<String, String> paramCodes;
}
