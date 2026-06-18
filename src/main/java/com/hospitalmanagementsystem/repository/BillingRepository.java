package com.hospitalmanagementsystem.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospitalmanagementsystem.models.Bill;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**

 * BillingRepository manages

 * reading and writing bill data

 * from JSON storage.

 */
@Repository
public class BillingRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String FILE_PATH =
            "/Users/dayanitharmarajahd/Documents/Hospital-Management-System (2)/Hospital-Management-System/src/main/resources/data/bills.json";

    public BillingRepository() {
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false
        );
    }

    // ================= GET ALL =================

    public List<Bill> getAllBills() {

        try {
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                File parent = file.getParentFile();

                if (parent != null) {
                    parent.mkdirs();
                }
                saveAllBills(new ArrayList<>());
            }

            if (file.length() == 0) {
                saveAllBills(new ArrayList<>());
            }

            return objectMapper.readValue(
                    file,
                    new TypeReference<List<Bill>>() {}
            );

        } catch (Exception e) {
            System.err.println("ERROR READING BILLS");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ================= SAVE ALL =================

    public void saveAllBills(List<Bill> bills) {

        try {
            File file = new File(FILE_PATH);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, bills);

        } catch (Exception e) {
            System.err.println("ERROR SAVING BILLS");
            e.printStackTrace();
        }
    }

    // ================= ADD =================

    public void addBill(Bill bill) {

        List<Bill> bills = getAllBills();
        bills.add(bill);
        saveAllBills(bills);
    }

    // ================= GET BY ID =================

    public Bill getBillById(String billId) {

        if (billId == null || billId.isEmpty()) {
            return null;
        }

        return getAllBills()
                .stream()
                .filter(bill ->
                        bill.getBillId() != null &&
                                bill.getBillId().equalsIgnoreCase(billId))
                .findFirst()
                .orElse(null);
    }

    // ================= UPDATE =================

    public void updateBill(Bill updatedBill) {

        List<Bill> bills = getAllBills();

        for (int i = 0; i < bills.size(); i++) {

            Bill existingBill = bills.get(i);

            if (existingBill.getBillId() != null &&
                    existingBill.getBillId().equalsIgnoreCase(updatedBill.getBillId())) {

                bills.set(i, updatedBill);
                saveAllBills(bills);
                return;
            }
        }
    }

    // ================= DELETE =================

    public void deleteBill(String billId) {

        List<Bill> bills = getAllBills();

        bills.removeIf(bill ->
                bill.getBillId() != null &&
                        bill.getBillId().equalsIgnoreCase(billId));

        saveAllBills(bills);
    }

    // ================= PATIENT BILLS =================

    public List<Bill> getBillsByPatientId(String patientId) {

        if (patientId == null || patientId.isEmpty()) {
            return new ArrayList<>();
        }

        return getAllBills()
                .stream()
                .filter(bill ->
                        bill.getPatientId() != null &&
                                bill.getPatientId().equalsIgnoreCase(patientId))
                .collect(Collectors.toList());
    }

    // ================= PENDING BILLS BY PATIENT =================

    public List<Bill> getPendingBillsByPatientId(String patientId) {

        return getBillsByPatientId(patientId)
                .stream()
                .filter(bill ->
                        bill.getPaymentStatus() == null ||
                                bill.getPaymentStatus().equalsIgnoreCase("Pending"))
                .collect(Collectors.toList());
    }

    // ================= MARK BILL AS PAID =================

    public void markBillAsPaid(String billId, String paymentMethod) {

        Bill bill = getBillById(billId);

        if (bill == null) {
            return;
        }

        bill.setPaymentStatus("Paid");

        if (paymentMethod == null || paymentMethod.isEmpty()) {
            bill.setPaymentMethod("Online");
        } else {
            bill.setPaymentMethod(paymentMethod);
        }

        bill.setPaymentDate(LocalDate.now().toString());

        updateBill(bill);
    }
}