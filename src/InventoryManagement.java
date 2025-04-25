import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

    public static DefaultTableModel model;  // Declare the model as public and static for access across classes

    public InventoryManagement() {
        // Initialize the DefaultTableModel and set columns
        model = new DefaultTableModel();
        model.addColumn("Product ID");
        model.addColumn("Product Name");
        model.addColumn("Category ID");
        model.addColumn("Quantity");
        model.addColumn("Price");

        // Initialize JTable and set its model
        table = new JTable(model);

        // Make the table scrollable by wrapping it in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        panel.setLayout(new BorderLayout()); // Set the panel layout to BorderLayout
        panel.add(scrollPane, BorderLayout.CENTER);  // Add the scrollable table to the center of the panel

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
    }

    public void loadInventoryData() {
        model.setRowCount(0);  // Clear the table before adding new rows
        ArrayList<Product> products = connect.getInventory();  // Get products from the database
        for (Product product : products) {
            // Add data to the table model (rows will appear under the column names)
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
        productComboBox.removeAllItems();  // Clear the combo box
        ArrayList<Product> products = connect.getInventory();  // Get products from the database
        for (Product product : products) {
            productComboBox.addItem(product);  // Add items to combo box
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
        Product selectedProduct = (Product) productComboBox.getSelectedItem();

        if (selectedProduct != null) {
            new DeleteProduct(selectedProduct);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.");
        }
        dispose();
    }

    public static void main(String[] args) {
        new InventoryManagement();
    }
}
