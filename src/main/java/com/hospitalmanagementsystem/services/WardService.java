package com.hospitalmanagementsystem.services;

import com.hospitalmanagementsystem.models.WardRoom;
import com.hospitalmanagementsystem.repository.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WardService {

    @Autowired
    private WardRepository wardRepository;

    // ================= GET ALL =================

    public List<WardRoom> getAllWardRooms() {
        return wardRepository.getAllWardRooms();
    }

    // ================= GET BY ID =================

    public WardRoom getWardRoomById(String wardId) {
        return wardRepository.getWardRoomById(wardId);
    }

    // ================= GET ACTIVE ADMISSIONS =================

    public List<WardRoom> getActiveAdmissions() {
        return wardRepository.getActiveAdmissions();
    }

    // ================= ADD / ADMIT PATIENT =================

    public void addWardRoom(WardRoom wardRoom) {

        if (wardRoom.getWardId() == null || wardRoom.getWardId().isEmpty()) {
            wardRoom.setWardId("WARD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        }

        if (wardRoom.getAdmissionDate() == null || wardRoom.getAdmissionDate().isEmpty()) {
            wardRoom.setAdmissionDate(LocalDate.now().toString());
        }

        if (wardRoom.getStatus() == null || wardRoom.getStatus().isEmpty()) {
            wardRoom.setStatus("Admitted");
        }

        if (wardRoom.getCapacity() <= 0) {
            wardRoom.setCapacity(1);
        }

        if (wardRoom.getOccupiedBeds() <= 0) {
            wardRoom.setOccupiedBeds(1);
        }

        wardRepository.addWardRoom(wardRoom);

        System.out.println("WARD PATIENT ADMITTED -> " + wardRoom.getWardId());
    }

    // ================= UPDATE =================

    public void updateWardRoom(WardRoom updatedWardRoom) {

        WardRoom existingWardRoom =
                wardRepository.getWardRoomById(updatedWardRoom.getWardId());

        if (existingWardRoom == null) {
            return;
        }

        if (updatedWardRoom.getAdmissionDate() == null ||
                updatedWardRoom.getAdmissionDate().isEmpty()) {
            updatedWardRoom.setAdmissionDate(existingWardRoom.getAdmissionDate());
        }

        if (updatedWardRoom.getStatus() == null ||
                updatedWardRoom.getStatus().isEmpty()) {
            updatedWardRoom.setStatus(existingWardRoom.getStatus());
        }

        if (updatedWardRoom.getCapacity() <= 0) {
            updatedWardRoom.setCapacity(existingWardRoom.getCapacity());
        }

        if (updatedWardRoom.getOccupiedBeds() < 0) {
            updatedWardRoom.setOccupiedBeds(existingWardRoom.getOccupiedBeds());
        }

        wardRepository.updateWardRoom(updatedWardRoom);

        System.out.println("WARD RECORD UPDATED -> " + updatedWardRoom.getWardId());
    }

    // ================= DELETE =================

    public void deleteWardRoom(String wardId) {
        wardRepository.deleteWardRoom(wardId);
        System.out.println("WARD RECORD DELETED -> " + wardId);
    }

    // ================= DISCHARGE PATIENT =================

    public void dischargePatient(String wardId) {

        WardRoom wardRoom = wardRepository.getWardRoomById(wardId);

        if (wardRoom == null) {
            return;
        }

        wardRoom.setStatus("Discharged");
        wardRoom.setDischargeDate(LocalDate.now().toString());

        if (wardRoom.getOccupiedBeds() > 0) {
            wardRoom.setOccupiedBeds(wardRoom.getOccupiedBeds() - 1);
        }

        wardRepository.updateWardRoom(wardRoom);

        System.out.println("PATIENT DISCHARGED FROM WARD -> " + wardId);
    }

    // ================= FILTER BY PATIENT =================

    public List<WardRoom> getWardRoomsByPatientId(String patientId) {
        return wardRepository.getWardRoomsByPatientId(patientId);
    }

    // ================= FILTER BY ROOM =================

    public List<WardRoom> getWardRoomsByRoomNumber(String roomNumber) {
        return wardRepository.getWardRoomsByRoomNumber(roomNumber);
    }

    // ================= DASHBOARD COUNTS =================

    public int getTotalWardRecordsCount() {
        return wardRepository.getAllWardRooms().size();
    }

    public int getActiveAdmissionsCount() {
        return wardRepository.getActiveAdmissions().size();
    }

    public int getDischargedCount() {
        return (int) wardRepository.getAllWardRooms()
                .stream()
                .filter(ward ->
                        ward.getStatus() != null &&
                                ward.getStatus().equalsIgnoreCase("Discharged"))
                .count();
    }

    public int getAvailableBedsCount() {

        int totalAvailable = 0;

        for (WardRoom ward : wardRepository.getAllWardRooms()) {
            int available = ward.getCapacity() - ward.getOccupiedBeds();

            if (available > 0) {
                totalAvailable += available;
            }
        }

        return totalAvailable;
    }

    // ================= STATUS FILTERS =================

    public List<WardRoom> getWardRoomsByStatus(String status) {

        return wardRepository.getAllWardRooms()
                .stream()
                .filter(ward ->
                        ward.getStatus() != null &&
                                ward.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
}