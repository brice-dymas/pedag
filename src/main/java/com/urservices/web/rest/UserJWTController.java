package com.urservices.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.urservices.security.jwt.JWTFilter;
import com.urservices.security.jwt.TokenProvider;
import com.urservices.service.MailService;
import com.urservices.web.rest.vm.LoginVM;
import java.time.LocalDate;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;
    private final MailService mailService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserJWTController(
        TokenProvider tokenProvider,
        MailService mailService,
        AuthenticationManagerBuilder authenticationManagerBuilder
    ) {
        this.tokenProvider = tokenProvider;
        this.mailService = mailService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        //        mailService.sendEmail("briceguemkam@gmail.com", "Test de Jhipster", "Ceci est un test Jhipster", false, false);
        //        mailService.sendEmail("brice.guemkam@iforce5.com", "Test de Jhipster", "Ceci est un test Jhipster", false, false);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        //        if (LocalDate.now().isAfter(LocalDate.of(2021, 6, 26))) {
        //            authenticationToken = null;
        //        }
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
