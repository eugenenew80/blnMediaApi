package kz.kegoc.bln.producer.file.reader.impl;

import kz.kegoc.bln.entity.common.InputMethod;
import kz.kegoc.bln.entity.common.ReceivingMethod;
import kz.kegoc.bln.entity.common.SourceSystem;
import kz.kegoc.bln.entity.data.MeasDataRaw;
import kz.kegoc.bln.producer.file.reader.FileMeteringDataReader;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.ejb.cdi.annotation.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Stateless
@XML
public class XmlMeasDataReaderImpl implements FileMeteringDataReader<MeasDataRaw> {
	private static final Logger logger = LoggerFactory.getLogger(XmlMeasDataReaderImpl.class);

	@Inject
	public XmlMeasDataReaderImpl(MeteringDataQueue<MeasDataRaw> service) {
		this.queue = service;
	}

	public void read(Path path)  {
		logger.info("XmlMeasDataReaderImpl.read started");
		logger.info("file: " + path.toString());

		logger.debug("Reading file content started");
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(path.toFile());
		}
		catch (Exception e) {
			logger.error("XmlMeasDataReaderImpl.read failed: " + e.getMessage());
			return;
		}
		logger.debug("Reading file content competed");

		logger.debug("Parsing file content started");
		List<MeasDataRaw> list = new ArrayList<>();
		for (int i=0; i<doc.getDocumentElement().getChildNodes().getLength(); i++) {
			Node nodeRow = doc.getDocumentElement().getChildNodes().item(i);
			if (nodeRow.getNodeName().equals("row")) 
				list.add(convert(nodeRow));
		}
		logger.debug("Parsing file content completed, count of rows: " + list.size());

		logger.debug("Adding data to queue");
		queue.addAll(list);
		logger.info("XmlMeasDataReaderImpl.read completed");
	}
	
	
	private MeasDataRaw convert(Node node) {
		MeasDataRaw d = new MeasDataRaw();

		for (int i=0; i< node.getAttributes().getLength(); i++) {
			Node nodeAttr = node.getAttributes().item(i);
			switch (nodeAttr.getNodeName()) {
				case "meteringDate":
					d.setMeasDate(LocalDateTime.parse(nodeAttr.getNodeValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")));
					break;
				case "code":
					d.setSourceMeteringPointCode(nodeAttr.getNodeValue());
					break;
				case "paramCode":
					d.setSourceParamCode(nodeAttr.getNodeValue());
					break;
				case "unitCode":
					d.setSourceUnitCode(nodeAttr.getNodeValue());
					break;
				case "val":
					d.setVal(Double.parseDouble(nodeAttr.getNodeValue()));
					break;					
			}
		}
		d.setSourceSystemCode(SourceSystem.NOT_SET);
		d.setInputMethod(InputMethod.FILE);
		d.setReceivingMethod(ReceivingMethod.NOT_SET);
		
		return d;
	}

	private MeteringDataQueue<MeasDataRaw> queue;
}
