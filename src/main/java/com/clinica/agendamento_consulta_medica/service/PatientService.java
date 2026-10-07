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
import com.clinica.agendamento_consulta_medica.repository.AccountRepository;
import com.clinica.agendamento_consulta_medica.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    @Transactional
    public PatientResponse registerPatient(PatientRequest request) {
        if (accountRepository.existsByLogin(request.getLogin())) {
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

        return new PatientResponse(patientRepository.save(patient));
    }

    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream().map(PatientResponse::new).toList();
    }

    public PatientResponse findById(Long id, Authentication authentication) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        ensureOwnerOrAdmin(patient, authentication);
        return new PatientResponse(patient);
    }

    @Transactional
    public PatientResponse update(Long id, PatientRequest request, Authentication authentication) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        ensureOwnerOrAdmin(patient, authentication);
        updateData(patient, request);
        return new PatientResponse(patientRepository.save(patient));
    }

    private void updateData(Patient patient, PatientRequest request) {
        Account account = patient.getAccount();
        if (accountRepository.existsByLoginAndIdNot(request.getLogin(), account.getId())) {
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
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        ensureOwnerOrAdmin(patient, authentication);

        try {
            patientRepository.delete(patient);
            patientRepository.flush();
        } catch (DataIntegrityViolationException exception) {
            throw new DataBaseException("The patient cannot be deleted because it is referenced by other records.");
        }
    }

    private void ensureOwnerOrAdmin(Patient patient, Authentication authentication) {
        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean owner = patient.getAccount().getLogin().equals(authentication.getName());
        if (!admin && !owner) throw new AccessDeniedException("You do not have permission to access this patient.");
    }
}
