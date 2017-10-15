package kz.kegoc.bln.producer.file.reader.impl.day;

import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.DayMeteringDataRaw;
import kz.kegoc.bln.producer.file.reader.FileMeteringDataRawReader;
import kz.kegoc.bln.queue.MeteringDataQueueService;
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
public class XmlDayMeteringDataRawReader implements FileMeteringDataRawReader<DayMeteringDataRaw> {

    @Inject
    public XmlDayMeteringDataRawReader(MeteringDataQueueService<DayMeteringDataRaw> service) {
        this.service=service;
    }

    public void loadFromFile(Path path) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(path.toFile());

        List<DayMeteringDataRaw> list = new ArrayList<>();
        for (int i=0; i<doc.getDocumentElement().getChildNodes().getLength(); i++) {
            Node nodeRow = doc.getDocumentElement().getChildNodes().item(i);
            if (nodeRow.getNodeName().equals("row"))
                list.add(convert(nodeRow));
        }

        service.addAll(list);
    }


    private DayMeteringDataRaw convert(Node node) throws Exception  {
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

    private MeteringDataQueueService<DayMeteringDataRaw> service;
}
