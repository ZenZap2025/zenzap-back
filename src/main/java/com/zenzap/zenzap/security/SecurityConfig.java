package com.zenzap.zenzap.security;

import com.zenzap.zenzap.service.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpSession;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOidcUserService customOidcUserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers( "/css/*", "/js/", "/images/", "/api/auth/", "/api/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/")
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(customOidcUserService)
                        )
                        .successHandler((request, response, authentication) -> {
                            // ✅ Lee de la sesión:
                            HttpSession session = request.getSession(false);
                            Long dbUserId = null;

                            if (session != null) {
                                dbUserId = (Long) session.getAttribute("dbUserId");
                            }
                            if (dbUserId == null) {
                                dbUserId = 0L; // fallback de ejemplo
                            }
                            System.out.println("id del usuariao enviado desde google" + dbUserId);
                            String redirectUrl = "http://localhost:5500/src/pagina_principal/pagina_principal_usuario.html?userId=" + dbUserId;
                            response.sendRedirect(redirectUrl);
                        })
                );
        return http.build();
    }
}
