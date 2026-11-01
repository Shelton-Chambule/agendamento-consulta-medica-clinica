package com.clinica.agendamento_consulta_medica.mapper;

import com.clinica.agendamento_consulta_medica.dto.account.AccountAdminResponse;
import com.clinica.agendamento_consulta_medica.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountAdminResponse toResponse(Account account) {
        return new AccountAdminResponse(account);
    }
}