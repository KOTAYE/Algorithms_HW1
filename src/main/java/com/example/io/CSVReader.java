package com.example.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.example.data.StudentStorage;
import com.example.model.Student;

public class CSVReader {
    public static void load(String file, StudentStorage db) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(",");


                try {
                    Student s = new Student(
                            p[0].trim(),
                            p[1].trim(),
                            p[2].trim(),
                            Integer.parseInt(p[3].trim()),
                            Integer.parseInt(p[4].trim()),
                            Integer.parseInt(p[5].trim()),
                            p[6].trim(),
                            Float.parseFloat(p[7].trim()),
                            p[8].trim()
                    );
                    db.addStudent(s);
                } catch (NumberFormatException e) {
                    System.err.println(line);
                }
            }
        }
    }
}
