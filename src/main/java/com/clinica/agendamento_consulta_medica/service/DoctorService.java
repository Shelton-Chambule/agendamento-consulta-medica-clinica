package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorRequest;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorResponse;
import com.clinica.agendamento_consulta_medica.entities.Account;
import com.clinica.agendamento_consulta_medica.entities.Doctor;
import com.clinica.agendamento_consulta_medica.entities.enums.AccountRole;
import com.clinica.agendamento_consulta_medica.repository.AccountRepository;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import com.clinica.agendamento_consulta_medica.service.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final  DoctorRepository doctorRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public DoctorResponse registerDoctor(DoctorRequest doctorRequestDTO) {

        Doctor doctor = new Doctor();
        Account account = new Account();

        account.setLogin(doctorRequestDTO.getLogin());
        account.setPassword(passwordEncoder.encode(doctorRequestDTO.getPassword()));
        account.setAccountRole(AccountRole.DOCTOR);
        accountRepository.save(account);

        doctor.setName(doctorRequestDTO.getName());
        doctor.setPhone(doctorRequestDTO.getPhone());
        doctor.setAccount(account);
        doctorRepository.save(doctor);
        return new DoctorResponse(doctor);
    }

    public List<DoctorResponse> findAll() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream().map(DoctorResponse::new).collect(Collectors.toList());
    }

    public DoctorResponse findById(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        return new DoctorResponse(doctor.orElseThrow(() -> new ResourceNotFoundException(id)));
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

    public DoctorResponse update(Long id, DoctorRequest doctor) {
        try {
            Doctor doctor1 = doctorRepository.getReferenceById(id);
            updateDate(doctor1, doctor);
            doctorRepository.save(doctor1);
            return new DoctorResponse(doctor1);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateDate(Doctor doctor1, DoctorRequest doctor) {

        Account account = new Account();

        doctor1.setName(doctor.getName());
        doctor1.setPhone(doctor.getPhone());
        account.setLogin(doctor.getLogin());
        account.setPassword(passwordEncoder.encode(doctor.getPassword()));
        accountRepository.save(account);
    }
}
