package com.example.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.example.model.Student;

public class HeapStudentDB implements StudentStorage {
    private final PriorityQueue<Student> heap = new PriorityQueue<>(
        Comparator.comparing(Student::getGroup)
                  .thenComparing(Student::getSurname)
                  .thenComparing(Student::getName)
    );

    @Override
    public void addStudent(Student s) {
        heap.add(s);
    }

    @Override
    public void changeGroup(String phone, String newGroup) {
        List<Student> tmp = new ArrayList<>(heap);
        heap.clear();
        for (Student s : tmp) {
            if (s.getPhoneNumber().equals(phone)) s.setGroup(newGroup);
            heap.add(s);
        }
    }

    @Override
    public List<Student> getStudentsByGroup(String group) {
        List<Student> res = new ArrayList<>();
        for (Student s : heap) {
            if (s.getGroup().equals(group)) res.add(s);
        }
        res.sort(Comparator.comparing(Student::getSurname).thenComparing(Student::getName));
        return res;
    }

    @Override
    public Set<String> getGroupsBySurname(String surname) {
        Set<String> res = new HashSet<>();
        for (Student s : heap) {
            if (s.getSurname().equals(surname)) res.add(s.getGroup());
        }
        return res;
    }

    @Override
    public Set<String> getAllPhones() {
        Set<String> res = new HashSet<>();
        for (Student s : heap) res.add(s.getPhoneNumber());
        return res;
    }

    @Override
    public Set<String> getAllGroups() {
        Set<String> res = new HashSet<>();
        for (Student s : heap) res.add(s.getGroup());
        return res;
    }

    @Override
    public Set<String> getAllSurnames() {
        Set<String> res = new HashSet<>();
        for (Student s : heap) res.add(s.getSurname());
        return res;
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(heap);
    }

    @Override
    public Student getStudentByPhone(String phone) {
        for (Student s : heap) {
            if (s.getPhoneNumber().equals(phone)) return s;
        }
        return null;
    }

    @Override
    public int size() {
        return heap.size();
    }
}