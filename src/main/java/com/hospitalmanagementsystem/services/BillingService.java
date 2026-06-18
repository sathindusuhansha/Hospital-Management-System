package com.hospitalmanagementsystem.services;

import com.hospitalmanagementsystem.models.Bill;
import com.hospitalmanagementsystem.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    // ================= GET ALL BILLS =================

    public List<Bill> getAllBills() {
        return billingRepository.getAllBills();
    }

    // ================= GET BILL BY ID =================

    public Bill getBillById(String billId) {
        return billingRepository.getBillById(billId);
    }

    // ================= GET PATIENT BILLS =================

    public List<Bill> getBillsByPatientId(String patientId) {
        return billingRepository.getBillsByPatientId(patientId);
    }

    // ================= GET PATIENT PENDING BILLS =================

    public List<Bill> getPendingBillsByPatientId(String patientId) {
        return billingRepository.getPendingBillsByPatientId(patientId);
    }

    // ================= ADD BILL =================

    public void addBill(Bill bill) {

        if (bill.getBillId() == null || bill.getBillId().isEmpty()) {
            bill.setBillId(
                    "BILL-" +
                            UUID.randomUUID()
                                    .toString()
                                    .substring(0, 6)
                                    .toUpperCase()
            );
        }

        if (bill.getBillDate() == null || bill.getBillDate().isEmpty()) {
            bill.setBillDate(LocalDate.now().toString());
        }

        if (bill.getPaymentStatus() == null || bill.getPaymentStatus().isEmpty()) {
            bill.setPaymentStatus("Pending");
        }

        if (bill.getPaymentMethod() == null || bill.getPaymentMethod().isEmpty()) {
            bill.setPaymentMethod("Not Paid");
        }

        bill.setTotalAmount(calculateTotalAmount(bill));

        billingRepository.addBill(bill);

        System.out.println("BILL ADDED -> " + bill.getBillId());
        System.out.println("PATIENT -> " + bill.getPatientName());
    }

    // ================= UPDATE BILL =================

    public void updateBill(Bill updatedBill) {

        Bill existingBill =
                billingRepository.getBillById(updatedBill.getBillId());

        if (existingBill == null) {
            return;
        }

        if (updatedBill.getBillDate() == null ||
                updatedBill.getBillDate().isEmpty()) {

            updatedBill.setBillDate(existingBill.getBillDate());
        }

        if (updatedBill.getPaymentStatus() == null ||
                updatedBill.getPaymentStatus().isEmpty()) {

            updatedBill.setPaymentStatus(
                    existingBill.getPaymentStatus()
            );
        }

        if (updatedBill.getPaymentMethod() == null ||
                updatedBill.getPaymentMethod().isEmpty()) {

            updatedBill.setPaymentMethod(
                    existingBill.getPaymentMethod()
            );
        }

        if (updatedBill.getPaymentDate() == null ||
                updatedBill.getPaymentDate().isEmpty()) {

            updatedBill.setPaymentDate(
                    existingBill.getPaymentDate()
            );
        }

        updatedBill.setTotalAmount(
                calculateTotalAmount(updatedBill)
        );

        billingRepository.updateBill(updatedBill);

        System.out.println(
                "BILL UPDATED -> " +
                        updatedBill.getBillId()
        );
    }

    // ================= DELETE BILL =================

    public void deleteBill(String billId) {

        billingRepository.deleteBill(billId);

        System.out.println(
                "BILL DELETED -> " +
                        billId
        );
    }

    // ================= PATIENT PAYMENT =================

    public void markBillAsPaid(String billId,
                               String paymentMethod) {

        Bill bill = billingRepository.getBillById(billId);

        if (bill == null) {
            return;
        }

        bill.setPaymentStatus("Paid");

        if (paymentMethod == null ||
                paymentMethod.isEmpty()) {

            paymentMethod = "Online";
        }

        bill.setPaymentMethod(paymentMethod);

        bill.setPaymentDate(
                LocalDate.now().toString()
        );

        billingRepository.updateBill(bill);

        System.out.println(
                "PAYMENT SUCCESS -> " +
                        billId
        );
    }

    // ================= CHECK IF PAID =================

    public boolean isBillPaid(String billId) {

        Bill bill = billingRepository.getBillById(billId);

        if (bill == null) {
            return false;
        }

        return bill.getPaymentStatus() != null &&
                bill.getPaymentStatus()
                        .equalsIgnoreCase("Paid");
    }

    // ================= TOTAL CALCULATION =================

    private double calculateTotalAmount(Bill bill) {

        return bill.getConsultationFee()
                + bill.getMedicineFee()
                + bill.getRoomFee()
                + bill.getLabFee()
                + bill.getOtherFee();
    }
}