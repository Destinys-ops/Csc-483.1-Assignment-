// SearchAlgorithms.java

import java.util.Arrays;

/**
 * Contains search algorithm implementations for TechMart product catalog.
 */
public class SearchAlgorithms {
    
    /**
     * Sequential search by product ID.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     * 
     * @param products Array of products to search
     * @param targetId ID to find
     * @return Product if found, null otherwise
     */
    public static Product sequentialSearchById(Product[] products, int targetId) {
        if (products == null) return null;
        
        for (Product p : products) {
            if (p != null && p.getProductId() == targetId) {
                return p;
            }
        }
        return null;
    }
    
    /**
     * Binary search by product ID (requires sorted array).
     * Time Complexity: O(log n)
     * Space Complexity: O(1) iterative, O(log n) recursive stack
     * 
     * @param products Sorted array of products
     * @param targetId ID to find
     * @return Product if found, null otherwise
     */
    public static Product binarySearchById(Product[] products, int targetId) {
        if (products == null) return null;
        
        int left = 0;
        int right = products.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2; // Prevent overflow
            Product midProduct = products[mid];
            
            if (midProduct.getProductId() == targetId) {
                return midProduct;
            } else if (midProduct.getProductId() < targetId) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }
    
    /**
     * Sequential search by product name (case-insensitive).
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     * 
     * @param products Array of products
     * @param targetName Name to search for
     * @return Product if found, null otherwise
     */
    public static Product searchByName(Product[] products, String targetName) {
        if (products == null || targetName == null) return null;
        
        String searchName = targetName.toLowerCase();
        
        for (Product p : products) {
            if (p != null && p.getProductName().toLowerCase().contains(searchName)) {
                return p;
            }
        }
        return null;
    }
    
    /**
     * Sorts products by ID for binary search preparation.
     * Uses built-in sort for demonstration (student should implement merge sort).
     */
    public static void sortById(Product[] products) {
        Arrays.sort(products); // Uses TimSort (hybrid merge/insertion sort)
    }
}
