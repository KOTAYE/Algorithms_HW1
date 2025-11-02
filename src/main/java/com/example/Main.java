package com.example;

import java.util.List;

import com.example.benchmark.Benchmark;
import com.example.data.HashMapStudentDB;
import com.example.data.HeapStudentDB;
import com.example.data.StudentStorage;
import com.example.data.TreeMapStudentDB;
import com.example.io.CSVReader;
import com.example.model.Student;
import com.example.sort.Sorter;
import com.example.sort.Sorter.SortResult;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Loading students from CSV...");
        String csvPath = "src/main/resources/students.csv";

        HashMapStudentDB hashDB = new HashMapStudentDB();
        TreeMapStudentDB treeDB = new TreeMapStudentDB();
        HeapStudentDB heapDB = new HeapStudentDB();

        CSVReader.load(csvPath, hashDB);
        CSVReader.load(csvPath, treeDB);
        CSVReader.load(csvPath, heapDB);

        System.out.println("Databases initialized successfully!");

        runBenchmark("HashMap", hashDB);
        runBenchmark("TreeMap", treeDB);
        runBenchmark("Heap", heapDB);

        System.out.println("ПОРІВНЯННЯ МЕТОДІВ СОРТУВАННЯ ЗА НОМЕРОМ ТЕЛЕФОНУ");
        
        List<Student> students = hashDB.getAllStudents();
        System.out.println("Кількість студентів: " + students.size());
        System.out.println();

        SortResult standardResult = Sorter.sortStandard(students);
        System.out.printf("%s: %d ms%n", 
            standardResult.methodName, standardResult.durationMs);

        SortResult radixResult = Sorter.sortRadix(students);
        System.out.printf("%s: %d ms%n", 
            radixResult.methodName, radixResult.durationMs);

        System.out.println();
        if (radixResult.durationMs == 0 && standardResult.durationMs == 0) {
            System.out.println("⚡ Обидва методи виконались менше ніж за 1 ms");
        } else if (radixResult.durationMs < standardResult.durationMs) {
            if (radixResult.durationMs > 0) {
                double speedup = (double) standardResult.durationMs / radixResult.durationMs;
                System.out.printf("Radix Sort швидше у %.2fx разів%n", speedup);
            } else {
                System.out.println("Radix Sort швидше (< 1ms vs " + standardResult.durationMs + "ms)");
            }
        } else if (standardResult.durationMs < radixResult.durationMs) {
            if (standardResult.durationMs > 0) {
                double speedup = (double) radixResult.durationMs / standardResult.durationMs;
                System.out.printf("Standard Sort швидше у %.2fx разів%n", speedup);
            } else {
                System.out.println("Standard Sort швидше (< 1ms vs " + radixResult.durationMs + "ms)");
            }
        } else {
            System.out.println("Методи показали однакову швидкість");
        }

        boolean isCorrect = verifySort(standardResult.sortedList, radixResult.sortedList);
        if (isCorrect) {
            System.out.println("Обидва методи дали однаковий результат");
        } else {
            System.out.println("Результати відрізняються!");
            findFirstDifference(standardResult.sortedList, radixResult.sortedList);
        }

        System.out.println();
        Sorter.saveToCSV(standardResult.sortedList, "sorted_students_standard.csv");
        Sorter.saveToCSV(radixResult.sortedList, "sorted_students_radix.csv");



        System.out.println("\n Program finished successfully!");
    }

    private static void runBenchmark(String name, StudentStorage db) {
        System.out.println("\n--- Benchmark " + name + " ---");

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long before = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);

        Benchmark bench = new Benchmark(db);
        bench.runFor10Seconds();

        long after = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
        System.out.println("Memory used: " + (after - before) + " MB");
    }

    private static boolean verifySort(List<Student> list1, List<Student> list2) {
        if (list1.size() != list2.size()) return false;
        for (int i = 0; i < list1.size(); i++) {
            String phone1 = digitsOnly(list1.get(i).getPhoneNumber());
            String phone2 = digitsOnly(list2.get(i).getPhoneNumber());
            if (!phone1.equals(phone2)) return false;
        }
        return true;
    }

    private static void findFirstDifference(List<Student> list1, List<Student> list2) {
        int diffCount = 0;
        for (int i = 0; i < Math.min(list1.size(), list2.size()) && diffCount < 5; i++) {
            Student s1 = list1.get(i);
            Student s2 = list2.get(i);
            String phone1 = digitsOnly(s1.getPhoneNumber());
            String phone2 = digitsOnly(s2.getPhoneNumber());
            
            if (!phone1.equals(phone2)) {
                diffCount++;
                System.out.printf("Позиція %d:%n", i);
                System.out.printf("  Standard: %s (%s) -> %s%n", 
                    s1.getName(), s1.getPhoneNumber(), phone1);
                System.out.printf("  Radix:    %s (%s) -> %s%n", 
                    s2.getName(), s2.getPhoneNumber(), phone2);
            }
        }

    }

    private static String digitsOnly(String phone) {
        return phone.replaceAll("\\D", "");
    }
}