package com.clinica.agendamento_consulta_medica.service;

import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorRequest;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorResponse;
import com.clinica.agendamento_consulta_medica.entity.Account;
import com.clinica.agendamento_consulta_medica.entity.Doctor;
import com.clinica.agendamento_consulta_medica.entity.enums.AccountRole;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.exception.UniqueLoginException;
import com.clinica.agendamento_consulta_medica.repository.AccountRepository;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepository, AccountRepository accountRepository,
                         PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public DoctorResponse registerDoctor(DoctorRequest request) {
        if (accountRepository.existsByLogin(request.getLogin())) {
            throw new UniqueLoginException("This login is already registered.");
        }

        Account account = new Account();
        account.setLogin(request.getLogin());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setAccountRole(AccountRole.DOCTOR);

        Doctor doctor = new Doctor();
        doctor.setName(request.getName());
        doctor.setPhone(request.getPhone());
        doctor.setAccount(account);

        return new DoctorResponse(doctorRepository.save(doctor));
    }

    public List<DoctorResponse> findAll() {
        return doctorRepository.findAll().stream().map(DoctorResponse::new).toList();
    }

    public DoctorResponse findById(Long id, Authentication authentication) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        ensureOwnerOrAdmin(doctor, authentication);
        return new DoctorResponse(doctor);
    }

    @Transactional
    public void deleteById(Long id, Authentication authentication) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        ensureOwnerOrAdmin(doctor, authentication);

        try {
            doctorRepository.delete(doctor);
            doctorRepository.flush();
        } catch (DataIntegrityViolationException exception) {
            throw new DataBaseException("The doctor cannot be deleted because it is referenced by other records.");
        }
    }

    @Transactional
    public DoctorResponse update(Long id, DoctorRequest request, Authentication authentication) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        ensureOwnerOrAdmin(doctor, authentication);
        updateData(doctor, request);
        return new DoctorResponse(doctorRepository.save(doctor));
    }

    private void updateData(Doctor doctor, DoctorRequest request) {
        Account account = doctor.getAccount();
        if (accountRepository.existsByLoginAndIdNot(request.getLogin(), account.getId())) {
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
            throw new AccessDeniedException("You do not have permission to access this doctor.");
        }
    }
}
