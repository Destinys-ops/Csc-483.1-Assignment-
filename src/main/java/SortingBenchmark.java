// SortingBenchmark.java

import java.util.*;
import java.io.*;

public class SortingBenchmark {
    private static final int[] SIZES = {100, 1000, 10000, 100000};
    private static final int RUNS = 5;
    
    public static void main(String[] args) throws IOException {
        List<BenchmarkResult> results = new ArrayList<>();
        
        for (int size : SIZES) {
            System.out.println("Testing size: " + size);
            
            // Generate datasets
            int[][] datasets = {
                generateRandom(size),
                generateSorted(size),
                generateReverse(size),
                generateNearlySorted(size),
                generateDuplicates(size)
            };
            String[] dataTypes = {"Random", "Sorted", "Reverse", "NearlySorted", "Duplicates"};
            
            for (int i = 0; i < datasets.length; i++) {
                results.addAll(benchmarkAlgorithm("InsertionSort", dataTypes[i], 
                    size, datasets[i], SortingBenchmark::insertionSort));
                results.addAll(benchmarkAlgorithm("MergeSort", dataTypes[i], 
                    size, datasets[i], SortingBenchmark::mergeSort));
                results.addAll(benchmarkAlgorithm("QuickSort", dataTypes[i], 
                    size, datasets[i], SortingBenchmark::quickSort));
            }
        }
        
        // Output results
        printResults(results);
        saveToCSV(results);
    }
    
    // Insertion Sort Implementation with counters
    static class SortResult {
        int[] array;
        long comparisons;
        long swaps;
        long timeMs;
    }
    
    public static SortResult insertionSort(int[] arr) {
        SortResult result = new SortResult();
        int[] a = Arrays.copyOf(arr, arr.length);
        long start = System.nanoTime();
        
        for (int i = 1; i < a.length; i++) {
            int key = a[i];
            int j = i - 1;
            result.swaps++; // Count key assignment
            
            while (j >= 0) {
                result.comparisons++;
                if (a[j] > key) {
                    a[j + 1] = a[j];
                    result.swaps++;
                    j--;
                } else break;
            }
            a[j + 1] = key;
        }
        
        result.timeMs = (System.nanoTime() - start) / 1_000_000;
        result.array = a;
        return result;
    }
    
    // Merge Sort with counters
    public static SortResult mergeSort(int[] arr) {
        SortResult result = new SortResult();
        int[] a = Arrays.copyOf(arr, arr.length);
        long start = System.nanoTime();
        
        result.comparisons = 0;
        result.swaps = 0; // Assignments in merge
        mergeSortHelper(a, 0, a.length - 1, new int[a.length], result);
        
        result.timeMs = (System.nanoTime() - start) / 1_000_000;
        result.array = a;
        return result;
    }
    
