import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class UpdateProduct extends JFrame {
    private JPanel mainPanel;
    private JButton updateButton;
    private JButton backButton;
    private JTextField productNameField;
    private JTextField quantityField;
    private JTextField priceField;
    private JTextField reorderLevelField;
    private JComboBox<Product> productComboBox;

    private Product productToUpdate;  // Store the selected product for updating

    public UpdateProduct(Product selectedProduct) {
        this.productToUpdate = selectedProduct;  // Store the selected product

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setContentPane(mainPanel);
        setVisible(true);
        mainPanel.setBackground(new Color(137, 207, 240));

        // Initialize the ComboBox for products
        productComboBox = new JComboBox<>();

        // Populate the product ComboBox
        populateProductComboBox();

        // Pre-fill the form fields with the selected product's details
        loadSelectedProductData();

        // Add listeners for the update button and back button
        updateButton.addActionListener(e -> updateProduct());
        backButton.addActionListener(e -> {
            dispose();
            new InventoryManagement();  // Navigate back to InventoryManagement
        });

        // Add listener to ComboBox to load product data when a product is selected
        productComboBox.addActionListener(e -> loadSelectedProductData());

        setVisible(true);
    }

    // Populate the ComboBox with products from the database
    private void populateProductComboBox() {
        try {
            ArrayList<Product> products = connect.getInventory();  // Fetch products from the database
            for (Product product : products) {
                productComboBox.addItem(product);  // Add each product to the ComboBox
            }

            // Load the selected product into the ComboBox
            if (productToUpdate != null) {
                productComboBox.setSelectedItem(productToUpdate);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage());
        }
    }

    // Load the selected product's data into the form fields
    private void loadSelectedProductData() {
        // Get the selected product from the ComboBox
        productToUpdate = (Product) productComboBox.getSelectedItem();

        if (productToUpdate != null) {
            // Set the selected product's data in the form fields
            productNameField.setText(productToUpdate.getProductName());
            quantityField.setText(String.valueOf(productToUpdate.getStockQuantity()));
            priceField.setText(String.valueOf(productToUpdate.getPrice()));
            reorderLevelField.setText(String.valueOf(productToUpdate.getReorderLevel()));
        }
    }

    // Update the product in the database
    private void updateProduct() {
        if (productToUpdate == null) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.");
            return;
        }

        // Get the updated values from the form fields
        String productName = productNameField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String priceText = priceField.getText().trim();
        String reorderText = reorderLevelField.getText().trim();

        // Check if any field is empty
        if (productName.isEmpty() || quantityText.isEmpty() || priceText.isEmpty() || reorderText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            // Parse the values from text fields
            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);
            int reorderLevel = Integer.parseInt(reorderText);

            // Update the product in the database using the `connect.updateProduct` method
            connect.updateProduct(
                    productToUpdate.getProductID(),
                    productName,
                    productToUpdate.getCategoryID(),  // Keep the original category ID (no need for category combo box)
                    quantity,
                    reorderLevel,
                    price
            );

            // Show a confirmation message
            JOptionPane.showMessageDialog(this, "Product updated successfully!");

            // Update the product in the ComboBox to reflect the changes
            productToUpdate.setProductName(productName);
            productToUpdate.setStockQuantity(quantity);
            productToUpdate.setPrice(price);
            productToUpdate.setReorderLevel(reorderLevel);

            // Refresh the ComboBox to show the updated product data
            productComboBox.repaint();

            // Close the UpdateProduct window and refresh the InventoryManagement window
            dispose();
            new InventoryManagement();  // Reopen InventoryManagement with updated data

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for quantity, price, and reorder level.");
        }
    }

    public static void main(String[] args) {
        // This is for testing purposes
        Product testProduct = new Product(1, "Test Product", 1, 100, 10, 50.0);  // Example product for testing
        new UpdateProduct(testProduct);
    }
}
