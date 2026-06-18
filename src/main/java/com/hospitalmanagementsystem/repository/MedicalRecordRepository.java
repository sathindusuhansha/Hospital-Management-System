package com.hospitalmanagementsystem.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospitalmanagementsystem.models.GeneralMedicalRecord;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MedicalRecordRepository {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    private static final String MEDICAL_RECORD_FILE ="src/main/resources/data/medical-records.json";
    // ================= READ ALL =================

    public List<GeneralMedicalRecord> getAllRecords() {

        try {
            File file = new File(MEDICAL_RECORD_FILE);

            if (!file.exists()) {
                File parent = file.getParentFile();
if (parent != null && !parent.exists()) {
    parent.mkdirs();
}
                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(file, new ArrayList<GeneralMedicalRecord>());
            }

            if (file.length() == 0) {
                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(file, new ArrayList<GeneralMedicalRecord>());
            }

            return objectMapper.readValue(
                    file,
                    new TypeReference<List<GeneralMedicalRecord>>() {}
            );

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ================= SAVE ALL =================

    public void saveRecords(List<GeneralMedicalRecord> records) {

        try {
            File file = new File(MEDICAL_RECORD_FILE);
            File parent = file.getParentFile();
if (parent != null && !parent.exists()) {
    parent.mkdirs();
}

            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, records);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= ADD =================

    public void addRecord(GeneralMedicalRecord record) {

        List<GeneralMedicalRecord> records = getAllRecords();
        records.add(record);
        saveRecords(records);

        System.out.println("MEDICAL RECORD ADDED = " + record.getRecordId());
    }

    // ================= GET BY ID =================

    public GeneralMedicalRecord getRecordById(String recordId) {

        if (recordId == null || recordId.isEmpty()) {
            return null;
        }

        List<GeneralMedicalRecord> records = getAllRecords();

        for (GeneralMedicalRecord record : records) {
            if (record.getRecordId() != null &&
                    record.getRecordId().trim().equalsIgnoreCase(recordId.trim())) {
                return record;
            }
        }

        return null;
    }

    // ================= UPDATE =================

    public void updateRecord(GeneralMedicalRecord updatedRecord) {

        if (updatedRecord == null || updatedRecord.getRecordId() == null) {
            return;
        }

        List<GeneralMedicalRecord> records = getAllRecords();

        for (int i = 0; i < records.size(); i++) {
            GeneralMedicalRecord existingRecord = records.get(i);

            if (existingRecord.getRecordId() != null &&
                    existingRecord.getRecordId().trim()
                            .equalsIgnoreCase(updatedRecord.getRecordId().trim())) {

                records.set(i, updatedRecord);
                saveRecords(records);

                System.out.println("MEDICAL RECORD UPDATED = " + updatedRecord.getRecordId());
                return;
            }
        }

        System.out.println("MEDICAL RECORD NOT FOUND FOR UPDATE = " + updatedRecord.getRecordId());
    }

    // ================= DELETE =================

    public void deleteRecord(String recordId) {

        if (recordId == null || recordId.isEmpty()) {
            return;
        }

        List<GeneralMedicalRecord> records = getAllRecords();

        boolean removed = records.removeIf(record ->
                record.getRecordId() != null &&
                        record.getRecordId().trim().equalsIgnoreCase(recordId.trim())
        );

        if (removed) {
            saveRecords(records);
            System.out.println("MEDICAL RECORD DELETED = " + recordId);
        } else {
            System.out.println("MEDICAL RECORD NOT FOUND = " + recordId);
        }
    }
}