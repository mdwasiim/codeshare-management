package com.codeshare.airline.master.common.controller;

import com.codeshare.airline.master.common.dto.CommonReferenceOptionDTO;
import com.codeshare.airline.master.common.entities.CommonReferenceOption;
import com.codeshare.airline.master.common.repository.CommonReferenceOptionRepository;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/common/reference-options")
@RequiredArgsConstructor
public class CommonReferenceOptionController {

    private final CommonReferenceOptionRepository repository;

    @GetMapping
    @Transactional(readOnly = true)
    public List<CommonReferenceOptionDTO> getAll(@RequestParam Map<String, String> filters) {
        return repository.findAll().stream()
                .filter(option -> matches(option, filters))
                .sorted(Comparator
                        .comparing(CommonReferenceOption::getCategoryCode, Comparator.nullsLast(String::compareTo))
                        .thenComparing(option -> Objects.requireNonNullElse(option.getDisplayOrder(), Integer.MAX_VALUE))
                        .thenComparing(CommonReferenceOption::getOptionLabel, Comparator.nullsLast(String::compareTo)))
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/categories")
    @Transactional(readOnly = true)
    public List<String> getCategories() {
        return repository.findAll().stream()
                .map(CommonReferenceOption::getCategoryCode)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public CommonReferenceOptionDTO getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Common reference option not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public CommonReferenceOptionDTO create(@RequestBody CommonReferenceOptionDTO dto) {
        CommonReferenceOption option = new CommonReferenceOption();
        apply(option, dto);
        return toDto(repository.save(option));
    }

    @PutMapping("/{id}")
    @Transactional
    public CommonReferenceOptionDTO update(@PathVariable Long id, @RequestBody CommonReferenceOptionDTO dto) {
        CommonReferenceOption option = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Common reference option not found"));
        apply(option, dto);
        return toDto(repository.save(option));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/{categoryCode}/options")
    @Transactional(readOnly = true)
    public List<CommonReferenceOptionDTO> getOptions(@PathVariable String categoryCode) {
        return repository.findByCategoryCodeAndRecordStatusOrderByDisplayOrderAscOptionLabelAsc(
                        normalize(categoryCode),
                        RecordStatus.ACTIVE
                ).stream()
                .map(this::toDto)
                .toList();
    }

    private CommonReferenceOptionDTO toDto(CommonReferenceOption option) {
        return new CommonReferenceOptionDTO(
                option.getId(),
                option.getCategoryCode(),
                option.getOptionCode(),
                option.getOptionLabel(),
                option.getDescription(),
                option.getDisplayOrder(),
                option.getRecordStatus(),
                option.isActive()
        );
    }

    private void apply(CommonReferenceOption option, CommonReferenceOptionDTO dto) {
        option.setCategoryCode(normalize(dto.categoryCode()));
        option.setOptionCode(normalize(dto.value()));
        option.setOptionLabel(trim(dto.label()));
        option.setDescription(trim(dto.description()));
        option.setDisplayOrder(dto.displayOrder());
        option.setRecordStatus(dto.recordStatus() == null ? RecordStatus.ACTIVE : dto.recordStatus());
        option.setActive(dto.active() == null || dto.active());
    }

    private boolean matches(CommonReferenceOption option, Map<String, String> filters) {
        if (filters == null || filters.isEmpty()) {
            return true;
        }

        return filters.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null && !entry.getValue().isBlank())
                .allMatch(entry -> matches(option, entry.getKey(), entry.getValue()));
    }

    private boolean matches(CommonReferenceOption option, String key, String expected) {
        String value = switch (key) {
            case "id" -> option.getId() == null ? null : String.valueOf(option.getId());
            case "categoryCode" -> option.getCategoryCode();
            case "value", "optionCode" -> option.getOptionCode();
            case "label", "optionLabel" -> option.getOptionLabel();
            case "description" -> option.getDescription();
            case "displayOrder" -> option.getDisplayOrder() == null ? null : String.valueOf(option.getDisplayOrder());
            case "recordStatus" -> option.getRecordStatus() == null ? null : option.getRecordStatus().name();
            case "active" -> String.valueOf(option.isActive());
            default -> null;
        };

        return value == null || value.trim().equalsIgnoreCase(expected.trim());
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
    }

    private String trim(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
