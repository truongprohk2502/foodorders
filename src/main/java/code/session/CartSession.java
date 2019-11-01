package code.session;

import code.model.Food;
import code.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CartSession {

    private List<Food> foods;

    private Double shippingFee;

    public CartSession() {
        shippingFee = 2.5;
        foods = new ArrayList<>();
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public int getSize() {
        return foods.size();
    }

    public Double getSubTotal() {
        Double d = 0.0;
        for (Food food : foods) {
            d += food.getPrice();
        }
        return d;
    }

    public Double getTotal() {
        return getSubTotal() + shippingFee;
    }

    public void add(Food food) {
        foods.add(food);
    }

    public void removeById(Long id) {
        for (Food food : foods) {
            if (food.getId() == id) {
                foods.remove(food);
                return;
            }
        }
    }
}
