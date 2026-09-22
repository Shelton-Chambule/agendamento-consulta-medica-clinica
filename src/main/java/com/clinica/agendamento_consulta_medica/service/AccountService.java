package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.AccountAdminRequest;
import com.clinica.agendamento_consulta_medica.dto.AccountAdminResponse;
import com.clinica.agendamento_consulta_medica.entities.Account;
import com.clinica.agendamento_consulta_medica.entities.enums.AccountRole;
import com.clinica.agendamento_consulta_medica.repository.AccountRepository;
import com.clinica.agendamento_consulta_medica.service.exception.UniqueLoginException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public AccountAdminResponse createAccountAdmin(AccountAdminRequest request){

        if(accountRepository.existsByLogin(request.getLogin())) throw new UniqueLoginException("Error this login is register");

        Account account = new Account();
        account.setName(request.getName());
        account.setLogin(request.getLogin());
        account.setAccountRole(AccountRole.ADMIN);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        accountRepository.save(account);
        return new  AccountAdminResponse(account);

    }

}
