package com.hospitalmanagementsystem.controllers;

import com.hospitalmanagementsystem.models.MedicineOrder;
import com.hospitalmanagementsystem.services.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    // PHARMACIST VIEW
    @GetMapping("/pharmacist/medicine-orders")
    public String pharmacistOrders(Model model) {

        model.addAttribute(
                "orders",
                medicineService.getAllOrders()
        );

        return "pharmacist/medicine-orders";
    }
    @GetMapping("/pharmacist/dashboard")
    public String pharmacistDashboard(Model model) {
        model.addAttribute("totalOrders", medicineService.getAllOrders().size());
        return "pharmacist/dashboard";
    }

    // DOCTOR PAGE
    @GetMapping("/doctor/medicine")
    public String doctorMedicine(Model model) {

        model.addAttribute(
                "orders",
                medicineService.getAllOrders()
        );

        model.addAttribute(
                "medicineOrder",
                new MedicineOrder()
        );

        return "doctor/medicine";
    }

    // ADD MEDICINE
    @PostMapping("/doctor/add-medicine")
    public String addMedicine(
            @ModelAttribute MedicineOrder medicineOrder
    ) {

        medicineService.addOrder(medicineOrder);

        return "redirect:/doctor/medicine";
    }

    // DELETE
    @GetMapping("/doctor/delete-medicine/{id}")
    public String deleteMedicine(
            @PathVariable String id
    ) {

        medicineService.deleteOrder(id);

        return "redirect:/doctor/medicine";
    }
}