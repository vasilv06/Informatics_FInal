import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class UpdateProduct extends JFrame {
    private JPanel mainPanel;
    private JComboBox<Product> productComboBox;
    private JTextField productNameField;
    private JComboBox<String> categoryComboBox;
    private JTextField quantityField;
    private JTextField priceField;
    private JTextField reorderLevelField;
    private JButton updateButton;
    private JButton backButton;

    public UpdateProduct() {
        setTitle("Update Product");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel setup
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        mainPanel.setBackground(new Color(245, 245, 245));
        setContentPane(mainPanel);

        // Heading
        JLabel heading = new JLabel("Update Product");
        heading.setFont(new Font("Arial", Font.BOLD, 18));
        heading.setForeground(new Color(34, 40, 49));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        mainPanel.add(heading, gbc);

        // Product Selection
        JLabel selectLabel = new JLabel("Select Product:");
        selectLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(selectLabel, gbc);

        productComboBox = new JComboBox<>();
        populateProductComboBox();
        productComboBox.addActionListener(e -> loadSelectedProductData());
        gbc.gridx = 1;
        mainPanel.add(productComboBox, gbc);

        // Product Name
        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(nameLabel, gbc);

        productNameField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(productNameField, gbc);

        // Category
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(categoryLabel, gbc);

        categoryComboBox = new JComboBox<>();
        populateCategories();
        gbc.gridx = 1;
        mainPanel.add(categoryComboBox, gbc);

        // Quantity
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(quantityLabel, gbc);

        quantityField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(quantityField, gbc);

        // Price
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(priceLabel, gbc);

        priceField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(priceField, gbc);

        // Reorder Level
        JLabel reorderLabel = new JLabel("Reorder Level:");
        reorderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(reorderLabel, gbc);

        reorderLevelField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(reorderLevelField, gbc);

        // Update Button
        updateButton = new JButton("Update Product");
        updateButton.setFont(new Font("Arial", Font.PLAIN, 14));
        updateButton.setBackground(new Color(255, 152, 0)); // Orange color
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(updateButton, gbc);

        // Back Button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(new Color(233, 30, 99));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(backButton, gbc);

        // Button actions
        updateButton.addActionListener(e -> updateProduct());
        backButton.addActionListener(e -> {
            dispose();
            new InventoryManagement();
        });

        setVisible(true);
    }

    private void populateProductComboBox() {
        try {
            ArrayList<Product> products = connect.getInventory();
            for (Product product : products) {
                productComboBox.addItem(product);
            }
            if (products.size() > 0) {
                loadSelectedProductData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage());
        }
    }

    private void populateCategories() {
        try {
            ArrayList<String> categories = getCategoriesFromDatabase();
            for (String category : categories) {
                categoryComboBox.addItem(category);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + ex.getMessage());
        }
    }

    private ArrayList<String> getCategoriesFromDatabase() throws SQLException {
        ArrayList<String> categories = new ArrayList<>();
        String query = "SELECT CategoryName FROM Categories";

        try (Connection conn = connect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                categories.add(rs.getString("category_name"));
            }
        }
        return categories;
    }

    private void loadSelectedProductData() {
        Product selected = (Product) productComboBox.getSelectedItem();
        if (selected != null) {
            productNameField.setText(selected.getProductName());
            quantityField.setText(String.valueOf(selected.getStockQuantity()));
            priceField.setText(String.valueOf(selected.getPrice()));
            reorderLevelField.setText(String.valueOf(selected.getReorderLevel()));

            // Set the category in the combo box
            try {
                String categoryName = getCategoryName(selected.getCategoryID());
                categoryComboBox.setSelectedItem(categoryName);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error loading category: " + ex.getMessage());
            }
        }
    }

    private String getCategoryName(int categoryId) throws SQLException {
        String query = "SELECT CategoryName FROM Categories WHERE CategoryID = ?";

        try (Connection conn = connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("category_name");
                }
            }
        }
        throw new SQLException("Category not found");
    }

    private void updateProduct() {
        Product selected = (Product) productComboBox.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.");
            return;
        }

        String productName = productNameField.getText().trim();
        String categoryName = (String) categoryComboBox.getSelectedItem();
        String quantityText = quantityField.getText().trim();
        String priceText = priceField.getText().trim();
        String reorderText = reorderLevelField.getText().trim();

        if (productName.isEmpty() || quantityText.isEmpty() || priceText.isEmpty() || reorderText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            int categoryId = getCategoryId(categoryName);
            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);
            int reorderLevel = Integer.parseInt(reorderText);

            connect.updateProduct(
                    selected.getProductID(),
                    productName,
                    categoryId,
                    quantity,
                    reorderLevel,
                    price
            );

            JOptionPane.showMessageDialog(this, "Product updated successfully!");
            selected.setProductName(productName);
            selected.setCategoryID(categoryId);
            selected.setStockQuantity(quantity);
            selected.setPrice(price);
            selected.setReorderLevel(reorderLevel);
            productComboBox.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for quantity, price, and reorder level.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private int getCategoryId(String categoryName) throws SQLException {
        String query = "SELECT CategoryID FROM Categories WHERE CategoryName = ?";

        try (Connection conn = connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, categoryName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("category_id");
                }
            }
        }
        throw new SQLException("Category not found");
    }

    public static void main(String[] args) {
        new UpdateProduct();
    }
}
