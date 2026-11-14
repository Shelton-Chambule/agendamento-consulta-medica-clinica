package com.clinica.agendamento_consulta_medica.mapper;

import com.clinica.agendamento_consulta_medica.dto.account.AccountResponse;
import com.clinica.agendamento_consulta_medica.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponse toResponse(Account account) {
        return new AccountResponse(account);
    }
}