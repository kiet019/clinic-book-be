package com.c1se62.clinic_booking.service.MedicineServices;

import com.c1se62.clinic_booking.dto.request.MedicineCreatedDTO;
import com.c1se62.clinic_booking.dto.request.MedicineUpdatedDTO;
import com.c1se62.clinic_booking.dto.response.MedicineResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MedicineService {
    MedicineResponseDTO createMedicine(MedicineCreatedDTO medicineCreatedDTO);
    MedicineResponseDTO getMedicineById(Integer id);
    Page<MedicineResponseDTO> getAllMedicines(int pageNo, int pageSize, String sortBy, String sortDir);
    MedicineResponseDTO updateMedicine(Integer id, MedicineUpdatedDTO medicine);
    void deleteMedicine(Integer id);
    MedicineResponseDTO updateMedicineStock(Integer id, Integer quantity);
    List<MedicineResponseDTO> getMedicinesByIds(List<Integer> ids);
    List<MedicineResponseDTO> getLowStockMedicines(Integer threshold);
    List<MedicineResponseDTO> searchMedicinesByName(String name);
}
