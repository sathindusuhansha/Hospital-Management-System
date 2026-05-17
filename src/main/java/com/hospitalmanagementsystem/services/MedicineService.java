package com.hospitalmanagementsystem.services;

import com.hospitalmanagementsystem.models.MedicineOrder;
import com.hospitalmanagementsystem.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    // GET ALL
    public List<MedicineOrder> getAllOrders() {

        return medicineRepository.getAllOrders();
    }

    // ADD
    public void addOrder(MedicineOrder order) {

        List<MedicineOrder> orders =
                medicineRepository.getAllOrders();

        orders.add(order);

        medicineRepository.saveAllOrders(orders);
    }

    // DELETE
    public void deleteOrder(String orderId) {

        List<MedicineOrder> orders =
                medicineRepository.getAllOrders();

        orders.removeIf(order ->
                order.getOrderId().equals(orderId));

        medicineRepository.saveAllOrders(orders);
    }
}