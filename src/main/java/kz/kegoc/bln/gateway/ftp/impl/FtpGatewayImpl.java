package kz.kegoc.bln.gateway.ftp.impl;

import kz.kegoc.bln.entity.data.PowerConsumption;
import kz.kegoc.bln.gateway.ftp.FtpGateway;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import javax.ejb.Singleton;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class FtpGatewayImpl implements FtpGateway {
    private List<PowerConsumption> pcList;
    private List<String> mpCodesList;

    public FtpGateway pcList(List<PowerConsumption> pcList) {
        this.pcList = pcList;
        this.mpCodesList = pcList.stream()
            .map(d -> d.getMeteringPoint().getExternalCode())
            .distinct()
            .collect(Collectors.toList());
        return this;
    }

    public void send() {
        Workbook book = builWorkBook();

        try {
            book.write(new FileOutputStream("qqq.xlsx"));
            book.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Workbook builWorkBook() {
        Workbook book = new HSSFWorkbook();

        CellStyle headerStyle = book.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle codeStyle = book.createCellStyle();
        codeStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        codeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Sheet sheet = book.createSheet("Лист 1");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Тип шаблона");
        cell = row.createCell(1);
        cell.setCellValue("Ручной импорт данных");

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("Субъект");
        cell = row.createCell(1);
        cell.setCellValue("СБРЭ");

        int rowNum = 1;
        for (String pointCode: mpCodesList) {
            rowNum++;
            sheet.createRow(rowNum);

            rowNum++;
            row = sheet.createRow(rowNum);
            cell = row.createCell(0);
            cell.setCellValue("Код ТУ");

            cell = row.createCell(1);
            cell.setCellStyle(codeStyle);
            cell.setCellValue(pointCode);

            cell = row.createCell(2);
            cell.setCellValue("Дискретность");

            cell = row.createCell(3);
            cell.setCellValue(3600);

            rowNum++;
            row = sheet.createRow(rowNum);
            cell = row.createCell(0);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("timestamp");

            cell = row.createCell(1);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(709);

            cell = row.createCell(2);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(710);

            cell = row.createCell(3);
            cell.setCellStyle(headerStyle);

            cell = row.createCell(4);
            cell.setCellStyle(headerStyle);

            rowNum++;
            row = sheet.createRow(rowNum);
            cell = row.createCell(0);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("Время");

            cell = row.createCell(1);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("A+ энергия за час");

            cell = row.createCell(2);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("A- энергия за час");

            cell = row.createCell(3);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("Forced");

            cell = row.createCell(4);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("Status");

            List<PowerConsumption> list = pcList.stream()
                .filter(d -> d.getMeteringPoint().getExternalCode().equals(pointCode))
                .collect(Collectors.toList());

            LocalDateTime curDate = LocalDate.now().atStartOfDay();
            for (int i=0; i<=23; i++) {
                rowNum++;
                row = sheet.createRow(rowNum);

                cell = row.createCell(0);
                DataFormat format = book.createDataFormat();
                CellStyle dateStyle = book.createCellStyle();
                dateStyle.setDataFormat(format.getFormat("DD.MM.YYYY HH:MM:SS"));
                cell.setCellStyle(dateStyle);
                cell.setCellValue(toDate(curDate.plusHours(i)));

                cell = row.createCell(1);
                cell.setCellValue(123);

                cell = row.createCell(2);
                cell.setCellValue(321);

                cell = row.createCell(3);
                cell.setCellValue(1);

                cell = row.createCell(4);
                cell.setCellValue(1);
            }

            rowNum++;
            sheet.createRow(rowNum);
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);

        return book;
    }


    private Date toDate(LocalDateTime localDate) {
        return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
    }
}
