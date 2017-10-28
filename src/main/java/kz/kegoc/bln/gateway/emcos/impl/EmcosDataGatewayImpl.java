package kz.kegoc.bln.gateway.emcos.impl;

import com.google.common.collect.BiMap;

import kz.kegoc.bln.ejb.cdi.annotation.EmcosParamUnits;
import kz.kegoc.bln.ejb.cdi.annotation.ParamCodes;
import kz.kegoc.bln.entity.media.*;
import kz.kegoc.bln.entity.media.raw.LastLoadInfo;
import kz.kegoc.bln.gateway.emcos.EmcosConfig;
import kz.kegoc.bln.gateway.emcos.EmcosDataGateway;
import kz.kegoc.bln.gateway.emcos.EmcosPointCfg;
import kz.kegoc.bln.gateway.emcos.MinuteMeteringData;
import kz.kegoc.bln.registry.emcos.TemplateRegistry;
import kz.kegoc.bln.service.media.raw.LastLoadInfoService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.ejb.*;
import javax.inject.Inject;
import java.io.StringReader;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.*;

@Singleton
public class EmcosDataGatewayImpl implements EmcosDataGateway {
    private static final Logger logger = LoggerFactory.getLogger(EmcosDataGatewayImpl.class);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");

    private List<LastLoadInfo> lastLoadInfoList;
    private List<EmcosPointCfg> pointsCfg;
    private String paramCode;
    private String emcosParamCode;
    private LocalDateTime requestedTime;

    public EmcosDataGateway cfg(List<EmcosPointCfg> pointsCfg) {
        this.pointsCfg = pointsCfg;
        return this;
    }

    public EmcosDataGateway requestedTime(LocalDateTime requestedTime) {
        this.requestedTime = requestedTime;
        return this;
    }

    public EmcosDataGateway paramCode(String paramCode) {
        this.paramCode = paramCode;
        this.emcosParamCode = paramCodes.get(paramCode);
        return this;
    }


    public List<MinuteMeteringData> request() {
        logger.info("EmcosDataGatewayImpl.request started");
        logger.info("Param: " + paramCode);
        logger.info("Time: " + requestedTime);

        if (pointsCfg==null || pointsCfg.isEmpty()) {
            logger.warn("List of points is empty, EmcosDataGatewayImpl.request interrupted");
            return emptyList();
        }
        
        this.lastLoadInfoList = lastLoadInfoService.findAll();

        List<MinuteMeteringData> list;
        try {
            logger.info("Send http request for metering data...");
            String body = buildBody();
            if (StringUtils.isEmpty(body)) {
            	logger.info("Request body is empty, EmcosDataGatewayImpl.request interrupted");
                return emptyList();
            }

            String answer = new HttpGatewayImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(body)
                .build()
                .doRequest();

            list = parseAnswer(answer);
            logger.info("EmcosDataGatewayImpl.request competed");
        }

        catch (Exception e) {
            list = emptyList();
            logger.error("EmcosDataGatewayImpl.request failed: " + e.toString());
        }

        return list;
    }

    private String buildBody() {
    	logger.debug("EmcosDataGatewayImpl.buildBody started");

        String strPoints = pointsCfg.stream()
    		.filter(p -> p.getEmcosParamCode().equals(emcosParamCode))
            .map( p-> serializePointCfg(p))
            .filter(p -> StringUtils.isNotEmpty(p))
            .collect(Collectors.joining());
        logger.trace("points: " + strPoints);

        if (StringUtils.isEmpty(strPoints)) {
        	logger.debug("List of points is empty, EmcosDataGatewayImpl.buildBody interrupted");
            return "";
        }

        String data = templateRegistry.getTemplate("EMCOS_REQML_DATA")
        	.replace("#points#", strPoints);
        logger.trace("data: " + data);

        String property = templateRegistry.getTemplate("EMCOS_REQML_PROPERTY")
        	.replace("#user#", config.getUser())
        	.replace("#isPacked#", config.getIsPacked().toString())
        	.replace("#func#", "REQML")
        	.replace("#attType#", config.getAttType());
        logger.trace("property: " + property);

        String body = templateRegistry.getTemplate("EMCOS_REQML_BODY")
        	.replace("#property#", property)
        	.replace("#data#", Base64.encodeBase64String(data.getBytes()));
        logger.trace("body for request metering data: " + body);

        logger.debug("EmcosDataGatewayImpl.buildBody completed");
        return body;
    }

    private List<MinuteMeteringData> parseAnswer(String answer) throws Exception {
    	logger.info("EmcosDataGatewayImpl.parseAnswer started");
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

        List<MinuteMeteringData> list = new ArrayList<>();
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
        
        logger.info("EmcosDataGatewayImpl.parseAnswer completed, count of rows: " + list.size());
        return list;
    }

    private String serializePointCfg(EmcosPointCfg emcosCfg) {
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
        LocalDateTime startTime = LocalDate.now(ZoneId.of("UTC+1")).atStartOfDay();
        if (lastLoadInfo!=null && lastLoadInfo.getLastLoadDate() !=null) {
            LocalDateTime lastLoadDate = lastLoadInfo.getLastLoadDate();
            startTime = lastLoadDate.getMinute() < 45
                    ? lastLoadDate.truncatedTo(ChronoUnit.HOURS)
                    : lastLoadDate.plusMinutes(15);
        }

        return startTime;
    }

    private MinuteMeteringData parseNode(Node node) {
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

        MinuteMeteringData data = new MinuteMeteringData();
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
    private TemplateRegistry templateRegistry;

    @Inject
    private EmcosConfig config;

    @Inject @ParamCodes
    private BiMap<String, String> paramCodes;

    @Inject @EmcosParamUnits
    private Map<String, String> emcosParamUnits;
}
