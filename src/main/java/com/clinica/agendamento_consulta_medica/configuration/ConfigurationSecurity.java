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
                        .requestMatchers(HttpMethod.POST,"/patients/save").permitAll()
                        .requestMatchers(HttpMethod.POST,"/authentications/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/authentications/register/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/doctors/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/patients/getAllPatient").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/consultations/save").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.PUT, "/consultations/{consultationId}").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.DELETE, "/consultations/{consultationId}").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/consultations/{consultationId}").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/patients/{patientId}").hasAnyRole("ADMIN","PATIENT")
                        .requestMatchers(HttpMethod.POST, "/consultations/{consultationId}/process").hasAnyRole("ADMIN","DOCTOR")
                        .requestMatchers(HttpMethod.PUT,"/patients/{patientId}").hasAnyRole("ADMIN","PATIENT")
                        .requestMatchers(HttpMethod.PUT,"/doctors/{doctorId}").hasAnyRole("ADMIN","DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/doctors/findAll").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/doctors/{doctorId}").hasAnyRole("ADMIN","DOCTOR")
                        .requestMatchers(HttpMethod.POST, "/specialtys/save").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/specialtys/getAll").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/specialtys/specialtyId").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/specialtys/specialtyId").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/specialtys/specialtyId").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/medicalschedules/save").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/medicalschedules/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/medicalschedules/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/medicalschedules/{id}").hasRole("ADMIN")
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
