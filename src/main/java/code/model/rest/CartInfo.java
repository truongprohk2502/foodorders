package code.model.rest;

public class CartInfo {

    private Double subtotal;
    private Double shippingFee;
    private Double total;

    public CartInfo() {
    }

    public CartInfo(Double subtotal, Double shippingFee, Double total) {
        this.subtotal = subtotal;
        this.shippingFee = shippingFee;
        this.total = total;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
