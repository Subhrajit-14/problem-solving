import java.util.*;
import java.util.stream.Collectors;

// Product class
class Product {
    private String id;
    private String name;
    private String category;
    private String description;
    private double price;
    private Map<String, String> additionalAttributes;

    // Constructor
    public Product(String id, String name, String category, String description, double price, Map<String, String> additionalAttributes) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.additionalAttributes = additionalAttributes;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Map<String, String> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(Map<String, String> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", additionalAttributes=" + additionalAttributes +
                '}';
    }
}

// ProductConfigSystem class
class ProductConfigSystem {
    private Map<String, Product> productMap; // Product ID to Product
    private Map<String, List<String>> categoryMap; // Category to List of Product IDs
    private Map<String, Set<String>> searchIndex; // Keyword to Set of Product IDs

    public ProductConfigSystem() {
        this.productMap = new HashMap<>();
        this.categoryMap = new HashMap<>();
        this.searchIndex = new HashMap<>();
    }

    // Add Product
    public void addProduct(Product product) {
        productMap.put(product.getId(), product);
        categoryMap.computeIfAbsent(product.getCategory(), k -> new ArrayList<>()).add(product.getId());
        indexProduct(product);
    }

    // Index Product for Search
    private void indexProduct(Product product) {
        indexField(product.getId(), product.getName());
        indexField(product.getId(), product.getCategory());
        indexField(product.getId(), product.getDescription());
        if (product.getAdditionalAttributes() != null) {
            product.getAdditionalAttributes().values().forEach(value -> indexField(product.getId(), value));
        }
    }

    private void indexField(String productId, String field) {
        String[] words = field.toLowerCase().split("\\W+");
        for (String word : words) {
            searchIndex.computeIfAbsent(word, k -> new HashSet<>()).add(productId);
        }
    }

    // Search Products
    public Set<Product> searchProducts(String query) {
        Set<String> productIds = searchIndex.get(query.toLowerCase());
        return productIds == null ? Collections.emptySet() : productIds.stream()
                .map(productMap::get)
                .collect(Collectors.toSet());
    }

    // Example Usage
    public static void main(String[] args) {
        ProductConfigSystem system = new ProductConfigSystem();

        Product product1 = new Product("1", "Laptop", "Electronics", "High-performance laptop with 16GB RAM", 1000.0, null);
        Product product2 = new Product("2", "Headphones", "Electronics", "Noise-cancelling headphones", 200.0, null);

        system.addProduct(product1);
        system.addProduct(product2);

        Set<Product> results = system.searchProducts("laptop");
        results.forEach(System.out::println);

        results = system.searchProducts("headphones");
        results.forEach(System.out::println);
    }
}
