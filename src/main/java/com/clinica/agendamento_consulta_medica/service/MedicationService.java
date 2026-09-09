package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.medication.MedicationRequestDTO;
import com.clinica.agendamento_consulta_medica.entities.Medications;
import com.clinica.agendamento_consulta_medica.repository.MedicationRepository;
import com.clinica.agendamento_consulta_medica.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicationService {

    private  final  MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public MedicationRequestDTO save(MedicationRequestDTO medicationDto){
        Medications medications = new Medications();
        medications.setName(medicationDto.getName());
        medicationRepository.save(medications);
        return new MedicationRequestDTO(medications);
    }

    public List<MedicationRequestDTO> findAll(){
        List<Medications> medications = medicationRepository.findAll();
        return medications.stream().map(MedicationRequestDTO::new).collect(Collectors.toList());
    }

    public MedicationRequestDTO findById(Long id){
        Optional<Medications> medications = medicationRepository.findById(id);
        return new MedicationRequestDTO(medications.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void  deleteById(Long id){
        if(!medicationRepository.existsById(id)){
            throw new ResourceNotFoundException(id);
        }
        medicationRepository.deleteById(id);
    }

    public MedicationRequestDTO update(MedicationRequestDTO medicationDto, Long id){
        try{
            Medications medications =  medicationRepository.getReferenceById(id);
            updateData(medications, medicationDto);
            medicationRepository.save(medications);
            return new MedicationRequestDTO(medications);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Medications medications, MedicationRequestDTO medicationDto) {
        medications.setName(medicationDto.getName());
    }
}
