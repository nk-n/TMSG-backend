package ku.cs.tmsg.security;


import ku.cs.tmsg.service.CustomUserDetailsService;
import ku.cs.tmsg.service.LineAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
    LineAuthenticationProvider lineAuthProvider;

    @Autowired
    private UnauthorizedEntryPointJwt unauthorizedHandler;


    @Bean
    public JwtAuthFilter authenticationJwtTokenFilter() {
        return new JwtAuthFilter();
    }

    @Bean
    public LineAuthFilter lineAuthFilter() {
        return new LineAuthFilter();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authenticationConfiguration
//    ) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(lineAuthProvider) // LINE
                .userDetailsService(userDetailsService)    // Web
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain lineChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/line/**")
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(lineAuthProvider)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.GET,"/api/line/**").hasAnyAuthority("ROLE_DRIVER")

                        // All other endpoints require authentication
                        .anyRequest().denyAll()
                );
        http.addFilterBefore(lineAuthFilter(),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                // Disable CSRF (not needed for stateless JWT)
                .csrf(csrf -> csrf.disable())
                .userDetailsService(userDetailsService)
                // Set session management to stateless
                .sessionManagement(sessionMnt ->                   sessionMnt.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exceptionHandling ->             exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
                )
                // Set permissions on endpoints
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // public endpoints
                                .requestMatchers("/api/auth/signin").permitAll()
//                                .requestMatchers("/api/metadata/**").permitAll()
                                .requestMatchers("/api/auth/line").permitAll()
                                .requestMatchers("/api/auth/link-line").permitAll()


                                // Role-based endpoints
                                .requestMatchers(HttpMethod.POST, "/api/auth/new-user").hasAnyAuthority("ROLE_ADMIN")

                                .requestMatchers(HttpMethod.GET,"/api/metadata/**").hasAnyAuthority("ROLE_USER")



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
