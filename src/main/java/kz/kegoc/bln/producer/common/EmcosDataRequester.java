package kz.kegoc.bln.producer.common;

import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.media.HourlyMeteringDataRaw;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.entity.media.WayEnteringData;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class EmcosDataRequester {
    private final EmcosConfig config;

    public EmcosDataRequester(EmcosConfig config) {
        this.config = config;
    }

    public List<HourlyMeteringDataRaw> requestMeteringData(List<MeteringPoint> points, LocalDateTime endDateTime) throws Exception {
        String requestBody = buildReqest(points, endDateTime);
        String answerData = doRequest(requestBody);
        return extractData(answerData);
    }

    private String buildReqest(List<MeteringPoint> points, LocalDateTime endDateTime) {
        String strPoints = points.stream()
                .map( p-> toXmlNode(p, "1041", endDateTime))
                .collect(Collectors.joining());
        
        System.out.println(strPoints);
        
        String data = ""
                + "<?xml version=\"1.0\" encoding=\"windows-1251\"?>"
                + "<DATAPACKET Version=\"2.0\">"
                + "<METADATA>"
                + "<FIELDS>"
                + "<FIELD attrname=\"PPOINT_CODE\" fieldtype=\"string\" required=\"true\" WIDTH=\"50\" />"
                + "<FIELD attrname=\"PML_ID\" fieldtype=\"fixed\" required=\"true\" WIDTH=\"6\" />"
                + "<FIELD attrname=\"PBT\" fieldtype=\"SQLdateTime\" />"
                + "<FIELD attrname=\"PET\" fieldtype=\"SQLdateTime\" />"
                + "</FIELDS>"
                + "<PARAMS LCID=\"0\" />"
                + "</METADATA>"
                + "<ROWDATA>"
                + strPoints
                + "</ROWDATA>"
                + "</DATAPACKET>";

        String requestBody = ""
                + "<?xml version=\"1.0\"?>"
                + "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<SOAP-ENV:Body>"
                + "<TransferEMCOSData xmlns=\"http://www.sigmatelas.lt/webservices\">"
                + "<parameters>"
                + "<aDProperty>"
                + "<UserId>" + config.getUser() + "</UserId>"
                + "<aPacked>" + config.getIsPacked().toString() + "</aPacked>"
                + "<Func>" + config.getFunc() + "</Func>"
                + "<Reserved></Reserved>"
                + "<AttType>" + config.getAttType() +"</AttType>"
                + "</aDProperty>"
                + "<aData>"
                + Base64.encodeBase64String(data.getBytes())
                + "</aData>"
                + "</parameters>"
                + "</TransferEMCOSData>"
                + "</SOAP-ENV:Body>"
                + "</SOAP-ENV:Envelope>";

        return requestBody;
    }

    private String doRequest(String requestBody) throws Exception {
        URL obj = new URL(config.getUrl());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(requestBody);
        wr.flush();
        wr.close();

        StringBuffer response = new StringBuffer();
        String output;
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        while ((output = in.readLine()) != null)
            response.append(output);
        in.close();

        String rawData = response.toString();

        int n1 = rawData.indexOf("<AnswerData>");
        int n2 = rawData.indexOf("</AnswerData>");
        String data = rawData.substring(n1+12, n2);

        return data;
    }


    private List<HourlyMeteringDataRaw> extractData(String answerData) throws Exception {
        List<HourlyMeteringDataRaw> pointDataList = new ArrayList<>();

        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new java.io.StringReader(new String(Base64.decodeBase64(answerData), "Cp1251"))));

        NodeList nodes =  doc.getDocumentElement().getParentNode().getFirstChild().getChildNodes();
        for(int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeName() == "ROWDATA") {
                NodeList rowData = nodes.item(i).getChildNodes();
                for(int j = 0; j < rowData.getLength(); j++) {
                    if (rowData.item(j).getNodeName() == "ROW")
                        pointDataList.add(fromXmlNode(rowData.item(j)));
                }
            }
        }

        return pointDataList;
    }


    private String toXmlNode(MeteringPoint point, String pmlId, LocalDateTime endDateTime) {
        LocalDateTime startDateTime;
        if (point.getLoadInfo()!=null && point.getLoadInfo().getLastLoadedDate()!=null) {
        	LocalDateTime lastLoadTime =point.getLoadInfo().getLastLoadedDate();
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
        
        return ""
                + "<ROW PPOINT_CODE=\"" + point.getExternalCode()
                + "\" PML_ID=\"" + pmlId + "\" "
                + "PBT=\"" + startDateTime.format(timeFormatter) + "\" "
                + "PET=\"" + endDateTime.format(timeFormatter) + "\" />";
    }


    private HourlyMeteringDataRaw fromXmlNode(Node node) {
        String pointCode = node.getAttributes().getNamedItem("PPOINT_CODE").getNodeValue() ;
        String pmlId = node.getAttributes().getNamedItem("PML_ID").getNodeValue() ;
        LocalDateTime time = null;
        Double val = null;

        String timeStr = node.getAttributes().getNamedItem("PBT").getNodeValue() ;
        String valStr = node.getAttributes().getNamedItem("PVAL").getNodeValue() ;

        if (timeStr!=null) {
            if (timeStr.indexOf("T")<0)
                timeStr = timeStr+"T00:00:00000";
            time = LocalDateTime.parse(timeStr, timeFormatter);
        }

        if (valStr!=null)
            val = Double.parseDouble(valStr);


        HourlyMeteringDataRaw d = new HourlyMeteringDataRaw();
        d.setExternalMeteringPointCode(pointCode);
        d.setMeteringDate(time);
        d.setHour( (byte) time.getHour());
        d.setWayEntering(WayEnteringData.EMCOS);
        d.setDataSourceCode("EMCOS");
        d.setStatus(MeteringDataStatus.DRAFT);
        d.setUnitCode("кВт.ч.");
        d.setParamCode("AE");
        d.setVal(val);

        return d;
    }

    

	public Date calendarFor(LocalDateTime time) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.YEAR, time.getYear());
        cal.set(Calendar.MONTH, time.getMonthValue());
        cal.set(Calendar.DAY_OF_MONTH, time.getDayOfMonth());
        cal.set(Calendar.HOUR_OF_DAY, time.getHour());
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }    
    
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
}
