package kz.kegoc.bln.producer.file.month;

import kz.kegoc.bln.entity.media.raw.MonthMeteringDataRaw;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.producer.file.FileMeteringDataRawReader;
import kz.kegoc.bln.queue.MeteringDataQueueService;
import kz.kegoc.bln.annotation.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Stateless
@XML
public class XmlMonthMeteringDataRawReader implements FileMeteringDataRawReader<MonthMeteringDataRaw> {

	@Inject
	public XmlMonthMeteringDataRawReader(MeteringDataQueueService<MonthMeteringDataRaw> service) {
		this.service=service;
	}

	public void loadFromFile(Path path) throws Exception {
		Document doc = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder()
						.parse(path.toFile());
		
		List<MonthMeteringDataRaw> list = new ArrayList<>();
		for (int i=0; i<doc.getDocumentElement().getChildNodes().getLength(); i++) {
			Node nodeRow = doc.getDocumentElement().getChildNodes().item(i);
			if (nodeRow.getNodeName().equals("row")) 
				list.add(convert(nodeRow));
		}

		service.addAll(list);
	}
	
	
	private MonthMeteringDataRaw convert(Node node) throws Exception  {
		MonthMeteringDataRaw d = new MonthMeteringDataRaw();

		for (int i=0; i< node.getAttributes().getLength(); i++) {
			Node nodeAttr = node.getAttributes().item(i);
			switch (nodeAttr.getNodeName()) {
				case "year":
					d.setYear(Short.parseShort(nodeAttr.getNodeValue()));
					break;
				case "meteringMont":
					d.setMonth(Short.parseShort(nodeAttr.getNodeValue()));
					break;
				case "code":
					d.setExternalCode(nodeAttr.getNodeValue());
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
		d.setWayEntering(WayEntering.XML);
		d.setStatus(DataStatus.RAW);
		d.setDataSourceCode("MANUAL");
		
		return d;
	}

	private MeteringDataQueueService<MonthMeteringDataRaw> service;
}
