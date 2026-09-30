package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.patient.PatientRequest;
import com.clinica.agendamento_consulta_medica.dto.patient.PatientResponse;
import com.clinica.agendamento_consulta_medica.entity.Account;
import com.clinica.agendamento_consulta_medica.entity.Patient;
import com.clinica.agendamento_consulta_medica.entity.enums.AccountRole;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.repository.AccountRepository;
import com.clinica.agendamento_consulta_medica.repository.PatientRepository;
import com.clinica.agendamento_consulta_medica.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.exception.UniqueLoginException;
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
public class PatientService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public PatientService(PatientRepository patientRepository, PasswordEncoder passwordEncoder, AccountRepository accountRepository) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public PatientResponse registerPatient(PatientRequest patientRequestDTO) {

        Patient patient = new Patient();
        Account account = new Account();

        if (accountRepository.existsByLogin(account.getLogin()))
            throw new UniqueLoginException("This email already register!");

        account.setLogin(patientRequestDTO.getLogin());
        account.setPassword(passwordEncoder.encode(patientRequestDTO.getPassword()));
        account.setAccountRole(AccountRole.PATIENT);

        accountRepository.save(account);

        patient.setName(patientRequestDTO.getName());
        patient.setPhone(patientRequestDTO.getPhone());
        patient.setDataNascimento(patientRequestDTO.getDataNascimento());
        patient.setAccount(account);
        patientRepository.save(patient);
        return new PatientResponse(patient);

    }

    public List<PatientResponse> findAll() {
        List<Patient> patientList = patientRepository.findAll();
        return patientList.stream().map(PatientResponse::new).collect(Collectors.toList());
    }

    public PatientResponse findById(Long id, Authentication authentication) throws AccessDeniedException {

        Optional<Patient> patient = patientRepository.findById(id);

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        boolean user = patient.get().getAccount().getLogin().equals(authentication.getName());

        if (!admin && !user) throw new AccessDeniedException("Access denied");

        return new PatientResponse(patient.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public PatientResponse update(Long id, PatientRequest patient, Authentication authentication) throws AccessDeniedException {
        try {
            Patient patient1 = patientRepository.getReferenceById(id);
            boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

            boolean user = patient1.getAccount().getLogin().equals(authentication.getName());

            if (!admin && !user) throw new AccessDeniedException("Not authorization to this access");

            updateData(patient1, patient);
            patientRepository.save(patient1);
            return new PatientResponse(patient1);
        } catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Patient patient1, PatientRequest patient) {

        Account account = new Account();
        account.setAccountRole(AccountRole.PATIENT);
        account.setLogin(patient.getLogin());
        account.setPassword(passwordEncoder.encode(patient.getPassword()));
        patient1.setName(patient.getName());
        patient1.setPhone(patient.getPhone());
        patient1.setDataNascimento(patient.getDataNascimento());
        accountRepository.save(account);

    }


    public void deleteById(Long id, Authentication authentication) throws java.nio.file.AccessDeniedException {

        Patient patient = patientRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException(id));

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        boolean user = patient.getAccount().getLogin().equals(authentication.getName());

        if (!admin && !user) throw new AccessDeniedException(" You don,t  authorization to delete this patient");

        try {
            patientRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Not possible delete this patient");
        }
    }
}
