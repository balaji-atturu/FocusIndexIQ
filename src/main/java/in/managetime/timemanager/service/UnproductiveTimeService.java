package in.managetime.timemanager.service;

import in.managetime.timemanager.dto.UnproductiveTimeDTO;
import in.managetime.timemanager.dto.UnproductiveTimeDTO;
import in.managetime.timemanager.entity.CategoryEntity;
import in.managetime.timemanager.entity.UnproductiveTimeEntity;
import in.managetime.timemanager.entity.UnproductiveTimeEntity;
import in.managetime.timemanager.entity.ProfileEntity;
import in.managetime.timemanager.repository.CategoryRepository;
import in.managetime.timemanager.repository.UnproductiveTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnproductiveTimeService {
    private final CategoryRepository categoryRepository;
    private final UnproductiveTimeRepository unproductivetimeRepository;
    private final ProfileService profileService;

    public UnproductiveTimeDTO addUnproductiveTime(UnproductiveTimeDTO dto) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        UnproductiveTimeEntity newUnproductiveTime = toEntity(dto, profile, category);
        newUnproductiveTime = unproductivetimeRepository.save(newUnproductiveTime);
        return toDTO(newUnproductiveTime);
    }

    public List<UnproductiveTimeDTO> getCurrentMonthUnproductiveTimesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<UnproductiveTimeEntity> list = unproductivetimeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return list.stream().map(this::toDTO).toList();
    }

    public void deleteUnproductiveTime(Long unproductivetimeId) {
        ProfileEntity profile = profileService.getCurrentProfile();
        UnproductiveTimeEntity entity = unproductivetimeRepository.findById(unproductivetimeId)
                .orElseThrow(() -> new RuntimeException("Unproductive Time not found"));
        if (!entity.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorized to delete this unproductivetime");
        }
        unproductivetimeRepository.delete(entity);
    }

    public List<UnproductiveTimeDTO> getLatest5UnproductiveTimesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<UnproductiveTimeEntity> list = unproductivetimeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }

    public BigDecimal getTotalUnproductiveTimeForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = unproductivetimeRepository.findTotalUnproductiveTimeByProfileId(profile.getId());
        return total != null ? total: BigDecimal.ZERO;
    }

    public List<UnproductiveTimeDTO> filterUnproductiveTimes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<UnproductiveTimeEntity> list = unproductivetimeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
        return list.stream().map(this::toDTO).toList();
    }

    private UnproductiveTimeEntity toEntity(UnproductiveTimeDTO dto, ProfileEntity profile, CategoryEntity category) {
        return UnproductiveTimeEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .time(dto.getTime())
                .date(dto.getDate())
                .profile(profile)
                .category(category)
                .build();
    }

    private UnproductiveTimeDTO toDTO(UnproductiveTimeEntity entity) {
        return UnproductiveTimeDTO.builder()
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
