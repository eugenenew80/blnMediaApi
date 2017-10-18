package kz.kegoc.bln.producer.emcos.helper.impl;

import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.DayMeteringBalanceRaw;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.EmcosMeteringPointCfg;
import kz.kegoc.bln.entity.media.MinuteMeteringDataRaw;
import kz.kegoc.bln.producer.emcos.helper.EmcosConfig;
import kz.kegoc.bln.producer.emcos.helper.EmcosDataService;
import kz.kegoc.bln.producer.emcos.helper.RegistryTemplate;
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

public class EmcosDataServiceImpl implements EmcosDataService {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    private final EmcosConfig config;
    private final List<MeteringPoint> points;
    private final LocalDateTime reqestedDateTime;
    private final String paramCode;
    private final String emcosParamCode;
    private final RegistryTemplate registryTemplate;
    private List<EmcosMeteringPointCfg> cfg = null;

    private EmcosDataServiceImpl(Builder builder) {
        this.config = builder.config;
        this.paramCode = builder.paramCode;
        this.emcosParamCode = builder.emcosParamCode;
        this.points = builder.points;
        this.reqestedDateTime = builder.reqestedDateTime;
        this.registryTemplate = builder.registryTemplate;
    }


    public List<EmcosMeteringPointCfg> requestCfg() throws Exception {
        String answer = new HttpReqesterImpl.Builder()
            .url(new URL(config.getUrl()))
            .method("POST")
            .body(buildBodyCfg("REQCFG"))
            .build()
            .doRequest();

        return extractCfg(answer);
    }
    
    
    public List<MinuteMeteringDataRaw> requestMeteringData() throws Exception {
    	cfg = requestCfg();
    	
        String answer = new HttpReqesterImpl.Builder()
            .url(new URL(config.getUrl()))
            .method("POST")
            .body(buildBodyData("REQML"))
            .build()
            .doRequest();

        return extractMeteringData(answer);
    }


