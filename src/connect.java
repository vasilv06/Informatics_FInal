import org.mindrot.jbcrypt.BCrypt;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class connect {
    static final String URL = "jdbc:mysql://localhost:3306/acs";
    static final String USER = "root";
    static final String PASSWORD = "0000";

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void addUser(String firstName, String lastName, InputStream img, String hashPassword) {
        String query = "INSERT INTO Employees (Name, Address, img, Password) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setBinaryStream(3, img);
            pstmt.setString(4, hashPassword);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static User login(String id, String password) {
        User user = null;
        String query = "SELECT EmployeeID, Name, img, Password FROM Employees WHERE EmployeeID = ?";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashed = rs.getString("Password");
                    if (BCrypt.checkpw(password, hashed)) {
                        InputStream imgStream = rs.getBinaryStream("img");
                        user = new User(rs.getString("EmployeeID"), rs.getString("Name"), imgStream);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return user;
    }

    public static void addProduct(String productName, int categoryID, int stockQuantity, int reorderLevel, double price) {
        String query = "INSERT INTO Products (ProductName, CategoryID, StockQuantity, ReorderLevel, Price) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, productName);
            pstmt.setInt(2, categoryID);
            pstmt.setInt(3, stockQuantity);
            pstmt.setInt(4, reorderLevel);
            pstmt.setDouble(5, price);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void updateProduct(int productID, String productName, int categoryID, int stockQuantity, int reorderLevel, double price) {
        String query = "UPDATE Products SET ProductName = ?, CategoryID = ?, StockQuantity = ?, ReorderLevel = ?, Price = ? WHERE ProductID = ?";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, productName);
            pstmt.setInt(2, categoryID);
            pstmt.setInt(3, stockQuantity);
            pstmt.setInt(4, reorderLevel);
            pstmt.setDouble(5, price);
            pstmt.setInt(6, productID);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean deleteProduct(int productID) {
        String query = "DELETE FROM Products WHERE ProductID = ?";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, productID);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  // Return true if a row was deleted, false otherwise
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static ArrayList<Product> getInventory() {
        ArrayList<Product> products = new ArrayList<>();
        String query = "SELECT ProductID, ProductName, CategoryID, StockQuantity, ReorderLevel, Price FROM Products";
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int categoryID = rs.getInt("CategoryID");
                int stockQuantity = rs.getInt("StockQuantity");
                int reorderLevel = rs.getInt("ReorderLevel");
                double price = rs.getDouble("Price");
                products.add(new Product(productID, productName, categoryID, stockQuantity, reorderLevel, price));  // Keep price as double
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return products;
    }
}
