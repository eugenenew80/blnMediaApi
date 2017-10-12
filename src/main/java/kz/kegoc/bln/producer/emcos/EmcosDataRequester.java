package kz.kegoc.bln.producer.emcos;

import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.raw.MinuteMeteringDataRaw;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmcosDataRequester {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
    private final EmcosConfig config;
    private final List<MeteringPoint> points;
    private final LocalDateTime reqestedDateTime;
    private final String paramCode;
    private final String emcosParamCode;
    private final RegistryTemplate registryTemplate;

    private EmcosDataRequester(Builder builder) {
        this.config = builder.config;
        this.paramCode = builder.paramCode;
        this.emcosParamCode = builder.emcosParamCode;
        this.points = builder.points;
        this.reqestedDateTime = builder.reqestedDateTime;
        this.registryTemplate = builder.registryTemplate;
    }


    public List<MinuteMeteringDataRaw> requestMeteringData() throws Exception {
        List<MinuteMeteringDataRaw> meteringData = new ArrayList<>();

        HttpRequester httpRequester = new HttpReqesterImpl.Builder()
    		.url(new URL(config.getUrl()))
    		.method("POST")
    		.body(buildBody())
    		.build();

        String answerData = httpRequester.doRequest();
        return extractData(answerData);
    }


    private String buildBody() {
        String strPoints = points.stream()
            .map( p-> toXmlNode(p))
            .collect(Collectors.joining());

        String data = registryTemplate.getTemplate("EMCOS_REQML_DATA")
        	.replace("#points#", strPoints);

        String property = registryTemplate.getTemplate("EMCOS_REQML_PROPERTY")
        	.replace("#user#", config.getUser())
        	.replace("#isPacked#", config.getIsPacked().toString())
        	.replace("#func#", config.getFunc())
        	.replace("#attType#", config.getAttType());
        
        String body = registryTemplate.getTemplate("EMCOS_REQML_BODY")
        	.replace("#property#", property)
        	.replace("#data#", Base64.encodeBase64String(data.getBytes()));

        return body;
    }
    

    private List<MinuteMeteringDataRaw> extractData(String answerData) throws Exception {
        List<MinuteMeteringDataRaw> meteringData = new ArrayList<>();

        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(new InputSource(new StringReader(new String(Base64.decodeBase64(answerData), "Cp1251"))));

        NodeList nodes =  doc.getDocumentElement().getParentNode()
            .getFirstChild()
            .getChildNodes();

        for(int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeName() == "ROWDATA") {
                NodeList rowData = nodes.item(i).getChildNodes();
                for(int j = 0; j < rowData.getLength(); j++) {
                    if (rowData.item(j).getNodeName() == "ROW")
                        meteringData.add(fromXmlNode(rowData.item(j)));
                }
            }
        }

        return meteringData;
    }


    private String toXmlNode(MeteringPoint point) {
        return ""
                + "<ROW PPOINT_CODE=\"" + point.getExternalCode() + "\" "
                + "PML_ID=\"" + emcosParamCode + "\" "
                + "PBT=\"" + buildStartDateTime(point).format(timeFormatter) + "\" "
                + "PET=\"" + reqestedDateTime.format(timeFormatter) + "\" />";
    }


    private LocalDateTime buildStartDateTime(MeteringPoint point) {
        LocalDateTime startDateTime;
        if (point.getLoadInfo()!=null && point.getLoadInfo().getLastLoadedDate()!=null) {
            LocalDateTime lastLoadTime = point.getLoadInfo().getLastLoadedDate();
            startDateTime = LocalDateTime.of(lastLoadTime.getYear(), lastLoadTime.getMonth(), lastLoadTime.getDayOfMonth(), lastLoadTime.getHour(), 0);
        }
        else {
            LocalDateTime now = LocalDateTime.now();
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

    
    private MinuteMeteringDataRaw fromXmlNode(Node node) {
        String externalCode = node.getAttributes()
            .getNamedItem("PPOINT_CODE")
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
            .getNodeValue() ;

        if (valStr!=null)
            val = Double.parseDouble(valStr);

        MinuteMeteringDataRaw meteringData = new MinuteMeteringDataRaw();
        meteringData.setExternalCode(externalCode);
        meteringData.setMeteringDate(time);
        meteringData.setWayEntering(WayEntering.EMCOS);
        meteringData.setDataSourceCode("EMCOS");
        meteringData.setStatus(DataStatus.RAW);
        meteringData.setUnitCode("кВт.ч.");
        meteringData.setParamCode(this.paramCode);
        meteringData.setVal(val);

        return meteringData;
    }


    public static class Builder {
        private EmcosConfig config;
        private List<MeteringPoint> points;
        private LocalDateTime reqestedDateTime;
        private String paramCode;
        private String emcosParamCode;
        private RegistryTemplate registryTemplate;

        public Builder config(EmcosConfig config) {
            this.config=config;
            return this;
        }

        public Builder points(List<MeteringPoint> points) {
            this.points=points;
            return this;
        }

        public Builder reqestedDateTime(LocalDateTime reqestedDateTime) {
            this.reqestedDateTime=reqestedDateTime;
            return this;
        }

        public Builder registryTemplate(RegistryTemplate registryTemplate) {
            this.registryTemplate=registryTemplate;
            return this;
        }
                
        public Builder paramCode(String paramCodee) {
            this.paramCode=paramCodee;

            switch (paramCode) {
                case "A+":
                    this.emcosParamCode = "1040";
                    break;
                case "A-":
                    this.emcosParamCode = "1041";
                    break;
                case "R+":
                    this.emcosParamCode = "1042";
                    break;
                case "R-":
                    this.emcosParamCode = "1043";
                    break;
            }
            return this;
        }

        public EmcosDataRequester build() {
            return new EmcosDataRequester(this);
        }
    }
}
