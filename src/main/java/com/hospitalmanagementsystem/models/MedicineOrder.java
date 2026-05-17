package com.hospitalmanagementsystem.models;

public class MedicineOrder {
    private String orderId;
    private String patientId;
    private String patientName;
    private String doctorName;
    private String medicineName;
    private String dosage;
    private String instructions;
    private String status;

    public MedicineOrder() {}

    public MedicineOrder(String orderId, String patientId, String patientName, String doctorName,
                         String medicineName, String dosage, String instructions, String status) {
        this.orderId = orderId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.instructions = instructions;
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}