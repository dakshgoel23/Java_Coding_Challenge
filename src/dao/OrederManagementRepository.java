package dao;

import exception.UserNotFoundException;
import exception.OrderNotFoundException;
import exception.ProductNotFoundException;
import entity.Product;
import entity.User;
import entity.OrderDetails;
import java.util.List;

public interface OrederManagementRepository {
    void createOrder(int userId, int productId) throws Exception;
    void cancelOrder(int userId, int orderId)throws Exception;
    void createProduct(User user, Product product)throws Exception;
    void createUser(User user)throws Exception;
    List<Product> getAllProducts()throws Exception;
    List<OrderDetails> getOrderByUser(User user)throws Exception;
}
