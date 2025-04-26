import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;


public class InventoryManagement extends JFrame {
    private JPanel panel;
    private JTable table;
    private JButton refreshButton;
    private JButton backButton;
    private JButton addProductButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JComboBox<Product> productComboBox;
    private JComboBox<String> sortComboBox;


    private DefaultTableModel model;


    public InventoryManagement() {
        // Initialize the DefaultTableModel
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Product ID", "Product Name", "Category", "Quantity", "Price"});
        table.setModel(model); // Set the table model


        // Load data and update the table
        loadInventoryData();
        setContentPane(panel);
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setBackground(new Color(137, 207, 240));





        initializeActions();

        refreshProductComboBox();
        setVisible(true);
    }


    public void initializeActions() {
        refreshButton.addActionListener(e -> {
            loadInventoryData();
            refreshProductComboBox();

        });


        backButton.addActionListener(e -> {
            new Welcome(new User("0", "User", null));
            dispose();
        });


        addProductButton.addActionListener(e -> newAddProduct());
        updateButton.addActionListener(e -> newUpdateProduct());
        deleteButton.addActionListener(e -> newDeleteProduct());
        sortComboBox.addActionListener(e -> sortInventoryData());
    }




    // Method to load inventory data into the JTable
    public void loadInventoryData() {
        model.setRowCount(0);  // Clear the table before adding new rows
        ArrayList<Product> products = connect.getInventory();  // Get products from the database
        for (Product product : products) {
            model.addRow(new Object[]{
                    product.getProductID(),
                    product.getProductName(),
                    product.getCategoryID(),
                    product.getStockQuantity(),
                    product.getPrice()
            });
        }
    }
    private void sortInventoryData() {
        String selectedSort = (String) sortComboBox.getSelectedItem();
        String query = "SELECT ProductID, ProductName, CategoryID, StockQuantity, ReorderLevel, Price FROM Products";

        switch (selectedSort) {
            case "Products A-Z":
                query += " ORDER BY ProductName ASC";
                break;
            case "Products Z-A":
                query += " ORDER BY ProductName DESC";
                break;
            case "Quantity < 10":
                query += " WHERE StockQuantity < 10 ORDER BY StockQuantity ASC";
                break;
            case "Price > 20":
                query += " WHERE Price > 20 ORDER BY Price ASC";
                break;
        }

        model.setRowCount(0);  // Clear the table before adding new rows
        try (Connection conn = connect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getInt("CategoryID"),
                        rs.getInt("StockQuantity"),
                        rs.getDouble("Price")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error sorting products: " + ex.getMessage());
        }
    }


    // Method to populate the ComboBox with products
    private void refreshProductComboBox() {
        productComboBox.removeAllItems();  // Clear the combo box
        ArrayList<Product> products = connect.getInventory();  // Get products from the database
        for (Product product : products) {
            productComboBox.addItem(product);  // Add items to combo box
        }
    }


    // Navigate to the Add Product window
    private void newAddProduct() {
        new AddProduct();
        dispose();
    }


    // Navigate to the Update Product window
    private void newUpdateProduct() {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        if (selectedProduct != null) {
            new UpdateProduct(selectedProduct);  // Pass the selected product to the UpdateProduct window
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to update.");
        }
        dispose();
    }


    // Navigate to the Delete Product window
    private void newDeleteProduct() {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        if (selectedProduct != null) {
            new DeleteProduct(selectedProduct);  // Pass the selected product to the DeleteProduct window
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.");
        }
        dispose();
    }


    public static void main(String[] args) {
        new InventoryManagement();
    }
}
