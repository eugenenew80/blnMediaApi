package kz.kegoc.bln.gateway.emcos.impl;

import kz.kegoc.bln.entity.common.DataStatus;
import kz.kegoc.bln.entity.common.InputMethod;
import kz.kegoc.bln.entity.common.ReceivingMethod;
import kz.kegoc.bln.entity.common.SourceSystem;
import kz.kegoc.bln.entity.data.EmcosConfig;
import kz.kegoc.bln.entity.data.PowerConsumptionRaw;
import kz.kegoc.bln.gateway.emcos.PowerConsumptionGateway;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.registry.emcos.TemplateRegistry;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.*;
import java.util.stream.Collectors;
import static java.util.Collections.*;
import static java.util.stream.Collectors.groupingBy;

@Singleton
public class PowerConsumptionGatewayImpl implements PowerConsumptionGateway {
    private static final Logger logger = LoggerFactory.getLogger(PowerConsumptionGatewayImpl.class);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
    private List<MeteringPointCfg> points;
    private EmcosConfig config;

    public PowerConsumptionGateway points(List<MeteringPointCfg> points) {
        this.points = points;
        return this;
    }

    public PowerConsumptionGateway config(EmcosConfig config) {
        this.config = config;
        return this;
    }

    public List<PowerConsumptionRaw> request() {
        logger.info("PowerConsumptionGatewayImpl.request started");

        if (config ==null) {
            logger.warn("Config is empty, PowerConsumptionGatewayImpl.request stopped");
            return emptyList();
        }

        if (points ==null || points.isEmpty()) {
            logger.warn("List of points is empty, PowerConsumptionGatewayImpl.request stopped");
            return emptyList();
        }

        List<PowerConsumptionRaw> list;
        try {
            logger.info("Send http request for metering data...");
            String body = buildBody();
            if (StringUtils.isEmpty(body)) {
            	logger.info("Request body is empty, PowerConsumptionGatewayImpl.request stopped");
                return emptyList();
            }

            String answer = new HttpGatewayImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(body)
                .build()
                .doRequest();

            list = parseAnswer(answer);
            logger.info("PowerConsumptionGatewayImpl.request competed");
        }

        catch (Exception e) {
            list = emptyList();
            logger.error("PowerConsumptionGatewayImpl.request failed: " + e.toString());
        }

        return list;
    }

    private String buildBody() {
    	logger.debug("PowerConsumptionGatewayImpl.buildBody started");

    	String strPoints = points.stream()
            .map( p-> buildPoint(p))
            .filter(p -> StringUtils.isNotEmpty(p))
            .collect(Collectors.joining());
        logger.trace("points: " + strPoints);

        if (StringUtils.isEmpty(strPoints)) {
        	logger.debug("List of points is empty, PowerConsumptionGatewayImpl.buildBody stopped");
            return "";
        }

        String data = templateRegistry.getTemplate("EMCOS_REQML_DATA")
        	.replace("#points#", strPoints);
        logger.trace("data: " + data);

        String property = templateRegistry.getTemplate("EMCOS_REQML_PROPERTY")
        	.replace("#user#", config.getUserName())
        	.replace("#isPacked#", "false")
        	.replace("#func#", "REQML")
        	.replace("#attType#", "1");
        logger.trace("property: " + property);

        String body = templateRegistry.getTemplate("EMCOS_REQML_BODY")
        	.replace("#property#", property)
        	.replace("#data#", Base64.encodeBase64String(data.getBytes()));
        logger.trace("body for request metering data: " + body);

        logger.debug("PowerConsumptionGatewayImpl.buildBody completed");
        return body;
    }

    private String buildPoint(MeteringPointCfg point) {
        return ""
                + "<ROW PPOINT_CODE=\"" + point.getPointCode() + "\" "
                + "PML_ID=\"" + point.getEmcosParamCode() + "\" "
                + "PBT=\"" + point.getStartTime().format(timeFormatter) + "\" "
                + "PET=\"" + point.getEndTime().format(timeFormatter) + "\" />";
    }

    private List<PowerConsumptionRaw> parseAnswer(String answer) throws Exception {
    	logger.info("PowerConsumptionGatewayImpl.parseAnswer started");
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

        List<PowerConsumptionRaw> list = new ArrayList<>();
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

        logger.debug("find unit codes for list started");
        Map<Pair<String, String>, List<MeteringPointCfg>> map = points.stream()
                .collect(groupingBy(p -> Pair.of(p.getEmcosParamCode(), p.getUnitCode())));

        list.forEach(mr -> {
            Pair<String, String> pair = map.keySet().stream()
                .filter(p -> p.getLeft().equals(mr.getSourceParamCode()))
                .findFirst()
                .orElse(null);

            if (pair!=null)
                mr.setSourceUnitCode(pair.getRight());
        });
        logger.debug("find unit codes for list completed");

        logger.info("PowerConsumptionGatewayImpl.parseAnswer completed, count of rows: " + list.size());
        return list;
    }

    private PowerConsumptionRaw parseNode(Node node) {
        NamedNodeMap attributes = node.getAttributes();

        String externalCode = attributes
            .getNamedItem("PPOINT_CODE")
            .getNodeValue() ;

        String sourceParamCode = attributes
            .getNamedItem("PML_ID")
            .getNodeValue() ;
        
        LocalDateTime time = null;
        String timeStr = attributes
            .getNamedItem("PBT")
            .getNodeValue() ;

        if (timeStr!=null) {
            if (timeStr.indexOf("T")<0) timeStr = timeStr+"T00:00:00000";
            time = LocalDateTime.parse(timeStr, timeFormatter);
        }

        Double val = null;
        String valStr = attributes
            .getNamedItem("PVAL")
            .getNodeValue();

        if (valStr!=null)
            val = Double.parseDouble(valStr);

        PowerConsumptionRaw pc = new PowerConsumptionRaw();
        pc.setSourceMeteringPointCode(externalCode);
        pc.setMeteringDate(time);
        pc.setSourceSystemCode(SourceSystem.EMCOS);
        pc.setStatus(DataStatus.TMP);
        pc.setInterval(900);
        pc.setInputMethod(InputMethod.AUTO);
        pc.setReceivingMethod(ReceivingMethod.AUTO);
        pc.setSourceParamCode(sourceParamCode);
        pc.setVal(val);
        
        return pc;
    }

    @Inject
    private TemplateRegistry templateRegistry;
}
