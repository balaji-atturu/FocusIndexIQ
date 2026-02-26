package in.managetime.timemanager.controller;

import in.managetime.timemanager.service.ExcelService;
import in.managetime.timemanager.service.UsefulTimeService;
import in.managetime.timemanager.service.UnproductiveTimeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;
    private final UnproductiveTimeService unproductiveTimeService;
    private final UsefulTimeService usefulTimeService;

    @GetMapping("/download/unproductivetime")
    public void downloadUnproductiveTimeExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Unproductive_timespent.xlsx");
        excelService.writeUnproductiveTimesToExcel(response.getOutputStream(), unproductiveTimeService.getCurrentMonthUnproductiveTimesForCurrentUser());
    }

    @GetMapping("/download/usefultime")
    public void downloadUsefulTimeExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Useful_timespent.xlsx");
        excelService.writeUsefulTimesToExcel(response.getOutputStream(), usefulTimeService.getCurrentMonthUsefulTimesForCurrentUser());
    }
}
