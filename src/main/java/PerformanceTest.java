// PerformanceTest.java

import java.util.Arrays;
import java.util.Random;

/**
 * Performance testing framework for search algorithms.
 */
public class PerformanceTest {
    private static final int DATASET_SIZE = 100_000;
    private static final int MAX_ID = 200_000;
    private static final Random random = new Random(42); // Seed for reproducibility
    
    public static void main(String[] args) {
        System.out.println("Generating dataset of " + DATASET_SIZE + " products...");
        Product[] products = generateDataset(DATASET_SIZE);
        
        // Sort copy for binary search
        Product[] sortedProducts = Arrays.copyOf(products, products.length);
        SearchAlgorithms.sortById(sortedProducts);
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TECHMART SEARCH PERFORMANCE ANALYSIS (n = " + DATASET_SIZE + ")");
        System.out.println("=".repeat(60));
        
        // SEQUENTIAL SEARCH TESTS
        System.out.println("\nSEQUENTIAL SEARCH:");
        
        // Best Case: First element
        int firstId = products[0].getProductId();
        long start = System.nanoTime();
        SearchAlgorithms.sequentialSearchById(products, firstId);
        long bestSeq = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Best Case (ID found at position 0): %d ms%n", bestSeq);
        
        // Average Case: Random ID
        long totalSeq = 0;
        int tests = 100;
        for (int i = 0; i < tests; i++) {
            int randomId = products[random.nextInt(DATASET_SIZE)].getProductId();
            start = System.nanoTime();
            SearchAlgorithms.sequentialSearchById(products, randomId);
            totalSeq += (System.nanoTime() - start);
        }
        long avgSeq = (totalSeq / tests) / 1_000_000;
        System.out.printf("Average Case (random ID): %.3f ms%n", (double)avgSeq);
        
        // Worst Case: Non-existent ID
        start = System.nanoTime();
        SearchAlgorithms.sequentialSearchById(products, MAX_ID + 1);
        long worstSeq = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Worst Case (ID not found): %d ms%n", worstSeq);
        
        // BINARY SEARCH TESTS
        System.out.println("\nBINARY SEARCH:");
        
        // Best Case: Middle element
        int midId = sortedProducts[DATASET_SIZE / 2].getProductId();
        start = System.nanoTime();
        SearchAlgorithms.binarySearchById(sortedProducts, midId);
        long bestBin = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Best Case (ID at middle): %d ms%n", bestBin);
        
        // Average Case: Random ID
        long totalBin = 0;
        for (int i = 0; i < tests; i++) {
            int randomId = sortedProducts[random.nextInt(DATASET_SIZE)].getProductId();
            start = System.nanoTime();
            SearchAlgorithms.binarySearchById(sortedProducts, randomId);
            totalBin += (System.nanoTime() - start);
        }
        long avgBin = (totalBin / tests) / 1_000_000;
        System.out.printf("Average Case (random ID): %.3f ms%n", (double)avgBin);
        
        // Worst Case: Non-existent ID
        start = System.nanoTime();
        SearchAlgorithms.binarySearchById(sortedProducts, MAX_ID + 1);
        long worstBin = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Worst Case (ID not found): %d ms%n", worstBin);
        
        // Performance Summary
        System.out.println("\n" + "-".repeat(60));
        double speedup = (double)avgSeq / avgBin;
        System.out.printf("PERFORMANCE IMPROVEMENT: Binary search is ~%.0fx faster on average%n", speedup);
        System.out.println("-".repeat(60));
    }
    
    private static Product[] generateDataset(int size) {
        Product[] products = new Product[size];
        String[] categories = {"Electronics", "Computers", "Phones", "Accessories", "Gaming"};
        
        for (int i = 0; i < size; i++) {
            int id = random.nextInt(MAX_ID) + 1;
            String name = "Product_" + id + "_" + random.nextInt(1000);
            String category = categories[random.nextInt(categories.length)];
            double price = 10.0 + random.nextDouble() * 990.0;
            int stock = random.nextInt(500) + 1;
            
            products[i] = new Product(id, name, category, price, stock);
        }
        return products;
    }
}
