package in.managetime.timemanager.controller;

import in.managetime.timemanager.dto.UsefulTimeDTO;
import in.managetime.timemanager.dto.FilterDTO;
import in.managetime.timemanager.dto.UnproductiveTimeDTO;
import in.managetime.timemanager.service.UsefulTimeService;
import in.managetime.timemanager.service.UnproductiveTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {

    private final UsefulTimeService usefulTimeService;
    private final UnproductiveTimeService unproductiveTimeService;

    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filter) {
        LocalDate startDate = filter.getStartDate() != null ? filter.getStartDate() : LocalDate.MIN;
        LocalDate endDate = filter.getEndDate() != null ? filter.getEndDate() : LocalDate.now();
        String keyword = filter.getKeyword() != null ? filter.getKeyword() : "";
        String sortField = filter.getSortField() != null ? filter.getSortField() : "date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filter.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);
        if ("Unproductive Time".equals(filter.getType())) {
            List<UnproductiveTimeDTO> incomes = unproductiveTimeService.filterUnproductiveTimes(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(incomes);
        } else if ("Useful Time".equalsIgnoreCase(filter.getType())) {
            List<UsefulTimeDTO> expenses = usefulTimeService.filterUsefulTimes(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(expenses);
        } else {
            return ResponseEntity.badRequest().body("Invalid type. Must be 'Useful Time' or 'Unproductive Time'");
        }
    }
}
