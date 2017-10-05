package kz.kegoc.bln.service.producer.daily;

import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.parsers.DocumentBuilderFactory;

import kz.kegoc.bln.service.producer.common.AbstractFileMeteringDataProducer;
import org.w3c.dom.*;

import kz.kegoc.bln.entity.media.DailyMeteringData;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.service.producer.common.MeteringDataProducer;


@Singleton
@Startup
public class XmlDailyMeteringDataProducer  extends AbstractFileMeteringDataProducer<DailyMeteringData> implements MeteringDataProducer {
    
    public XmlDailyMeteringDataProducer() {
		super("xml");
	}

    
	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		super.execute();
    }

	
	protected List<DailyMeteringData> loadFromFile(Path path) throws Exception {
		Document doc = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder()
						.parse(path.toFile());
		
		List<DailyMeteringData> list = new ArrayList<>();
		for (int i=0; i<doc.getDocumentElement().getChildNodes().getLength(); i++) {
			Node nodeRow = doc.getDocumentElement().getChildNodes().item(i);
			if (nodeRow.getNodeName().equals("row")) 
				list.add(convert(nodeRow));
		}
		
		return list;
	}
	
	
	private DailyMeteringData convert(Node node) throws Exception  {
		DailyMeteringData d = new DailyMeteringData();

		for (int i=0; i< node.getAttributes().getLength(); i++) {
			Node nodeAttr = node.getAttributes().item(i);
			switch (nodeAttr.getNodeName()) {
				case "meteringDate":
					SimpleDateFormat sd = new SimpleDateFormat("yyyy-mm-dd");
					d.setMeteringDate(sd.parse(nodeAttr.getNodeValue()));
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
