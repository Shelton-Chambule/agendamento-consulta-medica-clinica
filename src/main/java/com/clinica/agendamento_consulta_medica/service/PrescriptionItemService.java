package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.prescriptionItem.PrescriptionItemDto;
import com.clinica.agendamento_consulta_medica.entities.PrescriptionItem;
import com.clinica.agendamento_consulta_medica.repository.PrescriptionItemRepository;
import com.clinica.agendamento_consulta_medica.service.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.service.exception.ResourceNotFoundException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrescriptionItemService {


    private final PrescriptionItemRepository prescriptionItemRepository;

    public PrescriptionItemService(PrescriptionItemRepository prescriptionItemRepository) {
        this.prescriptionItemRepository = prescriptionItemRepository;
    }

    public PrescriptionItemDto save(PrescriptionItemDto prescriptionItemDto){
        PrescriptionItem p = new PrescriptionItem();
        p.setDosagem(prescriptionItemDto.getDosagem());
        p.setFrequency(prescriptionItemDto.getFrequency());
        prescriptionItemRepository.save(p);
        return new PrescriptionItemDto(p);
    }

    public List<PrescriptionItemDto> findAll(){
        List<PrescriptionItem> prescriptionItem = prescriptionItemRepository.findAll();
        return prescriptionItem.stream().map(PrescriptionItemDto ::new).collect(Collectors.toList());
    }

    public PrescriptionItemDto findById(Long id){
        Optional<PrescriptionItem> prescriptionItem = prescriptionItemRepository.findById(id);
        return new PrescriptionItemDto(prescriptionItem.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void deleteById (Long id) {
        if (!prescriptionItemRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }

        try {
            prescriptionItemRepository.deleteById(id);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new DataBaseException(e.getMessage());
        }
    }
}
