import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddProduct extends JFrame {
    private JPanel mainPanel;
    private JTextField quantityField;
    private JTextField productNameField;
    private JTextField categoryIdField;
    private JTextField priceField;
    private JTextField reorderLevelField;
    private JButton addProductButton;
    private JButton backButton;

    public AddProduct() {
        setSize(600, 500);
        setContentPane(mainPanel);
        setVisible(true);
        mainPanel.setBackground(new Color(137, 207, 240));

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProductToDatabase();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new InventoryManagement();  // Go back to the inventory management screen
            }
        });
    }

    private void addProductToDatabase() {
        String productName = productNameField.getText().trim();
        String categoryIdText = categoryIdField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String priceText = priceField.getText().trim();
        String reorderText = reorderLevelField.getText().trim();

        if (productName.isEmpty() || categoryIdText.isEmpty() || quantityText.isEmpty() || priceText.isEmpty() || reorderText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            int categoryId = Integer.parseInt(categoryIdText);  // Parse category ID from input field
            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);
            int reorderLevel = Integer.parseInt(reorderText);

            String query = "INSERT INTO Products (ProductName, CategoryID, StockQuantity, ReorderLevel, Price) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = connect.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, productName);
                pstmt.setInt(2, categoryId);
                pstmt.setInt(3, quantity);
                pstmt.setInt(4, reorderLevel);
                pstmt.setDouble(5, price);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Product added successfully!");
                    clearForm();
                    refreshInventoryManagement();  // Refresh the inventory management view to show the new product.
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for category ID, quantity, price, and reorder level.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void clearForm() {
        productNameField.setText("");
        categoryIdField.setText("");  // Clear categoryIdField
        quantityField.setText("");
        priceField.setText("");
        reorderLevelField.setText("");
        productNameField.requestFocus();
    }

    private void refreshInventoryManagement() {
        try {
            InventoryManagement inventoryManagement = new InventoryManagement();
            inventoryManagement.loadInventoryData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error refreshing inventory: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new AddProduct();
    }
}
