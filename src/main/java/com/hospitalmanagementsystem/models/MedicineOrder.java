package com.hospitalmanagementsystem.models;

public class MedicineOrder {

    private String orderId;

    private String patientId;
    private String patientName;

    private String doctorId;
    private String doctorName;

    private String medicineName;
    private String dosage;
    private String frequency;
    private String duration;

    private String instructions;

    private String status;
    private String orderDate;

    // ================= DEFAULT CONSTRUCTOR =================

    public MedicineOrder() {
    }

    // ================= FULL CONSTRUCTOR =================

    public MedicineOrder(
            String orderId,
            String patientId,
            String patientName,
            String doctorId,
            String doctorName,
            String medicineName,
            String dosage,
            String frequency,
            String duration,
            String instructions,
            String status,
            String orderDate) {

        this.orderId = orderId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.instructions = instructions;
        this.status = status;
        this.orderDate = orderDate;
    }

    // ================= GETTERS & SETTERS =================

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}