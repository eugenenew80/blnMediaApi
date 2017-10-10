package kz.kegoc.bln.producer.raw;

import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.entity.media.raw.MinuteMeteringDataRaw;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmcosDataRequester {
    private final EmcosConfig config;

    public EmcosDataRequester(EmcosConfig config) {
        this.config = config;
    }

    public List<MinuteMeteringDataRaw> requestMeteringData(List<MeteringPoint> points, LocalDateTime endDateTime) throws Exception {
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
        URL url = new URL(config.getUrl());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(requestBody);
            wr.flush();
        }

        StringBuffer response = new StringBuffer();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String output;
            while ((output = in.readLine()) != null)
                response.append(output);
        }

        String rawData = response.toString();

        int n1 = rawData.indexOf("<AnswerData>");
        int n2 = rawData.indexOf("</AnswerData>");

        if (n2>n1)
            return rawData.substring(n1+12, n2);
        else
            return "";
    }


    private List<MinuteMeteringDataRaw> extractData(String answerData) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(new InputSource(new StringReader(new String(Base64.decodeBase64(answerData), "Cp1251"))));

        List<MinuteMeteringDataRaw> pointDataList = new ArrayList<>();

        NodeList nodes =  doc.getDocumentElement().getParentNode()
            .getFirstChild()
            .getChildNodes();

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
        
        return ""
                + "<ROW PPOINT_CODE=\"" + point.getExternalCode()
                + "\" PML_ID=\"" + pmlId + "\" "
                + "PBT=\"" + startDateTime.format(timeFormatter) + "\" "
                + "PET=\"" + endDateTime.format(timeFormatter) + "\" />";
    }


    private MinuteMeteringDataRaw fromXmlNode(Node node) {
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

        MinuteMeteringDataRaw d = new MinuteMeteringDataRaw();
        d.setExternalCode(pointCode);
        d.setMeteringDate(time);
        d.setWayEntering(WayEnteringData.EMCOS);
        d.setDataSourceCode("EMCOS");
        d.setStatus(MeteringDataStatus.DRAFT);
        d.setUnitCode("кВт.ч.");
        d.setParamCode("AE");
        d.setVal(val);

        return d;
    }

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
}
