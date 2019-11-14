package code.service;

import code.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FoodService {

    Food findById(Long id);

    List<Food> findRandomFoods(int num);

    List<Food> findFoodsByFoodType(String foodType, int num);

    Page<Food> findUnlimitedFoodsByType(String type, Pageable pageable);

    Page<Food> findFoodsByFoodName(String name, Pageable pageable);

    Page<Food> findAll(Pageable pageable);

    void deleteFoodById(Long id);

    void save(Food food);
}
