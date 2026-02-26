package in.managetime.timemanager.service;

import in.managetime.timemanager.dto.UsefulTimeDTO;
import in.managetime.timemanager.dto.UnproductiveTimeDTO;
import in.managetime.timemanager.dto.RecentTransactionDTO;
import in.managetime.timemanager.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UnproductiveTimeService unproductivetimeService;
    private final UsefulTimeService usefultimeService;
    private final ProfileService profileService;

    public Map<String, Object> getDashboardData() {
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> returnValue = new LinkedHashMap<>();
        List<UnproductiveTimeDTO> latestUnproductiveTimes = unproductivetimeService.getLatest5UnproductiveTimesForCurrentUser();
        List<UsefulTimeDTO> latestUsefulTimes = usefultimeService.getLatest5UsefulTimesForCurrentUser();
        List<RecentTransactionDTO> recentTransactions = concat(latestUnproductiveTimes.stream().map(unproductivetime ->
                        RecentTransactionDTO.builder()
                                .id(unproductivetime.getId())
                                .profileId(profile.getId())
                                .icon(unproductivetime.getIcon())
                                .name(unproductivetime.getName())
                                .time(unproductivetime.getTime())
                                .date(unproductivetime.getDate())
                                .createdAt(unproductivetime.getCreatedAt())
                                .updatedAt(unproductivetime.getUpdatedAt())
                                .type("Unproductive Time")
                                .build()),
                latestUsefulTimes.stream().map(usefultime ->
                        RecentTransactionDTO.builder()
                                .id(usefultime.getId())
                                .profileId(profile.getId())
                                .icon(usefultime.getIcon())
                                .name(usefultime.getName())
                                .time(usefultime.getTime())
                                .date(usefultime.getDate())
                                .createdAt(usefultime.getCreatedAt())
                                .updatedAt(usefultime.getUpdatedAt())
                                .type("Useful Time")
                                .build()))
                .sorted((a, b) -> {
                    int cmp = b.getDate().compareTo(a.getDate());
                    if (cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;
                }).collect(Collectors.toList());
        returnValue.put("totalTime",
                unproductivetimeService.getTotalUnproductiveTimeForCurrentUser()
                        .add(usefultimeService.getTotalUsefulTimeForCurrentUser()));
        returnValue.put("totalUnproductiveTime", unproductivetimeService.getTotalUnproductiveTimeForCurrentUser());
        returnValue.put("totalUsefulTime", usefultimeService.getTotalUsefulTimeForCurrentUser());
        returnValue.put("recent5UsefulTimes", latestUsefulTimes);
        returnValue.put("recent5UnproductiveTimes", latestUnproductiveTimes);
        returnValue.put("recentTransactions", recentTransactions);
        return returnValue;
    }

}
