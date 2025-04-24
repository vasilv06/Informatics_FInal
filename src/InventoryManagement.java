import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class InventoryManagement extends JFrame {
    private JPanel panel;
    private JTable table;
    private JButton refreshButton;
    private JButton backButton;
    private JButton addProductButton;
    private JButton updateButton;
    private JButton deleteButton;
    private DefaultTableModel model;
    private JComboBox<Product> productComboBox;

    public InventoryManagement() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        model.addColumn("Product ID");
        model.addColumn("Product Name");
        model.addColumn("Category ID");
        model.addColumn("Quantity");
        model.addColumn("Price");

        loadInventoryData();

        productComboBox = new JComboBox<>();
        refreshProductComboBox();

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Select Product:"), BorderLayout.WEST);
        topPanel.add(productComboBox, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        refreshButton = new JButton("Refresh");
        backButton = new JButton("Back");
        addProductButton = new JButton("Add Product");
        updateButton = new JButton("Update Selected");
        deleteButton = new JButton("Delete Selected");

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        buttonPanel.add(addProductButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Inventory Management");
        setSize(800, 500);
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        refreshButton.addActionListener(e -> {
            loadInventoryData();
            refreshProductComboBox();
        });

        backButton.addActionListener(e -> {
            new Welcome(new User("0", "User", null));
            dispose();
        });

        addProductButton.addActionListener(e -> showAddProductDialog());
        updateButton.addActionListener(e -> showUpdateProductDialog());
        deleteButton.addActionListener(e -> deleteSelectedProduct());
    }

    public void refreshProductComboBox() {
        productComboBox.removeAllItems();
        ArrayList<Product> products = connect.getInventory();
        for (Product product : products) {
            productComboBox.addItem(product);
        }
    }

    public void loadInventoryData() {
        model.setRowCount(0);
        ArrayList<Product> data = connect.getInventory();
        for (Product product : data) {
            model.addRow(new Object[]{
                    product.getProductID(),
                    product.getProductName(),
                    product.getCategoryID(),
                    product.getStockQuantity(),
                    product.getPrice()
            });
        }
    }

    public void showAddProductDialog() {
        JTextField productNameField = new JTextField(20);
        JTextField categoryIdField = new JTextField(20);
        JTextField quantityField = new JTextField(20);
        JTextField priceField = new JTextField(20);
        JTextField reorderLevelField = new JTextField(20);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Product Name:"));
        inputPanel.add(productNameField);
        inputPanel.add(new JLabel("Category ID:"));
        inputPanel.add(categoryIdField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Reorder Level:"));
        inputPanel.add(reorderLevelField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);

        int option = JOptionPane.showConfirmDialog(this, inputPanel, "Add Product", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String productName = productNameField.getText();
            String categoryIdText = categoryIdField.getText();
            String quantityText = quantityField.getText();
            String priceText = priceField.getText();
            String reorderLevelText = reorderLevelField.getText();

            if (!productName.isEmpty() && !quantityText.isEmpty() &&
                    !priceText.isEmpty() && !reorderLevelText.isEmpty() && !categoryIdText.isEmpty()) {
                try {
                    int quantity = Integer.parseInt(quantityText);
                    double price = Double.parseDouble(priceText);
                    int reorderLevel = Integer.parseInt(reorderLevelText);
                    int categoryID = Integer.parseInt(categoryIdText);

                    connect.addProduct(productName, categoryID, quantity, reorderLevel, price);
                    JOptionPane.showMessageDialog(this, "Product added successfully!");
                    loadInventoryData();
                    refreshProductComboBox();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid numeric input.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
            }
        }
    }

    public void showUpdateProductDialog() {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.");
            return;
        }

        JTextField productNameField = new JTextField(selectedProduct.getProductName(), 20);
        JTextField categoryIdField = new JTextField(String.valueOf(selectedProduct.getCategoryID()), 20);
        JTextField quantityField = new JTextField(String.valueOf(selectedProduct.getStockQuantity()), 20);
        JTextField priceField = new JTextField(String.valueOf(selectedProduct.getPrice()), 20);
        JTextField reorderLevelField = new JTextField(String.valueOf(selectedProduct.getReorderLevel()), 20);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Product Name:"));
        inputPanel.add(productNameField);
        inputPanel.add(new JLabel("Category ID:"));
        inputPanel.add(categoryIdField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Reorder Level:"));
        inputPanel.add(reorderLevelField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);

        int option = JOptionPane.showConfirmDialog(this, inputPanel, "Update Product", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String productName = productNameField.getText();
            String categoryIdText = categoryIdField.getText();
            String quantityText = quantityField.getText();
            String priceText = priceField.getText();
            String reorderLevelText = reorderLevelField.getText();

            if (!productName.isEmpty() && !quantityText.isEmpty() &&
                    !priceText.isEmpty() && !reorderLevelText.isEmpty() && !categoryIdText.isEmpty()) {
                try {
                    int quantity = Integer.parseInt(quantityText);
                    double price = Double.parseDouble(priceText);
                    int reorderLevel = Integer.parseInt(reorderLevelText);
                    int categoryID = Integer.parseInt(categoryIdText);

                    connect.updateProduct(selectedProduct.getProductID(), productName, categoryID, quantity, reorderLevel, price);
                    JOptionPane.showMessageDialog(this, "Product updated successfully!");
                    loadInventoryData();
                    refreshProductComboBox();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid numeric input.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
            }
        }
    }

    public void deleteSelectedProduct() {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete " + selectedProduct.getProductName() + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            connect.deleteProduct(selectedProduct.getProductID());
            JOptionPane.showMessageDialog(this, "Product deleted successfully!");
            loadInventoryData();
            refreshProductComboBox();
        }
    }
}