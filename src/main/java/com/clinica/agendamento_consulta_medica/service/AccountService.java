package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.account.AccountAdminRequest;
import com.clinica.agendamento_consulta_medica.dto.account.AccountAdminResponse;
import com.clinica.agendamento_consulta_medica.entity.Account;
import com.clinica.agendamento_consulta_medica.entity.enums.AccountRole;
import com.clinica.agendamento_consulta_medica.repository.AccountRepository;
import com.clinica.agendamento_consulta_medica.exception.UniqueLoginException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.nio.file.AccessDeniedException;

@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByLogin(username);
    }

    public AccountAdminResponse createAccountAdmin(AccountAdminRequest request, Authentication authentication) throws AccessDeniedException {

        if (accountRepository.existsByLogin(request.getLogin()))
            throw new UniqueLoginException("Error this login is register");

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        if (!admin) throw new AccessDeniedException("Access denied");

        Account account = new Account();

        account.setLogin(request.getLogin());
        account.setAccountRole(AccountRole.ADMIN);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        accountRepository.save(account);
        return new AccountAdminResponse(account);

    }

}
