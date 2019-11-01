package code.controller;

import code.model.Food;
import code.service.FoodService;
import code.session.CartSession;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@SessionAttributes("cart")
public class MainController {

    @Autowired
    private FoodService foodService;

    @ModelAttribute("cart")
    public CartSession cartSession() {
        return new CartSession();
    }

    private void subList(ModelAndView modelAndView, List<Food> foods, String foodType) {
        List<Food> allFoods1;
        List<Food> allFoods2;
        if (foods.size() <= 4) {
            allFoods1 = foods.subList(0, foods.size());
        } else {
            allFoods1 = foods.subList(0, 4);
            allFoods2 = foods.subList(4, foods.size());
            modelAndView.addObject(foodType + "Foods2", allFoods2);
        }
        modelAndView.addObject(foodType + "Foods1", allFoods1);
    }

    @GetMapping("/home")
    public ModelAndView home(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");
        subList(modelAndView, foodService.findRandomFoods(), "all");
        subList(modelAndView, foodService.findFoodsByFoodType("BREAKFAST"), "breakfast");
        subList(modelAndView, foodService.findFoodsByFoodType("LUNCH"), "lunch");
        subList(modelAndView, foodService.findFoodsByFoodType("DINNER"), "dinner");
        subList(modelAndView, foodService.findFoodsByFoodType("COFFEE"), "coffee");
        subList(modelAndView, foodService.findFoodsByFoodType("SNACKS"), "snacks");
        return modelAndView;
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("detail");
        Food food = foodService.findById(id);
        if (food != null) {
            modelAndView.addObject("food", food);
        }
        return modelAndView;
    }
}
