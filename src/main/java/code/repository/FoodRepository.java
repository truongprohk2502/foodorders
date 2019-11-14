package code.repository;

import code.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {

    Page<Food> findAllByFoodType(String foodType, Pageable pageable);

    Page<Food> findAllByNameContaining(String name, Pageable pageable);
}
