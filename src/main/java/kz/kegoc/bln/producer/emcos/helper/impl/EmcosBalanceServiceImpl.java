package kz.kegoc.bln.producer.emcos.helper.impl;

import com.google.common.collect.BiMap;
import kz.kegoc.bln.annotation.ParamCodes;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.LastLoadInfo;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.producer.emcos.helper.*;
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
public class EmcosBalanceServiceImpl implements EmcosBalanceService {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    private List<LastLoadInfo> lastLoadInfoList;
    private List<EmcosPointCfg> pointsCfg;

    @PostConstruct
    private void init() {
        this.lastLoadInfoList = lastLoadInfoService.findAll();
    }


    public List<DayMeteringBalanceRaw> request(String paramCode, LocalDateTime requestedDateTime) {
        if (pointsCfg==null)
            this.pointsCfg = emcosCfgService.requestCfg();

        try {
            String answer = new HttpReqesterImpl.Builder()
                .url(new URL(config.getUrl()))
                .method("POST")
                .body(buildBody(paramCode, requestedDateTime))
                .build()
                .doRequest();

            return parseAnswer(answer);
        }

        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String buildBody(String paramCode, LocalDateTime requestedDateTime) {
        String emcosParamCode = paramCodes.get(paramCode);

        String strPoints = pointsCfg.stream()
    		.filter(p -> p.getPointCode().equals("120620300070020001") || p.getPointCode().equals("121420300070010003") )
    		.filter(p -> p.getEmcosParamCode().equals(emcosParamCode))
            .map( p-> serializePointCfg(p, requestedDateTime))
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
    
    private List<DayMeteringBalanceRaw> parseAnswer(String answerData) throws Exception {
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
                    	meteringBalance.add(parseNode(rowData.item(j)));
                }
            }
        }
        
        return meteringBalance;
    }
    


    private String serializePointCfg(EmcosPointCfg emcosCfg, LocalDateTime requestedDateTime) {
        String emcosParamCode = paramCodes.get(emcosCfg.getParamCode());

        LastLoadInfo lastLoadInfo = lastLoadInfoList.stream()
            .filter(t -> t.getExternalCode().equals(emcosCfg.getPointCode()) && t.getParamCode().equals(emcosCfg.getParamCode()) )
            .findFirst()
            .orElse(null);
        	
    	return ""
                + "<ROW PPOINT_CODE=\"" + emcosCfg.getPointCode() + "\" "
                + "PML_ID=\"" + emcosParamCode + "\" "
                + "PBT=\"" + buildStartTime(lastLoadInfo).format(timeFormatter) + "\" "
                + "PET=\"" + requestedDateTime.format(timeFormatter) + "\" />";
    }    


    private LocalDateTime buildStartTime(LastLoadInfo lastLoadInfo) {
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
    
    private DayMeteringBalanceRaw parseNode(Node node) {
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

    @Inject EmcosCfgService emcosCfgService;

    @Inject @ParamCodes
    private BiMap<String, String> paramCodes;
}
