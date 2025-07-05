package vn.com.mbbank.kanban.mbamt.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(
                        csrf -> csrf.disable()) // Disable CSRF for API calls (consider enabling for production with
                // proper token handling)
                .cors(cors -> cors.configurationSource(
                        corsConfigurationSource())) // Enable CORS with custom
                // configuration
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/**").permitAll()//
                                // Permit
                                // all requests to
                                // /change-template and /error
                                .anyRequest().authenticated()
                        // All other requests require authentication
                );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                Arrays.asList("http://localhost:5173")); // Allow frontend origin
        configuration.setAllowedMethods(
                Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // Allow
        // common
        // methods
        configuration.setAllowedHeaders(Arrays.asList("*")); // Allow all headers
        configuration.setAllowCredentials(
                true); // Allow credentials (e.g., cookies, authorization headers)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply CORS to all paths
        return source;
    }
}
