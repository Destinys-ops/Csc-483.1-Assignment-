// SearchAlgorithmsTest.java

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SearchAlgorithmsTest {
    private Product[] testProducts;
    
    @BeforeEach
    void setUp() {
        testProducts = new Product[]{
            new Product(1, "Laptop", "Electronics", 999.99, 10),
            new Product(3, "Mouse", "Accessories", 25.50, 50),
            new Product(5, "Keyboard", "Accessories", 75.00, 30),
            new Product(7, "Monitor", "Electronics", 299.99, 15),
            new Product(9, "Headphones", "Audio", 149.99, 20)
        };
    }
    
    @Test
    @DisplayName("Sequential search finds existing product")
    void testSequentialSearchFound() {
        Product result = SearchAlgorithms.sequentialSearchById(testProducts, 5);
        assertNotNull(result);
        assertEquals("Keyboard", result.getProductName());
    }
    
    @Test
    @DisplayName("Sequential search returns null for missing product")
    void testSequentialSearchNotFound() {
        Product result = SearchAlgorithms.sequentialSearchById(testProducts, 99);
        assertNull(result);
    }
    
    @Test
    @DisplayName("Binary search finds product in sorted array")
    void testBinarySearchFound() {
        Product result = SearchAlgorithms.binarySearchById(testProducts, 3);
        assertNotNull(result);
        assertEquals("Mouse", result.getProductName());
    }
    
    @Test
    @DisplayName("Binary search handles empty array")
    void testBinarySearchEmpty() {
        Product[] empty = new Product[0];
        assertNull(SearchAlgorithms.binarySearchById(empty, 1));
    }
    
    @Test
    @DisplayName("Search by name is case-insensitive")
    void testSearchByName() {
        Product result = SearchAlgorithms.searchByName(testProducts, "laptop");
        assertNotNull(result);
        assertEquals(1, result.getProductId());
    }
    
    @Test
    @DisplayName("Null inputs handled gracefully")
    void testNullInputs() {
        assertNull(SearchAlgorithms.sequentialSearchById(null, 1));
        assertNull(SearchAlgorithms.binarySearchById(null, 1));
        assertNull(SearchAlgorithms.searchByName(testProducts, null));
    }
}
