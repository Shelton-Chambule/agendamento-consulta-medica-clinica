package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.account.AccountRequest;
import com.clinica.agendamento_consulta_medica.dto.account.AccountResponse;
import com.clinica.agendamento_consulta_medica.entity.Account;
import com.clinica.agendamento_consulta_medica.entity.enums.AccountRole;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.mapper.AccountMapper;
import com.clinica.agendamento_consulta_medica.repository.AccountRepository;
import com.clinica.agendamento_consulta_medica.exception.UniqueLoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;
    private final CurrentUserService currentUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading account by username: {}", username);
        return accountRepository.findByLogin(username)
                .orElseThrow(() -> {
                    log.warn("Account not found for username: {}", username);
                    return new UsernameNotFoundException("Account not found.");
                });
    }

    @Transactional
    public AccountResponse createAccountAdmin(AccountRequest request, Authentication authentication) {
        log.info("Creating admin account with login: {}", request.getLogin());

        if (accountRepository.existsByLogin(request.getLogin())) {
            log.warn("Attempt to create admin account with an already registered login: {}", request.getLogin());
            throw new UniqueLoginException("This login is already registered.");
        }

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        if (!admin) {
            log.warn("User '{}' tried to create an admin account without permission.", authentication.getName());
            throw new AccessDeniedException("You do not have permission to create administrator accounts.");
        }

        Account account = new Account();

        account.setLogin(request.getLogin());
        account.setAccountRole(AccountRole.ADMIN);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        accountRepository.save(account);
        log.info("Admin account created successfully with login: {}", request.getLogin());
        return accountMapper.toResponse(account);

    }

    @Transactional
    public AccountResponse updateAccount(Long accountId,AccountRequest accountRequest){
        log.info("Updating account with id: {}", accountId);

        accountRepository.findById(accountId).orElseThrow(() ->{
            log.warn("Account  not found with id: {}",accountId);
            return    new ResourceNotFoundException(accountId);
        });

        Account account = currentUserService.getCurrentUser();

        account.setAccountRole(AccountRole.ADMIN);
        account.setLogin(accountRequest.getLogin());
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        Account saveAccount = accountRepository.save(account);
        log.info("Account id {} updated successfully.", accountId);
        return accountMapper.toResponse(saveAccount);
    }
}