    private static void mergeSortHelper(int[] a, int left, int right, 
                                       int[] temp, SortResult stats) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortHelper(a, left, mid, temp, stats);
            mergeSortHelper(a, mid + 1, right, temp, stats);
            merge(a, left, mid, right, temp, stats);
        }
    }
    
    private static void merge(int[] a, int left, int mid, int right, 
                             int[] temp, SortResult stats) {
        System.arraycopy(a, left, temp, left, right - left + 1);
        
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            stats.comparisons++;
            if (temp[i] <= temp[j]) {
                a[k++] = temp[i++];
            } else {
                a[k++] = temp[j++];
            }
            stats.swaps++;
        }
        while (i <= mid) {
            a[k++] = temp[i++];
            stats.swaps++;
        }
        while (j <= right) {
            a[k++] = temp[j++];
            stats.swaps++;
        }
    }
    
    // Quick Sort with counters
    public static SortResult quickSort(int[] arr) {
        SortResult result = new SortResult();
        int[] a = Arrays.copyOf(arr, arr.length);
        long start = System.nanoTime();
        
        result.comparisons = 0;
        result.swaps = 0;
        quickSortHelper(a, 0, a.length - 1, result);
        
        result.timeMs = (System.nanoTime() - start) / 1_000_000;
        result.array = a;
        return result;
    }
    
    private static void quickSortHelper(int[] a, int low, int high, SortResult stats) {
        if (low < high) {
            int pivot = partition(a, low, high, stats);
            quickSortHelper(a, low, pivot - 1, stats);
            quickSortHelper(a, pivot + 1, high, stats);
        }
    }
    
    private static int partition(int[] a, int low, int high, SortResult stats) {
        int pivot = a[high];
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            stats.comparisons++;
            if (a[j] <= pivot) {
                i++;
                swap(a, i, j, stats);
            }
        }
        swap(a, i + 1, high, stats);
        return i + 1;
    }
    
    private static void swap(int[] a, int i, int j, SortResult stats) {
        if (i != j) {
            int temp = a[i];
            a[i] = a[j];
            a[j] = temp;
            stats.swaps++;
        }
    }
    
    // Data generators
    private static int[] generateRandom(int n) {
        Random r = new Random(42);
        return r.ints(n, 0, n).toArray();
    }
    
    private static int[] generateSorted(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        return a;
    }
    
    private static int[] generateReverse(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = n - i;
        return a;
    }
    
    private static int[] generateNearlySorted(int n) {
        int[] a = generateSorted(n);
        Random r = new Random(42);
        // Swap 10% of elements
        for (int i = 0; i < n / 10; i++) {
            int idx1 = r.nextInt(n);
            int idx2 = r.nextInt(n);
            int temp = a[idx1];
            a[idx1] = a[idx2];
            a[idx2] = temp;
        }
        return a;
    }
    
    private static int[] generateDuplicates(int n) {
        Random r = new Random(42);
        return r.ints(n, 0, 10).toArray();
    }
    
    private static List<BenchmarkResult> benchmarkAlgorithm(
            String name, String dataType, int size, int[] data, 
            java.util.function.Function<int[], SortResult> sorter) {
        List<Long> times = new ArrayList<>();
        long totalComparisons = 0, totalSwaps = 0;
        
        for (int i = 0; i < RUNS; i++) {
            SortResult result = sorter.apply(data);
            times.add(result.timeMs);
            totalComparisons += result.comparisons;
            totalSwaps += result.swaps;
        }
        
        double avgTime = times.stream().mapToLong(Long::longValue).average().orElse(0);
        double stdDev = calculateStdDev(times, avgTime);
        
        BenchmarkResult br = new BenchmarkResult();
        br.algorithm = name;
        br.dataType = dataType;
        br.size = size;
        br.avgTimeMs = avgTime;
        br.stdDev = stdDev;
        br.avgComparisons = totalComparisons / RUNS;
        br.avgSwaps = totalSwaps / RUNS;
        
        return Collections.singletonList(br);
    }
    
    private static double calculateStdDev(List<Long> values, double mean) {
        double sum = 0;
        for (long v : values) {
            sum += Math.pow(v - mean, 2);
        }
        return Math.sqrt(sum / values.size());
    }
    
    static class BenchmarkResult {
        String algorithm, dataType;
        int size;
        double avgTimeMs, stdDev;
        long avgComparisons, avgSwaps;
    }
    
    private static void printResults(List<BenchmarkResult> results) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SORTING ALGORITHMS COMPARISON RESULTS");
        System.out.println("=".repeat(80));
        System.out.printf("%-15s %-12s %-8s %10s %12s %12s %12s%n",
            "Algorithm", "Data Type", "Size", "Time(ms)", "StdDev", "Comparisons", "Swaps");
        System.out.println("-".repeat(80));
        
        for (BenchmarkResult r : results) {
            System.out.printf("%-15s %-12s %-8d %10.3f %12.3f %12d %12d%n",
                r.algorithm, r.dataType, r.size, r.avgTimeMs, r.stdDev,
                r.avgComparisons, r.avgSwaps);
        }
    }
    
    private static void saveToCSV(List<BenchmarkResult> results) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter("benchmark_results.csv"))) {
            pw.println("Algorithm,DataType,Size,AvgTimeMs,StdDev,Comparisons,Swaps");
            for (BenchmarkResult r : results) {
                pw.printf("%s,%s,%d,%.3f,%.3f,%d,%d%n",
                    r.algorithm, r.dataType, r.size, r.avgTimeMs, r.stdDev,
                    r.avgComparisons, r.avgSwaps);
            }
        }
    }
}
