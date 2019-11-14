package code.service;

import code.model.Food;
import code.repository.FoodRepository;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Food findById(Long id) {
        return foodRepository.findOne(id);
    }

    @Override
    public List<Food> findRandomFoods(int num) {
        Query query = em.createNativeQuery("select * from Food order by rand() limit :num", Food.class);
        query.setParameter("num", num);
        return query.getResultList();
    }

    @Override
    public List<Food> findFoodsByFoodType(String foodType, int num) {
        Query query = em.createNativeQuery("select * from Food where foodType = :foodType order by rand() limit :num", Food.class);
        query.setParameter("num", num);
        query.setParameter("foodType", foodType);
        return query.getResultList();
    }

    @Override
    public Page<Food> findUnlimitedFoodsByType(String type, Pageable pageable) {
        if ("ALL".equals(type)) {
            return foodRepository.findAll(pageable);
        }
        return foodRepository.findAllByFoodType(type, pageable);
    }

    @Override
    public Page<Food> findFoodsByFoodName(String name, Pageable pageable) {
        return foodRepository.findAllByNameContaining(name, pageable);
    }

    @Override
    public Page<Food> findAll(Pageable pageable) {
        return foodRepository.findAll(pageable);
    }

    @Override
    public void deleteFoodById(Long id) {
        foodRepository.delete(id);
    }

    @Override
    public void save(Food food) {
        foodRepository.save(food);
    }
}
