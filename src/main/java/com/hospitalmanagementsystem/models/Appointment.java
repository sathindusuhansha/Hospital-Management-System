package com.hospitalmanagementsystem.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * OOP: Encapsulation
 * Appointment model represents a hospital appointment.
 */
public class Appointment {

    private String appointmentId;

    private String patientId;
    private String patientName;

    private String doctorCategory;
    private String doctorId;
    private String doctorName;

    private String appointmentDate;
    private String appointmentTime;

    private String reason;
    private String status;
    private String createdBy;

    public Appointment() {
    }

    public Appointment(String appointmentId,
                       String patientId,
                       String patientName,
                       String doctorCategory,
                       String doctorId,
                       String doctorName,
                       String appointmentDate,
                       String appointmentTime,
                       String reason,
                       String status,
                       String createdBy) {

        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorCategory = doctorCategory;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.reason = reason;
        this.status = status;
        this.createdBy = createdBy;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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

    public String getDoctorCategory() {
        return doctorCategory;
    }

    public void setDoctorCategory(String doctorCategory) {
        this.doctorCategory = doctorCategory;
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

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    @JsonIgnore
    public String getAppointmentSummary() {
        return patientName + " appointment with " + doctorName +
                " on " + appointmentDate + " at " + appointmentTime;
    }
    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId='" + appointmentId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", doctorCategory='" + doctorCategory + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", appointmentDate='" + appointmentDate + '\'' +
                ", appointmentTime='" + appointmentTime + '\'' +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}