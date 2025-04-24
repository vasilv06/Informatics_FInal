public class Product {
    private int productID;
    private String productName;
    private int categoryID;
    private int stockQuantity;
    private int reorderLevel;
    private int price;

    public Product(int productID, String productName, int categoryID, int stockQuantity, int reorderLevel, int price) {
        this.productID = productID;
        this.productName = productName;
        this.categoryID = categoryID;
        this.stockQuantity = stockQuantity;
        this.reorderLevel = reorderLevel;
        this.price = price;
    }

    // Getters and setters
    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "ProductID: " + productID + ", ProductName: " + productName + ", CategoryID: " + categoryID +
                ", StockQuantity: " + stockQuantity + ", ReorderLevel: " + reorderLevel + ", Price: " + price;
    }
}
