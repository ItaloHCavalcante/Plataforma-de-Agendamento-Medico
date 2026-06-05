package com.medschedule.api_agendamento.infra.security;

import com.medschedule.api_agendamento.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        
        // --- INÍCIO DOS LOGS DE DEBUG ---
        System.out.println("\n--- SecurityFilter DEBUG ---");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Token recuperado: " + (token != null ? "SIM" : "NÃO"));
        // --- FIM DOS LOGS DE DEBUG ---

        if (token != null) {
            var login = tokenService.validateToken(token); // Valida o token e retorna o 'sub' (login)
            
            // --- INÍCIO DOS LOGS DE DEBUG ---
            System.out.println("Login do token (sub): " + login);
            // --- FIM DOS LOGS DE DEBUG ---

            if(login != null && !login.isEmpty()) {
                // Carrega o UserDetails completo do banco de dados
                UserDetails user = usuarioRepository.findByLogin(login);
                
                // --- INÍCIO DOS LOGS DE DEBUG ---
                System.out.println("Usuário encontrado no DB para login '" + login + "': " + (user != null ? user.getUsername() : "NÃO ENCONTRADO"));
                if (user != null) {
                    System.out.println("Authorities do usuário do DB: " + user.getAuthorities());
                }
                // --- FIM DOS LOGS DE DEBUG ---

                if (user != null) {
                    // Cria o objeto de autenticação usando o UserDetails e suas authorities
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    // --- INÍCIO DOS LOGS DE DEBUG ---
                    System.out.println("Autenticação definida no SecurityContextHolder para: " + user.getUsername() + " com authorities: " + user.getAuthorities());
                    // --- FIM DOS LOGS DE DEBUG ---
                } else {
                    System.out.println("ERRO: UserDetails não encontrado para o login: " + login);
                }
            } else {
                System.out.println("ERRO: Login do token é nulo ou vazio.");
            }
        } else {
            System.out.println("Nenhum token de autorização encontrado no cabeçalho.");
        }
        System.out.println("--- FIM SecurityFilter DEBUG ---\n");
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
