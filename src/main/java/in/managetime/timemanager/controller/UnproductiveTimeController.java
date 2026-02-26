package in.managetime.timemanager.controller;

import in.managetime.timemanager.dto.UnproductiveTimeDTO;
import in.managetime.timemanager.service.UnproductiveTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/unproductivetimes")
public class UnproductiveTimeController {

    private final UnproductiveTimeService unproductiveTimeService;

    @PostMapping
    public ResponseEntity<UnproductiveTimeDTO> addUnproductiveTime(@RequestBody UnproductiveTimeDTO dto) {
        UnproductiveTimeDTO saved = unproductiveTimeService.addUnproductiveTime(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<UnproductiveTimeDTO>> getUnproductiveTimes() {
        List<UnproductiveTimeDTO> unproductivetime = unproductiveTimeService.getCurrentMonthUnproductiveTimesForCurrentUser();
        return ResponseEntity.ok(unproductivetime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnproductiveTime(@PathVariable Long id) {
        unproductiveTimeService.deleteUnproductiveTime(id);
        return ResponseEntity.noContent().build();
    }
}
