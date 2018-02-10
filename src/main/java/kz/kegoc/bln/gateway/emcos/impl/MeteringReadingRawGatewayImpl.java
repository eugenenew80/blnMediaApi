package kz.kegoc.bln.gateway.emcos.impl;

import kz.kegoc.bln.ejb.cdi.annotation.EmcosParamUnits;
import kz.kegoc.bln.ejb.cdi.annotation.ParamCodes;
import kz.kegoc.bln.entity.common.DataStatus;
import kz.kegoc.bln.entity.common.InputMethod;
import kz.kegoc.bln.entity.common.ReceivingMethod;
import kz.kegoc.bln.entity.common.SourceSystem;
import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import kz.kegoc.bln.entity.data.LastLoadInfo;
import kz.kegoc.bln.gateway.emcos.MeteringReadingRawGateway;
import kz.kegoc.bln.gateway.emcos.ServerConfig;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.registry.emcos.TemplateRegistry;
import kz.kegoc.bln.service.data.LastLoadInfoService;
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
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.*;
import com.google.common.collect.BiMap;
import static java.util.Collections.emptyList;

@Singleton
public class MeteringReadingRawGatewayImpl implements MeteringReadingRawGateway {
    private static final Logger logger = LoggerFactory.getLogger(MeteringReadingRawGatewayImpl.class);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    private List<LastLoadInfo> lastLoadInfoList;
    private List<MeteringPointCfg> pointsCfg;
    private String paramCode;
    private String emcosParamCode;
    private LocalDateTime requestedTime;

    public MeteringReadingRawGateway cfg(List<MeteringPointCfg> pointsCfg) {
        this.pointsCfg = pointsCfg;
        return this;
    }

    public MeteringReadingRawGateway requestedTime(LocalDateTime requestedTime) {
        this.requestedTime = requestedTime;
        return this;
    }

    public MeteringReadingRawGateway paramCode(String paramCode) {
        this.paramCode = paramCode;
        this.emcosParamCode = paramCodes.get(paramCode);
        return this;
    }

    public List<MeteringReadingRaw> request() {
        logger.info("MeteringReadingRawGatewayImpl.request started");
        logger.info("Param: " + paramCode);
        logger.info("Time: " + requestedTime);

        if (pointsCfg==null || pointsCfg.isEmpty()) {
            logger.warn("List of points is empty, MeteringReadingRawGatewayImpl.request interrupted");
            return emptyList();
        }

        lastLoadInfoList = lastLoadInfoService.findAll();

        List<MeteringReadingRaw> list;
        try {
            String body = buildBody();
            if (StringUtils.isEmpty(body)) {
                logger.info("Request body is empty, MeteringReadingRawGatewayImpl.request interrupted");
                return emptyList();
            }

            String answer = new HttpGatewayImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(body)
                .build()
                .doRequest();

            list = parseAnswer(answer);
            logger.info("MeteringReadingRawGatewayImpl.request succesfully completed");
        }

        catch (Exception e) {
            list = emptyList();
            logger.error("MeteringReadingRawGatewayImpl.request failed: " + e.toString());
        }

        return list;
    }

    private String buildBody() {
        logger.debug("MeteringReadingRawGatewayImpl.buildBody started");

        String strPoints = pointsCfg.stream()
    		.filter(p -> p.getEmcosParamCode().equals(emcosParamCode))
            .map( p-> serializePointCfg(p))
            .filter(p -> StringUtils.isNotEmpty(p))
            .collect(Collectors.joining());
        logger.trace("points: " + strPoints);

        if (StringUtils.isEmpty(strPoints)) {
            logger.debug("List of points is empty, MeteringReadingRawGatewayImpl.buildBody interrupted");
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
        logger.trace("body for request balances: " + body);

        logger.debug("MeteringReadingRawGatewayImpl.buildBody completed");
        return body;
    }
    
    private List<MeteringReadingRaw> parseAnswer(String answer) throws Exception {
        logger.info("MeteringReadingRawGatewayImpl.parseAnswer started");
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

        List<MeteringReadingRaw> list = new ArrayList<>();
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
        
        logger.info("MeteringReadingRawGatewayImpl.parseAnswer completed, count of rows: " + list.size());
        return list;
    }
    

    private String serializePointCfg(MeteringPointCfg pointCfg) {
        LastLoadInfo lastLoadInfo = lastLoadInfoList.stream()
            .filter(t -> t.getSourceMeteringPointCode().equals(pointCfg.getPointCode()) && t.getSourceParamCode().equals(pointCfg.getEmcosParamCode()) )
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
        if (lastLoadInfo!=null && lastLoadInfo.getLastLoadDate()!=null)
            return lastLoadInfo.getLastLoadDate().plusDays(1).truncatedTo(ChronoUnit.DAYS);
        else
            return LocalDate.now(ZoneId.of("UTC+1")).atStartOfDay();
    }
    
    private MeteringReadingRaw parseNode(Node node) {
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

        MeteringReadingRaw balance = new MeteringReadingRaw();
        balance.setSourceMeteringPointCode(externalCode);
        balance.setMeteringDate(date.atStartOfDay());
        balance.setSourceSystemCode(SourceSystem.EMCOS);
        balance.setStatus(DataStatus.TMP);
        balance.setInputMethod(InputMethod.AUTO);
        balance.setReceivingMethod(ReceivingMethod.AUTO);
        balance.setSourceUnitCode(emcosParamUnits.get(emcosParamCode));
        balance.setSourceParamCode(emcosParamCode);
        balance.setVal(val);

        return balance;
    }


    @Inject
    private LastLoadInfoService lastLoadInfoService;

    @Inject
    private TemplateRegistry templateRegistry;

    @Inject
    private ServerConfig config;

    @Inject @ParamCodes
    private BiMap<String, String> paramCodes;

    @Inject @EmcosParamUnits
    private Map<String, String> emcosParamUnits;
}
