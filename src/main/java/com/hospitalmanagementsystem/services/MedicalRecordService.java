package com.hospitalmanagementsystem.services;

import com.hospitalmanagementsystem.models.GeneralMedicalRecord;
import com.hospitalmanagementsystem.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    // ================= GET ALL =================

    public List<GeneralMedicalRecord> getAllRecords() {
        return medicalRecordRepository.getAllRecords();
    }

    // ================= GET BY ID =================

    public GeneralMedicalRecord getRecordById(String recordId) {

        if (recordId == null || recordId.isEmpty()) {
            return null;
        }

        return medicalRecordRepository.getRecordById(recordId);
    }

    // ================= DOCTOR RECORDS =================

    public List<GeneralMedicalRecord> getRecordsByDoctorId(String doctorId) {

        if (doctorId == null || doctorId.isEmpty()) {
            return new ArrayList<>();
        }

        return medicalRecordRepository.getAllRecords()
                .stream()
                .filter(record ->
                        record.getDoctorId() != null &&
                                record.getDoctorId().trim()
                                        .equalsIgnoreCase(doctorId.trim()))
                .collect(Collectors.toList());
    }

    // ================= PATIENT RECORDS =================

    public List<GeneralMedicalRecord> getRecordsByPatientId(String patientId) {

        if (patientId == null || patientId.isEmpty()) {
            return new ArrayList<>();
        }

        return medicalRecordRepository.getAllRecords()
                .stream()
                .filter(record ->
                        record.getPatientId() != null &&
                                record.getPatientId().trim()
                                        .equalsIgnoreCase(patientId.trim()))
                .collect(Collectors.toList());
    }

    // ================= ADD =================

    public void addRecord(GeneralMedicalRecord record) {

        if (record == null) {
            return;
        }

        if (record.getRecordId() == null || record.getRecordId().isEmpty()) {
            record.setRecordId("MR-" + UUID.randomUUID()
                    .toString()
                    .substring(0, 6)
                    .toUpperCase());
        }

        if (record.getRecordDate() == null || record.getRecordDate().isEmpty()) {
            record.setRecordDate(java.time.LocalDate.now().toString());
        }

        if (record.getRecordType() == null || record.getRecordType().isEmpty()) {
            record.setRecordType("General");
        }

        if (record.getStatus() == null || record.getStatus().isEmpty()) {
            record.setStatus("ACTIVE");
        }

        medicalRecordRepository.addRecord(record);

        System.out.println("MEDICAL RECORD ADDED -> " + record.getRecordId());
    }

    // ================= UPDATE =================

    public void updateRecord(GeneralMedicalRecord updatedRecord) {

        if (updatedRecord == null) {
            return;
        }

        medicalRecordRepository.updateRecord(updatedRecord);

        System.out.println("MEDICAL RECORD UPDATED -> " + updatedRecord.getRecordId());
    }

    // ================= DELETE =================

    public void deleteRecord(String recordId) {

        medicalRecordRepository.deleteRecord(recordId);

        System.out.println("MEDICAL RECORD DELETED -> " + recordId);
    }
}