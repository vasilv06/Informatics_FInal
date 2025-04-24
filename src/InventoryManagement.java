import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class InventoryManagement extends JFrame {
    private JPanel panel;              // Bound from GUI form
    private JTable table;              // Table with model set in GUI form
    private JButton refreshButton;
    private JButton backButton;
    private JButton addProductButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JComboBox<Product> productComboBox;

    public InventoryManagement() {
        setTitle("Inventory Management");
        setContentPane(panel);
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setBackground(new Color(137, 207, 240));

        initializeActions();
        loadInventoryData();
        refreshProductComboBox();

        setVisible(true);
    }

    private void initializeActions() {
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
    }

    private void loadInventoryData() {
        // Use the table model defined in the GUI form
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        ArrayList<Product> products = connect.getInventory();
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

    private void refreshProductComboBox() {
        productComboBox.removeAllItems();
        ArrayList<Product> products = connect.getInventory();
        for (Product product : products) {
            productComboBox.addItem(product);
        }
    }

    private void newAddProduct() {
        new AddProduct();
        dispose();
    }

    private void newUpdateProduct() {
        new UpdateProduct();
        dispose();
    }

    private void newDeleteProduct() {
        new DeleteProduct();
        dispose();
    }
}
