import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
public class DeleteProduct extends JFrame {
    private JPanel mainPanel;
    private JButton deleteButton;
    private JButton backButton;
    private JLabel productDetailsLabel;
    private JComboBox<Product> productComboBox;

    private Product productToDelete;  // This will hold the product to delete

    // Constructor to accept the product to delete
    public DeleteProduct(Product selectedProduct) {
        this.productToDelete = selectedProduct;  // Store the selected product

        setSize(500, 300);
        setContentPane(mainPanel);
        setVisible(true);
        mainPanel.setBackground(new Color(137, 207, 240));

        // Initialize the product details
        updateProductDetails();

        // Add action listeners using lambdas
        deleteButton.addActionListener(e -> deleteSelectedProduct());
        backButton.addActionListener(e -> {
            dispose();
            new InventoryManagement();
        });
    }

    private void updateProductDetails() {
        if (productToDelete != null) {
            String details = String.format(
                    "Do you want to delete : %s | Category ID: %d | Stock: %d | Price: $%.2f",
                    productToDelete.getProductName(),
                    productToDelete.getCategoryID(),
                    productToDelete.getStockQuantity(),
                    productToDelete.getPrice()
            );
            productDetailsLabel.setText(details);
        }
    }

    private void deleteSelectedProduct() {
        if (productToDelete != null) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete '" + productToDelete.getProductName() + "'?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = connect.deleteProduct(productToDelete.getProductID());

                if (success) {
                    JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                    dispose();
                    new InventoryManagement();// Close the delete window
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete product.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No product selected.");
        }
    }

}
