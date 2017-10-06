package kz.kegoc.bln.producer.raw.hourly;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
//import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Node;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import kz.kegoc.bln.entity.media.HourlyMeteringDataRaw;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.producer.common.MeteringDataProducer;
import kz.kegoc.bln.queue.common.MeteringDataQueueService;

@Singleton
@Startup
public class EmcosHourlyMeteringDataRawProducer implements MeteringDataProducer {
    private LocalDateTime lastDateTime = LocalDateTime.of(2017, Month.SEPTEMBER, 30, 23, 45);
	
	
	@Schedule(second = "*/30", minute = "*", hour = "*", persistent = false)
	public void execute() {
    	
		System.out.println("EmcosHourlyMeteringDataRawProducer started");
		
    	String URL = "http://10.8.144.11/STWS2/STExchangeWS2.dll/soap/IST_ExchWebService2";
    	String USER_ID = "yug";
    	//String URL = "http://10.9.44.13/STWS/STExchangeWS.dll/soap/IST_ExchWebService";
    	//String USER_ID = "Akmol";
    	String FUNC = "REQML";
    	Boolean IS_PACKED = false;
    	String ATT_TYPE = "1";    	
    	
    	List<String> points = new ArrayList<>();
    	points.add("120620300070020001");
    	
    	LocalDateTime now = LocalDateTime.now();
    	LocalDateTime startDateTime = this.lastDateTime.plusMinutes(15); 
    	LocalDateTime endDateTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), Math.round(now.getMinute() / 15)*15 );
    	
    	System.out.println(startDateTime);
    	System.out.println(endDateTime);
    	
		String strPoints = points.stream()
			 	.map( p-> toXmlNode(p, "1041", startDateTime, endDateTime))
			 	.collect(Collectors.joining());   
		 
		 String params = ""
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
	 				+ "<UserId>" + USER_ID + "</UserId>"
	 				+ "<aPacked>" + IS_PACKED.toString() + "</aPacked>"
	 				+ "<Func>" + FUNC + "</Func>"
	 				+ "<Reserved></Reserved>"
	 				+ "<AttType>" + ATT_TYPE +"</AttType>"
	 				+ "</aDProperty>"
	 				+ "<aData>"
	 				+ Base64.encodeBase64String(params.getBytes())
	 				+ "</aData>"
	 				+ "</parameters>"
	 				+ "</TransferEMCOSData>"
	 				+ "</SOAP-ENV:Body>"
	 				+ "</SOAP-ENV:Envelope>";
		  
		  
		try {
			URL obj = new URL(URL);
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
			System.out.println(rawData);
			
			int n1 = rawData.indexOf("<AnswerData>");
			int n2 = rawData.indexOf("</AnswerData>");
			String data = rawData.substring(n1+12, n2);
			
			
			List<HourlyMeteringDataRaw> pointDataList = new ArrayList<>(); 
			
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder()
					.parse(new InputSource(new java.io.StringReader(new String(Base64.decodeBase64(data), "Cp1251"))));			
			
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
			
			
			//System.out.println(pointDataList);
			
			service.addMeteringListData(pointDataList);
			
			this.lastDateTime = endDateTime;
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		
		System.out.println("EmcosHourlyMeteringDataRawProducer finished");	
    }
    
    
    
	public String toXmlNode(String pointCode, String pmlId, LocalDateTime startTime, LocalDateTime endTime) {
		return ""
		+ "<ROW PPOINT_CODE=\"" + pointCode 
		+ "\" PML_ID=\"" + pmlId + "\" "
		+ "PBT=\"" + startTime.format(timeFormatter) + "\" "
		+ "PET=\"" + endTime.format(timeFormatter) + "\" />";
	}
	
	
	public HourlyMeteringDataRaw fromXmlNode(Node node) {
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
		d.setMeteringDate(Date.from(time.toInstant(ZoneOffset.UTC)));
		d.setHour( (byte) time.getHour());
		d.setWayEntering(WayEnteringData.EMCOS);
		d.setDataSourceCode("EMCOS");
		d.setStatus(MeteringDataStatus.DRAFT);
		d.setUnitCode("кВт.ч.");
		d.setParamCode("AE");
		d.setVal(val);
		
		return d;
	}
	
	
	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:'00000'");
	
	@Inject
	private MeteringDataQueueService<HourlyMeteringDataRaw> service;
}
