package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.benchmark.Benchmark;
import com.example.data.HashMapStudentDB;
import com.example.data.HeapStudentDB;
import com.example.data.StudentStorage;
import com.example.data.TreeMapStudentDB;
import com.example.io.CSVReader;


public class BenchmarkRunner {
    
    private static final int[] TEST_SIZES = {100, 1000, 10000, 100000};
    private static final String FULL_CSV = "src/main/resources/students.csv";
    
    public static void main(String[] args) throws Exception {
        System.out.println("=" .repeat(80));
        System.out.println("ЕКСПЕРИМЕНТАЛЬНЕ ПОРІВНЯННЯ КОНТЕЙНЕРІВ");
        System.out.println("Співвідношення операцій A:B:C = 2:10:30");
        
        Map<String, List<BenchmarkResult>> results = new LinkedHashMap<>();
        results.put("HashMap", new ArrayList<>());
        results.put("TreeMap", new ArrayList<>());
        results.put("Heap", new ArrayList<>());
        
        for (int size : TEST_SIZES) {
            System.out.println("ТЕСТУВАННЯ З БАЗОЮ РОЗМІРОМ: " + size + " студентів");
            
            String tempCSV = createTempCSV(size);
            
            BenchmarkResult hashResult = testContainer("HashMap", tempCSV, 
                () -> new HashMapStudentDB());
            results.get("HashMap").add(hashResult);
            
            BenchmarkResult treeResult = testContainer("TreeMap", tempCSV, 
                () -> new TreeMapStudentDB());
            results.get("TreeMap").add(treeResult);
            
            BenchmarkResult heapResult = testContainer("Heap", tempCSV, 
                () -> new HeapStudentDB());
            results.get("Heap").add(heapResult);
            
            new File(tempCSV).delete();
        }
        
        printSummaryTable(results);
        
        exportResultsToCSV(results);
        
        System.out.println("\nЕкспериментальні дані збережено у benchmark_results.csv");
    }
    
    private static BenchmarkResult testContainer(String name, String csvPath, 
                                                  ContainerFactory factory) throws Exception {
        System.out.println("\nТестування: " + name);
        
        StudentStorage db = factory.create();
        CSVReader.load(csvPath, db);
        
        Runtime runtime = Runtime.getRuntime();
        System.gc();
        Thread.sleep(100); 
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        
        Benchmark bench = new Benchmark(db);
        long operations = bench.runFor10SecondsWithCount();
        
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = (memoryAfter - memoryBefore) / (1024 * 1024); 
        
        System.out.printf("   Операцій за 10с: %,d%n", operations);
        System.out.printf("   Пам'ять: %d MB%n", memoryUsed);
        
        return new BenchmarkResult(name, db.size(), operations, memoryUsed);
    }
    
    private static String createTempCSV(int maxLines) throws IOException {
        String tempFile = "temp_students_" + maxLines + ".csv";
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FULL_CSV));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            
            String header = reader.readLine();
            if (header != null) {
                writer.write(header);
                writer.newLine();
            }
            
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < maxLines) {
                writer.write(line);
                writer.newLine();
                count++;
            }
        }
        
        return tempFile;
    }
    
    private static void printSummaryTable(Map<String, List<BenchmarkResult>> results) {
        System.out.println("ПІДСУМКОВА ТАБЛИЦЯ РЕЗУЛЬТАТІВ");
        System.out.printf("%-15s", "Розмір бази");
        for (String container : results.keySet()) {
            System.out.printf("%-20s", container);
        }
        System.out.println();
        System.out.println("-".repeat(80));
        
        for (int i = 0; i < TEST_SIZES.length; i++) {
            System.out.printf("%-15s", TEST_SIZES[i] + " студентів");
            for (List<BenchmarkResult> containerResults : results.values()) {
                BenchmarkResult result = containerResults.get(i);
                System.out.printf("%-20s", String.format("%,d ops (%dMB)", 
                    result.operations, result.memoryMB));
            }
            System.out.println();
        }
        System.out.println("=".repeat(80));
    }
    
    private static void exportResultsToCSV(Map<String, List<BenchmarkResult>> results) 
            throws IOException {
        try (PrintWriter writer = new PrintWriter("benchmark_results.csv")) {
            writer.print("Database_Size");
            for (String container : results.keySet()) {
                writer.print("," + container + "_Operations");
                writer.print("," + container + "_Memory_MB");
            }
            writer.println();
            
            for (int i = 0; i < TEST_SIZES.length; i++) {
                writer.print(TEST_SIZES[i]);
                for (List<BenchmarkResult> containerResults : results.values()) {
                    BenchmarkResult result = containerResults.get(i);
                    writer.print("," + result.operations);
                    writer.print("," + result.memoryMB);
                }
                writer.println();
            }
        }
    }
    
    @FunctionalInterface
    interface ContainerFactory {
        StudentStorage create();
    }
    
    static class BenchmarkResult {
        final String containerName;
        final int dbSize;
        final long operations;
        final long memoryMB;
        
        BenchmarkResult(String containerName, int dbSize, long operations, long memoryMB) {
            this.containerName = containerName;
            this.dbSize = dbSize;
            this.operations = operations;
            this.memoryMB = memoryMB;
        }
    }
}