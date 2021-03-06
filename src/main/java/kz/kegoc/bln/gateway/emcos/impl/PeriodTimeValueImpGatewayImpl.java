package kz.kegoc.bln.gateway.emcos.impl;

import kz.kegoc.bln.common.enums.ProcessingStatusEnum;
import kz.kegoc.bln.common.enums.InputMethodEnum;
import kz.kegoc.bln.common.enums.ReceivingMethodEnum;
import kz.kegoc.bln.common.enums.SourceSystemEnum;
import kz.kegoc.bln.entity.media.ConnectionConfig;
import kz.kegoc.bln.entity.media.InputMethod;
import kz.kegoc.bln.gateway.CompressService;
import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import kz.kegoc.bln.entity.media.ReceivingMethod;
import kz.kegoc.bln.gateway.emcos.PeriodTimeValueImpGateway;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.registry.TemplateRegistry;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.*;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.ejb.*;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.Collections.*;
import static java.util.stream.Collectors.groupingBy;

@Singleton
public class PeriodTimeValueImpGatewayImpl implements PeriodTimeValueImpGateway {
    private static final Logger logger = LoggerFactory.getLogger(PeriodTimeValueImpGatewayImpl.class);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
    private List<MeteringPointCfg> points;
    private ConnectionConfig config;

    public PeriodTimeValueImpGateway points(List<MeteringPointCfg> points) {
        this.points = points;
        return this;
    }

    public PeriodTimeValueImpGateway config(ConnectionConfig config) {
        this.config = config;
        return this;
    }

    public List<PeriodTimeValueRaw> request() throws Exception {
        logger.info("request started");

        if (config ==null) {
            logger.warn("Config is empty, request stopped");
            return emptyList();
        }

        if (points ==null || points.isEmpty()) {
            logger.warn("List of points is empty, request stopped");
            return emptyList();
        }

        List<PeriodTimeValueRaw> list;
        try {
            logger.info("Send http request for metering media...");
            byte[] body = buildBody();
            if (body==null || body.length==0) {
            	logger.info("Request body is empty, request stopped");
                return emptyList();
            }

            byte[] byteAnswer = new HttpGatewayImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(body)
                .build()
                .doRequest();

            String answer = new String(byteAnswer, "UTF-8");
            int n1 = answer.indexOf("<AnswerData>");
            int n2 = answer.indexOf("</AnswerData>");
            if (n2>n1)
                answer = answer.substring(n1+12, n2);

            list = parseAnswer(answer);
            logger.info("request competed");
        }

        catch (Exception e) {
            logger.error("request failed: " + e.toString());
            throw e;
        }

        return list;
    }

    private byte[] buildBody() {
    	logger.debug("buildBody started");

    	String strPoints = points.stream()
            .filter(p -> !p.getStartTime().isAfter(p.getEndTime()))
            .map( p-> buildPoint(p))
            .filter(p -> StringUtils.isNotEmpty(p))
            .collect(Collectors.joining());
        logger.trace("points: " + strPoints);

        if (StringUtils.isEmpty(strPoints)) {
        	logger.debug("List of points is empty, buildBody stopped");
            return new byte[0];
        }

        String aData = templateRegistry.getTemplate("EMCOS_REQML_DATA")
        	.replace("#points#", strPoints);
        logger.trace("media: " + aData);

        String property = templateRegistry.getTemplate("EMCOS_REQML_PROPERTY")
        	.replace("#user#", config.getUserName())
        	.replace("#isPacked#", "false")
        	.replace("#func#", "REQML")
        	.replace("#attType#", "1");
        logger.trace("property: " + property);

        String body1 = templateRegistry.getTemplate("EMCOS_REQML_BODY_1")
        	.replace("#property#", property);

        String body2 = templateRegistry.getTemplate("EMCOS_REQML_BODY_2")
        	.replace("#property#", property);

        logger.trace("body part 1 for request metering data: " + body1);
        logger.trace("body part 2 for request metering data: " + body2);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(body1.getBytes());
            baos.write(Base64.encodeBase64(aData.getBytes()));
            baos.write(body2.getBytes());
            baos.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try { baos.close(); }
            catch (IOException e) { }
        }

        logger.debug("buildBody completed");
        return baos.toByteArray();
    }

    private String buildPoint(MeteringPointCfg point) {
        return ""
                + "<ROW PPOINT_CODE=\"" + point.getSourceMeteringPointCode() + "\" "
                + "PML_ID=\"" + point.getSourceParamCode() + "\" "
                + "PBT=\"" + point.getStartTime().format(timeFormatter) + "\" "
                + "PET=\"" + point.getEndTime().format(timeFormatter) + "\" />";
    }

    private List<PeriodTimeValueRaw> parseAnswer(String answer) throws Exception {
    	logger.info("parseAnswer started");
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

        List<PeriodTimeValueRaw> list = new ArrayList<>();
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
        Map<Pair<String, String>, List<MeteringPointCfg>> map1 = points.stream()
                .collect(groupingBy(p -> Pair.of(p.getSourceParamCode(), p.getSourceUnitCode())));

        Map<Pair<String, Integer>, List<MeteringPointCfg>> map2 = points.stream()
                .collect(groupingBy(p -> Pair.of(p.getSourceParamCode(), p.getInterval())));

        list.forEach(mr -> {
            Pair<String, String> pair1 = map1.keySet().stream()
                .filter(p -> p.getLeft().equals(mr.getSourceParamCode()))
                .findFirst()
                .orElse(null);

            Pair<String, Integer> pair2 = map2.keySet().stream()
                    .filter(p -> p.getLeft().equals(mr.getSourceParamCode()))
                    .findFirst()
                    .orElse(null);

            if (pair1!=null)
                mr.setSourceUnitCode(pair1.getRight());

            if (pair2!=null)
                mr.setInterval(pair2.getRight());
        });
        logger.debug("find unit codes for list completed");

        logger.info("parseAnswer completed, count of rows: " + list.size());
        return list;
    }

    private PeriodTimeValueRaw parseNode(Node node) {
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

        PeriodTimeValueRaw pc = new PeriodTimeValueRaw();
        pc.setSourceMeteringPointCode(externalCode);
        pc.setMeteringDate(time);
        pc.setSourceSystemCode(SourceSystemEnum.EMCOS);
        pc.setStatus(ProcessingStatusEnum.TMP);
        pc.setInputMethod(InputMethod.newInstance(InputMethodEnum.AUTO));
        pc.setReceivingMethod(ReceivingMethod.newInstance(ReceivingMethodEnum.SERVICE));
        pc.setSourceParamCode(sourceParamCode);
        pc.setVal(val);
        
        return pc;
    }

    @Inject
    private TemplateRegistry templateRegistry;

    @Inject
    private CompressService compressService;
}
