package kz.kegoc.bln.producer.emcos.helper.impl;

import kz.kegoc.bln.annotation.ParamCodes;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.entity.media.LastLoadInfo;
import kz.kegoc.bln.producer.emcos.helper.EmcosConfig;
import kz.kegoc.bln.producer.emcos.helper.EmcosDataService;
import kz.kegoc.bln.producer.emcos.helper.EmcosPointParamCfg;
import kz.kegoc.bln.producer.emcos.helper.MinuteMeteringDataRaw;
import kz.kegoc.bln.producer.emcos.helper.RegistryTemplate;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmcosDataServiceImpl implements EmcosDataService {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    private final EmcosConfig config;
    private final List<LastLoadInfo> lastLoadInfoList;
    private final LocalDateTime reqestedDateTime;
    private final String paramCode;
    private final String emcosParamCode;
    private final RegistryTemplate registryTemplate;
    private List<EmcosPointParamCfg> pointsCfg;

    private EmcosDataServiceImpl(Builder builder) {
        this.config = builder.config;
        this.paramCode = builder.paramCode;
        this.emcosParamCode = paramCodes.get(builder.paramCode);
        this.lastLoadInfoList = builder.lastLoadInfoList;
        this.reqestedDateTime = builder.reqestedDateTime;
        this.registryTemplate = builder.registryTemplate;
        this.pointsCfg = builder.pointsCfg;
    }


    public List<EmcosPointParamCfg> requestCfg() throws Exception {
        String answer = new HttpReqesterImpl.Builder()
            .url(new URL(config.getUrl()))
            .method("POST")
            .body(buildBodyCfg("REQCFG"))
            .build()
            .doRequest();

        return extractCfg(answer);
    }
    
    
    public List<MinuteMeteringDataRaw> requestMeteringData() throws Exception {
        String answer = new HttpReqesterImpl.Builder()
            .url(new URL(config.getUrl()))
            .method("POST")
            .body(buildBodyData("REQML"))
            .build()
            .doRequest();

        return extractMeteringData(answer);
    }


    public List<DayMeteringBalanceRaw> requestMeteringBalance() throws Exception {
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
        String strPoints = pointsCfg.stream()
    		.filter(p -> p.getPointCode().equals("120620300070020001") || p.getPointCode().equals("121420300070010003") )	
    		.filter(p -> p.getEmcosParamCode().equals(emcosParamCode))
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
        String strPoints = pointsCfg.stream()
    		.filter(p -> p.getPointCode().equals("120620300070020001") || p.getPointCode().equals("121420300070010003") )
    		.filter(p -> p.getEmcosParamCode().equals(emcosParamCode))
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
    
    
    private List<EmcosPointParamCfg> extractCfg(String answer) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(new InputSource(new StringReader( new String(Base64.decodeBase64(answer), "Cp1251") )));

        NodeList nodes =  doc.getDocumentElement().getParentNode()
            .getFirstChild()
            .getChildNodes();    	
        
        pointsCfg = new ArrayList<>();
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
                        
                        if  (emcosParamCode!=null && (emcosParamCode.equals("1040") || emcosParamCode.equals("1041") || emcosParamCode.equals("1042") || emcosParamCode.equals("1043") 
                        		                   || emcosParamCode.equals("1498") || emcosParamCode.equals("1499") || emcosParamCode.equals("1500") || emcosParamCode.equals("1501") ) ) {
	                        EmcosPointParamCfg p = new EmcosPointParamCfg();
	                        p.setPointCode(pointCode);
	                        p.setEmcosParamCode(emcosParamCode);
	                        p.setUnitCode(unitCode);
	                        pointsCfg.add(p);
                        }
                    }
                }
            }
        }    
        
        return pointsCfg;
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
    
    
    private String pointToXmlData(EmcosPointParamCfg emcosCfg) {
    	LastLoadInfo lastLoadInfo = lastLoadInfoList.stream()
    		.filter(t -> t.getExternalCode().equals(emcosCfg.getPointCode()) && t.getParamCode().equals(paramCode) )
    		.findFirst()
    		.orElse(null);
        	
    	return ""
		        + "<ROW PPOINT_CODE=\"" + emcosCfg.getPointCode() + "\" "
		        + "PML_ID=\"" + emcosParamCode + "\" "
		        + "PBT=\"" + buildStartDateTimeData(lastLoadInfo).format(timeFormatter) + "\" "
		        + "PET=\"" + reqestedDateTime.format(timeFormatter) + "\" />";    	
    }

    
    private String pointToXmlBalance(EmcosPointParamCfg emcosCfg) {
    	LastLoadInfo lastLoadInfo = lastLoadInfoList.stream()
        		.filter(t -> t.getExternalCode().equals(emcosCfg.getPointCode()) && t.getParamCode().equals(paramCode) )
        		.findFirst()
        		.orElse(null);
        	
    	return ""
                + "<ROW PPOINT_CODE=\"" + emcosCfg.getPointCode() + "\" "
                + "PML_ID=\"" + emcosParamCode + "\" "
                + "PBT=\"" + buildStartDateTimeBalance(lastLoadInfo).format(timeFormatter) + "\" "
                + "PET=\"" + reqestedDateTime.format(timeFormatter) + "\" />";
    }    

    
    private LocalDateTime buildStartDateTimeData(LastLoadInfo lastLoadInfo) {
        LocalDateTime startDateTime;
        if (lastLoadInfo!=null && lastLoadInfo.getLastLoadDate()!=null) {
            LocalDateTime lastLoadTime = lastLoadInfo.getLastLoadDate();
            startDateTime = LocalDateTime.of(
            	lastLoadTime.getYear(), 
            	lastLoadTime.getMonth(), 
            	lastLoadTime.getDayOfMonth(), 
            	lastLoadTime.getHour(), 
            	0
            );
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

    
    private LocalDateTime buildStartDateTimeBalance(LastLoadInfo lastLoadInfo) {
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

        EmcosPointParamCfg emcosMeteringPoint = pointsCfg.stream()
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
        meteringBalance.setMeteringDate(date.atStartOfDay());
        meteringBalance.setWayEntering(WayEntering.EMCOS);
        meteringBalance.setDataSourceCode("EMCOS");
        meteringBalance.setStatus(DataStatus.RAW);
        meteringBalance.setUnitCode("-");
        meteringBalance.setParamCode(this.paramCode);
        meteringBalance.setVal(val);

        EmcosPointParamCfg emcosMeteringPoint = pointsCfg.stream()
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
        private List<LastLoadInfo> lastLoadInfoList = new ArrayList<>();
        private LocalDateTime reqestedDateTime;
        private String paramCode;
        private RegistryTemplate registryTemplate;
        private List<EmcosPointParamCfg> pointsCfg = new ArrayList<>();

        public Builder config(EmcosConfig config) {
            this.config=config;
            return this;
        }

        public Builder lastLoadInfoList(List<LastLoadInfo> lastLoadInfoList) {
            this.lastLoadInfoList=lastLoadInfoList;
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
        
        public Builder pointsCfg(List<EmcosPointParamCfg> pointsCfg) {
        	this.pointsCfg = pointsCfg;
        	return this;
        }
        
        public Builder paramCode(String paramCodee) {
            this.paramCode=paramCodee;
            return this;
        }

        public EmcosDataServiceImpl build() {
            return new EmcosDataServiceImpl(this);
        }
    }

    @Inject @ParamCodes
    private Map<String, String> paramCodes;
}
