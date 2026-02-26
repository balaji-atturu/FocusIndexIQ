package in.managetime.timemanager.controller;

import in.managetime.timemanager.entity.ProfileEntity;
import in.managetime.timemanager.service.*;
import in.managetime.timemanager.service.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final ExcelService excelService;
    private final UnproductiveTimeService unproductiveTimeService;
    private final UsefulTimeService usefulTimeService;
    private final EmailService emailService;
    private final ProfileService profileService;

    @GetMapping("/unproductivetime-excel")
    public ResponseEntity<Void> emailUnproductiveTimeExcel() throws IOException, MessagingException {
        ProfileEntity profile = profileService.getCurrentProfile();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        excelService.writeUnproductiveTimesToExcel(baos, unproductiveTimeService.getCurrentMonthUnproductiveTimesForCurrentUser());
        emailService.sendEmailWithAttachment(profile.getEmail(),
                "Your UnproductiveTime Excel Report",
                "Please find attached your UnproductiveTime report",
                baos.toByteArray(),
                "Unproductive_timespent.xlsx");
        return ResponseEntity.ok(null);
    }

    @GetMapping("/usefultime-excel")
    public ResponseEntity<Void> emailUsefulTimeExcel() throws IOException, MessagingException {
        ProfileEntity profile = profileService.getCurrentProfile();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        excelService.writeUsefulTimesToExcel(baos, usefulTimeService.getCurrentMonthUsefulTimesForCurrentUser());
        emailService.sendEmailWithAttachment(
                profile.getEmail(),
                "Your Useful Time Spent Excel Report",
                "Please find attached your Useful Time Spent report.",
                baos.toByteArray(),
                "Useful_timespent.xlsx");
        return ResponseEntity.ok(null);
    }
}
