package code.model;

import javax.persistence.*;

@Entity
@Table(name = "singleOrder")
public class SingleOrder {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "foodId")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "orderedId")
    private Ordered ordered;

    private Long quantity;

    @Transient
    private Double totalPrice;

    @Transient
    private Boolean remove;

    @Transient
    private Long foodId;

    public SingleOrder() {
    }

    public SingleOrder(Food food, Long quantity) {
        this.food = food;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Ordered getOrdered() {
        return ordered;
    }

    public void setOrdered(Ordered ordered) {
        this.ordered = ordered;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalPrice() {
        return food.getPrice() * quantity;
    }

    public Boolean getRemove() {
        return remove;
    }

    public void setRemove(Boolean remove) {
        this.remove = remove;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }
}
