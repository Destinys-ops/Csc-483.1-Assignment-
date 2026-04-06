// HybridProductCatalog.java

import java.util.*;

/**
 * Hybrid search implementation using sorted array + HashMap index.
 * Provides O(log n) ID search and O(1) average name lookup.
 */
public class HybridProductCatalog {
    private Product[] products;
    private int size;
    private final Map<String, List<Integer>> nameIndex;
    private static final int INITIAL_CAPACITY = 100_000;
    private static final double GROWTH_FACTOR = 1.5;
    
    public HybridProductCatalog() {
        this.products = new Product[INITIAL_CAPACITY];
        this.size = 0;
        this.nameIndex = new HashMap<>();
    }
    
    /**
     * Adds product while maintaining sorted order by ID.
     * Time Complexity: O(n) for shifting + O(1) for index update = O(n)
     * Amortized: O(n) due to occasional array resizing
     * 
     * @param newProduct Product to add
     * @return true if added successfully
     */
    public boolean addProduct(Product newProduct) {
        if (newProduct == null) return false;
        
        // Ensure capacity
        if (size >= products.length) {
            resizeArray();
        }
        
        // Find insertion point using binary search
        int insertPos = findInsertionPoint(newProduct.getProductId());
        
        // Check for duplicate ID
        if (insertPos < size && products[insertPos].getProductId() == newProduct.getProductId()) {
            return false; // Duplicate ID
        }
        
        // Shift elements to the right
        System.arraycopy(products, insertPos, products, insertPos + 1, size - insertPos);
        
        // Insert new product
        products[insertPos] = newProduct;
        size++;
        
        // Update name index
        updateNameIndex(newProduct.getProductName(), insertPos);
        
        // Adjust indices for shifted elements
        adjustIndices(insertPos);
        
        return true;
    }
    
    /**
     * Binary search for insertion point.
     */
    private int findInsertionPoint(int targetId) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (products[mid].getProductId() < targetId) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }
    
    /**
     * Search by ID using binary search.
     * Time Complexity: O(log n)
     */
    public Product searchById(int targetId) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midId = products[mid].getProductId();
            if (midId == targetId) return products[mid];
            if (midId < targetId) left = mid + 1;
            else right = mid - 1;
        }
        return null;
    }
    
    /**
     * Search by name using HashMap index.
     * Time Complexity: O(1) average lookup + O(k) retrieval where k = matches
     */
    public List<Product> searchByName(String name) {
        List<Integer> indices = nameIndex.get(name.toLowerCase());
        if (indices == null) return Collections.emptyList();
        
        List<Product> results = new ArrayList<>();
        for (int idx : indices) {
            if (idx < size && products[idx] != null) {
                results.add(products[idx]);
            }
        }
        return results;
    }
    
    private void updateNameIndex(String name, int index) {
        String key = name.toLowerCase();
        nameIndex.computeIfAbsent(key, k -> new ArrayList<>()).add(index);
    }
    
    private void adjustIndices(int fromIndex) {
        // Update all indices in nameIndex that are >= fromIndex
        for (List<Integer> indices : nameIndex.values()) {
            for (int i = 0; i < indices.size(); i++) {
                if (indices.get(i) >= fromIndex) {
                    indices.set(i, indices.get(i) + 1);
                }
            }
        }
    }
    
    private void resizeArray() {
        int newCapacity = (int)(products.length * GROWTH_FACTOR);
        products = Arrays.copyOf(products, newCapacity);
    }
}
