package com.example.sort;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.model.Student;


public class Sorter {

    private static String digitsOnly(String phone) {
        return phone.replaceAll("\\D", "");
    }

    public static SortResult sortStandard(List<Student> students) {
        List<Student> copy = new ArrayList<>(students);
        
        long startTime = System.nanoTime();
        copy.sort(Comparator.comparing(s -> digitsOnly(s.getPhoneNumber())));
        long endTime = System.nanoTime();
        
        long durationMs = (endTime - startTime) / 1_000_000;
        return new SortResult(copy, durationMs, "Standard Sort (TimSort)");
    }

    public static SortResult sortRadix(List<Student> students) {
        long startTime = System.nanoTime();
        
        List<Student> list = new ArrayList<>(students);
        List<String> keys = new ArrayList<>(list.size());

        int maxLen = 0;
        for (Student s : list) {
            String digits = digitsOnly(s.getPhoneNumber());
            keys.add(digits);
            maxLen = Math.max(maxLen, digits.length());
        }

        List<String> paddedKeys = new ArrayList<>(keys.size());
        for (String key : keys) {
            paddedKeys.add(String.format("%" + maxLen + "s", key).replace(' ', '0'));
        }

        for (int pos = maxLen - 1; pos >= 0; pos--) {
            int[] count = new int[10];
            for (String key : paddedKeys) {
                int digit = key.charAt(pos) - '0';
                count[digit]++;
            }

            for (int i = 1; i < 10; i++)
                count[i] += count[i - 1];

            List<Student> newList = new ArrayList<>(Collections.nCopies(list.size(), null));
            List<String> newKeys = new ArrayList<>(Collections.nCopies(list.size(), null));

            for (int i = list.size() - 1; i >= 0; i--) {
                String k = paddedKeys.get(i);
                int d = k.charAt(pos) - '0';
                int idx = --count[d];
                newList.set(idx, list.get(i));
                newKeys.set(idx, k);
            }

            list = newList;
            paddedKeys = newKeys;
        }
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        
        return new SortResult(list, durationMs, "Radix Sort (LSD)");
    }

    private static int getDigitFromRight(String s, int posFromRight) {
        int idx = s.length() - 1 - posFromRight;
        if (idx < 0) return 0;
        return s.charAt(idx) - '0';
    }

    public static void saveToCSV(List<Student> students, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("name,surname,email,birth_year,birth_month,birth_day,group,rating,phone\n");
            for (Student s : students) {
                writer.write(String.format(
                        "%s,%s,%s,%d,%d,%d,%s,%.2f,%s\n",
                        s.getName(),
                        s.getSurname(),
                        s.getEmail(),
                        s.getBirthYear(),
                        s.getBirthMonth(),
                        s.getBirthDay(),
                        s.getGroup(),
                        s.getRating(),
                        s.getPhoneNumber()
                ));
            }
        }
        System.out.println("Sorted students saved to " + filename);
    }



    public static class SortResult {
        public final List<Student> sortedList;
        public final long durationMs;
        public final String methodName;

        public SortResult(List<Student> sortedList, long durationMs, String methodName) {
            this.sortedList = sortedList;
            this.durationMs = durationMs;
            this.methodName = methodName;
        }
    }
}