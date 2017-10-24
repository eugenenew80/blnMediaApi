package kz.kegoc.bln.producer.file.reader.impl.day;

import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.day.DayMeteringDataRaw;
import kz.kegoc.bln.producer.file.reader.FileMeteringReader;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.ejb.annotation.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Stateless
@XML
public class XmlDayMeteringDataRawReaderImpl implements FileMeteringReader<DayMeteringDataRaw> {

    @Inject
    public XmlDayMeteringDataRawReaderImpl(MeteringDataQueue<DayMeteringDataRaw> service) {
        this.service=service;
    }

    public void read(Path path) {
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(path.toFile());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (doc==null) return;

        List<DayMeteringDataRaw> list = new ArrayList<>();
        for (int i=0; i<doc.getDocumentElement().getChildNodes().getLength(); i++) {
            Node nodeRow = doc.getDocumentElement().getChildNodes().item(i);
            if (nodeRow.getNodeName().equals("row"))
                list.add(convert(nodeRow));
        }

        service.addAll(list);
    }


    private DayMeteringDataRaw convert(Node node)   {
        DayMeteringDataRaw d = new DayMeteringDataRaw();

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
        d.setWayEntering(WayEntering.XML);
        d.setStatus(DataStatus.RAW);
        d.setDataSourceCode("MANUAL");

        return d;
    }

    private MeteringDataQueue<DayMeteringDataRaw> service;
}
