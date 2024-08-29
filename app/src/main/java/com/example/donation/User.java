package com.example.donation;

public class User {
    private String name;
    private String email;
    private int age;
    private String bloodGroup;

    public User(String name, String email, int age, String bloodGroup) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.bloodGroup = bloodGroup;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getAge() { return age; }
    public String getBloodGroup() { return bloodGroup; }
}
