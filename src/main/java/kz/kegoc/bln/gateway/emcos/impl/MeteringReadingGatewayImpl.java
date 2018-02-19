package kz.kegoc.bln.gateway.emcos.impl;

import kz.kegoc.bln.entity.common.DataStatus;
import kz.kegoc.bln.entity.common.InputMethod;
import kz.kegoc.bln.entity.common.ReceivingMethod;
import kz.kegoc.bln.entity.common.SourceSystem;
import kz.kegoc.bln.entity.data.EmcosConfig;
import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import kz.kegoc.bln.gateway.emcos.MeteringReadingGateway;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.registry.emcos.TemplateRegistry;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
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
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;

@Singleton
public class MeteringReadingGatewayImpl implements MeteringReadingGateway {
    private static final Logger logger = LoggerFactory.getLogger(MeteringReadingGatewayImpl.class);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private List<MeteringPointCfg> points;
    private EmcosConfig config;

    public MeteringReadingGateway points(List<MeteringPointCfg> points) {
        this.points = points;
        return this;
    }

    public MeteringReadingGateway config(EmcosConfig config) {
        this.config = config;
        return this;
    }

    public List<MeteringReadingRaw> request() {
        logger.info("MeteringReadingGatewayImpl.request started");

        if (config ==null) {
            logger.warn("Config is empty, MeteringReadingGatewayImpl.request stopped");
            return emptyList();
        }

        if (points ==null || points.isEmpty()) {
            logger.warn("List of points is empty, MeteringReadingGatewayImpl.request stopped");
            return emptyList();
        }

        List<MeteringReadingRaw> list;
        try {
            String body = buildBody();
            if (StringUtils.isEmpty(body)) {
                logger.info("Request body is empty, MeteringReadingGatewayImpl.request stopped");
                return emptyList();
            }

            String answer = new HttpGatewayImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(body)
                .build()
                .doRequest();

            list = parseAnswer(answer);
            logger.info("MeteringReadingGatewayImpl.request successfully completed");
        }

        catch (Exception e) {
            list = emptyList();
            logger.error("MeteringReadingGatewayImpl.request failed: " + e.toString());
        }

        return list;
    }

    private String buildBody() {
        logger.debug("MeteringReadingGatewayImpl.buildBody started");

        String strPoints = points.stream()
            .map( p-> buildNode(p))
            .filter(p -> StringUtils.isNotEmpty(p))
            .collect(Collectors.joining());
        logger.trace("points: " + strPoints);

        if (StringUtils.isEmpty(strPoints)) {
            logger.debug("List of points is empty, MeteringReadingGatewayImpl.buildBody stopped");
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
        logger.trace("body for request balances: " + body);

        logger.debug("MeteringReadingGatewayImpl.buildBody completed");
        return body;
    }

    private String buildNode(MeteringPointCfg point) {
        return ""
                + "<ROW PPOINT_CODE=\"" + point.getSourceMeteringPointCode() + "\" "
                + "PML_ID=\"" + point.getSourceParamCode() + "\" "
                + "PBT=\"" + point.getStartTime().format(timeFormatter) + "\" "
                + "PET=\"" + point.getEndTime().format(timeFormatter) + "\" />";
    }

    private List<MeteringReadingRaw> parseAnswer(String answer) throws Exception {
        logger.info("MeteringReadingGatewayImpl.parseAnswer started");
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
                        MeteringReadingRaw node = parseNode(rowData.item(j));
                        if (node!=null)
                            list.add(node);
                    }
                }
            }
        }
        logger.debug("convert xml to list completed");

        logger.debug("find unit codes for list started");
        Map<Pair<String, String>, List<MeteringPointCfg>> map = points.stream()
            .collect(groupingBy(p -> Pair.of(p.getSourceParamCode(), p.getSourceUnitCode())));

        list.forEach(mr -> {
            Pair<String, String> pair = map.keySet().stream()
                .filter(p -> p.getLeft().equals(mr.getSourceParamCode()))
                .findFirst()
                .orElse(null);

            if (pair!=null)
                mr.setSourceUnitCode(pair.getRight());
        });
        logger.debug("find unit codes for list completed");

        logger.info("MeteringReadingGatewayImpl.parseAnswer completed, count of rows: " + list.size());
        return list;
    }

    private MeteringReadingRaw parseNode(Node node) {
        NamedNodeMap attributes = node.getAttributes();

        String externalCode = attributes
            .getNamedItem("PPOINT_CODE")
            .getNodeValue() ;

        String sourceParamCode = attributes
            .getNamedItem("PML_ID")
            .getNodeValue() ;
        
        LocalDate date = null;
        String dateStr = attributes
            .getNamedItem("PBT")
            .getNodeValue();
        dateStr = dateStr.substring(0, 8);

        try {
            if (dateStr != null)
                date = LocalDate.parse(dateStr, dateFormatter);
        }
        catch (Exception e) {
            logger.error("parse date error :  " + e.getMessage());
            logger.error("dateStr = " + dateStr);
            logger.error("sourceParamCode = " + sourceParamCode);
            logger.error("externalCode = " + externalCode);
        }
        if (date==null)
            return null;

        Double val = null;
        String valStr = attributes
            .getNamedItem("PVAL")
            .getNodeValue();

        if (valStr!=null)
            val = Double.parseDouble(valStr);

        MeteringReadingRaw mr = new MeteringReadingRaw();
        mr.setSourceMeteringPointCode(externalCode);
        mr.setMeteringDate(date.atStartOfDay());
        mr.setSourceSystemCode(SourceSystem.EMCOS);
        mr.setStatus(DataStatus.TMP);
        mr.setInputMethod(InputMethod.AUTO);
        mr.setReceivingMethod(ReceivingMethod.AUTO);
        mr.setSourceParamCode(sourceParamCode);
        mr.setVal(val);

        return mr;
    }

    @Inject
    private TemplateRegistry templateRegistry;
}
