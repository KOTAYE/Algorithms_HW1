package com.example.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.example.model.Student;

public class HashMapStudentDB implements StudentStorage {
    private final Map<String, Student> byPhone = new HashMap<>();
    private final Map<String, TreeSet<Student>> byGroup = new HashMap<>();
    private final Map<String, Set<String>> groupsBySurname = new HashMap<>();

    @Override
    public void addStudent(Student s) {
        byPhone.put(s.getPhoneNumber(), s);
        byGroup.computeIfAbsent(s.getGroup(), g -> new TreeSet<>(
            Comparator.comparing(Student::getSurname)
                      .thenComparing(Student::getName)
        )).add(s);
        groupsBySurname.computeIfAbsent(s.getSurname(), k -> new HashSet<>()).add(s.getGroup());
    }

    @Override
    public void changeGroup(String phone, String newGroup) {
        Student s = byPhone.get(phone);
        if (s == null) return;
        byGroup.get(s.getGroup()).remove(s);
        s.setGroup(newGroup);
        addStudent(s);
    }

    @Override
    public List<Student> getStudentsByGroup(String group) {
        return new ArrayList<>(byGroup.getOrDefault(group, new TreeSet<>()));
    }

    @Override
    public Set<String> getGroupsBySurname(String surname) {
        return groupsBySurname.getOrDefault(surname, Collections.emptySet());
    }

    @Override
    public Set<String> getAllPhones() { 
        return byPhone.keySet(); 
    }

    @Override
    public Set<String> getAllGroups() { 
        return byGroup.keySet(); 
    }

    @Override
    public Set<String> getAllSurnames() { 
        return groupsBySurname.keySet(); 
    }

    @Override
    public List<Student> getAllStudents() { 
        return new ArrayList<>(byPhone.values()); 
    }

    @Override
    public Student getStudentByPhone(String phone) {
        return byPhone.get(phone);
    }

    @Override
    public int size() {
        return byPhone.size();
    }
}