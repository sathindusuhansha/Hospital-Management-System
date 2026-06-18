package com.hospitalmanagementsystem.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospitalmanagementsystem.models.WardRoom;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class WardRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String FILE_PATH =
            "src/main/resources/data/ward-rooms.json";

    // ================= GET ALL WARD ROOMS =================

    public List<WardRoom> getAllWardRooms() {

        try {

            File file = new File(FILE_PATH);

            if (!file.exists()) {

                File parent = file.getParentFile();
if (parent != null && !parent.exists()) {
    parent.mkdirs();
}

                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(file, new ArrayList<WardRoom>());
            }

            if (file.length() == 0) {

                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(file, new ArrayList<WardRoom>());
            }

            System.out.println("READING WARD ROOMS FROM:");
            System.out.println(FILE_PATH);

            List<WardRoom> wards = objectMapper.readValue(
                    file,
                    new TypeReference<List<WardRoom>>() {}
            );

            System.out.println("TOTAL WARD RECORDS LOADED = " + wards.size());

            return wards;

        } catch (Exception e) {

            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ================= SAVE ALL =================

    public void saveAllWardRooms(List<WardRoom> wardRooms) {

        try {

            File file = new File(FILE_PATH);

            File parent = file.getParentFile();
if (parent != null && !parent.exists()) {
    parent.mkdirs();
}

            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, wardRooms);

            System.out.println("WARD ROOMS SAVED TO:");
            System.out.println(FILE_PATH);

            System.out.println("TOTAL WARD RECORDS SAVED = " + wardRooms.size());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ================= ADD =================

    public void addWardRoom(WardRoom wardRoom) {

        List<WardRoom> wardRooms = getAllWardRooms();

        wardRooms.add(wardRoom);

        saveAllWardRooms(wardRooms);

        System.out.println("WARD ADMISSION ADDED = " + wardRoom.getWardId());
    }

    // ================= GET BY ID =================

    public WardRoom getWardRoomById(String wardId) {

        return getAllWardRooms()
                .stream()
                .filter(ward ->
                        ward.getWardId() != null &&
                                ward.getWardId().equalsIgnoreCase(wardId))
                .findFirst()
                .orElse(null);
    }

    // ================= UPDATE =================

    public void updateWardRoom(WardRoom updatedWard) {

        List<WardRoom> wardRooms = getAllWardRooms();

        for (int i = 0; i < wardRooms.size(); i++) {

            WardRoom existingWard = wardRooms.get(i);

            if (existingWard.getWardId() != null &&
                    existingWard.getWardId().equalsIgnoreCase(updatedWard.getWardId())) {

                wardRooms.set(i, updatedWard);

                saveAllWardRooms(wardRooms);

                System.out.println("WARD UPDATED = " + updatedWard.getWardId());

                return;
            }
        }
    }

    // ================= DELETE =================

    public void deleteWardRoom(String wardId) {

        List<WardRoom> wardRooms = getAllWardRooms();

        wardRooms.removeIf(ward ->
                ward.getWardId() != null &&
                        ward.getWardId().equalsIgnoreCase(wardId));

        saveAllWardRooms(wardRooms);

        System.out.println("WARD DELETED = " + wardId);
    }

    // ================= GET BY PATIENT =================

    public List<WardRoom> getWardRoomsByPatientId(String patientId) {

        return getAllWardRooms()
                .stream()
                .filter(ward ->
                        ward.getPatientId() != null &&
                                ward.getPatientId().equalsIgnoreCase(patientId))
                .collect(Collectors.toList());
    }

    // ================= GET ACTIVE PATIENTS =================

    public List<WardRoom> getActiveAdmissions() {

        return getAllWardRooms()
                .stream()
                .filter(ward ->
                        ward.getStatus() != null &&
                                ward.getStatus().equalsIgnoreCase("Admitted"))
                .collect(Collectors.toList());
    }

    // ================= GET BY ROOM =================

    public List<WardRoom> getWardRoomsByRoomNumber(String roomNumber) {

        return getAllWardRooms()
                .stream()
                .filter(ward ->
                        ward.getRoomNumber() != null &&
                                ward.getRoomNumber().equalsIgnoreCase(roomNumber))
                .collect(Collectors.toList());
    }
}