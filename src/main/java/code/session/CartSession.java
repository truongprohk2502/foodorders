package code.session;

import code.model.Food;
import code.model.SingleOrder;

import java.util.ArrayList;
import java.util.List;

public class CartSession {

    private List<SingleOrder> singleOrders;

    private Double shippingFee;

    public CartSession() {
        shippingFee = 2.5;
        singleOrders = new ArrayList<>();
    }

    public List<SingleOrder> getSingleOrders() {
        return singleOrders;
    }

    public void setSingleOrders(List<SingleOrder> singleOrders) {
        this.singleOrders = singleOrders;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public int getSize() {
        return singleOrders.size();
    }

    public Double getSubTotal() {
        Double d = 0.0;
        for (SingleOrder singleOrder : singleOrders) {
            d += singleOrder.getFood().getPrice() * singleOrder.getQuantity();
        }
        return d;
    }

    public Double getTotal() {
        return getSubTotal() != 0 ? getSubTotal() + shippingFee : 0D;
    }

    public void add(SingleOrder singleOrder) {
        singleOrders.add(singleOrder);
    }

    public void removeById(Long id) {
        for (SingleOrder singleOrder : singleOrders) {
            if (singleOrder.getFood().getId() == id) {
                singleOrders.remove(singleOrder);
                return;
            }
        }
    }

    public void setQtyById(Long id, Long qty) {
        for (SingleOrder singleOrder : singleOrders) {
            if (singleOrder.getFood().getId() == id) {
                singleOrder.setQuantity(qty);
                return;
            }
        }
    }

    public boolean constains(Food food) {
        for (SingleOrder s : singleOrders) {
            if (s.getFood().getId() == food.getId()) {
                return true;
            }
        }
        return false;
    }
}
