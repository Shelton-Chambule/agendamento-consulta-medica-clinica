package com.clinica.agendamento_consulta_medica.entities;
import com.clinica.agendamento_consulta_medica.entities.enums.AccountRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Table(name = "tb_account")
@Entity
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;

    @OneToOne(mappedBy = "account")
    private Patient patient;

    @OneToOne(mappedBy = "account")
    private Doctor doctor;

    public Account(Long id,String login, String password, AccountRole accountRole) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.accountRole = accountRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (accountRole) {
            case PATIENT -> List.of(
                    new SimpleGrantedAuthority("ROLE_PATIENT")
            );
            case DOCTOR -> List.of(
                    new SimpleGrantedAuthority("ROLE_DOCTOR")
            );
            case ADMIN -> List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_DOCTOR"),
                    new SimpleGrantedAuthority("ROLE_PATIENT")
            );
        };
    }

    @Override
    public String getUsername() {
        return login;
    }
}
