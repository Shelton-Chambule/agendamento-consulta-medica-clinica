package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorRequest;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorResponse;
import com.clinica.agendamento_consulta_medica.entity.Account;
import com.clinica.agendamento_consulta_medica.entity.Doctor;
import com.clinica.agendamento_consulta_medica.entity.Specialty;
import com.clinica.agendamento_consulta_medica.entity.enums.AccountRole;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.exception.UniqueLoginException;
import com.clinica.agendamento_consulta_medica.mapper.DoctorMapper;
import com.clinica.agendamento_consulta_medica.repository.AccountRepository;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import com.clinica.agendamento_consulta_medica.repository.SpecialtyRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final DoctorMapper doctorMapper;
    private final SpecialtyRepository specialtyRepository;

    public DoctorService(DoctorRepository doctorRepository, AccountRepository accountRepository,
                         PasswordEncoder passwordEncoder, DoctorMapper doctorMapper,
                         SpecialtyRepository specialtyRepository) {
        this.doctorRepository = doctorRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.doctorMapper = doctorMapper;
        this.specialtyRepository = specialtyRepository;
    }

    @Transactional
    public DoctorResponse registerDoctor(DoctorRequest request) {
        log.info("Registering doctor with login: {}", request.getLogin());
        if (accountRepository.existsByLogin(request.getLogin())) {
            log.warn("Attempt to register doctor with an already registered login: {}", request.getLogin());
            throw new UniqueLoginException("This login is already registered.");
        }

        if (request.getNameSpecialty() == null || request.getNameSpecialty().isBlank()) {
            log.warn("Attempt to register doctor '{}' without a specialty name.", request.getLogin());
            throw new IllegalArgumentException("Doctor specialty name must be provided.");
        }

        Account account = new Account();
        account.setLogin(request.getLogin());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setAccountRole(AccountRole.DOCTOR);

        Doctor doctor = new Doctor();
        doctor.setName(request.getName());
        doctor.setPhone(request.getPhone());
        doctor.setAccount(account);

        Doctor saved = doctorRepository.save(doctor);
        log.info("Doctor registered successfully with id: {}", saved.getDoctorId());

        Specialty specialty = new Specialty();
        specialty.setName(request.getNameSpecialty());
        specialty.setDoctors(saved);
        specialtyRepository.save(specialty);
        log.info("Specialty '{}' saved for doctor id: {}", request.getNameSpecialty(), saved.getDoctorId());

        return doctorMapper.toResponse(saved);
    }

    public Page<DoctorResponse> findAll(Pageable pageable) {
        log.debug("Fetching doctors page: {}", pageable);
        Page<Doctor> doctors = doctorRepository.findAll(pageable);
        return doctorMapper.toResponsePage(doctors);
    }

    public DoctorResponse findById(Long id, Authentication authentication) {
        log.debug("Fetching doctor with id: {}", id);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Doctor not found with id: {}", id);
                    return new ResourceNotFoundException(id);
                });
        ensureOwnerOrAdmin(doctor, authentication);
        return doctorMapper.toResponse(doctor);
    }

    @Transactional
    public void deleteById(Long id, Authentication authentication) {
        log.info("Deleting doctor with id: {}", id);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Doctor not found with id: {}", id);
                    return new ResourceNotFoundException(id);
                });
        ensureOwnerOrAdmin(doctor, authentication);

        try {
            doctorRepository.delete(doctor);
            doctorRepository.flush();
            log.info("Doctor id {} deleted successfully.", id);
        } catch (DataIntegrityViolationException exception) {
            log.error("Failed to delete doctor id {} due to a data integrity violation.", id, exception);
            throw new DataBaseException("The doctor cannot be deleted because it is referenced by other records.");
        }
    }

    @Transactional
    public DoctorResponse update(Long id, DoctorRequest request, Authentication authentication) {
        log.info("Updating doctor with id: {}", id);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Doctor not found with id: {}", id);
                    return new ResourceNotFoundException(id);
                });
        ensureOwnerOrAdmin(doctor, authentication);
        updateData(doctor, request);
        Doctor saved = doctorRepository.save(doctor);
        log.info("Doctor id {} updated successfully.", id);
        return doctorMapper.toResponse(saved);
    }

    private void updateData(Doctor doctor, DoctorRequest request) {
        Account account = doctor.getAccount();
        if (accountRepository.existsByLoginAndIdNot(request.getLogin(), account.getId())) {
            log.warn("Attempt to update doctor id {} with an already registered login: {}", doctor.getDoctorId(), request.getLogin());
            throw new UniqueLoginException("This login is already registered.");
        }

        account.setLogin(request.getLogin());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        doctor.setName(request.getName());
        doctor.setPhone(request.getPhone());
    }

    private void ensureOwnerOrAdmin(Doctor doctor, Authentication authentication) {
        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean owner = doctor.getAccount().getLogin().equals(authentication.getName());
        if (!admin && !owner) {
            log.warn("User '{}' denied access to doctor id {}", authentication.getName(), doctor.getDoctorId());
            throw new AccessDeniedException("You do not have permission to access this doctor.");
        }
    }
}