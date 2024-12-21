package org.roadmap.tasktrackerbackend.security.filter;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.model.User;
import org.roadmap.tasktrackerbackend.service.JwtService;
import org.roadmap.tasktrackerbackend.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAccessFilter extends OncePerRequestFilter {

    private static final String PREFIX = "Bearer ";

    private final JwtService jwtService;

    private final UserService userService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        if (uri.equals("/user") && method.equals("POST")) {
            return true;
        }
        return uri.equals("/auth/login");
    }

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith(PREFIX)) {
            String token = header.substring(PREFIX.length());
            authorize(request, token);
        }
        filterChain.doFilter(request, response);
    }

    private void authorize(HttpServletRequest request, String token) {
        User user = userService.getUserByUsernameAndPassword(
                jwtService.extractEmail(token),
                jwtService.extractPassword(token));
        var authToken = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
    }
}
