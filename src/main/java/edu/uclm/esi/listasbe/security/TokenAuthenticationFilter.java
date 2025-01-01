package edu.uclm.esi.listasbe.security;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import edu.uclm.esi.listasbe.services.ProxyDEU;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private ProxyDEU proxyDEU;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Obtener el token desde las cookies
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // Validar el token y establecer atributos en la solicitud
        if (token != null) {
            Map<String, Object> resultado = proxyDEU.validarTokenYObtenerInfo(token);
            if (Boolean.TRUE.equals(resultado.get("isValid"))) {
                request.setAttribute("userEmail", resultado.get("email"));
                request.setAttribute("isPremium", resultado.get("isPremium"));
            }
        }

        filterChain.doFilter(request, response);
    }
}
