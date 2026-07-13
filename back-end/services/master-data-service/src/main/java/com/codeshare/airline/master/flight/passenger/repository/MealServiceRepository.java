package com.codeshare.airline.master.flight.passenger.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.passenger.entities.MealService;


public interface MealServiceRepository extends CSMDataBaseRepository<MealService, Long> {
    boolean existsByMealCode(String mealCode);
}
