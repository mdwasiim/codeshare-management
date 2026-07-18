package com.codeshare.airline.master.common.base;


import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
public abstract class BaseServiceImpl<E extends CSMDataAbstractEntity, D, ID>  implements BaseService<D, ID> {

    private static final Set<String> RESERVED_FILTER_PARAMS = Set.of(
            "page",
            "size",
            "sort",
            "direction",
            "_",
            "_dc"
    );

    protected final CSMDataBaseRepository<E, ID> repository;
    protected final CSMGenericMapper<E, D> mapper;

    @Override
    public D create(D dto) {
        E entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public D update(ID id, D dto) {
        E existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        mapper.updateEntityFromDto(dto, existing);
        return mapper.toDTO(repository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public D getById(ID id) {
        return mapper.toDTO(
                repository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Entity not found"))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<D> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<D> searchExact(Map<String, String> filters) {
        Map<String, String> effectiveFilters = effectiveFilters(filters);

        if (effectiveFilters.isEmpty()) {
            return getAll();
        }

        Specification<E> specification = (root, query, cb) -> {
            List<Predicate> predicates = effectiveFilters.entrySet().stream()
                    .map(entry -> findField(root.getJavaType(), entry.getKey())
                            .map(field -> exactPredicate(root.get(field.getName()), field, entry.getValue(), cb))
                            .orElse(null))
                    .filter(predicate -> predicate != null)
                    .toList();

            if (predicates.isEmpty()) {
                return cb.conjunction();
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };

        return mapper.toDTOList(repository.findAll(specification)).stream()
                .filter(dto -> matchesDtoFilters(dto, effectiveFilters))
                .toList();
    }


    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }

    private Map<String, String> effectiveFilters(Map<String, String> filters) {
        if (filters == null || filters.isEmpty()) {
            return Map.of();
        }

        return filters.entrySet().stream()
                .filter(entry -> isEffectiveFilter(entry.getKey(), entry.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().trim(),
                        (first, second) -> second
                ));
    }

    private boolean isEffectiveFilter(String key, String value) {
        return key != null
                && !key.isBlank()
                && !RESERVED_FILTER_PARAMS.contains(key)
                && value != null
                && !value.isBlank();
    }

    private Optional<Field> findField(Class<?> type, String fieldName) {
        Class<?> current = type;

        while (current != null && current != Object.class) {
            try {
                return Optional.of(current.getDeclaredField(fieldName));
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }

        return Optional.empty();
    }

    private boolean matchesDtoFilters(D dto, Map<String, String> filters) {
        return filters.entrySet().stream()
                .allMatch(entry -> findField(dto.getClass(), entry.getKey())
                        .map(field -> fieldMatches(field, dto, entry.getValue()))
                        .orElse(true));
    }

    private boolean fieldMatches(Field field, D dto, String expected) {
        try {
            field.setAccessible(true);
            Object value = field.get(dto);

            if (value == null) {
                return false;
            }

            return String.valueOf(value).trim().equalsIgnoreCase(expected.trim());
        } catch (IllegalAccessException ignored) {
            return true;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Predicate exactPredicate(Path<?> path, Field field, String rawValue, jakarta.persistence.criteria.CriteriaBuilder cb) {
        String value = rawValue.trim();
        Class<?> type = boxed(field.getType());

        if (String.class.equals(type)) {
            return cb.equal(
                    cb.lower(cb.trim(path.as(String.class))),
                    value.toLowerCase(Locale.ROOT)
            );
        }

        if (Boolean.class.equals(type)) {
            Boolean parsed = parseBoolean(value);
            return parsed == null ? cb.disjunction() : cb.equal(path, parsed);
        }

        if (Number.class.isAssignableFrom(type)) {
            Number parsed = parseNumber(type, value);
            return parsed == null ? cb.disjunction() : cb.equal(path, parsed);
        }

        if (Enum.class.isAssignableFrom(type)) {
            try {
                return cb.equal(path, Enum.valueOf((Class<Enum>) type.asSubclass(Enum.class), value.toUpperCase(Locale.ROOT)));
            } catch (IllegalArgumentException ignored) {
                return cb.disjunction();
            }
        }

        if (LocalDate.class.equals(type)) {
            try {
                return cb.equal(path, LocalDate.parse(value));
            } catch (RuntimeException ignored) {
                return cb.disjunction();
            }
        }

        if (CSMDataAbstractEntity.class.isAssignableFrom(type)) {
            Long id = parseLong(value);
            return id == null ? cb.disjunction() : cb.equal(path.get("id"), id);
        }

        return null;
    }

    private Class<?> boxed(Class<?> type) {
        if (!type.isPrimitive()) {
            return type;
        }

        if (boolean.class.equals(type)) {
            return Boolean.class;
        }
        if (int.class.equals(type)) {
            return Integer.class;
        }
        if (long.class.equals(type)) {
            return Long.class;
        }
        if (double.class.equals(type)) {
            return Double.class;
        }
        if (float.class.equals(type)) {
            return Float.class;
        }
        if (short.class.equals(type)) {
            return Short.class;
        }
        if (byte.class.equals(type)) {
            return Byte.class;
        }

        return type;
    }

    private Boolean parseBoolean(String value) {
        return switch (value.toLowerCase(Locale.ROOT)) {
            case "true", "yes", "y", "1" -> Boolean.TRUE;
            case "false", "no", "n", "0" -> Boolean.FALSE;
            default -> null;
        };
    }

    private Number parseNumber(Class<?> type, String value) {
        try {
            if (Integer.class.equals(type)) {
                return Integer.valueOf(value);
            }
            if (Long.class.equals(type)) {
                return Long.valueOf(value);
            }
            if (Double.class.equals(type)) {
                return Double.valueOf(value);
            }
            if (Float.class.equals(type)) {
                return Float.valueOf(value);
            }
            if (Short.class.equals(type)) {
                return Short.valueOf(value);
            }
            if (Byte.class.equals(type)) {
                return Byte.valueOf(value);
            }
            if (BigDecimal.class.equals(type)) {
                return new BigDecimal(value);
            }
            if (BigInteger.class.equals(type)) {
                return new BigInteger(value);
            }
        } catch (NumberFormatException ignored) {
            return null;
        }

        return null;
    }

    private Long parseLong(String value) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
