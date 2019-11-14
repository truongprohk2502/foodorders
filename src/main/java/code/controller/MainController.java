package code.controller;

import code.model.Account;
import code.model.Food;
import code.model.Ordered;
import code.service.AccountService;
import code.service.FoodService;
import code.service.OrderedService;
import code.session.CartSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@SessionAttributes("cart")
public class MainController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderedService orderedService;

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
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("index");
        subList(modelAndView, foodService.findRandomFoods(8), "all");
        subList(modelAndView, foodService.findFoodsByFoodType("BREAKFAST", 8), "breakfast");
        subList(modelAndView, foodService.findFoodsByFoodType("LUNCH", 8), "lunch");
        subList(modelAndView, foodService.findFoodsByFoodType("DINNER", 8), "dinner");
        subList(modelAndView, foodService.findFoodsByFoodType("COFFEE", 8), "coffee");
        subList(modelAndView, foodService.findFoodsByFoodType("SNACKS", 8), "snacks");
        return modelAndView;
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("detail");
        modelAndView.addObject("food", foodService.findById(id));
        modelAndView.addObject("popularFoods", foodService.findRandomFoods(3));
        return modelAndView;
    }

    @GetMapping("/cart-food")
    public ModelAndView cartFood(@ModelAttribute("cart") CartSession cart) {
        ModelAndView modelAndView = new ModelAndView("cart");
        modelAndView.addObject("orders", cart.getSingleOrders());
        return modelAndView;
    }

    @GetMapping("/checkout")
    public ModelAndView checkout(Principal principal, @ModelAttribute("cart") CartSession cart) {
        ModelAndView modelAndView = new ModelAndView("checkout");
        modelAndView.addObject("orders", cart.getSingleOrders());
        modelAndView.addObject("subtotal", cart.getSubTotal());
        modelAndView.addObject("shippingFee", cart.getShippingFee());
        modelAndView.addObject("total", cart.getTotal());
        if (principal != null) {
            modelAndView.addObject("account", accountService.findAccountByUsername(principal.getName()));
        } else {
            modelAndView.addObject("account", new Account());
        }
        return modelAndView;
    }

    @GetMapping("/contact")
    public ModelAndView contact() {
        ModelAndView modelAndView = new ModelAndView("contact");

        return modelAndView;
    }

    @GetMapping("/list/{category}")
    public ModelAndView listFoods(@PathVariable String category, Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("list");
        modelAndView.addObject("category", category);
        pageable = new PageRequest(pageable.getPageNumber(), 6);
        modelAndView.addObject("foods",  foodService.findUnlimitedFoodsByType(category.toUpperCase(), pageable));
        return modelAndView;
    }

    @GetMapping("/find{search}")
    public ModelAndView searchFoods(@RequestParam String search, Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("search");
        modelAndView.addObject("foodName", search);
        pageable = new PageRequest(pageable.getPageNumber(), 6);
        modelAndView.addObject("foods",  foodService.findFoodsByFoodName(search, pageable));
        return modelAndView;
    }

    @GetMapping("/admin/manage-food")
    public ModelAndView manageFoods(Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("manage-food");
        pageable = new PageRequest(pageable.getPageNumber(), 6);
        modelAndView.addObject("foods", foodService.findAll(pageable));
        return modelAndView;
    }

    @GetMapping("/admin/delivering-order")
    public ModelAndView deliveringFoods(Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("delivering-order");
        pageable = new PageRequest(pageable.getPageNumber(), 6);
        modelAndView.addObject("orders", orderedService.findAll("DELIVERING", pageable));
        return modelAndView;
    }

    @GetMapping("/admin/delivered-order")
    public ModelAndView deliveredFoods(Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("finish-order");
        pageable = new PageRequest(pageable.getPageNumber(), 6);
        modelAndView.addObject("title", "Delivered Orders");
        modelAndView.addObject("orders", orderedService.findAll("DELIVERED", pageable));
        return modelAndView;
    }

    @GetMapping("/admin/canceled-order")
    public ModelAndView canceledFoods(Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("finish-order");
        pageable = new PageRequest(pageable.getPageNumber(), 6);
        modelAndView.addObject("title", "Canceled Orders");
        modelAndView.addObject("orders", orderedService.findAll("CANCELED", pageable));
        return modelAndView;
    }
}