    public List<DayMeteringBalanceRaw> requestMeteringBalance() throws Exception {
    	cfg = requestCfg();
    	
        String answer = new HttpReqesterImpl.Builder()
            .url(new URL(config.getUrl()))
            .method("POST")
            .body(buildBodyBalance("REQML"))
            .build()
            .doRequest();

        return extractMeteringBalance(answer);
    }    

    
    private String buildBodyCfg(String emcosFunc) {
        String data = registryTemplate.getTemplate("EMCOS_" + emcosFunc + "_DATA")
        	.replace("#points#", "");

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
    
    
    private String buildBodyData(String emcosFunc) {
        String strPoints = cfg.stream()
    		.filter(p -> p.getPointCode().equals("120620300070020001") || p.getPointCode().equals("121420300070010003") )	
            .map( p-> pointToXmlData(p))
            .collect(Collectors.joining());

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
    
    
    private String buildBodyBalance(String emcosFunc) {
        String strPoints = cfg.stream()
    		.filter(p -> p.getPointCode().equals("120620300070020001") || p.getPointCode().equals("121420300070010003") )	
            .map( p-> pointToXmlBalance(p))
            .collect(Collectors.joining());
        
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
    
    
    private List<EmcosMeteringPointCfg> extractCfg(String answer) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(new InputSource(new StringReader( new String(Base64.decodeBase64(answer), "Cp1251") )));

        NodeList nodes =  doc.getDocumentElement().getParentNode()
            .getFirstChild()
            .getChildNodes();    	
        
        cfg = new ArrayList<>();
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
                        
                        if  (!(emcosParamCode!=null && (emcosParamCode.equals("1040") || emcosParamCode.equals("1041") || emcosParamCode.equals("1042") || emcosParamCode.equals("1043")) )) {
	                        EmcosMeteringPointCfg p = new EmcosMeteringPointCfg();
	                        p.setPointCode(pointCode);
	                        p.setEmcosParamCode(emcosParamCode);
	                        p.setUnitCode(unitCode);
	                        cfg.add(p);
                        }
                    }
                }
            }
        }    
        
        return cfg;
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


    private List<DayMeteringBalanceRaw> extractMeteringBalance(String answerData) throws Exception {
    	List<DayMeteringBalanceRaw> meteringBalance = new ArrayList<>();
        
        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(new InputSource(new StringReader( new String(Base64.decodeBase64(answerData), "Cp1251") )));

        
        System.out.println( new String(Base64.decodeBase64(answerData), "Cp1251"));
        
        NodeList nodes =  doc.getDocumentElement().getParentNode()
            .getFirstChild()
            .getChildNodes();

        for(int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeName() == "ROWDATA") {
                NodeList rowData = nodes.item(i).getChildNodes();
                for(int j = 0; j < rowData.getLength(); j++) {
                    if (rowData.item(j).getNodeName() == "ROW")
                    	meteringBalance.add(fromXmlToMeteringBalance(rowData.item(j)));
                }
            }
        }
        
        return meteringBalance;
    }
    
    
    private String pointToXmlData(EmcosMeteringPointCfg emcosCfg) {
    	MeteringPoint point = points.stream()
    		.filter(t -> t.getExternalCode().equals(emcosCfg.getPointCode()))
    		.findFirst()
    		.orElse(null);
    	
    	return ""
                + "<ROW PPOINT_CODE=\"" + emcosCfg.getPointCode() + "\" "
                + "PML_ID=\"" + emcosParamCode + "\" "
                + "PBT=\"" + buildStartDateTime(point).format(timeFormatter) + "\" "
                + "PET=\"" + reqestedDateTime.format(timeFormatter) + "\" />";
    }

    
    private String pointToXmlBalance(EmcosMeteringPointCfg emcosCfg) {
    	MeteringPoint point = points.stream()
    		.filter(t -> t.getExternalCode().equals(emcosCfg.getPointCode()))
    		.findFirst()
    		.orElse(null);
    	
    	return ""
                + "<ROW PPOINT_CODE=\"" + emcosCfg.getPointCode() + "\" "
                + "PML_ID=\"" + emcosParamCode + "\" "
                + "PBT=\"" + buildStartDateTime(point).toLocalDate().plusDays(1).format(dateFormatter) + "\" "
                + "PET=\"" + buildStartDateTime(point).toLocalDate().plusDays(1).format(dateFormatter) + "\" />";
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
        meteringData.setUnitCode("-");
        meteringData.setParamCode(this.paramCode);
        meteringData.setVal(val);

        EmcosMeteringPointCfg emcosMeteringPoint = cfg.stream()
        	.filter(t -> t.getPointCode().equals(meteringData.getExternalCode()))
        	.filter(t -> t.getEmcosParamCode().equals(emcosParamCode))
        	.findFirst()
        	.orElse(null);
        
        if (emcosMeteringPoint!=null) 
        	meteringData.setUnitCode(emcosMeteringPoint.getUnitCode());
        
        return meteringData;
    }


    private DayMeteringBalanceRaw fromXmlToMeteringBalance(Node node) {
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

        DayMeteringBalanceRaw meteringBalance = new DayMeteringBalanceRaw();
        meteringBalance.setExternalCode(externalCode);
        meteringBalance.setMeteringDate(date);
        meteringBalance.setWayEntering(WayEntering.EMCOS);
        meteringBalance.setDataSourceCode("EMCOS");
        meteringBalance.setStatus(DataStatus.RAW);
        meteringBalance.setUnitCode("-");
        meteringBalance.setParamCode(this.paramCode);
        meteringBalance.setVal(val);

        EmcosMeteringPointCfg emcosMeteringPoint = cfg.stream()
        	.filter(t -> t.getPointCode().equals(meteringBalance.getExternalCode()))
        	.filter(t -> t.getEmcosParamCode().equals(emcosParamCode))
        	.findFirst()
        	.orElse(null);
        
        if (emcosMeteringPoint!=null) 
        	meteringBalance.setUnitCode(emcosMeteringPoint.getUnitCode());
        
        return meteringBalance;
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
                case "AB+":
                    this.emcosParamCode = "1498";
                    break;
                case "AB-":
                    this.emcosParamCode = "1499";
                    break;
                case "RB+":
                    this.emcosParamCode = "1500";
                    break;
                case "RB-":
                    this.emcosParamCode = "1501";
                    break;                    
            }
            return this;
        }

        public EmcosDataServiceImpl build() {
            return new EmcosDataServiceImpl(this);
        }
    }
}
