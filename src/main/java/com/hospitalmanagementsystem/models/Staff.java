package com.hospitalmanagementsystem.models;

public class Staff extends User {
    private String department;
    private String specialization;
    private String qualification;

    public Staff() {}

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
}