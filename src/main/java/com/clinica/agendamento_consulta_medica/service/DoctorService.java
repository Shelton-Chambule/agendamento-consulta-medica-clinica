package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorDto;
import com.clinica.agendamento_consulta_medica.entities.Doctor;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import com.clinica.agendamento_consulta_medica.service.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final  DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public DoctorDto save(DoctorDto doctor) {
        Doctor doctor1 = new Doctor();
        doctor1.setDoctorId(doctor.getId());
        doctor1.setName(doctor.getName());
        doctor1.setPhone(doctor.getPhone());
        doctor1.setEmail(doctor.getEmail());
        doctorRepository.save(doctor1);
        return new DoctorDto(doctor1);
    }

    public List<DoctorDto> findAll() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream().map(DoctorDto::new).collect(Collectors.toList());
    }

    public DoctorDto findById(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        return new DoctorDto(doctor.orElseThrow(() -> new ResourceNotFoundException(id)));
    }


    public void deleteById(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        try {
            doctorRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    public DoctorDto update(Long id, DoctorDto doctor) {
        try {
            Doctor doctor1 = doctorRepository.getReferenceById(id);
            updateDate(doctor1, doctor);
            doctorRepository.save(doctor1);
            return new DoctorDto(doctor1);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateDate(Doctor doctor1, DoctorDto doctor) {
        doctor1.setName(doctor.getName());
        doctor1.setEmail(doctor.getEmail());
        doctor1.setPhone(doctor.getPhone());
    }
}
