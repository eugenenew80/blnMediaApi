package kz.kegoc.bln.producer.emcos.helper.impl;

import kz.kegoc.bln.ejb.annotation.EmcosParamUnits;
import kz.kegoc.bln.ejb.annotation.ParamCodes;
import kz.kegoc.bln.entity.media.*;
import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.producer.emcos.helper.*;
import kz.kegoc.bln.service.media.LastLoadInfoService;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.ejb.Singleton;
import javax.inject.Inject;

import java.io.StringReader;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.*;
import com.google.common.collect.BiMap;

import static java.util.Collections.emptyList;

@Singleton
public class EmcosBalanceServiceImpl implements EmcosBalanceService {
    private static final Logger logger = LoggerFactory.getLogger(EmcosBalanceServiceImpl.class);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    private List<LastLoadInfo> lastLoadInfoList;
    private List<EmcosPointCfg> pointsCfg;

    public void setPointsCfg(List<EmcosPointCfg> pointsCfg) {
        this.pointsCfg = pointsCfg;
    }

    public List<DayMeteringBalanceRaw> request(String paramCode, LocalDateTime requestedTime) {
        logger.info("EmcosBalanceServiceImpl.request started");
        logger.info("Param: " + paramCode);
        logger.info("Time: " + requestedTime);

        if (pointsCfg==null || pointsCfg.isEmpty()) {
            logger.warn("List of points is empty, EmcosBalanceServiceImpl.request interrupted");
            return emptyList();
        }

        lastLoadInfoList = lastLoadInfoService.findAll();

        List<DayMeteringBalanceRaw> list;
        try {
            String body = buildBody(paramCodes.get(paramCode), requestedTime);
            if (StringUtils.isEmpty(body)) {
                logger.info("Request body is empty, EmcosBalanceServiceImpl.request interrupted");
                return emptyList();
            }

            String answer = new HttpReqesterImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(body)
                .build()
                .doRequest();

            list = parseAnswer(answer);
            logger.info("EmcosBalanceServiceImpl.request succesfully completed");
        }

        catch (Exception e) {
            list = emptyList();
            logger.error("EmcosBalanceServiceImpl.request failed: " + e.toString());
        }

        return list;
    }

    private String buildBody(String emcosParamCode, LocalDateTime requestedTime) {
        logger.debug("EmcosBalanceServiceImpl.buildBody started");

        String strPoints = pointsCfg.stream()
    		.filter(p -> p.getEmcosParamCode().equals(emcosParamCode))
            .map( p-> serializePointCfg(p, requestedTime))
            .filter(p -> StringUtils.isNotEmpty(p))
            .collect(Collectors.joining());

        if (StringUtils.isEmpty(strPoints)) {
        	logger.debug("List of points is empty, EmcosBalanceServiceImpl.buildBody interrupted");
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
        logger.trace("body for request balances: " + body);

        logger.debug("EmcosBalanceServiceImpl.buildBody completed");
        return body;
    }
    
    private List<DayMeteringBalanceRaw> parseAnswer(String answer) throws Exception {
        logger.info("EmcosBalanceServiceImpl.parseAnswer started");
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

        List<DayMeteringBalanceRaw> list = new ArrayList<>();
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
        
        logger.info("EmcosBalanceServiceImpl.parseAnswer completed, count of rows: " + list.size());
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
        balance.setUnitCode(emcosParamUnits.get(emcosParamCode));
        balance.setParamCode(paramCodes.inverse().get(emcosParamCode));
        balance.setVal(val);

        return balance;
    }


    @Inject
    private LastLoadInfoService lastLoadInfoService;

    @Inject
    private RegistryTemplate registryTemplate;

    @Inject
    private EmcosConfig config;

    @Inject @ParamCodes
    private BiMap<String, String> paramCodes;

    @Inject @EmcosParamUnits
    private Map<String, String> emcosParamUnits;
}
