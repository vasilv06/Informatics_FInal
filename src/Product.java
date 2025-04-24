public class Product {
    private int productID;
    private String productName;
    private int categoryID;
    private int stockQuantity;
    private int reorderLevel;
    private double price;

    // Constructor
    public Product(int productID, String productName, int categoryID, int stockQuantity, int reorderLevel, double price) {
        this.productID = productID;
        this.productName = productName;
        this.categoryID = categoryID;
        this.stockQuantity = stockQuantity;
        this.reorderLevel = reorderLevel;
        this.price = price;
    }

    // Getter and Setter for productName
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Getter and Setter for categoryID
    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    // Getter and Setter for stockQuantity
    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    // Getter and Setter for reorderLevel
    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    // Getter and Setter for price
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Getter for productID (no setter, as it's auto-generated)
    public int getProductID() {
        return productID;
    }

    // Override toString to display product name in JComboBox
    @Override
    public String toString() {
        return productName;  // This will display only the product name in the combo box
    }
}
