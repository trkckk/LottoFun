package com.lottofun.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SimpleAuthFilter extends OncePerRequestFilter {

    private final SessionTokenService sessionTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Bearer token kontrolü
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " sonrasını al

            // Token geçerli değilse 401 dön
            if (!sessionTokenService.isValid(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Geçersiz token");
                return;
            }

            // Token geçerliyse kullanıcı ID'sini al
            Long userId = sessionTokenService.getUserIdFromToken(token);

            // İsteğe kullanıcı ID'sini attribute olarak koy (servislerde erişmek için)
            request.setAttribute("userId", userId);


            // Güvenlik bağlamına sahte authentication yerleştir
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, List.of());//List.of(() -> "ROLE_USER")
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } else {
            // Eğer yetkisiz erişim (register/login dışı) ve Authorization yoksa → 401
            String path = request.getRequestURI();
            if (!path.startsWith("/api/users") && !path.startsWith("/h2-console")
                    && !path.startsWith("/h2-console")
                    && !path.startsWith("/draw-websocket.html")
                    && !path.startsWith("/ws")
                    && !path.startsWith("/topic")
                    && !path.startsWith("/sockjs")
                    && !path.endsWith(".js")
                    && !path.endsWith(".css")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Authorization header eksik");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
