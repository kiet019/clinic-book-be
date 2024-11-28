package com.c1se62.clinic_booking.controller;

import com.c1se62.clinic_booking.dto.request.MedicineCreatedDTO;
import com.c1se62.clinic_booking.dto.request.MedicineUpdatedDTO;
import com.c1se62.clinic_booking.dto.response.MedicineResponseDTO;
import com.c1se62.clinic_booking.service.MedicineServices.MedicineService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicines")
@AllArgsConstructor
public class MedicineController {
    MedicineService medicineService;

    @PostMapping
    public ResponseEntity<MedicineResponseDTO> createMedicine(@Valid @RequestBody MedicineCreatedDTO medicineDTO) {
        MedicineResponseDTO response = medicineService.createMedicine(medicineDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMedicines(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "medicineId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Page<MedicineResponseDTO> medicines = medicineService.getAllMedicines(pageNo, pageSize, sortBy, sortDir);
        
        Map<String, Object> response = new HashMap<>();
        response.put("medicines", medicines.getContent());
        response.put("currentPage", medicines.getNumber());
        response.put("totalItems", medicines.getTotalElements());
        response.put("totalPages", medicines.getTotalPages());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponseDTO> getMedicineById(@PathVariable Integer id) {
        return ResponseEntity.ok(medicineService.getMedicineById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineResponseDTO> updateMedicine(
            @PathVariable Integer id,
            @Valid @RequestBody MedicineUpdatedDTO medicineDTO) {
        return ResponseEntity.ok(medicineService.updateMedicine(id, medicineDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Integer id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<MedicineResponseDTO> updateMedicineStock(
            @PathVariable Integer id,
            @RequestParam @Min(0) Integer quantity) {
        return ResponseEntity.ok(medicineService.updateMedicineStock(id, quantity));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<MedicineResponseDTO>> getLowStockMedicines(
            @RequestParam(defaultValue = "10") Integer threshold) {
        return ResponseEntity.ok(medicineService.getLowStockMedicines(threshold));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MedicineResponseDTO>> searchMedicines(
            @RequestParam String name) {
        return ResponseEntity.ok(medicineService.searchMedicinesByName(name));
    }
} 