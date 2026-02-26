package in.managetime.timemanager.controller;

import in.managetime.timemanager.dto.UsefulTimeDTO;
import in.managetime.timemanager.service.UsefulTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usefultimes")
public class UsefulTimeController {

    private final UsefulTimeService usefulTimeService;

    @PostMapping
    public ResponseEntity<UsefulTimeDTO> addUsefulTime(@RequestBody UsefulTimeDTO dto) {
        UsefulTimeDTO saved = usefulTimeService.addUsefulTime(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<UsefulTimeDTO>> getUsefulTimes() {
        List<UsefulTimeDTO> usefultimes = usefulTimeService.getCurrentMonthUsefulTimesForCurrentUser();
        return ResponseEntity.ok(usefultimes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsefulTime(@PathVariable Long id) {
        usefulTimeService.deleteUsefulTime(id);
        return ResponseEntity.noContent().build();
    }
}
