package com.codeshare.airline.master.flight.passenger.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.passenger.entities.MealService;

import java.util.UUID;

public interface MealServiceRepository extends CSMDataBaseRepository<MealService, UUID> {
    boolean existsByMealCode(String mealCode);
}
