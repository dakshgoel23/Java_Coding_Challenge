package main;

import dao.OrderProcessor;
import entity.OrderDetails;
import entity.Product;
import entity.User;
import exception.OrderNotFoundException;
import exception.UserNotFoundException;
import exception.ProductNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MainModule {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OrderProcessor orderProcessor = new OrderProcessor();

        while (true) {
            System.out.println("\n--- Order Management System ---");
            System.out.println("1. Create User");
            System.out.println("2. Create Product");
            System.out.println("3. Cancel Order");
            System.out.println("4. Get All Products");
            System.out.println("5. Get Orders by User");
            System.out.println("6. Create Order");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    System.out.print("Enter role (Admin/User): ");
                    String role = scanner.nextLine();
                    User newUser = new User(0, username, password, role); // userId will be auto-generated
                    try {
                        orderProcessor.createUser(newUser);
                    } catch (SQLException e) {
                        System.out.println("Error creating user: " + e.getMessage());
                    }
                    break;

                case 2:
                    
                    System.out.print("Enter product name: ");
                    String productName = scanner.nextLine();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter quantity in stock: ");
                    int quantityInStock = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter product type (Electronics/Clothing): ");
                    String type = scanner.nextLine();
                    Product newProduct = new Product(0, productName, description, price, quantityInStock, type); 
                    User adminUser = new User(1, "admin", "admin@123", "Admin"); 
                    try {
                        orderProcessor.createProduct(adminUser, newProduct);
                    } catch (UserNotFoundException | SQLException e) {
                        System.out.println("Error creating product: " + e.getMessage());
                    }
                    break;

                case 3:
                    
                    System.out.print("Enter user ID: ");
                    int userIdCancel = scanner.nextInt();
                    System.out.print("Enter order ID: ");
                    int orderIdCancel = scanner.nextInt();
                    try {
                        orderProcessor.cancelOrder(userIdCancel, orderIdCancel);
                    } catch (UserNotFoundException | OrderNotFoundException | SQLException e) {
                        System.out.println("Error cancelling order: " + e.getMessage());
                    }
                    break;

                case 4:
                    // Get All Products
                    try {
                        List<Product> products = orderProcessor.getAllProducts();
                        System.out.println("All Products:");
                        for (Product product : products) {
                            System.out.println(product);
                        }
                    } catch (SQLException e) {
                        System.out.println("Error retrieving products: " + e.getMessage());
                    }
                    break;

                case 5:
                    // Get Orders by User
                    System.out.print("Enter user ID: ");
                    int userIdGetOrders = scanner.nextInt();
                    User user = new User(userIdGetOrders, "", "", ""); // Create a user object with userId only
                    try {
                        List<OrderDetails> orders = orderProcessor.getOrderByUser(user);
                        System.out.println("Orders for User ID " + userIdGetOrders + ":");
                        for (OrderDetails order : orders) {
                            System.out.println(order);
                        }
                    } catch (UserNotFoundException | SQLException e) {
                        System.out.println("Error retrieving orders: " + e.getMessage());
                    }
                    break;
                    
                case 6:
                    // Create Order
                    System.out.print("Enter user ID for the order: ");
                    int userIdOrder = scanner.nextInt();
                    System.out.print("Enter product ID to order: ");
                    int productIdOrder = scanner.nextInt();

                    try {
                        orderProcessor.createOrder(userIdOrder, productIdOrder);
                    } catch (UserNotFoundException | ProductNotFoundException | SQLException e) {
                        System.out.println("Error creating order: " + e.getMessage());
                    }
                    break;


                case 7:
                   
                    System.out.println("Exiting the system. Thank you!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
