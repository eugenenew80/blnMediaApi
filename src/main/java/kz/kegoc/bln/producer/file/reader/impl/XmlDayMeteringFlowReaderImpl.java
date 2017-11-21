package kz.kegoc.bln.producer.file.reader.impl;

import kz.kegoc.bln.entity.common.DataSource;
import kz.kegoc.bln.entity.common.DataStatus;
import kz.kegoc.bln.entity.data.DayMeteringFlow;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Stateless
@XML
public class XmlDayMeteringFlowReaderImpl implements FileMeteringDataReader<DayMeteringFlow> {
    private static final Logger logger = LoggerFactory.getLogger(XmlDayMeteringFlowReaderImpl.class);

    @Inject
    public XmlDayMeteringFlowReaderImpl(MeteringDataQueue<DayMeteringFlow> service) {
        this.queue =service;
    }

    public void read(Path path) {
        logger.info("XmlDayMeteringFlowReaderImpl.read started");
        logger.info("file: " + path.toString());

        logger.debug("Reading file content started");
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(path.toFile());
        }
        catch (Exception e) {
            logger.error("XmlDayMeteringFlowReaderImpl.read failed: " + e.getMessage());
            return;
        }
        logger.debug("Reading file content competed");

        logger.debug("Parsing file content started");
        List<DayMeteringFlow> list = new ArrayList<>();
        for (int i=0; i<doc.getDocumentElement().getChildNodes().getLength(); i++) {
            Node nodeRow = doc.getDocumentElement().getChildNodes().item(i);
            if (nodeRow.getNodeName().equals("row"))
                list.add(convert(nodeRow));
        }
        logger.debug("Parsing file content completed, count of rows: " + list.size());

        logger.debug("Adding data to queue");
        queue.addAll(list);
        logger.info("XmlDayMeteringFlowReaderImpl.read completed");
    }


    private DayMeteringFlow convert(Node node)   {
        DayMeteringFlow d = new DayMeteringFlow();

        for (int i=0; i< node.getAttributes().getLength(); i++) {
            Node nodeAttr = node.getAttributes().item(i);
            switch (nodeAttr.getNodeName()) {
                case "meteringDate":
                    d.setMeteringDate(LocalDate.parse(nodeAttr.getNodeValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
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
        d.setStatus(DataStatus.RAW);
        d.setDataSource(DataSource.XML);

        return d;
    }

    private MeteringDataQueue<DayMeteringFlow> queue;
}
