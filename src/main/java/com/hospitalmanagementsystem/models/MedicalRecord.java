package com.hospitalmanagementsystem.models;

/**
 * OOP: Abstraction + Encapsulation
 * MedicalRecord is an abstract base class for patient medical records.
 */
public abstract class MedicalRecord {

    private String recordId;

    private String patientId;
    private String patientName;

    private String doctorId;
    private String doctorName;

    private String recordDate;
    private String recordType;

    private String diagnosis;
    private String prescription;
    private String notes;

    private String status;

    public MedicalRecord() {
    }

    public MedicalRecord(String recordId,
                         String patientId,
                         String patientName,
                         String doctorId,
                         String doctorName,
                         String recordDate,
                         String recordType,
                         String diagnosis,
                         String prescription,
                         String notes,
                         String status) {

        this.recordId = recordId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.recordDate = recordDate;
        this.recordType = recordType;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.notes = notes;
        this.status = status;
    }

    public abstract String generateSummary();

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}