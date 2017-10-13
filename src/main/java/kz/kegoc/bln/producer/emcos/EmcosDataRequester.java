package kz.kegoc.bln.producer.emcos;

import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.raw.EmcosMeteringPoint;
import kz.kegoc.bln.entity.media.raw.MinuteMeteringDataRaw;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URL;
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
    private List<EmcosMeteringPoint> cfg = null;
    private String strPoints = "";
    
    private EmcosDataRequester(Builder builder) {
        this.config = builder.config;
        this.paramCode = builder.paramCode;
        this.emcosParamCode = builder.emcosParamCode;
        this.points = builder.points;
        this.reqestedDateTime = builder.reqestedDateTime;
        this.registryTemplate = builder.registryTemplate;
    }


    public List<EmcosMeteringPoint> requestCfg() throws Exception {
        HttpRequester httpRequesterCfg = new HttpReqesterImpl.Builder()
    		.url(new URL(config.getUrl()))
    		.method("POST")
    		.body(buildBody("REQCFG"))
    		.build();    	
    	
        return extractMeteringPoints(httpRequesterCfg.doRequest());
    }
    
    
    public List<MinuteMeteringDataRaw> requestMeteringData() throws Exception {
    	cfg = requestCfg();
    	
        strPoints = cfg.stream()
            .map( p-> pointToXml(p))
            .collect(Collectors.joining());
    	
    	HttpRequester httpRequester = new HttpReqesterImpl.Builder()
    		.url(new URL(config.getUrl()))
    		.method("POST")
    		.body(buildBody("REQML"))
    		.build();

        return extractMeteringData(httpRequester.doRequest());
    }


    private String buildBody(String emcosFunc) {
        String data = registryTemplate.getTemplate("EMCOS_" + emcosFunc + "_DATA")
        	.replace("#points#", strPoints);

        String property = registryTemplate.getTemplate("EMCOS_" + emcosFunc + "_PROPERTY")
        	.replace("#user#", config.getUser())
        	.replace("#isPacked#", config.getIsPacked().toString())
        	.replace("#func#", emcosFunc)
        	.replace("#attType#", config.getAttType());
        
        String body = registryTemplate.getTemplate("EMCOS_" + emcosFunc + "_BODY")
        	.replace("#property#", property)
        	.replace("#data#", Base64.encodeBase64String(data.getBytes()));

        return body;
    }
    
    
    private List<EmcosMeteringPoint> extractMeteringPoints(String answerData) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(new InputSource(new StringReader( new String(Base64.decodeBase64(answerData), "Cp1251") )));

        NodeList nodes =  doc.getDocumentElement().getParentNode()
            .getFirstChild()
            .getChildNodes();    	
        
        List<EmcosMeteringPoint> points = new ArrayList<>();
        for(int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeName() == "ROWDATA") {
                NodeList rowData = nodes.item(i).getChildNodes();
                for(int j = 0; j < rowData.getLength(); j++) {
                    if (rowData.item(j).getNodeName() == "ROW") {
                        String pointCode = rowData.item(j).getAttributes()
                            .getNamedItem("POINT_CODE")
                            .getNodeValue();
                        
                        String emcosParamCode = rowData.item(j).getAttributes()
                            .getNamedItem("ML_ID")
                            .getNodeValue();
                        
                        String unitCode = rowData.item(j).getAttributes()
                            .getNamedItem("EU_CODE")
                            .getNodeValue();                        
                        
                        if (emcosParamCode!=null && (emcosParamCode.equals("1040") || emcosParamCode.equals("1041") || emcosParamCode.equals("1042") || emcosParamCode.equals("1043")) ) {
	                        EmcosMeteringPoint p = new EmcosMeteringPoint();
	                        p.setPointCode(pointCode);
	                        p.setEmcosParamCode(emcosParamCode);
	                        p.setUnitCode(unitCode);
	                        points.add(p);
                        }
                    }
                }
            }
        }    
        
        return points;
    }

    
    private List<MinuteMeteringDataRaw> extractMeteringData(String answerData) throws Exception {
        List<MinuteMeteringDataRaw> meteringData = new ArrayList<>();

        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(new InputSource(new StringReader( new String(Base64.decodeBase64(answerData), "Cp1251") )));

        NodeList nodes =  doc.getDocumentElement().getParentNode()
            .getFirstChild()
            .getChildNodes();

        for(int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeName() == "ROWDATA") {
                NodeList rowData = nodes.item(i).getChildNodes();
                for(int j = 0; j < rowData.getLength(); j++) {
                    if (rowData.item(j).getNodeName() == "ROW")
                        meteringData.add(fromXmlToMeterinData(rowData.item(j)));
                }
            }
        }

        return meteringData;
    }


    private String pointToXml(EmcosMeteringPoint emcosPoint) {
    	MeteringPoint point = points.stream()
    		.filter(t -> t.getExternalCode().equals(emcosPoint.getPointCode()))
    		.findFirst()
    		.orElse(null);
    	
    	return ""
                + "<ROW PPOINT_CODE=\"" + emcosPoint.getPointCode() + "\" "
                + "PML_ID=\"" + emcosParamCode + "\" "
                + "PBT=\"" + buildStartDateTime(point).format(timeFormatter) + "\" "
                + "PET=\"" + reqestedDateTime.format(timeFormatter) + "\" />";
    }


    private LocalDateTime buildStartDateTime(MeteringPoint point) {
        LocalDateTime startDateTime;
        if (point!=null && point.getLoadInfo()!=null && point.getLoadInfo().getLastLoadedDate()!=null) {
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

    
    private MinuteMeteringDataRaw fromXmlToMeterinData(Node node) {
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

        MinuteMeteringDataRaw meteringData = new MinuteMeteringDataRaw();
        meteringData.setExternalCode(externalCode);
        meteringData.setMeteringDate(time);
        meteringData.setWayEntering(WayEntering.EMCOS);
        meteringData.setDataSourceCode("EMCOS");
        meteringData.setStatus(DataStatus.RAW);
        meteringData.setUnitCode("кВт.ч.");
        meteringData.setParamCode(this.paramCode);
        meteringData.setVal(val);

        EmcosMeteringPoint emcosMeteringPoint = cfg.stream()
        	.filter(t -> t.getPointCode().equals(meteringData.getExternalCode()))
        	.filter(t -> t.getEmcosParamCode().equals(emcosParamCode))
        	.findFirst()
        	.get();
        
        if (emcosMeteringPoint!=null) 
        	meteringData.setUnitCode(emcosMeteringPoint.getUnitCode());
        
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
