package com.example.model;

public class Student {
    private String name;
    private String surname;
    private String email;
    private int birthYear;
    private int birthMonth;
    private int birthDay;
    private String group;
    private float rating;
    private String phoneNumber;

    public Student(String name, String surname, String email, int birthYear,
                   int birthMonth, int birthDay, String group, float rating, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.group = group;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
    }

    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getEmail() { return email; }
    public int getBirthYear() { return birthYear; }
    public int getBirthMonth() { return birthMonth; }
    public int getBirthDay() { return birthDay; }
    public String getGroup() { return group; }
    public float getRating() { return rating; }
    public String getPhoneNumber() { return phoneNumber; }

    public void setGroup(String group) { this.group = group; }

    @Override
    public String toString() {
        return surname + " " + name + " (" + group + ")";
    }
}
