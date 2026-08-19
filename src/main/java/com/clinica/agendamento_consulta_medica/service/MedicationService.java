package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.medication.MedicationDto;
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

    public MedicationDto save( MedicationDto medicationDto){
        Medications medications = new Medications();
        medications.setName(medicationDto.getName());
        medicationRepository.save(medications);
        return new MedicationDto(medications);
    }

    public List<MedicationDto> findAll(){
        List<Medications> medications = medicationRepository.findAll();
        return medications.stream().map(MedicationDto::new).collect(Collectors.toList());
    }

    public MedicationDto findById(Long id){
        Optional<Medications> medications = medicationRepository.findById(id);
        return new MedicationDto(medications.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void  deleteById(Long id){
        if(!medicationRepository.existsById(id)){
            throw new ResourceNotFoundException(id);
        }
        medicationRepository.deleteById(id);
    }

    public MedicationDto update(MedicationDto medicationDto, Long id){
        try{
            Medications medications =  medicationRepository.getReferenceById(id);
            updateData(medications, medicationDto);
            medicationRepository.save(medications);
            return new MedicationDto(medications);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Medications medications, MedicationDto medicationDto) {
        medications.setName(medicationDto.getName());
    }
}
