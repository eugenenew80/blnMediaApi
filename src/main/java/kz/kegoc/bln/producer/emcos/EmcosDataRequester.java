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
    private final HttpRequest httpRequest;

    private EmcosDataRequester(Builder builder) {
        this.config = builder.config;
        this.paramCode = builder.paramCode;
        this.emcosParamCode = builder.emcosParamCode;
        this.points = builder.points;
        this.reqestedDateTime = builder.reqestedDateTime;
        this.httpRequest = new HttpReqestImpl();
    }


    public List<MinuteMeteringDataRaw> requestMeteringData() throws Exception {
    	String requestBody = buildReqest();
        String answerData = httpRequest.doRequest(new URL(config.getUrl()), requestBody);
        return extractData(answerData);
    }


    private String buildReqest() {
        String strPoints = points.stream()
            .map( p-> toXmlNode(p))
            .collect(Collectors.joining());

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

    private List<MinuteMeteringDataRaw> extractData(String answerData) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(new InputSource(new StringReader(new String(Base64.decodeBase64(answerData), "Cp1251"))));

        NodeList nodes =  doc.getDocumentElement().getParentNode()
            .getFirstChild()
            .getChildNodes();

        List<MinuteMeteringDataRaw> meteringData = new ArrayList<>();
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
                    1, //now.getDayOfMonth(),
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
