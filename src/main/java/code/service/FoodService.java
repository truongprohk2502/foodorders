package code.service;

import code.model.Food;

import java.util.List;

public interface FoodService {

    public Food findById(Long id);

    public List<Food> findRandomFoods();

    public List<Food> findFoodsByFoodType(String foodType);
}
