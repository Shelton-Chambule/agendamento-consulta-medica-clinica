package com.clinica.agendamento_consulta_medica.dto.account;
import com.clinica.agendamento_consulta_medica.entity.Account;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AccountAdminRequest {

    @NotBlank
    @JsonProperty(required = true)
    @Email(message = "format email invalid")
    private String login;

    @NotBlank
    @JsonProperty(required = true)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$",
            message = "The password must contain an uppercase letter, a lowercase letter, a number, and a special character."
    )
    private String password;

    public AccountAdminRequest(Account account) {
        this.login = account.getLogin();
        this.password = account.getPassword();
    }
}
