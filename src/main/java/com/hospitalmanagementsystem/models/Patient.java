package com.hospitalmanagementsystem.models;

/**
 * OOP: Encapsulation — all fields private, accessed only via getters/setters.
 * Patient model represents a patient in the hospital system.
 */
public class Patient {

    // ── Private fields ───────────────────────────────────────

    private String patientId;
    private String firstName;
    private String lastName;

    // Added for JSON compatibility
    private String fullName;

    private String email;
    private String username;
    private String password;
    private String phone;
    private String dateOfBirth;
    private String gender;
    private String address;
    private String city;
    private String medicalHistory;
    private String registrationDate;
    private Boolean isActive;

    // ── Constructors ─────────────────────────────────────────

    public Patient() {
    }

    public Patient(String patientId,
                   String firstName,
                   String lastName,
                   String email,
                   String username,
                   String password,
                   String phone,
                   String dateOfBirth,
                   String gender,
                   String address,
                   String city) {

        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.isActive = true;
    }

    // ── Getters & Setters ────────────────────────────────────

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateFullName();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateFullName();
    }

    public String getFullName() {

        if (fullName != null && !fullName.isBlank()) {
            return fullName;
        }

        String first = firstName == null ? "" : firstName;
        String last = lastName == null ? "" : lastName;

        return (first + " " + last).trim();
    }

    public void setFullName(String fullName) {

        this.fullName = fullName;

        if (fullName != null && fullName.contains(" ")) {

            String[] parts = fullName.trim().split("\\s+", 2);

            this.firstName = parts[0];

            if (parts.length > 1) {
                this.lastName = parts[1];
            }
        }
    }

    private void updateFullName() {

        String first = firstName == null ? "" : firstName;
        String last = lastName == null ? "" : lastName;

        this.fullName = (first + " " + last).trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}