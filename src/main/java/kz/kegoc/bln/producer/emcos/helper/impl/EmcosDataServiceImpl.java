package kz.kegoc.bln.producer.emcos.helper.impl;

import com.google.common.collect.BiMap;
import jodd.util.StringUtil;
import kz.kegoc.bln.annotation.ParamCodes;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.entity.media.LastLoadInfo;
import kz.kegoc.bln.producer.emcos.helper.EmcosConfig;
import kz.kegoc.bln.producer.emcos.helper.EmcosDataService;
import kz.kegoc.bln.producer.emcos.helper.EmcosPointCfg;
import kz.kegoc.bln.producer.emcos.helper.MinuteMeteringDataRaw;
import kz.kegoc.bln.producer.emcos.helper.RegistryTemplate;
import kz.kegoc.bln.service.media.LastLoadInfoService;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class EmcosDataServiceImpl implements EmcosDataService {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    private List<LastLoadInfo> lastLoadInfoList;
    private List<EmcosPointCfg> pointsCfg;

    @PostConstruct
    private void init() {
        this.lastLoadInfoList = lastLoadInfoService.findAll();
    }


    public List<EmcosPointCfg> requestCfg()  {
        try {
            String answer = new HttpReqesterImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(buildBodyCfg())
                .build()
                .doRequest();

            return extractCfg(answer);
        }

        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    
    public List<MinuteMeteringDataRaw> requestData(String paramCode, LocalDateTime requestedDateTime) {
        if (pointsCfg==null)
            this.pointsCfg = requestCfg();

        try {
            String answer = new HttpReqesterImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(buildBodyData(paramCode, requestedDateTime))
                .build()
                .doRequest();

            return extractData(answer);
        }

        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public List<DayMeteringBalanceRaw> requestBalance(String paramCode, LocalDateTime requestedDateTime) {
        if (pointsCfg==null)
            this.pointsCfg = requestCfg();

        try {
            String answer = new HttpReqesterImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(buildBodyBalance(paramCode, requestedDateTime))
                .build()
                .doRequest();

            return extractBalance(answer);
        }

        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    
    private String buildBodyCfg() {
        String data = registryTemplate.getTemplate("EMCOS_REQCFG_DATA")
        	.replace("#points#", "");

        String property = registryTemplate.getTemplate("EMCOS_REQCFG_PROPERTY")
        	.replace("#user#", config.getUser())
        	.replace("#isPacked#", config.getIsPacked().toString())
        	.replace("#func#", "REQCFG")
        	.replace("#attType#", config.getAttType());
        
        String body = registryTemplate.getTemplate("EMCOS_REQCFG_BODY")
        	.replace("#property#", property)
        	.replace("#data#", Base64.encodeBase64String(data.getBytes()));

        return body;
    }
    
    
    private String buildBodyData(String paramCode, LocalDateTime requestedDateTime) {
        String emcosParamCode = paramCodes.get(paramCode);

        String strPoints = pointsCfg.stream()
    		.filter(p -> p.getPointCode().equals("120620300070020001") || p.getPointCode().equals("121420300070010003") )	
    		.filter(p -> p.getEmcosParamCode().equals(emcosParamCode))
            .map( p-> pointToData(p, requestedDateTime))
            .collect(Collectors.joining());

        String data = registryTemplate.getTemplate("EMCOS_REQML_DATA")
        	.replace("#points#", strPoints);

        String property = registryTemplate.getTemplate("EMCOS_REQML_PROPERTY")
        	.replace("#user#", config.getUser())
        	.replace("#isPacked#", config.getIsPacked().toString())
        	.replace("#func#", "REQML")
        	.replace("#attType#", config.getAttType());
        
        String body = registryTemplate.getTemplate("EMCOS_REQML_BODY")
        	.replace("#property#", property)
        	.replace("#data#", Base64.encodeBase64String(data.getBytes()));

        return body;
    }
    
    
    private String buildBodyBalance(String paramCode, LocalDateTime requestedDateTime) {
        String emcosParamCode = paramCodes.get(paramCode);

        String strPoints = pointsCfg.stream()
    		.filter(p -> p.getPointCode().equals("120620300070020001") || p.getPointCode().equals("121420300070010003") )
    		.filter(p -> p.getEmcosParamCode().equals(emcosParamCode))
            .map( p-> pointToBalance(p, requestedDateTime))
            .collect(Collectors.joining());
        
        String data = registryTemplate.getTemplate("EMCOS_REQML_DATA")
        	.replace("#points#", strPoints);

        String property = registryTemplate.getTemplate("EMCOS_REQML_PROPERTY")
        	.replace("#user#", config.getUser())
        	.replace("#isPacked#", config.getIsPacked().toString())
        	.replace("#func#", "REQML")
        	.replace("#attType#", config.getAttType());
        
        String body = registryTemplate.getTemplate("EMCOS_REQML_BODY")
        	.replace("#property#", property)
        	.replace("#data#", Base64.encodeBase64String(data.getBytes()));

        return body;
    }
    
    
    private List<EmcosPointCfg> extractCfg(String answer) throws Exception {
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
                        EmcosPointCfg pointCfg = nodeToCfg(rowData.item(j));
                        if (StringUtil.isNotEmpty(pointCfg.getParamCode()))
                            pointsCfg.add(pointCfg);
                    }
                }
            }
        }    
        
        return pointsCfg;
    }


    private List<MinuteMeteringDataRaw> extractData(String answerData) throws Exception {
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
                        meteringData.add(nodeToData(rowData.item(j)));
                }
            }
        }

        return meteringData;
    }


    private List<DayMeteringBalanceRaw> extractBalance(String answerData) throws Exception {
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
                    	meteringBalance.add(nodeToBalance(rowData.item(j)));
                }
            }
        }
        
        return meteringBalance;
    }
    

    private String pointToData(EmcosPointCfg emcosCfg, LocalDateTime requestedDateTime) {
        String emcosParamCode = paramCodes.get(emcosCfg.getParamCode());

        LastLoadInfo lastLoadInfo = lastLoadInfoList.stream()
    		.filter(t -> t.getExternalCode().equals(emcosCfg.getPointCode()) && t.getParamCode().equals(emcosCfg.getParamCode()) )
    		.findFirst()
    		.orElse(null);
        	
    	return ""
		        + "<ROW PPOINT_CODE=\"" + emcosCfg.getPointCode() + "\" "
		        + "PML_ID=\"" + emcosParamCode + "\" "
		        + "PBT=\"" + buildStartDateTimeData(lastLoadInfo).format(timeFormatter) + "\" "
		        + "PET=\"" + requestedDateTime.format(timeFormatter) + "\" />";
    }

    
    private String pointToBalance(EmcosPointCfg emcosCfg, LocalDateTime requestedDateTime) {
        String emcosParamCode = paramCodes.get(emcosCfg.getParamCode());

        LastLoadInfo lastLoadInfo = lastLoadInfoList.stream()
            .filter(t -> t.getExternalCode().equals(emcosCfg.getPointCode()) && t.getParamCode().equals(emcosCfg.getParamCode()) )
            .findFirst()
            .orElse(null);
        	
    	return ""
                + "<ROW PPOINT_CODE=\"" + emcosCfg.getPointCode() + "\" "
                + "PML_ID=\"" + emcosParamCode + "\" "
                + "PBT=\"" + buildStartDateTimeBalance(lastLoadInfo).format(timeFormatter) + "\" "
                + "PET=\"" + requestedDateTime.format(timeFormatter) + "\" />";
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
    

    private EmcosPointCfg nodeToCfg(Node node) {
        EmcosPointCfg p = new EmcosPointCfg();

        String pointCode = node.getAttributes()
                .getNamedItem("POINT_CODE")
                .getNodeValue();

        String emcosParamCode = node.getAttributes()
                .getNamedItem("ML_ID")
                .getNodeValue();

        String unitCode = node.getAttributes()
                .getNamedItem("EU_CODE")
                .getNodeValue();

        p.setPointCode(pointCode);
        p.setEmcosParamCode(emcosParamCode);
        p.setUnitCode(unitCode);

        if (paramCodes.containsValue(emcosParamCode))
            p.setParamCode(paramCodes.inverse().get(emcosParamCode));

        return p;
    }


    private MinuteMeteringDataRaw nodeToData(Node node) {
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

        MinuteMeteringDataRaw data = new MinuteMeteringDataRaw();
        data.setExternalCode(externalCode);
        data.setMeteringDate(time);
        data.setWayEntering(WayEntering.EMCOS);
        data.setDataSourceCode("EMCOS");
        data.setStatus(DataStatus.RAW);
        data.setUnitCode("-");
        data.setVal(val);

        EmcosPointCfg pointCfg = pointsCfg.stream()
        	.filter(t -> t.getPointCode().equals(data.getExternalCode()) && t.getEmcosParamCode().equals(emcosParamCode))
        	.findFirst()
        	.orElse(null);

        if (pointCfg!=null) {
            data.setParamCode(pointCfg.getParamCode());
            data.setUnitCode(pointCfg.getUnitCode());
        }
        
        return data;
    }


    private DayMeteringBalanceRaw nodeToBalance(Node node) {
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

        DayMeteringBalanceRaw balance = new DayMeteringBalanceRaw();
        balance.setExternalCode(externalCode);
        balance.setMeteringDate(date.atStartOfDay());
        balance.setWayEntering(WayEntering.EMCOS);
        balance.setDataSourceCode("EMCOS");
        balance.setStatus(DataStatus.RAW);
        balance.setUnitCode("-");
        balance.setVal(val);

        EmcosPointCfg pointCfg = pointsCfg.stream()
        	.filter(t -> t.getPointCode().equals(balance.getExternalCode()) && t.getEmcosParamCode().equals(emcosParamCode))
        	.findFirst()
        	.orElse(null);

        if (pointCfg!=null) {
            balance.setParamCode(pointCfg.getParamCode());
            balance.setUnitCode(pointCfg.getUnitCode());
        }
        
        return balance;
    }


    @Inject
    private LastLoadInfoService lastLoadInfoService;

    @Inject
    private RegistryTemplate registryTemplate;

    @Inject
    private EmcosConfig config;

    @Inject @ParamCodes
    private BiMap<String, String> paramCodes;
}
