package dao;

import entity.Product;
import entity.OrderDetails;
import entity.User;
import exception.UserNotFoundException;
import exception.OrderNotFoundException;
import exception.ProductNotFoundException;
import util.DBConnUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderProcessor implements OrederManagementRepository {

	
	@Override
	public void createOrder(int userId, int productId) throws UserNotFoundException, ProductNotFoundException, SQLException {
	    Connection connection = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        connection = DBConnUtil.getConnection();
	        
	       
	        ps = connection.prepareStatement("SELECT * FROM user WHERE userId = ?");
	        ps.setInt(1, userId);
	        rs = ps.executeQuery();
	        if (!rs.next()) {
	            throw new UserNotFoundException("User with ID: " + userId + " not found.");
	        }

	        
	        ps = connection.prepareStatement("SELECT * FROM product WHERE productId = ?");
	        ps.setInt(1, productId);
	        rs = ps.executeQuery();
	        if (!rs.next()) {
	            throw new ProductNotFoundException("Product with ID: " + productId + " not found.");
	        }

	       
	        ps = connection.prepareStatement("INSERT INTO orders (userId, productId) VALUES (?, ?)");
	        ps.setInt(1, userId);
	        ps.setInt(2, productId);
	        ps.executeUpdate();

	        System.out.println("Order successfully created for User ID: " + userId + " for Product ID: " + productId);

	    } finally {
	        if (rs != null) rs.close();
	        if (ps != null) ps.close();
	        if (connection != null) connection.close();
	    }
	}


    @Override
    public void cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = DBConnUtil.getConnection();

           
            ps = connection.prepareStatement("SELECT * FROM user WHERE userId = ?");
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (!rs.next()) {
                throw new UserNotFoundException("User with ID: " + userId + " not found.");
            }

           
            ps = connection.prepareStatement("SELECT * FROM orders WHERE orderId = ? AND userId = ?");
            ps.setInt(1, orderId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();
            if (!rs.next()) {
                throw new OrderNotFoundException("Order with ID: " + orderId + " not found for User ID: " + userId);
            }

         
            ps = connection.prepareStatement("DELETE FROM orders WHERE orderId = ?");
            ps.setInt(1, orderId);
            ps.executeUpdate();
            System.out.println("Order successfully cancelled for User ID: " + userId);

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public void createProduct(User user, Product product) throws UserNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = DBConnUtil.getConnection();

            // Check if user is an admin
            ps = connection.prepareStatement("SELECT * FROM user WHERE role = 'Admin'");
           // ps.setInt(1, user.getUserId());
            rs = ps.executeQuery();
            if (!rs.next()) {
                throw new UserNotFoundException("User with ID: " + user.getUserId() + " is not an Admin.");
            }

            // Insert product into the product table
            ps = connection.prepareStatement("INSERT INTO product (productName, description, price, quantityInStock, type) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getQuantityInStock());
            ps.setString(5, product.getType());
            ps.executeUpdate();

            System.out.println("Product successfully added to the database.");

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public void createUser(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = DBConnUtil.getConnection();

          
            ps = connection.prepareStatement("INSERT INTO user (username, password, role) VALUES (?, ?, ?)");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.executeUpdate();

            System.out.println("User successfully added to the database.");

        } finally {
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = DBConnUtil.getConnection();

            ps = connection.prepareStatement("SELECT * FROM product");
            rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("productId"),
                    rs.getString("productName"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("quantityInStock"),
                    rs.getString("type")
                );
                products.add(product);
            }

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        }

        return products;
    }

    @Override
    public List<OrderDetails> getOrderByUser(User user) throws UserNotFoundException, SQLException {
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = DBConnUtil.getConnection();

           
            ps = connection.prepareStatement("SELECT * FROM user WHERE userId = ?");
            ps.setInt(1, user.getUserId());
            rs = ps.executeQuery();
            if (!rs.next()) {
                throw new UserNotFoundException("User with ID: " + user.getUserId() + " not found.");
            }

         
            ps = connection.prepareStatement("SELECT * FROM orders WHERE userId = ?");
            ps.setInt(1, user.getUserId());
            rs = ps.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("orderId");
                int productId = rs.getInt("productId");
                OrderDetails orderDetails = new OrderDetails(orderId, productId);
                orderDetailsList.add(orderDetails);
            }

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        }

        return orderDetailsList;
    }
}
