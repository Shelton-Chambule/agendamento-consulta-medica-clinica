package com.clinica.agendamento_consulta_medica.configuration;
import com.clinica.agendamento_consulta_medica.entity.Account;
import com.clinica.agendamento_consulta_medica.entity.enums.AccountRole;
import com.clinica.agendamento_consulta_medica.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class CreateAdmin  implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private  final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if(!accountRepository.existsByLogin("admin@gmail.com")){
            Account admin = new Account();
            admin.setLogin("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setAccountRole(AccountRole.ADMIN);
            accountRepository.save(admin);
        }
    }
}
