package kz.kegoc.bln.service.producer.hourly;

import kz.kegoc.bln.entity.media.HourlyMeteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.service.producer.common.AbstractFileMeteringDataProducer;
import kz.kegoc.bln.service.producer.common.MeteringDataProducer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@Singleton
@Startup
public class XmlHourlyMeteringDataProducer extends AbstractFileMeteringDataProducer<HourlyMeteringData> implements MeteringDataProducer {

    public XmlHourlyMeteringDataProducer() {
		super("xml");
	}

    
	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		super.execute();
    }

	
	protected List<HourlyMeteringData> loadFromFile(Path path) throws Exception {
		Document doc = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder()
						.parse(path.toFile());
		
		List<HourlyMeteringData> list = new ArrayList<>();
		for (int i=0; i<doc.getDocumentElement().getChildNodes().getLength(); i++) {
			Node nodeRow = doc.getDocumentElement().getChildNodes().item(i);
			if (nodeRow.getNodeName().equals("row")) 
				list.add(convert(nodeRow));
		}
		
		return list;
	}
	
	
	private HourlyMeteringData convert(Node node) throws Exception  {
		HourlyMeteringData d = new HourlyMeteringData();

		for (int i=0; i< node.getAttributes().getLength(); i++) {
			Node nodeAttr = node.getAttributes().item(i);
			switch (nodeAttr.getNodeName()) {
				case "meteringDate":
					SimpleDateFormat sd = new SimpleDateFormat("yyyy-mm-dd");
					d.setMeteringDate(sd.parse(nodeAttr.getNodeValue()));
					break;
				case "hour":
					d.setHour(Byte.parseByte(nodeAttr.getNodeValue()));
					break;
				case "meteringPointCode":
					d.setMeteringPointCode(nodeAttr.getNodeValue());
					break;
				case "paramCode":
					d.setParamCode(nodeAttr.getNodeValue());
					break;
				case "unitCode":
					d.setUnitCode(nodeAttr.getNodeValue());
					break;
				case "val":
					d.setVal(Double.parseDouble(nodeAttr.getNodeValue()));
					break;					
			}
		}
		d.setWayEntering(WayEnteringData.XML);
		d.setStatus(MeteringDataStatus.DRAFT);	
		d.setDataSourceCode("MANUAL");
		
		return d;
	} 	
}
