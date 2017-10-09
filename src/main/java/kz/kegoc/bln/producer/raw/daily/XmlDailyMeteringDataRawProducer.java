package kz.kegoc.bln.producer.raw.daily;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.parsers.DocumentBuilderFactory;

import kz.kegoc.bln.producer.common.AbstractFileMeteringDataProducer;
import org.w3c.dom.*;

import kz.kegoc.bln.entity.media.DailyMeteringDataRaw;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.producer.common.MeteringDataProducer;


@Singleton
@Startup
public class XmlDailyMeteringDataRawProducer extends AbstractFileMeteringDataProducer<DailyMeteringDataRaw> implements MeteringDataProducer {
    
    public XmlDailyMeteringDataRawProducer() {
		super("daily/xml");
	}

    
	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		super.execute();
    }

	
	protected List<DailyMeteringDataRaw> loadFromFile(Path path) throws Exception {
		Document doc = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder()
						.parse(path.toFile());
		
		List<DailyMeteringDataRaw> list = new ArrayList<>();
		for (int i=0; i<doc.getDocumentElement().getChildNodes().getLength(); i++) {
			Node nodeRow = doc.getDocumentElement().getChildNodes().item(i);
			if (nodeRow.getNodeName().equals("row")) 
				list.add(convert(nodeRow));
		}
		
		return list;
	}
	
	
	private DailyMeteringDataRaw convert(Node node) throws Exception  {
		DailyMeteringDataRaw d = new DailyMeteringDataRaw();

		for (int i=0; i< node.getAttributes().getLength(); i++) {
			Node nodeAttr = node.getAttributes().item(i);
			switch (nodeAttr.getNodeName()) {
				case "meteringDate":
					d.setMeteringDate(LocalDateTime.parse(nodeAttr.getNodeValue(), DateTimeFormatter.ofPattern("yyyy-mm-dd")));
					break;
				case "code":
					d.setCode(nodeAttr.getNodeValue());
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
