import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DeleteProduct extends JFrame {
    private JPanel mainPanel;
    private JButton deleteButton;
    private JButton backButton;
    private JLabel productDetailsLabel;
    private JComboBox<Product> productComboBox;

    public DeleteProduct() {
        setSize(500, 300);
        setContentPane(mainPanel);
        setVisible(true);

        deleteButton.addActionListener(e -> deleteSelectedProduct());
        backButton.addActionListener(e -> {
            dispose();
            new InventoryManagement();
        });


    }



    private void updateProductDetails() {
        Product selected = (Product) productComboBox.getSelectedItem();
        if (selected != null) {
            String details = String.format(
                    "Name: %s | Category ID: %d | Stock: %d | Price: $%.2f",
                    selected.getProductName(),
                    selected.getCategoryID(),
                    selected.getStockQuantity(),
                    selected.getPrice()
            );
            productDetailsLabel.setText(details);
        }
    }

    private void deleteSelectedProduct() {
        Product selected = (Product) productComboBox.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete '" + selected.getProductName() + "'?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            connect.deleteProduct(selected.getProductID()); // Call the deleteProduct method
            JOptionPane.showMessageDialog(this, "Product deleted successfully!");
            productComboBox.removeItem(selected);
            if (productComboBox.getItemCount() > 0) {
                productComboBox.setSelectedIndex(0);
            } else {
                productDetailsLabel.setText("No products available");
            }
        }
    }

    public static void main(String[] args) {
        new DeleteProduct();
    }
}
