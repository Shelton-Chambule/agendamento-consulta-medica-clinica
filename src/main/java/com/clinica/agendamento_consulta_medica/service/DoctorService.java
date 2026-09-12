package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorRequestDTO;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorResponseDTO;
import com.clinica.agendamento_consulta_medica.entities.Doctor;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import com.clinica.agendamento_consulta_medica.service.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.service.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.service.exception.UniqueEmailException;
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

    public String  validateEmail(String email){
        if(doctorRepository.existsByEmail(email)){
            throw new UniqueEmailException("This email already register!");
        }
        return email;
    }

    public DoctorResponseDTO save(DoctorRequestDTO doctorRequestDTO) {
        Doctor doctor = new Doctor();
        doctor.setName(doctorRequestDTO.getName());
        doctor.setPhone(doctorRequestDTO.getPhone());
        doctor.setEmail(validateEmail(doctorRequestDTO.getEmail()));
        doctorRepository.save(doctor);
        return new DoctorResponseDTO(doctor);
    }

    public List<DoctorResponseDTO> findAll() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream().map(DoctorResponseDTO::new).collect(Collectors.toList());
    }

    public DoctorResponseDTO findById(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        return new DoctorResponseDTO(doctor.orElseThrow(() -> new ResourceNotFoundException(id)));
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

    public DoctorResponseDTO update(Long id, DoctorRequestDTO doctor) {
        try {
            Doctor doctor1 = doctorRepository.getReferenceById(id);
            updateDate(doctor1, doctor);
            doctorRepository.save(doctor1);
            return new DoctorResponseDTO(doctor1);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateDate(Doctor doctor1, DoctorRequestDTO doctor) {
        doctor1.setName(doctor.getName());
        doctor1.setEmail(doctor.getEmail());
        doctor1.setPhone(doctor.getPhone());
    }
}
