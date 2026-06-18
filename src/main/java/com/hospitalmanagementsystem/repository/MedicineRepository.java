package com.hospitalmanagementsystem.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospitalmanagementsystem.models.MedicineOrder;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MedicineRepository {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final String FILE_PATH =
            "src/main/resources/data/medicine-orders.json";

    // ================= GET ALL =================

    public List<MedicineOrder> getAllOrders() {

        try {
            File file = new File(FILE_PATH);

            if (!file.exists()) {

                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(file, new ArrayList<MedicineOrder>());
            }

            if (file.length() == 0) {
                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(file, new ArrayList<MedicineOrder>());
            }

            return objectMapper.readValue(
                    file,
                    new TypeReference<List<MedicineOrder>>() {}
            );

        } catch (Exception e) {
            System.err.println("ERROR READING MEDICINE ORDERS");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ================= SAVE ALL =================

    public void saveAllOrders(List<MedicineOrder> orders) {

        try {
            File file = new File(FILE_PATH);

            File parent = file.getParentFile();
if (parent != null && !parent.exists()) {
    parent.mkdirs();
}

            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, orders);

            System.out.println("MEDICINE ORDERS SAVED: " + orders.size());

        } catch (Exception e) {
            System.err.println("ERROR SAVING MEDICINE ORDERS");
            e.printStackTrace();
        }
    }

    // ================= ADD =================

    public void addOrder(MedicineOrder order) {

        List<MedicineOrder> orders = getAllOrders();

        orders.add(order);

        saveAllOrders(orders);

        System.out.println("MEDICINE ORDER ADDED TO JSON: " + order.getOrderId());
    }

    // ================= GET BY ID =================

    public MedicineOrder getOrderById(String id) {

        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        return getAllOrders()
                .stream()
                .filter(order ->
                        order.getOrderId() != null &&
                                order.getOrderId().trim().equalsIgnoreCase(id.trim()))
                .findFirst()
                .orElse(null);
    }

    // ================= UPDATE =================

    public void updateOrder(MedicineOrder updatedOrder) {

        if (updatedOrder == null ||
                updatedOrder.getOrderId() == null ||
                updatedOrder.getOrderId().trim().isEmpty()) {

            System.out.println("MEDICINE ORDER UPDATE FAILED: INVALID ORDER");
            return;
        }

        List<MedicineOrder> orders = getAllOrders();

        for (int i = 0; i < orders.size(); i++) {

            MedicineOrder existingOrder = orders.get(i);

            if (existingOrder.getOrderId() != null &&
                    existingOrder.getOrderId().trim()
                            .equalsIgnoreCase(updatedOrder.getOrderId().trim())) {

                orders.set(i, updatedOrder);
                saveAllOrders(orders);

                System.out.println("MEDICINE ORDER UPDATED IN JSON: "
                        + updatedOrder.getOrderId());

                return;
            }
        }

        System.out.println("MEDICINE ORDER NOT FOUND FOR UPDATE: "
                + updatedOrder.getOrderId());
    }

    // ================= DELETE =================

    public void deleteOrder(String id) {

        if (id == null || id.trim().isEmpty()) {
            System.out.println("MEDICINE ORDER DELETE FAILED: INVALID ID");
            return;
        }

        List<MedicineOrder> orders = getAllOrders();

        boolean removed = orders.removeIf(order ->
                order.getOrderId() != null &&
                        order.getOrderId().trim().equalsIgnoreCase(id.trim()));

        if (removed) {
            saveAllOrders(orders);
            System.out.println("MEDICINE ORDER DELETED FROM JSON: " + id);
        } else {
            System.out.println("MEDICINE ORDER NOT FOUND FOR DELETE: " + id);
        }
    }

    // ================= PATIENT ORDERS =================

    public List<MedicineOrder> getOrdersByPatientId(String patientId) {

        if (patientId == null || patientId.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return getAllOrders()
                .stream()
                .filter(order ->
                        order.getPatientId() != null &&
                                order.getPatientId().trim()
                                        .equalsIgnoreCase(patientId.trim()))
                .collect(Collectors.toList());
    }

    // ================= DOCTOR ORDERS =================

    public List<MedicineOrder> getOrdersByDoctorId(String doctorId) {

        if (doctorId == null || doctorId.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return getAllOrders()
                .stream()
                .filter(order ->
                        order.getDoctorId() != null &&
                                order.getDoctorId().trim()
                                        .equalsIgnoreCase(doctorId.trim()))
                .collect(Collectors.toList());
    }
}