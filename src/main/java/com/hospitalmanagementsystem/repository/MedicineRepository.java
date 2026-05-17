package com.hospitalmanagementsystem.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospitalmanagementsystem.models.MedicineOrder;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MedicineRepository {

    private static final String FILE_PATH = "data/medicine-orders.json";

    private final ObjectMapper objectMapper = new ObjectMapper();

    // READ ALL MEDICINE ORDERS
    public List<MedicineOrder> getAllOrders() {

        try {

            File file = new File(FILE_PATH);

            if (!file.exists()) {
                return new ArrayList<>();
            }

            return objectMapper.readValue(
                    file,
                    new TypeReference<List<MedicineOrder>>() {}
            );

        } catch (Exception e) {

            e.printStackTrace();

            return new ArrayList<>();
        }
    }

    // SAVE ALL MEDICINE ORDERS
    public void saveAllOrders(List<MedicineOrder> orders) {

        try {

            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(
                            new File(FILE_PATH),
                            orders
                    );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}