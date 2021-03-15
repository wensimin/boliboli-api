package tech.shali.boliboliapi.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
class ResourceServerConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .authorizeRequests().apply {
                antMatchers("/admin/**").hasAuthority(Auth.ADMIN.name)
                anyRequest().authenticated()
            }.and()
            .oauth2ResourceServer()
            .jwt()
        return http.build()
    }

}