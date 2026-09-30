package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorRequest;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorResponse;
import com.clinica.agendamento_consulta_medica.entity.Account;
import com.clinica.agendamento_consulta_medica.entity.Doctor;
import com.clinica.agendamento_consulta_medica.entity.enums.AccountRole;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.repository.AccountRepository;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import com.clinica.agendamento_consulta_medica.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
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

    public DoctorResponse findById(Long id, Authentication authentication) throws java.nio.file.AccessDeniedException {

        Optional<Doctor> doctor = doctorRepository.findById(id);

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        boolean user = doctor.get().getAccount().getLogin().equals(authentication.getName());

        if (!admin && !user) throw new java.nio.file.AccessDeniedException("Access Denied");

        return new DoctorResponse(doctor.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void deleteById(Long id, Authentication authentication) {

        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        boolean user = doctor.getAccount().getLogin().equals(authentication.getName());

        if (!admin && !user) throw new AccessDeniedException("You don,t  authorization to delete this doctor");

        try {
            doctorRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Not possible delete this doctor");
        }
    }

    public DoctorResponse update(Long id, DoctorRequest doctor, Authentication authentication) throws AccessDeniedException {
        try {

            Doctor doctor1 = doctorRepository.getReferenceById(id);

            boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

            boolean user = doctor1.getAccount().getLogin().equals(authentication.getName());

            if (!admin && !user) throw new AccessDeniedException("Not authorization to this access");

            updateDate(doctor1, doctor);
            doctorRepository.save(doctor1);
            return new DoctorResponse(doctor1);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateDate(Doctor doctor1, DoctorRequest doctor) {

        Account account = new Account();
        account.setAccountRole(AccountRole.DOCTOR);
        account.setLogin(doctor.getLogin());
        account.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctor1.setName(doctor.getName());
        doctor1.setPhone(doctor.getPhone());
        accountRepository.save(account);
    }
}
