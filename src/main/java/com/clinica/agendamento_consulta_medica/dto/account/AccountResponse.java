package com.clinica.agendamento_consulta_medica.dto.account;
import com.clinica.agendamento_consulta_medica.entity.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AccountResponse {

    private Long id;
    private String login;

    public AccountResponse(Account account) {
        this.id = account.getId();
        this.login = account.getLogin();
    }
}
