package com.c1se62.clinic_booking.service.MedicineServices;

import com.c1se62.clinic_booking.dto.request.MedicineCreatedDTO;
import com.c1se62.clinic_booking.dto.request.MedicineUpdatedDTO;
import com.c1se62.clinic_booking.dto.response.MedicineResponseDTO;
import com.c1se62.clinic_booking.entity.Medicine;
import com.c1se62.clinic_booking.entity.Prescription;
import com.c1se62.clinic_booking.exception.APIException;
import com.c1se62.clinic_booking.exception.ResourceNotFoundException;
import com.c1se62.clinic_booking.repository.MedicineRepository;
import com.c1se62.clinic_booking.repository.PrescriptionRepository;
import com.c1se62.clinic_booking.service.SecurityServices.SecurityService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    
    private final MedicineRepository medicineRepository;
    private final SecurityService securityService;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    @Transactional
    public MedicineResponseDTO createMedicine(MedicineCreatedDTO medicineDTO) {
        securityService.validateAdminAccess();
        Medicine medicine = new Medicine();
        medicine.setName(medicineDTO.getName());
        medicine.setDescription(medicineDTO.getDescription());
        medicine.setStock(medicineDTO.getStock());
        medicine.setManufacturer(medicineDTO.getManufacturer());
        medicine.setUnitPrice(medicineDTO.getUnitPrice());
        medicine.setExpirationDate(medicineDTO.getExpirationDate());
        Medicine savedMedicine = medicineRepository.save(medicine);
        return mapToMedicineResponse(savedMedicine);
    }

    @Override
    public MedicineResponseDTO getMedicineById(Integer id) {
        Medicine medicine = getMedicineEntity(id);
        return mapToMedicineResponse(medicine);
    }

    @Override
    public Page<MedicineResponseDTO> getAllMedicines(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Medicine> medicines = medicineRepository.findAll(pageable);
        return medicines.map(this::mapToMedicineResponse);
    }

    @Override
    @Transactional
    public MedicineResponseDTO updateMedicine(Integer id, MedicineUpdatedDTO medicineDTO) {
        securityService.validateAdminAccess();
        Medicine existingMedicine = getMedicineEntity(id);
        if (medicineDTO.getName() != null) {
            existingMedicine.setName(medicineDTO.getName());
        }
        if (medicineDTO.getDescription() != null) {
            existingMedicine.setDescription(medicineDTO.getDescription());
        }
        if (medicineDTO.getStock() != null) {
            existingMedicine.setStock(medicineDTO.getStock());
        }
        if (medicineDTO.getManufacturer() != null) {
            existingMedicine.setManufacturer(medicineDTO.getManufacturer());
        }
        if (medicineDTO.getUnitPrice() != null) {
            existingMedicine.setUnitPrice(medicineDTO.getUnitPrice());
        }
        if (medicineDTO.getExpirationDate() != null) {
            existingMedicine.setExpirationDate(medicineDTO.getExpirationDate());
        }
        Medicine updatedMedicine = medicineRepository.save(existingMedicine);
        return mapToMedicineResponse(updatedMedicine);
    }

    @Override
    @Transactional
    public void deleteMedicine(Integer id) {
        securityService.validateAdminAccess();
        Medicine medicine = getMedicineEntity(id);
        // Check if medicine is referenced in any prescriptions
        List<Prescription> prescriptions = prescriptionRepository.findByMedicineMedicineId(id);
        if (!prescriptions.isEmpty()) {
            throw new APIException(
                HttpStatus.CONFLICT, 
                "Cannot delete medicine as it is referenced in prescriptions. Please remove related prescriptions first."
            );
        }
        medicineRepository.delete(medicine);
    }

    @Override
    @Transactional
    public MedicineResponseDTO updateMedicineStock(Integer id, Integer quantity) {
        securityService.validateAdminAccess();
        if (quantity < 0) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Stock quantity cannot be negative");
        }
        Medicine medicine = getMedicineEntity(id);
        medicine.setStock(quantity);
        Medicine updatedMedicine = medicineRepository.save(medicine);
        return mapToMedicineResponse(updatedMedicine);
    }

    @Override
    public List<MedicineResponseDTO> getMedicinesByIds(List<Integer> ids) {
        return medicineRepository.findByIds(ids).stream()
                .map(this::mapToMedicineResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicineResponseDTO> getLowStockMedicines(Integer threshold) {
        securityService.validateAdminAccess();
        return medicineRepository.findByStockLessThan(threshold).stream()
                .map(this::mapToMedicineResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicineResponseDTO> searchMedicinesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Search term cannot be empty");
        }
        return medicineRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToMedicineResponse)
                .collect(Collectors.toList());
    }

    private Medicine getMedicineEntity(Integer id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine", "Id", id));
    }

    private MedicineResponseDTO mapToMedicineResponse(Medicine medicine) {
        MedicineResponseDTO response = new MedicineResponseDTO();
        response.setMedicineId(medicine.getMedicineId());
        response.setName(medicine.getName());
        response.setDescription(medicine.getDescription());
        response.setStock(medicine.getStock());
        response.setManufacturer(medicine.getManufacturer());
        response.setUnitPrice(medicine.getUnitPrice());
        response.setExpirationDate(medicine.getExpirationDate());
        boolean isLowStock = medicine.getStock() < 10;
        response.setLowStock(isLowStock);
        return response;
    }
}
