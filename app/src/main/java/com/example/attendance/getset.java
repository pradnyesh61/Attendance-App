package com.example.attendance;

import java.util.ArrayList;
import java.util.List;

public class getset {

    private String name;
    private Long phn;
    private String email;
    private String password;
    private String department;

    private Long Student_count;
    private String Subject_name;
    private String Subject_department;
    private String Subject_sem;
    private String Subject_year;


    public getset() {
    }

    public Long getStudent_count() {
        return Student_count;
    }

    public void setStudent_count(Long student_count) {
        Student_count = student_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhn() {
        return phn;
    }

    public void setPhn(Long phn) {
        this.phn = phn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    public String getSubject_name() {
        return Subject_name;
    }

    public void setSubject_name(String subject_name) {
        Subject_name = subject_name;
    }

    public String getSubject_department() {
        return Subject_department;
    }

    public void setSubject_department(String subject_department) {
        Subject_department = subject_department;
    }

    public String getSubject_sem() {
        return Subject_sem;
    }

    public void setSubject_sem(String subject_sem) {
        Subject_sem = subject_sem;
    }

    public String getSubject_year() {
        return Subject_year;
    }

    public void setSubject_year(String subject_year) {
        Subject_year = subject_year;
    }
}
