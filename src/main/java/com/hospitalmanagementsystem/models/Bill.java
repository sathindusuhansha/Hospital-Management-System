package com.hospitalmanagementsystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Bill {

    private String billId;
    private String patientId;
    private String patientName;

    private String billDate;
    private String serviceName;
    private String description;

    private double consultationFee;
    private double medicineFee;
    private double roomFee;
    private double labFee;
    private double otherFee;
    private double totalAmount;

    private String paymentStatus;   // Pending, Paid, Cancelled
    private String paymentMethod;   // Cash, Card, Online, Not Paid
    private String paymentDate;

    private String createdBy;

    public Bill() {
    }

    public Bill(String billId, String patientId, String patientName, String billDate,
                String serviceName, String description, double consultationFee,
                double medicineFee, double roomFee, double labFee, double otherFee,
                double totalAmount, String paymentStatus, String paymentMethod,
                String paymentDate, String createdBy) {

        this.billId = billId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.billDate = billDate;
        this.serviceName = serviceName;
        this.description = description;
        this.consultationFee = consultationFee;
        this.medicineFee = medicineFee;
        this.roomFee = roomFee;
        this.labFee = labFee;
        this.otherFee = otherFee;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.createdBy = createdBy;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
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

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public double getMedicineFee() {
        return medicineFee;
    }

    public void setMedicineFee(double medicineFee) {
        this.medicineFee = medicineFee;
    }

    public double getRoomFee() {
        return roomFee;
    }

    public void setRoomFee(double roomFee) {
        this.roomFee = roomFee;
    }

    public double getLabFee() {
        return labFee;
    }

    public void setLabFee(double labFee) {
        this.labFee = labFee;
    }

    public double getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(double otherFee) {
        this.otherFee = otherFee;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    @JsonIgnore
    public String getStatusDisplay() {
        if (paymentStatus == null || paymentStatus.isEmpty()) {
            return "Pending Payment";
        }

        switch (paymentStatus.toUpperCase()) {
            case "PAID":
                return "Payment Completed";
            case "CANCELLED":
                return "Bill Cancelled";
            default:
                return "Payment Pending";
        }
    }
}