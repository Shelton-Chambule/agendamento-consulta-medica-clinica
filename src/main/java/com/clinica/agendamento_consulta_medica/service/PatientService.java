package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.patient.PatientRequest;
import com.clinica.agendamento_consulta_medica.dto.patient.PatientResponse;
import com.clinica.agendamento_consulta_medica.entity.Account;
import com.clinica.agendamento_consulta_medica.entity.Patient;
import com.clinica.agendamento_consulta_medica.entity.enums.AccountRole;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.exception.UniqueLoginException;
import com.clinica.agendamento_consulta_medica.mapper.PatientMapper;
import com.clinica.agendamento_consulta_medica.repository.AccountRepository;
import com.clinica.agendamento_consulta_medica.repository.PatientRepository;
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
public class PatientService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository, PasswordEncoder passwordEncoder,
                          AccountRepository accountRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.patientMapper = patientMapper;
    }

    @Transactional
    public PatientResponse registerPatient(PatientRequest request) {
        log.info("Registering patient with login: {}", request.getLogin());
        if (accountRepository.existsByLogin(request.getLogin())) {
            log.warn("Attempt to register patient with an already registered login: {}", request.getLogin());
            throw new UniqueLoginException("This login is already registered.");
        }

        Account account = new Account();
        account.setLogin(request.getLogin());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setAccountRole(AccountRole.PATIENT);

        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setPhone(request.getPhone());
        patient.setDataNascimento(request.getDataNascimento());
        patient.setAccount(account);

        Patient saved = patientRepository.save(patient);
        log.info("Patient registered successfully with id: {}", saved.getPatientId());
        return patientMapper.toResponse(saved);
    }

    public Page<PatientResponse> findAll(Pageable pageable) {
        log.debug("Fetching patients page: {}", pageable);
        Page<Patient> patients = patientRepository.findAll(pageable);
        return patientMapper.toResponsePage(patients);
    }

    public PatientResponse findById(Long id, Authentication authentication) {
        log.debug("Fetching patient with id: {}", id);
        Patient patient = patientRepository.findById(id).orElseThrow(() -> {
            log.warn("Patient not found with id: {}", id);
            return new ResourceNotFoundException(id);
        });
        ensureOwnerOrAdmin(patient, authentication);
        return patientMapper.toResponse(patient);
    }

    @Transactional
    public PatientResponse update(Long id, PatientRequest request, Authentication authentication) {
        log.info("Updating patient with id: {}", id);
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Patient not found with id: {}", id);
                    return new ResourceNotFoundException(id);
                });
        ensureOwnerOrAdmin(patient, authentication);
        updateData(patient, request);
        Patient saved = patientRepository.save(patient);
        log.info("Patient id {} updated successfully.", id);
        return patientMapper.toResponse(saved);
    }

    private void updateData(Patient patient, PatientRequest request) {
        Account account = patient.getAccount();
        if (accountRepository.existsByLoginAndIdNot(request.getLogin(), account.getId())) {
            log.warn("Attempt to update patient id {} with an already registered login: {}", patient.getPatientId(), request.getLogin());
            throw new UniqueLoginException("This login is already registered.");
        }

        account.setLogin(request.getLogin());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        patient.setName(request.getName());
        patient.setPhone(request.getPhone());
        patient.setDataNascimento(request.getDataNascimento());
    }

    @Transactional
    public void deleteById(Long id, Authentication authentication) {
        log.info("Deleting patient with id: {}", id);
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Patient not found with id: {}", id);
                    return new ResourceNotFoundException(id);
                });
        ensureOwnerOrAdmin(patient, authentication);

        try {
            patientRepository.delete(patient);
            patientRepository.flush();
            log.info("Patient id {} deleted successfully.", id);
        } catch (DataIntegrityViolationException exception) {
            log.error("Failed to delete patient id {} due to a data integrity violation.", id, exception);
            throw new DataBaseException("The patient cannot be deleted because it is referenced by other records.");
        }
    }

    private void ensureOwnerOrAdmin(Patient patient, Authentication authentication) {
        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean owner = patient.getAccount().getLogin().equals(authentication.getName());
        if (!admin && !owner) {
            log.warn("User '{}' denied access to patient id {}", authentication.getName(), patient.getPatientId());
            throw new AccessDeniedException("You do not have permission to access this patient.");
        }
    }
}