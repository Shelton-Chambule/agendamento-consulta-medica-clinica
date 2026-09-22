package com.clinica.agendamento_consulta_medica.dto;
import com.clinica.agendamento_consulta_medica.entities.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AccountAdminResponse {

    private Long id;
    private String name;
    private String login;

    public AccountAdminResponse(Account account) {
        this.id = account.getId();
        this.login = account.getLogin();
    }
}
