package com.clinica.agendamento_consulta_medica.configuration;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ConfigurationSecurity{

    private final SecurityFilter securityFilter;
    private final SecurityExceptionHandler securityExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(securityExceptionHandler)
                        .accessDeniedHandler(securityExceptionHandler))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"/patients/save").permitAll()
                        .requestMatchers(HttpMethod.POST,"/authentications/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/authentications/register/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/doctors/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/patients/getAllPatient").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/doctors/getAll").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/consultations/getAll").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/specialtys/save").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/specialtys/{specialtyId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/specialtys/{specialtyId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/medicalschedules/save").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/medicalschedules/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/medicalschedules/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/historyConsultation/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/historyConsultation/**").denyAll()
                        .requestMatchers(HttpMethod.POST, "/consultations/save").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.PUT, "/consultations/{consultationId}").hasAnyRole("ADMIN", "PATIENT")
                        .requestMatchers(HttpMethod.DELETE, "/consultations/{consultationId}").hasAnyRole("ADMIN", "PATIENT")
                        .requestMatchers(HttpMethod.GET, "/consultations/{consultationId}").hasAnyRole("ADMIN", "PATIENT", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/patients/{patientId}").hasAnyRole("ADMIN","PATIENT")
                        .requestMatchers(HttpMethod.DELETE, "/patients/{patientId}").hasAnyRole("ADMIN", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "/consultations/{consultationId}/process").hasAnyRole("ADMIN","DOCTOR")
                        .requestMatchers(HttpMethod.PUT,"/patients/{patientId}").hasAnyRole("ADMIN","PATIENT")
                        .requestMatchers(HttpMethod.PUT,"/doctors/{doctorId}").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.DELETE,"/doctors/{doctorId}").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/doctors/{doctorId}").hasAnyRole("ADMIN","DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/specialtys/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/medicalschedules/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/prescriptions/create").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/prescriptions/getAll").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/prescriptions/{id}").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/prescriptions/{id}").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/prescriptions/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .anyRequest().denyAll()).
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
