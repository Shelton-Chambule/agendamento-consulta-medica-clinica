package com.clinica.agendamento_consulta_medica.dto;
import com.clinica.agendamento_consulta_medica.entities.Account;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AccountAdminRequest {

    @NotBlank(message = "Required field")
    private String name;

    @NotBlank(message = "Required field")
    private String login;

    @NotBlank(message = "Required field")
    private String password;

    public AccountAdminRequest(Account account) {
        this.name = account.getName();
        this.login = account.getLogin();
        this.password = account.getPassword();
    }
}
