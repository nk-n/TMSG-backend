package ku.cs.tmsg.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ku.cs.tmsg.service.LineAuthenticationToken;
import ku.cs.tmsg.service.LineUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class LineAuthFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtil jwtUtils;


    @Autowired
    private LineUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {


        try {
            String jwt = null;


            String path = request.getRequestURI();
            if (!path.startsWith("/api/line/")) {
                filterChain.doFilter(request, response);
                return;
            }


            // Get authorization header and validate
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer "))
                jwt = authHeader.substring(7);


            // Get jwt token and validate
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String lineID = jwtUtils.getUserIDFromToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(lineID);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                auth.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println("Cannot set line authentication: " + e);
        }
    }
}