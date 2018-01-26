package kz.kegoc.bln.gateway.emcos.impl;

import kz.kegoc.bln.ejb.cdi.annotation.EmcosParamUnits;
import kz.kegoc.bln.ejb.cdi.annotation.ParamCodes;
import kz.kegoc.bln.entity.common.DataSource;
import kz.kegoc.bln.entity.common.DataStatus;
import kz.kegoc.bln.entity.data.DayMeteringBalance;
import kz.kegoc.bln.entity.data.LastLoadInfo;
import kz.kegoc.bln.gateway.emcos.EmcosBalanceGateway;
import kz.kegoc.bln.gateway.emcos.EmcosConfig;
import kz.kegoc.bln.gateway.emcos.EmcosPointCfg;
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
public class EmcosBalanceGatewayImpl implements EmcosBalanceGateway {
    private static final Logger logger = LoggerFactory.getLogger(EmcosBalanceGatewayImpl.class);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    private List<LastLoadInfo> lastLoadInfoList;
    private List<EmcosPointCfg> pointsCfg;
    private String paramCode;
    private String emcosParamCode;
    private LocalDateTime requestedTime;

    public EmcosBalanceGateway cfg(List<EmcosPointCfg> pointsCfg) {
        this.pointsCfg = pointsCfg;
        return this;
    }

    public EmcosBalanceGateway requestedTime(LocalDateTime requestedTime) {
        this.requestedTime = requestedTime;
        return this;
    }

    public EmcosBalanceGateway paramCode(String paramCode) {
        this.paramCode = paramCode;
        this.emcosParamCode = paramCodes.get(paramCode);
        return this;
    }

    public List<DayMeteringBalance> request() {
        logger.info("EmcosBalanceGatewayImpl.request started");
        logger.info("Param: " + paramCode);
        logger.info("Time: " + requestedTime);

        if (pointsCfg==null || pointsCfg.isEmpty()) {
            logger.warn("List of points is empty, EmcosBalanceGatewayImpl.request interrupted");
            return emptyList();
        }

        lastLoadInfoList = lastLoadInfoService.findAll();

        List<DayMeteringBalance> list;
        try {
            String body = buildBody();
            if (StringUtils.isEmpty(body)) {
                logger.info("Request body is empty, EmcosBalanceGatewayImpl.request interrupted");
                return emptyList();
            }

            String answer = new HttpGatewayImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(body)
                .build()
                .doRequest();

            list = parseAnswer(answer);
            logger.info("EmcosBalanceGatewayImpl.request succesfully completed");
        }

        catch (Exception e) {
            list = emptyList();
            logger.error("EmcosBalanceGatewayImpl.request failed: " + e.toString());
        }

        return list;
    }

    private String buildBody() {
        logger.debug("EmcosBalanceGatewayImpl.buildBody started");

        String strPoints = pointsCfg.stream()
    		.filter(p -> p.getEmcosParamCode().equals(emcosParamCode))
            .map( p-> serializePointCfg(p))
            .filter(p -> StringUtils.isNotEmpty(p))
            .collect(Collectors.joining());
        logger.trace("points: " + strPoints);

        if (StringUtils.isEmpty(strPoints)) {
            logger.debug("List of points is empty, EmcosBalanceGatewayImpl.buildBody interrupted");
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

        logger.debug("EmcosBalanceGatewayImpl.buildBody completed");
        return body;
    }
    
    private List<DayMeteringBalance> parseAnswer(String answer) throws Exception {
        logger.info("EmcosBalanceGatewayImpl.parseAnswer started");
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

        List<DayMeteringBalance> list = new ArrayList<>();
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
        
        logger.info("EmcosBalanceGatewayImpl.parseAnswer completed, count of rows: " + list.size());
        return list;
    }
    

    private String serializePointCfg(EmcosPointCfg pointCfg) {
        LastLoadInfo lastLoadInfo = lastLoadInfoList.stream()
            .filter(t -> t.getSourceMeteringPointCode().equals(pointCfg.getPointCode()) && t.getSourceParamCode().equals(pointCfg.getParamCode()) )
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
    
    private DayMeteringBalance parseNode(Node node) {
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

        DayMeteringBalance balance = new DayMeteringBalance();
        balance.setSourceMeteringPointCode(externalCode);
        balance.setMeteringDate(date.atStartOfDay());
        balance.setDataSourceCode(DataSource.EMCOS);
        balance.setStatus(DataStatus.RAW);
        balance.setSourceUnitCode(emcosParamUnits.get(emcosParamCode));
        balance.setSourceParamCode(emcosParamCode);
        balance.setParamCode(paramCodes.inverse().get(emcosParamCode));
        balance.setVal(val);

        return balance;
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
