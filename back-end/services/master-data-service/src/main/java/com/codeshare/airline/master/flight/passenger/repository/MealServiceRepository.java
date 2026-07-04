package com.codeshare.airline.master.flight.passenger.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.passenger.entities.MealService;

import java.util.UUID;

public interface MealServiceRepository extends CSMDataBaseRepository<MealService, UUID> {
    boolean existsByMealCode(String mealCode);
}
