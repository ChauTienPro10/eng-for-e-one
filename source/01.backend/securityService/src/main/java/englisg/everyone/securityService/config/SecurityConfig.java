package englisg.everyone.securityService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_GET_APIS = {
            "/actuator/health",
            "/api/v1/public/ping",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    private static final String[] PUBLIC_POST_APIS = {
            "/keycloak-api/login",
            "/keycloak-api/register",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF cho API Stateless
                .authorizeHttpRequests(authorize -> authorize

                        // 1. Nạp các API Public dùng phương thức GET
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_APIS).permitAll()

                        // 2. Nạp các API Public dùng phương thức POST
                        .requestMatchers(HttpMethod.POST, PUBLIC_POST_APIS).permitAll()

                        // 3. Bảo mật cho tất cả các API còn lại
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtNavigationConverter()))
        );
        return http.build();
    }

    // Bộ chuyển đổi giúp biến các Role trong cấu trúc Keycloak thành GrantedAuthority của Spring
    private Converter<Jwt, AbstractAuthenticationToken> jwtNavigationConverter() {
        return jwt -> {
            Collection<GrantedAuthority> authorities = extractRoles(jwt);
            // Mặc định Spring dùng claim 'sub' làm Name, bạn có thể đổi thành 'preferred_username'
            String principalClaimName = jwt.getClaimAsString("preferred_username");
            return new JwtAuthenticationToken(jwt, authorities, principalClaimName);
        };
    }

    // Logic bóc tách "realm_access.roles" từ JSON của Keycloak JWT Token
    private Collection<GrantedAuthority> extractRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess == null || !realmAccess.containsKey("roles")) {
            return Collections.emptyList();
        }

        Collection<?> roles = (Collection<?>) realmAccess.get("roles");
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase())) // Thêm tiền tố ROLE_ theo chuẩn Spring
                .collect(Collectors.toList());
    }
}