package com.medschedule.api_agendamento.controller;

import com.medschedule.api_agendamento.dto.RegisterDTO;
import com.medschedule.api_agendamento.infra.security.TokenService;
import com.medschedule.api_agendamento.model.Usuario;
import com.medschedule.api_agendamento.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Usuario usuario) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getPassword());
        var authentication = manager.authenticate(authenticationToken);
        var token = tokenService.generateToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data) {
        if(this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = passwordEncoder.encode(data.password());
        Usuario newUser = new Usuario(null, data.login(), encryptedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}