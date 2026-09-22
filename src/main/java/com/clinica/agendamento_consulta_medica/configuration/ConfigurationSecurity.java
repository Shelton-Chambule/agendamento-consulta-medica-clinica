package com.clinica.agendamento_consulta_medica.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ConfigurationSecurity{

    private final SecurityFilter securityFilter;

    public ConfigurationSecurity(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        return httpSecurity.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"/patient/save/patient").permitAll()
                        .requestMatchers(HttpMethod.POST,"/authentication/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/authentication/register/admin").permitAll()
                        .requestMatchers(HttpMethod.POST,"/doctor/save/doctor").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/patient/findAllPatient").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/consultation/save/consultation").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.PUT, "/consultation/{consultationId}").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.DELETE, "/consultation/{consultationId}").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/consultation/{consultationId}").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/patient/{patientId}").hasAnyRole("ADMIN","PATIENT")
                        .requestMatchers(HttpMethod.POST, "/consultation/{consultationId}/process").hasAnyRole("ADMIN","DOCTOR")
                        .requestMatchers(HttpMethod.PUT,"/patient/{patientId}").hasAnyRole("ADMIN","PATIENT")
                        .requestMatchers(HttpMethod.PUT,"/doctor/{doctorId}").hasAnyRole("ADMIN","DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/doctor/findAll").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/doctor/{doctorId}").hasRole("ADMIN")
                        .anyRequest().authenticated()).
                addFilterBefore(securityFilter,  UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
