package com.hospitalmanagementsystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WardRoom {

    private String wardId;
    private String patientId;
    private String patientName;

    private String nurseId;
    private String nurseName;

    private String wardName;
    private String wardType;
    private String roomNumber;
    private String bedNumber;

    private int capacity;
    private int occupiedBeds;

    private String admissionDate;
    private String dischargeDate;

    private String status;
    private String notes;

    public WardRoom() {
    }

    public WardRoom(String wardId, String patientId, String patientName,
                    String nurseId, String nurseName,
                    String wardName, String wardType,
                    String roomNumber, String bedNumber,
                    int capacity, int occupiedBeds,
                    String admissionDate, String dischargeDate,
                    String status, String notes) {

        this.wardId = wardId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.nurseId = nurseId;
        this.nurseName = nurseName;
        this.wardName = wardName;
        this.wardType = wardType;
        this.roomNumber = roomNumber;
        this.bedNumber = bedNumber;
        this.capacity = capacity;
        this.occupiedBeds = occupiedBeds;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.status = status;
        this.notes = notes;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
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

    public String getNurseId() {
        return nurseId;
    }

    public void setNurseId(String nurseId) {
        this.nurseId = nurseId;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getWardType() {
        return wardType;
    }

    public void setWardType(String wardType) {
        this.wardType = wardType;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupiedBeds() {
        return occupiedBeds;
    }

    public void setOccupiedBeds(int occupiedBeds) {
        this.occupiedBeds = occupiedBeds;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @JsonIgnore
    public boolean isAvailable() {
        return capacity > occupiedBeds;
    }

    @JsonIgnore
    public String getOccupancyStatus() {
        if (capacity <= 0) {
            return "Invalid Capacity";
        }

        if (occupiedBeds >= capacity) {
            return "Full";
        }

        if (occupiedBeds == 0) {
            return "Empty";
        }

        return "Available";
    }
}