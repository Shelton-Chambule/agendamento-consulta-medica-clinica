package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.AccountAdminRequest;
import com.clinica.agendamento_consulta_medica.dto.AccountAdminResponse;
import com.clinica.agendamento_consulta_medica.dto.token.TokenResponse;
import com.clinica.agendamento_consulta_medica.entities.Account;
import com.clinica.agendamento_consulta_medica.service.AccountService;
import com.clinica.agendamento_consulta_medica.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AccountAuthenticationController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;

    public AccountAuthenticationController(TokenService tokenService, AuthenticationManager authenticationManager, AccountService accountService) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login (@Valid @RequestBody Account account){

        var authentication = authenticationManager.authenticate(new  UsernamePasswordAuthenticationToken(account.getLogin(), account.getPassword()));
        var token = tokenService.generatedToken( (Account) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/register/admin")
    public  ResponseEntity<AccountAdminResponse> createAccountAdmin( @Valid @RequestBody  AccountAdminRequest request){
        AccountAdminResponse account = accountService.createAccountAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

}
