package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.entity.Account;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final AccountRepository accountRepository;

    public Account getCurrentUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated() ||  authentication instanceof AnonymousAuthenticationToken)
                throw new  AccessDeniedException("User not authenticated");

        String login  = extractLogin(authentication);

        return accountRepository.findByLogin(login).orElseThrow(() -> { return new ResourceNotFoundException(login);});

    }

    private String extractLogin(Authentication authentication){

        Object principal = authentication.getPrincipal();

        if (principal instanceof Account account) return account.getLogin();

        if (principal instanceof UserDetails userDetails) return userDetails.getUsername();

        if (principal instanceof String login) return  login;

        String name = authentication.getName();

        if (name == null || name.isBlank() || "anonymousUser".equals(name)) throw new AccessDeniedException("User not authenticated");

        return  name;
    }
}
