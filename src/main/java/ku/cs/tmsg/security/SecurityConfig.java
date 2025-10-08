package ku.cs.tmsg.security;


import ku.cs.tmsg.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {
    @Autowired
    CustomUserDetailsService userDetailsService;


    @Autowired
    private UnauthorizedEntryPointJwt unauthorizedHandler;


    @Bean
    public JwtAuthFilter authenticationJwtTokenFilter() {
        return new JwtAuthFilter();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (not needed for stateless JWT)
                .csrf(csrf -> csrf.disable())
                // Set session management to stateless
                .sessionManagement(sessionMnt ->                   sessionMnt.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exceptionHandling ->             exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
                )
                // Set permissions on endpoints
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // public endpoints
//                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/auth/signin").permitAll()
                                .requestMatchers("/api/metadata/**").permitAll()

                                // Role-based endpoints
                                .requestMatchers(HttpMethod.POST, "/api/auth/new-user").hasAnyAuthority("ROLE_ADMIN")



                                // All other endpoints require authentication
                                .anyRequest().authenticated()
                );

                http.addFilterBefore(authenticationJwtTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/h2-console/**");
    }
}
