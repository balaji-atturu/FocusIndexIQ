package in.managetime.timemanager.service;

import in.managetime.timemanager.dto.UsefulTimeDTO;
import in.managetime.timemanager.entity.CategoryEntity;
import in.managetime.timemanager.entity.UsefulTimeEntity;
import in.managetime.timemanager.entity.ProfileEntity;
import in.managetime.timemanager.repository.CategoryRepository;
import in.managetime.timemanager.repository.UsefulTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsefulTimeService {

    private final CategoryRepository categoryRepository;
    private final UsefulTimeRepository usefultimeRepository;
    private final ProfileService profileService;

    public UsefulTimeDTO addUsefulTime(UsefulTimeDTO dto) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        UsefulTimeEntity newUsefulTime = toEntity(dto, profile, category);
        newUsefulTime = usefultimeRepository.save(newUsefulTime);
        return toDTO(newUsefulTime);
    }

    public List<UsefulTimeDTO> getCurrentMonthUsefulTimesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<UsefulTimeEntity> list = usefultimeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return list.stream().map(this::toDTO).toList();
    }

    public void deleteUsefulTime(Long usefultimeId) {
        ProfileEntity profile = profileService.getCurrentProfile();
        UsefulTimeEntity entity = usefultimeRepository.findById(usefultimeId)
                .orElseThrow(() -> new RuntimeException("Useful Time  not found"));
        if (!entity.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorized to delete this details");
        }
        usefultimeRepository.delete(entity);
    }

    public List<UsefulTimeDTO> getLatest5UsefulTimesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<UsefulTimeEntity> list = usefultimeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }

    public BigDecimal getTotalUsefulTimeForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = usefultimeRepository.findTotalUsefulTimeByProfileId(profile.getId());
        return total != null ? total: BigDecimal.ZERO;
    }

    public List<UsefulTimeDTO> filterUsefulTimes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<UsefulTimeEntity> list = usefultimeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
        return list.stream().map(this::toDTO).toList();
    }

    public List<UsefulTimeDTO> getUsefulTimesForUserOnDate(Long profileId, LocalDate date) {
        List<UsefulTimeEntity> list = usefultimeRepository.findByProfileIdAndDate(profileId, date);
        return list.stream().map(this::toDTO).toList();
    }

    private UsefulTimeEntity toEntity(UsefulTimeDTO dto, ProfileEntity profile, CategoryEntity category) {
        return UsefulTimeEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .time(dto.getTime())
                .date(dto.getDate())
                .profile(profile)
                .category(category)
                .build();
    }

    private UsefulTimeDTO toDTO(UsefulTimeEntity entity) {
        return UsefulTimeDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId(): null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName(): "N/A")
                .time(entity.getTime())
                .date(entity.getDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
