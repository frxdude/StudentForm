/**
 * Author_code: B180910040
 * Author_name: I.Sainjargal
 * Created_Date&Time: 2021/1/2 23:06
 * Last_Modified_Date&Time: 2021/1/3 03:43
 * Lab: 2-5
 */
package com.CS314.Lab2.Model;

import javax.persistence.*;

@Entity
@Table(name = "Students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String major;
    private String year;
    private String name;
    private String code;
    private String lab;

    public Student(long id, String major, String year, String name, String code, String lab) {
        this.id = id;
        this.major = major;
        this.year = year;
        this.name = name;
        this.code = code;
        this.lab = lab;
    }

    public Student() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }
}
