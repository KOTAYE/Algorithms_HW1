package com.example.data;

import java.util.List;
import java.util.Set;

import com.example.model.Student;

public interface StudentStorage {
    
    void addStudent(Student student);
    Student getStudentByPhone(String phoneNumber);
    List<Student> getAllStudents();
    int size();
    
    void changeGroup(String phoneNumber, String newGroup);
    
    List<Student> getStudentsByGroup(String group);
    
    Set<String> getGroupsBySurname(String surname);
    
    Set<String> getAllPhones();
    Set<String> getAllGroups();
    Set<String> getAllSurnames();
}