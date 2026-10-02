package com.clinica.agendamento_consulta_medica.service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.clinica.agendamento_consulta_medica.entity.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    @Value("${api.agendamento.clinico}")
    private String secret;

    public String generatedToken(Account account){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("clinica.agendamento")
                    .withSubject(account.getLogin())
                    .withExpiresAt(expireToken())
                    .sign(algorithm);
            return token;
        }catch (JWTCreationException exception){
            throw new IllegalStateException("Unable to generate the authentication token.", exception);
        }
    }

    public  String validateToken(String token){

        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("clinica.agendamento")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException exception){
            return null;
        }
    }

    public Instant expireToken(){
        return Instant.now().plus(2, ChronoUnit.HOURS);
    }
}
