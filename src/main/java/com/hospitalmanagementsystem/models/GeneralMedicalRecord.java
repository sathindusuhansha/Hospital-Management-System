package com.hospitalmanagementsystem.models;

/**
 * OOP: Inheritance + Polymorphism
 * GeneralMedicalRecord extends MedicalRecord
 * and provides its own implementation of generateSummary().
 */
public class GeneralMedicalRecord extends MedicalRecord {

    public GeneralMedicalRecord() {
        super();
    }

    @Override
    public String generateSummary() {

        return "Medical Record Summary | " +
                "Patient: " + getPatientName() +
                " | Doctor: " + getDoctorName() +
                " | Diagnosis: " + getDiagnosis() +
                " | Prescription: " + getPrescription();
    }
    public String getSummary() {
        return generateSummary();
    }
    @Override
    public String toString() {

        return "GeneralMedicalRecord{" +
                "recordId='" + getRecordId() + '\'' +
                ", patientId='" + getPatientId() + '\'' +
                ", patientName='" + getPatientName() + '\'' +
                ", doctorId='" + getDoctorId() + '\'' +
                ", doctorName='" + getDoctorName() + '\'' +
                ", recordDate='" + getRecordDate() + '\'' +
                ", recordType='" + getRecordType() + '\'' +
                ", diagnosis='" + getDiagnosis() + '\'' +
                ", prescription='" + getPrescription() + '\'' +
                ", notes='" + getNotes() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}