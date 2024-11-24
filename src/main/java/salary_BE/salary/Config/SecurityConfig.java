package salary_BE.salary.Config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 설정
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Arrays.asList("http://localhost:8080")); // 정확한 Origin 설정
            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
            config.setAllowCredentials(true);
            config.setMaxAge(3600L);
            return config;
        }));

        // CSRF 비활성화
        http.csrf(csrf -> csrf.disable());

        // 기본 로그인 폼 비활성화
        http.formLogin(form -> form.disable());

        // HTTP 기본 인증 비활성화
        http.httpBasic(httpBasic -> httpBasic.disable());

        // 인증 및 권한 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/", "/join").permitAll() // 인증 없이 접근 가능한 경로
                .anyRequest().authenticated() // 그 외 요청은 인증 필요
        );

        // 세션 정책: Stateless
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }
}
