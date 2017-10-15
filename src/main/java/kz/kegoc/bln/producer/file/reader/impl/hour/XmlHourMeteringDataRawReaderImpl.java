package kz.kegoc.bln.producer.file.reader.impl.hour;

import kz.kegoc.bln.entity.media.HourMeteringDataRaw;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.producer.file.reader.FileMeteringDataReader;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.annotation.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Stateless
@XML
public class XmlHourMeteringDataRawReaderImpl implements FileMeteringDataReader<HourMeteringDataRaw> {

	@Inject
	public XmlHourMeteringDataRawReaderImpl(MeteringDataQueue<HourMeteringDataRaw> service) {
		this.service = service;
	}

	public void loadFromFile(Path path) throws Exception {
		Document doc = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder()
						.parse(path.toFile());
		
		List<HourMeteringDataRaw> list = new ArrayList<>();
		for (int i=0; i<doc.getDocumentElement().getChildNodes().getLength(); i++) {
			Node nodeRow = doc.getDocumentElement().getChildNodes().item(i);
			if (nodeRow.getNodeName().equals("row")) 
				list.add(convert(nodeRow));
		}

		service.addAll(list);
	}
	
	
	private HourMeteringDataRaw convert(Node node) throws Exception  {
		HourMeteringDataRaw d = new HourMeteringDataRaw();

		for (int i=0; i< node.getAttributes().getLength(); i++) {
			Node nodeAttr = node.getAttributes().item(i);
			switch (nodeAttr.getNodeName()) {
				case "meteringDate":
					d.setMeteringDate(LocalDate.parse(nodeAttr.getNodeValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
					break;
				case "hour":
					d.setHour(Integer.parseInt(nodeAttr.getNodeValue()));
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

	private MeteringDataQueue<HourMeteringDataRaw> service;
}
