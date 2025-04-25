import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class UpdateProduct extends JFrame {
    private JPanel mainPanel;
    private JComboBox<Product> productComboBox;
    private JTextField productNameField;
    private JTextField quantityField;
    private JTextField priceField;
    private JTextField reorderLevelField;
    private JButton updateButton;
    private JButton backButton;

    public UpdateProduct() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setContentPane(mainPanel);
        setVisible(true);
        mainPanel.setBackground(new Color(137, 207, 240));

        // Initialize the ComboBox for products
        productComboBox = new JComboBox<>();

        // Populate product combo box
        populateProductComboBox();

        // Add listeners
        updateButton.addActionListener(e -> updateProduct());
        backButton.addActionListener(e -> {
            dispose();
            new InventoryManagement();  // Navigate back to InventoryManagement
        });

        // Add listener to ComboBox to load product data when a product is selected
        productComboBox.addActionListener(e -> loadSelectedProductData());

        setVisible(true);
    }

    private void populateProductComboBox() {
        try {
            ArrayList<Product> products = connect.getInventory();
            for (Product product : products) {
                productComboBox.addItem(product);  // Add product to combo box
            }
            if (products.size() > 0) {
                loadSelectedProductData();  // Load the first product's data
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage());
        }
    }

    private void loadSelectedProductData() {
        Product selected = (Product) productComboBox.getSelectedItem();
        if (selected != null) {
            productNameField.setText(selected.getProductName());
            quantityField.setText(String.valueOf(selected.getStockQuantity()));
            priceField.setText(String.valueOf(selected.getPrice()));
            reorderLevelField.setText(String.valueOf(selected.getReorderLevel()));
        }
    }

    private void updateProduct() {
        Product selected = (Product) productComboBox.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.");
            return;
        }

        String productName = productNameField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String priceText = priceField.getText().trim();
        String reorderText = reorderLevelField.getText().trim();

        if (productName.isEmpty() || quantityText.isEmpty() || priceText.isEmpty() || reorderText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);
            int reorderLevel = Integer.parseInt(reorderText);

            // Update the product in the database
            connect.updateProduct(
                    selected.getProductID(),
                    productName,
                    selected.getCategoryID(),  // Keep the original category ID (no need for category combo box)
                    quantity,
                    reorderLevel,
                    price
            );

            JOptionPane.showMessageDialog(this, "Product updated successfully!");
            selected.setProductName(productName);
            selected.setStockQuantity(quantity);
            selected.setPrice(price);
            selected.setReorderLevel(reorderLevel);
            productComboBox.repaint();  // Update ComboBox to reflect the changes

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for quantity, price, and reorder level.");
        }
    }

    public static void main(String[] args) {
        new UpdateProduct();
    }
}
