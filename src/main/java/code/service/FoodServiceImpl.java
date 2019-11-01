package code.service;

import code.model.Food;
import code.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
    public List<Food> findRandomFoods() {
        return em.createNativeQuery("select * from Food order by rand() limit 8", Food.class).getResultList();
    }

    @Override
    public List<Food> findFoodsByFoodType(String foodType) {
        Query query = em.createNativeQuery("select * from Food where foodType = :foodType order by rand() limit 8", Food.class);
        query.setParameter("foodType", foodType);
        return query.getResultList();
    }
}
