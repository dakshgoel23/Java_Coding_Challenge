package entity;

public class OrderDetails {
    private int orderId;
    private int productId;

    public OrderDetails(int orderId, int productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                '}';
    }
}
