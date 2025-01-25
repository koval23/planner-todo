package ru.javabegin.micro.planner.users.security;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.javabegin.micro.planner.utils.converter.KCRoleConverter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
// исключить авто конфигурация для подключения к БД
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KCRoleConverter());

        // все сетевые настройки
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("admin")
                        .requestMatchers("/auth/**").hasRole("user")
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)) // Добавляем конвертер
                );

        return http.build();
    }

//    для проверки роли каждый раз и чтоб роли не кешировались
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(); // Не кэширует роли
    }


//    @Bean
//    @SuppressWarnings("unchecked")
//    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
//        return (authorities) -> {
//            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//            authorities.forEach(authority -> {
//                if (authority instanceof OidcUserAuthority oidcUserAuthority) {
//                    OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();
//                    Map<String, Object> realmAccess = userInfo.getClaim("realm_access");
//                    Collection<String> realmRoles;
//                    if (realmAccess != null
//                            && (realmRoles = (Collection<String>) realmAccess.get("roles")) != null) {
//                        realmRoles
//                                .forEach(role -> mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
//                    }
//                }
//            });
//            return mappedAuthorities;
//        };
//    }

}
