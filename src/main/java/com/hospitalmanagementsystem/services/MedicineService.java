package com.hospitalmanagementsystem.services;

import com.hospitalmanagementsystem.models.MedicineOrder;
import com.hospitalmanagementsystem.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    // ================= GET ALL =================

    public List<MedicineOrder> getAllOrders() {
        return medicineRepository.getAllOrders();
    }

    // ================= GET BY ID =================

    public MedicineOrder getOrderById(String orderId) {
        return medicineRepository.getOrderById(orderId);
    }

    // ================= GET BY PATIENT =================

    public List<MedicineOrder> getOrdersByPatientId(String patientId) {
        return medicineRepository.getOrdersByPatientId(patientId);
    }

    // ================= GET BY DOCTOR =================

    public List<MedicineOrder> getOrdersByDoctorId(String doctorId) {
        return medicineRepository.getOrdersByDoctorId(doctorId);
    }

    // ================= ADD =================

    public void addOrder(MedicineOrder order) {

        if (order.getOrderId() == null ||
                order.getOrderId().isEmpty()) {

            order.setOrderId(
                    "MED-" +
                            UUID.randomUUID()
                                    .toString()
                                    .substring(0, 6)
                                    .toUpperCase()
            );
        }

        medicineRepository.addOrder(order);

        System.out.println(
                "MEDICINE ORDER ADDED: "
                        + order.getOrderId());
    }

    // ================= UPDATE =================

    public void updateOrder(MedicineOrder updatedOrder) {

        medicineRepository.updateOrder(updatedOrder);

        System.out.println(
                "MEDICINE ORDER UPDATED: "
                        + updatedOrder.getOrderId());
    }

    // ================= DELETE =================

    public void deleteOrder(String orderId) {

        medicineRepository.deleteOrder(orderId);

        System.out.println(
                "MEDICINE ORDER DELETED: "
                        + orderId);
    }
}