package in.managetime.timemanager.service;

import in.managetime.timemanager.dto.UsefulTimeDTO;
import in.managetime.timemanager.dto.UnproductiveTimeDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ExcelService {

    public void writeUnproductiveTimesToExcel(OutputStream os, List<UnproductiveTimeDTO> unproductivetimes) throws IOException {
        try(Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("UnproductiveTimes");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("S.No");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Category");
            header.createCell(3).setCellValue("Time");
            header.createCell(4).setCellValue("Date");
            IntStream.range(0, unproductivetimes.size())
                    .forEach(i -> {
                        UnproductiveTimeDTO unproductivetime = unproductivetimes.get(i);
                        Row row = sheet.createRow(i + 1);
                        row.createCell(0).setCellValue(i + 1);
                        row.createCell(1).setCellValue(unproductivetime.getName() != null ? unproductivetime.getName(): "N/A");
                        row.createCell(2).setCellValue(unproductivetime.getCategoryId() != null ? unproductivetime.getCategoryName(): "N/A");
                        row.createCell(3).setCellValue(unproductivetime.getTime() != null ? unproductivetime.getTime().doubleValue(): 0);
                        row.createCell(4).setCellValue(unproductivetime.getDate() != null ? unproductivetime.getDate().toString(): "N/A");
                    });
            workbook.write(os);
        }
    }

    public void writeUsefulTimesToExcel(OutputStream os, List<UsefulTimeDTO> usefultimes) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("UsefulTimes");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("S.No");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Category");
            header.createCell(3).setCellValue("Amount");
            header.createCell(4).setCellValue("Date");
            IntStream.range(0, usefultimes.size())
                    .forEach(i -> {
                        UsefulTimeDTO usefultime = usefultimes.get(i);
                        Row row = sheet.createRow(i + 1);
                        row.createCell(0).setCellValue(i + 1); // Serial number
                        row.createCell(1).setCellValue(usefultime.getName() != null ? usefultime.getName() : "");
                        row.createCell(2)
                                .setCellValue(usefultime.getCategoryId() != null ? usefultime.getCategoryName() : "N/A");
                        row.createCell(3)
                                .setCellValue(usefultime.getTime() != null ? usefultime.getTime().doubleValue() : 0);
                        row.createCell(4).setCellValue(usefultime.getDate() != null ? usefultime.getDate().toString() : "");
                    });
            workbook.write(os);
        }
    }
}
