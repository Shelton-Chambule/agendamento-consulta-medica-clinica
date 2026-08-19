package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.specialty.SpecialtyDto;
import com.clinica.agendamento_consulta_medica.entities.Specialty;
import com.clinica.agendamento_consulta_medica.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpecialtyService {

    private  final SpecialtyRepository especialtyRepository;

    public SpecialtyService(SpecialtyRepository especialtyRepository) {
        this.especialtyRepository = especialtyRepository;
    }

    public SpecialtyDto save (SpecialtyDto especialty){
        Specialty specialty = new Specialty();
        specialty.setId(especialty.getId());
        specialty.setName(especialty.getName());
        specialty.setPrice(especialty.getPrice());
        especialtyRepository.save(specialty);
        return new SpecialtyDto(specialty);
    }

    public List<SpecialtyDto> findAll(){
        List<Specialty> specialty =  especialtyRepository.findAll();
        return specialty.stream().map(SpecialtyDto::new).collect(Collectors.toList());
    }

    public SpecialtyDto findById(Long id){
        Optional<Specialty> especialty = especialtyRepository.findById(id);
        return new SpecialtyDto(especialty.get());
    }

    public void deleteById(Long id){
        especialtyRepository.deleteById(id);
    }


    public SpecialtyDto update(Long id, SpecialtyDto especialty){
        Specialty especialty1 = especialtyRepository.getReferenceById(id);
        updateData(especialty1,especialty);
         especialtyRepository.save(especialty1);
         return new SpecialtyDto(especialty1);
    }

    private void updateData(Specialty especialty1, SpecialtyDto especialty) {
            especialty1.setName(especialty.getName());
            especialty1.setPrice(especialty.getPrice());

    }
}
